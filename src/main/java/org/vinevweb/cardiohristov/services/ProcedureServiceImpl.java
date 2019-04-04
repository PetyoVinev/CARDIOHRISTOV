package org.vinevweb.cardiohristov.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vinevweb.cardiohristov.domain.entities.Procedure;
import org.vinevweb.cardiohristov.domain.models.service.ProcedureServiceModel;
import org.vinevweb.cardiohristov.repositories.ProcedureRepository;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class ProcedureServiceImpl implements ProcedureService {


    private final ProcedureRepository procedureRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public ProcedureServiceImpl(ProcedureRepository procedureRepository, ModelMapper modelMapper) {
        this.procedureRepository = procedureRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public boolean createProcedure(ProcedureServiceModel procedureServiceModel) {
        Procedure procedure = this.modelMapper.map(procedureServiceModel, Procedure.class);

        this.procedureRepository.save(procedure);

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
        this.procedureRepository.deleteById(id);
    }

}
