package antifraud.model.dto;

import antifraud.model.annotation.EnumValueCorrect;
import antifraud.model.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Min;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FeedBackDto {
    @Min(value = 0, message = "Incorrect id!")
    @NonNull
    private Long transactionId;
    @EnumValueCorrect(enumClazz = TransactionStatus.class, message = "Incorrect feedback!")
    private String feedback;
}
