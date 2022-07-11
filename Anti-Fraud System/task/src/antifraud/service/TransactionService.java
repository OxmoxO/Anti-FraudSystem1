package antifraud.service;


import antifraud.model.dto.ResultDto;
import antifraud.model.dto.TransactionDto;
import antifraud.model.entity.Transaction;
import antifraud.model.enums.TransactionStatus;
import antifraud.model.mapper.ToEntityMapper;
import antifraud.model.util.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

@Component
public class TransactionService {
    private final CardAndIpService cardAndIpService;

    private final ToEntityMapper<TransactionDto, Transaction> transactionMapper;
    private final StatusService statusService;

    private static final int HOURS_BEFORE_TRANSACTION = 1;
    private static final String AMOUNT_IS_INCORRECT = "amount";
    private static final String CARD_IS_STOLEN = "card-number";
    private static final String IP_IS_BLOCKED = "ip";
    private static final String ALL_IS_CORRECT = "none";

    private static final String SUS_IP_OPERATION = "ip-correlation";
    private static final String SUS_REGION_OPERATION = "region-correlation";

    @Autowired
    public TransactionService(CardAndIpService cardAndIpService,
                              ToEntityMapper<TransactionDto, Transaction> transactionMapper,
                              StatusService statusService) {

        this.cardAndIpService = cardAndIpService;
        this.transactionMapper = transactionMapper;
        this.statusService= statusService;
    }

    public ResultDto resolve(TransactionDto transactionDto) {
        Transaction transaction = transactionMapper.toEntity(transactionDto);
        TransactionStatus status = statusService.getStatusWithAmount(transaction.getAmount());

        Set<String> info = new TreeSet<>();
        if (status != TransactionStatus.ALLOWED) {
            info.add(AMOUNT_IS_INCORRECT);
        }

        if (cardAndIpService.isCardStolen(transactionDto.getNumber())) {
            clearInfoIfThereMoreSevereStatus(status, info);
            status = TransactionStatus.PROHIBITED;
            info.add(CARD_IS_STOLEN);
        }

        if (cardAndIpService.isIpBanned(transactionDto.getIp())) {
            clearInfoIfThereMoreSevereStatus(status, info);
            status = TransactionStatus.PROHIBITED;
            info.add(IP_IS_BLOCKED);
        }
        TransactionStatus regionSt = getStatusIfSameCardFromDifferentRegions(transaction);
        if (regionSt != TransactionStatus.ALLOWED) {
            status = regionSt;
            info.add(SUS_REGION_OPERATION);
        }
        TransactionStatus ipSt = sameCardDifferentIP(transaction);
        if (ipSt != TransactionStatus.ALLOWED) {
            status = ipSt;
            info.add(SUS_IP_OPERATION);
        }


        if (status == TransactionStatus.ALLOWED) {
            info.add(ALL_IS_CORRECT);
        }

        return new ResultDto(status.name(),
                String.join(", ", info));
    }

    private TransactionStatus getStatusIfSameCardFromDifferentRegions(Transaction transaction) {
        List<Transaction> lastHour = cardAndIpService
                .getTransactionsCardRegionAndTime(
                        transaction.getNumber(),
                        transaction.getRegion(),
                        transaction
                                .getDate()
                                .minusHours(HOURS_BEFORE_TRANSACTION),
                        transaction.getDate());
        long regionCount = countDistinctByField(lastHour, Transaction::getRegion);
        return getStatusBasedOnNumber(regionCount);
    }

    private TransactionStatus sameCardDifferentIP(Transaction transaction) {

        List<Transaction> lastHour = cardAndIpService
                .getTransactionsCardIpAndTime(
                        transaction.getNumber(),
                        transaction.getIp(),
                        transaction
                                .getDate()
                                .minusHours(HOURS_BEFORE_TRANSACTION),
                        transaction.getDate());
        long ipCount = countDistinctByField(lastHour, Transaction::getIp);
        return getStatusBasedOnNumber(ipCount);
    }

    private TransactionStatus getStatusBasedOnNumber(long num) {

        TransactionStatus status = TransactionStatus.ALLOWED;
        if (num > 2) {
            status = TransactionStatus.PROHIBITED;
        } else if (num == 2) {
            status = TransactionStatus.MANUAL_PROCESSING;
        }
        return status;
    }

    public <T> long countDistinctByField(List<T> list,
                                         Function<T, Object> getFiledFunction) {
        return list
                .stream()
                .filter(new Field<>(getFiledFunction))
                .count();
    }

    private void clearInfoIfThereMoreSevereStatus(TransactionStatus status,
                                                  Set<String> info) {
        if (status == TransactionStatus.MANUAL_PROCESSING) {
            info.clear();
        }
    }

}
