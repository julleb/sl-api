package se.slapi.repository.sllines;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import se.slapi.repository.exceptions.RepositoryException;
import se.slapi.repository.sllines.model.JourneyPatternPointOnLine;
import se.slapi.repository.sllines.model.TransportModeCode;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        Mockito.when(mockedRestTemplate.getForEntity(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(responseEntity);
        Collection<JourneyPatternPointOnLine> listOfJourneyPatternPointOnLine = slLinesRepository.getListOfJourneyPatternPointOnLine(TransportModeCode.BUS);
        assertEquals(2, listOfJourneyPatternPointOnLine.size());
        var asList = listOfJourneyPatternPointOnLine.stream().toList();
        assertEquals(1, asList.get(0).lineNumber());
        assertEquals(10008, asList.get(0).journeyPatternPointNumber());
    }

    @Test
    void testListOfJourneyPatternPointOnLineWhenHttpNotFound() {
        
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
