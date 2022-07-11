package antifraud.dao;


import antifraud.model.entity.SuspiciousIp;
import org.springframework.data.repository.CrudRepository;

public interface SuspiciousIpDao extends CrudRepository<SuspiciousIp, Long> {
    boolean existsByIp(String ip);

    SuspiciousIp findFirstByIp(String ip);
}
