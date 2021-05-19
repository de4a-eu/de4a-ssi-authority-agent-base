package um.si.de4a.model.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "organization")
@XmlAccessorType(XmlAccessType.FIELD)
public class Organization extends Agent {
    private String registration;
    private String vatIdentifier;
    private String taxIdentifier;
    private String homepage;
    private String hasAccreditation;
    private Organization hasUnit;
    private Organization unitOf;
    private String logo;

    public Organization() {
    }

    public Organization(String registration, String vatIdentifier, String taxIdentifier, String homepage, String hasAccreditation, Organization hasUnit, Organization unitOf, String logo) {
        this.registration = registration;
        this.vatIdentifier = vatIdentifier;
        this.taxIdentifier = taxIdentifier;
        this.homepage = homepage;
        this.hasAccreditation = hasAccreditation;
        this.hasUnit = hasUnit;
        this.unitOf = unitOf;
        this.logo = logo;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getVatIdentifier() {
        return vatIdentifier;
    }

    public void setVatIdentifier(String vatIdentifier) {
        this.vatIdentifier = vatIdentifier;
    }

    public String getTaxIdentifier() {
        return taxIdentifier;
    }

    public void setTaxIdentifier(String taxIdentifier) {
        this.taxIdentifier = taxIdentifier;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getHasAccreditation() {
        return hasAccreditation;
    }

    public void setHasAccreditation(String hasAccreditation) {
        this.hasAccreditation = hasAccreditation;
    }

    public Organization getHasUnit() {
        return hasUnit;
    }

    public void setHasUnit(Organization hasUnit) {
        this.hasUnit = hasUnit;
    }

    public Organization getUnitOf() {
        return unitOf;
    }

    public void setUnitOf(Organization unitOf) {
        this.unitOf = unitOf;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
