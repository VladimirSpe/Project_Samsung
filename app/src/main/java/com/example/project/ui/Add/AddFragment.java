package com.example.project.ui.Add;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

public class AddFragment extends Fragment {
    private int c = 0;
    private final int Pick_image = 1;
    GridView gridview1;
    Bitmap[] a = new Bitmap[5];
    Button inp;
    ProgressBar inp_prog;
    EditText name, preview, cost, number;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        gridview1 = (GridView) view.findViewById(R.id.gridview1);
        inp = (Button) view.findViewById(R.id.add_Button);
        name = (EditText) view.findViewById(R.id.add_name);
        preview = (EditText) view.findViewById(R.id.add_preview);
        cost = (EditText) view.findViewById(R.id.add_cost);
        number = (EditText) view.findViewById(R.id.add_number);
        inp_prog = (ProgressBar) view.findViewById(R.id.Progbar_add);
        gridview1.setAdapter(new ImageAdapter(getActivity(), a, R.layout.add_menu1));
        gridview1.setOnItemClickListener(gridviewOnItemClickListener);
        gridview1.setNumColumns(3);
        Log.d("Click", Arrays.toString(a));
        a[0] = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        inp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddFragment.Async_Registr().execute();
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
                        a[c] = selectedImage;
                        gridview1.setAdapter(new ImageAdapter(getActivity(), a, R.layout.add_menu1));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
    private class Async_Registr extends AsyncTask<Void, String, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inp.setVisibility(View.INVISIBLE);
            inp_prog.setVisibility(View.VISIBLE);
        }
        protected Boolean doInBackground(Void... args) {

            boolean p = true;

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
                cost.setError("Название должно состоять минимум из 1 цифры");
                cost.requestFocus();
                p = false;
            }
            if (number.length() < 4) {
                number.setError("Название должно состоять минимум из 4 цифр");
                number.requestFocus();
                p = false;
            }
            if (p) {
                Adds adds = new Adds(a, name.getText().toString(), preview.getText().toString(), Integer.parseInt(cost.getText().toString()), number.getText().toString());
                if (!(adds.Add_Adds())) {
                    p = false;

                }
            }
            return p;
        }

        protected void onPostExecute(Boolean p) {
            if (p) {
                Toast.makeText(getActivity(), "Объявление добавлено", Toast.LENGTH_LONG).show();
                name.setText("");
                preview.setText("");
                cost.setText("");
                number.setText("");
                Arrays.fill(a, null);
                gridview1.setAdapter(new ImageAdapter(getActivity(), a, R.layout.add_menu1));
                inp.setVisibility(View.VISIBLE);
                inp_prog.setVisibility(View.INVISIBLE);
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
}




