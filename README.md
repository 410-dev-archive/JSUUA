# JSUUA: Java Simple Update Utility for Application

This package will allow developers to implement the update feature easily.

## Usage:

A static file in the server is required. It should be accessible via HTTP or HTTPS.
The static file should contain:

Latest build (integer), and its file URL
Stable build (integer), and its file URL.

The file should follow the format:
```
latest:2400
latest-url:https://github.com/410-dev/MyProgram/releases/download/2.4/MyCompiledProgram.jar
stable:2130
stable-url:https://github.com/410-dev/MyProgram/releases/download/2.13/MyCompiledProgram.jar
```

For GitHub, the URL to this file must be raw.githubusercontent.com.

URL to this file is called "Update Checker URL".



## Documentation

All methods in all classes are static.

### Versioning

#### Final variables

These variables are compared during `UpdateUtility.checkUpdate()` method. If versioning class contains the same value with the default values, then it will throw an VersioningMissingException.

To change the versioning, refer to the methods below.

```java
// Default build number
// -2,147,483,648 (INT MIN)
int DEFAULT_BUILDNUM 
```

```java
// Default Update URL
// "____"
String DEFAULT_UPDATE_URL
```



#### Methods

```java
// Set basic information: Build number, Update Checker URL
// Stable channel usage will be automatically configured to "true".
void setInfo(int buildNumber, String updateCheckerURL)
```

```java
// Set basic information: Build number, Update Checker URL, Stable Channel Usage Configuration
void setInfo(int buildNumber, String updateCheckerURL, boolean useStableChannel)
```

```java
// Set build number of the program
void setBuildNum(int num)
```

```java
// Set Update Checker URL of the program
void setUpdateCheckerURL(String url)
```

```java
// Set the channel configuration to stable release
void useStableRelease()
```

```java
// Set the channel configuration to latest release
void useLatestRelease()
```

```java
// Getter for build number
int getBuildNum()
```

```java
// Getter for Update Checker URL
String getUpdateCheckerURL()
```

```java
// Getter for channel configuration
boolean isUsingStableRelease()
```



### UpdateUtility

#### Methods

```java
// Check update
// Versioning data using Versioning.[setters] must be set before this method is called
Object[] checkUpdate() throws VersioningMissingException, IOException

// The length of returning Object[] is 3.
// [0]: true if update is available, false if not
// [1]: the new version if available, null if not
// [2]: Update file binary URL
```

```java
// Download the update.
// The file name will be ./updateFile.jar
void doUpdate() throws UpdateNotCheckedException, IOException
```

```java
// Download the update with specific path
void doUpdate(String filePath) throws UpdateNotCheckedException, IOException
```



## Example code

```java
// Initialize update utility
// Setting program information
Versioning.setBuildNum(1);
Versioning.setUpdateCheckerURL("https://raw.githubusercontent.com/410-dev/MyProgram/main/updateinf");
Versioning.useStableRelease();
try {
    Object[] result = UpdateUtility.checkUpdate();
    if ((Boolean) result[0] == true) {   // Has update
        SystemLogger.log("Update available!");
        SystemLogger.log("Current version: " + Versioning.getBuildNum());
        SystemLogger.log("Latest version: " + result[1]);
        SystemLogger.log("Download URL: " + result[2]);

        // Show user the prompt
        int resultInt = JOptionPane.showConfirmDialog(null, "Update available!\nCurrent build: " + Versioning.getBuildNum() + "\nLatest build: " + result[1] + "\nDownload URL: " + result[2] + "\n\nUpdate now?", "Update available", JOptionPane.YES_NO_OPTION);

        // If user wants, do update
        if (resultInt == JOptionPane.YES_OPTION) {
            UpdateUtility.doUpdate("./Slip.jar");
            JOptionPane.showMessageDialog(null, "Update downloaded.");
            System.exit(0);
        }
    }
}catch(Exception e) {
    SystemLogger.error("Update Failed", true, SystemLogger.CONTINUE, e);
}

```

