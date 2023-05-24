package se.slapi.repository.sllines.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SlLinesApiResponse<T>(@JsonProperty("StatusCode") int statusCode,
                                    @JsonProperty("Message") String message,
                                    @JsonProperty("ResponseData") ResponseData<T> responseData) {
}
