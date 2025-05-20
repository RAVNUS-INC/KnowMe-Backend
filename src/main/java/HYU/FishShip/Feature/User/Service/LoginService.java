package HYU.FishShip.Feature.User.Service;

import HYU.FishShip.Core.Entity.Role;
import HYU.FishShip.Core.Entity.User;
import HYU.FishShip.Core.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(loginId);

        Role role = user.getRole();

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role.name()));

        // user.getPassword()는 이미 암호화된 상태여야 함!
        return new org.springframework.security.core.userdetails.User(
                user.getLoginId(),
                user.getPassword(), // 암호화된 비번
                authorities
        );
    }
}
