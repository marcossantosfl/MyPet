package com.myapp.mypet.controller.sign;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.myapp.mypet.R;

public class FragmentSignInAge extends Fragment {

    LinearLayout save = null;

    EditText petAge = null;

    ProgressBar progressBar = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in_age, container, false);

        save = v.findViewById(R.id.save);

        petAge = v.findViewById(R.id.petAge);

        progressBar = v.findViewById(R.id.progressBar);

        saveListener();

        return v;

    }

    void saveListener() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                petAge.requestFocus();

                if(petAge.getText().toString().equals(""))
                {
                    petAge.setError("You must set an age.");
                    return;
                }

                int value = Integer.parseInt(petAge.getText().toString());

                if (value < 0) {
                    petAge.setError("Age can not be less than 0.");
                } else if (value > 100) {
                    petAge.setError("Age can not be more than 100.");
                } else {

                    closeKeyboard();

                    save.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            ((FragmentSignIn) getActivity()).getPet().setAge(Integer.parseInt(petAge.getText().toString()));
                            ((FragmentSignIn) getActivity()).nextFragment();
                            ((FragmentSignIn) getActivity()).updateProgress(65);
                        }
                    }, 1500);
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

