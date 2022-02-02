package updateutil;

public class UpdateNotCheckedException extends Exception {
    public UpdateNotCheckedException() {
        super("Update not checked. Please call UpdateUtility.checkUpdate() first.");
    }    
}
