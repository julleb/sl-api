package se.slapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.slapi.repository.sllines.model.StopPoint;
import se.slapi.service.model.Busline;
import se.slapi.service.model.TrafficRoute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssignmentTaskTest {

    private BuslineService buslineService;
    private AssignmentTask assignmentTask;

    private static final String expectedStopPointName = "LolasGata 1337";


    @BeforeEach
    void setup() {
        buslineService = Mockito.mock(BuslineService.class);
        assignmentTask = new AssignmentTask();
    }


    @Test
    void test() throws ServiceException {
        List<Busline> buslines = new ArrayList<>();
        buslines.add(createABusline(1, Arrays.asList(createATrafficRouteWithStopPoints(1, 3))));
        buslines.add(createABusline(2, Arrays.asList(createATrafficRouteWithStopPoints(1, 6))));
        buslines.add(createABusline(3, Arrays.asList(createATrafficRouteWithStopPoints(1, 1))));
        buslines.add(createABusline(9, Arrays.asList(createATrafficRouteWithStopPoints(1, 100))));
        Mockito.when(buslineService.getBuslines()).thenReturn(buslines);

        var buslineResponse = assignmentTask.task(buslineService);

        assertEquals(buslines.size(), buslineResponse.size());
        assertEquals(9, buslineResponse.get(0).id());
        assertEquals(2, buslineResponse.get(1).id());
        assertEquals(1, buslineResponse.get(2).id());
        assertEquals(3, buslineResponse.get(3).id());
    }

    @Test
    void testWhenBuslineHasSeveralTrafficRoutes() throws ServiceException {
        int expectedRouteId = 2;
        List<Busline> buslines = new ArrayList<>();
        List<TrafficRoute> trafficRoutes = new ArrayList<>();
        trafficRoutes.add(createATrafficRouteWithStopPoints(1, 3));
        trafficRoutes.add(createATrafficRouteWithStopPoints(expectedRouteId, 50));
        trafficRoutes.add(createATrafficRouteWithStopPoints(3, 7));
        buslines.add(createABusline(1, trafficRoutes));
        Mockito.when(buslineService.getBuslines()).thenReturn(buslines);

        var response = assignmentTask.task(buslineService);
        assertEquals(expectedRouteId, assignmentTask.getLongestTrafficRouteForBusline(buslines.get(0)).id());


    }

    Busline createABusline(int id, List<TrafficRoute> trafficRouteList) {
        return new Busline(id, trafficRouteList);
    }

    TrafficRoute createATrafficRouteWithStopPoints(int id, int amountOfStopPoints) {
        List<StopPoint> stopPoints = new ArrayList<>();
        for(int i = 0; i < amountOfStopPoints; i++) {
            stopPoints.add(new StopPoint(i, expectedStopPointName));
        }
        return new TrafficRoute(id, stopPoints);
    }
}
