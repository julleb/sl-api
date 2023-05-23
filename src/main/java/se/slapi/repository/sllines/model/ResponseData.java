package se.slapi.repository.sllines.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResponseData<T>(@JsonProperty("Type") String type, @JsonProperty("Result") Collection<T> result) {
}
