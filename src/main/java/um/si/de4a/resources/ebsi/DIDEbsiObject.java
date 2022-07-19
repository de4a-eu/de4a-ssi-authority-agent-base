package um.si.de4a.resources.ebsi;

public class DIDEbsiObject {
    public String did;

    public DIDEbsiObject(String did){
        this.did = did;
    }

    public String getDid(){
        return this.did;
    }

    public void setDid(String did){
        this.did = did;
    }
}
