package com.artixtise.richdialer.presentation.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.logging.Handler;

public class TService extends Service {
    MediaRecorder recorder;
    File audiofile;
    String name, phonenumber;
    String audio_format;
    public String Audio_Type;
    int audioSource;
    Context context;
    private Handler handler;
    Timer timer;
    Boolean offHook = false, ringing = false;
    Toast toast;
    Boolean isOffHook = false;
    private boolean recordstarted = false;

    private static final String ACTION_IN = "android.intent.action.PHONE_STATE";
    private static final String ACTION_OUT = "android.intent.action.NEW_OUTGOING_CALL";
    private PhonecallReceiver br_call;




    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("StartService", "TService");
        final IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_OUT);
        filter.addAction(ACTION_IN);
        this.registerReceiver(new CallReceiver(), filter);
        return super.onStartCommand(intent, flags, startId);
    }

    private void startRecording() {
        File sampleDir = new File(Environment.getExternalStorageDirectory(), "/RichCallDialer");
        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }
        String file_name = "RichCall"+System.currentTimeMillis();
        try {
            audiofile = File.createTempFile(file_name, ".amr", sampleDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        recorder = new MediaRecorder();
//                          recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);

        recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(audiofile.getAbsolutePath());
        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recorder.start();
        recordstarted = true;
    }

    private void stopRecording() {
        if (recordstarted) {
            recorder.stop();
            recordstarted = false;
        }
    }


    public abstract class PhonecallReceiver extends BroadcastReceiver {

        //The receiver will be recreated whenever android feels like it.  We need a static variable to remember data between instantiations

        private int lastState = TelephonyManager.CALL_STATE_IDLE;
        private Date callStartTime;
        private boolean isIncoming;
        private String savedNumber;  //because the passed incoming is only valid in ringing


        @Override
        public void onReceive(Context context, Intent intent) {
//        startRecording();
            //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
            if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
                savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
            } else {
                String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
                String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                int state = 0;
                if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    state = TelephonyManager.CALL_STATE_IDLE;
                } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    state = TelephonyManager.CALL_STATE_OFFHOOK;
                } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    state = TelephonyManager.CALL_STATE_RINGING;
                }


                onCallStateChanged(context, state, number);
            }
        }

        //Derived classes should override these to respond to specific events of interest
        protected abstract void onIncomingCallReceived(Context ctx, String number, Date start);

        protected abstract void onIncomingCallAnswered(Context ctx, String number, Date start);

        protected abstract void onIncomingCallEnded(Context ctx, String number, Date start, Date end);

        protected abstract void onOutgoingCallStarted(Context ctx, String number, Date start);

        protected abstract void onOutgoingCallEnded(Context ctx, String number, Date start, Date end);

        protected abstract void onMissedCall(Context ctx, String number, Date start);

        //Deals with actual events

        //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
        //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
        public void onCallStateChanged(Context context, int state, String number) {
            if (lastState == state) {
                //No change, debounce extras
                return;
            }
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    isIncoming = true;
                    callStartTime = new Date();
                    savedNumber = number;
                    onIncomingCallReceived(context, number, callStartTime);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                    if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                        isIncoming = false;
                        callStartTime = new Date();
                        startRecording();
                        onOutgoingCallStarted(context, savedNumber, callStartTime);
                    } else {
                        isIncoming = true;
                        callStartTime = new Date();
                        startRecording();
                        onIncomingCallAnswered(context, savedNumber, callStartTime);
                    }

                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                    if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                        //Ring but no pickup-  a miss
                        onMissedCall(context, savedNumber, callStartTime);
                    } else if (isIncoming) {
                        stopRecording();
                        onIncomingCallEnded(context, savedNumber, callStartTime, new Date());
                    } else {
                        stopRecording();
                        onOutgoingCallEnded(context, savedNumber, callStartTime, new Date());
                    }
                    break;
            }
            lastState = state;
        }

    }

    public class CallReceiver extends PhonecallReceiver {

        @Override
        protected void onIncomingCallReceived(Context ctx, String number, Date start) {
            Log.d("onIncomingCallReceived", number + " " + start.toString());
        }

        @Override
        protected void onIncomingCallAnswered(Context ctx, String number, Date start) {
            Log.d("onIncomingCallAnswered", number + " " + start.toString());
        }

        @Override
        protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
            Log.d("onIncomingCallEnded", number + " " + start.toString() + "\t" + end.toString());
        }

        @Override
        protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
            Log.d("onOutgoingCallStarted", number + " " + start.toString());
        }

        @Override
        protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
            Log.d("onOutgoingCallEnded", number + " " + start.toString() + "\t" + end.toString());
        }

        @Override
        protected void onMissedCall(Context ctx, String number, Date start) {
            Log.d("onMissedCall", number + " " + start.toString());
//        PostCallHandler postCallHandler = new PostCallHandler(number, "janskd" , "")
        }

    }

}

