package com.domain.mystream.Adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.domain.mystream.Constants.Configs;
import com.domain.mystream.Model.PostModel;
import com.domain.mystream.R;

import java.util.List;

public class StreamPostAdpater extends RecyclerView.Adapter<StreamPostAdpater.PersonViewHolder> {
    List<PostModel> postModelList;

    Context context;

    public class PersonViewHolder extends RecyclerView.ViewHolder {

        ImageView avatarImg, streamImg;
        TextView fullnameTxt, usernameTxt, streamTxt, likesTxt, commentsTxt, playingTimeTxt;
        Button optionsButt, likeButt, commentsButt, shareButt, playButt;


        public PersonViewHolder(View rowView) {
            super(rowView);

            avatarImg = rowView.findViewById(R.id.sdAvatarImg);
            streamImg = rowView.findViewById(R.id.sdStreamImg);
            fullnameTxt = rowView.findViewById(R.id.sdFullnameTxt);
            fullnameTxt.setTypeface(Configs.titSemibold);
            usernameTxt = rowView.findViewById(R.id.sdUsernameTxt);
            usernameTxt.setTypeface(Configs.titRegular);
            /*streamTxt =rowView. findViewById(R.id.sdStreamTxt);*/
            streamTxt.setTypeface(Configs.titRegular);
            optionsButt = rowView.findViewById(R.id.sdOptionsButt);
            likeButt = rowView.findViewById(R.id.sdLikeButt);
            commentsButt = rowView.findViewById(R.id.sdCommentsButt);
            shareButt = rowView.findViewById(R.id.sdShareButt);
            likesTxt = rowView.findViewById(R.id.sdLikesTxt);
            likesTxt.setTypeface(Configs.titRegular);
            commentsTxt = rowView.findViewById(R.id.sdCommentsTxt);
            commentsTxt.setTypeface(Configs.titRegular);
            playButt = rowView.findViewById(R.id.sdPlayButt);
            playingTimeTxt = rowView.findViewById(R.id.sdPlayingTimeTxt);
            playingTimeTxt.setTypeface(Configs.titSemibold);

        }
    }

    public StreamPostAdpater(Context context, List<PostModel> projectDisputedList) {
        this.context = context;
        this.postModelList = projectDisputedList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);


    }

    @Override
    public StreamPostAdpater.PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stream_details, parent, false);
        StreamPostAdpater.PersonViewHolder pvh = new StreamPostAdpater.PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final StreamPostAdpater.PersonViewHolder holder, int i) {
        // avatarImg, streamImg;
     /*   Glide.with(context)
                .load(postModelList.get(i).getUser().)
                .placeholder(context.getResources().getDrawable(R.drawable.audio_image))

                .into(holder.imageView);*/
        holder.fullnameTxt.setText(postModelList.get(i).getPostName());
        holder.usernameTxt.setText(postModelList.get(i).getUser().getUserName());
        holder.streamTxt.setText(postModelList.get(i).getCompany().getSystemId());
        // holder.likesTxt.setText(postModelList.get(i).getLikes().length);
        // holder.commentsTxt.setText(postModelList.get(i).getComments().length);
        holder.playingTimeTxt.setText(postModelList.get(i).getCreatedOnDate());

        holder.optionsButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(context, view);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(
                                context,
                                "You Clicked : " + item.getTitle(),
                                Toast.LENGTH_SHORT
                        ).show();
                        return true;
                    }
                });

                popup.show(); //showing popup menu

            }
        });

    }

    @Override
    public int getItemCount() {
        return postModelList.size();
    }

}









