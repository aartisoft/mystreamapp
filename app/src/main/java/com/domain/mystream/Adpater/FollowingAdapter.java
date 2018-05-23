package com.domain.mystream.Adpater;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.domain.mystream.Configs;
import com.domain.mystream.Model.NewChatModel;
import com.domain.mystream.OtherUserProfile;
import com.domain.mystream.R;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.domain.mystream.Login.editor;
import static com.domain.mystream.Login.myPref;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.PersonViewHolder> {


    Context context;
    NewChatModel[] newChatModels;
    String userid;
    String identifier;
    CheckBox checkBox;
    Boolean ischecked = false;

    public class PersonViewHolder extends RecyclerView.ViewHolder {

        private ImageView avatarImg, streamImg;
        private TextView fullnameTxt, usernameTxt, aboutTxt;
        private Button optionsButt;


        public PersonViewHolder(View rowView) {

            super(rowView);

            avatarImg = rowView.findViewById(R.id.cfAvatarimg);
            fullnameTxt = rowView.findViewById(R.id.cfFullnameTxt);
            fullnameTxt.setTypeface(Configs.titSemibold);
            usernameTxt = rowView.findViewById(R.id.cfUsernameTxt);
            usernameTxt.setTypeface(Configs.titRegular);

            aboutTxt = rowView.findViewById(R.id.cfAboutTxt);
            aboutTxt.setTypeface(Configs.titRegular);
            streamImg = rowView.findViewById(R.id.imageView7);
            streamImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myactivity = new Intent(context, OtherUserProfile.class);
                    myactivity.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(myactivity);
                    }
            });
        }
    }

    public FollowingAdapter(Context context, NewChatModel[] newChatModels) {
        this.context = context;
        this.newChatModels = newChatModels;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);


    }

    @Override
    public FollowingAdapter.PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_follow, parent, false);
        FollowingAdapter.PersonViewHolder pvh = new FollowingAdapter.PersonViewHolder(v);

        SharedPreferences sharedPreferences = context.getSharedPreferences(myPref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userid = sharedPreferences.getString("userid", "0");

        return pvh;
    }

    int po = 0;

    @Override
    public void onBindViewHolder(final FollowingAdapter.PersonViewHolder holder, final int i) {
        // avatarImg, streamImg;


        String url = "https://qas.veamex.com" + newChatModels[i].getProfilePic();
        Glide.with(context)
                .load(url)
                .into(holder.avatarImg);


        holder.fullnameTxt.setText(newChatModels[i].getFullName());
        holder.aboutTxt.setText(newChatModels[i].getAbout());
        holder.usernameTxt.setText(newChatModels[i].getUserName());




    }


    @Override
    public int getItemCount() {
        return newChatModels.length;
    }


}









