package dev.semkoksharov.vibeshub2.dto.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseForm extends BaseResponseForm{
    private Object data;

    public ResponseForm(String status, String message, String timestamp, Object data) {
        super(status, message, timestamp);
        this.data = data;
    }

    public ResponseForm(String status, String message, String timestamp) {
        super(status, message, timestamp);
    }


}
