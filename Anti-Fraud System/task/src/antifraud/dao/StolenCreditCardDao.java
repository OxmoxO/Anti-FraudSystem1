package antifraud.dao;

import antifraud.model.entity.StolenCreditCard;
import org.springframework.data.repository.CrudRepository;

public interface StolenCreditCardDao extends CrudRepository<StolenCreditCard, Long> {

    boolean existsByNumber(String number);

    StolenCreditCard findFirstByNumber(String cardNumber);
}
