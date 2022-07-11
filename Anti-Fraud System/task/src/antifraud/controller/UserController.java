package antifraud.controller;


import antifraud.model.dto.ChangeRoleDto;
import antifraud.model.dto.OperationDto;
import antifraud.model.dto.StatusDto;
import antifraud.model.dto.UserDto;
import antifraud.model.entity.User;
import antifraud.model.enums.Operation;
import antifraud.model.enums.Role;
import antifraud.model.mapper.UserConverterMapper;
import antifraud.security.exeption.BadRequestException;
import antifraud.security.exeption.RoleIsAlreadyProvidedException;
import antifraud.service.CardAndIpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
public class UserController {
        private final CardAndIpService cardAndIpService;
        private final UserConverterMapper userMapper;
        private final PasswordEncoder passwordEncoder;


        @Autowired
        public UserController(CardAndIpService cardAndIpService,
                              UserConverterMapper userMapper,
                              PasswordEncoder passwordEncoder) {

            this.cardAndIpService = cardAndIpService;
            this.userMapper = userMapper;
            this.passwordEncoder = passwordEncoder;
        }


        @PostMapping("/api/auth/user")
        public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto) {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            User newUser = cardAndIpService.create(userMapper.toEntity(userDto));
            return new ResponseEntity<>(userMapper.toDto(newUser),
                                                HttpStatus.CREATED);
        }

        @GetMapping("/api/auth/list")
        public List<UserDto> getAllUsers() {
            return cardAndIpService
                                    .getAllUsers()
                                    .stream()
                                    .map(userMapper::toDto)
                                    .collect(Collectors.toList());
        }

        @DeleteMapping("/api/auth/user/{username}")
        public StatusDto deleteUser(@PathVariable String username) {
            cardAndIpService.delete(username);
            return new StatusDto(username, "Deleted successfully!");
        }

        @PutMapping("/api/auth/role")
        public UserDto updateUserRole(@RequestBody @Valid ChangeRoleDto changeRoleDto) {
            User user = cardAndIpService.findUser(changeRoleDto.getUsername());
            Role newRole = Role.valueOf(changeRoleDto.getRole());
            if (!List.of(Role.MERCHANT, Role.SUPPORT).contains(newRole)) {
                        throw new BadRequestException("Operation not allowed!");
            }

            if (user.getRole() == newRole) {
                throw new RoleIsAlreadyProvidedException();
            }
            user.setRole(newRole);
            cardAndIpService.update(user);
            return userMapper.toDto(user);
        }

        @PutMapping("/api/auth/access")
        public StatusDto lockUnlockUser(@RequestBody @Valid OperationDto userOperationDto) {
            String username = userOperationDto.getUsername();
            User user = cardAndIpService.findUser(username);

            if (user.getRole() == Role.ADMINISTRATOR) {
                throw new BadRequestException("Operation not allowed!");
            }

            Operation operation = Operation.valueOf(userOperationDto.getOperation());
            user.setLocked(operation == Operation.LOCK);
            cardAndIpService.update(user);
            return operation == Operation.LOCK ?
                    new StatusDto.UserLockedDto(username) :
                    new StatusDto.UserUnlockedDto(username);
        }
}