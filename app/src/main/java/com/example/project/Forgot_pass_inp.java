package com.example.project;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_pass_inp extends AppCompatActivity {
    EditText email;
    Button button;
    ProgressBar progressBar;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_inp);
        email = findViewById(R.id.Forgot_email);
        email.setText((String) getIntent().getSerializableExtra("email"));
        button = findViewById(R.id.Forgot_button);
        progressBar = findViewById(R.id.Progbar_forgot);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Async_Forgot().execute();
            }
        });
    }
    private class Async_Forgot extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            button.setVisibility(Button.INVISIBLE);
            progressBar.setVisibility(ProgressBar.VISIBLE);

        }
        protected Void doInBackground(Void... args) {
            auth.sendPasswordResetEmail(email.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");
                            }
                            else {

                            }
                        }
                    });
            return null;
        }

        protected void onPostExecute(Void image) {
            Intent intent = new Intent(Forgot_pass_inp.this, Inp_Act.class);
            startActivity(intent);
            finish();
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
}