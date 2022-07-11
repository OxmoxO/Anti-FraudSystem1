package antifraud.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResultDto {

    private String result;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String info;
}
