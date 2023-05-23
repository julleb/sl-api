package se.slapi.repository.sllines;

import se.slapi.repository.exceptions.RepositoryException;
import se.slapi.repository.sllines.model.JourneyPatternPointOnLine;
import se.slapi.repository.sllines.model.TransportModeCode;

import java.util.Collection;

public interface SlLinesRepository {
    Collection<JourneyPatternPointOnLine> getListOfJourneyPatternPointOnLine(TransportModeCode transportModeCode) throws RepositoryException;


}
