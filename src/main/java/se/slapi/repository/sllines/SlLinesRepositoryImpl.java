package se.slapi.repository.sllines;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import se.slapi.repository.exceptions.RepositoryException;
import se.slapi.repository.sllines.model.*;

import java.util.Collection;
import java.util.Objects;

@Repository
@Profile("!slLinesRepositoryMock")
class SlLinesRepositoryImpl implements SlLinesRepository {


    @Value("${se.slapi.sllines.api.url}")
    private String SL_API_URL;

    @Value("${se.slapi.sllines.api.key}")
    private String API_KEY;

    private RestTemplate restTemplate;

    @Autowired
    SlLinesRepositoryImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    SlLinesRepositoryImpl(RestTemplate restTemplate, String apiUrl, String apiKey) {
        this.restTemplate = restTemplate;
        this.SL_API_URL = apiUrl;
        this.API_KEY = apiKey;
    }

    @Override
    public Collection<JourneyPatternPointOnLine> getListOfJourneyPatternPointOnLine(TransportModeCode transportModeCode) throws RepositoryException {
        Objects.requireNonNull(transportModeCode);
        var uriBuilder = createBaseApiUrl(ModelType.JOURNEY_PATTERN_POINT_ON_LINE);
        if(transportModeCode != TransportModeCode.ALL) {
            uriBuilder.queryParam("DefaultTransportModeCode", transportModeCode);
        }
        String url = uriBuilder.toUriString();
        var response = restTemplate.getForEntity(url, String.class);
        String responseAsString = response.getBody();
        //TODO hantera http errors
        ObjectMapper ob = new ObjectMapper();
        try {
            TypeReference<SlLinesApiResponse<JourneyPatternPointOnLine>> typeRef
                    = new TypeReference<SlLinesApiResponse<JourneyPatternPointOnLine>>(){};
            SlLinesApiResponse<JourneyPatternPointOnLine> apiResponse = ob.readValue(responseAsString, typeRef);
            return apiResponse.responseData().result();
        } catch (JsonProcessingException e) {
            throw new RepositoryException("Failed to parse json", e);
        }
    }

    @Override
    public Collection<StopPoint> getStopPoints() throws RepositoryException {
        var uriBuilder = createBaseApiUrl(ModelType.STOP);
        String url = uriBuilder.toUriString();
        var response = restTemplate.getForEntity(url, String.class);
        String responseAsString = response.getBody();
        ObjectMapper ob = new ObjectMapper();
        try {
            TypeReference<SlLinesApiResponse<StopPoint>> typeRef
                    = new TypeReference<SlLinesApiResponse<StopPoint>>(){};
            SlLinesApiResponse<StopPoint> apiResponse = ob.readValue(responseAsString, typeRef);
            return apiResponse.responseData().result();
        } catch (JsonProcessingException e) {
            throw new RepositoryException("Failed to parse json", e);
        }
    }

    private UriComponentsBuilder createBaseApiUrl(ModelType modelType) {
        return UriComponentsBuilder.fromHttpUrl(SL_API_URL)
                .queryParam("key", API_KEY)
                .queryParam("model", modelType.toString());
    }
}
