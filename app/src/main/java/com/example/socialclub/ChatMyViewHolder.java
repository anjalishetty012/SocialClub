package com.example.socialclub;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMyViewHolder extends RecyclerView.ViewHolder {
    CircleImageView firstUserProfile,secondUserProfile;
    TextView firstUserText,secondUserText;


    public ChatMyViewHolder(@NonNull View itemView) {
        super(itemView);
        firstUserText=itemView.findViewById(R.id.firstUserText);
        firstUserProfile=itemView.findViewById(R.id.firstUserProfile);
        secondUserText=itemView.findViewById(R.id.secondUserText);
        secondUserProfile=itemView.findViewById(R.id.secondUserProfile);

    }
}
