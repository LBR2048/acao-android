package com.penseapp.acaocontabilidade.domain;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class FirebaseHelper {

    //region Constants
    private static final String NOTIFICATIONS = "notifications";
    private static final String USERS_PATH = "users";
    private static final String CHAT_MESSAGES_PATH = "chat-messages";
    private static final String USER_CHATS_PATH = "user-chats";
    private static final String USER_CHATS_PROPERTIES_PATH = "user-chats-properties";
    private static final String USER_CHAT_CONTACTS_CHAT = "user-chatContacts:chat";
    private static final String FCM_TOKEN = "fcm_token";
    private static final String NEWS_PATH = "news";
    public final static String GS_PREFIX = "gs://acao-f519d.appspot.com/";
    //endregion

    //region Member variables
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    //endregion

    public static FirebaseHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final FirebaseHelper INSTANCE = new FirebaseHelper();
    }

    private FirebaseHelper() {
        if (database == null) {
            database = FirebaseDatabase.getInstance();
//            database.setPersistenceEnabled(true);
            databaseRef = database.getReference();
        }
    }

    //region Authentication
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

    public void logout() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
    }
    //endregion

    //region Database
    public DatabaseReference getUsersReference(){
        return databaseRef.child(USERS_PATH);
    }

    public DatabaseReference getChatMessagesReference(){
        return databaseRef.child(CHAT_MESSAGES_PATH);
    }

    private DatabaseReference getUserChatsReference(){
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

    private DatabaseReference getCurrentUserFcmTokenReference() {
        DatabaseReference currentUserFcmTokenReference = null;
        String authUserId = getAuthUserId();
        if (authUserId != null) {
            currentUserFcmTokenReference = getUsersReference().child(authUserId).child(FCM_TOKEN);
        }
        return currentUserFcmTokenReference;
    }

    public DatabaseReference getNotificationsReference() {
        return getUsersReference().child(getAuthUserId()).child(NOTIFICATIONS);
    }

    public DatabaseReference getNewsReference(){
        return databaseRef.child(NEWS_PATH);
    }
    //endregion

    //region Storage
    public FirebaseStorage getStorage() {
        return FirebaseStorage.getInstance();
    }

    // TODO handle wrong http to avoid crashing the app
    public void getHttpFromGs(final GetHttpFromGsCallback getHttpFromGsCallback, String http) {
        getStorage().getReferenceFromUrl(http).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        getHttpFromGsCallback.showHttp(uri);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // TODO Log error
                    }
                });
    }

    public void getPdfHttpFromGsWithoutPrefix(final GetHttpFromGsCallback getHttpFromGsCallback, String http) {
        String gsPrefix = "gs://acao-f519d.appspot.com/";
        getHttpFromGs(getHttpFromGsCallback, gsPrefix + http);
    }

    public interface GetHttpFromGsCallback {

        void showHttp(Uri http);
    }
    //endregion

    //region Push Notifications
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
    //endregion
}