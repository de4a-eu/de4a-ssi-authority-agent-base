package um.si.de4a.model.json;

public class Enums {
    static enum TYPE {
        DECIMAL("xsd:decimal"),
        INTEGER("xsd:integer"),
        STRING("xsd:string"),
        HTML("rdf:HTML");

        private final String text;

        TYPE(String s) {
            this.text = s;
        }
        @Override
        public String toString() {
            return text;
        }
    }

    static enum ACTIVITY_TYPE {
        EDUCATION("http://data.europa.eu/europass/learningActivityType/educationalProgram"),
        WORKSHOP("http://data.europa.eu/europass/learningActivityType/workShop"),
        INTERNSHIP("http://data.europa.eu/europass/learningActivityType/internship"),
        APPRENTICESHIP("http://data.europa.eu/europass/learningActivityType/apprenticeship"),
        SELF_MOTIVATED_STUDY("http://data.europa.eu/europass/learningActivityType/selfmotivatedstudy"),
        VOLUNTEERING("http://data.europa.eu/europass/learningActivityType/volunteering"),
        JOB_EXPERIENCE("http://data.europa.eu/europass/learningActivityType/jobexperience"),
        CLASSROOM("http://data.europa.eu/europass/learningActivityType/classroom"),
        LAB("http://data.europa.eu/europass/learningActivityType/lab"),
        ELEARNING("http://data.europa.eu/europass/learningActivityType/elearning"),
        RESEARCH("http://data.europa.eu/europass/learningActivityType/research");

        private final String text;

        ACTIVITY_TYPE(String s) {
            this.text = s;
        }
        @Override
        public String toString() {
            return text;
        }
    }

    static enum LANGUAGE {
        BG("bg"),
        CA("ca"),
        CS("cs"),
        CY("cy"),
        DA("da"),
        DE("de"),
        EN("en"),
        ES("es"),
        ET("et"),
        EL("el"),
        EU("eu"),
        FI("fi"),
        FR("fr"),
        GA("ga"),
        GD("gd"),
        GL("gl"),
        HR("hr"),
        HU("hu"),
        IS("is"),
        IT("it"),
        LV("lv"),
        LT("lt"),
        MT("mt"),
        NO("no"),
        NL("nl"),
        PL("pl"),
        PT("pt"),
        RO("ro"),
        RU("ru"),
        SK("sk"),
        SL("sl"),
        SR("sr"),
        SV("sv"),
        TR("tr");

        private final String text;

        LANGUAGE(String s) {
            text = s;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
