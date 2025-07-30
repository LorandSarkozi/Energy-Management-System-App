package ro.tuc.ds2020.entities;

public class TypingStatus {

    private String personId;
    private boolean typing;

    // Constructors, Getters, Setters
    public TypingStatus() {}

    public TypingStatus(String personId, boolean typing) {
        this.personId = personId;
        this.typing = typing;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public boolean isTyping() {
        return typing;
    }

    public void setTyping(boolean typing) {
        this.typing = typing;
    }
}
