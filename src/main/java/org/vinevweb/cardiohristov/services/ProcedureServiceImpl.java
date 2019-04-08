package org.vinevweb.cardiohristov.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.vinevweb.cardiohristov.domain.entities.Procedure;
import org.vinevweb.cardiohristov.domain.entities.User;
import org.vinevweb.cardiohristov.domain.models.service.ProcedureServiceModel;
import org.vinevweb.cardiohristov.repositories.ProcedureRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.vinevweb.cardiohristov.common.Constants.*;


@Service
public class ProcedureServiceImpl implements ProcedureService {



    private final ProcedureRepository procedureRepository;

    private final ModelMapper modelMapper;

    private final LogService logService;


    @Autowired
    public ProcedureServiceImpl(ProcedureRepository procedureRepository, ModelMapper modelMapper, LogService logService) {
        this.procedureRepository = procedureRepository;
        this.modelMapper = modelMapper;
        this.logService = logService;
    }


    @Override
    public boolean createProcedure(ProcedureServiceModel procedureServiceModel) {
        Procedure procedure = this.modelMapper.map(procedureServiceModel, Procedure.class);

        this.procedureRepository.save(procedure);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User)authentication.getPrincipal();

        this.logService.addEvent(new String[]{ LocalDateTime.now().toString(),
                currentUser.getUsername(),
                PROCEDURE_HAS_BEEN_CREATED + procedure.getName()});
        return true;
    }

    @Override
    public List<ProcedureServiceModel> getAllByDateAsc() {
        return this.procedureRepository
                .findAllByOrderByDateAsc()
                .stream()
                .map(x -> this.modelMapper.map(x, ProcedureServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public ProcedureServiceModel findById(String id) {
        return modelMapper.map(Objects.requireNonNull(this.procedureRepository.findById(id).orElse(null)), ProcedureServiceModel.class);
    }

    @Override
    public ProcedureServiceModel findByName(String name) {
        return modelMapper.map(Objects.requireNonNull(this.procedureRepository.findByName(name)), ProcedureServiceModel.class);
    }

    @Override
    public void deleteProcedure(String id) {

        String procedureName = this.procedureRepository.getOne(id).getName();

        this.procedureRepository.deleteById(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User)authentication.getPrincipal();

        this.logService.addEvent(new String[]{ LocalDateTime.now().toString(),
                currentUser.getUsername(),
                PROCEDURE_HAS_BEEN_DELETED + procedureName});
    }

}
