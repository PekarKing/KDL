package com.android.kdl;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText login, pass, passRep;
    private Button getReg;
    private CheckBox iAgree;
    private TextView Agreement;
    private FirebaseAuth auth;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getReg = findViewById(R.id.getRegister);
        login = findViewById(R.id.enterLogin);
        pass = findViewById(R.id.enterPassword);
        passRep = findViewById(R.id.repeatPassword);
        iAgree = findViewById(R.id.iAgree);
        Agreement = findViewById(R.id.Agreement);
        progressBar = findViewById(R.id.regProgressBar);

        getReg.setOnClickListener(this);
        Agreement.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Agreement:
                Toast.makeText(this, "Пользовательское соглашение", Toast.LENGTH_LONG).show();
                break;
            case R.id.getRegister:
                CheckingAll();
                break;
        }
    }

    private void CheckingAll() {

        String loginStr = login.getText().toString().trim();
        String passStr = pass.getText().toString().trim();
        String passRepStr = passRep.getText().toString().trim();

        if (TextUtils.isEmpty(loginStr)) {
            Toast.makeText(getApplicationContext(), "Введите логин!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(passStr)) {
            Toast.makeText(getApplicationContext(), "Введите пароль!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (passStr.length() < 6) {
            Toast.makeText(getApplicationContext(), "Пароль слишком короткий!\nМинимальная длинна 6 символов", Toast.LENGTH_SHORT).show();
            return;
        }

        if (passStr.equals(passRepStr) == false) {
            Toast.makeText(this, "Пароли не совпадают!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (iAgree.isChecked() == false) {
            Toast.makeText(this, "Примите пользовательское соглашение", Toast.LENGTH_SHORT).show();
            return;
        }

        startRegistering();
    }

    private void startRegistering() {
        auth = FirebaseAuth.getInstance();

        final String loginStr = login.getText().toString().trim();
        final String passStr = pass.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(loginStr, passStr)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(RegisterActivity.this, "Пользователь создан", Toast.LENGTH_SHORT).show();
                        if (!task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, "Не удалось создать\nпользователя", Toast.LENGTH_SHORT).show();
                        } else {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("users");
                            myRef.child(auth.getUid()).child("email").setValue(loginStr);

                            auth.signInWithEmailAndPassword(loginStr, passStr)
                                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            progressBar.setVisibility(View.GONE);
                                                Intent intent = new Intent(RegisterActivity.this, MainScreen.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                    });
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
