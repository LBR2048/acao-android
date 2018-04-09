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
import android.widget.Button;
import android.widget.EditText;

import com.penseapp.acaocontabilidade.R;
import com.penseapp.acaocontabilidade.chat.chats.view.ChatsActivity;
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
    private final static String LOG_TAG = MessagesActivity.class.getSimpleName();
    public static ArrayList<Message> mMessages = new ArrayList<>();
    private MessagesPresenter messagesPresenter;
    private RecyclerView mMessagesRecyclerView;
    private MessagesAdapter messagesAdapter;
    private String mChatId;
    public final static int PICK_PHOTO_CODE = 1046;
    public final static int PICK_DOCUMENT_CODE = 1047;
    private String senderId = FirebaseHelper.getInstance().getAuthUserId();;
    private String senderName = FirebaseHelper.getInstance().getAuthUserEmail();

    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private final String DIRECTORY_TAG = "Camera";
    private File photoFile;
    private String photoFileName = "photo.jpg";

    //region Lifecycle
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
    //endregion

    //region Toolbar
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
                MessagesActivityPermissionsDispatcher.launchCameraWithPermissionCheck(this);
                return true;
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

    public void resetUnreadMessageCount() {
        messagesPresenter.resetUnreadMessageCount(mChatId);
    }
    //endregion

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

        // Clear messageEdit and hide keyboard
        messageEdit.getText().clear();
//        Utilities.hideSoftKeyboard(this);

        if (!text.isEmpty()) {
            messagesPresenter.sendMessage(text, senderId, senderName, null, null);
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

    private void pickFileFromGallery() {
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
        intent.setType("*/*");

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_DOCUMENT_CODE);
        }
    }

    @NeedsPermission({Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(MessagesActivity.this, "com.penseapp.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    private File getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
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

    // Returns true if external storage for photos is available
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
            if (requestCode == PICK_PHOTO_CODE) {
                Uri photoUri = data.getData();
//                String filePath = photoUri.getPath();
//                Toast.makeText(this, filePath + " selected", Toast.LENGTH_SHORT).show();

                messagesPresenter.sendMessage(null, senderId, senderName, photoUri, null);

//            // Do something with the photo based on Uri
//            Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
//            // Load the selected image into a preview
//            ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
//            ivPreview.setImageBitmap(selectedImage);
            } else if (requestCode == PICK_DOCUMENT_CODE) {
                Uri documentUri = data.getData();
//                String documentPath = documentUri.getPath();
//                Toast.makeText(this, documentPath + " selected", Toast.LENGTH_SHORT).show();

                messagesPresenter.sendMessage(null, senderId, senderName, null, documentUri);

            } else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
//                Toast.makeText(this, "Camera OK", Toast.LENGTH_SHORT).show();
                // by this point we have the camera photo on disk
//                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getPath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                // ivPreview.setImageBitmap(takenImage);

                Uri photoUri = Uri.fromFile(photoFile);
                messagesPresenter.sendMessage(null, senderId, senderName, photoUri, null);
            }
        }
    }
}
