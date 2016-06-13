package com.milog.source.networking;

import com.milog.source.util.HTMLParser;
import com.milog.source.util.Hashing;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NetworkManager {

    private OkHttpClient client;

    private String username;
    private String password;

    public static final String BASE_URL = "https://ps.seattleschools.org/";

    private static NetworkManager INSTANCE;

    private List<NetworkManagerInterface> listeners = new ArrayList<>();

    public NetworkState networkState;

    public static NetworkManager getInstance() {
        synchronized (NetworkManager.class) {
            if (INSTANCE == null) INSTANCE = new NetworkManager();
            return INSTANCE;
        }
    }

    private NetworkManager() {

        client = new OkHttpClient.Builder()
                .cookieJar(new SourceCookieJar())
                .connectTimeout(30, TimeUnit.SECONDS)
                .followRedirects(true)
                .build();

        networkState = NetworkState.LOGGED_OUT;
    }

    public void addListener(NetworkManagerInterface listener) {
        listeners.add(listener);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private Request initialRequest() {
        return new Request.Builder()
                .url(BASE_URL)
                .build();
    }

    private Request semesterRequest(HttpUrl httpUrl) {
        return new Request.Builder()
                .url(httpUrl)
                .build();
    }

    private Request loginRequest(String contextData, String pstoken) {

        RequestBody homePost = new FormBody.Builder()
                .add("pstoken", pstoken)
                .add("contextData", contextData)
                .add("dbpw", Hashing.dbpwHash(password, contextData))
                .add("pw", Hashing.hash(password, contextData))
                .add("serviceName", "PS Parent Portal")
                .add("pcasServerUrl", "/")
                .add("credentialType", "User Id and Password Credential")
                .add("Account", username)
                .add("ldappassword", password)
                .build();

        return new Request.Builder()
                .url(BASE_URL + "guardian/home.html")
                .post(homePost)
                .build();
    }

    public void attemptLogin() {
        performInitialRequest(initialRequest());
    }

    public void attemptSemesterRequest(HttpUrl url) { performSemesterRequest(semesterRequest(url)); }


    private void performRequest(Request request) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code" + response);

                requestDone(response);
            }
        });
    }



    private void requestDone(Response response) {

        switch (networkState) {

            case LOADING_INITIAL_PAGE:
                System.out.println("Login Load Done");

                PasswordContext context = HTMLParser.passwordContext(response.body());
                Request loginRequest = loginRequest(context.contextData, context.pstoken);

                performLoginRequest(loginRequest);

                break;
            case LOGGING_IN:
                handleLoginResponse(response);
                break;
            case LOADING_SEMESTER:
                handleSemesterResponse(response);
        }
    }

    private void handleSemesterResponse(Response response) {
        try {
            String responseBody = response.body().string();

            notifyNetworkRequestDone(responseBody);
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleLoginResponse(Response response) {

        try {
            String responseBody = response.body().string();
            NetworkLoginStatus loginStatus = HTMLParser.checkLogin(responseBody);

            switch (loginStatus) {

                case INVALID_PASSWORD:
                    System.err.println("INVALID PASSWORD");
                    break;
                case INVALID_USERNAME:
                    System.err.println("INVALID USERNAME");
                    break;
                default:
                    networkState = NetworkState.LOGGED_IN;
                    notifyNetworkRequestDone(responseBody);
            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void notifyNetworkRequestDone(String response) {
        for (NetworkManagerInterface listener : listeners) {
            listener.networkRequestDone(response);
        }
    }

    private void performSemesterRequest(Request request) {
        System.out.println("Requesting semester " + request.url());

        networkState = NetworkState.LOADING_SEMESTER;
        performRequest(request);
    }

    private void performLoginRequest(Request request) {
        System.out.println("Attempting Login");

        networkState = NetworkState.LOGGING_IN;
        performRequest(request);
    }

    private void performInitialRequest(Request request) {
        System.out.println("Loading Initial Login Page");

        networkState = NetworkState.LOADING_INITIAL_PAGE;
        performRequest(request);
    }
}
