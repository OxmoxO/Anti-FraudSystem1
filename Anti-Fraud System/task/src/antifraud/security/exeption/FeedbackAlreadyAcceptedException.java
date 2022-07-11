package antifraud.security.exeption;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Feedback already accepted!")
public class FeedbackAlreadyAcceptedException extends RuntimeException {
}
