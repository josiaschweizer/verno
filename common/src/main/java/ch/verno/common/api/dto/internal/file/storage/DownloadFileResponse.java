package ch.verno.common.api.dto.internal.file.storage;

public record DownloadFileResponse(
        byte[] bytes,
        String filename,
        String contentType,
        long size,
        String checksumSha256
) {}