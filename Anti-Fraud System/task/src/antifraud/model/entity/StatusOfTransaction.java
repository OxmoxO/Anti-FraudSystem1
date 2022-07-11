package antifraud.model.entity;

import antifraud.model.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name = "transaction_status")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StatusOfTransaction {
    @Id
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private long maxAmount;
}
