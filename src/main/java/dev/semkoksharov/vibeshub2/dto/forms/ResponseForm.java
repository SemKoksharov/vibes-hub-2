package dev.semkoksharov.vibeshub2.dto.forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseForm {

    private String status;
    private String message;
    private String timestamp;
    private Object data;

}
