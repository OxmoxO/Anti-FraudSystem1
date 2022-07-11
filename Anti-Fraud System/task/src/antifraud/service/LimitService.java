package antifraud.service;

import antifraud.model.enums.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LimitService {
    private final StatusService statusService;
    private ChangerService changer;

    @Autowired
    public LimitService(StatusService statusService) {
        this.statusService = statusService;
        init();
    }

    private void init() {
        changer = new LimitStatusServiceImpl(statusService, TransactionStatus.ALLOWED);

        ChangerService manual = new LimitStatusServiceImpl(statusService, TransactionStatus.MANUAL_PROCESSING);
        ChangerService prohibited = new LimitStatusServiceImpl(statusService, TransactionStatus.PROHIBITED);

        changer
                .setNextLimitChanger(manual)
                .setNextLimitChanger(prohibited);
    }

    public void recalculateLimits(TransactionStatus from,
                                  TransactionStatus to,
                                  long amount) {
        changer.changeLimit(from, to, amount);
    }
}
