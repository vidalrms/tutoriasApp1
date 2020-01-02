package com.example.tutoriasapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class profileActivity extends AppCompatActivity {
    //firebase auth
    FirebaseAuth firebaseAuth;

    //views
    TextView mprofileTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Action bar
        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("Perfil");

        //init
        firebaseAuth =FirebaseAuth.getInstance();

        mprofileTv=findViewById(R.id.profileTv);

    }
    private void checUserStatus(){
        //
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null){
            //user is signed set email of logged
            mprofileTv.setText(user.getEmail());

        }
        else{
            //user no signed
            startActivity(new Intent(profileActivity.this,MainActivity.class));
        }
    }
    @Override
    protected void onStart(){
        //checck on start of app
        checUserStatus();
        super.onStart();
    }

    /*inflate option menu*/
    @Override
    public boolean onCreateOptionsMenu( Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected( MenuItem item){
        int id=item.getItemId();
        if(id==R.id.action_logout){
            firebaseAuth.signOut();
            checUserStatus();
        }

        return super.onOptionsItemSelected(item);
    }

}
