package ch.verno.contract.dto.file.storage;

public record DownloadFileResponse(
        byte[] bytes,
        String filename,
        String contentType,
        long size,
        String checksumSha256
) {}