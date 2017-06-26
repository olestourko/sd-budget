package com.olestourko.sdbudget.core.services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author oles
 */
public class VersionComparisonService {

    public enum Status {
        OLDVERSION,
        CURRENTVERSION,
        UNRELEASED
    }

    public Status compare(String currentVersion, String latestVersion) {
        String currentVersionBase = getBase(currentVersion);
        String currentVersionBug = getBug(currentVersion);
        String latestVersionBase = getBase(latestVersion);
        String latestVersionBug = getBug(latestVersion);

        if (currentVersionBase.compareTo(latestVersionBase) == -1) {
            return Status.OLDVERSION;
        } else if (currentVersionBase.compareTo(latestVersionBase) == 1) {
            return Status.UNRELEASED;
        } else {
            // The base version are the same, what about the bug?
            Status status = Status.CURRENTVERSION;
            switch (currentVersionBug.compareTo(latestVersionBug)) {
                case -1:
                    status = Status.OLDVERSION;
                    break;
                case 0:
                    status = Status.CURRENTVERSION;
                    break;
                case 1:
                    status = Status.UNRELEASED;
                    break;
            }

            return status;
        }
    }

    private String getBase(String version) {
        Pattern pattern = Pattern.compile("[0-9]+\\.[0-9]+\\.[0-9]+");
        Matcher matcher = pattern.matcher(version);
        return matcher.find() ? matcher.group() : "0.0.0";
    }

    private String getBug(String version) {
        Pattern pattern = Pattern.compile("b[0-9]+");
        Matcher matcher = pattern.matcher(version);
        return matcher.find() ? matcher.group() : "b0";
    }
}
