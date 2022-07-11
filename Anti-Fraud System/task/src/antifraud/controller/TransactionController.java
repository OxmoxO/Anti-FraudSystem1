package antifraud.controller;

import antifraud.model.dto.FeedbackDto;
import antifraud.model.dto.ResultDto;
import antifraud.model.dto.TransactionDto;
import antifraud.model.entity.Transaction;
import antifraud.model.enums.TransactionStatus;
import antifraud.model.mapper.ConverterMapper;
import antifraud.security.exeption.FeedbackAlreadyAcceptedException;
import antifraud.security.exeption.SameLimitChangeException;
import antifraud.service.CardAndIpService;
import antifraud.service.LimitService;
import antifraud.service.TransactionService;
import org.hibernate.validator.constraints.LuhnCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
public class TransactionController {
    private final CardAndIpService cardAndIpService;
    private final TransactionService transactionService;
    private final ConverterMapper<Transaction, TransactionDto> converterMapper;
    private final LimitService limitService;

    @Autowired
    public TransactionController(CardAndIpService cardAndIpService,
                                 TransactionService transactionService,
                                 ConverterMapper<Transaction, TransactionDto> converterMapper,
                                 LimitService limitService) {

        this.cardAndIpService = cardAndIpService;
        this.transactionService = transactionService;
        this.converterMapper= converterMapper;
        this.limitService = limitService;
    }

    @PostMapping("/api/antifraud/transaction")
    public ResultDto acceptTransaction(@RequestBody @Valid TransactionDto dto) {
        ResultDto result = transactionService.resolve(dto);
        logTransaction(dto, TransactionStatus.valueOf(result.getResult()));
        return result;
    }

    private void logTransaction(TransactionDto dto, TransactionStatus status) {
        Transaction transaction = converterMapper.toEntity(dto);
        transaction.setStatus(status);
        cardAndIpService.saveTransactionToHistory(transaction);
    }


    @PutMapping("/api/antifraud/transaction")
    public TransactionDto makeFeedbackForTransaction(@RequestBody @Valid FeedbackDto feedbackDto) {
        Transaction transaction = cardAndIpService
                .getTransaction(feedbackDto.getTransactionId());

        TransactionStatus newStatus = TransactionStatus.valueOf(feedbackDto.getFeedback());
        TransactionStatus transactionStatus = transaction.getStatus();
        System.out.println((transaction));
        System.out.println((newStatus));
        System.out.println((transactionStatus));

        if (transaction.getFeedback() != null) {
            throw new FeedbackAlreadyAcceptedException();
        }

        if (transactionStatus == newStatus) {
            throw new SameLimitChangeException();
        }

        transaction.setFeedback(newStatus);
        limitService
                    .recalculateLimits(transactionStatus, newStatus,
                                       transaction.getAmount());
        cardAndIpService.saveTransactionToHistory(transaction);
        return converterMapper.toDto(transaction);
    }

    @GetMapping("/api/antifraud/history")
    public List<TransactionDto> showTransactionHistory() {
        return cardAndIpService
                               .getAllTransactionsFromHistory()
                               .stream()
                               .map(converterMapper::toDto)
                               .collect(Collectors.toList());
    }

    @GetMapping("/api/antifraud/history/{number}")
    public List<TransactionDto> getTransactionFromHistoryByCardNumber(@PathVariable @Valid @LuhnCheck
                                                                      String number) {
        return cardAndIpService.getTransactionsByCardNumber(number).stream()
                .map(converterMapper::toDto)
                .collect(Collectors.toList());
    }
}
