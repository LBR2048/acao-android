package com.penseapp.acaocontabilidade.chat.storage;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;

/**
 * Created by unity on 31/01/18.
 */

public class StorageImpl implements Storage {

    private FirebaseHelper mFirebaseHelperInstance = FirebaseHelper.getInstance();
    private FirebaseStorage mStorage = mFirebaseHelperInstance.getStorage();

    @Override
    public void uploadFile(final UploadFileCallback uploadFileCallback, Uri fileUri, String storagePath) {
        // Create a storage reference from our app
        StorageReference storageRef = mStorage.getReference();

        // Upload from a local file
        //You can upload local files on the device, such as photos and videos from the camera,
        // with the putFile() method. putFile() takes a File and returns an UploadTask which
        // you can use to manage and monitor the status of the upload.
        StorageReference riversRef = storageRef.child(storagePath);
        UploadTask uploadTask = riversRef.putFile(fileUri);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.i("Storage", "Failure");
                uploadFileCallback.onFailure(exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                uploadFileCallback.onSuccess(taskSnapshot);
            }
        });
    }
}
