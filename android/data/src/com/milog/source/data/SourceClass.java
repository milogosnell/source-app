package com.milog.source.data;

import javax.xml.transform.Source;
import java.util.ArrayList;

/**
 * Created by Milo on 6/6/16.
 */
public class SourceClass {

    public String className;
    public String teacherName;
    public ArrayList<SourceTerm> quarters;
    public SourceTerm semester;

    public SourceClass(String className, String teacherName, ArrayList<SourceTerm>quarters, SourceTerm semester){

        this.className = className;
        this.teacherName = teacherName;
        this.quarters = quarters;
        this.semester = semester;
    }
}
