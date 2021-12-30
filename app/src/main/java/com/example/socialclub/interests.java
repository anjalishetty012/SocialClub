package com.example.socialclub;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.socialclub.R.id;
import static com.example.socialclub.R.mipmap;


public class interests extends Fragment {



    RecyclerView.LayoutManager layoutManager;
    FirebaseAuth fAuth;
    FirebaseUser mUser;
    CircleImageView mimage;
    TextView name,email;
    TextView message;







    //ProgressDialog pd;
    EditText msearch;

    RecyclerView mresultlist;






    FirestoreRecyclerAdapter recyclerAdapter;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_interests, container, false);
        message=v.findViewById(R.id.textView5);

        mimage=v.findViewById(R.id.ProfileImage);
        mresultlist = v.findViewById(id.resultlist);
        mresultlist.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        mresultlist.setLayoutManager(layoutManager);


        // Inflate the layout for this fragment
        msearch = v.findViewById(R.id.search);
       // msearchbtn = v.findViewById(R.id.searchbtn);
        mresultlist = (RecyclerView) v.findViewById(R.id.resultlist);

        mimage=v.findViewById(id.ProfileImage);
        name=v.findViewById(id.username);
        email=v.findViewById(id.email);


        fAuth = FirebaseAuth.getInstance();
        mUser=fAuth.getCurrentUser();

        mresultlist.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mresultlist.setLayoutManager(layoutManager);
        message.setVisibility(View.VISIBLE);


        firestoreUserSearch("");
        msearch.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                message.setVisibility(View.GONE);



            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString()!=null){
                    firestoreUserSearch(s.toString());
                }
                else{
                    firestoreUserSearch("");
                }

            }
        });










        return v;


    }








       





    private void firestoreUserSearch(String data) {
        CollectionReference collectionReference=fStore.collection("users");


        Query query = collectionReference.whereArrayContains("fav",data);




        FirestoreRecyclerOptions<Member> options = new FirestoreRecyclerOptions.Builder<Member>()
                .setQuery(query, Member.class)
                .build();




       // Toast.makeText(getActivity(), "Started searching", Toast.LENGTH_SHORT).show();

       // Query query = fStore.collection("users").orderBy("fName").startAt(searchText).endAt(searchText + '\uf8ff');
        recyclerAdapter = new FirestoreRecyclerAdapter<Member, UsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Member model) {
                String docId = getSnapshots().getSnapshot(position).getId();
                if(!mUser.getUid().equals(docId)){
                    holder.name.setText(model.getfName());
                    holder.email.setText(model.getEmail());
                    Picasso.get().load(model.getImage()).placeholder(mipmap.ic_launcher_round).fit().centerCrop().into(holder.image);


                }
                else{
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                }
                holder.itemView.setOnClickListener(v -> {
                    Intent i=new Intent(v.getContext(), ViewFriendActivity.class);
                    i.putExtra("userkey",docId);
                    /*i.putExtra("fullName",name.getText().toString());
                    i.putExtra("email",email.getText().toString());*/
                  //  i.putExtra("image",mimage.g().toString());
                    startActivity(i);

                });








            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);

                return new UsersViewHolder(view);
            }
        };
        recyclerAdapter.startListening();
        mresultlist.setAdapter(recyclerAdapter);


    }



    //viewholder class

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView email;
        ImageView image;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.username);
            email = itemView.findViewById(R.id.email);
            image=itemView.findViewById(R.id.ProfileImage);




        }
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerAdapter.startListening();


    }

    @Override
    public void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();
    }
}










