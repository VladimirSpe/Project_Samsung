package com.example.project.ui.Profile;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.example.project.Registration.scaleImage;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.project.MainActivity;
import com.example.project.Ocn;
import com.example.project.R;
import com.example.project.Registration;
import com.example.project.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


class OnSwipeTouchListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;

    public OnSwipeTouchListener (Context ctx){
        gestureDetector = new GestureDetector(ctx, new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }
}


public class ProfileFragment extends Fragment {
    FrameLayout frameLayout, name_layout;
    CircleImageView profile_image;
    ImageButton profile_frame_close;
    AppCompatTextView profile_name, profile_wallet;
    Button profile_ex;
    ProgressBar image_b, name_b;
    static final int Pick_image = 1;
    User profile_user;
    boolean p = true;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_profile, container, false);

        frameLayout = view.findViewById(R.id.frameLayout);
        image_b = view.findViewById(R.id.image_progress_b);
        name_b = view.findViewById(R.id.name_progress_b);
        profile_frame_close = view.findViewById(R.id.fragment_profile_frame);
        profile_image = view.findViewById(R.id.profile_image);
        name_layout = view.findViewById(R.id.name_frame_layout);
        profile_name = view.findViewById(R.id.profile_fragment_name);
        profile_wallet = view.findViewById(R.id.user_Wallet);
        profile_ex = view.findViewById(R.id.profile_exit);
        new Async_Profile().execute();
        if (!p){
            profile_image.setVisibility(View.INVISIBLE);
            profile_wallet.setVisibility(View.VISIBLE);

        }
        profile_frame_close.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (p){
                    profile_image.animate().alpha(0).setDuration(500);
                    profile_image.setVisibility(View.INVISIBLE);
                    profile_wallet.setVisibility(View.VISIBLE);
                    profile_wallet.animate().alpha(1).setDuration(500);
                    p = false;
                }
                else{
                    profile_image.animate().alpha(1).setDuration(500);
                    profile_image.setVisibility(View.VISIBLE);
                    profile_wallet.setVisibility(View.INVISIBLE);
                    profile_wallet.animate().alpha(0).setDuration(500);
                    p = true;
                }
                profile_frame_close.animate().rotationBy(180).setDuration(100).start();

            }
        });
        profile_name.setOnTouchListener(new OnSwipeTouchListener(getActivity()){
            public void onSwipeLeft(){
                profile_name.setVisibility(View.INVISIBLE);
                name_layout.setVisibility(View.VISIBLE);
                profile_ex.setVisibility(View.VISIBLE);
            }
            public void onSwipeRight(){
                profile_name.setVisibility(View.VISIBLE);
                name_layout.setVisibility(View.INVISIBLE);
                profile_ex.setVisibility(View.INVISIBLE);
            }

        });
        profile_ex.setOnTouchListener(new OnSwipeTouchListener(getActivity()){
            public void onSwipeRight(){
                profile_name.setVisibility(View.VISIBLE);
                name_layout.setVisibility(View.INVISIBLE);
                profile_ex.setVisibility(View.INVISIBLE);
            }
            public void onSwipeLeft(){
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, Pick_image);
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case Pick_image:
                if (resultCode == RESULT_OK) {
                    try {

                        final Uri imageUri = imageReturnedIntent.getData();
                        Context applicationContext = Ocn.getContextOfApplication();
                        final InputStream imageStream = applicationContext.getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        Bitmap cut_image = scaleImage(selectedImage, 200, 200);
                        profile_image.setImageBitmap(cut_image);
                        User user = new User();
                        user.changePhoto(cut_image);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }
    private class Async_Profile extends AsyncTask<Void, Integer, Void> {
        boolean er;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            profile_image.setVisibility(View.INVISIBLE);
            profile_name.setVisibility(View.INVISIBLE);
            image_b.setVisibility(View.VISIBLE);
            name_b.setVisibility(View.VISIBLE);
        }
        protected Void doInBackground(Void... args) {
            final String[] user = {Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()};
            Log.d("id", user[0]);
            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://help-me-e2de7-default-rtdb.europe-west1.firebasedatabase.app");
            DatabaseReference myRef = mDatabase.getReference("users");
            myRef.child(user[0]).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    profile_name.setText(user.Name);
                    name_b.setVisibility(View.INVISIBLE);
                    profile_name.setVisibility(View.VISIBLE);
                    profile_image.setImageBitmap(StringToBitMap(user.Photo));
                    image_b.setVisibility(View.INVISIBLE);
                    profile_image.setVisibility(View.VISIBLE);
                    profile_wallet.setText(user.Wallet + "P");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            return null;
        }

        protected void onPostExecute(Void image) {
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
}