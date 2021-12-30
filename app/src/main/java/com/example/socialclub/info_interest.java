package com.example.socialclub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class info_interest extends AppCompatActivity {
    //create list of type slideritems
    List<slideritems> slideritems=new ArrayList<>();
    ArrayList<String>title=new ArrayList<>();
    ArrayList<String>desc=new ArrayList<>();
    ArrayList<String>image=new ArrayList<>();
    ArrayList<String>newslink=new ArrayList<>();
   CollectionReference collectionReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_interest);



        final VerticalViewPager verticalViewPager=(VerticalViewPager) findViewById(R.id.verticalViewPager);

        collectionReference= FirebaseFirestore.getInstance().collection("info");


        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                    title.add(documentSnapshot.get("title").toString());
                    desc.add(documentSnapshot.get("desc").toString());
                    newslink.add(documentSnapshot.get("newslink").toString());
                    image.add(documentSnapshot.get("image").toString());

                }
                for (int i=0;i<image.size();i++)
                {
                    slideritems.add(new slideritems(image.get(i)));






                }
                verticalViewPager.setAdapter(new ViewPagerAdapter(info_interest.this,slideritems,title,desc,newslink,verticalViewPager));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });





    }
}