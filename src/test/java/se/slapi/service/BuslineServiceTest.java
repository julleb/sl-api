package se.slapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.slapi.repository.exceptions.RepositoryException;
import se.slapi.repository.sllines.SlLinesRepository;
import se.slapi.repository.sllines.model.JourneyPatternPointOnLine;
import se.slapi.repository.sllines.model.StopPoint;
import se.slapi.repository.sllines.model.TransportModeCode;
import se.slapi.service.model.Busline;
import se.slapi.service.model.TrafficRoute;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BuslineServiceTest {

    private BuslineService buslineService;

    private SlLinesRepository slLinesRepository;


    @BeforeEach
    void setup() {
        slLinesRepository = Mockito.mock(SlLinesRepository.class);
        buslineService = new BuslineService(slLinesRepository);
    }

    @Test
    void testGetBuslines() throws RepositoryException, ServiceException {
        int expectedAmountOfStopPoints = 3;
        int expectedBuslineId = 1;
        var stopPoints = createStopPoints(expectedAmountOfStopPoints);
        var journeys = createJourneyPatterPointWithStopPoints(expectedBuslineId, 2, stopPoints);
        Collection<JourneyPatternPointOnLine> listOfJourneyPatternPointOnLine = new ArrayList<>();
        Collection<StopPoint> listOfStopPoints = new ArrayList<>();
        listOfStopPoints.addAll(stopPoints);
        listOfJourneyPatternPointOnLine.addAll(journeys);
        Mockito.when(slLinesRepository.getListOfJourneyPatternPointOnLine(TransportModeCode.BUS)).thenReturn(listOfJourneyPatternPointOnLine);
        Mockito.when(slLinesRepository.getStopPoints()).thenReturn(listOfStopPoints);

        List<Busline> buslines = buslineService.getBuslines();
        assertEquals(1, buslines.size());
        Busline busline = buslines.get(0);
        assertEquals(expectedBuslineId, busline.id());
        List<TrafficRoute> trafficRoutes = busline.trafficRoutes();
        assertEquals(1, trafficRoutes.size());
        assertEquals(expectedAmountOfStopPoints, trafficRoutes.get(0).stopPoints().size());
    }

    @Test
    void testGetBuslineWhenSeveralBuslines() throws RepositoryException, ServiceException {
        Collection<JourneyPatternPointOnLine> listOfJourneyPatternPointOnLine = new ArrayList<>();
        Collection<StopPoint> listOfStopPoints = new ArrayList<>();
        int expectedAmountOfBuslines = 3;
        for(int i = 0; i < expectedAmountOfBuslines; i++) {
            var stopPoints = createStopPoints(i + 1);
            var journeys = createJourneyPatterPointWithStopPoints(i+1, 2, stopPoints);
            listOfJourneyPatternPointOnLine.addAll(journeys);
        }
        Mockito.when(slLinesRepository.getListOfJourneyPatternPointOnLine(TransportModeCode.BUS)).thenReturn(listOfJourneyPatternPointOnLine);
        Mockito.when(slLinesRepository.getStopPoints()).thenReturn(listOfStopPoints);

        List<Busline> buslines = buslineService.getBuslines();
        assertEquals(expectedAmountOfBuslines, buslines.size());
    }

    @Test
    void testGetBuslineInformationWhenJourneyPatternPointOnLineRepositoryError() throws RepositoryException {
        Mockito.when(slLinesRepository.getListOfJourneyPatternPointOnLine(TransportModeCode.BUS)).thenThrow(RepositoryException.class);
        assertThrows(ServiceException.class, () -> buslineService.getBuslines());
    }

    @Test
    void testGetBuslinesWhenJourneyPatternPointOnLineReturnsEmptyData() throws RepositoryException, ServiceException {
        Mockito.when(slLinesRepository.getListOfJourneyPatternPointOnLine(TransportModeCode.BUS)).thenReturn(Arrays.asList());
        assertEquals(0, buslineService.getBuslines().size());
    }

    @Test
    void testGetBuslinesWhenStopPointsAreEmpty() throws RepositoryException, ServiceException {
        Collection<JourneyPatternPointOnLine> listOfJourneyPatternPointOnLine = new ArrayList<>();
        var journeys = createJourneyPatterPointWithStopPoints(1, 2, createStopPoints(1));
        listOfJourneyPatternPointOnLine.addAll(journeys);
        Mockito.when(slLinesRepository.getListOfJourneyPatternPointOnLine(TransportModeCode.BUS)).thenReturn(listOfJourneyPatternPointOnLine);
        Mockito.when(slLinesRepository.getStopPoints()).thenReturn(Arrays.asList());
        assertEquals(1, buslineService.getBuslines().size());
    }

    @Test
    void testGetBuslinesWhenStopPointsRepositoryError() throws RepositoryException {
        Mockito.when(slLinesRepository.getStopPoints()).thenThrow(RepositoryException.class);
        assertThrows(ServiceException.class, () -> buslineService.getBuslines());
    }


    List<StopPoint> createStopPoints(int amount) {
        List<StopPoint> listOfStopPoints = new ArrayList<>();
        for(int i = 0; i < amount; i++) {
            var stopPoint = new StopPoint(i, "Slipsknutsgatan " + i);
            listOfStopPoints.add(stopPoint);
        }
        return listOfStopPoints;
    }


    List<JourneyPatternPointOnLine> createJourneyPatterPointWithStopPoints(int id, int directionCode, List<StopPoint> stopPoints) {
        List<JourneyPatternPointOnLine> journeys = new ArrayList<>();
        for(var stopPoint : stopPoints) {
            JourneyPatternPointOnLine journey = new JourneyPatternPointOnLine(id, directionCode, stopPoint.id());
            journeys.add(journey);
        }
        return journeys;
    }


}
