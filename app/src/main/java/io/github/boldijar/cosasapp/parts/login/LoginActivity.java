package io.github.boldijar.cosasapp.parts.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.boldijar.cosasapp.R;
import io.github.boldijar.cosasapp.base.BaseActivity;
import io.github.boldijar.cosasapp.parts.home.HomeActivity;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_email)
    EditText mEmail;
    @BindView(R.id.login_password)
    EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            return;
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login_continue)
    void register() {
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(mEmail.getText().toString(),
                        mPassword.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    }
                })
                .addOnFailureListener(e -> {
                    if (e instanceof FirebaseAuthUserCollisionException) {
                        login();
                        return;
                    }
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }

    private void login() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                mEmail.getText().toString(),
                mPassword.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid login", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
