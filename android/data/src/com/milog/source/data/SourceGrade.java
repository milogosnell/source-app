package com.milog.source.data;

/**
 * Created by Milo on 6/7/16.
 */
public class SourceGrade {
    public String points;
    public String letter;
    public String pointsEarned;
    public String pointsPossible;


    public SourceGrade(String points, String letter) {
        this.points = points;
        this.letter = letter;
    }

    public SourceGrade(String pointsEarned, String pointsPossible, String letter) {
        this.letter = letter;
        this.pointsEarned = pointsEarned;
        this.pointsPossible = pointsPossible;
    }
}
