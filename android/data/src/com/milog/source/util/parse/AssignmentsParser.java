package com.milog.source.util.parse;

import com.milog.source.data.SourceCategory;
import com.milog.source.data.SourceClass;
import com.milog.source.data.SourceGrade;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class AssignmentsParser {

    private static final String CATEGORIES_ID = "sps-assignment-categories";


    public static SourceClass parseSourceClass(String htmlBody) {

        Document doc = Jsoup.parse(htmlBody);

        Element categoriesTable = doc.select(tableSelector(CATEGORIES_ID)).get(0);
        List<SourceCategory> categories = parseCategoriesTable(categoriesTable);




        return null;
    }


    private static ArrayList<SourceCategory> parseCategoriesTable(Element tableElement) {
        Elements categoryElements = tableElement.select("tr");

        ArrayList<SourceCategory> categories = new ArrayList<>();

        for (Element categoryEl : categoryElements) {

            Elements categoryParts = categoryEl.select("td");

            String categoryName   = categoryParts.get(CategoryIndex.NAME            .ordinal()).text();
            String weight         = categoryParts.get(CategoryIndex.WEIGHT          .ordinal()).text();
            String assignCount    = categoryParts.get(CategoryIndex.ASSIGNMENT_COUNT.ordinal()).text();
            String pointsEarned   = categoryParts.get(CategoryIndex.POINTS_EARNED   .ordinal()).text();
            String pointsPossible = categoryParts.get(CategoryIndex.POINTS_POSSIBLE .ordinal()).text();
            String letter         = categoryParts.get(CategoryIndex.GRADE           .ordinal()).text();

            SourceGrade catGrade = new SourceGrade(pointsEarned, pointsPossible, letter);
            SourceCategory sourceCategory = new SourceCategory(catGrade, categoryName, assignCount, weight);

            categories.add(sourceCategory);
        }

        return categories;
    }

    private static String tableSelector(String s) {
        return "#" + s + " > tbody";
    }
}
