package com.domain.mystream.Adpater;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.domain.mystream.Activity.Comments;
import com.domain.mystream.Constants.Configs;
import com.domain.mystream.Constants.MarshMallowPermission;
import com.domain.mystream.Model.Comment;
import com.domain.mystream.Model.CommentGsonModel;
import com.domain.mystream.Model.PostModel;
import com.domain.mystream.Activity.OtherUserProfile;
import com.domain.mystream.R;
import com.domain.mystream.Activity.ShareActivity;
import com.domain.mystream.Activity.StreamDetails;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.domain.mystream.Constants.Configs.editor;
import static com.domain.mystream.Constants.Configs.error_toast;
import static com.domain.mystream.Constants.Configs.myPref;
import static com.domain.mystream.Constants.Configs.sharedPreferences;
import static com.domain.mystream.Constants.MyStreamApis.IMAGE_URL;
import static com.domain.mystream.Constants.MyStreamApis.LIKE_POST;


public class OtherProfileAdpater extends RecyclerView.Adapter<OtherProfileAdpater.PersonViewHolder> {
    List<PostModel> postModelList;
    String referenceId, referenceTypeId, reactionTypeId, interactionByUserId, comment, parentCommentId;
    Context context;
    MarshMallowPermission mmp;
    PostModel postModel;
    CommentGsonModel[] commentGsonModels;
    public final static int FINGER_TOUCHED = 1;
    public final static int FINGER_RELEASED = 0;
    private int fingerState = FINGER_RELEASED;

    public class PersonViewHolder extends RecyclerView.ViewHolder {

        ImageView avatarImg, streamImg;
        public TextView fullnameTxt, usernameTxt, streamTxt, likesTxt, commentsTxt, textat, usernameTimeTxt;
        public Button optionsButt, likeButt, commentsButt, shareButt, playButt;
        public WebView webView;
        RelativeLayout relativeLayout;
        View view;


        public PersonViewHolder(View rowView) {
            super(rowView);

            avatarImg = rowView.findViewById(R.id.csAvatarImg);
            streamImg = rowView.findViewById(R.id.csStreamImg);

            webView = rowView.findViewById(R.id.csStreamTxt);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient());
            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);

            relativeLayout = rowView.findViewById(R.id.postrelative);

