package com.milog.source.util.parse;

import com.milog.source.networking.NetworkLoginStatus;
import com.milog.source.networking.PasswordContext;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Milo on 6/6/16.
 */


public class LoginParser {

    private static final String CONTEXT_DATA_KEY = "contextData";
    private static final String PSTOKEN_KEY = "pstoken";
    private static final String VALUE_ATTR = "value";

    private static final String INVALID_PW_TOKEN = "Access denied";
    private static final String INVALID_USERNAME_TOKEN = "Invalid Username";

    public static PasswordContext passwordContext(ResponseBody body) {

        try {
            Document doc = Jsoup.parse(body.string());
            String contextData = doc.getElementsByAttributeValue("name", CONTEXT_DATA_KEY).attr(VALUE_ATTR);
            String pstoken = doc.getElementsByAttributeValue("name", PSTOKEN_KEY).attr(VALUE_ATTR);
            return new PasswordContext(contextData, pstoken);
        }

        catch (IOException e ) {
            e.printStackTrace();
        }

        return new PasswordContext("","");
    }


    public static NetworkLoginStatus checkLogin(String body) {

        if (body.contains(INVALID_PW_TOKEN)) return NetworkLoginStatus.INVALID_PASSWORD;
        if (body.contains(INVALID_USERNAME_TOKEN)) return NetworkLoginStatus.INVALID_USERNAME;
        return NetworkLoginStatus.SUCCESSFUL;
    }

}
