package com.penseapp.acaocontabilidade.chat.messages.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.penseapp.acaocontabilidade.R;
import com.penseapp.acaocontabilidade.chat.messages.model.Message;
import com.penseapp.acaocontabilidade.chat.messages.presenter.MessagesPresenter;
import com.penseapp.acaocontabilidade.chat.messages.presenter.MessagesPresenterImpl;
import com.penseapp.acaocontabilidade.chat.chats.view.ChatsActivity;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;

import java.util.ArrayList;

public class MessagesActivity extends AppCompatActivity implements MessagesView {
    private final static String LOG_TAG = MessagesActivity.class.getSimpleName();
    public static ArrayList<Message> mMessages = new ArrayList<>();
    private MessagesPresenter messagesPresenter;
    private RecyclerView mMessagesRecyclerView;
    private MessagesAdapter messagesAdapter;
    private String mChatId;
    public final static int PICK_PHOTO_CODE = 1046;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        setupToolBar();

        // Get information from Intent that called this Activity
        mChatId = getIntent().getExtras().getString(ChatsActivity.SELECTED_CHAT_KEY);
        String mChatName = getIntent().getExtras().getString(ChatsActivity.SELECTED_CHAT_NAME);
        setTitle(mChatName);

        mMessagesRecyclerView = (RecyclerView) findViewById(R.id.list_messages);

        // Chats list RecyclerView
        setupRecyclerView();
//        setupRecyclerViewDecorator();
//        setOnItemDragDropSwipe();
//        setOnItemClickListener();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Connect to Presenters
        messagesPresenter = new MessagesPresenterImpl(this, mChatId);

        clearRecyclerView();
        resetUnreadMessageCount();
        messagesPresenter.subscribeForMessagesUpdates();
    }

    @Override
    protected void onStop() {
        messagesPresenter.unsubscribeForMessagesUpdates();
        super.onStop();
    }

    // Toolbar
    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null) return;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_send_file, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_camera:
                Toast.makeText(this, "Open camera", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_gallery:
                Toast.makeText(this, "Open gallery", Toast.LENGTH_SHORT).show();
                pickPhotoFromGallery();
                return true;
            case R.id.action_document:
                Toast.makeText(this, "Open document", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Messages list

    private void setupRecyclerView() {
        messagesAdapter = new MessagesAdapter(this, mMessages);
        mMessagesRecyclerView.setAdapter(messagesAdapter);
        mMessagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mMessagesRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
//                mMessagesRecyclerView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mMessagesRecyclerView.smoothScrollToPosition(messagesAdapter.getItemCount() - 1);
//                    }
//                }, 100);
//                mMessagesRecyclerView.smoothScrollToPosition(mMessages.size() - 1);
//                Toast.makeText(getApplicationContext(), "Evento", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void clearRecyclerView() {
        mMessages.clear();
        messagesAdapter.notifyDataSetChanged(); // TODO not efficient
    }

    public void resetUnreadMessageCount() {
        messagesPresenter.resetUnreadMessageCount(mChatId);
    }

    @Override
    public void onMessageAdded(Message message) {
        Log.i(LOG_TAG, "View onMessageAdded called");
        mMessages.add(message);
        messagesAdapter.notifyItemInserted(mMessages.size() - 1);
        mMessagesRecyclerView.smoothScrollToPosition(mMessages.size() - 1);

        resetUnreadMessageCount();
    }

    public void onClickSendMessage(View view) {
//        Toast.makeText(getApplicationContext(), "Enviando mensagem", Toast.LENGTH_SHORT).show();
        EditText messageEdit = (EditText) findViewById(R.id.messageEdit);
        Button sendButton = (Button) findViewById(R.id.sendButton);

        // Create message with entered text and current user information
        String text = messageEdit.getText().toString().trim();
        String senderId = FirebaseHelper.getInstance().getAuthUserId();
        String senderName = FirebaseHelper.getInstance().getAuthUserEmail();

        // Clear messageEdit and hide keyboard
        messageEdit.getText().clear();
//        Utilities.hideSoftKeyboard(this);

        if (!text.isEmpty()) {
            messagesPresenter.sendMessage(text, senderId, senderName);
        }
    }

    private void pickPhotoFromGallery() {
        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("image/*");

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Uri photoUri = data.getData();
            Toast.makeText(this, photoUri.getPath() + "selected", Toast.LENGTH_SHORT).show();

//            // Do something with the photo based on Uri
//            Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
//            // Load the selected image into a preview
//            ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
//            ivPreview.setImageBitmap(selectedImage);
        }
    }
}
