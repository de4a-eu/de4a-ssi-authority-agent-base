package um.si.de4a.db;

public enum VPStatusEnum {
    REQUEST_SENT("request_sent"),
    VP_RECEIVED("vp_received"),
    VP_REJECTED("vp_rejected"),
    VP_VALID("vp_valid"),
    VP_NOT_VALID("vp_not_valid");

    private final String text;

    /**
     * @param text
     */
    VPStatusEnum(final String text) {
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
