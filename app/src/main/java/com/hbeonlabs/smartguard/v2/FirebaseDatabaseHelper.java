package com.hbeonlabs.smartguard.v2;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefrenceDataManager;
    private List<DataManager> data=new ArrayList<>();

    public interface DataStatus{
        void  DataIsLoaded(List<DataManager> data,List<String> keys);
        void  DataIsInserted();
        void  DataIsUpdated();
        void  DataIsDeleted();
    }

    public FirebaseDatabaseHelper() {
        mDatabase= FirebaseDatabase.getInstance();
        mRefrenceDataManager =mDatabase.getReference("Data");

    }

    public void readData(final DataStatus dataStatus){
        mRefrenceDataManager.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                List<String> keys=new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    DataManager newdata=keyNode.getValue(DataManager.class);
                    data.add(newdata);
                }

                dataStatus.DataIsLoaded(data,keys);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
