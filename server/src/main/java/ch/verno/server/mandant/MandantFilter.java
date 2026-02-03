package ch.verno.server.mandant;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class MandantFilter extends OncePerRequestFilter {

  private final MandantProperties props;
  private final MandantResolver resolver;

  public MandantFilter(final MandantProperties props, final MandantResolver resolver) {
    this.props = props;
    this.resolver = resolver;
  }

  @Override
  protected void doFilterInternal(@Nonnull final HttpServletRequest request,
                                  @Nonnull final HttpServletResponse response,
                                  @Nonnull final FilterChain filterChain) throws ServletException, IOException {
    if (!props.isEnabled()) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      final var mandantId = resolver.resolveMandantId(request).orElseThrow(() -> new MandantNotResolvedException("Mandant could not be resolved"));
      MandantContext.set(mandantId);
      filterChain.doFilter(request, response);
    } finally {
      MandantContext.clear();
    }
  }
}