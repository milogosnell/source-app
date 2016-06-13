package com.milog.source.util;

import com.milog.source.data.SourceClass;
import com.milog.source.data.SourceExpression;
import com.milog.source.data.SourceGrade;
import com.milog.source.data.SourceTerm;
import com.milog.source.networking.NetworkManager;

import okhttp3.HttpUrl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Milo on 6/6/16.
 */


public class HTMLParserClasses extends HTMLParser {

    private static final int QUARTER_ONE   = 0;
    private static final int QUARTER_TWO   = 1;
    private static final int SEMESTER_ONE  = 2;

    private static final String TERM_SELECTOR = "td[class=colorMyGrade]";
    private static final String DETAIL_SELECTOR = "td[align=left]";

    private static final String GRADE_TABLE_ID = "tblgrades";

    private static final DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");


    public static ArrayList<SourceExpression> parseHomeClasses(String responseString) {

        Document doc1 = Jsoup.parse(responseString);
        Element gradeTable = doc1.getElementById(GRADE_TABLE_ID);

        ArrayList<SourceExpression> expressions = new ArrayList<>();

        Elements classes = gradeTable.select("tr[class=center]");
        for (Element classElement : classes) {

            String classExp = classElement.select("th").text();
            SourceExpression testExp = new SourceExpression(classExp);

            if (expressions.contains(testExp)) {
                SourceExpression exp = expressions.get(expressions.indexOf(testExp));
                exp.classes.add(sourceClassFromElement(classElement, 1));
            } else {
                SourceExpression exp = new SourceExpression(classExp);
                exp.classes.add(sourceClassFromElement(classElement, 0));
                expressions.add(exp);
            }

        }

        return expressions;
    }

    private static SourceClass sourceClassFromElement(Element classElement, int sem) {

        Elements terms = classElement.select(TERM_SELECTOR);

        int offset = sem * 3;

        SourceTerm quarter1Term = termFromTermElement(terms.get(QUARTER_ONE + offset), "Q" + Integer.toString((sem*2) + 1));
        SourceTerm quarter2Term = termFromTermElement(terms.get(QUARTER_TWO + offset), "Q" + Integer.toString((sem*2) + 2));
        SourceTerm semesterTerm = termFromTermElement(terms.get(SEMESTER_ONE + offset), "S" + Integer.toString(sem + 1));

        String className   = classElement.select(DETAIL_SELECTOR).first().ownText();
        String teacherName = classElement.select(DETAIL_SELECTOR + " > a").text();

        ArrayList<SourceTerm> quarters = new ArrayList<>();
        quarters.add(quarter1Term);
        quarters.add(quarter2Term);

        return new SourceClass(className, teacherName, quarters, semesterTerm);
    }

    private static SourceTerm termFromTermElement(Element termElement, String name) {

        SourceGrade grade = gradeFromTermElement(termElement);

        String termURLString = termElement.select("a").attr("href");
        HttpUrl termURL = HttpUrl.parse(NetworkManager.BASE_URL + "guardian/" + termURLString);

        Date startDate = dateFromDateString(termURL.queryParameter("begdate"));
        Date endDate = dateFromDateString(termURL.queryParameter("enddate"));

        return new SourceTerm(grade, name, startDate, endDate, termURL);

    }

    private static SourceGrade gradeFromTermElement(Element termElement) {
        String[] parts = termElement.text().split(" ");
        return new SourceGrade(parts[1], parts[0]);
    }

    private static Date dateFromDateString(String dateString) {
        try {
            return formatter.parse(dateString);
        }
        catch (Exception e) {
            return null;
        }
    }
}

