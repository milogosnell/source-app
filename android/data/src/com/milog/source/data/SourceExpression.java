package com.milog.source.data;

import javax.xml.transform.Source;
import java.util.ArrayList;

/**
 * Created by Milo on 6/6/16.
 */
public class SourceExpression {

    public ArrayList<SourceClass> classes;
    public String expressionName;

    public SourceExpression(String expressionName) {
        classes = new ArrayList<>();
        this.expressionName = expressionName;
    }

    @Override
    public boolean equals(Object obj) {
        return this.expressionName.equals(((SourceExpression)obj).expressionName);
    }
}
