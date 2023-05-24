package se.slapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.slapi.repository.exceptions.RepositoryException;
import se.slapi.repository.sllines.SlLinesRepository;
import se.slapi.repository.sllines.model.JourneyPatternPointOnLine;
import se.slapi.repository.sllines.model.StopPoint;
import se.slapi.repository.sllines.model.TransportModeCode;
import se.slapi.service.model.BuslineInformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
        Map<Integer, BuslineInformation> busInformationMap = slService.getBusLineInformation();
        assertEquals(10, busInformationMap.get(1).stopNames().size());
        for(String stopName : busInformationMap.get(1).stopNames()) {
            assertTrue(stopName.contains("Slipsknutsgatan"));
        }
    }

    @Test
    void testDoTheTask() throws ServiceException, RepositoryException {
        mockSlLinesRepository();

        Collection<BuslineInformation> buslineInformations = slService.doTheTask();
        assertEquals(4, buslineInformations.size());
        var list = buslineInformations.stream().toList();
        assertEquals(1, list.get(0).buslineNumber());
        assertEquals(4, list.get(1).buslineNumber());
        assertEquals(3, list.get(2).buslineNumber());
        assertEquals(2, list.get(3).buslineNumber());
    }


    List<StopPoint> createStopPoints() {
        List<StopPoint> listOfStopPoints = new ArrayList<>();
        for(int i = 0; i < 100; i++) {
            var stopPoint = new StopPoint(i, "Slipsknutsgatan " + i);
            listOfStopPoints.add(stopPoint);
        }
        return listOfStopPoints;
    }

    List<JourneyPatternPointOnLine> createABusLine(int lineNumber, int amountOfStops) {
        var stopPoints = createStopPoints();
        List<JourneyPatternPointOnLine> listOfJourneyPatternPointOnLine = new ArrayList<>();
        for(int i = 0; i < amountOfStops; i++) {
            var journeyPatternPoint = new JourneyPatternPointOnLine(lineNumber, 2, stopPoints.get(i).id());
            listOfJourneyPatternPointOnLine.add(journeyPatternPoint);
        }
        return listOfJourneyPatternPointOnLine;
    }

    void mockSlLinesRepository() throws RepositoryException {
        Collection<JourneyPatternPointOnLine> listOfJourneyPatternPointOnLine = new ArrayList<>();
        Collection<StopPoint> listOfStopPoints = new ArrayList<>();


        listOfJourneyPatternPointOnLine.addAll(createABusLine(1, 10));
        listOfJourneyPatternPointOnLine.addAll(createABusLine(2, 3));
        listOfJourneyPatternPointOnLine.addAll(createABusLine(3, 5));
        listOfJourneyPatternPointOnLine.addAll(createABusLine(4, 8));

        listOfStopPoints.addAll(createStopPoints());

        Mockito.when(slLinesRepository.getListOfJourneyPatternPointOnLine(TransportModeCode.BUS)).thenReturn(listOfJourneyPatternPointOnLine);
        Mockito.when(slLinesRepository.getStopPoints()).thenReturn(listOfStopPoints);
    }

}
