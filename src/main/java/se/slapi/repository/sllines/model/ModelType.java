package se.slapi.repository.sllines.model;

public enum ModelType {

    JOURNEY_PATTERN_POINT_ON_LINE {
        public String toString() {
            return "jour";
        }
    },

    STOP {
        public String toString() {
            return "stop";
        }
    };

}
