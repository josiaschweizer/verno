package ch.verno.server.tenant;

import ch.verno.common.db.dto.table.TenantDto;
import ch.verno.common.server.service.intern.ITenantService;
import ch.verno.server.mapper.TenantMapper;
import ch.verno.server.repository.TenantRepository;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class TenantService implements ITenantService {

  @Nonnull private final TenantRepository tenantRepository;

  @Nonnull private final Map<String, Long> cacheBySlug;
  @Nonnull private final Map<String, Long> cacheByName;

  public TenantService(@Nonnull final TenantRepository tenantRepository) {
    this.tenantRepository = tenantRepository;

    this.cacheBySlug = new ConcurrentHashMap<>();
    this.cacheByName = new ConcurrentHashMap<>();
  }

  @Nonnull
  @Override
  public Optional<Long> findTenantIdBySlug(@Nonnull final String slug) {
    final var key = slug.trim().toLowerCase();
    final var cached = cacheBySlug.get(key);
    if (cached != null) {
      return Optional.of(cached);
    }

    final var idOpt = tenantRepository.findIdBySlug(key);
    idOpt.ifPresent(id -> cacheBySlug.put(key, id));
    return idOpt;
  }

  @Nonnull
  @Override
  public Optional<Long> findTenantIdByName(@Nonnull final String name) {
    final var cached = cacheByName.get(name);
    if (cached != null) {
      return Optional.of(cached);
    }

    final var idOpt = tenantRepository.findIdByName(name);
    idOpt.ifPresent(id -> cacheByName.put(name, id));
    return idOpt;
  }

  @Nonnull
  @Override
  public Optional<TenantDto> findById(@Nonnull final Long id) {
    return tenantRepository.findById(id)
            .map(TenantMapper::toDto);
  }

  @Nonnull
  @Override
  public List<TenantDto> findAllTenants() {
    return tenantRepository.findAll().stream()
            .map(TenantMapper::toDto)
            .toList();
  }

  public void evictSlug(@Nonnull final String slug) {
    cacheBySlug.remove(slug.trim().toLowerCase());
  }

  public void clearCache() {
    cacheBySlug.clear();
  }
}