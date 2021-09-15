package com.myapp.mypet.controller.apresentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.myapp.mypet.R;
import com.myapp.mypet.controller.sign.Sign;

public class FragmentC extends Fragment {

    LinearLayout buttonStart = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apresentation_c, container, false);

        buttonStart = view.findViewById(R.id.buttonStart);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Sign.class);
                startActivity(intent);
            }
        });


        return view;
    }
}
