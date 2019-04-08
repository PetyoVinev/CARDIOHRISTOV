package org.vinevweb.cardiohristov.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.vinevweb.cardiohristov.domain.models.binding.AppointmentCreateBindingModel;
import org.vinevweb.cardiohristov.domain.models.binding.AppointmentDeleteBindingModel;
import org.vinevweb.cardiohristov.domain.models.binding.UserRegisterBindingModel;
import org.vinevweb.cardiohristov.domain.models.service.AppointmentServiceModel;
import org.vinevweb.cardiohristov.domain.models.view.AllAppointmentViewModel;
import org.vinevweb.cardiohristov.domain.models.view.AllProceduresProcedureViewModel;
import org.vinevweb.cardiohristov.domain.models.view.AppointmentCreatedViewModel;
import org.vinevweb.cardiohristov.domain.models.view.EditDeleteAppointmentViewModel;
import org.vinevweb.cardiohristov.errors.AppointmentCreateFailureException;
import org.vinevweb.cardiohristov.services.AppointmentService;
import org.vinevweb.cardiohristov.services.ProcedureService;
import org.vinevweb.cardiohristov.web.annotations.PageTitle;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.vinevweb.cardiohristov.common.Constants.APPOINTMENT_CREATE_FAILURE_EXCEPTION;
import static org.vinevweb.cardiohristov.common.Constants.TITLE_SCHEDULE;


@Controller
@RequestMapping("/appointments")
public class AppointmentController extends BaseController {


    private final ModelMapper modelMapper;
    private final AppointmentService appointmentService;
    private final ProcedureService procedureService;


    @Autowired
    public AppointmentController(ModelMapper modelMapper, AppointmentService appointmentService, ProcedureService procedureService) {
        this.modelMapper = modelMapper;
        this.appointmentService = appointmentService;
        this.procedureService = procedureService;
    }


    @GetMapping(value = "/api/checkSpecificHour", produces = "application/json")
    @ResponseBody
    public boolean checkAvailableDateTime(@RequestParam("date") String date,
                                          @RequestParam("time") String time) {

        return appointmentService.findByDateTime(date, time);
    }

    @GetMapping(value = "/api/getBuzyHoursForDate", produces = "application/json")
    @ResponseBody
    public String[][] getBuzyHoursForDate(@RequestParam("date") String date) {
        String[][] result = appointmentService.getBuzyHoursForDate(date);
        return result;
    }

    @GetMapping(value = "/api/getAppointmentById", produces = "application/json")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public EditDeleteAppointmentViewModel editDeleteAppointmentViewModel(@RequestParam("id") String id) {
        EditDeleteAppointmentViewModel  editDeleteAppointmentViewModel = modelMapper.map(appointmentService.findById(id),
                EditDeleteAppointmentViewModel.class);
        editDeleteAppointmentViewModel.setAppointmentTime();
        editDeleteAppointmentViewModel.setAppointmentDate();
        return editDeleteAppointmentViewModel;

    }


    @PostMapping("/create")
    public ModelAndView createAppointmentConfirm(@Valid @ModelAttribute AppointmentCreateBindingModel appointmentCreateBindingModel,
                                                 @ModelAttribute("userRegisterBindingModel") UserRegisterBindingModel userRegisterBindingModel,
                                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new AppointmentCreateFailureException(APPOINTMENT_CREATE_FAILURE_EXCEPTION);
        }

        appointmentCreateBindingModel.setDatetime();
        AppointmentServiceModel appointmentServiceModel = this.modelMapper
                .map(appointmentCreateBindingModel, AppointmentServiceModel.class);


        String id = this.appointmentService
                .createAppointment(appointmentServiceModel);

        if (id == null) {
            return super.view("appointment-failed", "appointment", appointmentCreateBindingModel);
        }
        AppointmentCreatedViewModel appointmentCreatedViewModel = modelMapper.map(appointmentCreateBindingModel , AppointmentCreatedViewModel.class);
        appointmentCreatedViewModel.setId(id);

        return super.view("appointment-created", "appointment", appointmentCreatedViewModel);
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView updateAppointmentConfirm(@ModelAttribute AppointmentCreateBindingModel appointmentCreateBindingModel) {

        appointmentCreateBindingModel.setDatetime();
        AppointmentServiceModel appointmentServiceModel = this.modelMapper
                .map(appointmentCreateBindingModel, AppointmentServiceModel.class);


        this.appointmentService
                .updateAppointment(appointmentServiceModel);

        return super.redirect("/appointments/all");
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteAppointment(@ModelAttribute AppointmentDeleteBindingModel appointmentDeleteBindingModel) {
        AppointmentServiceModel appointmentServiceModel = this.modelMapper
                .map(appointmentDeleteBindingModel, AppointmentServiceModel.class);
        this.appointmentService
                .deleteAppointment(appointmentServiceModel);

        return super.redirect("/appointments/all");
    }

    @GetMapping("/annul")
    public ModelAndView annul(@RequestParam("id") String id) {
        this.appointmentService
                .annulAppointment(id);
        return super.redirect("/");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle(TITLE_SCHEDULE)
    public ModelAndView getAppointments(@ModelAttribute("userRegisterBindingModel") UserRegisterBindingModel userRegisterBindingModel) {

        Map<String, Object> stringObjectMap = new HashMap<>();
        List<AllProceduresProcedureViewModel> allProceduresProcedureViewModelSet = procedureService.getAllByDateAsc().stream()
                .map(p -> modelMapper.map(p, AllProceduresProcedureViewModel.class))
                .collect(Collectors.toList());
        stringObjectMap.put("procedures", allProceduresProcedureViewModelSet);

        List<AllAppointmentViewModel> allAppointmentViewModels = appointmentService.getAllByDateTimeAsc().stream()
                .map(p -> modelMapper.map(p, AllAppointmentViewModel.class))
                .collect(Collectors.toList());
        stringObjectMap.put("appointments", allAppointmentViewModels);
        return super.view("schedule", stringObjectMap);
    }


    @GetMapping("/create")
    public ModelAndView createAppointment() {
        return super.redirect("/");
    }
}
