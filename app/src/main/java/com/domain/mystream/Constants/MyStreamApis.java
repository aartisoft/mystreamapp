package com.domain.mystream.Constants;

public class MyStreamApis {
    public static final String BASE_URL ="https://app_api_json.veamex.com/api/common/";
    public static final String LOGIN_URL =BASE_URL+"LoginByUserDetail?";
    public static final String GET_POST =BASE_URL+"GetPosts?";
    public static final String DELETE_POST =BASE_URL+"DeletePost?";
    public static final String LIKE_POST =BASE_URL+"NewPostReaction?";
    public static final String COMMENT_POST =BASE_URL+"NewComment?";
    public static final String GET_ALL_POST_COMMENT =BASE_URL+"GetPostComments?";
    public static final String UPDATE_PROFILE =BASE_URL+"UpdateProfile";
    public static final String UPLOAD_PROFILE_PIC =BASE_URL+"UpdateProfilePic?";
    public static final String OTHER_USER_PROFILE_URL =BASE_URL+"GetUserById?";
    public static final String SHARE_POST =BASE_URL+"SharePost?";
    public static final String CONNECTED_USERS =BASE_URL+"GetConnectedUsers?";
    public static final String GET_ALL_MESSAGES =BASE_URL+"GetChatsByUserId?";
    public static final String DELETE_COMMENT =BASE_URL+"DeleteComment?";
    public static final String DELETE_CHAT =BASE_URL+"DeleteChatById?";
    public static final String GET_CHAT =BASE_URL+"GetChatMessages?";
    public static final String INSERT_CHAT =BASE_URL+"InsertUpdateChatMessages";
    public static final String UPLOAD_MEDIA =BASE_URL+"PostUserMedia?";
    public static final String GET_MULTIPLE_CHAT =BASE_URL+"GetMultipleChatParticipants?";
    public static final String IMAGE_URL ="https://app_api_json.veamex.com";
}
