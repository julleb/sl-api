package se.slapi.repository.sllines;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import se.slapi.repository.exceptions.RepositoryException;
import se.slapi.repository.sllines.model.JourneyPatternPointOnLine;
import se.slapi.repository.sllines.model.StopPoint;
import se.slapi.repository.sllines.model.TransportModeCode;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SLLinesRepositoryImplTest {

    private SlLinesRepositoryImpl slLinesRepository;

    private RestTemplate mockedRestTemplate;

    private static String API_KEY = "xxx";
    private static String API_URL = "https://slapi.se/lines";

    @BeforeEach
    void setup() {
        mockedRestTemplate = Mockito.mock(RestTemplate.class);
        slLinesRepository = new SlLinesRepositoryImpl(mockedRestTemplate, API_URL, API_KEY);
    }

    @Test
    void testListOfJourneyPatternPointOnLine() throws RepositoryException {
        ResponseEntity<String> responseEntity = new ResponseEntity<>(getJourneyPatternPointOnLineJson(), HttpStatusCode.valueOf(200));
        Mockito.when(mockedRestTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(String.class))).thenReturn(responseEntity);
        Collection<JourneyPatternPointOnLine> listOfJourneyPatternPointOnLine = slLinesRepository.getListOfJourneyPatternPointOnLine(TransportModeCode.BUS);
        assertEquals(2, listOfJourneyPatternPointOnLine.size());
        var asList = listOfJourneyPatternPointOnLine.stream().toList();
        assertEquals(1, asList.get(0).lineNumber());
        assertEquals(10008, asList.get(0).journeyPatternPointNumber());
    }

    @Test
    void testListOfJourneyPatternPointOnLineWhenStatusIsNotOk() {
        String json = """
                {
                  "StatusCode": 1337,
                  "Message": "something bad"
                }
                """;
        ResponseEntity<String> responseEntity = new ResponseEntity<>(json, HttpStatusCode.valueOf(200));
        Mockito.when(mockedRestTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(String.class))).thenReturn(responseEntity);
        assertThrows(RepositoryException.class, () -> slLinesRepository.getListOfJourneyPatternPointOnLine(TransportModeCode.BUS));
    }

    @Test
    void testListOfJourneyPatternPointOnLineWhenHttpError() {
        Mockito.when(mockedRestTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(String.class))).thenThrow(RestClientException.class);
        assertThrows(RepositoryException.class, () -> slLinesRepository.getListOfJourneyPatternPointOnLine(TransportModeCode.BUS));
    }

    @Test
    void testGetStopPoints() throws RepositoryException {
        ResponseEntity<String> responseEntity = new ResponseEntity<>(getStopPointsJson(), HttpStatusCode.valueOf(200));
        Mockito.when(mockedRestTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(String.class))).thenReturn(responseEntity);
        Collection<StopPoint> stopPoints =  slLinesRepository.getStopPoints();
        assertEquals(1, stopPoints.size());
        assertEquals(10001, stopPoints.stream().toList().get(0).id());
        assertEquals("Stadshagsplan", stopPoints.stream().toList().get(0).name());
    }

    private String getStopPointsJson() {
        return """
                {
                "StatusCode":0,
                "Message":null,
                "ExecutionTime":720,
                "ResponseData":
                    {
                        "Version":"2023-05-23 00:12",
                        "Type":"StopPoint",
                        "Result":[
                            {
                                "StopPointNumber":"10001",
                                "StopPointName":"Stadshagsplan",
                                "StopAreaNumber":"10001",
                                "LocationNorthingCoordinate":"59.3373571967995",
                                "LocationEastingCoordinate":"18.0214674159693",
                                "ZoneShortName":"A","StopAreaTypeCode":"BUSTERM",
                                "LastModifiedUtcDateTime":"2022-10-28 00:00:00.000",
                                "ExistsFromDate":"2022-10-28 00:00:00.000"
                            }
                        ]
                    }
                }
                """;
    }

    private String getJourneyPatternPointOnLineJson() {
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
