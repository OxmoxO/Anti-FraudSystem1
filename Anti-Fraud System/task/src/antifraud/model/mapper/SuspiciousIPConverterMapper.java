package antifraud.model.mapper;

import antifraud.model.dto.SuspiciousIpDto;
import antifraud.model.entity.SuspiciousIp;
import org.springframework.stereotype.Component;

@Component
public class SuspiciousIPConverterMapper implements ConverterMapper<SuspiciousIp,
                                                                    SuspiciousIpDto> {

    @Override
    public SuspiciousIpDto toDto(SuspiciousIp suspiciousIp) {
        return new SuspiciousIpDto(suspiciousIp.getId(), suspiciousIp.getIp());
    }

    @Override
    public SuspiciousIp toEntity(SuspiciousIpDto suspiciousIpDto) {
        return new SuspiciousIp(suspiciousIpDto.getId(), suspiciousIpDto.getIp());
    }
}
