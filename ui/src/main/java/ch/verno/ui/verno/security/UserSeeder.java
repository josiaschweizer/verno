package ch.verno.ui.verno.security;

import ch.verno.common.db.dto.AppUserDto;
import ch.verno.common.db.service.IAppUserService;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserSeeder implements CommandLineRunner {

  @Nonnull private final IAppUserService appUserService;
  @Nonnull private final PasswordEncoder encoder;

  public UserSeeder(@Nonnull final IAppUserService appUserService,
                    @Nonnull final PasswordEncoder encoder) {
    this.appUserService = appUserService;
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
    if (appUserService.findByUserName(username).isPresent()) {
      return;
    }

    final var encodedPassword = encoder.encode(rawPassword);
    var user = new AppUserDto(
            username,
            encodedPassword != null ? encodedPassword : Publ.EMPTY_STRING,
            role
    );

    appUserService.save(user);
  }
}