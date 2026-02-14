package ch.verno.ui.client.file;

import ch.verno.common.api.dto.internal.file.storage.FileUploadResponse;
import ch.verno.publ.ApiUrl;
import ch.verno.ui.client.BaseApiClient;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.io.InputStream;

public class FileApiClient extends BaseApiClient {

  public FileApiClient(@Nonnull final String baseUrl) {
    super(BaseApiClient.build(baseUrl));
  }

  public FileApiClient(final RestClient restClient) {
    super(restClient);
  }

  @Nullable
  public FileUploadResponse uploadReportTemplate(@Nonnull final String tenantKey,
                                                 @Nonnull final String filename,
                                                 @Nonnull final String contentType,
                                                 @Nonnull final InputStream inputStream,
                                                 final long size) {
    final MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

    final var fileResource = new InputStreamResource(inputStream) {
      @Override
      public String getFilename() {
        return filename;
      }

      @Override
      public long contentLength() {
        return size;
      }
    };

    body.add("file", fileResource);

    return restClient.post()
            .uri(ApiUrl.FILES)
            .header("X-Mandant", tenantKey)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(body)
            .retrieve()
            .body(FileUploadResponse.class);
  }


}
