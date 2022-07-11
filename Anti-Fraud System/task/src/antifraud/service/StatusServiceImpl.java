package antifraud.service;

import antifraud.dao.TransactionStatusDao;
import antifraud.model.entity.StatusOfTransaction;
import antifraud.model.enums.TransactionStatus;
import antifraud.security.exeption.TransactionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StatusServiceImpl implements StatusService {
    TransactionStatusDao transactionStatusDao;

    @Autowired
    public StatusServiceImpl(TransactionStatusDao transactionStatusDao) {
        this.transactionStatusDao = transactionStatusDao;
    }

    @Override
    public TransactionStatus getStatusWithAmount(long amount) {
        return transactionStatusDao
                .findFirstByMaxAmountGreaterThanEqualOrderByMaxAmountAsc(amount)
                .getStatus();
    }

    @Override
    public StatusOfTransaction get(TransactionStatus status) {
        return transactionStatusDao
                .findById(status)
                .orElseThrow(TransactionNotFoundException::new);
    }

    @Override
    public StatusOfTransaction update( StatusOfTransaction statusOfTransaction) {
        return transactionStatusDao.save(statusOfTransaction);
    }
}