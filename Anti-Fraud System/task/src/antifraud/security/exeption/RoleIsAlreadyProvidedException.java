package antifraud.security.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Excessive role")
public class RoleIsAlreadyProvidedException extends RuntimeException{ }
