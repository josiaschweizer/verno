package ch.verno.server.properties.application;

import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ConfigurationProperties(prefix = "verno.application")
public class ApplicationProperties {

    @Nullable
    private String runMode;

    @Nonnull
    public String getRunMode() {
        return Optional.ofNullable(runMode).orElse(Publ.EMPTY_STRING);
    }

    public void setRunMode(@Nullable final String runMode) {
        this.runMode = runMode;
    }
}
