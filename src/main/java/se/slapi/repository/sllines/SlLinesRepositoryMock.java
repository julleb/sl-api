package se.slapi.repository.sllines;


import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import se.slapi.repository.exceptions.RepositoryException;
import se.slapi.repository.sllines.model.JourneyPatternPointOnLine;
import se.slapi.repository.sllines.model.StopPoint;
import se.slapi.repository.sllines.model.TransportModeCode;

import java.util.Collection;

@Repository
@Profile("slLinesRepositoryMock")
class SlLinesRepositoryMock implements SlLinesRepository {
    @Override
    public Collection<JourneyPatternPointOnLine> getListOfJourneyPatternPointOnLine(TransportModeCode transportModeCode) throws RepositoryException {
        return null;
    }

    @Override
    public Collection<StopPoint> getStopPoints() throws RepositoryException {
        return null;
    }
}
