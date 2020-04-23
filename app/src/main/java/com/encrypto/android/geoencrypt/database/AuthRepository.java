package com.encrypto.android.geoencrypt.database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.encrypto.android.geoencrypt.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthRepository {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = rootRef.collection("users");


    public  MutableLiveData<User> getCurrentUser(String uid){
        final MutableLiveData<User> currentUserLiveData = new MutableLiveData<>();
        final DocumentReference uidRef = usersRef.document(uid);
        uidRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                currentUserLiveData.setValue(user);
            }
        });

        return currentUserLiveData;
    }

    public MutableLiveData<User> firebaseSignInWithGoogle(AuthCredential googleAuthCredential) {
        final MutableLiveData<User> authenticatedUserMutableLiveData = new MutableLiveData<>();
        firebaseAuth.signInWithCredential(googleAuthCredential).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> authTask) {
                        if (authTask.isSuccessful()) {
                            boolean isNewUser = authTask.getResult().getAdditionalUserInfo().isNewUser();
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                String uid = firebaseUser.getUid();
                                String name = firebaseUser.getDisplayName();
                                String email = firebaseUser.getEmail();
                                User user = new User(uid, name, email);
                                if(isNewUser){
                                     createUserInFirestoreIfNotExists(user);
                                }
                                authenticatedUserMutableLiveData.setValue(user);
                            }
                        } else {
                            Log.e("ERROR", authTask.getException().getMessage());
                        }
                    }
                });
        return authenticatedUserMutableLiveData;
    }


    public void createUserInFirestoreIfNotExists(final User authenticatedUser) {
        final DocumentReference uidRef = usersRef.document(authenticatedUser.uid);
        uidRef.get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> uidTask) {
                        if (uidTask.isSuccessful()) {
                            DocumentSnapshot document = uidTask.getResult();
                            if (!document.exists()) {
                                uidRef.set(authenticatedUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> userCreationTask) {

                                            Log.e("ERROR", userCreationTask.getException().getMessage());
                                        }
                                });
                            }
                        } else {
                            Log.e("ERROR", uidTask.getException().getMessage());
                        }
                    }
                });
    }
}
