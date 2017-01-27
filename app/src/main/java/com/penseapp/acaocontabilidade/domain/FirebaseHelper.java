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
    private static final String CHATS_PATH = "chats";
    private static final String USER_CHATS_PATH = "user-chats";
    private static final String CHAT_USERS_PATH = "chat-users";
    private static final String USER_CHATCONTACTS_CHAT = "user-chatContacts:chat";
    private static final String MESSAGES_PATH = "messages";
    private static final String NEWS_PATH = "news";

    private static class SingletonHolder {
        private static final FirebaseHelper INSTANCE = new FirebaseHelper();

    }

    public static FirebaseHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private FirebaseHelper(){
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
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

    public DatabaseReference getChatsReference(){
        return databaseRef.child(CHATS_PATH);
    }

    public DatabaseReference getCurrentChatReference(String chatId){
        return getChatsReference().child(chatId).child(MESSAGES_PATH);
    }

    public DatabaseReference getUserChatsReference(){
        return databaseRef.child(USER_CHATS_PATH);
    }

    public DatabaseReference getCurrentUserChatsReference(){
        return getUserChatsReference().child(getAuthUserId());
    }

    public DatabaseReference getUserChatContactsReference(){
        return databaseRef.child(USER_CHATCONTACTS_CHAT);
    }

    public DatabaseReference getChatUsersReference(){
        return databaseRef.child(CHAT_USERS_PATH);
    }

    public DatabaseReference getCurrentChatUsersReference(){
        return getChatUsersReference().child(getAuthUserId());
    }

    public DatabaseReference getNewsReference(){
        return databaseRef.child(NEWS_PATH);
    }


//    public String getCurrentUserType() {
//        String type;
//        getCurrentUserReference().child("type").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                type = dataSnapshot.getValue().toString();
//                System.out.println(type);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        return type;
//    }
}