package um.si.de4a.model.xml;

import com.sun.xml.bind.XmlAccessorFactory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "contactPoint")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContactPoint {

    @XmlElement(name = "address")
    private String address;

    public ContactPoint() {
    }

    public ContactPoint(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

