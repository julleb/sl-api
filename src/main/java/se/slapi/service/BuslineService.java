package se.slapi.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.slapi.repository.exceptions.RepositoryException;
import se.slapi.repository.sllines.SlLinesRepository;
import se.slapi.repository.sllines.model.JourneyPatternPointOnLine;
import se.slapi.repository.sllines.model.StopPoint;
import se.slapi.repository.sllines.model.TransportModeCode;
import se.slapi.service.model.Busline;
import se.slapi.service.model.TrafficRoute;

import java.util.*;

@Service
public class BuslineService {

    private SlLinesRepository slLinesRepository;

    @Autowired
    public BuslineService(SlLinesRepository slLinesRepository) {
        this.slLinesRepository = slLinesRepository;
    }


    public List<Busline> getBuslines() throws ServiceException {
        List<Busline> buslines = new ArrayList<>();
        Collection<JourneyPatternPointOnLine> journeyPatternPointOnLineCollection;
        Collection<StopPoint> stopPoints;
        try {
            journeyPatternPointOnLineCollection = slLinesRepository.getListOfJourneyPatternPointOnLine(TransportModeCode.BUS);
            stopPoints = slLinesRepository.getStopPoints();
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        Map<Integer, StopPoint> stopPointsMap = new HashMap<>();
        for(StopPoint stopPoint : stopPoints) {
            stopPointsMap.put(stopPoint.id(), stopPoint);
        }
        Map<Integer, Busline> buslineMap = new HashMap<>();
        Map<Integer, Map<Integer,TrafficRoute>> buslineIdToTrafficRoutes = new HashMap<>();
        for(var journeyPatternPoint : journeyPatternPointOnLineCollection) {
            int busLineNumber = journeyPatternPoint.lineNumber();
            Busline busline = buslineMap.get(busLineNumber);
            if(busline == null) {
                busline = new Busline(busLineNumber, new ArrayList<>());
                buslineMap.put(busline.id(), busline);
                buslineIdToTrafficRoutes.put(busline.id(), new HashMap<>());
                buslines.add(busline);
            }
            int stopPointId = journeyPatternPoint.journeyPatternPointNumber();
            int directionCode = journeyPatternPoint.DirectionCode();
            TrafficRoute trafficRoute = buslineIdToTrafficRoutes.get(busline.id()).get(directionCode);
            if(trafficRoute == null) {
                trafficRoute = new TrafficRoute(directionCode, new ArrayList<>());
                busline.trafficRoutes().add(trafficRoute);
                buslineIdToTrafficRoutes.get(busline.id()).put(directionCode, trafficRoute);
            }
            StopPoint stopPoint = stopPointsMap.get(stopPointId);
            trafficRoute.stopPoints().add(stopPoint);
        }
        return buslines;
    }
}
