package com.milog.source.networking;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import javax.xml.transform.Source;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Milo on 6/5/16.
 */
public class SourceCookieJar implements CookieJar {

    private List<Cookie> cookies;

    public SourceCookieJar() {
        cookies = new ArrayList<>();
    }

    @Override
    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
        for (int i = 0; i < list.size(); i++) {
            int cookieE = cookieExists(list.get(i));
            if (cookieE == -1) {
                cookies.add(list.get(i));
            } else {
                cookies.set(cookieE, list.get(i));
            }
        }
    }

    private int cookieExists(Cookie cookie) {
        for (int i = 0; i < cookies.size(); i++)
            if (cookies.get(i).name().equals(cookie.name()))
                return i;
        return -1;
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
        if (cookies != null)
            return cookies;
        return new ArrayList<>();
    }
}
