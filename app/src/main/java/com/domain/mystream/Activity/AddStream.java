package com.domain.mystream.Activity;

/* ================================

    - MyStream -

    created by cubycode @2018
    All Rights reserved

===================================*/

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.domain.mystream.Activity.ShowVideo;
import com.domain.mystream.Adpater.MyStreamPostAdpater;
import com.domain.mystream.Constants.Configs;
import com.domain.mystream.Constants.MarshMallowPermission;
import com.domain.mystream.Constants.VolleyMultipartRequest;
import com.domain.mystream.Model.OtherProfileModel;
import com.domain.mystream.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.domain.mystream.Constants.Configs.editor;
import static com.domain.mystream.Constants.Configs.error_toast;
import static com.domain.mystream.Constants.Configs.myPref;
import static com.domain.mystream.Constants.Configs.sharedPreferences;
import static com.domain.mystream.Constants.Configs.user_id;
import static com.domain.mystream.Constants.MyStreamApis.UPLOAD_MEDIA;


public class AddStream extends AppCompatActivity {

    /* Views */
    ImageView avatarImg, streamImg;
    EditText streamTxt;
    TextView fullnameTxt, recordDurationTxt;
    Button removeImageButt, playButt;
    RelativeLayout keyboardToolbar, recordAudioLayout;
    WebView recordingWebView;
    String post;
    MyStreamPostAdpater myStreamPostAdpater;
    String tag = "msg";

    /* Variables */
    String streamAttachment;
    MarshMallowPermission mmp = new MarshMallowPermission(this);
    boolean audioIsPlaying = false;
    String audioURL = null;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int SELECT_VIDEO = 3;
    private static final int SELECT_AUDIO = 2;
    String userid, selectedPath;
    JSONObject jsonObject;
    Bitmap bitmap;
    String fullPost = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_stream);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Hide ActionBar
