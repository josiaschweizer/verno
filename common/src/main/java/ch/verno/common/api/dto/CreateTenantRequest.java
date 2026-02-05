package ch.verno.common.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateTenantRequest(
        @NotBlank @Size(max = 128) String tenantName,

        @NotBlank
        @Size(min = 3, max = 64)
        @Pattern(regexp = "^[a-z0-9][a-z0-9-]{2,63}$")
        String tenantKey,

        @NotBlank
        @Size(min = 3, max = 64)
        @Pattern(regexp = "^[a-z0-9][a-z0-9-]{2,63}$")
        String subdomain,

        @NotBlank
        @Pattern(regexp = "^(de|en|fr)$")
        String preferredLanguage,

        @NotBlank @Email @Size(max = 255) String adminEmail,
        @NotBlank @Size(max = 128) String adminDisplayName,
        @NotBlank @Size(min = 8, max = 128) String adminPassword
) {}