package se.slapi.service.model;

import java.util.Collection;

public record BusInformation(int busLineNumber, Collection<String> stopNames) {
}
