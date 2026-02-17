package ch.verno.ui.client.file;

import ch.verno.common.api.dto.internal.file.storage.DownloadFileResponse;
import ch.verno.common.api.dto.internal.file.storage.FileUploadResponse;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.publ.ApiUrl;
import ch.verno.publ.Publ;
import ch.verno.ui.client.BaseApiClient;
import com.vaadin.flow.component.charts.model.Global;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class FileApiClient extends BaseApiClient {

  public FileApiClient(@Nonnull final GlobalInterface globalInterface,
                       @Nonnull final String baseUrl) {
    super(globalInterface, BaseApiClient.build(baseUrl));
  }

  public FileApiClient(@Nonnull final GlobalInterface globalInterface,
                       @Nonnull final RestClient restClient) {
    super(globalInterface, restClient);
  }

  @Nullable
  public FileUploadResponse uploadReportTemplate(@Nonnull final String tenantKey,
                                                 @Nonnull final String filename,
                                                 @Nonnull final String contentType,
                                                 @Nonnull final InputStream inputStream,
                                                 final long size) {

    final var fileBytes = readBytes(inputStream);
    final MultiValueMap<String, Object> multipartBody = createMultipartBody(filename, contentType, fileBytes, size);

    return post(ApiUrl.FILES, Map.of(
                    "X-Mandant", tenantKey
            ),
            MediaType.MULTIPART_FORM_DATA,
            multipartBody)
            .retrieve()
            .body(FileUploadResponse.class);
  }

  @Nullable
  public DownloadFileResponse getReportTemplate(@Nonnull final String tenantKey,
                                                @Nonnull final Long id) {
    return get(ApiUrl.FILES + Publ.SLASH + id, Map.of())
            .retrieve()
            .body(DownloadFileResponse.class);
  }

  public void deleteReportTemplate(@Nonnull final String tenantKey, @Nonnull final Long fileId) {
    delete(ApiUrl.FILES + Publ.SLASH + fileId,
            Map.of())
            .retrieve()
            .toBodilessEntity();
  }

  @Nonnull
  private byte[] readBytes(@Nonnull final InputStream inputStream) {
    try {
      final byte[] bytes = inputStream.readAllBytes();
      if (bytes.length == 0) {
        throw new IllegalArgumentException("File is empty - no empty files are allowed");
      }
      return bytes;
    } catch (IOException e) {
      throw new RuntimeException("Failed to read input stream", e);
    }
  }

  @Nonnull
  private MultiValueMap<String, Object> createMultipartBody(@Nonnull final String filename,
                                                            @Nonnull final String contentType,
                                                            @Nonnull final byte[] fileBytes,
                                                            final long size) {

    final var fileHeaders = new HttpHeaders();
    fileHeaders.setContentType(MediaType.valueOf(contentType));
    fileHeaders.setContentDispositionFormData("file", filename);
    fileHeaders.setContentLength(size);

    final var resource = new ByteArrayResource(fileBytes) {
      @Override
      public String getFilename() {
        return filename;
      }
    };

    final var fileEntity = new HttpEntity<>(resource, fileHeaders);

    final var body = new LinkedMultiValueMap<String, Object>();
    body.add("file", fileEntity);

    return body;
  }

}