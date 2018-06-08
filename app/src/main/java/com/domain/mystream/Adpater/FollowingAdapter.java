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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.domain.mystream.Constants.Configs;
import com.domain.mystream.Model.NewChatModel;
import com.domain.mystream.Activity.OtherUserProfile;
import com.domain.mystream.R;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.domain.mystream.Constants.Configs.editor;
import static com.domain.mystream.Constants.Configs.myPref;
import static com.domain.mystream.Constants.Configs.sharedPreferences;
import static com.domain.mystream.Constants.MyStreamApis.IMAGE_URL;


public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.PersonViewHolder> {


    Context context;
    List<NewChatModel> newChatModels1;
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

        }
    }

    public FollowingAdapter(Context context, List<NewChatModel> newChatModels1) {
        this.context = context;
        this.newChatModels1 = newChatModels1;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);


    }

    @Override
    public FollowingAdapter.PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_follow, parent, false);
        FollowingAdapter.PersonViewHolder pvh = new FollowingAdapter.PersonViewHolder(v);

         sharedPreferences = context.getSharedPreferences(myPref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userid = sharedPreferences.getString("userid", "0");

        return pvh;
    }

    int po = 0;

    @Override
    public void onBindViewHolder(final FollowingAdapter.PersonViewHolder holder, final int i) {
        // avatarImg, streamImg;


        final String url = IMAGE_URL + newChatModels1.get(i).getProfilePic();
        Glide.with(context)
                .load(url)
                .into(holder.avatarImg);


        holder.fullnameTxt.setText(newChatModels1.get(i).getFullName());
        holder.aboutTxt.setText(newChatModels1.get(i).getAbout());
        holder.usernameTxt.setText(newChatModels1.get(i).getUserName());


        holder.streamImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("img",url);
                editor.putInt("otherUserId", newChatModels1.get(i).getUserId());
                editor.apply();

                Intent myactivity = new Intent(context, OtherUserProfile.class);
                myactivity.addFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(myactivity);
            }
        });

    }


    @Override
    public int getItemCount() {
        return newChatModels1.size();
    }


}









