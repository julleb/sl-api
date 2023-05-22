package se.slapi.repository.busline;


import org.springframework.stereotype.Repository;

@Repository
class BusLineRepositoryMock implements BusLineRepository {
    @Override
    public void getStopsAndLines() {
        System.out.println("getStopsAndLines");
    }
}
