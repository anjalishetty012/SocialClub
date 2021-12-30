package com.example.socialclub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewFriendActivity extends AppCompatActivity {
   // EditText profileFullNam
    CollectionReference reference;
    DatabaseReference requestRef,friendref;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    FirebaseUser mUser;
    String profileimageUrl,username,email;
    Button btnPerform,btnDecline;

    CircleImageView pImage;
    TextView pName,pEmail;
    String userID;
    String myProfileImage,myEmail,myUsername;

    String CurrentState="No_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend);
        userID=getIntent().getStringExtra("userkey");
        Toast.makeText(this, ""+userID, Toast.LENGTH_SHORT).show();

        fStore=FirebaseFirestore.getInstance();
        fAuth=FirebaseAuth.getInstance();
        reference=fStore.collection("users");
        requestRef = FirebaseDatabase.getInstance().getReference().child("requests");

        friendref=FirebaseDatabase.getInstance().getReference().child("friends");
        mUser=fAuth.getCurrentUser();

        pImage=findViewById(R.id.pimage);
        pName=findViewById(R.id.pUsername);
        pEmail=findViewById(R.id.pEmail);
        btnPerform=findViewById(R.id.btnPerform);
        btnDecline=findViewById(R.id.btnDecline);




        LoadUser();
        LoadMyProfile();


        btnPerform.setOnClickListener(v -> PerformAction(userID));
        CheckUserExist(userID);

        btnDecline.setOnClickListener(v -> Unfriend(userID));




    }


    private void LoadMyProfile() {
        reference.document(mUser.getUid()).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()) {
                myProfileImage=documentSnapshot.get("image").toString();
                myUsername = documentSnapshot.get("fName").toString();
                myEmail = documentSnapshot.get("email").toString();
              //  Picasso.get().load("image").placeholder(R.drawable.ic_person).into(myProfileImage);


                /*Picasso.get().load("image").placeholder(R.drawable.ic_person).into(pImage);
                pName.setText(username);
                pEmail.setText(email);*/
            }
            else{
                Toast.makeText(ViewFriendActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void Unfriend(String userID) {
        if(CurrentState.equals("friend")){
            friendref.child(mUser.getUid()).child(userID).removeValue().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    friendref.child(userID).child(mUser.getUid()).removeValue().addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful()){
                            Toast.makeText(ViewFriendActivity.this, "No longer friends", Toast.LENGTH_SHORT).show();
                            CurrentState="No_Activity";
                            btnPerform.setText("Send Friend Request");
                            btnDecline.setVisibility(View.GONE);

                        }

                    });
                }
            });

        }
        if(CurrentState.equals("he_sent_pending")){
            HashMap hashMap=new HashMap();
            hashMap.put("status","decline");

            requestRef.child(userID).child(mUser.getUid()).updateChildren(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                {
                    Toast.makeText(ViewFriendActivity.this, "You have declined friend request", Toast.LENGTH_SHORT).show();
                    CurrentState="he_sent_decline";
                    btnDecline.setVisibility(View.GONE);
                    btnPerform.setVisibility(View.GONE);
                }


            });
        }
    }

    private void CheckUserExist(String userID) {

        friendref.child(mUser.getUid()).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    CurrentState = "friend";
                    btnPerform.setText("Send Hello");
                    btnDecline.setText("Unfriend");
                    btnDecline.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        friendref.child(userID).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    CurrentState = "friend";
                    btnPerform.setText("Send Hello");
                    btnDecline.setText("Unfriend");
                    btnDecline.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







        requestRef.child(mUser.getUid()).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("status").getValue().toString().equals("pending")){
                        CurrentState="i_sent_pending";
                        btnPerform.setText("Cancel Friend request");
                        btnDecline.setVisibility(View.GONE);
                    }
                    if(snapshot.child("status").getValue().toString().equals("decline")){
                        CurrentState="i_sent_decline";
                        btnPerform.setText("Cancel Friend request");
                        btnDecline.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        requestRef.child(userID).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("status").getValue().toString().equals("pending")){
                        CurrentState="he_sent_pending";
                        btnPerform.setText("Accept friend request");
                        btnDecline.setText("Decline");
                        btnDecline.setVisibility(View.VISIBLE);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        if(CurrentState.equals("No_Activity")){

            btnPerform.setText("Send Friend Request");
            btnDecline.setVisibility(View.GONE);
        }
    }

    private void PerformAction(String userID) {
        if (CurrentState.equals("No_Activity")) {



            HashMap hashMap=new HashMap();
            hashMap.put("status", "pending");


            requestRef.child(mUser.getUid()).child(userID).updateChildren(hashMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ViewFriendActivity.this, "You have sent friend request", Toast.LENGTH_SHORT).show();
                                btnDecline.setVisibility(View.GONE);
                                CurrentState = "i_sent_pending";
                                btnPerform.setText("Cancel Friend Request");

                            } else {
                                Toast.makeText(ViewFriendActivity.this, "" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
        if (CurrentState.equals("i_sent_pending") || CurrentState.equals("i_sent_decline")) {
            requestRef.child(mUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ViewFriendActivity.this, "Cancelled friend request", Toast.LENGTH_SHORT).show();
                        CurrentState = "No_Activity";
                        btnPerform.setText("Send Friend Request");
                        btnDecline.setVisibility(View.GONE);


                    } else {
                        Toast.makeText(ViewFriendActivity.this, "" + task.getException().toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
        if (CurrentState.equals("he_sent_pending")) {
            requestRef.child(userID).child(mUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        HashMap hashMap=new HashMap();
                        hashMap.put("status", "friend");
                        hashMap.put("username",username);
                        hashMap.put("image",profileimageUrl);

                        final HashMap hashMap1=new HashMap();
                        hashMap1.put("status","friend");
                        hashMap1.put("username",myUsername);
                       // hashMap1.put("email",myEmail);
                        hashMap1.put("image",myProfileImage);



                        friendref.child(mUser.getUid()).child(userID).updateChildren(hashMap).addOnCompleteListener(task12 -> {
                            if (task12.isSuccessful()) {



                                friendref.child(userID).child(mUser.getUid()).updateChildren(hashMap1).addOnCompleteListener((OnCompleteListener<Void>) task1 -> {
                                    Toast.makeText(ViewFriendActivity.this, "You added friend", Toast.LENGTH_SHORT).show();
                                    CurrentState = "friend";
                                    btnPerform.setText("Send Hello");
                                    btnDecline.setText("Unfriend");
                                    btnDecline.setVisibility(View.VISIBLE);
                                });
                            }
                        });

                    }

                }
            });
        }
        if (CurrentState.equals("friend")) {
            Intent intent=new Intent(ViewFriendActivity.this,Chat.class);
            intent.putExtra("OtherUserID",userID);
            startActivity(intent);

        }

    }
    private void LoadUser() {

        reference.document(userID).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()) {
                profileimageUrl=documentSnapshot.getString("image");
                username = documentSnapshot.get("fName").toString();
                email = documentSnapshot.get("email").toString();


                Picasso.get().load(profileimageUrl).placeholder(R.drawable.ic_person).into(pImage);
                pName.setText(username);
                pEmail.setText(email);
            }
            else{
                Toast.makeText(ViewFriendActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            }


        });





    }



}