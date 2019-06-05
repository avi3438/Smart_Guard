package com.hbeonlabs.smartguard.v2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.database.collection.LLRBNode;

import java.util.List;

public class RecyclerView_Config extends AppCompatDialogFragment {

    private  static  final String TAG="RecyclerView_Config";
    public static String tempph,tempid,tempid2,temp_toggle_msg;

    private Context mcontext;
    private DataAdapter mDataAdapter;






    public void setConfig(RecyclerView recyclerView, Context context,List<DataManager> data, List<String> keys){
        mcontext=context;
        mDataAdapter=new DataAdapter(data,keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mDataAdapter);
    }

    class DataItemView extends RecyclerView.ViewHolder{

        public  TextView mph,mid,msendon,msendoff,mrecon,mrecoff;
        private Button mbtn;
        private  String key;
        private TextView mstatus;
        private ImageView mdelete;

        public  DataItemView(ViewGroup parent){
            super(LayoutInflater.from(mcontext).
                    inflate(R.layout.data_list_design,parent,false));

            mid=(TextView) itemView.findViewById(R.id.etListPumpId);
            mph=(TextView) itemView.findViewById(R.id.etListMob);
            mbtn=(Button) itemView.findViewById(R.id.btnListOnOff);
            mdelete=(ImageView)itemView.findViewById(R.id.imvDelete);
            mstatus=(TextView)itemView.findViewById(R.id.etStatus);
            //TEXT fields for send and receive
            msendon=(TextView)itemView.findViewById(R.id.etON);
            msendoff=(TextView)itemView.findViewById(R.id.etOFF);
            mrecon=(TextView)itemView.findViewById(R.id.etONReceive);
            mrecoff=(TextView)itemView.findViewById(R.id.etOFFReceive);


        }
        public void bind(DataManager data, String key){
            mid.setText(data.getPump_id());
            mph.setText(data.getPh());

            if(data.getPump_status()==false){
             mstatus.setText("Status: OFF");
             mbtn.setBackgroundColor(Color.parseColor("#008577"));
             mbtn.setTextColor(Color.parseColor("white"));
             mbtn.setText("ON");
            }
            if(data.getPump_status()){
                mstatus.setText("Status: ON");
                mbtn.setText("OFF");
                mbtn.setBackgroundColor(Color.parseColor("#b70125"));
                mbtn.setTextColor(Color.parseColor("white"));
            }
            this.key=key;

        }
    }
    class DataAdapter extends RecyclerView.Adapter<DataItemView>{

        private List<DataManager> mdatalist;
        private List<String> mkeys;

        public DataAdapter(List<DataManager> mdatalist, List<String> mkeys) {
            this.mdatalist = mdatalist;
            this.mkeys = mkeys;
        }

        @NonNull
        @Override
        public DataItemView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new DataItemView(viewGroup);
        }


        @Override
        public void onBindViewHolder(@NonNull final DataItemView dataItemView, final int i) {

            dataItemView.bind(mdatalist.get(i),mkeys.get(i));
           final DataManager currentdata =mdatalist.get(i);


            //Clicklistener for sending messages
            dataItemView.mbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dataItemView.mph.setText(currentdata.getPh());

                    tempid= currentdata.getId();
                    Boolean tempstatus;
                    tempstatus=currentdata.getPump_status();
                    String msg;
                    if(tempstatus){
                        msg =currentdata.getOff_send() ;
                        temp_toggle_msg=currentdata.getOff_receive();
                    }
                    else{
                        msg=currentdata.getOn_send();
                        temp_toggle_msg=currentdata.getOn_receive();
                    }

                    int permissionCheck= ContextCompat.checkSelfPermission(mcontext, Manifest.permission.SEND_SMS);
                    if(permissionCheck== PackageManager.PERMISSION_GRANTED){
                        Log.d(TAG,"permission granted");

                        String phno = dataItemView.mph.getText().toString().trim(); tempph= new String(phno);

                        if (!phno.equals("")) {
                            Log.d(TAG, "message sending");
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(phno, null, msg, null, null);
                            Toast.makeText(mcontext, "Message Sent", Toast.LENGTH_SHORT).show();
                        }


                    }
                    Log.d(TAG,"button active");


                }


            });

            dataItemView.mdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tempid2=currentdata.getId();

                    AlertDialog.Builder builder= new AlertDialog.Builder(mcontext);
                    builder.setTitle("Attention !")
                            .setMessage("Do you really want to delete this field ?").setCancelable(false)
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    DatabaseReference dbref= FirebaseDatabase.getInstance().
                                            getReference("Data").child(tempid2);
                                    dbref.removeValue();
                                    Toast.makeText(mcontext, "Data deleted from Database", Toast.LENGTH_LONG).show();

                                }
                            });

                    AlertDialog alert=builder.create();
                    alert.show();







                }
            });

        }

        @Override
        public int getItemCount() {
            return mdatalist.size();
        }


    }
}
