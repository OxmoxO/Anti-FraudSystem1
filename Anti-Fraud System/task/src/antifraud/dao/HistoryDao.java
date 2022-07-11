package antifraud.dao;


import antifraud.model.entity.Transaction;
import antifraud.model.enums.WorldRegion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public
interface HistoryDao extends CrudRepository<Transaction, Long> {

    List<Transaction> findAllByNumberAndRegionNotAndDateBetween(
            String number,
            WorldRegion region,
            LocalDateTime publicationDateStart,
            LocalDateTime publicationDateEnd
    );

    List<Transaction> findAllByNumberAndIpNotAndDateBetween(String number, String ip,
                                                            LocalDateTime publicationDateStart,
                                                            LocalDateTime publicationDateEnd);
    List<Transaction> findAllByOrderByIdAsc();

    List<Transaction> findAllByNumber(String number);
}