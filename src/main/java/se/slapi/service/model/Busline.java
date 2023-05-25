package se.slapi.service.model;

import java.util.List;

public record Busline(int id, List<TrafficRoute> trafficRoutes){
}
