package com.ksh.motsa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ksh.motsa.Model.Users;
import com.ksh.motsa.Prevalent.Prevalent;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private Button mLogin;
    private EditText mInputEmail, mInputPwd;
    private FirebaseAuth mFirebaseAuth;
    private ProgressBar mProgressBar;
    private CheckBox mCheckBoxRememberMe;
    private String userId, admin;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initWidget();

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                loginUser();
            }
        });

    }

    private void initWidget() {
        mInputEmail = (EditText) findViewById(R.id.login_email_input);
        mInputPwd = (EditText) findViewById(R.id.login_pwd_input);
        mLogin = (Button) findViewById(R.id.login_btn);
        mProgressBar = (ProgressBar) findViewById(R.id.login_loadingBar);
        mCheckBoxRememberMe = (CheckBox) findViewById(R.id.remember_me_chkb);
        mProgressBar.setVisibility(View.GONE);
    }

    private void loginUser() {
        String email = mInputEmail.getText().toString().trim();
        String pwd = mInputPwd.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(LoginActivity.this, "이메일을 적어주세요.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(LoginActivity.this, "비밀번호를 적어주세요.", Toast.LENGTH_SHORT).show();
        } else {
            allowAccessToAccount(email, pwd);
        }
    }

    private void allowAccessToAccount(final String email, String pwd) {

        mFirebaseAuth = mFirebaseAuth.getInstance();

        mFirebaseAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userId = mFirebaseAuth.getCurrentUser().getUid();

                            if (email.equals("admin@admin.com")) {
                                adminLogin(userId);
                            } else {
                                Toast.makeText(LoginActivity.this, "환영합니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "로그인 오류 다시시도 해주세요", Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void adminLogin(final String userId) {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Admins").child(userId).exists()) {
                    Toast.makeText(LoginActivity.this, "관리자님 환영합니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(LoginActivity.this, "관리자 로그인 오류 다시시도 해주세요", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}

