package com.penseapp.acaocontabilidade.chat.messages.view;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.penseapp.acaocontabilidade.R;
import com.penseapp.acaocontabilidade.chat.messages.model.Message;
import com.penseapp.acaocontabilidade.chat.messages.presenter.MessagesPresenter;
import com.penseapp.acaocontabilidade.chat.messages.presenter.MessagesPresenterImpl;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;

import java.io.File;
import java.util.ArrayList;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MessagesActivity extends AppCompatActivity implements MessagesView {

    public static final String SELECTED_CHAT_KEY = "selected_chat_key";
    public static final String SELECTED_CHAT_NAME = "selected_chat_name";
    //region Constants
    private final static String LOG_TAG = MessagesActivity.class.getSimpleName();
    private final static int PICK_PHOTO_CODE = 1046;
    private final static int PICK_DOCUMENT_CODE = 1047;
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    //endregion

    private static final String DIRECTORY_TAG = "Camera";
    private MessagesPresenter messagesPresenter;
    private RecyclerView mMessagesRecyclerView;
    private MessagesAdapter messagesAdapter;
    private String mChatId;
    private static final String photoFileName = "photo.jpg";
    //region Member variables
    private final ArrayList<Message> mMessages = new ArrayList<>();
    private final String senderId = FirebaseHelper.getInstance().getAuthUserId();
    private final String senderName = FirebaseHelper.getInstance().getAuthUserEmail();
    private File photoFile;
    //endregion

    //region Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        setupToolBar();

        // Get information from Intent that called this Activity
        if (getIntent().getExtras() != null) {
            mChatId = getIntent().getExtras().getString(SELECTED_CHAT_KEY);

            String mChatName = getIntent().getExtras().getString(SELECTED_CHAT_NAME);
            setTitle(mChatName);
        }

        mMessagesRecyclerView = findViewById(R.id.list_messages);

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
    //endregion

    //region Toolbar
    private void setupToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar == null) return;

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
//            case R.id.action_camera:
//                MessagesActivityPermissionsDispatcher.launchCameraWithPermissionCheck(this);
//                return true;
            case R.id.action_gallery:
                pickPhotoFromGallery();
                return true;
            case R.id.action_document:
                pickFileFromGallery();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //endregion

    //region Messages list
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

    private void resetUnreadMessageCount() {
        messagesPresenter.resetUnreadMessageCount(mChatId);
    }
    //endregion

    @Override
    public void onMessageAdded(Message message) {
        Log.i(LOG_TAG, "View onMessageAdded called");
        mMessages.add(message);
        messagesAdapter.notifyItemInserted(mMessages.size() - 1);
        mMessagesRecyclerView.scrollToPosition(mMessages.size() - 1);

        resetUnreadMessageCount();
    }

    @Override
    public void onMessageChanged(Message message) {
        int index = getIndexForKey(message.getKey());
        mMessages.set(index, message);
        messagesAdapter.notifyItemChanged(index);
    }

    public void onClickSendMessage(View view) {
        EditText messageEdit = findViewById(R.id.messageEdit);
        String text = messageEdit.getText().toString().trim();
        messageEdit.getText().clear();
        if (!text.isEmpty()) {
            messagesPresenter.sendMessage(text, senderId, senderName, null, null);
        }
    }

    private void pickPhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }

    private void pickFileFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, PICK_DOCUMENT_CODE);
        }
    }

    @NeedsPermission({Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        // required for API >= 24
        // https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(MessagesActivity.this, "com.penseapp.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private File getPhotoFileUri(String fileName) {
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES), DIRECTORY_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                Log.d(DIRECTORY_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            return new File(mediaStorageDir.getPath() + File.separator + fileName);
        }
        return null;
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Delegate the permission handling to generated method
        MessagesActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case PICK_PHOTO_CODE: {
                    Uri photoUri = data.getData();
                    messagesPresenter.sendMessage(null, senderId, senderName, photoUri, null);

                    break;
                }
                case PICK_DOCUMENT_CODE:
                    Uri documentUri = data.getData();
                    messagesPresenter.sendMessage(null, senderId, senderName, null, documentUri);

                    break;
                case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE: {
                    Uri photoUri = Uri.fromFile(photoFile);
                    messagesPresenter.sendMessage(null, senderId, senderName, photoUri, null);
                    break;
                }
            }
        }
    }

    // TODO this method should go somewhere else? Does it belong in the View?
    // TODO duplicado em ExerciseChooserActivity
    private int getIndexForKey(String key) {
        int index = 0;
        for (Message message : mMessages) {
            if (message.getKey().equals(key)) {
                return index;
            } else {
                index++;
            }
        }
        throw new IllegalArgumentException("Key not found");
    }
}
