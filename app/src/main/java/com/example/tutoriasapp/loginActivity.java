package com.example.tutoriasapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    TextView notaccount, mrecoverPAsstv;
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
        actionBar.setTitle("Iniciar Sesion");

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
        mrecoverPAsstv= findViewById(R.id.rpasswLog);
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
                finish();
            }
        });

        //Recuperar password
        mrecoverPAsstv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverpassworddialog();

            }
        });


        //init progres dialog
        pd=new ProgressDialog(this);

    }
    //recuperar password
    private void showRecoverpassworddialog(){
        //alert dialog
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Recuperar Password");

        //set layout linear layout
        LinearLayout linearLayout=new LinearLayout(this);
        final EditText emailEt= new EditText(this);
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        /*sets the min  width of a EDitView to fit a text of n M letters regardless of the actual
        text extension and text size*/
        emailEt.setMinEms(16);



        linearLayout.addView(emailEt);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);
        //boton recover
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = emailEt.getText().toString().trim();
                beginRecovery(email);
            }
        });
        //boton cancel
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dismiss dialog
                dialog.dismiss();
            }
        });
        //SHOW DIALOG
        builder.create().show();

    }
    private  void beginRecovery(String email){
        //show progress dialog
        pd.setMessage("Sending email...");
        pd.show();
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    pd.dismiss();
                    Toast.makeText(loginActivity.this,"Enviado al email",Toast.LENGTH_SHORT );
                }else{
                    Toast.makeText(loginActivity.this,"Failed...",Toast.LENGTH_SHORT );
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();

                //get and show proper error message
                Toast.makeText(loginActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loginUser(String email, String password){
        pd.setMessage("Loggin In...");
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
