package com.dev.android.sit_chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dev.android.sit_chat.Models.Users;
import com.dev.android.sit_chat.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

       //hide Action Bar
        getSupportActionBar().hide();

        progressDialog=new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Creating account");
        progressDialog.setMessage("We're creating your account");

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.txtUsername.getText().toString().isEmpty() && !binding.txtEmail.getText().toString().isEmpty() &&
                        !binding.txtPassword.getText().toString().isEmpty())
                {
                      progressDialog.show();
                      mAuth.createUserWithEmailAndPassword(binding.txtEmail.getText().toString(),binding.txtPassword.getText().toString())
                              .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                  @Override
                                  public void onComplete(@NonNull Task<AuthResult> task) {
                                      progressDialog.dismiss();
                                      if(task.isSuccessful()){
                                          //Database
                                          Users user=new Users(binding.txtUsername.getText().toString(),
                                                  binding.txtEmail.getText().toString(),binding.txtPassword.getText().toString());
                                          //Get Uid
                                          String id=task.getResult().getUser().getUid();
                                          //Save Data in Database
                                          database.getReference().child("Users").child(id).setValue(user);

                                          //End Database

                                          Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                          binding.txtUsername.setText("");
                                          binding.txtEmail.setText("");
                                          binding.txtPassword.setText("");
                                      }else{
                                          Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

                                      }
                                  }
                              });
                }else{
                    Toast.makeText(SignUpActivity.this, "Enter Credential", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Go to SignIn Activity
        binding.txtAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}