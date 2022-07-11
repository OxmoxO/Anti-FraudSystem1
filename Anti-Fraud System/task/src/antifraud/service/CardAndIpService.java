package antifraud.service;

import antifraud.dao.HistoryDao;
import antifraud.dao.StolenCreditCardDao;
import antifraud.dao.SuspiciousIpDao;
import antifraud.dao.UserDao;
import antifraud.model.entity.StolenCreditCard;
import antifraud.model.entity.SuspiciousIp;
import antifraud.model.entity.Transaction;
import antifraud.model.entity.User;
import antifraud.model.enums.Role;
import antifraud.model.enums.WorldRegion;
import antifraud.security.exeption.CardIsStolenException;
import antifraud.security.exeption.CardNotFoundException;
import antifraud.security.exeption.IpBannedException;
import antifraud.security.exeption.IpNotFoundException;
import antifraud.security.exeption.TransactionNotFoundException;
import antifraud.security.exeption.UserExistsException;
import antifraud.security.exeption.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CardAndIpService {

    private final UserDao userDao;
    private final SuspiciousIpDao ipDao;
    private final StolenCreditCardDao cardDao;
    private final HistoryDao historyDao;
    @Autowired

    public CardAndIpService(UserDao userDao,
                            SuspiciousIpDao ipDao,
                            StolenCreditCardDao cardDao,
                            HistoryDao historyDao) {

            this.userDao = userDao;
            this.ipDao = ipDao;
            this.cardDao = cardDao;
            this.historyDao = historyDao;
        }

        public User create(User user) {
            if (userDao
                    .existsByUsernameIgnoreCase(user.getUsername())) {
                throw new UserExistsException();
            }
            user.setRole(countUsers() == 0 ?
                    Role.ADMINISTRATOR : Role.MERCHANT);
            user.setLocked(user.getRole() != Role.ADMINISTRATOR);
            return userDao.save(user);
        }

        public List<User> getAllUsers() {
            return (List<User>) userDao.findAll();
        }

        public void delete(String username) {
            userDao.delete(findUser(username));
        }
        public long countUsers() {
            return userDao.count();
        }
        public User findUser(String username) {
            return userDao
                    .findByUsernameIgnoreCase(username)
                    .orElseThrow(UserNotFoundException::new);
        }
        public void update(User user) {
            userDao.save(user);

    }

    public SuspiciousIp create(SuspiciousIp ip) {
        if (isIpBanned(ip.getIp())) {
            throw new IpBannedException();
        }
        return ipDao.save(ip);
    }

    public boolean isIpBanned(String ip) {
        return ipDao.existsByIp(ip);
    }

    public void deleteSuspiciousIp(String ip) {
        if (!isIpBanned(ip)) {
            throw new IpNotFoundException();
        }
        ipDao.delete(ipDao.findFirstByIp(ip));
    }

    public List<SuspiciousIp> getAllSuspiciousIps() {
        return (List<SuspiciousIp>) ipDao.findAll();
    }

    public StolenCreditCard create(StolenCreditCard card) {
        if (isCardStolen(card.getNumber())) {
            throw new CardIsStolenException();
        }
        return cardDao.save(card);
    }

    public boolean isCardStolen(String number) {
        return cardDao.existsByNumber(number);
    }

    public void deleteStolenCreditCard(String cardNumber) {
        if (!isCardStolen(cardNumber)) {
            throw new CardNotFoundException();
        }
        cardDao.delete(cardDao.findFirstByNumber(cardNumber));
    }

    public List<StolenCreditCard> getAllStolenCards() {
        return (List<StolenCreditCard>) cardDao.findAll();
    }

    public Transaction saveTransactionToHistory(Transaction transaction) {
        return historyDao.save(transaction);
    }

    public List<Transaction> getTransactionsCardRegionAndTime(String cardNumber,
                                                              WorldRegion region,
                                                              LocalDateTime start,
                                                              LocalDateTime end) {
        return historyDao
                .findAllByNumberAndRegionNotAndDateBetween(cardNumber, region, start, end);
    }

    public List<Transaction> getTransactionsCardIpAndTime(String cardNumber,
                                                          String ip,
                                                          LocalDateTime start,
                                                          LocalDateTime end) {
        return historyDao.findAllByNumberAndIpNotAndDateBetween(cardNumber, ip, start, end);
    }

    public Transaction getTransaction(long id) {
        return historyDao
                         .findById(id)
                         .orElseThrow(TransactionNotFoundException::new);
    }

    public List<Transaction> getAllTransactionsFromHistory() {
        return historyDao.findAllByOrderByIdAsc();
    }

    public List<Transaction> getTransactionsByCardNumber(String number) {
        var transactionEntities = historyDao
                                                            .findAllByNumber(number);
        if (transactionEntities.isEmpty()) {
            throw new TransactionNotFoundException();
        }
        return transactionEntities;
    }
}

