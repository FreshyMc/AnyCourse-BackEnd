package xyz.anycourse.app.domain.enumeration;

public enum MaterialTag {
    UNTAGGED("Untagged");

    private final String name;

    MaterialTag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
