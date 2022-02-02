package updateutil;

public class NoUpdateURLException extends Exception {
    public NoUpdateURLException() {
        super("Update URL not filled. Please add the update URL settings to your project.");
    }
}
