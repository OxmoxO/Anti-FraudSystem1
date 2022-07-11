package antifraud.model.mapper;

import antifraud.model.dto.TransactionDto;
import antifraud.model.entity.Transaction;
import antifraud.model.enums.WorldRegion;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper implements ToEntityMapper<TransactionDto, Transaction> {

    @Override
    public Transaction toEntity(TransactionDto dto) {
        return new Transaction(dto.getAmount(), dto.getIp(), dto.getNumber(),
                              WorldRegion.valueOf(dto.getRegion()), dto.getDate());
    }
}
