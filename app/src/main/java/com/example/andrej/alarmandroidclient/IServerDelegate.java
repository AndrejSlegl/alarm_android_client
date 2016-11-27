package com.example.andrej.alarmandroidclient;

public interface IServerDelegate {
    void runOnUiThread(Runnable action);
    void serverGeneralException(Exception ex);
    void serverNewClientMessage(String message);
}
