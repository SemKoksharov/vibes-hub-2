package dev.semkoksharov.vibeshub2.dto.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public abstract class BaseResponseForm {

    private String status;
    private String message;
    private final String timestamp;


}
