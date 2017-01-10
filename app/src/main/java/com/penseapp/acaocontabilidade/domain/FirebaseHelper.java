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

    public static final String DEFAULT_EXERCISE_IMAGE_PNG = "exercise_image.png";
    public static final String USER_CHATCONTACTS_CHAT = "user-chatContacts:chat";
    public static final String NEWS = "news";

    // Realtime database
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private static final String USERS_PATH = "users";
    private static final String CHATS_PATH = "chats";
    private static final String USER_CHATS_PATH = "user-chats";
    private static final String CHAT_USERS_PATH = "chat-users";


    // Storage
//    private FirebaseStorage storage;
//    private StorageReference storageRef;
//    private StorageReference defaultExercisesImagesRef;
//    private static final String STORAGE_REFERENCE_URL = "gs://gogym-c807f.appspot.com";
//    private static final String DEFAULT_EXERCISES_IMAGES = "default_exercises_images";

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
            // TODO ativar data persistence sem rede (não sei se deve ser colocado aqui)
            databaseRef = database.getReference();
        }
//        if (storage == null) {
//            storage = FirebaseStorage.getInstance();
//            storageRef = storage.getReferenceFromUrl(STORAGE_REFERENCE_URL);
//            defaultExercisesImagesRef = storageRef.child(DEFAULT_EXERCISES_IMAGES);
//        }
    }

    public DatabaseReference getDatabaseRef() {
        return databaseRef;
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

    public DatabaseReference getUserReference(String email){
        DatabaseReference userReference = null;
        if (email != null) {
            String emailKey = email.replace(".", "_");
            userReference = databaseRef.getRoot().child(USERS_PATH).child(emailKey);
        }
        return userReference;
    }


    // Realtime database

    public DatabaseReference getUsersReference(){
        return databaseRef.child(USERS_PATH);
    }

    public DatabaseReference getChatsReference(){
        return databaseRef.child(CHATS_PATH);
    }

    public DatabaseReference getCurrentChatReference(String chatId){
        return getChatsReference().child(chatId).child("messages");
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
        return databaseRef.child(NEWS);
    }

    public DatabaseReference getCurrentUserReference() {
        return getUserReference(getAuthUserId());
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

    // Storage

//    public StorageReference getDefaultExercisesImagesRef() {
//        return defaultExercisesImagesRef;
//    }

//    public StorageReference getImageRef() {
//        return defaultExercisesImagesRef.child(DEFAULT_EXERCISE_IMAGE_PNG);
//    }

//
//    public void changeUserConnectionStatus(boolean online) {
//        if (getMyUserReference() != null) {
//            Map<String, Object> updates = new HashMap<String, Object>();
//            updates.put("online", online);
//            getMyUserReference().updateChildren(updates);
//
//            notifyContactsOfConnectionChange(online);
//        }
//    }
//
//    public void notifyContactsOfConnectionChange(final boolean online, final boolean signoff) {
//        final String myEmail = getAuthUserEmail();
//        getMyContactsReference().addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                for (DataSnapshot child : snapshot.getChildren()) {
//                    String email = child.getKey();
//                    DatabaseReference reference = getOneContactReference(email, myEmail);
//                    reference.setValue(online);
//                }
//                if (signoff){
//                    FirebaseAuth.getInstance().signOut();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError firebaseError) {
//            }
//        });
//    }
//
//    public void notifyContactsOfConnectionChange(boolean online) {
//        notifyContactsOfConnectionChange(online, false);
//    }

//    public void signOff(){
//        notifyContactsOfConnectionChange(User.OFFLINE, true);
//    }
}