//        getSupportActionBar().hide();

        // Change StatusBar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.light_blue));
        sharedPreferences = getSharedPreferences(myPref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.facebook_icon);
        userid = sharedPreferences.getString("userid", "0");

        TextView titleTxt = findViewById(R.id.asTitleTxt);
        titleTxt.setTypeface(Configs.titSemibold);
        avatarImg = findViewById(R.id.asAvatarimg);
        streamImg = findViewById(R.id.asStreamImg);
        streamTxt = findViewById(R.id.asStreamTxt);

        String pp = sharedPreferences.getString("img",null);
        Glide.with(AddStream.this)
                .load(pp)
                .into(avatarImg);

        streamTxt.setTypeface(Configs.titRegular);
        fullnameTxt = findViewById(R.id.asFullnameTxt);
        fullnameTxt.setTypeface(Configs.titSemibold);
        removeImageButt = findViewById(R.id.asRemoveImageButt);
        keyboardToolbar = findViewById(R.id.asKeyboardToolbar);
      /*  recordAudioLayout = findViewById(R.id.asRecordAudioLayout);
        //   hideRecordAudioLayout();
        playButt = findViewById(R.id.asPlayButt);
        playButt.setVisibility(View.INVISIBLE);*/

        recordingWebView = findViewById(R.id.asRecordingWebView);
        recordingWebView.loadUrl("file:///android_asset/recording.gif");
        recordingWebView.getSettings().setLoadWithOverviewMode(true);
        recordingWebView.getSettings().setUseWideViewPort(true);

        recordDurationTxt = findViewById(R.id.asRecordDurationTxt);
        recordDurationTxt.setTypeface(Configs.titRegular);


        // Get streamAttachment variable
        streamAttachment = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            streamAttachment = extras.getString("streamAttachment");
        }
        if (streamAttachment != null) {
            switch (streamAttachment) {
                case "image":
                    //  addImage();
                    //     uploadMedia();
                    break;
                case "video":
                    // addVideo();
                    break;
                case "audio":
                    //addAudio();
                    break;
                default:
                    break;
            }
        }


        // MARK: - REMOVE STREAM IMAGE BUTTON ------------------------------------
        removeImageButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Reset views and variables
                streamAttachment = null;
                //videoURI = null;
                audioURL = null;
                streamImg.setImageDrawable(null);
                playButt.setVisibility(View.INVISIBLE);
                removeImageButt.setVisibility(View.INVISIBLE);
            }
        });


        // MARK: - CANCEL (CLOSE ACTIVITY) ------------------------------------
        Button cancelButt = findViewById(R.id.asCancelButt);
        cancelButt.setTypeface(Configs.titSemibold);
        cancelButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                finish();
            }
        });


        // MARK: - ADD STREAM BUTTON (ON KEYBOARD TOOLBAR) -------------------------------
        Button addStreamButt = findViewById(R.id.asAddStreamButt);
        addStreamButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dismissKeyboard();
                showAddStreamPanel();
            }
        });




        // MARK: - POST STREAM BUTTON ------------------------------------
        Button postButt = findViewById(R.id.asPostButt);
        postButt.setTypeface(Configs.titSemibold);
        postButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                dateFormatter.setLenient(false);
                Date today = new Date();
                String s = dateFormatter.format(today);
                if (!fullPost.equals("")) {
                    post = fullPost;
                    fullPost = "";
                } else {
                    post = "<p>" + streamTxt.getText().toString() + "</p>";
                }
                if (post.equals("")) {
                    Configs.simpleAlert("You must type something!", AddStream.this);

                } else {

                    jsonObject = new JSONObject();
                    try {
                        jsonObject.put("PostId", "");
                        jsonObject.put("PostName", "");
                        jsonObject.put("PostBody", post);
                        jsonObject.put("PostTypeId", "7");
                        jsonObject.put("CreatedOnDate", s);
                        jsonObject.put("CreatedByUserId", Integer.parseInt(userid));
                        jsonObject.put("LastUpdatedOnDate", s);
                        jsonObject.put("LastUpdatedByUserId", Integer.parseInt(userid));
                        jsonObject.put("IsRemoved", false);
                        jsonObject.put("IsFullPost", true);
                        jsonObject.put("SystemId", 0);
                        jsonObject.put("UserGroupId", 0);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    addPost(jsonObject);
                }


            }
        });


    }// end onCreate()


    // MARK: - ADD IMAGE -------------------------------------
    void addImage() {
        dismissKeyboard();
        if (addStreamPanel != null) {
            hideAddStreamPanel();
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(AddStream.this);
        alert.setTitle("SELECT SOURCE")
                .setIcon(R.drawable.logo)
                .setItems(new CharSequence[]{
                        "Take a picture",
                        "Pick from Gallery"
                }, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {

                            // OPEN CAMERA
                            case 0:
                                if (!mmp.checkPermissionForCamera()) {
                                    mmp.requestPermissionForCamera();
                                } else {
                                    openCamera();
                                }
                                break;

                            // OPEN GALLERY
                            case 1:
                                if (!mmp.checkPermissionForReadExternalStorage()) {
                                    mmp.requestPermissionForReadExternalStorage();
                                } else {
                                    openGallery();
                                }
                                break;

                        }
                    }
                })
                .setNegativeButton("Cancel", null);
        alert.create().show();
    }


    // MARK: - ADD VIDEO -------------------------------------
    void addVideo() {
        dismissKeyboard();
        if (addStreamPanel != null) {
            hideAddStreamPanel();
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(AddStream.this);
        alert.setTitle("Select source")
                .setIcon(R.drawable.logo)
                .setItems(new CharSequence[]{
                "Take a video",
                "Pick from Gallery"
        }, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {

                    // OPEN VIDEO CAMERA
                    case 0:
                        if (!mmp.checkPermissionForCamera()) {
                            mmp.requestPermissionForCamera();
                        } else {
                            openVideoCamera();
                        }
                        break;

                    // OPEN VIDEO GALLERY
                    case 1:
                        if (!mmp.checkPermissionForReadExternalStorage()) {
                            mmp.requestPermissionForReadExternalStorage();
                        } else {
                            openVideoGallery();
                        }
                        break;

                }
            }
        })
                .setNegativeButton("Cancel", null);
        alert.create().show();
    }


    // IMAGE/VIDEO HANDLING METHODS ------------------------------------------------------------------------
    int CAMERA = 0;
    int GALLERY = 1;
    int VIDEO_CAMERA = 2;
    int VIDEO_GALLERY = 3;
    String videoPath = null;
    Uri videoURI = null;
    Uri imageURI;
    File file;


    // OPEN CAMERA
    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStorageDirectory(), "image.jpg");
        imageURI = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
        startActivityForResult(intent, CAMERA);
    }


    // OPEN GALLERY
    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), GALLERY);

    }


    // OPEN VIDEO CAMERA
    public void openVideoCamera() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, VIDEO_CAMERA);
    }

    // OPEN VIDEO GALLERY
    public void openVideoGallery() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), VIDEO_GALLERY);
    }

    public void openAudioGallery() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Audio"), VIDEO_GALLERY);
    }


    // GET VIDEO PATH AS A STRING -------------------------------------
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    // MARK: - ADD STICKER -------------------------------------
    void addSticker() {
        dismissKeyboard();
        if (addStreamPanel != null) {
            hideAddStreamPanel();
        }

        showStickersPanel();
    }


    // MARK: - STICKERS LAYOUT --------------------------------------
    AlertDialog stickersPanel;

    void showStickersPanel() {
        AlertDialog.Builder db = new AlertDialog.Builder(AddStream.this);
        LayoutInflater inflater = (LayoutInflater) AddStream.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View dialogView = inflater.inflate(R.layout.stickers_panel, null);

        final List<String> stickersArr = new ArrayList<>();
        for (int i = 0; i < Configs.STICKERS_AMOUNT; i++) {
            stickersArr.add("s" + i);
        }
        Log.i("log-", "STICKERS ARR: " + stickersArr);


        // CUSTOM GRID ADAPTER
        class GridAdapter extends BaseAdapter {
            private Context context;

            public GridAdapter(Context context) {
                super();
                this.context = context;
            }


            // CONFIGURE CELL
            @Override
            public View getView(int position, View cell, ViewGroup parent) {
                if (cell == null) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    assert inflater != null;
                    cell = inflater.inflate(R.layout.cell_sticker, null);
                }

                // Get Image
                ImageView sImg = cell.findViewById(R.id.cellsStickerImg);
                String sName = stickersArr.get(position);
                int resID = getResources().getIdentifier(sName, "drawable", getPackageName());
                sImg.setImageResource(resID);

                return cell;
            }

            @Override
            public int getCount() {
                return Configs.STICKERS_AMOUNT;
            }

            @Override
            public Object getItem(int position) {
                return stickersArr.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }
        }


        // Init GridView and set its adapter
        GridView aGrid = dialogView.findViewById(R.id.spStickersGridView);
        aGrid.setAdapter(new GridAdapter(AddStream.this));

        // Set number of Columns accordingly to the device used
        float scalefactor = getResources().getDisplayMetrics().density * 100; // 100 is the cell's width
        int number = getWindowManager().getDefaultDisplay().getWidth();
        int columns = (int) ((float) number / (float) scalefactor);
        aGrid.setNumColumns(columns);

        aGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Set selected sticker as stream Image
                String sName = stickersArr.get(position);
                int resID = getResources().getIdentifier(sName, "drawable", getPackageName());
                streamImg.setImageResource(resID);
                hideStickersPanel();
                removeImageButt.setVisibility(View.VISIBLE);
            }
        });


        // MARK: - DISMISS STICKERS PANEL BUTTON ------------------------------------
        Button dismissButt = dialogView.findViewById(R.id.spCancelButt);
        dismissButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideStickersPanel();
                showKeyboard();
            }
        });


        db.setView(dialogView);
        db.setCancelable(false);
        stickersPanel = db.create();
        stickersPanel.show();
    }

    void hideStickersPanel() {
        stickersPanel.dismiss();
    }


    // MARK: - ADD STREAM PANEL ----------------------------
    AlertDialog addStreamPanel;

    void showAddStreamPanel() {
        AlertDialog.Builder db = new AlertDialog.Builder(AddStream.this);
        LayoutInflater inflater = (LayoutInflater) AddStream.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View dialogView = inflater.inflate(R.layout.addstream_panel, null);


        // PANEL BUTTONS -------------------------------------
        Button photoButt = dialogView.findViewById(R.id.asAddPhotoButt);
        photoButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage();

            }
        });

        Button videoButt = dialogView.findViewById(R.id.asAddVideoButt);
        videoButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addVideo();
            }
        });

     /*   Button audioButt = dialogView.findViewById(R.id.asAddAudioButt);
        audioButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAudio();

            }
        });
*/
        Button stickerButt = dialogView.findViewById(R.id.asAddStickerButt);
        stickerButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSticker();
            }
        });

        // MARK: - DISMISS ADD STREAM PANEL BUTTON ------------------------------------
        Button dismissButt = dialogView.findViewById(R.id.asDismissAddStreamLayoutButt);
        dismissButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAddStreamPanel();
                showKeyboard();
            }
        });


        db.setView(dialogView);
        db.setCancelable(false);
        addStreamPanel = db.create();
        addStreamPanel.show();
    }

    void hideAddStreamPanel() {
        addStreamPanel.dismiss();
    }


    // MARK: - DISMISS KEYBOARD ------------------------------------------
    void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(streamTxt.getWindowToken(), 0);
        }
    }


    // MARK: - SHOW KEYBOARD ------------------------------------------
    void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(streamTxt, 0);
        }
    }

    private void addPost(JSONObject jsonObject) {

        final String requestBody = jsonObject.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://app_api_json.veamex.com/api/common/InsertUpdatePost",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        AlertDialog.Builder alert = new AlertDialog.Builder(AddStream.this);
                        alert.setMessage("Your Stream has been posted!")
                                .setTitle(R.string.app_name)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // Dismiss Activity
                                        Configs.mustRefresh = true;
                                        finish();
                                    }
                                })
                                .setCancelable(false)
                                .setIcon(R.drawable.logo);
                        alert.create().show();

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }

                }
            }
        }) {

            @Override
            public String getBodyContentType() {
                return String.format("application/json; charset=utf-8");
            }


            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AddStream.this);
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                uploadMedia();


            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == SELECT_VIDEO && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedVideoUri = data.getData();
            selectedPath = getPath(selectedVideoUri);

          //  Toast.makeText(this, selectedPath, Toast.LENGTH_SHORT).show();
            // System.out.println("SELECT_VIDEO Path : " + selectedPath);

            try {
                byte[] bytes = convert(selectedPath);
                uploadVideo(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private String getPath(final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat) {
            // MediaStore (and general)
            return getForApi19(uri);
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    @TargetApi(19)
    private String getForApi19(Uri uri) {
        Log.e(tag, "+++ API 19 URI :: " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            Log.e(tag, "+++ Document URI");
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                Log.e(tag, "+++ External Document URI");
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    Log.e(tag, "+++ Primary External Document URI");
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                Log.e(tag, "+++ Downloads External Document URI");
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                Log.e(tag, "+++ Media Document URI");
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    Log.e(tag, "+++ Image Media Document URI");
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    Log.e(tag, "+++ Video Media Document URI");
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    Log.e(tag, "+++ Audio Media Document URI");
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            Log.e(tag, "+++ No DOCUMENT URI :: CONTENT ");

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            Log.e(tag, "+++ No DOCUMENT URI :: FILE ");
            return uri.getPath();
        }
        return null;
    }

    public String getDataColumn(Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    String path;

    private void uploadMedia() {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, UPLOAD_MEDIA+"userId="+user_id,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        //  iv_imageview.setImageBitmap(bitmap);

                        Log.e("VolleyOnResponse200", response.toString());

                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            String id = obj.getString("Id");
                            String path = obj.getString("Path");
                            String msg = obj.getString("Message");
                            String post = streamTxt.getText().toString();

                            String fullPath = "<p><img class=\\\"img-fluid\\\" style=\\\"max-width: 100%;\\\" src=\"" + path + "\"></p>";
                            fullPost = post + fullPath;
                            streamTxt.setText(fullPost);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyonErrorResponse200", "Error: " + error.getMessage());
                        // Toasty.error(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".jpg", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);

    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadVideo(final byte[] selectedPath) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, UPLOAD_MEDIA+"userId="+user_id,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        //  iv_imageview.setImageBitmap(bitmap);

                        Log.e("VolleyOnResponse200", response.toString());

                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            String id = obj.getString("Id");
                            String path = obj.getString("Path");
                            String msg = obj.getString("Message");
                            post = streamTxt.getText().toString();

                            String fullPath = "<p><video  width=\"100\" controls ><source src=\"" + path + "\" type=\"video/mp4\" ></video></p>";
                            String fullPost = post + fullPath;
                            streamTxt.setText(fullPost);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                // Now you can use any deserializer to make sense of data

                                JSONObject obj = new JSONObject(res);
                            } catch (UnsupportedEncodingException e1) {
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                                // returned data is not JSONObject?
                                e2.printStackTrace();
                            }
                        }
                    }
                }) {

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("video", new DataPart(imagename + ".mp4", selectedPath));
                return params;
            }
        };
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);

    }

    public byte[] convert(String path) throws IOException {

        FileInputStream fis = new FileInputStream(path);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];

        for (int readNum; (readNum = fis.read(b)) != -1; ) {
            bos.write(b, 0, readNum);
        }

        byte[] bytes = bos.toByteArray();

        return bytes;
    }


}
// @end
