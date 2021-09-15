package com.myapp.mypet.controller.sign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.myapp.mypet.R;

public class FragmentSignInName extends Fragment {

    LinearLayout save = null;

    EditText petName = null;

    ProgressBar progressBar = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in_name, container, false);

        save = v.findViewById(R.id.save);

        petName = v.findViewById(R.id.petName);

        progressBar = v.findViewById(R.id.progressBar);

        saveListener();

        return v;

    }

    void saveListener() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                petName.requestFocus();

                if ((petName.length() < 2)) {
                    petName.setError("Must have at least 3 characters.");
                } else if ((petName.length() > 14)) {
                    petName.setError("Max characters is 15.");
                } else {

                    closeKeyboard();

                    save.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            ((FragmentSignIn) getActivity()).getPet().setName(petName.getText().toString());
                            ((FragmentSignIn) getActivity()).nextFragment();
                            ((FragmentSignIn) getActivity()).updateProgress(25);
                        }
                    }, 500);
                }
            }
        });
    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();

        if (view != null) {

            InputMethodManager manager
                    = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }
    }
}
