package antifraud.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class StatusDto {


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String username;

    @NonNull
    private String status;

    public static class UserLockedDto extends StatusDto {
        public UserLockedDto(String username) {
            super(String.format("User %s locked!", username));
        }
    }

    public static class UserUnlockedDto extends StatusDto {

        public UserUnlockedDto(String username) {
            super(String.format("User %s unlocked!", username));
        }
    }

    public  static class IpRemovedDto extends StatusDto {

        public IpRemovedDto(String ip) {
            super(String.format("IP %s successfully removed!", ip));
        }
    }

    public static class CreditCardRemovedDto extends StatusDto {

        public CreditCardRemovedDto(String number) {
            super(String.format("Card %s successfully removed!", number));
        }
    }


}