package antifraud.service;


import antifraud.dao.UserDao;
import antifraud.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserInfoDetailsService implements UserDetailsService {
    @Autowired
    UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username)
                                          throws UsernameNotFoundException {
        User user = userDao
                           .findByUsernameIgnoreCase(username)
                           .orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException("Not found: " + username);
        }

        return new UserInfoDetails(user);
    }
}
