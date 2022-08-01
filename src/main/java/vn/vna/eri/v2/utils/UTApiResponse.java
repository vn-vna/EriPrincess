package vn.vna.eri.v2.utils;

import java.util.Objects;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import vn.vna.eri.v2.utils.helper.ResponseCode;

public class UTApiResponse {

  public static final String HEADER_CONTENT_TYPE = "Content-Type";

  public static final String CONTENT_TYPE_JSON = "application/json";

  public static ResponseEntity<String> responseString(ResponseCode status, String str) {
    HttpHeaders headers = new HttpHeaders();
    headers.add(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON);

    return ResponseEntity.status(status.getCode()).headers(headers).body(str);
  }

  public static ResponseEntity<String> responseJson(ResponseCode status, UTJsonClass obj) {
    return UTApiResponse.responseString(status, Objects.nonNull(obj) ? obj.toJson() : "");
  }
}
