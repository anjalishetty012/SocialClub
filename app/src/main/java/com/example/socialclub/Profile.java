package com.example.socialclub;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import static com.example.socialclub.Register.TAG;



public class Profile extends Fragment {
    TextView fullName,email,phone;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String UserID;
    ImageView mprofileimage;
    TextView meditprofile;
    StorageReference storageReference;
    Button mlogout;






    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile, container, false);

        // Inflate the layout for this fragment

        mlogout=(Button)view.findViewById(R.id.button);

        fullName= view.findViewById(R.id.profilename);
        email= view.findViewById(R.id.profileemail);
        phone= view.findViewById(R.id.profilephone);

        meditprofile=view.findViewById(R.id.editprofile);
        mprofileimage=view.findViewById(R.id.profileimage);

        fAuth= FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        storageReference= FirebaseStorage.getInstance().getReference();

        StorageReference profileRef=storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"profile.jpg");//each user will have their own profile picture
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(mprofileimage);
            }
        });

        UserID= fAuth.getCurrentUser().getUid();

        DocumentReference documentReference=fStore.collection("users").document(UserID);
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            phone.setText(documentSnapshot.getString("phone"));
                            email.setText(documentSnapshot.getString("email"));
                            fullName.setText(documentSnapshot.getString("fName"));
                        } else {
                            Toast.makeText(Profile.this.getActivity(), "Profile does not exist", Toast.LENGTH_SHORT).show();
                        }


                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(),"Error!",Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());

                });

        meditprofile.setOnClickListener(v -> {
            //open gallery
            //Intent openGalleryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //startActivityForResult(openGalleryIntent,1000);
            Intent i=new Intent(v.getContext(), EditProfile.class);
            i.putExtra("fullName",fullName.getText().toString());
            i.putExtra("email",email.getText().toString());
            i.putExtra("phone",phone.getText().toString());
            startActivity(i);
        });

        mlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();//logout
                getActivity().finish();
                startActivity(new Intent(getActivity(),Login.class));


            }
        });



        return view;





    }



}




