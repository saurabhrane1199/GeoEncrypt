package com.encrypto.android.geoencrypt.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.encrypto.android.geoencrypt.database.AuthRepository;
import com.encrypto.android.geoencrypt.models.User;
import com.google.firebase.auth.AuthCredential;

import java.util.List;

public class AuthViewModel extends AndroidViewModel {
    private AuthRepository authRepository;
    public LiveData<User> authenticatedUserLiveData;
    public LiveData<User> existingUserLiveData;

    public AuthViewModel(Application application) {
        super(application);
        authRepository = new AuthRepository();
    }

    public  void checkAndGetCurrentUser(String uid){
        existingUserLiveData = authRepository.getCurrentUser(uid);
    }


    public void signInWithGoogle(AuthCredential googleAuthCredential) {
        authenticatedUserLiveData = authRepository.firebaseSignInWithGoogle(googleAuthCredential);
    }


}
