package com.ksh.motsa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputEmail, inputPwd, inputLocation, inputPhone, inputName;
    private Button createAccountButton;
    private FirebaseAuth firebaseAuth;
    private String userId;
    private ProgressBar mProgrssBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initWidgets();

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgrssBar.setVisibility(View.VISIBLE);
                createAccount();
            }
        });
    }

    // 위젯 초기화
    private void initWidgets() {
        inputEmail = (EditText) findViewById(R.id.sinUp_email_input);
        inputPwd = (EditText) findViewById(R.id.sinUp_pwd_input);
        inputLocation = (EditText) findViewById(R.id.sinUp_location_input);
        inputPhone = (EditText) findViewById(R.id.sinUp_phone_input);
        inputName = (EditText) findViewById(R.id.sinUp_name_input);
        mProgrssBar = (ProgressBar) findViewById(R.id.signUp_loadingBar);
        createAccountButton = (Button) findViewById(R.id.sinUp_btn);
        mProgrssBar.setVisibility(View.GONE);
    }

    // 아이디생성
    private void createAccount() {

        String email = inputEmail.getText().toString().trim();
        String pwd = inputPwd.getText().toString().trim();
        String phone = inputPhone.getText().toString().trim();
        String location = inputLocation.getText().toString();
        String name = inputName.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(location) || TextUtils.isEmpty(name)) {
            Toast.makeText(SignUpActivity.this, "모든 내용을 적어주세요", Toast.LENGTH_SHORT).show();
        } else {
            // 유효성 검사
            authAdd(email, pwd, phone, location, name);
        }
    }

    private void authAdd(final String email, final String pwd, final String phone, final String location, final String name) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    userId = firebaseAuth.getCurrentUser().getUid();
                    validateEmail(name, location, phone, userId);
                } else {
                    Toast.makeText(SignUpActivity.this, "등록에러 다시시도 해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void validateEmail(final String name, final String location, final String phone, final String userId) {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((!dataSnapshot.child("Users").child(userId).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phone);
                    userdataMap.put("location", location);
                    userdataMap.put("name", name);
                    rootRef.child("Users").child(userId).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "축하드립니다. 회원가입이 되셨습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignUpActivity.this, "네트워크 오류입니다. 다시 시도 해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(SignUpActivity.this, "이미 이메일이 존재합니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
