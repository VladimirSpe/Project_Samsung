package com.example.project;

import static android.content.ContentValues.TAG;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class Inp_Act extends AppCompatActivity {
    EditText Inp_log, Inp_Pass;
    CheckBox checkBox;
    TextView forgot, erro;
    Button Inp_b;
    ProgressBar Inp_p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inp);
        Inp_log = findViewById(R.id.Email_inp);
        Inp_Pass = findViewById(R.id.Password_inp);
        Inp_b = findViewById(R.id.Inp_Button);
        Inp_p = findViewById(R.id.Progbar_forgot);
        forgot = findViewById(R.id.forgot_pass);
        checkBox = findViewById(R.id.Password_vis);
        erro = findViewById(R.id.Text_error);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Inp_Act.this, Forgot_pass_inp.class);
                intent.putExtra("email", Inp_log.getText().toString());
                startActivity(intent);
            }
        });
        Inp_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Async_Registr().execute();
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!(b)){
                    Inp_Pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                else{
                    Inp_Pass.setTransformationMethod(null);
                }
            }
        });
    }
    private class Async_Registr extends AsyncTask<Void, String, Boolean> {
        boolean inp;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Inp_b.setVisibility(Button.INVISIBLE);
            Inp_p.setVisibility(ProgressBar.VISIBLE);
        }
        protected Boolean doInBackground(Void... args) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if (auth.getCurrentUser() != null) {
                Log.d("Inp", "Already signed in");
                inp = true;
            } else {
                auth.signInWithEmailAndPassword(Inp_log.getText().toString(), Inp_Pass.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Inp_Successful");
                                    inp = true;
                                }
                                else{
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthInvalidUserException e) {
                                        Inp_log.setError("Ошибка Email");
                                        Inp_log.requestFocus();
                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        Inp_Pass.setError("Неверный пароль");
                                        Inp_Pass.requestFocus();
                                    } catch (FirebaseNetworkException e) {
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    inp = false;
                                }
                            }
                        });
            }
            return inp;
        }

        protected void onPostExecute(Boolean inp) {
            if (inp) {
                Log.d(TAG, "inp_successful");
                Intent intent = new Intent(Inp_Act.this, Ocn.class);
                startActivity(intent);
                finish();
            }
            else{
                Inp_p.setVisibility(ProgressBar.INVISIBLE);
                Inp_b.setVisibility(Button.VISIBLE);
            }
        }
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}

