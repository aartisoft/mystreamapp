package com.domain.mystream.Adpater;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.domain.mystream.Constants.Configs;
import com.domain.mystream.Activity.InboxActivity;
import com.domain.mystream.Model.MessageModel;
import com.domain.mystream.Model.PostModel;
import com.domain.mystream.R;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


import static com.domain.mystream.Adpater.CellStreamPostAdpater.dateStr;
import static com.domain.mystream.Constants.Configs.editor;
import static com.domain.mystream.Constants.Configs.error_toast;
import static com.domain.mystream.Constants.Configs.myPref;
import static com.domain.mystream.Constants.Configs.sharedPreferences;
import static com.domain.mystream.Constants.Configs.timeAgoSinceDate;
import static com.domain.mystream.Constants.MyStreamApis.DELETE_CHAT;
import static com.domain.mystream.Constants.MyStreamApis.IMAGE_URL;

public class MessageAdpater extends RecyclerView.Adapter<MessageAdpater.PersonViewHolder> {


    Context context;

    List<PostModel> postModelList;

    MessageModel[] messageModels;
    String userid;
    String identifier;

    public class PersonViewHolder extends RecyclerView.ViewHolder {

        private ImageView avatarImg, streamImg;
        private TextView fullnameTxt, dateTxt, senderTxt;
        private Button optionsButt;


        public PersonViewHolder(View rowView) {

            super(rowView);

            avatarImg = rowView.findViewById(R.id.cmessAvatarImg);
            fullnameTxt = rowView.findViewById(R.id.cmessFullnameTxt);
            fullnameTxt.setTypeface(Configs.titSemibold);
            senderTxt = rowView.findViewById(R.id.cmessSenderTxt);
            senderTxt.setTypeface(Configs.titRegular);
            dateTxt = rowView.findViewById(R.id.cmessDateTxt);
            dateTxt.setTypeface(Configs.titRegular);


            // Get last Message
         /*   TextView lastMessTxt = rowView.findViewById(R.id.cmessLastMessTxt);
            lastMessTxt.setTypeface(Configs.titRegular);*/

        }
    }

    public MessageAdpater(Context context, MessageModel[] messageModels) {
        this.context = context;
        this.messageModels = messageModels;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);


    }

    @Override
    public MessageAdpater.PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_messages, parent, false);
        MessageAdpater.PersonViewHolder pvh = new MessageAdpater.PersonViewHolder(v);

        sharedPreferences = context.getSharedPreferences(myPref, Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();

        userid = sharedPreferences.getString("userid", "0");

        return pvh;
    }

    int po = 0;

    @Override
    public void onBindViewHolder(final MessageAdpater.PersonViewHolder holder, final int i) {

        // avatarImg, streamImg;

        if (messageModels[i].getParticipants().size() > 0) {
            String url = IMAGE_URL + messageModels[i].getParticipants().get(0).getProfilePic();
            Glide.with(context)
                    .load(url)
                    .into(holder.avatarImg);
            holder.fullnameTxt.setText(messageModels[i].getParticipants().get(0).getFullName());
            dateStr = messageModels[i].getParticipants().get(0).getCreatedOnDate();
          /*  try {
                date = inputFormat.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
*/
            String niceDateStr = timeAgoSinceDate(dateStr);
            holder.dateTxt.setText(niceDateStr);

            if (messageModels[i].getLastChatMessage() != null) {
                if (messageModels[i].getLastChatMessage().getChat() != null) {
                    if (messageModels[i].getLastChatMessage().getChat().getLastChatMessage() != null)
                        holder.senderTxt.setText(messageModels[i].getLastChatMessage().getChat().getLastChatMessage().toString());
                }
            }


        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                identifier = messageModels[i].getIdentifier();

                Intent myactivity = new Intent(context, InboxActivity.class);
                myactivity.putExtra("identifier", identifier);
                if (messageModels[i].getParticipants().size() > 0) {
                    myactivity.putExtra("username", messageModels[i].getParticipants().get(0).getUserName());
                }
                myactivity.addFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(myactivity);
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final String cm = messageModels[holder.getAdapterPosition()].getIdentifier();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Delete Chat");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteChat(cm);
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                return false;
            }
        });

    }


    @Override
    public int getItemCount() {
        return messageModels.length;
    }


    private void deleteChat(final String identifier) {

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.GET, DELETE_CHAT+"id=" + identifier, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                MessageModel[] commentGson1 = new MessageModel[messageModels.length - 1];


                for (int i = 0, j = 0; i < messageModels.length; i++) {
                    if (messageModels[i].getIdentifier() != identifier) {
                        commentGson1[j] = messageModels[i];
                        j++;
                    }
                }
                messageModels = commentGson1;
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

}









