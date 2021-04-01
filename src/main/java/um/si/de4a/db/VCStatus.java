package um.si.de4a.db;

public enum VCStatus {
    CONNECTION_ESTABLISHED("ConnectionEstablished"),
    INVITATION_GENERATED("InvitationGenerated"),
    INVITATION_ACCEPTED("InvitationAccepted"),
    INVITATION_REJECTED("InvitationRejected"),
    VC_ACCEPTED("VCAccepted"),
    VC_SENT("VCSent"),
    OFFER_ACCEPTED("OfferAccepted"),
    OFFER_NOT_SENT("OfferNotSent"),
    OFFER_REJECTED("OfferRejected"),
    VC_REJECTED("VCRejected");

    private final String text;

    /**
     * @param text
     */
    VCStatus(final String text) {
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
