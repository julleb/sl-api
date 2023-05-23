package se.slapi.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.slapi.repository.exceptions.RepositoryException;
import se.slapi.repository.sllines.SlLinesRepository;
import se.slapi.repository.sllines.model.JourneyPatternPointOnLine;
import se.slapi.repository.sllines.model.TransportModeCode;

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

    public void getBusLineInformation() throws ServiceException {
        Collection<JourneyPatternPointOnLine> journeyPatternPointOnLineCollection;
        try {
            journeyPatternPointOnLineCollection = slLinesRepository.getListOfJourneyPatternPointOnLine(TransportModeCode.BUS);
        } catch (RepositoryException e) {
            throw new ServiceException();
        }
        Map<Integer, Integer> amountOfStopsPerLine = new HashMap<>();
        for(var journeyPatternPoint : journeyPatternPointOnLineCollection) {
            amountOfStopsPerLine.putIfAbsent(journeyPatternPoint.lineNumber(), 0);
            amountOfStopsPerLine.computeIfPresent(journeyPatternPoint.lineNumber(), (key, value) -> value + 1);
        }
    }
}
