package com.domain.mystream.Adpater;

import android.content.Context;
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
import com.domain.mystream.Constants.Configs;
import com.domain.mystream.Model.NewChatModel;
import com.domain.mystream.R;

import static com.domain.mystream.Constants.Configs.editor;
import static com.domain.mystream.Constants.Configs.myPref;
import static com.domain.mystream.Constants.Configs.sharedPreferences;
import static com.domain.mystream.Constants.MyStreamApis.IMAGE_URL;


public class NewChatAdapter extends RecyclerView.Adapter<NewChatAdapter.PersonViewHolder> {


    Context context;
    NewChatModel[] newChatModels;
    String userid;
    String identifier;
    CheckBox checkBox;
    Boolean ischecked = false;

    public class PersonViewHolder extends RecyclerView.ViewHolder {

        private ImageView avatarImg, streamImg;
        private TextView fullnameTxt, dateTxt, senderTxt;
        private Button optionsButt;


        public PersonViewHolder(View rowView) {

            super(rowView);

            avatarImg = rowView.findViewById(R.id.cchatAvatarImg);
            fullnameTxt = rowView.findViewById(R.id.cchatFullnameTxt);
            fullnameTxt.setTypeface(Configs.titSemibold);
            checkBox = rowView.findViewById(R.id.checkbox);


        }
    }

    public NewChatAdapter(Context context, NewChatModel[] newChatModels) {
        this.context = context;
        this.newChatModels = newChatModels;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);


    }

    @Override
    public NewChatAdapter.PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_newchat, parent, false);
        NewChatAdapter.PersonViewHolder pvh = new NewChatAdapter.PersonViewHolder(v);

        sharedPreferences = context.getSharedPreferences(myPref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userid = sharedPreferences.getString("userid", "0");

        return pvh;
    }

    int po = 0;

    @Override
    public void onBindViewHolder(final NewChatAdapter.PersonViewHolder holder, final int i) {
        // avatarImg, streamImg;


        String url = IMAGE_URL + newChatModels[i].getProfilePic();
        Glide.with(context)
                .load(url)
                .into(holder.avatarImg);


        holder.fullnameTxt.setText(newChatModels[i].getFullName());

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                newChatModels[i].setChecked(isChecked);
                String ids = "";
                for (int e = 0; e < newChatModels.length; e++) {
                    if (newChatModels[e].getChecked())
                        ids = ids + newChatModels[e].getUserId() + ",";
                }
                String id = ids.substring(0, ids.length() - 1);
                editor.putString("check", id);
                editor.commit();


            }
        });


    }


    @Override
    public int getItemCount() {
        return newChatModels.length;
    }


}









