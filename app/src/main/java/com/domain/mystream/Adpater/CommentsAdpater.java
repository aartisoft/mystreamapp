package com.domain.mystream.Adpater;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.domain.mystream.Comments;
import com.domain.mystream.Configs;
import com.domain.mystream.InboxActivity;
import com.domain.mystream.Model.Comment;
import com.domain.mystream.Model.CommentGson;
import com.domain.mystream.Model.CommentGsonModel;
import com.domain.mystream.Model.Likes;
import com.domain.mystream.Model.PostModel;
import com.domain.mystream.Model.User_;
import com.domain.mystream.R;
import com.domain.mystream.RepltComment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.domain.mystream.Login.editor;
import static com.domain.mystream.Login.myPref;

public class CommentsAdpater extends RecyclerView.Adapter<CommentsAdpater.PersonViewHolder> {


    Context context;

    List<PostModel> postModelList;

    Integer commentId;
    CommentGsonModel[] commentGsonModels;
    String fullname, date, commentText, url;


    public class PersonViewHolder extends RecyclerView.ViewHolder {

        private ImageView avatarImg, streamImg;
        private TextView fullnameTxt, dateTxt, commentTxt;
        private Button optionsButt;


        public PersonViewHolder(View rowView) {

            super(rowView);

            avatarImg = rowView.findViewById(R.id.ccommAvatarimg);
            fullnameTxt = rowView.findViewById(R.id.ccommFullnameTxt);
            fullnameTxt.setTypeface(Configs.titSemibold);
            commentTxt = rowView.findViewById(R.id.ccommCommTxt);
            commentTxt.setTypeface(Configs.titRegular);
            dateTxt = rowView.findViewById(R.id.ccommDateTxt);
            dateTxt.setTypeface(Configs.titRegular);
            optionsButt = rowView.findViewById(R.id.ccommOptionsButt);
        }
    }

    public CommentsAdpater(Context context, CommentGsonModel[] commentGsonModels) {
        this.context = context;
        this.commentGsonModels = commentGsonModels;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);


    }

    @Override
    public CommentsAdpater.PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_comment, parent, false);
        CommentsAdpater.PersonViewHolder pvh = new CommentsAdpater.PersonViewHolder(v);
        SharedPreferences sharedPreferences = context.getSharedPreferences(myPref, Context.MODE_PRIVATE);

        return pvh;
    }

    int po = 0;

    @Override
    public void onBindViewHolder(final CommentsAdpater.PersonViewHolder holder, final int i) {
        // avatarImg, streamImg;


        final CommentGson commentGson = commentGsonModels[i].getComment();
        User_ user_ = commentGson.getUser();
        fullname = user_.getFirstName() + " " + user_.getLastName();
        holder.fullnameTxt.setText(fullname);
        commentText = commentGson.getText();
        holder.commentTxt.setText(commentText);
        url = "https://qas.veamex.com" + user_.getProfilePic();
        Glide.with(context)
                .load(url)
                .into(holder.avatarImg);
        date = user_.getCreatedOnDate();
        holder.dateTxt.setText(date);
        commentId = commentGson.getCommentId();


        holder.optionsButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Integer cm = commentGsonModels[holder.getAdapterPosition()].getCommentId();
//holder.getAdapterPosition();
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("SELECT SOURCE")
                        .setIcon(R.drawable.logo)
                        .setItems(new CharSequence[]{
                                "Delete",
                                "Reply",

                        }, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {


                                    // REPORT COMMENT -------------------------

                                    case 0:

                                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                        alert.setMessage("Are you sure you want to delete this commment?")
                                                .setTitle(R.string.app_name)
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int ss) {


                                                        deleteComment(cm);


                                                    }
                                                })
                                                .setNegativeButton("Cancel", null)
                                                .setIcon(R.drawable.logo);
                                        alert.create().show();
                                        break;
                                    // -------------------------------------------------------
                                    case 1:
                                        Intent myactivity = new Intent(context, RepltComment.class);
                                        myactivity.putExtra("parentcoment", commentGson.getParentCommentId());
                                        myactivity.putExtra("fullname", fullname);
                                        myactivity.putExtra("date", date);
                                        myactivity.putExtra("img", url);
                                        myactivity.putExtra("comment", commentText);

                                        myactivity.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(myactivity);


                                }
                            }
                        })
                        .setNegativeButton("Cancel", null);
                alert.create().show();

            }
        });// end optionsButt


        // end userPointer


    }

    private void deleteComment(final Integer commentid) {

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.GET, "https://app_api_json.veamex.com/api/common/DeleteComment?commentId=" + String.valueOf(commentid), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_LONG).show();
                //  hideProgress(getActivity());

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                CommentGsonModel[] commentGson1 = new CommentGsonModel[commentGsonModels.length - 1]; //= new ArrayList<>();


                for (int i = 0, j = 0; i < commentGsonModels.length; i++) {
                    if (commentGsonModels[i].getCommentId() != commentid) {
                        commentGson1[j] = commentGsonModels[i];
                        j++;
                    }
                }
                commentGsonModels = commentGson1;

                notifyDataSetChanged();


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
                Toast.makeText(context, "Server error or No internet connection", Toast.LENGTH_LONG).show();
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
        return commentGsonModels.length;
    }

}









