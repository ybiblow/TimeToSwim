package dev.jacob_ba.timetoswim.Activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import dev.jacob_ba.timetoswim.R;
import dev.jacob_ba.timetoswim.model.Controller;
import dev.jacob_ba.timetoswim.model.Student;

public class LoginActivity extends AppCompatActivity {
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createSignInIntent();
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Log.i("Info", "User Uid: " + user.getUid());
            Log.i("Info", "User display name: " + user.getDisplayName());
//            Teacher teacher = new Teacher(user.getUid(), "Yotam", "Asgard");
//            Controller.getInstance().addTeacherToDatabase(teacher);
            Controller.getInstance().loadSystem(this);
            /*
            if (Controller.getInstance().isUserTeacher()) {
                Intent intent = new Intent(this, AddShiftActivity.class);
                this.startActivity(intent);
                this.finish();
            }
            if (Controller.getInstance().isUserStudent()) {
                if (!Controller.getInstance().isUserExists()) {
                    Student s = new Student(user.getUid(), user.getDisplayName());
                }
                Intent intent = new Intent(this, AddLessonActivity.class);
                this.startActivity(intent);
                this.finish();
            }
            */

        } else {

            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }

    public void createSignInIntent() {
        // [START auth_fui_create_intent]
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers).setLogo(R.drawable.logo_white)
                .setIsSmartLockEnabled(false)
                .setTheme(R.style.LoginTheme)
                .build();
        signInLauncher.launch(signInIntent);
        // [END auth_fui_create_intent]
    }

}