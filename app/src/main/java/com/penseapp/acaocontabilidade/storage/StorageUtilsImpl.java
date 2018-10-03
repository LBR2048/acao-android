package com.penseapp.acaocontabilidade.storage;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by leonardo on 10/12/17.
 */

public class StorageUtilsImpl implements StorageUtils {

    private static String LOG_TAG = StorageUtilsImpl.class.getSimpleName();

    //    @Override
    public static void getFileDownloadUrl(String firebaseStorageUri) {
        FirebaseStorage storage = FirebaseHelper.getInstance().getStorage();

        storage.getReferenceFromUrl(firebaseStorageUri).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Log.d(LOG_TAG, uri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.d(LOG_TAG, "Failure");
            }
        });
    }

//    @Override
    public static void downloadFile(@NonNull String gsRef) {
//        File dir = new File(Environment.getExternalStorageDirectory() + "/photos");
//        final File localFile = new File(dir, UUID.randomUUID().toString() + ".png");
//        try {
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//            localFile.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        File localFile = getStorageDir("album");

        StorageReference fileRef = FirebaseHelper.getInstance().getStorage().getReferenceFromUrl(gsRef);

        try {
//            File localFile = File.createTempFile("images", "jpg");

            fileRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    Log.i(LOG_TAG, "Success");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Log.i(LOG_TAG, "Failure");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static File getStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }
}
