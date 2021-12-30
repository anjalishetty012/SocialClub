package com.example.socialclub;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;


public class home extends Fragment {


    CheckBox c1, c2, c4, c5, c6, c7, c8, c9;
    EditText mfav;
    Button mDone;
    String UserID;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    Button mExplore;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mExplore=view.findViewById(R.id.explore);



        mDone = view.findViewById(R.id.Done);
        c1 = view.findViewById(R.id.c1);
        c2 = view.findViewById(R.id.c2);
        c4 = view.findViewById(R.id.c4);
        c5 = view.findViewById(R.id.c5);
        c6 = view.findViewById(R.id.c6);
        c7 = view.findViewById(R.id.c7);
        c8 = view.findViewById(R.id.c8);
        c9 = view.findViewById(R.id.c9);
        progressBar=view.findViewById(R.id.progressBar2);

        mfav = view.findViewById(R.id.fav);

        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();




        String l1 = "#food";
        String l2 = "#music";
        String l3 = "#gaming";
        String l4 = "#art";
        String l5 = "#nature";
        String l6 = "#finance";
        String l7 = "#animals";


        UserID = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(UserID);



        mDone.setOnClickListener(v -> {
            Map<String, Object> map = new HashMap<>();
            progressBar.setVisibility(View.VISIBLE);

            documentReference.update(map);

            if (c1.isChecked()) {
                map.put("fav", FieldValue.arrayUnion(l1));

               // SetOptions.merge();
            } else {
                map.put("fav", FieldValue.arrayRemove(l1));


            }
            documentReference.update(map);
            if (c2.isChecked()) {


                map.put("fav", FieldValue.arrayUnion(l2));

               // SetOptions.merge();

            } else {


                map.put("fav", FieldValue.arrayRemove(l2));
            }
            documentReference.update(map);
            if (c4.isChecked()) {

                map.put("fav", FieldValue.arrayUnion(l3));

               // SetOptions.merge();
            } else {

                map.put("fav", FieldValue.arrayRemove(l3));

            }
            documentReference.update(map);
            if (c5.isChecked()) {

                map.put("fav", FieldValue.arrayUnion(l4));

               // SetOptions.merge();
            } else {

                map.put("fav", FieldValue.arrayRemove(l4));


            }
            documentReference.update(map);
            if (c6.isChecked()) {

                map.put("fav", FieldValue.arrayUnion(l5));

              //  SetOptions.merge();
            } else {

                map.put("fav", FieldValue.arrayRemove(l5));

            }
            documentReference.update(map);
            if (c7.isChecked()) {

                map.put("fav", FieldValue.arrayUnion(l6));

               // SetOptions.merge();
            } else {

                map.put("fav", FieldValue.arrayRemove(l6));

            }
            documentReference.update(map);
            if (c8.isChecked()) {

                map.put("fav", FieldValue.arrayUnion(l7));

                //SetOptions.merge();
            } else {

                map.put("fav", FieldValue.arrayRemove(l7));

            }
            documentReference.update(map);
            String l8 = "#lifestyle";

            if (c9.isChecked()) {

                //map.put("name9", l8);
                map.put("fav", FieldValue.arrayUnion(l8));

              // SetOptions.merge();

            } else {

                map.put("fav", FieldValue.arrayRemove(l8));

            }
            documentReference.update(map);
            String favorite = mfav.getText().toString();


            if (TextUtils.isEmpty(favorite)) {

                map.put("fav", FieldValue.arrayRemove(favorite));



            } else {
                String[] favArray=favorite.split("\\s*,\\s*");//\\s* will remove the empty spaces

               // List<String> likes= Arrays.asList(favArray);


                map.put("fav", FieldValue.arrayUnion(favArray));

              //  SetOptions.merge();


            }
            documentReference.update(map);

            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(),"Done",Toast.LENGTH_SHORT).show();
            //mDone.setText("Update");







        });
        mExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), info_interest.class));

            }
        });



        return view;


    }
}
