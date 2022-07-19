package um.si.de4a.db;

public enum DIDConnStatusEnum {
    CONNECTION_ESTABLISHED("ConnectionEstablished"),
    INVITATION_GENERATED("InvitationGenerated"),
    INVITATION_ACCEPTED("InvitationAccepted");

    private final String text;

    /**
     * @param text
     */
    DIDConnStatusEnum(final String text) {
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
