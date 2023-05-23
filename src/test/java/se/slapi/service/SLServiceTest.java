package se.slapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.slapi.repository.exceptions.RepositoryException;
import se.slapi.repository.sllines.SlLinesRepository;
import se.slapi.repository.sllines.model.JourneyPatternPointOnLine;
import se.slapi.repository.sllines.model.StopPoint;
import se.slapi.repository.sllines.model.TransportModeCode;
import se.slapi.service.model.BusInformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SLServiceTest {

    private SLService slService;

    private SlLinesRepository slLinesRepository;


    @BeforeEach
    void setup() {
        slLinesRepository = Mockito.mock(SlLinesRepository.class);
        slService = new SLService(slLinesRepository);
    }

    @Test
    void testGetBusInformation() throws RepositoryException, ServiceException {
        mockSlLinesRepository();
        Map<Integer, BusInformation> busInformationMap = slService.getBusLineInformation();
        assertEquals(10, busInformationMap.get(1).stopNames().size());
        for(String stopName : busInformationMap.get(1).stopNames()) {
            assertTrue(stopName.contains("Slipsknutsgatan"));
        }
    }


    void mockSlLinesRepository() throws RepositoryException {
        Collection<JourneyPatternPointOnLine> listOfJourneyPatternPointOnLine = new ArrayList<>();
        Collection<StopPoint> listOfStopPoints = new ArrayList<>();

        //lineNumber 1
        for(int i = 0; i < 10; i++) {
            var stopPoint = new StopPoint(i, "Slipsknutsgatan " + i);
            var journeyPatternPoint = new JourneyPatternPointOnLine(1, 2, stopPoint.id());
            listOfStopPoints.add(stopPoint);
            listOfJourneyPatternPointOnLine.add(journeyPatternPoint);

        }

        //lineNumber 2
        for(int i = 10; i < 20; i++) {
            var stopPoint = new StopPoint(i, "Dualitygatan " + i);
            var journeyPatternPoint = new JourneyPatternPointOnLine(2, 2, stopPoint.id());
            listOfStopPoints.add(stopPoint);
            listOfJourneyPatternPointOnLine.add(journeyPatternPoint);

        }
        Mockito.when(slLinesRepository.getListOfJourneyPatternPointOnLine(TransportModeCode.BUS)).thenReturn(listOfJourneyPatternPointOnLine);
        Mockito.when(slLinesRepository.getStopPoints()).thenReturn(listOfStopPoints);
    }

}
