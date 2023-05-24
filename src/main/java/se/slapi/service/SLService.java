package se.slapi.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.slapi.repository.exceptions.RepositoryException;
import se.slapi.repository.sllines.SlLinesRepository;
import se.slapi.repository.sllines.model.JourneyPatternPointOnLine;
import se.slapi.repository.sllines.model.StopPoint;
import se.slapi.repository.sllines.model.TransportModeCode;
import se.slapi.service.model.BuslineInformation;

import java.util.*;

@Service
public class SLService {

    private SlLinesRepository slLinesRepository;

    @Autowired
    public SLService(SlLinesRepository slLinesRepository) {
        this.slLinesRepository = slLinesRepository;
    }

    Map<Integer, BuslineInformation> getBusLineInformation() throws ServiceException {
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

        Map<Integer, BuslineInformation> busInformationMap = new HashMap<>();
        for(var journeyPatternPoint : journeyPatternPointOnLineCollection) {
            int busLineNumber = journeyPatternPoint.lineNumber();
            BuslineInformation busLineInformation = busInformationMap.get(busLineNumber);
            if(busLineInformation == null) {
                busLineInformation = new BuslineInformation(busLineNumber, new ArrayList<>());
                busInformationMap.put(busLineInformation.buslineNumber(), busLineInformation);
            }
            int stopPointId = journeyPatternPoint.journeyPatternPointNumber();
            StopPoint stopPoint = stopPointsMap.get(stopPointId);
            String stopPointName = stopPoint == null || stopPoint.name() == null ? "" : stopPoint.name();
            if(!busLineInformation.stopNames().contains(stopPointName)) //SL api returnerar dubbletter? vrf? TODO dumt att gå igenom hela arrayen varje gång
                busLineInformation.stopNames().add(stopPointName);
        }
        return busInformationMap;
    }

    public Collection<BuslineInformation> doTheTask() throws ServiceException {
        Map<Integer, BuslineInformation> busInformationMap = getBusLineInformation();
        List<BuslineInformation> buslineInformations
                = busInformationMap.values().stream().sorted(Comparator.comparing(o -> o.stopNames().size())).toList();

        List<BuslineInformation> reversedBuslineInformationList = new ArrayList<>();
        for(int i = buslineInformations.size() - 1; i >= 0; i--) {
            reversedBuslineInformationList.add(buslineInformations.get(i));
        }

        reversedBuslineInformationList = reversedBuslineInformationList.stream().limit(10).toList();
        System.out.println("TOP 10 BUS LINES");
        for(var busInfo : reversedBuslineInformationList) {
            System.out.println("BusLine="+busInfo.buslineNumber() + " AmountOfStops="+ busInfo.stopNames().size() + " StopNames="+busInfo.stopNames());
        }
        return reversedBuslineInformationList;

    }
}
