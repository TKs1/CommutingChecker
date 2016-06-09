package com.example.taek.commutingchecker;

import android.util.Log;

/**
 * Created by Taek on 2016-06-08.
 */
public class CheckCallback {
    private Thread thread;
    boolean flag;
    public CheckCallback(final DeviceInfo deviceInfo1, final DeviceInfo deviceInfo2, final DeviceInfo deviceInfo3){
        flag = true;

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(flag){
                    Log.d("CheckCallback", "Running");
                    BLEScanService.isCallbackRunning = false;

                    try{
                        Thread.sleep(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    if(!BLEScanService.isCallbackRunning){ // callback method isn't running
                        Log.d("isCallbackRunning)", "false");
                        SendEvent.sendEvent(deviceInfo1, deviceInfo2, deviceInfo3, false);
                        flag = false;
                    }
                }
            }
        });

        thread.start();
    }
}
