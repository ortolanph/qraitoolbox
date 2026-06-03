package pt.pauloortolan.qraitoolbox.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ApodResponse(
        String date,
        String title,
        String url,
        String hdUrl,
        @JsonProperty("media_type") String mediaType,
        String copyright,
        @JsonProperty("service_version") String serviceVersion) {
}
