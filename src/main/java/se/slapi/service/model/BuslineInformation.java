package se.slapi.service.model;

import java.util.Collection;

public record BuslineInformation(int buslineNumber, Collection<String> stopNames) {
}
