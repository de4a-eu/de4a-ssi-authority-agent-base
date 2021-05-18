package json.organisation;

import json.Title;

public class Organisation {
    private String id;
    private Title preferredName;

    public Organisation(String id, Title preferredName) {
        this.id = id;
        this.preferredName = preferredName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Title getPreferredName() {
        return preferredName;
    }

    public void setPreferredName(Title preferredName) {
        this.preferredName = preferredName;
    }
}
