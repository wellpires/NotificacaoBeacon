package br.com.everis.notificacaobeacon.utils;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;

import br.com.everis.notificacaobeacon.bd.DAOHelper;

/**
 * Created by wgoncalv on 18/10/2017.
 */

public class FirebaseUtils {

    private FirebaseStorage sf = null;

    public FirebaseUtils() {
        this.sf = FirebaseStorage.getInstance();
    }

    public File uploadFile(File file, OnSuccessListener<UploadTask.TaskSnapshot> onSuccessListener, OnFailureListener onFailureListener) throws FileNotFoundException {
        StorageReference reference = sf.getReference();
        StorageReference child = reference.child("anexos/" + file.getName());
        UploadTask uploadTask = child.putFile(Uri.fromFile(file));
        uploadTask.addOnFailureListener(onFailureListener);
        uploadTask.addOnSuccessListener(onSuccessListener);
        return file;
    }

}
