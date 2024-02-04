package training.quizTdd.infrastructure.persistence;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import training.quizTdd.infrastructure.persistence.entities.AppUserEntity;

public class AppUserDetailsServiceImpl implements UserDetailsService {

    private final AppUserRepository userRepository;

    public AppUserDetailsServiceImpl(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final AppUserEntity appUserEntity = userRepository.findAppUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new AppUserPersistenceAdapter(appUserEntity);

    }
}
