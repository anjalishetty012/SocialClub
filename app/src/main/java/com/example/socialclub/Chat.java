package com.example.socialclub;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.socialclub.Register.TAG;

public class Chat extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerview;
    EditText inputMessage;
    ImageView btnSend;
    CircleImageView userProfileImageAppbar;
    TextView usernameAppbar;
    String OtherUserID;
    DocumentReference documentReference;
    DatabaseReference smsRef;
    String Url="https://fcm.googleapis.com/fcm/send";
    RequestQueue requestQueue;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String UserID;
    String UserImage,Username;
    String OtherUsername,OtherProfileImage;
    FirebaseRecyclerOptions<chatModel> options;
    FirebaseRecyclerAdapter<chatModel,ChatMyViewHolder>adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerview=findViewById(R.id.recyclerview);
        toolbar=findViewById(R.id.toolbar);

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        inputMessage=findViewById(R.id.inputMessage);
        btnSend=findViewById(R.id.btnSend);

        usernameAppbar=findViewById(R.id.usernameAppbar);
        userProfileImageAppbar=findViewById(R.id.userProfileImageAppbar);

        fAuth= FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        UserID= fAuth.getCurrentUser().getUid();

        OtherUserID=getIntent().getStringExtra("OtherUserID");
        smsRef= FirebaseDatabase.getInstance().getReference().child("Message");
        requestQueue= Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic(UserID);










        LoadOtherUser();
        LoadMyProfile();

        btnSend.setOnClickListener(v -> SendSMS());

        Loadsms();







    }
    private void LoadOtherUser() {
        documentReference=fStore.collection("users").document(OtherUserID);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                OtherUsername=documentSnapshot.getString("fName");
                OtherProfileImage=documentSnapshot.getString("image");
                Picasso.get().load(OtherProfileImage).into(userProfileImageAppbar);
                usernameAppbar.setText(OtherUsername);
            } else {
                Toast.makeText(Chat.this, "Profile does not exist", Toast.LENGTH_SHORT).show();
            }


        }).addOnFailureListener(e -> {
            Toast.makeText(Chat.this,"Error!",Toast.LENGTH_SHORT).show();
            Log.d(TAG, e.toString());

        });
    }

    private void LoadMyProfile() {
        documentReference=fStore.collection("users").document(UserID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Username=documentSnapshot.getString("fName");
                    UserImage=documentSnapshot.getString("image");


                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Chat.this, "Error!"+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });



    }
    private void Loadsms() {
        options=new FirebaseRecyclerOptions.Builder<chatModel>().setQuery(smsRef.child(UserID).child(OtherUserID),chatModel.class).build();
        adapter=new FirebaseRecyclerAdapter<chatModel, ChatMyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatMyViewHolder holder, int position, @NonNull chatModel model) {
                if (model.getUserID().equals(UserID)){
                    holder.firstUserText.setVisibility(View.GONE);
                    holder.firstUserProfile.setVisibility(View.GONE);
                    holder.secondUserProfile.setVisibility(View.VISIBLE);
                    holder.secondUserText.setVisibility(View.VISIBLE);

                    holder.secondUserText.setText(model.getSms());
                    Picasso.get().load(UserImage).into(holder.secondUserProfile);
                }
                else{
                    holder.firstUserText.setVisibility(View.VISIBLE);
                    holder.firstUserProfile.setVisibility(View.VISIBLE);
                    holder.secondUserProfile.setVisibility(View.GONE);
                    holder.secondUserText.setVisibility(View.GONE);

                    holder.firstUserText.setText(model.getSms());
                    Picasso.get().load(OtherProfileImage).into(holder.firstUserProfile);
                }
            }

            @NonNull
            @Override
            public ChatMyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singleview_sms,parent,false);
                return new ChatMyViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerview.setAdapter(adapter);
    }

    private void SendSMS() {
        String sms=inputMessage.getText().toString();
        if(sms.isEmpty()){
            Toast.makeText(this, "Please type message", Toast.LENGTH_SHORT).show();
        }
        else{
            HashMap hashMap=new HashMap();
            hashMap.put("sms",sms);
            hashMap.put("status","unseen");
            hashMap.put("userID",UserID);
            smsRef.child(OtherUserID).child(UserID).push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    smsRef.child(UserID).child(OtherUserID).push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                sendNotification(sms);
                                inputMessage.setText(null);
                                Toast.makeText(Chat.this, "Message sent", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            });
        }
    }

    private void sendNotification(String sms) {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("to","/topics/"+OtherUserID);
            JSONObject jsonObject1=new JSONObject();
            jsonObject1.put("title","Message from "+Username);
            jsonObject1.put("body",sms);

            JSONObject jsonObject2=new JSONObject();
            jsonObject2.put("userID",UserID);
            jsonObject2.put("type","sms");

            jsonObject.put("notification",jsonObject1);
            jsonObject.put("data",jsonObject2);

            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, Url,jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, error -> {

            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String>map=new HashMap<>();
                    map.put("content-type","application/json");
                    map.put("authorization","key=AAAAIhCFqm0:APA91bEEvl3YvgW4hsYCQ2WxvYW5FhGZmFy2fyZSdjNBX6_bbUD7XAkC5GVTop7O4vRst5nlgoT8xobTQxKAOIzIYVl5ywju_8yDTZShlVhpMAXvAC8mC3gDj8z2ZI5WZzcSDouFqt8B");
                    return map;
                }
            };
            requestQueue.add(request);

        } catch (JSONException e){
            e.printStackTrace();
        }



    }


}