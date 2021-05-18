package json.location;

import json.location.Location;

public class LocationReferences {
    private Location location;

    public LocationReferences(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
