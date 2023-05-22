package se.slapi.repository.busline;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!buslineRepositoryMock")
public class BusLineRepositoryImpl implements BusLineRepository {
    @Override
    public void getStopsAndLines() {

    }
}
