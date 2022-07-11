package antifraud.model.dto;

import antifraud.model.annotation.EnumValueCorrect;
import antifraud.model.enums.Operation;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Data
public class OperationDto {

    @NotEmpty
    private String username;

    @EnumValueCorrect(enumClazz = Operation.class,
                      message = "Operation must be LOCK or UNLOCK!")
    private String operation;
}