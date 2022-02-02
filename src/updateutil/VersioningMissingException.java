package updateutil;

public class VersioningMissingException extends Exception {
    public VersioningMissingException(String missingElement) {
        super("Versioning information is default. Please add the following versioning information settings to your project: " + missingElement);
    }
}
