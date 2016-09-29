package com.example.taek.commutingchecker.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.taek.commutingchecker.R;
import com.example.taek.commutingchecker.services.BLEScanService;
import com.example.taek.commutingchecker.ui.MainActivity;
import com.example.taek.commutingchecker.ui.MainFragment;
import com.example.taek.commutingchecker.ui.SetupFragment;
import com.example.taek.commutingchecker.ui.ThresholdAdjustmentFragment;

/**
 * Created by Taek on 2016-09-05.
 */
public class RegisterReceiver {
    public BroadcastReceiver createReceiver(int receiverType){
        BroadcastReceiver broadcastReceiver;
        switch (receiverType){
            case Constants.BROADCAST_RECEIVER_TYPE_REQEUST_DATA:
                broadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        try{
                            // Getting a public key from server
                            BLEScanService.mSocketIO.getServersRsaPublicKey(BLEScanService.myMacAddress);

                            BLEScanService.mSocketIO.requestEssentialData(SocketIO.SERVICE_CALLBACK);
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(BLEScanService.ServiceContext, "서비스 실행상태가 아닙니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                };
                return broadcastReceiver;
            case Constants.BROADCAST_RECEIVER_TYPE_SHOW_DATA:
                broadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        try{
                            if(BLEScanService.EssentialDataArray.size() > 0) {
                                GenerateNotification.generateNotification(BLEScanService.ServiceContext, "ShowData", "Data_Info",
                                        "사무실 총 수: " + + BLEScanService.EssentialDataArray.size() + ", 좌표 값: "
                                                //+ "사무실 번호: " + BLEScanService.EssentialDataArray.get(0).get("id_workplace").toString() + ","
                                                //+ BLEScanService.EssentialDataArray.get(0).get("beacon_address1").toString() + ": "
                                                + BLEScanService.EssentialDataArray.get(0).get("coordinateX").toString() + ","
                                                // + BLEScanService.EssentialDataArray.get(0).get("beacon_address2").toString() + ": "
                                                + BLEScanService.EssentialDataArray.get(0).get("coordinateY").toString() + ", "
                                                //+ BLEScanService.EssentialDataArray.get(0).get("beacon_address3").toString() + ": "
                                                + BLEScanService.EssentialDataArray.get(0).get("coordinateZ").toString());
                                Log.d("ShowData", BLEScanService.EssentialDataArray.get(0).get("coordinateX").toString() + ", " +
                                        BLEScanService.EssentialDataArray.get(0).get("coordinateY").toString() + ", " +
                                        BLEScanService.EssentialDataArray.get(0).get("coordinateZ").toString());
                            }else{
                                GenerateNotification.generateNotification(BLEScanService.ServiceContext, "ShowData", "No Data",
                                        "");
                                //Toast.makeText(BLEScanService.ServiceContext, "no data", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            GenerateNotification.generateNotification(BLEScanService.ServiceContext, "ShowData", "ShowData failed", "");
                            //Toast.makeText(BLEScanService.ServiceContext, "서비스 실행상태가 아닙니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                };
                return broadcastReceiver;
            case Constants.BROADCAST_RECEIVER_TYPE_NETWORK_CHANGE:
                broadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                         String status_str = NetworkUtil.getConnectivityStatusString(context);
                        int status_int = NetworkUtil.getConnectivityStatus(context);
                        switch (status_int) {
                            case Constants.NETWORK_TYPE_NOT_CONNECTED:
                                Toast.makeText(context, status_str, Toast.LENGTH_SHORT).show();
                                break;
                            case Constants.NETWORK_TYPE_MOBILE:
                                if (!BLEScanService.mSocketIO.connected()) {
                                    BLEScanService.mSocketIO.connect();
                                }
                                break;
                            case Constants.NETWORK_TYPE_WIFI:
                                if (!BLEScanService.mSocketIO.connected()) {
                                    BLEScanService.mSocketIO.connect();
                                }
                                break;
                        }
                    }
                };
                return broadcastReceiver;
            case Constants.BROADCAST_RECEIVER_TYPE_COME_TO_WORK_STATE:
                broadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        SetupFragment.commutingState.setText("출근 상태");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            SetupFragment.commutingState.setTextColor(MainActivity.MainActivityContext.getResources().getColor(R.color.colorGreen, null));
                        else {
                            SetupFragment.commutingState.setTextColor(MainActivity.MainActivityContext.getResources().getColor(R.color.colorGreen));
                        }
                    }
                };
                return broadcastReceiver;
            case Constants.BROADCAST_RECEIVER_TYPE_LEAVE_WORK_STATE:
                broadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        SetupFragment.commutingState.setText("퇴근 상태");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            SetupFragment.commutingState.setTextColor(MainActivity.MainActivityContext.getResources().getColor(R.color.colorRed, null));
                        else {
                            SetupFragment.commutingState.setTextColor(MainActivity.MainActivityContext.getResources().getColor(R.color.colorRed));
                        }
                    }
                };
                return broadcastReceiver;
            case Constants.BROADCAST_RECEIVER_TYPE_STAND_BY_COME_TO_WORK_STATE:
                broadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        SetupFragment.commutingState.setText("출근 대기 상태");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            SetupFragment.commutingState.setTextColor(MainActivity.MainActivityContext.getResources().getColor(R.color.colorBlue, null));
                        else {
                            SetupFragment.commutingState.setTextColor(MainActivity.MainActivityContext.getResources().getColor(R.color.colorBlue));
                        }
                    }
                };
                return broadcastReceiver;
        }
        return null;
    }

    public IntentFilter createPackageFilter(int receiverType){
        IntentFilter intentFilter;
        switch (receiverType){
            case Constants.BROADCAST_RECEIVER_TYPE_REQEUST_DATA:
                intentFilter = new IntentFilter();
                intentFilter.addAction("android.intent.action.REQUEST_DATA");
                intentFilter.addDataScheme("RequestData");
                return intentFilter;

            case Constants.BROADCAST_RECEIVER_TYPE_SHOW_DATA:
                intentFilter = new IntentFilter();
                intentFilter.addAction("android.intent.action.SHOW_DATA");
                intentFilter.addDataScheme("ShowData");
                return intentFilter;

            case Constants.BROADCAST_RECEIVER_TYPE_NETWORK_CHANGE:
                intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                return intentFilter;

            case Constants.BROADCAST_RECEIVER_TYPE_STOP_SERVICE:
                intentFilter = new IntentFilter();
                intentFilter.addAction("android.intent.action.STOP_SERVICE");
                intentFilter.addDataScheme("StopSelf");
                return intentFilter;

            case Constants.BROADCAST_RECEIVER_TYPE_COME_TO_WORK_STATE:
                intentFilter = new IntentFilter();
                intentFilter.addAction("android.intent.action.COME_TO_WORK_STATE");
                intentFilter.addDataScheme("comeToWork");
                return intentFilter;

            case Constants.BROADCAST_RECEIVER_TYPE_LEAVE_WORK_STATE:
                intentFilter = new IntentFilter();
                intentFilter.addAction("android.intent.action.LEAVE_WORK_STATE");
                intentFilter.addDataScheme("leaveWork");
                return intentFilter;

            case Constants.BROADCAST_RECEIVER_TYPE_STAND_BY_COME_TO_WORK_STATE:
                intentFilter = new IntentFilter();
                intentFilter.addAction("android.intent.action.STAND_BY_COME_TO_WORK_STATE");
                intentFilter.addDataScheme("standByComeToWork");
                return intentFilter;
        }
        return null;
    }
}