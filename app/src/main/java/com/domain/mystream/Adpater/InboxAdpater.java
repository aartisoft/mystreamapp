package com.domain.mystream.Adpater;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.domain.mystream.Configs;
import com.domain.mystream.InboxActivity;
import com.domain.mystream.Model.Chat;
import com.domain.mystream.Model.GetChatModel;
import com.domain.mystream.Model.InboxChat;
import com.domain.mystream.Model.LastChatMessage;
import com.domain.mystream.Model.MessageModel;
import com.domain.mystream.Model.PostModel;
import com.domain.mystream.Model.Sender;
import com.domain.mystream.R;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.domain.mystream.Login.editor;
import static com.domain.mystream.Login.myPref;
import static com.domain.mystream.Login.sharedPreferences;

public class InboxAdpater extends RecyclerView.Adapter<InboxAdpater.PersonViewHolder> {


    Context context;

    List<PostModel> postModelList;

    Integer commentId;
    GetChatModel[] getChatModel;
    String userid;
    RelativeLayout senderCell, receiverCell;


    public class PersonViewHolder extends RecyclerView.ViewHolder {

        private ImageView avatarImg, savatarImg;
        private TextView messTxt, dateTxt, usernTxt, susernTxt, smessTxt, sdateTxt;
        private Button optionsButt;


        public PersonViewHolder(View rowView) {

            super(rowView);
            ////RECEVIR CELL
            usernTxt = rowView.findViewById(R.id.rUsernameTxt);
            usernTxt.setTypeface(Configs.titSemibold);

            // Get message
            messTxt = rowView.findViewById(R.id.rMessTxt);
            messTxt.setTypeface(Configs.titLight);

            // Get date
            dateTxt = rowView.findViewById(R.id.rDateTxt);
            dateTxt.setTypeface(Configs.titLight);

            // Get avatar
            avatarImg = rowView.findViewById(R.id.rAvatarImg);

///////////SENDER CELL

            susernTxt = rowView.findViewById(R.id.sUsernameTxt);
            susernTxt.setTypeface(Configs.titSemibold);

            // Get message
            smessTxt = rowView.findViewById(R.id.sMessTxt);
            smessTxt.setTypeface(Configs.titLight);

            // Get date
            sdateTxt = rowView.findViewById(R.id.sDateTxt);
            sdateTxt.setTypeface(Configs.titLight);

            // Get avatar
            savatarImg = rowView.findViewById(R.id.sAvatarImg);

            senderCell = rowView.findViewById(R.id.senderCell);
            receiverCell = rowView.findViewById(R.id.receiverCell);


        }
    }

    public InboxAdpater(Context context, GetChatModel[] getChatModel) {
        this.context = context;
        this.getChatModel = getChatModel;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);


    }

    @Override
    public InboxAdpater.PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_inbox, parent, false);
        InboxAdpater.PersonViewHolder pvh = new InboxAdpater.PersonViewHolder(v);

        SharedPreferences sharedPreferences = context.getSharedPreferences(myPref, Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();

        userid = sharedPreferences.getString("userid", "0");

        return pvh;
    }


    @Override
    public void onBindViewHolder(final InboxAdpater.PersonViewHolder holder, final int i) {
        // avatarImg, streamImg;




        Integer senderId =getChatModel[i].getSenderId();

        if (userid.equals(String.valueOf(senderId))) {

            senderCell.setVisibility(View.VISIBLE);
            holder.smessTxt.setText(getChatModel[i].getMessageBody());
            holder.susernTxt.setText(getChatModel[i].getSender().getFullName());
            holder.sdateTxt.setText(getChatModel[i].getSentOn());

            String url ="https://qas.veamex.com"+getChatModel[i].getSender().getProfilePic();
            Glide.with(context).load(url).into(holder.savatarImg);



        } else {
            receiverCell.setVisibility(View.VISIBLE);
            holder.messTxt.setText(getChatModel[i].getMessageBody());
            holder.usernTxt.setText(getChatModel[i].getReceiver().getFullName());
            holder.dateTxt.setText(getChatModel[i].getSentOn());
            String url ="https://qas.veamex.com"+getChatModel[i].getReceiver().getProfilePic();
            Glide.with(context).load(url).into(holder.avatarImg);

        }


        // end userPointer


    }


    @Override
    public int getItemCount() {
        return getChatModel.length;
    }

}