            likesTxt = rowView.findViewById(R.id.csLikesTxt);
            likesTxt.setTypeface(Configs.titRegular);
            commentsTxt = rowView.findViewById(R.id.csCommentsTxt);
            commentsTxt.setTypeface(Configs.titRegular);
            fullnameTxt = rowView.findViewById(R.id.csFullnameTxt);
            fullnameTxt.setTypeface(Configs.titSemibold);
            usernameTimeTxt = rowView.findViewById(R.id.csUsernameTimeTxt);
            usernameTimeTxt.setTypeface(Configs.titRegular);
            likeButt = rowView.findViewById(R.id.csLikeButt);
            commentsButt = rowView.findViewById(R.id.csCommentsButt);
            shareButt = rowView.findViewById(R.id.csShareButt);
            textat = rowView.findViewById(R.id.textat);
            textat.setText("@");

        }
    }

    String userid;

    public OtherProfileAdpater(Context context, List<PostModel> projectDisputedList) {
        this.context = context;
        this.postModelList = projectDisputedList;

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);


    }

    @Override
    public OtherProfileAdpater.PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_stream, parent, false);
        OtherProfileAdpater.PersonViewHolder pvh = new OtherProfileAdpater.PersonViewHolder(v);


         sharedPreferences = context.getSharedPreferences(myPref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        userid = sharedPreferences.getString("userid", "0");
        return pvh;
    }

    @Override
    public void onBindViewHolder(final OtherProfileAdpater.PersonViewHolder holder, final int i) {
        // avatarImg, streamImg;
        final String url = IMAGE_URL + postModelList.get(i).getUser().getProfilePic();
        Glide.with(context)
                .load(url)
                .into(holder.avatarImg);

        holder.streamImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostModel postModel = new PostModel();

                Intent intent = new Intent(context.getApplicationContext(), StreamDetails.class);
                intent.putExtra("postmodel", String.valueOf(postModel));
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent);

            }
        });
        postModel = new PostModel();
        String summary = postModelList.get(i).getPostBody();

        String c = summary.replace("src=\"", "src=\"https://app_api_json.veamex.com");

        String head = "<!DOCTYPE html><html><head><meta name=\"viewport\" content=\"width=device-width, user-scalable=yes\" /></head>";


        String html = head + "<body style='background-color:#ffffff;'>" + c + "</body></html>";
        holder.webView.loadData(html, "text/html", null);

        holder.webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    Intent intent = new Intent(context.getApplicationContext(), StreamDetails.class);
                    intent.putExtra("PostId", postModelList.get(i).getPostId());
                    intent.putExtra("PostBody", postModelList.get(i).getPostBody());
                    intent.putExtra("PostName", postModelList.get(i).getPostName());
                    intent.putExtra("CreatedOnDate", postModelList.get(i).getCreatedOnDate());
                    intent.putExtra("CreatedByUserId", postModelList.get(i).getCreatedByUserId());
                    intent.putExtra("LastUpdatedByUserId", postModelList.get(i).getLastUpdatedByUserId());
                    intent.putExtra("PostTypeId", postModelList.get(i).getPostTypeId());
                    intent.putExtra("FullName", postModelList.get(i).getUser().getFirstName() + postModelList.get(i).getUser().getLastName());
                    intent.putExtra("UserName", postModelList.get(i).getUser().getUserName());
                    intent.putExtra("Likes", postModelList.get(i).isLikebyme());
                    intent.putExtra("LikesText", postModelList.get(i).getLikes().size());
                    intent.putExtra("CommentsValue", postModelList.get(i).getComments().size());
                    intent.putExtra("img", url);

                    context.startActivity(intent);
                }

                return false;
            }
        });


        holder.fullnameTxt.setText(postModelList.get(i).getUser().getFirstName() + " " + postModelList.get(i).getUser().getLastName());
        holder.usernameTimeTxt.setText(postModelList.get(i).getUser().getUserName() + " " + postModelList.get(i).getCreatedOnDate());

        if (postModelList.get(i).getLikes() != null)
            holder.likesTxt.setText(Configs.roundThousandsIntoK(postModelList.get(i).getLikes().size()));

        if (postModelList.get(i).isLikebyme()) {
            holder.likeButt.setBackgroundResource(R.drawable.liked_butt_small);
        } else {
            holder.likeButt.setBackgroundResource(R.drawable.like_butt_small);
        }
        holder.likeButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postModelList.get(i).isLikebyme()) {
                    holder.likeButt.setBackgroundResource(R.drawable.like_butt_small);
                    if (postModelList.get(i).getLikes() != null)
                        if (postModelList.get(i).getLikes().size() > 0) {

                            holder.likesTxt.setText(Configs.roundThousandsIntoK(postModelList.get(i).getLikes().size() - 1));
                        } else {
                            holder.likesTxt.setText(String.valueOf(0));

                        }
                    postModelList.get(i).setLikebyme(false);


                } else {
                    holder.likeButt.setBackgroundResource(R.drawable.liked_butt_small);
                    if (postModelList.get(i).getLikes() != null)
                        if (postModelList.get(i).getLikes().size() > 0) {
                            holder.likesTxt.setText(String.valueOf(postModelList.get(i).getLikes().size() + 1));
                        } else {
                            holder.likesTxt.setText(String.valueOf(1));
                        }
                    postModelList.get(i).setLikebyme(true);

                }

                likePost(postModelList.get(i).getPostId(), postModelList.get(i).getPostTypeId(), "1", userid);
            }
        });


        // MARK: - AVATAR BUTTON ------------------------------------
        holder.avatarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myactivity = new Intent(context, OtherUserProfile.class);
                myactivity.addFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(myactivity);
            }
        });

        if (postModelList.get(i).getComments() != null)
            if (postModelList.get(i).getComments().size() > 0) {
                holder.commentsTxt.setText(Configs.roundThousandsIntoK(postModelList.get(i).getComments().size()));
            } else {
                holder.commentsTxt.setText(String.valueOf(0));

            }

        // MARK: - COMMENTS BUTTON ------------------------------------
        holder.commentsButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                postModel = new PostModel();
                Intent myactivity = new Intent(context.getApplicationContext(), Comments.class);
                myactivity.putExtra("PostId", postModelList.get(i).getPostId());
                myactivity.putExtra("PostTypeId", postModelList.get(i).getPostTypeId());
                myactivity.putExtra("PostBody", postModelList.get(i).getPostBody());
                myactivity.putExtra("FullName", postModelList.get(i).getUser().getFirstName() + " " + postModelList.get(i).getUser().getLastName());

                Comment comment = new Comment();
                myactivity.putExtra("Comment", comment.getText());
                if (null != comment.getCommentId())
                    editor.putString("CommentId", comment.getCommentId());
                else
                    editor.putString("CommentId", "0");
                editor.apply();
                context.startActivity(myactivity);
            }
        });

        // MARK: - SHARE BUTTON ------------------------------------
        holder.shareButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ShareActivity.class);
                intent.putExtra("PostId", postModelList.get(i).getPostId());
                intent.putExtra("PostTypeId", postModelList.get(i).getPostTypeId());
                intent.putExtra("PostBody", postModelList.get(i).getPostBody());
                intent.putExtra("FullName", postModelList.get(i).getUser().getFirstName() + " " + postModelList.get(i).getUser().getLastName());
                intent.putExtra("Image", url);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


            }
        });

    }


    private void likePost(String referenceId, String referenceTypeId, String reactionTypeId, String interactionByUserId) {

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.GET, LIKE_POST+"referenceId=" + referenceId + "&referenceTypeId=" + referenceTypeId + "&reactionTypeId=" + reactionTypeId + "&interactionByUserId=" + interactionByUserId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Configs.hidePD();
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject jsonObject = new JSONObject(responseBody);
                } catch (Exception e) {
                    //Handle a malformed json response

                }
                Toast.makeText(context, error_toast, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };
        // showProgress(getActivity(), "Loading....", "Please wait!");
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    @Override
    public int getItemCount() {
        return postModelList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}









