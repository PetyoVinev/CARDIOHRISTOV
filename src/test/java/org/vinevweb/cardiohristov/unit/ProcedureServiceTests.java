package org.vinevweb.cardiohristov.unit;

import org.junit.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.vinevweb.cardiohristov.domain.entities.Procedure;
import org.vinevweb.cardiohristov.domain.entities.User;
import org.vinevweb.cardiohristov.domain.models.service.ProcedureServiceModel;
import org.vinevweb.cardiohristov.repositories.ProcedureRepository;
import org.vinevweb.cardiohristov.services.LogServiceImpl;
import org.vinevweb.cardiohristov.services.ProcedureServiceImpl;

import java.util.HashSet;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.vinevweb.cardiohristov.Constants.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@DataJpaTest
@ActiveProfiles("test")
public class ProcedureServiceTests {


    private static final String USERNAME = "boko@abv.bg";
    private static final String PROCEDURE_TITLE = "Procedure Title";
    private static final String PROCEDURE_ID = "1234";
    private static final String NAME = "Name";

    @InjectMocks
    private ProcedureServiceImpl procedureService;

    @Mock
    private LogServiceImpl logService;

    @Mock
    private ProcedureRepository procedureRepository;

    @Mock
    private ModelMapper modelMapper;


    @Test
    public void createProcedureCreatesItInDbAndCreateLog() {

        User user = new User();
        user.setUsername(USERNAME);

        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext secCont = Mockito.mock(SecurityContext.class);
        Mockito.when(secCont.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(secCont);
        Mockito.when(auth.getPrincipal()).thenReturn(user);

        ProcedureServiceModel procedureServiceModel = new ProcedureServiceModel();

        Procedure procedure = new Procedure();
        procedure.setName(PROCEDURE_TITLE);

        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(procedure);

        this.procedureService.createProcedure(procedureServiceModel);

        verify(procedureRepository)
                .save(procedure);
        verify(logService)
                .addEvent(any());

    }

    @Test
    public void getAllByDateAscInvokeRepository() {
        this.procedureService.getAllByDateAsc();
        verify(procedureRepository)
                .findAllByOrderByDateAsc();

    }

    @Test
    public void findByIdReturnsCorrectEntity() {
        ProcedureServiceModel procedureServiceModel = new ProcedureServiceModel();
        procedureServiceModel.setId(PROCEDURE_ID);
        Procedure procedure = new Procedure();
        procedure.setId(PROCEDURE_ID);

        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(procedureServiceModel);
        Mockito.when(this.procedureRepository.findById(Mockito.any())).thenReturn(Optional.of(procedure));
        String result = procedureService.findById(PROCEDURE_ID).getId();
        Assert.assertEquals(PROCEDURE_ID, result);

    }

    @Test(expected = NullPointerException.class)
    public void findByInvalidIdThrowsException() {
            procedureService.findById(PROCEDURE_ID);
            verify(procedureRepository)
                    .findById(PROCEDURE_ID);
        }


    @Test
    public void findByNameReturnsCorrectEntity() {
        ProcedureServiceModel procedureServiceModel = new ProcedureServiceModel();
        procedureServiceModel.setName(NAME);
        Procedure procedure = new Procedure();
        procedure.setName(NAME);

        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(procedureServiceModel);
        Mockito.when(this.procedureRepository.findByName(Mockito.any())).thenReturn(procedure);
        String result = procedureService.findByName(NAME).getName();
        Assert.assertEquals(NAME, result);

    }

    @Test
    public void deleteArticleRemovesItFromDBAndCreatesLog() {

        Procedure procedure = new Procedure();
        procedure.setId(PROCEDURE_ID);
        procedure.setName(NAME);

        Mockito.when(this.procedureRepository.getOne(Mockito.any())).thenReturn(procedure);

        User user = new User();
        user.setUsername(USERNAME);
        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext secCont = Mockito.mock(SecurityContext.class);
        Mockito.when(secCont.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(secCont);
        Mockito.when(auth.getPrincipal()).thenReturn(user);

        this.procedureService.deleteProcedure(PROCEDURE_ID);

        verify(procedureRepository)
                .deleteById(PROCEDURE_ID);

        verify(logService)
                .addEvent(any());



    }

}
