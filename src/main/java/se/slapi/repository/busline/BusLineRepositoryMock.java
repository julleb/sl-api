package se.slapi.repository.busline;


import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("buslineRepositoryMock")
class BusLineRepositoryMock implements BusLineRepository {
    @Override
    public void getStopsAndLines() {
        System.out.println("getStopsAndLines");
    }
}
