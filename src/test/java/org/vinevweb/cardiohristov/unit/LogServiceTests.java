package org.vinevweb.cardiohristov.unit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.vinevweb.cardiohristov.domain.entities.Log;
import org.vinevweb.cardiohristov.domain.models.service.LogServiceModel;
import org.vinevweb.cardiohristov.repositories.LogRepository;
import org.vinevweb.cardiohristov.services.LogServiceImpl;
import static org.vinevweb.cardiohristov.Constants.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@DataJpaTest
@ActiveProfiles("test")
public class LogServiceTests {

    private static final String DATETIME = "2019-04-09T13:47:23.542358";
    private static final String DATETIME_EXPECTED = "2019-04-09T13:47:23";
    private static final String USERNAME = "roko@abv.bg";
    private static final String ARTICLE_CREATE_MSG = "Създадена е статия със заглавие: Title";


    @InjectMocks
    private LogServiceImpl logService;

    @Mock
    private LogRepository logRepository;

    @Mock
    private ModelMapper modelMapper;


    @Test
    public void addEventCreatesEventAndSaveItToDB() {

        String[] eventParams = new String[]{DATETIME, USERNAME, ARTICLE_CREATE_MSG};

        this.logService.addEvent(eventParams);

        ArgumentCaptor<Log> argument = ArgumentCaptor.forClass(Log.class);
        verify(logRepository).save(argument.capture());
        assertEquals(USERNAME, argument.getValue().getUser());
        assertEquals(LocalDateTime.parse(DATETIME_EXPECTED), argument.getValue().getDateTime());
        assertEquals(ARTICLE_CREATE_MSG, argument.getValue().getEvent());


    }

    @Test
    public void getLogsOrderByDateInvokeCorrectRepoMethod() {

        Log logg = new Log();
        List<Log> logs = new ArrayList<>();
        logs.add(logg);

        Mockito.when(this.logRepository.findAllByOrderByDateTimeDesc()).thenReturn(logs);
        List<LogServiceModel> result = this.logService.getLogsOrderedByDate();

        verify(logRepository).findAllByOrderByDateTimeDesc();
        assertEquals(logs.stream()
                .map(log -> this.modelMapper.map(log, LogServiceModel.class))
                .collect(Collectors.toList()), result);

    }

}
