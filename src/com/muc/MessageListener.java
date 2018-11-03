package com.muc;

/**
 *
 * @author Thanhhao
 */


public interface MessageListener {
    void onMessage(String fromClient, String body);
}
