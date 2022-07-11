package antifraud.model.mapper;

import antifraud.model.dto.StolenCreditCardDto;
import antifraud.model.entity.StolenCreditCard;
import org.springframework.stereotype.Component;

@Component
public class StolenCardConverterMapper implements ConverterMapper<StolenCreditCard,
        StolenCreditCardDto> {
    @Override
    public StolenCreditCardDto toDto(StolenCreditCard stolenCreditCard) {
        return new StolenCreditCardDto(stolenCreditCard.getId(),
                stolenCreditCard.getNumber());
    }

    @Override
    public StolenCreditCard toEntity(StolenCreditCardDto stolenCreditCardDto) {
        return new StolenCreditCard(stolenCreditCardDto.getId(),
                stolenCreditCardDto.getNumber());
    }
}