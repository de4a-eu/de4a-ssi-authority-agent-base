package um.si.de4a.db;

public enum VCStatusEnum {
    VC_ACCEPTED("VCAccepted"),
    VC_SENT("VCSent"),
    OFFER_ACCEPTED("OfferAccepted"),
    OFFER_REJECTED("OfferRejected"),
    OFFER_SENT("OfferSent"),
    VC_REJECTED("VCRejected");

    private final String text;

    /**
     * @param text
     */
    VCStatusEnum(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}
