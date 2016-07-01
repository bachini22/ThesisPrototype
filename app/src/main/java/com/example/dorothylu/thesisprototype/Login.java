package com.example.dorothylu.thesisprototype;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class Login extends AppCompatActivity {

    private String email;
    private String pass;
    private EditText emailField;
    private EditText passField;
    Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_login);
        ref = new Firebase("https://thesis-cb2dc.firebaseio.com/");
        Button login = (Button)findViewById(R.id.signin);
        login.setOnClickListener( new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                emailField = (EditText)findViewById(R.id.emailField);
                passField = (EditText)findViewById(R.id.passwordField);
                ref.authWithPassword(emailField.getText().toString(), passField.getText().toString(), authResultHandler);
            }
        });

    }


    //handles authentication
    Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
        @Override
        public void onAuthenticated(AuthData authData) {
            // Authenticated successfully with payload authData
            //go to newsfeed
            TextView message = (TextView)findViewById(R.id.message);
            message.setText("Success Farting");

        }
        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            // Authenticated failed with error firebaseError
            //invalid details
            TextView message = (TextView)findViewById(R.id.message);
            message.setText("Wrong details");
        }
    };



}
