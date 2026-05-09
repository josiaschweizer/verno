package ch.verno.common.gate;

import ch.verno.common.db.dto.table.TenantDto;
import ch.verno.common.db.service.intern.ITenantService;
import ch.verno.common.gate.properties.EnvProperties;
import ch.verno.common.gate.properties.UserProperties;
import ch.verno.common.tenant.TenantContext;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.i18n.I18NProvider;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class GlobalInterface {

  private static ApplicationContext context;

  @Nonnull private final ConcurrentMap<Class<?>, Object> cached;

  @Autowired
  public GlobalInterface(@Nonnull final ApplicationContext applicationContext) {
    context = applicationContext;

    cached = new ConcurrentHashMap<>();
  }

  @Nonnull
  public static GlobalInterface getInstance() {
    return context.getBean(GlobalInterface.class);
  }

  @Nonnull
  public <T> T getGate(@Nonnull final Class<T> serviceClass) {
    return context.getBean(serviceClass);
  }

  @Nonnull
  public <T> T getService(@Nonnull final Class<T> serviceClass) {
    return context.getBean(serviceClass);
  }

  @Nonnull
  public Locale getLocale() {
    final var ui = UI.getCurrent();
    return ui != null ? ui.getLocale() : Locale.getDefault();
  }

  @Nonnull
  public I18NProvider getI18NProvider() {
    return context.getBean(I18NProvider.class);
  }

  @Nullable
  public TenantDto resolveTenant() {
    final var tenantService = getService(ITenantService.class);
    final var tenantId = TenantContext.get();

    if (tenantId != null) {
      return tenantService.findById(tenantId).orElse(null);
    }

    return null;
  }

  @Nonnull
  public PasswordEncoder getPasswordEncoder() {
    return context.getBean(PasswordEncoder.class);
  }


  @Nonnull
  public EnvProperties getEnvProperties() {
    return (EnvProperties) cached.computeIfAbsent(
            EnvProperties.class,
            cls -> context.getBean(EnvProperties.class)
    );
  }

  @Nonnull
  public UserProperties getUserProperties() {
    return (UserProperties) cached.computeIfAbsent(
            UserProperties.class,
            cls -> context.getBean(UserProperties.class)
    );
  }
}
