package antifraud.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "stolen_credit_card")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class StolenCreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private String number;
}
