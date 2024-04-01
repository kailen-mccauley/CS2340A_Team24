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




public class CreateAccountViewModel {
    private static volatile CreateAccountViewModel instance;

    private MutableLiveData<Boolean> createSuccess = new MutableLiveData<>(false);
    public static CreateAccountViewModel getInstance() {
        if (instance == null) {
            synchronized (PersonalActivityViewModel.class) {
                if (instance == null) {
                    instance = new CreateAccountViewModel();
                }
            }
        }
        return instance;
    }

    public LiveData<Boolean> getCreateSuccess() {
        return createSuccess;
    }

    public void setCreateSuccess(boolean success) {
        createSuccess.setValue(success);
    }


    public void createAccount(String email, String password,
                              FirebaseAuth mAuth, CreateAccountActivity createAccountActivity) {
        // if userName and password pass check, we must create an account before going to login
        Task<AuthResult> authResultTask = mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(createAccountActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            setCreateSuccess(true);

                        } else {
                            // If sign up fails, display a message to the user.
                            setCreateSuccess(false);
                            Log.w(TAG, "createUserWithEmail:failure",
                                    task.getException());
                            Toast.makeText(createAccountActivity,
                                    "Authentication failed." + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }
}
