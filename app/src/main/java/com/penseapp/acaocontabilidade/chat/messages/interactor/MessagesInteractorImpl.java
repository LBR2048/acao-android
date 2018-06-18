package com.penseapp.acaocontabilidade.chat.messages.interactor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.penseapp.acaocontabilidade.chat.contacts.interactor.ContactsInteractorImpl;
import com.penseapp.acaocontabilidade.chat.messages.model.Message;
import com.penseapp.acaocontabilidade.chat.messages.presenter.MessagesPresenter;
import com.penseapp.acaocontabilidade.chat.storage.Storage;
import com.penseapp.acaocontabilidade.chat.storage.StorageImpl;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;


/**
 * Created by unity on 21/11/16.
 */

public class MessagesInteractorImpl implements MessagesInteractor {

    private final static String LOG_TAG = ContactsInteractorImpl.class.getSimpleName();

    private final MessagesPresenter messagesPresenter;
    private final DatabaseReference chatMessagesReference;
    private final Storage storage;

    // Firebase
    private FirebaseHelper mFirebaseHelperInstance = FirebaseHelper.getInstance();
    private FirebaseStorage mStorage = mFirebaseHelperInstance.getStorage();
    private ChildEventListener messagesChildEventListener;
    private DatabaseReference userChatPropertiesReference;
    private String currentChatId;
    private String currentUserId = mFirebaseHelperInstance.getAuthUserId();

    public MessagesInteractorImpl(MessagesPresenter messagesPresenter, String currentChatId) {
        this.messagesPresenter = messagesPresenter;
        chatMessagesReference = mFirebaseHelperInstance.getChatMessagesReference();
        userChatPropertiesReference = mFirebaseHelperInstance.getUserChatPropertiesReference();
        this.currentChatId = currentChatId;
        storage = new StorageImpl();
    }

    @Override
    public void subscribeForMessagesUpdates() {
        Log.i(LOG_TAG, "Interactor " + LOG_TAG + " called");

        if (messagesChildEventListener == null) {
            messagesChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                    Log.d("Messages", dataSnapshot.toString());
                    String messageKey = dataSnapshot.getKey();
                    try {
                        final Message message = dataSnapshot.getValue(Message.class);
                        message.setKey(messageKey);
                        messagesPresenter.onMessageAdded(message);

                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Error while reading message " + messageKey);
                        Message message = new Message();
                        messagesPresenter.onMessageAdded(message);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            chatMessagesReference.child(currentChatId).addChildEventListener(messagesChildEventListener);
        }
    }

    private void getPdfHttpFromGs(final DataSnapshot dataSnapshot, final Message message) {
        String gsPrefix = "gs://acao-f519d.appspot.com/";
        mStorage.getReferenceFromUrl(gsPrefix + message.getPDF()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        message.setPDFDownloadURL(uri.toString());
                        messagesPresenter.onMessageAdded(message);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d(LOG_TAG, "Failure");

                        Log.i(LOG_TAG, dataSnapshot.toString() + " added");
                        messagesPresenter.onMessageAdded(message);
                    }
                });
    }

    private void getPhotoHttpFromGs(final DataSnapshot dataSnapshot, final Message message) {
        mStorage.getReferenceFromUrl(message.getPhotoURL()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        message.setPhotoDownloadURL(uri.toString());
                        messagesPresenter.onMessageAdded(message);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d(LOG_TAG, "Failure");

                        Log.i(LOG_TAG, dataSnapshot.toString() + " added");
                        messagesPresenter.onMessageAdded(message);
                    }
                });
    }

    @Override
    public void unsubscribeForMessagesUpdates() {
        if (messagesChildEventListener != null) {
            chatMessagesReference.child(currentChatId).removeEventListener(messagesChildEventListener);
        }
    }

    @Override
    public void resetUnreadMessageCount(String chatId) {
        userChatPropertiesReference.child(currentUserId).child(chatId).child("unreadMessageCount").setValue(0);
    }

    @Override
    public void sendMessage(final String messageText, final String senderId, final String senderEmail, Uri imageUri, Uri documentUri) {

        if (imageUri != null) {
            final String imagePath = "images/" + String.valueOf(System.currentTimeMillis());

            storage.uploadFile(new Storage.UploadFileCallback() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Log.i("Storage", "Success: " + downloadUrl);
                    saveMessage(messageText, senderId, senderEmail, FirebaseHelper.GS_PREFIX + imagePath, null);
                }

                @Override
                public void onFailure(Exception exception) {
                    Log.i("Storage", exception.getMessage());
                }
            }, imageUri, imagePath);

            return;
        }

        if (documentUri != null) {
            final String documentPath = "documents/" + String.valueOf(System.currentTimeMillis());

            storage.uploadFile(new Storage.UploadFileCallback() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Log.i("Storage", "Success: " + downloadUrl);
                    saveMessage(messageText, senderId, senderEmail, null, documentPath);
                }

                @Override
                public void onFailure(Exception exception) {
                    Log.i("Storage", exception.getMessage());
                }
            }, documentUri, documentPath);

            return;
        }

        saveMessage(messageText, senderId, senderEmail, null, null);
    }

    private void saveMessage(String messageText, String senderId, String senderEmail, String imagePath, String documentPath) {
        // Create empty message at chats/$currentChatId/messages and get its key so we can further reference it
        String newMessageKey = chatMessagesReference.child(currentChatId).push().getKey();

        // Create new message with key received from Firebase
        Message newMessage = new Message();
        newMessage.setText(messageText);
        newMessage.setSenderId(senderId);
        newMessage.setSenderEmail(senderEmail);
        newMessage.setPhotoURL(imagePath);
        newMessage.setPDF(documentPath);
        newMessage.setTimestamp(System.currentTimeMillis());

        // Save new messages in one node only
        // Add newly created message to Firebase chats/$currentChatId/messages/$messageId
        chatMessagesReference.child(currentChatId).child(newMessageKey).setValue(newMessage);

        // Save new messages in two nodes, one for each user
        // Add newly created message to Firebase user-chats-messages/$senderId/$currentChatId/$messageId
        // Add newly created message to Firebase user-chats-messages/$recipientId/$currentChatId/$messageId

        String currentUserId = mFirebaseHelperInstance.getAuthUserId();
        updateLatestMessageTimestamp(currentUserId);
        mFirebaseHelperInstance.getUserChatPropertiesReference().child(currentUserId).child(currentChatId).child("contactId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String contactId = dataSnapshot.getValue(String.class);
                updateLatestMessageTimestamp(contactId);
                incrementUnreadMessageCount(contactId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateLatestMessageTimestamp(String userId) {
        userChatPropertiesReference.child(userId).child(currentChatId).child("latestMessageTimestamp").setValue(- System.currentTimeMillis());
    }

    private void incrementUnreadMessageCount(final String userId) {
        final DatabaseReference unreadMessageCountPath = userChatPropertiesReference.child(userId).child(currentChatId).child("unreadMessageCount");
        unreadMessageCountPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    long unreadMessageCount = dataSnapshot.getValue(long.class);
                    unreadMessageCountPath.setValue(unreadMessageCount + 1);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Could not read unreadMessageCount");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
