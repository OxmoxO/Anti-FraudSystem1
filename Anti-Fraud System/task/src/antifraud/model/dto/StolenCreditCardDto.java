package antifraud.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.LuhnCheck;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StolenCreditCardDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long id;

    @NonNull
    @LuhnCheck(message = "Incorrect card number!")
    private String number;
}
