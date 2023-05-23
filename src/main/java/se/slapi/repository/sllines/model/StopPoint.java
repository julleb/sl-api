package se.slapi.repository.sllines.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StopPoint(@JsonProperty("StopPointNumber") int id, @JsonProperty("StopPointName") String name) {
}
