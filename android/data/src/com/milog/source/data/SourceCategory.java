package com.milog.source.data;

import java.util.regex.Pattern;

/**
 * Created by Milo on 6/12/16.
 */
public class SourceCategory {

    public SourceGrade grade;
    public String originalName;
    public String weight;
    public String assignmentCount;

    public SourceCategory(SourceGrade grade, String originalName, String assCount, String weight) {
        this.grade = grade;
        this.originalName = originalName;
        this.weight = weight;
        this.assignmentCount = assCount;
    }

    public String getShortName() {
        int lastParenIdx = originalName.lastIndexOf("(");
        return originalName.substring(lastParenIdx + 1, originalName.length() - 1);
    }

    public String getLongName() {
        int lastParenIdx = originalName.lastIndexOf("(");
        return originalName.substring(0, lastParenIdx - 1);
    }
}
