package updateutil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UpdateUtility {

    private static String updateBinaryURL = "";

    // Check if there is an update available
    // The versioning class and update URL must be filled.
    public static Object[] checkUpdate() throws VersioningMissingException, IOException {

        // [0]: true if update is available, false if not
        // [1]: the new version if available, null if not
        // [2]: Update file binary URL - Will be downloaded if user accepts or system requires update
        Object[] result = new Object[3];

        // Check if the versioning class has been filled
        // If not, throw an exception
        if (Versioning.getBuildNum() == Versioning.DEFAULT_BUILDNUM) {
            throw new VersioningMissingException("Build Number");
        }else if (Versioning.getUpdateCheckerURL().equals(Versioning.DEFAULT_UPDATE_URL)) {
            throw new VersioningMissingException("Update URL");
        }

        // Check if update is available
        String[] updateInfo = downloadUpdateInformation(Versioning.getUpdateCheckerURL());

        // Parse the first index
        int newBuildNum = Integer.parseInt(updateInfo[0]);

        if (newBuildNum > Versioning.getBuildNum()) {
            result[0] = true;             // Update available
            result[1] = newBuildNum;      // New build number
            result[2] = updateInfo[1];    // Update file binary URL
        }else{
            result[0] = false;            // No update available
            result[1] = null;             // No new build number
            result[2] = updateInfo[1];    // Update file binary URL
        }
        updateBinaryURL = updateInfo[1];

        return result;
    }

    public static void doUpdate() throws UpdateNotCheckedException, IOException {
        doUpdate("./updateFile.jar");
    }

    public static void doUpdate(String filePath) throws UpdateNotCheckedException, IOException {

        // If update URL is empty, throw an exception
        if (updateBinaryURL.equals("")) {
            throw new UpdateNotCheckedException();
        }
        downloadFromURL(updateBinaryURL, filePath);
    }

    private static String[] downloadUpdateInformation(String url) throws IOException {
        // Download update info from the url
        // Return the update info as a string

        String outputPath = "." + File.separator + ".updateinf.juutil";
        downloadFromURL(url, outputPath);

        // Read the update info file
        String s = "";
        BufferedReader reader = new BufferedReader(new FileReader(outputPath));
        while(true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            s += line.replaceAll(" ", "") + "\n";
        }
        reader.close();
        String[] parsable = s.split("\n");

        // Delete the update info file
        File f = new File(outputPath);
        f.delete();

        // Parse the update info file
        // [0]: Build number
        // [1]: URL
        String[] result;
        if (Versioning.isUsingStableRelease()) {
            result = lineParser("stable", parsable);
        }else{
            result = lineParser("latest", parsable);
        }

        return result;
    }

    private static String[] lineParser(String usingSV, String[] parsable) {
        String[] result = new String[2];
        for(String line : parsable) {
            if (line.startsWith(usingSV + ":")) {
                result[0] = line.split(":")[1];
            }else if (line.startsWith(usingSV + "-url:")) {
                line = line.replaceAll(usingSV + "-url:", "");
                result[1] = "";
                for(String l2 : line.split(":")) {
                    result[1] += l2 + ":";
                }
                // Remove the last ":"
                result[1] = result[1].substring(0, result[1].length() - 1);
            }
        }
        return result;
    }

    private static String incrementNamingInteger(int lastInteger, String filePathWithoutExtension, String extension) {
        File f = new File(filePathWithoutExtension + lastInteger + "." + extension);
        if (f.exists()) {
            return incrementNamingInteger(lastInteger + 1, filePathWithoutExtension, extension);
        }else{
            return filePathWithoutExtension + lastInteger + "." + extension;
        }
    }

    private static String[] separateFilePathAndExtension(String fullPath) {
        String[] result = new String[2];
        int lastIndex = fullPath.lastIndexOf(".");
        result[0] = fullPath.substring(0, lastIndex);
        result[1] = fullPath.substring(lastIndex + 1);
        return result;
    }

    private static void downloadFromURL(String url, String outputPath) throws IOException {
        // Download the update file from the URL
        // If the download fails, throw an exception
        InputStream in = new URL(url).openStream();
        File f = new File(outputPath);
        String newName = "";
        if (f.exists()) {
            newName = incrementNamingInteger(1, separateFilePathAndExtension(outputPath)[0], separateFilePathAndExtension(outputPath)[1]);
        }else{
            newName = outputPath;
        }
        Path filePath = Paths.get(newName);
        Files.copy(in, filePath); 
        in.close();
    }
}
