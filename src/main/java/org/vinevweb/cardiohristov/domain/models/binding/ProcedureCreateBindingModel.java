package org.vinevweb.cardiohristov.domain.models.binding;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;

import static org.vinevweb.cardiohristov.common.Constants.PROCEDURE_DATETIME_FORMAT;

public class ProcedureCreateBindingModel {


    private String name;

    private String content;

    @DateTimeFormat(pattern = PROCEDURE_DATETIME_FORMAT, iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;

    private MultipartFile procedurePicture;

    public ProcedureCreateBindingModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public MultipartFile getProcedurePicture() {
        return procedurePicture;
    }

    public void setProcedurePicture(MultipartFile procedurePicture) {
        this.procedurePicture = procedurePicture;
    }
}
