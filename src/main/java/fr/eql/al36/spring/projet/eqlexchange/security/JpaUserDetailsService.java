package fr.eql.al36.spring.projet.eqlexchange.security;

import fr.eql.al36.spring.projet.eqlexchange.domain.Authority;
import fr.eql.al36.spring.projet.eqlexchange.domain.User;
import fr.eql.al36.spring.projet.eqlexchange.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Email: " + email + " not found"));
        System.out.println("userrrrrrrrrrrrrrr" + user);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), true,
                                                                      true, true,
                                                                      true,
                                                                      convertToSpringAuthorities(
                                                                              user.getAuthorities()));
    }


    private Collection<SimpleGrantedAuthority> convertToSpringAuthorities(Set<Authority> authorities) {
        if(authorities != null && authorities.size() > 0) {
            return authorities.stream().map(Authority::getRole).map(SimpleGrantedAuthority::new).collect(
                    Collectors.toSet());
        } else {
            return new HashSet<>();
        }
    }

}
