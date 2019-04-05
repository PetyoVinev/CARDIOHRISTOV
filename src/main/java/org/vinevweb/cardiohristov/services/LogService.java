package org.vinevweb.cardiohristov.services;



import org.vinevweb.cardiohristov.domain.models.service.LogServiceModel;

import java.util.List;

public interface LogService {

    void addEvent(String[] eventParams);

    List<LogServiceModel> getLogsOrderedByDate();
}
