package ch.verno.ui.client.file;

import ch.verno.contract.dto.file.storage.api.DownloadFileResponse;
import ch.verno.contract.dto.file.storage.api.FileUploadResponse;
import ch.verno.contract.endpoint.properties.api.ApiConfigResource;
import ch.verno.contract.gateway.ApiUrl;
import ch.verno.lib.New;
import ch.verno.lib.Publ;
import ch.verno.rpc.rpc.RpcFactory;
import ch.verno.ui.client.BaseApiClient;
import com.google.inject.Inject;
import com.google.inject.Injector;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class FileApiClient extends BaseApiClient {

  @Inject
  public FileApiClient(@Nonnull final Injector injector) {
    this(injector, getBasicAuthRequest(injector));
  }

  public FileApiClient(@Nonnull final Injector injector,
                       @Nonnull final BasicAuthRequest basicAuthRequest) {
    super(injector, BaseApiClient.buildWithBasicAuth(basicAuthRequest), basicAuthRequest);
  }

  @Nullable
  public FileUploadResponse uploadReportTemplate(@Nonnull final String tenantKey, //TODO delete tenant key?
                                                 @Nonnull final String filename,
                                                 @Nonnull final String contentType,
                                                 @Nonnull final InputStream inputStream,
                                                 final long size) {

    final var fileBytes = readBytes(inputStream);
    final var multipartBody = createMultipartBody(filename, contentType, fileBytes, size);

    return post(ApiUrl.FILES, New.map(), MediaType.MULTIPART_FORM_DATA, multipartBody)
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
      throw new RuntimeException("Failed to read input byteData", e);
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

  @Nonnull
  private static BasicAuthRequest getBasicAuthRequest(@Nonnull final Injector injector) {
    final var apiProperties = injector.getInstance(RpcFactory.class).create(ApiConfigResource.class);
    final var url = apiProperties.getBaseUrl();
    final var username = apiProperties.getApiUsername();
    final var password = apiProperties.getApiPassword();
    return new BasicAuthRequest(url, username, password);
  }

}