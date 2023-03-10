package com.dev.android.sit_chat;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dev.android.sit_chat.databinding.ActivitySignInBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {
   ActivitySignInBinding binding;
   ProgressDialog progressDialog;
   private FirebaseAuth mAuth;
   private FirebaseDatabase database;

   private  static  final String TAG="GoogleActivity";
   private static final int RC_SIGN_IN=900;

   private GoogleSignInClient mGoogleSignInClient;


   @Override
   public void onStart(){
       super.onStart();
       FirebaseUser currentUser= mAuth.getCurrentUser();
   }


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


        //Start Google SignIn
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);

        //End Google SignIn



        //Start SignIn Button
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!binding.txtSignInEmail.getText().toString().isEmpty() &&
                       !binding.txtSignInPassword.getText().toString().isEmpty())
               {
                   progressDialog.show();
                   mAuth.signInWithEmailAndPassword(binding.txtSignInEmail.getText().toString(),
                           binding.txtSignInPassword.getText().toString())
                           .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {
                                  progressDialog.dismiss();
                                  if(task.isSuccessful()){
                                      Intent intent=new Intent(SignInActivity.this,MainActivity.class);

                                      binding.txtSignInEmail.setText("");
                                      binding.txtSignInPassword.setText("");
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
        //End SignIn Button

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

        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                signIn();
            }
        });

    }
    //Start Google signIn
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       if(requestCode==RC_SIGN_IN){
           Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
           try{
               GoogleSignInAccount account=task.getResult(ApiException.class);
               Log.d(TAG,"firebaseAuthWithGoogle:"+account.getId());
               firebaseAuthWithGoogle(account.getIdToken());
           }catch (ApiException e){
               Log.w(TAG,"Google Sign in failed: ",e);
           }
       }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential= GoogleAuthProvider.getCredential(idToken,null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Log.d(TAG,"SigninWithCredential:Success");
                            FirebaseUser user=mAuth.getCurrentUser();
                            updateUI(user);
                        }else{
                            Log.w(TAG,"SigninWithCredential:Failure",task.getException());
                            updateUI(null);
                        }
                    }
                });
    }
    private  void signIn(){
       Intent intent=mGoogleSignInClient.getSignInIntent();
       startActivityForResult(intent,RC_SIGN_IN);
    }
    private  void updateUI(FirebaseUser user){
       Intent intent=new Intent(SignInActivity.this,MainActivity.class);
       if(user!=null){
           startActivity(intent);
           finish();
       }
    }
}