package json.awardingprocess;

import json.awardingprocess.AwardingProcess;

public class AwardingProcessReferences {
    private AwardingProcess awardingProcess;

    public AwardingProcessReferences(AwardingProcess awardingProcess) {
        this.awardingProcess = awardingProcess;
    }

    public AwardingProcess getAwardingProcess() {
        return awardingProcess;
    }

    public void setAwardingProcess(AwardingProcess awardingProcess) {
        this.awardingProcess = awardingProcess;
    }
}
