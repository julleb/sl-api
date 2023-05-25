package se.slapi.service;

import se.slapi.service.model.Busline;

import se.slapi.service.model.TrafficRoute;

import java.util.*;

public class AssignmentTask {


    TrafficRoute getLongestTrafficRouteForBusline(Busline busline) {
        int maxSize = -1;
        TrafficRoute longestTrafficRoute = null;
        for(TrafficRoute trafficRoute : busline.trafficRoutes()) {
            if(trafficRoute.stopPoints().size() > maxSize) {
                longestTrafficRoute = trafficRoute;
                maxSize = trafficRoute.stopPoints().size();
            }
        }
        if(longestTrafficRoute == null) {
            throw new IllegalArgumentException("Busline has no traffic route");
        }
        return longestTrafficRoute;
    }

    public List<Busline> task(BuslineService buslineService) throws ServiceException {
        var buslines = buslineService.getBuslines();
        if(buslines.isEmpty()) return buslines;
        var sortedBuslines = buslines.stream().sorted(Comparator.comparing(b -> getLongestTrafficRouteForBusline(b).stopPoints().size())).toList();
        List<Busline> reversedBuslines = new ArrayList<>();
        for(int i = sortedBuslines.size() - 1; i >= 0; i--) {
            reversedBuslines.add(sortedBuslines.get(i));
        }

        reversedBuslines = reversedBuslines.stream().limit(10).toList();
        System.out.println("LISTING TOP 10 BUS LINES WITH MOST BUS STOPS");
        for(var busline : reversedBuslines) {
            TrafficRoute trafficRoute = getLongestTrafficRouteForBusline(busline);
            System.out.println("BusLine="+busline.id() + " AmountOfStops="+ trafficRoute.stopPoints().size());
        }

        var buslineWithMostStops = reversedBuslines.get(0);
        TrafficRoute trafficRoute = getLongestTrafficRouteForBusline(buslineWithMostStops);
        System.out.println("LISTING STOP NAMES FOR Busline=" + buslineWithMostStops.id());
        List<String> stopNames = new ArrayList<>();
        for(var stopPoint : trafficRoute.stopPoints()) {
            stopNames.add(stopPoint.name());
        }
        System.out.println(stopNames);
        return reversedBuslines;
    }
}
