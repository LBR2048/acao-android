package com.penseapp.acaocontabilidade.chat.storage;

import android.net.Uri;

import com.google.firebase.storage.UploadTask;

/**
 * Created by unity on 31/01/18.
 */

public interface Storage {

    interface UploadFileCallback {

        void onSuccess(UploadTask.TaskSnapshot taskSnapshot);

        void onFailure(Exception exception);
    }

    void uploadFile(UploadFileCallback uploadFileCallback, Uri fileUri, String storagePath);
}
