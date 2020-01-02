package com.example.tutoriasapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {
    EditText memailEt,mpasswordEt;
    TextView notaccount;
    Button mloginlogbtn;

    //FIREBASE
    private FirebaseAuth mAuth;

    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Actionbar and title
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Inicia Sesion");

        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //In the onCreate() method, initialize the FirebaseAuth instance.
        mAuth = FirebaseAuth.getInstance();

        //init
        memailEt= findViewById(R.id.emailEt);
        mpasswordEt=findViewById(R.id.passwordEt);
        mloginlogbtn=findViewById(R.id.loginlogBtn);
        notaccount=findViewById(R.id.ntcuenta_rgTV);
        mloginlogbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=memailEt.getText().toString();
                String passw=mpasswordEt.getText().toString().trim();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    //ivalid paaterns
                    memailEt.setError("Email Invalido");
                    memailEt.setFocusable(true);
                }else{
                    //invalid email pattern
                    loginUser(email,passw);
                }
            }
        });
        //no tienes cuentas?
        notaccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginActivity.this,RegisterActivity.class));
            }
        });
        //init progres dialog
        pd=new ProgressDialog(this);
        pd.setMessage("Loggin In...");
    }

    private void loginUser(String email, String password){
        pd.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //dissmis progress dilog
                            pd.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(loginActivity.this,profileActivity.class));
                            finish();
                        } else {
                            //dissmis progress dilog
                            pd.dismiss();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(loginActivity.this, "Contraseña o Email invalido",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }}).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //dissmis progress dilog
                pd.dismiss();
                Toast.makeText(loginActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return  super.onSupportNavigateUp();
    }
}
