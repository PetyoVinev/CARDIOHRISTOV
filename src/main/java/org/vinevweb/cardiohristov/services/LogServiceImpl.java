package org.vinevweb.cardiohristov.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.vinevweb.cardiohristov.common.Constants;
import org.vinevweb.cardiohristov.domain.entities.Log;
import org.vinevweb.cardiohristov.domain.entities.User;
import org.vinevweb.cardiohristov.domain.models.service.LogServiceModel;
import org.vinevweb.cardiohristov.repositories.LogRepository;
import org.vinevweb.cardiohristov.repositories.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.vinevweb.cardiohristov.common.Constants.LOGGER_FULL_DATETIME_FORMAT;

@Service
public class LogServiceImpl implements LogService {


    private final LogRepository logRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public LogServiceImpl(LogRepository logRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.logRepository = logRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Async
    public void addEvent(String[] eventParams) {

      /*  //check async
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/
        Log logEntity = this.prepareLogEntity(eventParams);
        this.logRepository.save(logEntity);
    }

    @Override
    public List<LogServiceModel> getLogsOrderedByDate() {
        return this.logRepository.findAllByOrderByDateTimeDesc()
                .stream()
                .map(log ->{
                    LogServiceModel logServiceModel = this.modelMapper.map(log, LogServiceModel.class);

                    return logServiceModel;
                })
                .collect(Collectors.toList());
    }

    private Log prepareLogEntity(String[] eventParams) {
        Log logEntity = new Log();
        logEntity.setDateTime(this.formatDate(eventParams[0]));

        this.checkUserExistence(eventParams[1]);

        logEntity.setUser(eventParams[1]);
        logEntity.setEvent(eventParams[2]);

        return logEntity;
    }

    private LocalDateTime formatDate(String dateTimeStr) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(LOGGER_FULL_DATETIME_FORMAT);

        return LocalDateTime
                .parse(dateTimeStr.replace("T", " ")
                        .substring(0, dateTimeStr.lastIndexOf(".")), format);
    }

    private void checkUserExistence(String userMail) {
        if (userMail == null) {
            throw new UsernameNotFoundException(Constants.WRONG_NON_EXISTENT_EMAIL);
        }
    }
}
