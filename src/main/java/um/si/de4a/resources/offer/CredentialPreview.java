package um.si.de4a.resources.offer;

import java.util.ArrayList;

public class CredentialPreview {
    private ArrayList<Attribute> attributes = null;

    public CredentialPreview(ArrayList<Attribute> attributes) {
        this.attributes = attributes;
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<Attribute> attributes) {
        this.attributes = attributes;
    }
}
