package org.vinevweb.cardiohristov;

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

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@DataJpaTest
@ActiveProfiles("test")
public class ProcedureServiceTests {


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
        user.setUsername("boko@abv.bg");

        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext secCont = Mockito.mock(SecurityContext.class);
        Mockito.when(secCont.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(secCont);
        Mockito.when(auth.getPrincipal()).thenReturn(user);

        ProcedureServiceModel procedureServiceModel = new ProcedureServiceModel();

        Procedure procedure = new Procedure();
        procedure.setName("Procedure Title");

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
        procedureServiceModel.setId("1234");
        Procedure procedure = new Procedure();
        procedure.setId("1234");

        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(procedureServiceModel);
        Mockito.when(this.procedureRepository.findById(Mockito.any())).thenReturn(Optional.of(procedure));
        String result = procedureService.findById("1234").getId();
        Assert.assertEquals("1234", result);

    }

    @Test(expected = NullPointerException.class)
    public void findByInvalidIdThrowsException() {
            procedureService.findById("asd");
            verify(procedureRepository)
                    .findById("asd");
        }


    @Test
    public void findByNameReturnsCorrectEntity() {
        ProcedureServiceModel procedureServiceModel = new ProcedureServiceModel();
        procedureServiceModel.setName("Name");
        Procedure procedure = new Procedure();
        procedure.setName("Name");

        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(procedureServiceModel);
        Mockito.when(this.procedureRepository.findByName(Mockito.any())).thenReturn(procedure);
        String result = procedureService.findByName("Name").getName();
        Assert.assertEquals("Name", result);

    }

    @Test
    public void deleteArticleRemovesItFromDBAndCreatesLog() {

        Procedure procedure = new Procedure();
        procedure.setId("1234");
        procedure.setName("Name");

        Mockito.when(this.procedureRepository.getOne(Mockito.any())).thenReturn(procedure);

        User user = new User();
        user.setUsername("boko@abv.bg");
        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext secCont = Mockito.mock(SecurityContext.class);
        Mockito.when(secCont.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(secCont);
        Mockito.when(auth.getPrincipal()).thenReturn(user);

        this.procedureService.deleteProcedure("1234");

        verify(procedureRepository)
                .deleteById("1234");

        verify(logService)
                .addEvent(any());



    }

}
