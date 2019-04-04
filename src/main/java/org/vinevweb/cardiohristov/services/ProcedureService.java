package org.vinevweb.cardiohristov.services;


import org.vinevweb.cardiohristov.domain.models.service.ProcedureServiceModel;

import java.util.List;

public interface ProcedureService {
    boolean createProcedure(ProcedureServiceModel procedureServiceModel);

    List<ProcedureServiceModel> getAllByDateAsc();

    ProcedureServiceModel findById(String id);

    ProcedureServiceModel findByName(String name);

    void deleteProcedure(String id);

}
