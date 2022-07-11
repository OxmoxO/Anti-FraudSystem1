package antifraud.service;

import antifraud.model.entity.StatusOfTransaction;
import antifraud.model.enums.TransactionStatus;

public interface StatusService {
    TransactionStatus getStatusWithAmount(long amount);

    StatusOfTransaction get(TransactionStatus status);

    StatusOfTransaction update(StatusOfTransaction statusOfTransaction);
}
