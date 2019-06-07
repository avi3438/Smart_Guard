package com.hbeonlabs.smartguard.v2;
import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private  static  final String TAG="MainActivity";

    private Button btnfab,btn;

    private EditText pumpid,phno,on_send_text,off_send_text,on_receive_text,off_receive_text;
    private Button btnCan,btnOK;
    int Permission_All=1;

    //Extraction from database
    private RecyclerView mRecyclerView;


    FirebaseDatabase db=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      // btnfab=findViewById(R.id.fab);
        btn=findViewById(R.id.btn);

        databaseReference=db.getReference("Data");

        // permissions
        String[] Permissions= {Manifest.permission.SEND_SMS,Manifest.permission.RECEIVE_SMS};
        if(!haspermissions(this,Permissions)){
            ActivityCompat.requestPermissions(this, Permissions, Permission_All);
        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onclick..Opening dialoge");

                final Dialog dialog=new Dialog(MainActivity.this);
                dialog.setTitle("Register Details");
                dialog.setContentView(R.layout.customdialog);
                dialog.setCancelable(false);
                dialog.show();



                pumpid=dialog.findViewById(R.id.etPumpID);
                phno=dialog.findViewById(R.id.etPhoneNo);
                on_send_text=dialog.findViewById(R.id.etON);
                off_send_text=dialog.findViewById(R.id.etOFF);
                on_receive_text=dialog.findViewById(R.id.etONReceive);
                off_receive_text=dialog.findViewById(R.id.etOFFReceive);
                btnCan=dialog.findViewById(R.id.btnCan);
                btnOK=dialog.findViewById(R.id.btnSumbit);

                btnCan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.cancel();
                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        getdetails();
                        dialog.hide();

                    }
                });

            }


        });

        mRecyclerView=findViewById(R.id.rvData);
        new FirebaseDatabaseHelper().readData(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<DataManager> data, List<String> keys) {
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                new RecyclerView_Config().setConfig(mRecyclerView,MainActivity.this,data,keys);
            }

            @Override
            public void DataIsInserted() {


            }

            @Override
            public void DataIsUpdated() {


            }

            @Override
            public void DataIsDeleted() {

            }
        });



    }
    public void getdetails(){


        String pid=pumpid.getText().toString().trim();
        String mob=phno.getText().toString().replaceAll("\\s","");
        String onsend=on_send_text.getText().toString().trim();
        String offsend=off_send_text.getText().toString().trim();
        String onrec=on_receive_text.getText().toString().trim();
        String offrec=off_receive_text.getText().toString().trim();

        String id=databaseReference.push().getKey();

        if(!TextUtils.isEmpty(pid) || !TextUtils.isEmpty(mob)){



            DataManager data = new DataManager(pid,mob,id,onsend,offsend,onrec,offrec,false);
            databaseReference.child(id).setValue(data);
            Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
        }


    }
    public static boolean haspermissions(Context context, String... permissions){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && context!=null && permissions!=null){
            for(String permission: permissions){
                if(ActivityCompat.checkSelfPermission(context,permission)!= PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }


        return true;

    }


}
