package vn.vna.erivampir.utilities;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vna.erivampir.EriServerConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SauceNAOSearcher {

    public static final String SAUCENAO_SEARCH_URL  = "https://saucenao.com/search.php?";
    public static final String SAUCENAO_SEARCH_DB   = "999";
    public static final String SAUCENAO_OUTPUT_TYPE = "2";
    public static final String SAUCENAO_TESTMODE    = "1";
    public static final String SAUCENAO_NUM_RESULTS = "10";
    public static final Logger logger               = LoggerFactory.getLogger(SauceNAOSearcher.class);

    public static String createAPIUrl(String imgURL) {
        String              url;
        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("db", SAUCENAO_SEARCH_DB);
        reqParams.put("output_type", SAUCENAO_OUTPUT_TYPE);
        reqParams.put("testmode", SAUCENAO_TESTMODE);
        reqParams.put("numres", SAUCENAO_NUM_RESULTS);
        reqParams.put("api_key", EriServerConfig.getInstance().getConfiguration(EriServerConfig.CFG_SAUCENAO_APIKEY));
        reqParams.put("url", imgURL);

        url = reqParams
            .keySet()
            .stream()
            .map((key) -> key + "=" + URLEncoder.encode(reqParams.get(key), StandardCharsets.UTF_8))
            .collect(Collectors.joining("&", SAUCENAO_SEARCH_URL, ""));

        return url;
    }

    public static SauceNAOResult parseResultFromJSON(JSONObject jsonObject) {
        SauceNAOResult result = new SauceNAOResult();

        return result;
    }

    public static SauceNAOResult requestSauceNAOSearch(String imgURL) {
        SauceNAOResult result = null;
        try {
            String url = createAPIUrl(imgURL);
            logger.info("Request to SauceNAO to find image: " + url);
            InputStream    inputStream   = new URL(url).openStream();
            BufferedReader br            = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder  stringBuilder = new StringBuilder();
            String         tmp;
            while (!Objects.isNull(tmp = br.readLine())) {
                stringBuilder.append(tmp).append("\n");
            }
            br.close();
            Gson gson = new Gson();
            result = gson.fromJson(stringBuilder.toString(), SauceNAOResult.class);
        } catch (IOException ioex) {
            logger.error("Open connection to fetch API from SauceNAO failed");
        }

        return result;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SauceNAOResult {
        private SauceNAOResultHeader     header;
        private List<SauceNAOResultData> results;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SauceNAOResultHeader {

            @SerializedName("account_type")
            private String                   accountType;
            @SerializedName("index")
            private Map<String, HeaderIndex> index;
            @SerializedName("long_limit")
            private String                   longLimit;
            @SerializedName("long_remaining")
            private Integer                  longRemaining;
            @SerializedName("minimum_similarity")
            private Float                    minimumSimilarity;
            @SerializedName("query_image")
            private String                   queryImage;
            @SerializedName("query_image_display")
            private String                   queryImageDisplay;
            @SerializedName("results_requested")
            private Integer                  resultsRequested;
            @SerializedName("results_returned")
            private Integer                  resultsReturned;
            @SerializedName("search_depth")
            private String                   searchDepth;
            @SerializedName("short_limit")
            private String                   shortLimit;
            @SerializedName("short_remaining")
            private Integer                  shortRemaining;
            @SerializedName("status")
            private Integer                  status;
            @SerializedName("user_id")
            private Integer                  userId;

            @Getter
            @Setter
            @NoArgsConstructor
            @AllArgsConstructor
            public static class HeaderIndex {
                @SerializedName("status")
                public Integer status;
                @SerializedName("parent_id")
                public Integer parentId;
                @SerializedName("id")
                public Integer id;
                @SerializedName("results")
                public Integer results;
            }
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SauceNAOResultData {

            @SerializedName("header")
            SauceNAOResultDataHeader header;
            @SerializedName("data")
            SauceNAOResultDataBody   data;

            @Getter
            @Setter
            @NoArgsConstructor
            @AllArgsConstructor
            public static class SauceNAOResultDataHeader {
                @SerializedName("dupes")
                public Integer dupes;
                @SerializedName("hidden")
                public Integer hidden;
                @SerializedName("index_id")
                public Integer indexId;
                @SerializedName("index_name")
                public String  indexName;
                @SerializedName("similarity")
                public String  similarity;
                @SerializedName("thumbnail")
                public String  thumbnail;
            }

            @Getter
            @Setter
            @NoArgsConstructor
            @AllArgsConstructor
            public static class SauceNAOResultDataBody {
                @SerializedName("title")
                public String       title;     // Art title
                @SerializedName("ext_urls")
                public List<String> extUrls;   // Result URL

                @SerializedName("author_name")
                public String authorName;      // Deviant Art
                @SerializedName("author_url")
                public String authorUrl;       // Deviant Art
                @SerializedName("da_id")
                public String daId;            // Deviant Art

                @SerializedName("member_id")
                public Integer memberId;       // Pixiv
                @SerializedName("member_name")
                public String  memberName;     // Pixiv
                @SerializedName("pixiv_id")
                public Integer pixivId;        // pixiv
            }
        }

    }

}
