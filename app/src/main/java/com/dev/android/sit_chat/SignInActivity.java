package com.dev.android.sit_chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dev.android.sit_chat.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {
   ActivitySignInBinding binding;
   ProgressDialog progressDialog;
   private FirebaseAuth mAuth;
   private FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        progressDialog=new ProgressDialog(SignInActivity.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Please Wait\n Validation in progress....");

        //Hide Action Bar
        getSupportActionBar().hide();


        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!binding.txtEmail.getText().toString().isEmpty() &&
                       !binding.txtPassword.getText().toString().isEmpty())
               {
                   progressDialog.show();
                   mAuth.signInWithEmailAndPassword(binding.txtEmail.getText().toString(),
                           binding.txtPassword.getText().toString())
                           .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {
                                  progressDialog.dismiss();
                                  if(task.isSuccessful()){
                                      Intent intent=new Intent(SignInActivity.this,MainActivity.class);

                                      binding.txtEmail.setText("");
                                      binding.txtPassword.setText("");
                                      startActivity(intent);
                                      finish();
                                  }else{
                                      Toast.makeText(SignInActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                  }
                               }
                           });

               }else{
                   Toast.makeText(SignInActivity.this, "Enter Credential", Toast.LENGTH_SHORT).show();
               }
            }
        });
        //After kill app login user should stay log in
        if(mAuth.getCurrentUser()!=null){
            Intent intent=new Intent(SignInActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        //Go to Sign Up Activity
        binding.txtClickSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}