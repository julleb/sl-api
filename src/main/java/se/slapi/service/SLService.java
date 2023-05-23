package se.slapi.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.slapi.repository.exceptions.RepositoryException;
import se.slapi.repository.sllines.SlLinesRepository;
import se.slapi.repository.sllines.model.JourneyPatternPointOnLine;
import se.slapi.repository.sllines.model.StopPoint;
import se.slapi.repository.sllines.model.TransportModeCode;
import se.slapi.service.model.BusInformation;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SLService {


    private SlLinesRepository slLinesRepository;

    @Autowired
    public SLService(SlLinesRepository slLinesRepository) {
        this.slLinesRepository = slLinesRepository;
    }

    Map<Integer, BusInformation> getBusLineInformation() throws ServiceException {
        Collection<JourneyPatternPointOnLine> journeyPatternPointOnLineCollection;
        Collection<StopPoint> stopPoints;
        try {
            journeyPatternPointOnLineCollection = slLinesRepository.getListOfJourneyPatternPointOnLine(TransportModeCode.BUS);
            stopPoints = slLinesRepository.getStopPoints();
        } catch (RepositoryException e) {
            throw new ServiceException(); //TODO
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
            if(!busInformation.stopNames().contains(stopPointName)) //SL api returnerar dubbletter? vrf? TODO dumt att gå igenom hela arrayen varje gång
                busInformation.stopNames().add(stopPointName);
        }
        return busInformationMap;
    }

    public Collection<BusInformation> doTheTask() throws ServiceException {
        Map<Integer, BusInformation> busInformationMap = getBusLineInformation();
        List<BusInformation> busInformations
                = busInformationMap.values().stream().sorted(Comparator.comparing(o -> o.stopNames().size())).toList();

        List<BusInformation> reversedBusInformationList = new ArrayList<>();
        for(int i = busInformations.size() - 1; i >= 0; i--) {
            reversedBusInformationList.add(busInformations.get(i));
        }

        reversedBusInformationList = reversedBusInformationList.stream().limit(10).toList();
        System.out.println("TOP 10 BUS LINES");
        for(var busInfo : reversedBusInformationList) {
            System.out.println("BusLine="+busInfo.busLineNumber() + " AmountOfStops="+ busInfo.stopNames().size() + " StopNames="+busInfo.stopNames());
        }
        return reversedBusInformationList;

    }
}
