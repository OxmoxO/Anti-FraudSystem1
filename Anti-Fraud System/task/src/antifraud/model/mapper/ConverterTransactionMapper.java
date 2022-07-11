package antifraud.model.mapper;

import antifraud.model.dto.TransactionDto;
import antifraud.model.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class ConverterTransactionMapper extends TransactionMapper implements ConverterMapper<Transaction, TransactionDto> {

    @Override
    public TransactionDto toDto(Transaction transaction) {

        return new TransactionDto(transaction.getId(), transaction.getAmount(),
                                  transaction.getIp(), transaction.getNumber(),
                                  transaction.getRegion().name(),
                                  transaction.getDate(),
                                  transaction.getStatus().name(),
                                  transaction.getFeedback() == null ?
                                          "" : transaction.getFeedback().name());
    }
}
