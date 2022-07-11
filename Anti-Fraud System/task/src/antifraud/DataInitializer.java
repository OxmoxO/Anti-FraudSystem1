package antifraud;

import antifraud.dao.TransactionStatusDao;
import antifraud.model.entity.StatusOfTransaction;
import antifraud.model.enums.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {
    private boolean isDBInitialized = false;
    private final List<StatusOfTransaction> statuses = List.of(
            new StatusOfTransaction(TransactionStatus.ALLOWED,200),
            new StatusOfTransaction(TransactionStatus.MANUAL_PROCESSING, 1500),
            new StatusOfTransaction(TransactionStatus.PROHIBITED, Long.MAX_VALUE)
    );

    @Autowired
    private TransactionStatusDao statusDao;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!isDBInitialized) {
            statuses.forEach(this::createStatus);
            isDBInitialized = true;
        }
    }

    private void createStatus(StatusOfTransaction s) {
        boolean isStatusExists = statusDao.existsById(s.getStatus());
        if (!isStatusExists) {
            statusDao.save(s);
        }
    }
}
