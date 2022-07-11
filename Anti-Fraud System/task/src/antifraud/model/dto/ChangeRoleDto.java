package antifraud.model.dto;


import antifraud.model.annotation.EnumValueCorrect;
import antifraud.model.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Data
public class ChangeRoleDto {

    @NotEmpty
    private String username;

    @EnumValueCorrect(enumClazz = Role.class, message = "Role incorrect!")
    private String role;
}