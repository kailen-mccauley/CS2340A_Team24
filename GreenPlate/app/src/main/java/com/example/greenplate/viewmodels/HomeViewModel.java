package com.example.greenplate.viewmodels;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.greenplate.views.CreateAccountActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class HomeViewModel {

    private static volatile HomeViewModel instance;
    private FirebaseAuth mAuth;
    private MutableLiveData<Boolean> createSuccess = new MutableLiveData<>(false);
    private HomeViewModel() {
        this.mAuth = FirebaseAuth.getInstance();
    }
    public static HomeViewModel getInstance() {
        if (instance == null) {
            synchronized (PersonalActivityViewModel.class) {
                if (instance == null) {
                    instance = new HomeViewModel();
                }
            }
        }
        return instance;
    }

}
