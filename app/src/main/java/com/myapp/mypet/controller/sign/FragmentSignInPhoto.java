package com.myapp.mypet.controller.sign;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import com.myapp.mypet.R;
import com.myapp.mypet.customize.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static androidx.core.content.ContextCompat.getSystemService;

public class FragmentSignInPhoto extends Fragment {

    LinearLayout save = null;

    LinearLayout select = null;

    com.myapp.mypet.customize.RoundedImageView petPhoto = null;

    ProgressBar progressBar = null;

    private static int SELECT_PICTURE = 1;

    private String encodedPhoto = "photo";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in_photo, container, false);

        save = v.findViewById(R.id.save);

        petPhoto = v.findViewById(R.id.petPhoto);

        select = v.findViewById(R.id.select);

        petPhoto.setImageResource(R.drawable.dog);

        progressBar = v.findViewById(R.id.progressBar);

        ((FragmentSignIn) getActivity()).getPet().setPetPhoto(encodedPhoto);

        selectListener();
        saveListener();

        return v;

    }

    private void saveListener() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                save.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                   @Override public void run() {
                        ((FragmentSignIn) getActivity()).nextFragment();
                        ((FragmentSignIn) getActivity()).updateProgress(40);
                    }
                }, 500);
            }
        });
    }

    private void selectListener() {
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
    }

    void imageChooser() {

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    2000);
        } else {

            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_PICK);

            ActivityResultLauncher<Intent> launcherPhoto = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {

                            if (result.getResultCode() == Activity.RESULT_OK && result.getData().getData() != null) {
                                Uri uri = result.getData().getData();


                                Bitmap bitmap = null;
                                try {
                                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                                byte[] byteArray = outputStream.toByteArray();

                                String encodedPhoto = Base64.encodeToString(byteArray, Base64.DEFAULT);

                                ((FragmentSignIn) getActivity()).getPet().setPetPhoto(encodedPhoto);

                                petPhoto.setImageURI(uri);
                                petPhoto.setVisibility(View.VISIBLE);
                                select.setVisibility(View.GONE);
                            }

                        }
                    });

            Intent chooserIntent = Intent.createChooser(i, "Select Picture");
            launcherPhoto.launch(chooserIntent);

        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}

