package antifraud.controller;

import antifraud.model.dto.StatusDto;
import antifraud.model.dto.StolenCreditCardDto;
import antifraud.model.dto.SuspiciousIpDto;
import antifraud.model.entity.StolenCreditCard;
import antifraud.model.entity.SuspiciousIp;
import antifraud.model.util.RegIP;
import antifraud.model.mapper.ConverterMapper;
import antifraud.service.CardAndIpService;
import org.hibernate.validator.constraints.LuhnCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
public class CardAndIpController {
    private final CardAndIpService cardAndIpService;
    private final ConverterMapper<StolenCreditCard, StolenCreditCardDto> cardMapper;
    private final ConverterMapper<SuspiciousIp, SuspiciousIpDto> ipMapper;

    @Autowired
    public CardAndIpController(CardAndIpService cardAndIpService,
                               ConverterMapper<StolenCreditCard, StolenCreditCardDto> cardMapper,
                               ConverterMapper<SuspiciousIp, SuspiciousIpDto> ipMapper) {

        this.cardAndIpService = cardAndIpService;
        this.cardMapper = cardMapper;
        this.ipMapper = ipMapper;
    }

    @PostMapping("/api/antifraud/suspicious-ip")
    public ResponseEntity<SuspiciousIpDto> addSuspiciousIP(@RequestBody @Valid SuspiciousIpDto ipDto) {
        SuspiciousIp ip = cardAndIpService.create(ipMapper.toEntity(ipDto));
        ipDto.setId(ip.getId());
        return ResponseEntity.ok(ipDto);
    }

    @DeleteMapping("/api/antifraud/suspicious-ip/{ip}")
    public StatusDto.IpRemovedDto deleteSuspiciousIP(@PathVariable @Valid
                                                         @Pattern(regexp = RegIP.IP_FORMAT,
                                                                 message = "Wrong ip format!")
                                                         String ip) {
        cardAndIpService.deleteSuspiciousIp(ip);
        return new StatusDto.IpRemovedDto(ip);
    }

    @GetMapping("/api/antifraud/suspicious-ip")
    public List<SuspiciousIpDto> showAllSuspiciousIPs() {
        return cardAndIpService.getAllSuspiciousIps().stream()
                .map(ipMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/api/antifraud/stolencard")
    public ResponseEntity<StolenCreditCardDto> addStolenCard(@RequestBody @Valid StolenCreditCardDto cardDto) {
        StolenCreditCard card = cardAndIpService.create(cardMapper.toEntity(cardDto));
        cardDto.setId(card.getId());
        return ResponseEntity.ok(cardDto);
    }

    @DeleteMapping("/api/antifraud/stolencard/{number}")
    public StatusDto.CreditCardRemovedDto deleteStolenCard(@PathVariable @Valid @LuhnCheck String number) {
        cardAndIpService.deleteStolenCreditCard(number);
        return new StatusDto.CreditCardRemovedDto(number);
    }

    @GetMapping("/api/antifraud/stolencard")
    public List<StolenCreditCardDto> showAllStolenCards() {
        return cardAndIpService
                .getAllStolenCards()
                .stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }
}
