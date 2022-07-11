package antifraud.security.exeption;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Transaction not found!")
public class TransactionNotFoundException extends RuntimeException{
}
