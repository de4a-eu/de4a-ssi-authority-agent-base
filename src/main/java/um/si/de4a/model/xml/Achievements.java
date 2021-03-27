package um.si.de4a.model.xml;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "achievements")
@XmlAccessorType(XmlAccessType.FIELD)
public class Achievements {
    @XmlElement(name="learningAchievementType", type=LearningAchievementType.class)
    private List<LearningAchievementType> achievementTypes = new ArrayList<LearningAchievementType>();

    public Achievements(){};

    public Achievements(List<LearningAchievementType> achievementTypes) {
        this.achievementTypes = achievementTypes;
    }

    public List<LearningAchievementType> getAchievementTypes() {
        return achievementTypes;
    }

    public void setAchievementTypes(List<LearningAchievementType> achievementTypes) {
        this.achievementTypes = achievementTypes;
    }
}
