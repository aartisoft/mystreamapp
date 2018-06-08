package com.domain.mystream.Adpater;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
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
import com.domain.mystream.Activity.StreamDetails;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import static com.domain.mystream.Adpater.CellStreamPostAdpater.date;
import static com.domain.mystream.Adpater.CellStreamPostAdpater.dateStr;

import static com.domain.mystream.Constants.Configs.editor;
import static com.domain.mystream.Constants.Configs.error_toast;
import static com.domain.mystream.Constants.Configs.myPref;
import static com.domain.mystream.Constants.Configs.sharedPreferences;
import static com.domain.mystream.Constants.Configs.timeAgoSinceDate;
import static com.domain.mystream.Constants.MyStreamApis.IMAGE_URL;
import static com.domain.mystream.Constants.MyStreamApis.LIKE_POST;

public class MyStreamPostAdpater extends RecyclerView.Adapter<MyStreamPostAdpater.PersonViewHolder> {
    List<PostModel> postModelList;
    String referenceId, referenceTypeId, reactionTypeId, interactionByUserId, comment, parentCommentId;
    Context context;
    MarshMallowPermission mmp;
    PostModel postModel;
    CommentGsonModel[] commentGsonModels;

    public class PersonViewHolder extends RecyclerView.ViewHolder {

        ImageView avatarImg, streamImg;
        public TextView fullnameTxt, usernameTxt, streamTxt, likesTxt, commentsTxt, textat, usernameTimeTxt;
        public Button optionsButt, likeButt, commentsButt, shareButt, playButt;
        WebView webView;


        public PersonViewHolder(View rowView) {
            super(rowView);

            avatarImg = rowView.findViewById(R.id.csAvatarImg);
            streamImg = rowView.findViewById(R.id.csStreamImg);
            webView = rowView.findViewById(R.id.csStreamTxt);

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

    public MyStreamPostAdpater(Context context, List<PostModel> projectDisputedList) {
        this.context = context;
        this.postModelList = projectDisputedList;

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);


    }

    @Override
    public MyStreamPostAdpater.PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_stream, parent, false);
        MyStreamPostAdpater.PersonViewHolder pvh = new MyStreamPostAdpater.PersonViewHolder(v);


         sharedPreferences = context.getSharedPreferences(myPref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        userid = sharedPreferences.getString("userid", "0");
        return pvh;
    }

    @Override
    public void onBindViewHolder(final MyStreamPostAdpater.PersonViewHolder holder, final int i) {
        // avatarImg, streamImg;
        String url =IMAGE_URL + postModelList.get(i).getUser().getProfilePic();
        Glide.with(context)
                .load(url)
                .into(holder.avatarImg);

        String summary = postModelList.get(i).getPostBody();

        String c = summary.replace("src=\"", "src=\"https://qas.veamex.com");

        String head = "<!DOCTYPE html><html><head><meta name=\"viewport\" content=\"width=device-width, user-scalable=yes\" /></head>";

        String html = head + "<body style='background-color:#ffffff;'>" + c + "</body></html>";
        holder.webView.loadData(html, "text/html", null);
        holder.webView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postModel = new PostModel();

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

                context.startActivity(intent);
            }
        });

        holder.fullnameTxt.setText(postModelList.get(i).getUser().getFirstName() + " " + postModelList.get(i).getUser().getLastName());

        dateStr = postModelList.get(i).getCreatedOnDate();

       /* try {
            date = inputFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        String niceDateStr = timeAgoSinceDate(dateStr);
        holder.usernameTimeTxt.setText(postModelList.get(i).getUser().getUserName() + " " + niceDateStr);

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

                Intent myactivity = new Intent(context.getApplicationContext(), OtherUserProfile.class);
                myactivity.addFlags(FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(myactivity);
          /*      Intent i = new Intent(Home.this, OtherUserProfile.class);
                Bundle extras = new Bundle();
                extras.putString("userID", userPointer.getObjectId());
                i.putExtras(extras);
                startActivity(i);*/
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
                if (null != postModelList.get(i).getComments())
                    if (postModelList.get(i).getComments().size() > 0)
                        myactivity.putExtra("ParentCommentId", postModelList.get(i).getComments().get(i).getPostCommentId());
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
  /*              if (!mmp.checkPermissionForWriteExternalStorage()) {
                    mmp.requestPermissionForWriteExternalStorage();
                } else {
                    Bitmap bitmap;
                    if (sObj.getParseFile(Configs.STREAMS_IMAGE) != null) {
                        bitmap = ((BitmapDrawable) holder.streamImg.getDrawable()).getBitmap();
                    } else {
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
                    }
                    Uri uri = Configs.getImageUri(context, bitmap);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/jpeg");
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.putExtra(Intent.EXTRA_TEXT, sObj.getString(Configs.STREAMS_TEXT) +
                            " on #" + getString(R.string.app_name));
                    startActivity(Intent.createChooser(intent, "Share on..."));
                }
*/

                // Increment shares amount
              /*  sObj.increment(Configs.STREAMS_SHARES, 1);
                sObj.saveInBackground();*/

            }
        });

    }


    private void likePost(String referenceId, String referenceTypeId, String reactionTypeId, String interactionByUserId) {

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.GET, LIKE_POST+"referenceId=" + referenceId + "&referenceTypeId=" + referenceTypeId + "&reactionTypeId=" + reactionTypeId + "&interactionByUserId=" + interactionByUserId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_LONG).show();
                //  hideProgress(getActivity());




              /*  int likes = sObj.getInt(Configs.STREAMS_LIKES);
                holder.likesTxt.setText(Configs.roundThousandsIntoK(likes));

                // Show liked icon
                List<String>likedBy = sObj.getList(Configs.STREAMS_LIKED_BY);
                if (likedBy.contains(currUser.getObjectId()) ){
                 likeButt.setBackgroundResource(R.drawable.liked_butt_small);
                } else {
                    likeButt.setBackgroundResource(R.drawable.like_butt_small);
                }
*/
            /*    try {
                    // Configs.hidePD();
                    JSONArray rs = new JSONArray(response);
                    for(int i=0;i<rs.length();i++)
                    {
                        JSONObject object= rs.getJSONObject(i);
                        PostModel postModel=new PostModel();


                    }

                    CellStreamPostAdpater cellStreamPostAdpater = new CellStreamPostAdpater(context,postModelList);
                    cellStreamPostAdpater.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

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









