package se.slapi.repository.sllines.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JourneyPatternPointOnLine(@JsonProperty("LineNumber") int lineNumber,
                                        @JsonProperty("DirectionCode") int directionCode,
                                        @JsonProperty("JourneyPatternPointNumber") int journeyPatternPointNumber) {
}
