package com.milog.source.data;

import com.milog.source.networking.NetworkManager;
import com.milog.source.networking.NetworkManagerInterface;
import com.milog.source.util.HTMLParserClasses;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Milo on 6/6/16.
 */
public class DataManager implements NetworkManagerInterface {

    private static DataManager INSTANCE;
    private static int WORKING_SEMESTER;

    public static DataManager getInstance() {
        synchronized (DataManager.class) {
            if (INSTANCE == null) {
                INSTANCE = new DataManager();
            }
            return INSTANCE;
        }
    }


    private static NetworkManager networkManager;

    private static int semesterCount = 0;
    private static int semesterTotal = 0;
    private static ArrayList<String> semesterResponses = new ArrayList<>();

    private static long start;

    private DataManager() {
        networkManager = NetworkManager.getInstance();
        networkManager.addListener(this);

        WORKING_SEMESTER = 1;
        start = System.nanoTime();
        attemptNetworkLoginRequest();
    }

    public void setUsername(String username) {
        networkManager.setUsername(username);
    }

    public void setPassword(String password) {
        networkManager.setPassword(password);
    }

    private void attemptNetworkLoginRequest() {
        networkManager.attemptLogin();
    }

    private void attemptSemesterRequest(HttpUrl url) { networkManager.attemptSemesterRequest(url); }

    @Override
    public void networkRequestDone(String responseString) {
        switch (networkManager.networkState) {
            case LOGGED_IN: {
                System.out.println("Callback successful and logged in. Took " + (System.nanoTime() - start) / 1000000000.0);

                List<SourceExpression> expressions = HTMLParserClasses.parseHomeClasses(responseString);

                semesterCount = 0;
                semesterTotal = expressions.size();
                semesterResponses = new ArrayList<>();

                expressions.forEach(sourceExpression -> {
                    SourceClass properClass = sourceExpression.classes.get(WORKING_SEMESTER);
                    attemptSemesterRequest(properClass.semester.URL);
                });

                break;
            }
            case LOADING_SEMESTER: {
                semesterCount++;
                semesterResponses.add(responseString);

                if (semesterCount == semesterTotal) {
                    System.out.println("Got all semesters from classes. Took " + (System.nanoTime() - start) / 1000000000.0);
                    System.out.println(semesterCount);
                }

                break;
            }
        }
    }

    @Override
    public void networkRequestError(Exception exception) {

    }
}
