package um.si.de4a.model.json;

public class Title {
    private Text text;

    public Title(Text text) {
        this.text = text;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

}
