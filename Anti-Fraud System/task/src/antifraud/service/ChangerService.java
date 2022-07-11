package antifraud.service;

import antifraud.model.entity.StatusOfTransaction;
import antifraud.model.enums.TransactionStatus;

import java.util.function.BiFunction;

public abstract class ChangerService {
    private final StatusService statusService;
    private final TransactionStatus status;
    private ChangerService changer;

    public ChangerService(StatusService statusService, TransactionStatus status) {
        this.statusService = statusService;
        this.status = status;
    }

    public ChangerService setNextLimitChanger(ChangerService changer) {
        this.changer = changer;
        return this.changer;
    }

    private void changeLimitIfStatusDESCDirection(TransactionStatus from, TransactionStatus to, long amount) {
        if (status.getPriority() < from.getPriority() && status.getPriority() >= to.getPriority()) {
            BiFunction<Long, Long, Long> calculationMethod = this::calculateIncreasedLimit;
            updateLimit(amount, calculationMethod);
        }
        if (changer != null) {
            changer.changeLimitIfStatusDESCDirection(from, to, amount);
        }
    }

    private void changeLimitIfStatusASCDirection(TransactionStatus from, TransactionStatus to, long amount) {
        if (status.getPriority() >= from.getPriority() &&
                                   status.getPriority() < to.getPriority()) {
            BiFunction<Long, Long, Long> calculationMethod = this::calculateDecreasedLimit;
            updateLimit(amount, calculationMethod);
        }
        if (changer!= null) {
            changer.changeLimitIfStatusASCDirection(from, to, amount);
        }
    }

    private void updateLimit(long amount, BiFunction<Long, Long, Long> limitCalculationMethod) {
        StatusOfTransaction current = statusService.get(status);
        long newLimit = limitCalculationMethod.apply(current.getMaxAmount(), amount);
        current.setMaxAmount(newLimit);
        statusService.update(current);
    }

    public void changeLimit(TransactionStatus from, TransactionStatus to, long amount) {

        if (from.getPriority() > to.getPriority()) {
            changeLimitIfStatusDESCDirection(from, to, amount);
        }
        if (from.getPriority() < to.getPriority()) {
            changeLimitIfStatusASCDirection(from, to, amount);
        }
    }

    abstract long calculateIncreasedLimit(long old, long amount);

    abstract long calculateDecreasedLimit(long old, long amount);
}
