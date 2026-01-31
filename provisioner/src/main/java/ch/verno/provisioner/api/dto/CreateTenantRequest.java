package ch.verno.provisioner.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateTenantRequest(

        @NotBlank
        @Pattern(regexp = "^[a-z0-9][a-z0-9-]{1,61}[a-z0-9]$", message = "tenantKey must be dns-safe (lowercase, digits, hyphen)")
        String tenantKey,

        @NotBlank
        @Size(max = 255)
        String tenantName,

        @NotBlank
        @Pattern(regexp = "^[a-z0-9][a-z0-9-]{1,61}[a-z0-9]$", message = "subdomain must be dns-safe (lowercase, digits, hyphen)")
        String subdomain,

        @NotBlank
        @Size(max = 8)
        String preferredLanguage,

        @NotBlank
        @Size(max = 255)
        String adminEmail,

        @NotBlank
        @Size(max = 255)
        String adminDisplayName,

        @NotBlank
        @Size(min = 8, max = 200)
        String adminPassword
) {}