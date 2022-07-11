package antifraud.dao;

import antifraud.model.entity.StatusOfTransaction;
import antifraud.model.enums.TransactionStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionStatusDao extends CrudRepository<StatusOfTransaction, TransactionStatus> {
    StatusOfTransaction findFirstByMaxAmountGreaterThanEqualOrderByMaxAmountAsc(long amount);
}
