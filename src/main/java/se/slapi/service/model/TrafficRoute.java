package se.slapi.service.model;

import se.slapi.repository.sllines.model.StopPoint;

import java.util.List;

public record TrafficRoute(int id, List<StopPoint> stopPoints){
}
