package com.milog.source.networking;

import okhttp3.Response;

/**
 * Created by Milo on 6/6/16.
 */
public interface NetworkManagerInterface {

    void networkRequestDone(String responseString);
    void networkRequestError(Exception exception);


}
