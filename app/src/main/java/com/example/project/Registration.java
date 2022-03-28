package com.example.project;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity {
    EditText login, password, re_password, name, number;
    Button All_reg_b;
    ProgressBar pr;
    TextView recover;
    public Bitmap user_photo;
    static final int Pick_image = 1;
    ImageButton photo;
    CheckBox checkBox1, checkBox2;
    LinearLayout linearLayout;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        login = findViewById(R.id.Email_login);
        password = findViewById(R.id.Password_pa);
        All_reg_b = findViewById(R.id.Registr_Button);
        checkBox1 = findViewById(R.id.Password_vis_reg);
        name = findViewById(R.id.user_name);
        number = findViewById(R.id.user_number);

        photo = findViewById(R.id.user_photo);
        checkBox2 = findViewById(R.id.Password_vis_reg1);
        recover = findViewById(R.id.Recover_pass);
        pr = findViewById(R.id.Progbar);
        linearLayout = findViewById(R.id.linearLayout);
        re_password = findViewById(R.id.Password_re);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, Pick_image);
            }
        });
        All_reg_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recover.setVisibility(TextView.INVISIBLE);
                if (isEmailValid(login.getText().toString())){
                    if (isPasswordValid(password.getText().toString())) {
                        if (re_password.getText().toString().equals(password.getText().toString())) {
                            new Async_Registr().execute();
                        }
                        else{
                            re_password.setError("Пароли не совпадают");
                        }
                    }
                    else{
                        password.setError("Пароль не соответствует требованиям:\n-Минимум 8 символов\n-Содержит символы верхнего и нижнего регистров" +
                                "\n-Содержит символ символ из набора '@#$%^&+=!'\n");
                    }
                }
                else{
                    login.setError("Неверный email");
                }

            }
        });
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!(b)){
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                else{
                    password.setTransformationMethod(null);
                }
            }
        });
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!(b)){
                    re_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                else{
                    re_password.setTransformationMethod(null);
                }
            }
        });
        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration.this, Forgot_pass_inp.class);
                intent.putExtra("email", login.getText().toString());
                startActivity(intent);
                finish();
            }
        });
    }
    private class Async_Registr extends AsyncTask<Void, Integer, Void> {
        boolean er;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            All_reg_b.setVisibility(Button.INVISIBLE);
            pr.setVisibility(ProgressBar.VISIBLE);

        }
        protected Void doInBackground(Void... args) {
            FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(login.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Reg_successful");
                                er = true;
                                //FirebaseUser user1 = mAuth.getCurrentUser();
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://help-me-e2de7-default-rtdb.europe-west1.firebasedatabase.app");
                                DatabaseReference myRef = mDatabase.getReference("users");
                                assert user != null;
                                myRef.child(user.getUid());
                                myRef.child(user.getUid()).child("Name").setValue(name.getText().toString());
                                myRef.child(user.getUid()).child("Number").setValue(number.getText().toString());
                                 myRef.child(user.getUid()).child("Wallet").setValue(1000);
                                Log.d("if", BitMapToString(user_photo));
                                myRef.child(user.getUid()).child("Photo").setValue(BitMapToString(user_photo));
                                pr.getIndeterminateDrawable();
                                Intent intent = new Intent(Registration.this, Ocn.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                er = false;
                                try {
                                    throw task.getException();
                                } catch(FirebaseAuthWeakPasswordException e) {
                                    password.setError("Ошибка в пароле");
                                    password.requestFocus();
                                } catch(FirebaseAuthInvalidCredentialsException e) {
                                    login.setError("Ошибка в email");
                                    login.requestFocus();
                                } catch(FirebaseAuthUserCollisionException e) {
                                    recover.setVisibility(TextView.VISIBLE);
                                    login.setError("Пользователь с таким email уже существует");
                                    login.requestFocus();
                                } catch(Exception e) {
                                    Log.e(TAG, e.getMessage());
                                }
                            }
                        }
                    });

            return null;
        }

        protected void onPostExecute(Void image) {
            if (er) {
                pr.getIndeterminateDrawable();
                Intent intent = new Intent(Registration.this, Ocn.class);
                startActivity(intent);
                finish();
            }
            else{
                pr.setVisibility(ProgressBar.INVISIBLE);
                All_reg_b.setVisibility(Button.VISIBLE);

            }
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
    private static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private static boolean isPasswordValid(String password){
        String expression = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case Pick_image:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        Bitmap cut_image = scaleImage(selectedImage, 200, 200);
                        photo.setImageBitmap(cut_image);
                        user_photo = cut_image;


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b = baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    public static  Bitmap  scaleImage(Bitmap bm, int newWidth, int newHeight){
        if (bm == null){
            return null;
        }
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,true);
        if (bm != null & !bm.isRecycled()){
            bm.recycle();//Уничтожить исходное изображение
            bm = null;
        }
        return newbm;
    }
}



