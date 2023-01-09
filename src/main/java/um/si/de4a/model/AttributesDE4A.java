package um.si.de4a.model;

public class AttributesDE4A {
    public String hash;
    public String body;

    public AttributesDE4A(String hash, String body) {
        this.hash = hash;
        this.body = body;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
