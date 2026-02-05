package ch.verno.ui.verno.security;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;

@Component
@Profile("dev")
public class SecurityContextHolderConfig {

  private static final Logger LOG = LoggerFactory.getLogger(SecurityContextHolderConfig.class);

  @PostConstruct
  public void init() {
    LOG.debug("Setting SecurityContextHolder strategy to MODE_INHERITABLETHREADLOCAL");
    SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
  }
}
