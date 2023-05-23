package se.slapi.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.slapi.repository.exceptions.RepositoryException;
import se.slapi.repository.sllines.SlLinesRepository;
import se.slapi.repository.sllines.model.JourneyPatternPointOnLine;
import se.slapi.repository.sllines.model.StopPoint;
import se.slapi.repository.sllines.model.TransportModeCode;
import se.slapi.service.model.BusInformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class SLService {


    private SlLinesRepository slLinesRepository;

    @Autowired
    public SLService(SlLinesRepository slLinesRepository) {
        this.slLinesRepository = slLinesRepository;
    }

    public Map<Integer, BusInformation> getBusLineInformation() throws ServiceException {
        Collection<JourneyPatternPointOnLine> journeyPatternPointOnLineCollection;
        Collection<StopPoint> stopPoints;
        try {
            journeyPatternPointOnLineCollection = slLinesRepository.getListOfJourneyPatternPointOnLine(TransportModeCode.BUS);
            stopPoints = slLinesRepository.getStopPoints();
        } catch (RepositoryException e) {
            throw new ServiceException();
        }

        Map<Integer, StopPoint> stopPointsMap = new HashMap<>();
        for(StopPoint stopPoint : stopPoints) {
            stopPointsMap.put(stopPoint.id(), stopPoint);
        }

        Map<Integer, BusInformation> busInformationMap = new HashMap<>();
        for(var journeyPatternPoint : journeyPatternPointOnLineCollection) {
            int busLineNumber = journeyPatternPoint.lineNumber();
            BusInformation busInformation = busInformationMap.get(busLineNumber);
            if(busInformation == null) {
                busInformation = new BusInformation(busLineNumber, new ArrayList<>());
                busInformationMap.put(busInformation.busLineNumber(), busInformation);
            }
            int stopPointId = journeyPatternPoint.journeyPatternPointNumber();
            StopPoint stopPoint = stopPointsMap.get(stopPointId);
            String stopPointName = stopPoint == null || stopPoint.name() == null ? "" : stopPoint.name();
            busInformation.stopNames().add(stopPointName);
        }
        return busInformationMap;
    }
}
