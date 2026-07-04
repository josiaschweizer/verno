package ch.verno.server.service.entity.tenant;

import ch.verno.contract.dto.table.tenant.TenantDto;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.lib.Lazy;
import ch.verno.lib.New;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.tenant.TenantMapper;
import ch.verno.server.repository.tenant.TenantRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TenantService {

  @Nonnull private final Lazy<TenantMapper> tenantMapper;
  @Nonnull private final Lazy<TenantRepository> tenantRepository;

  @Nonnull private final Map<String, Long> cacheBySlug;
  @Nonnull private final Map<String, Long> cacheByName;

  public TenantService(@Nonnull final ServerBean bean) {
    this.tenantMapper = Lazy.of(() -> bean.get(TenantMapper.class));
    this.tenantRepository = Lazy.of(() -> bean.get(TenantRepository.class));
    this.cacheBySlug = New.concurrentHashMap();
    this.cacheByName = New.concurrentHashMap();
  }

  @Nonnull
  public Optional<Long> findTenantIdBySlug(@Nonnull final String slug) {
    final var key = slug.trim().toLowerCase();
    final var cached = cacheBySlug.get(key);

    if (cached != null) {
      return Optional.of(cached);
    }

    final var id = tenantRepository.get().findIdBySlug(key);
    id.ifPresent(value -> cacheBySlug.put(key, value));
    return id;
  }

  @Nonnull
  public Optional<Long> findTenantIdByName(@Nonnull final String name) {
    final var key = name.trim();
    final var cached = cacheByName.get(key);

    if (cached != null) {
      return Optional.of(cached);
    }

    final var id = tenantRepository.get().findIdByName(key);
    id.ifPresent(value -> cacheByName.put(key, value));
    return id;
  }

  @Nonnull
  public Optional<TenantDto> findById(@Nonnull final Long id) {
    return tenantRepository.get().findById(id)
            .map(entity -> tenantMapper.get().toDto(entity));
  }

  @Nonnull
  public List<TenantDto> findAllTenants() {
    return tenantRepository.get().findAll().stream()
            .map(entity -> tenantMapper.get().toDto(entity))
            .toList();
  }

  public boolean existsBySlug(@Nonnull final String slug) {
    final var key = slug.trim().toLowerCase();
    final var cached = cacheBySlug.get(key);

    if (cached != null) {
      return true;
    }

    return tenantRepository.get().existsBySlug(slug);
  }

  @Nonnull
  public TenantDto create(@Nonnull final TenantDto dto) {
    TenantEntity entity = tenantMapper.get().toNewEntity(dto);
    entity = tenantRepository.get().save(entity);
    return tenantMapper.get().toDto(entity);
  }

  @Nonnull
  public Long countTenants() {
    return tenantRepository.get().count();
  }

  @Nonnull
  public Long nextId() {
    return tenantRepository.get().nextId();
  }

  public void evictSlug(@Nonnull final String slug) {
    cacheBySlug.remove(slug.trim().toLowerCase());
  }

  public void clearCache() {
    cacheBySlug.clear();
    cacheByName.clear();
  }
}