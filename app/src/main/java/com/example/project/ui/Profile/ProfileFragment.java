package com.example.project.ui.Profile;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.project.Ocn;
import com.example.project.R;
import com.google.android.material.textview.MaterialTextView;

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
    AppCompatTextView profile_name;
    Button profile_ex;
    boolean p = true;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_profile, container, false);
        frameLayout = view.findViewById(R.id.frameLayout);
        profile_frame_close = view.findViewById(R.id.fragment_profile_frame);
        profile_image = view.findViewById(R.id.profile_image);
        name_layout = view.findViewById(R.id.name_frame_layout);
        profile_name = view.findViewById(R.id.profile_fragment_name);
        profile_ex = view.findViewById(R.id.profile_exit);
        if (!p){
            profile_image.setVisibility(View.INVISIBLE);
        }
        profile_frame_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (p){
                    profile_image.animate().alpha(0).setDuration(500);
                    p = false;
                }
                else{
                    profile_image.animate().alpha(1).setDuration(500);
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
        });
        return view;
    }
}