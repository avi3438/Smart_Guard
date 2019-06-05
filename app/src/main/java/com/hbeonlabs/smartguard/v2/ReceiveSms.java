package com.hbeonlabs.smartguard.v2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ReceiveSms extends BroadcastReceiver {

    private  static  final String TAG="ReceiveSms";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseRef = database.getReference().child("Data").child(RecyclerView_Config.tempid);
    public static String pstat=null;


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle=intent.getExtras();
            SmsMessage[] msgs;
            String msg_from;
            if(bundle!=null) try {
                Object[] pdus = (Object[]) bundle.get("pdus");
                msgs = new SmsMessage[pdus.length];
                for (int i = 0; i < msgs.length; i++) {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    msg_from = msgs[i].getOriginatingAddress();
                    String msgBody = msgs[i].getMessageBody();
                    String ph = RecyclerView_Config.tempph.toString();

                    if (msg_from.equals(ph) && (msgBody.equals(RecyclerView_Config.temp_toggle_msg))) {

                        Toast.makeText(context, "Sms Received updating status", Toast.LENGTH_SHORT).show();


                            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                   pstat=dataSnapshot.child("pump_status").getValue().toString();
                                   Log.d(TAG,"initial status: "+pstat);


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        if(pstat.equals("false")) {
                            mDatabaseRef.child("pump_status").setValue(true);
                            pstat="true";
                            Log.d(TAG, "new status: "+pstat);

                        }
                        else if(pstat.equals("true"))
                        {
                            mDatabaseRef.child("pump_status").setValue(false);
                            pstat="false";
                            Log.d(TAG, "new status: "+pstat);
                        }

                        pstat=null;



                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}