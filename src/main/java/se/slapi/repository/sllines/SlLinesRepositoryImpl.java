package se.slapi.repository.sllines;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import se.slapi.repository.exceptions.RepositoryException;
import se.slapi.repository.sllines.model.*;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;

@Repository
@Profile("!slLinesRepositoryMock")
class SlLinesRepositoryImpl implements SlLinesRepository {

    private final static Logger logger = LoggerFactory.getLogger(SlLinesRepositoryImpl.class);

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
        String responseAsString = callApi(url);
        ObjectMapper ob = new ObjectMapper();
        try {
            TypeReference<SlLinesApiResponse<JourneyPatternPointOnLine>> typeRef
                    = new TypeReference<>(){};
            SlLinesApiResponse<JourneyPatternPointOnLine> apiResponse = ob.readValue(responseAsString, typeRef);
            return handleApiResponse(apiResponse);
        } catch (JsonProcessingException e) {
            logger.error("Failed when parsing response from SL Lines API at url=" + url
                    + ". ErrorMessage=" + e.getMessage() + " Response=" + responseAsString, e);
            throw new RepositoryException("Failed to parse json", e);
        }
    }

    @Override
    public Collection<StopPoint> getStopPoints() throws RepositoryException {
        var uriBuilder = createBaseApiUrl(ModelType.STOP);
        String url = uriBuilder.toUriString();
        String responseAsString = callApi(url);
        ObjectMapper ob = new ObjectMapper();

        try {
            TypeReference<SlLinesApiResponse<StopPoint>> typeRef
                    = new TypeReference<>(){};
            SlLinesApiResponse<StopPoint> apiResponse = ob.readValue(responseAsString, typeRef);
            return handleApiResponse(apiResponse);
        } catch (JsonProcessingException e) {
            logger.error("Failed when parsing response from SL Lines API at url=" + url
                    + ". ErrorMessage=" + e.getMessage() + " Response=" + responseAsString, e);
            throw new RepositoryException("Failed to parse response", e);
        }
    }

    private <T> Collection<T> handleApiResponse(SlLinesApiResponse<T> apiResponse) throws RepositoryException {
        if(apiResponse.statusCode() == 0) {
            return apiResponse.responseData().result();
        } else {
            throw new RepositoryException("Error when calling api. Message=" + apiResponse.message());
        }
    }

    private String callApi(String url) throws RepositoryException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate");
        var httpEntity = new HttpEntity<>(headers);
        try {
            var response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
            String body = response.getBody();
            String bodyAsUtf8 = new String(body.getBytes(StandardCharsets.UTF_8));
            return bodyAsUtf8;
        } catch(RestClientException e) {
            throw new RepositoryException(e.getMessage(), e);
        }
    }

    private UriComponentsBuilder createBaseApiUrl(ModelType modelType) {
        return UriComponentsBuilder.fromHttpUrl(SL_API_URL)
                .queryParam("key", API_KEY)
                .queryParam("model", modelType.toString());
    }
}
