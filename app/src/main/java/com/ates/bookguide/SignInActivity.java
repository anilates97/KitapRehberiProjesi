package com.ates.bookguide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {
   private static final int RC_SIGN_IN = 2;
    TextInputEditText email, password;

    SignInButton signInButton;
    Button submit,forgotPassword;
    String emailText, passwordText;
    FirebaseAuth auth;
    private GoogleSignInClient signInClient;
    LinearLayout wrapper;
    ProgressDialog progressDialog;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        email = findViewById(R.id.emailEdit);
        password = findViewById(R.id.passwordEdit);
        signInButton = findViewById(R.id.signInBtn);
        submit = findViewById(R.id.submitBtn);
        wrapper = findViewById(R.id.wrapper);
        toolbar = findViewById(R.id.toolbarBookAdd);
        forgotPassword = findViewById(R.id.forgotPassword);

        toolbar.setTitle("");

        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Lütfen bekleyiniz...");




        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        GoogleSignInOptions Options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(this,Options);


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailText = email.getText().toString();
                passwordText = password.getText().toString();
                if (TextUtils.isEmpty(emailText)){
                    email.requestFocus();
                    email.setError("Email alanı gerekli");
                }else if (TextUtils.isEmpty(passwordText)){
                    password.requestFocus();
                    password.setError("Şifre alanı gerekli");
                }else  {
                    progressDialog.show();
                    auth.signInWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                progressDialog.cancel();
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                String uid = user.getUid();
                                Log.i("Name", String.valueOf(user.getDisplayName()));
                                Log.i("Email", String.valueOf(user.getEmail()));
                                Log.i("ImageURL", String.valueOf(user.getPhotoUrl()));
                                Intent intent = new Intent(SignInActivity.this, HomePageActivity.class);
                                intent.putExtra("name",String.valueOf(user.getDisplayName()));
                                intent.putExtra("email",String.valueOf(user.getEmail()));
                                intent.putExtra("photo",String.valueOf(user.getPhotoUrl()));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }else {
                                progressDialog.cancel();
                                Alert.showFailed(SignInActivity.this, "Email veya şifre hatalı");
                            }
                        }
                    });
                }

            }
        });


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
               Intent signInIntent =  signInClient.getSignInIntent();
               startActivityForResult(signInIntent,RC_SIGN_IN);


            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            progressDialog.cancel();
            Task<GoogleSignInAccount>task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
               if (account != null){
                    authWithGoogle(account);
                   Log.i("ACCOUNT","Kullanıcı adı boş değil");
               }

            }catch (ApiException e){
                progressDialog.cancel();
                e.printStackTrace();
                Log.i("ACCOUNT","Kullanıcı adı boş");

            }
        }else {
            progressDialog.cancel();
            Alert.showFailed(this, "Giriş Başarısız");
        }
    }


    public   void  authWithGoogle(final GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()){
                           Log.i("Başarıyla kayıt oldunuz","Successful");
                           Log.i("Name", account.getDisplayName());
                           Log.i("Email", account.getEmail());
                           Intent intent = new Intent(SignInActivity.this, HomePageActivity.class);
                           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                           startActivity(intent);
                           finish();
                       }else {
                           Snackbar.make(wrapper, "Bilgilerinizi tekrar girmeyi deneyin", Snackbar.LENGTH_SHORT).show();
                       }
                    }
                });

    }
}
