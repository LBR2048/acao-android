package com.penseapp.acaocontabilidade.domain;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by unity.
 */
public class FirebaseHelper {
    private static final String LOG_TAG = FirebaseHelper.class.getSimpleName();

    // Firebase database
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private static final String USERS_PATH = "users";
    private static final String CHAT_MESSAGES_PATH = "chat-messages";
    private static final String USER_CHATS_PATH = "user-chats";
    private static final String USER_CHATS_PROPERTIES_PATH = "user-chats-properties";
    private static final String USER_CHAT_CONTACTS_CHAT = "user-chatContacts:chat";
    private static final String FCM_TOKEN = "fcm_token";
    private static final String NEWS_PATH = "news";

    // TODO útil apenas para grupos retirar por enquanto
//    private static final String CHAT_USERS_PATH = "chat-users";

    private static class SingletonHolder {
        private static final FirebaseHelper INSTANCE = new FirebaseHelper();

    }

    public static FirebaseHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private FirebaseHelper(){
        if (database == null) {
            database = FirebaseDatabase.getInstance();
//            database.setPersistenceEnabled(true);
            databaseRef = database.getReference();
        }
    }


    // Authentication

    public String getAuthUserEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = null;
        if (user != null) {
            email = user.getEmail();
        }
        return email;
    }

    public String getAuthUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = null;
        if (user != null) {
            userId = user.getUid();
        }
        return userId;
    }


    // Realtime database

    public DatabaseReference getUsersReference(){
        return databaseRef.child(USERS_PATH);
    }

    public DatabaseReference getChatMessagesReference(){
        return databaseRef.child(CHAT_MESSAGES_PATH);
    }

    public DatabaseReference getUserChatsReference(){
        return databaseRef.child(USER_CHATS_PATH);
    }

    public DatabaseReference getCurrentUserChatsReference(){
        return getUserChatsReference().child(getAuthUserId());
    }

    public DatabaseReference getUserChatContactsReference(){
        return databaseRef.child(USER_CHAT_CONTACTS_CHAT);
    }

    public DatabaseReference getUserChatPropertiesReference(){
        return databaseRef.child(USER_CHATS_PROPERTIES_PATH);
    }

    // TODO útil apenas para grupos retirar por enquanto
//    public DatabaseReference getChatUsersReference(){
//        return databaseRef.child(CHAT_USERS_PATH);
//    }

    // TODO útil apenas para grupos retirar por enquanto
//    public DatabaseReference getCurrentChatUsersReference(){
//        return getChatUsersReference().child(getAuthUserId());
//    }

    public DatabaseReference getCurrentUserFcmTokenReference() {
        DatabaseReference currentUserFcmTokenReference = null;
        String authUserId = getAuthUserId();
        if (authUserId != null) {
            currentUserFcmTokenReference = getUsersReference().child(authUserId).child(FCM_TOKEN);
        }
        return currentUserFcmTokenReference;
    }

    public DatabaseReference getNewsReference(){
        return databaseRef.child(NEWS_PATH);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    public void sendFcmTokenToServer(String token) {
        DatabaseReference currentUserFcmTokenReference = FirebaseHelper.getInstance().getCurrentUserFcmTokenReference();
        if (currentUserFcmTokenReference != null) {
            currentUserFcmTokenReference.setValue(token);
        }
    }

}