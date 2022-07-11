package antifraud.service;


import antifraud.model.enums.TransactionStatus;


public class LimitStatusServiceImpl extends ChangerService{

    public LimitStatusServiceImpl(StatusService statusService,
                                  TransactionStatus status) {
        super(statusService, status);
    }

    @Override
    long calculateIncreasedLimit(long oldAmount, long amount) {
        return (long) Math.ceil(0.8 * oldAmount + 0.2 * amount);
    }

    @Override
    long calculateDecreasedLimit(long oldAmount, long amount) {
        return (long) Math.ceil(0.8 * oldAmount - 0.2 * amount);
    }
}
