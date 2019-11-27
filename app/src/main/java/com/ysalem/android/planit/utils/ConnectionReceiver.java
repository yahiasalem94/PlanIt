package com.ysalem.android.planit.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.HashSet;

public class ConnectionReceiver extends BroadcastReceiver {

    public static final String TAG = ConnectionReceiver.class.getSimpleName();

    protected HashSet<NetworkStateReceiverListener> listeners;
    protected Boolean connected;

    public ConnectionReceiver() {
        listeners = new HashSet<NetworkStateReceiverListener>();
        connected = null;
    }

    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "OnReceive");
        if(intent == null || intent.getExtras() == null)
            return;

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

//        if(networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
//            connected = true;
//        } else if(intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,Boolean.FALSE)) {
//            connected = false;
//        }
        connected = isOnline(context);


        notifyStateToAll();
    }

    private boolean isOnline (Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }
    private void notifyStateToAll() {
        for(NetworkStateReceiverListener listener : listeners)
            notifyState(listener);
    }

    private void notifyState(NetworkStateReceiverListener listener) {
        if(connected == null || listener == null)
            return;

        listener.networkAvailable(connected);
    }

    public void addListener(NetworkStateReceiverListener listener) {
        listeners.add(listener);
        notifyState(listener);
    }

    public void removeListener(NetworkStateReceiverListener listener) {
        listeners.remove(listener);
    }

    public interface NetworkStateReceiverListener {
        public void networkAvailable(boolean isAvailable);
    }
}