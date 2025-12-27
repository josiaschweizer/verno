package ch.verno.ui.verno.security;

import ch.verno.common.util.Publ;
import ch.verno.db.entity.user.AppUserEntity;
import ch.verno.server.repository.AppUserRepository;
import jakarta.annotation.Nonnull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserSeeder implements CommandLineRunner {

  private final AppUserRepository repository;
  private final PasswordEncoder encoder;

  public UserSeeder(@Nonnull final AppUserRepository repository,
                    @Nonnull final PasswordEncoder encoder) {
    this.repository = repository;
    this.encoder = encoder;
  }

  @Override
  public void run(String... args) {
    seedUserIfMissing("admin", "admin1234", "ADMIN");
    seedUserIfMissing("user", "user1234", "USER");
  }

  private void seedUserIfMissing(@Nonnull final String username,
                                 @Nonnull final String rawPassword,
                                 @Nonnull final String role) {
    if (repository.findByUsername(username).isPresent()) {
      return;
    }

    final var encodedPassword = encoder.encode(rawPassword);
    var user = new AppUserEntity(
            username,
            encodedPassword != null ? encodedPassword : Publ.EMPTY_STRING,
            role
    );

    repository.save(user);
  }
}