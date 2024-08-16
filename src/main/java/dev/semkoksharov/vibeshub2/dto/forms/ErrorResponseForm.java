package dev.semkoksharov.vibeshub2.dto.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseForm extends BaseResponseForm{

    private String exception;
    private String stackTrace;


    public ErrorResponseForm(String status, String message, String timestamp, String exception, String stackTrace) {
        super(status, message, timestamp);
        this.exception = exception;
        this.stackTrace = stackTrace;
    }
}
