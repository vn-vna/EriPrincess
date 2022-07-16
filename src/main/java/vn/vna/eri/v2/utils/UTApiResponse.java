package vn.vna.eri.v2.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public class UTApiResponse {

  public static final Integer STATUS_OK = 200;
  public static final Integer STATUS_NOTFOUND = 404;

  public static final String HEADER_CONTENT_TYPE = "Content-Type";

  public static final String CONTENT_TYPE_JSON = "application/json";

  public static ResponseEntity<String> responseString(Integer status, String str) {
    HttpHeaders headers = new HttpHeaders();
    headers.add(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON);

    return ResponseEntity
        .status(status)
        .headers(headers)
        .body(str);
  }

  public static ResponseEntity<String> responseJson(Integer status, UTJsonClass obj) {
    return UTApiResponse.responseString(status, obj.toJson());
  }
}
