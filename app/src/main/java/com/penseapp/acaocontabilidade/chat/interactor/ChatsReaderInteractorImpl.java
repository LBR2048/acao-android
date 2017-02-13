package com.penseapp.acaocontabilidade.chat.interactor;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.penseapp.acaocontabilidade.chat.model.Chat;
import com.penseapp.acaocontabilidade.chat.presenter.ChatsReaderPresenter;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;

/**
 * Created by unity on 21/11/16.
 */

public class ChatsReaderInteractorImpl implements ChatsReaderInteractor {

    private final static String LOG_TAG = ChatsReaderInteractorImpl.class.getSimpleName();

    private final ChatsReaderPresenter chatsReaderPresenter;

    // Firebase
    private FirebaseHelper mFirebaseHelperInstance = FirebaseHelper.getInstance();
    private DatabaseReference currentUserChatsReference = mFirebaseHelperInstance.getCurrentUserChatsReference();
    private String currentUserId = mFirebaseHelperInstance.getAuthUserId();

    private ChildEventListener chatsChildEventListener;

    public ChatsReaderInteractorImpl(ChatsReaderPresenter chatsReaderPresenter) {
        this.chatsReaderPresenter = chatsReaderPresenter;
    }

    @Override
    public void subscribeForUserChatsUpdates() {

        Log.i(LOG_TAG, "subscribeForUserChatsUpdates called, but listener already exists");

        if (chatsChildEventListener == null) {
            Log.i(LOG_TAG, "subscribeForUserChatsUpdates called");

            chatsChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    final String chatKey = dataSnapshot.getKey();
                    try {
                        Chat chat = dataSnapshot.getValue(Chat.class);
                        chat.setKey(dataSnapshot.getKey());
                        Log.i(LOG_TAG, "Chat " + chat.getName() + " added");
                        chatsReaderPresenter.onChatAdded(chat);
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Error while reading chat " + chatKey);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    final String chatKey = dataSnapshot.getKey();
                    try {
                        Chat chat = dataSnapshot.getValue(Chat.class);
                        Log.i(LOG_TAG, "Chat " + chat.getName() + " changed");
                        chat.setKey(dataSnapshot.getKey());
                        chatsReaderPresenter.onChatChanged(chat);
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Error while reading chat " + chatKey);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    final String chatKey = dataSnapshot.getKey();
                    try {
                        String chatId = dataSnapshot.getKey();
                        Log.i(LOG_TAG, "Chat " + chatId + " removed");
                        chatsReaderPresenter.onChatRemoved(chatId);
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Error while reading chat " + chatKey);
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            DatabaseReference userChatPropertiesReference = mFirebaseHelperInstance.getUserChatPropertiesReference();
            userChatPropertiesReference.child(currentUserId).orderByChild("latestMessageTimestamp").addChildEventListener(chatsChildEventListener);
        }
    }
//    @Override
//    public void subscribeForUserChatsUpdates() {
//
//        Log.i(LOG_TAG, "subscribeForUserChatsUpdates called, but listener already exists");
//
//        if (chatsChildEventListener == null) {
//            Log.i(LOG_TAG, "subscribeForUserChatsUpdates called");
//
//            chatsChildEventListener = new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    final String chatKey = dataSnapshot.getKey();
//                    chatsReference.child(chatKey).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            try {
//                                Chat chat = dataSnapshot.getValue(Chat.class);
//                                chat.setKey(dataSnapshot.getKey());
//                                Log.i(LOG_TAG, "Chat " + chat.getName() + " added");
//                                userChatsPresenter.onChatAdded(chat);
//                            } catch (Exception e) {
//                                Log.e(LOG_TAG, "Error while reading chat " + chatKey);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                    final String chatKey = dataSnapshot.getKey();
//                    chatsReference.child(chatKey).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            try {
//                                Chat chat = dataSnapshot.getValue(Chat.class);
//                                Log.i(LOG_TAG, "Chat " + chat.getName() + " changed");
//                                chat.setKey(dataSnapshot.getKey());
//                                userChatsPresenter.onChatChanged(chat);
//                            } catch (Exception e) {
//                                Log.e(LOG_TAG, "Error while reading chat " + chatKey);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//                    final String chatKey = dataSnapshot.getKey();
//                    chatsReference.child(chatKey).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            try {
//                                String chatId = dataSnapshot.getKey();
//                                Log.i(LOG_TAG, "Chat " + chatId + " removed");
//                                userChatsPresenter.onChatRemoved(chatId);
//                            } catch (Exception e) {
//                                Log.e(LOG_TAG, "Error while reading chat " + chatKey);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            };
//
////            currentUserChatsReference.addChildEventListener(chatsChildEventListener);
//            mFirebaseHelperInstance.getUserChatPropertiesReference().child(currentUserId).addChildEventListener(chatsChildEventListener);
//        }
//
//    }

    @Override
    public void unsubscribeForUserChatsUpdates() {
        Log.i(LOG_TAG, "unsubscribeForUserChatsUpdates called, but listener already exists");

        if (chatsChildEventListener != null) {
            Log.i(LOG_TAG, "unsubscribeForUserChatsUpdates called");

            currentUserChatsReference.removeEventListener(chatsChildEventListener);
        }
    }

}