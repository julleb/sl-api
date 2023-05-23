package se.slapi.repository.sllines;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import se.slapi.repository.exceptions.RepositoryException;
import se.slapi.repository.sllines.model.JourneyPatternPointOnLine;
import se.slapi.repository.sllines.model.ModelType;
import se.slapi.repository.sllines.model.SlLinesApiResponse;
import se.slapi.repository.sllines.model.TransportModeCode;

import java.util.Collection;
import java.util.Objects;

@Repository
@Profile("!slLinesRepositoryMock")
public class SlLinesRepositoryImpl implements SlLinesRepository {
    

    private static String SL_API_URL = "https://api.sl.se/api2/LineData.json";

    private static String API_KEY = "xxx"; //should be injected as env-var

    private RestTemplate restTemplate;


    //TODO autowire resttemplate
    SlLinesRepositoryImpl() {
        restTemplate = new RestTemplate();
    }

    @Override
    public Collection<JourneyPatternPointOnLine> getListOfJourneyPatternPointOnLine(TransportModeCode transportModeCode) throws RepositoryException {
        Objects.requireNonNull(transportModeCode);
        var uriBuilder = UriComponentsBuilder.fromHttpUrl(SL_API_URL)
                .queryParam("key", API_KEY)
                .queryParam("model", ModelType.JOURNEY_PATTERN_POINT_ON_LINE.toString());
        if(transportModeCode != TransportModeCode.ALL) {
            uriBuilder.queryParam("DefaultTransportModeCode", transportModeCode);
        }
        String url = uriBuilder.toUriString();
        System.out.println(url);
        String responseAsString = getJsonTest();
        //var responseAsString = restTemplate.getForEntity(url, String.class);
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

    private String getJsonTest() {
       return """
                {
                  "StatusCode": 0,
                  "Message": null,
                  "ExecutionTime": 482,
                  "ResponseData": {
                    "Version": "2023-05-22 00:12",
                    "Type": "JourneyPatternPointOnLine",
                    "Result": [
                      {
                        "LineNumber": "1",
                        "DirectionCode": "1",
                        "JourneyPatternPointNumber": "10008",
                        "LastModifiedUtcDateTime": "2022-02-15 00:00:00.000",
                        "ExistsFromDate": "2022-02-15 00:00:00.000"
                      },
                      {
                        "LineNumber": "1",
                        "DirectionCode": "1",
                        "JourneyPatternPointNumber": "10012",
                        "LastModifiedUtcDateTime": "2023-03-07 00:00:00.000",
                        "ExistsFromDate": "2023-03-07 00:00:00.000"
                      }
                    ]
                  }
                }
                """;
    }
}
