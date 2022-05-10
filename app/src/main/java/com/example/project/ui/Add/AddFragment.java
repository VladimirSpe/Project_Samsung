package com.example.project.ui.Add;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.project.Adds;
import com.example.project.ImageAdapter;
import com.example.project.Inp_Act;
import com.example.project.Ocn;
import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class AddFragment extends Fragment {
    private int c = 0;
    boolean p;
    private final int Pick_image = 1;
    GridView gridview1;
    Bitmap[] a = new Bitmap[5];
    Button inp;
    String id = "";
    ProgressBar inp_prog;
    ImageButton back_to_adds;
    EditText name, preview, cost, number;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        Bundle bundle = getArguments();
        Log.d("nn", String.valueOf(bundle));
        ArrayList<String> photo = new ArrayList<>();
        gridview1 = (GridView) view.findViewById(R.id.gridview1);
        inp = (Button) view.findViewById(R.id.add_Button);
        name = (EditText) view.findViewById(R.id.add_name);

        preview = (EditText) view.findViewById(R.id.add_preview);

        cost = (EditText) view.findViewById(R.id.add_cost);

        number = (EditText) view.findViewById(R.id.add_number);
        back_to_adds = view.findViewById(R.id.back_button);
        inp_prog = (ProgressBar) view.findViewById(R.id.Progbar_add);
        gridview1.setAdapter(new ImageAdapter(getActivity(), a, R.layout.add_menu1));
        gridview1.setOnItemClickListener(gridviewOnItemClickListener);
        gridview1.setNumColumns(3);
        if (bundle != null) {
            inp.setText("Редактировать");
            photo = bundle.getStringArrayList("Photo");
            name.setText(bundle.getString("Name"));
            preview.setText(bundle.getString("Preview"));
            cost.setText(bundle.getString("Cost"));
            id = bundle.getString("ID");
            for (int i = 0; i < photo.size(); i++){
                a[i] = StringToBitMap(photo.get(i));
            }
        }
        if (a[0] == null) {
            a[0] = getBitmapFromVectorDrawable(getActivity(), R.drawable.add_photo);
        }
        inp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p = true;
                inp.setVisibility(View.INVISIBLE);
                inp_prog.setVisibility(View.VISIBLE);
                if (name.length() <= 1) {
                    name.setError("Название должно состоять минимум из 2 знаков");
                    name.requestFocus();
                    p = false;
                }
                if (preview.length() <= 1) {
                    preview.setError("Описание должно состоять минимум из 2 знаков");
                    preview.requestFocus();
                    p = false;
                }
                if (cost.length() < 1) {
                    cost.setError("Цена должна состоять минимум из 1 цифры");
                    cost.requestFocus();
                    p = false;
                }
                if (number.length() < 4) {
                    number.setError("Номер должен состоять минимум из 4 цифр");
                    number.requestFocus();
                    p = false;
                }
                if (p) {
                    ArrayList<String> a1 = new ArrayList<>();
                    for (Bitmap i: a){
                        if (i != null && i != BitmapFactory.decodeResource(getResources(), R.drawable.add_photo)){
                            a1.add(BitMapToString(i));
                        }
                    }
                    Log.d("gg", String.valueOf(a1));
                    Adds adds = new Adds(a1, name.getText().toString(), preview.getText().toString(), Integer.parseInt(cost.getText().toString()), number.getText().toString(), id);
                    if (!(adds.Add_Adds())) {
                        p = false;
                    }
                }
                if (p) {
                    name.setText("");
                    preview.setText("");
                    cost.setText("");
                    number.setText("");
                    Arrays.fill(a, null);
                    gridview1.setAdapter(new ImageAdapter(getActivity(), a, R.layout.add_menu1));
                    inp.setVisibility(View.VISIBLE);
                    inp_prog.setVisibility(View.INVISIBLE);
                    Fragment frag2 = new Start_addFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.nav_host_fragment_activity_main, frag2);
                    ft.addToBackStack(frag2.toString());
                    ft.commit();
                }
                else {
                    Toast.makeText(getActivity(), "Ошибка", Toast.LENGTH_LONG).show();
                    inp.setVisibility(View.VISIBLE);
                    inp_prog.setVisibility(View.INVISIBLE);
                }
            }
        });
        back_to_adds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag2 = new Start_addFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment_activity_main, frag2);
                ft.addToBackStack(frag2.toString());
                ft.commit();
            }
        });
        return view;
    }


    private final GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position,
                                long id) {
            Log.d("Click", String.valueOf(position));
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, Pick_image);
            c = (int) id;

        }
    };

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
                        a[c] = cut_image;
                        if (c != 4 && a[c + 1] == null) {
                            a[c + 1] = getBitmapFromVectorDrawable(getActivity(), R.drawable.add_photo);
                        }
                        gridview1.setAdapter(new ImageAdapter(getActivity(), a, R.layout.add_menu1));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
    private class Async_Add extends AsyncTask<Void, String, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected Boolean doInBackground(Void... args) {
            return p;
        }

        protected void onPostExecute(Boolean p) {
            if (p) {
                name.setText("");
                preview.setText("");
                cost.setText("");
                number.setText("");
                Arrays.fill(a, null);
                gridview1.setAdapter(new ImageAdapter(getActivity(), a, R.layout.add_menu1));
                inp.setVisibility(View.VISIBLE);
                inp_prog.setVisibility(View.INVISIBLE);
                Fragment frag2 = new Start_addFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment_activity_main, frag2);
                ft.addToBackStack(frag2.toString());
                ft.commit();
            }
            else {
                Toast.makeText(getActivity(), "Ошибка", Toast.LENGTH_LONG).show();
            }
        }
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
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
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}




