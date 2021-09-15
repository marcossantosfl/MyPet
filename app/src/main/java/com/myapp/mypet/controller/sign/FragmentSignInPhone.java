package com.myapp.mypet.controller.sign;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myapp.mypet.R;
import com.myapp.mypet.controller.volley.VolleyController;
import com.myapp.mypet.controller.volley.VolleyMultipartRequest;
import com.myapp.mypet.model.Account;
import com.myapp.mypet.model.Pet;

import net.rimoto.intlphoneinput.IntlPhoneInput;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class FragmentSignInPhone extends Fragment {

    LinearLayout save = null;

    IntlPhoneInput phoneNumber = null;

    ProgressBar progressBar = null;

    private static final String petNameString = "MyPet";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in_phone, container, false);

        save = v.findViewById(R.id.save);

        phoneNumber = v.findViewById(R.id.phoneNumber);

        progressBar = v.findViewById(R.id.progressBar);

        phoneNumber.setDefault();

        saveListener();

        return v;

    }

    void saveListener() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber.requestFocus();

                if (phoneNumber.isValid()) {
                    closeKeyboard();

                    save.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);

                    postRequest(phoneNumber.getText().toString(),((FragmentSignIn) getActivity()).getAccount(), ((FragmentSignIn) getActivity()).getPet());
                }
                else
                {
                    Toast toast=Toast. makeText(getActivity().getApplicationContext(),"Phone Number is invalid!",Toast. LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private void postRequest(String number, Account account, Pet pet) {

        try {
            String URL = "https://api-courier-produk.skipthedishes.com/v2/couriers/334460ba-0a65-4008-949d-756ceb055c49/shifts/scheduled?includeAvailable=true&timezone=Europe/London&hasCourierRefreshedOpenShifts=true";
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("number", number);
            jsonBody.put("age", pet.getAge());
            jsonBody.put("name", pet.getName());
            jsonBody.put("photo", pet.getPetPhoto());

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    try {
                        if (response.getInt("return") == 200) {
                            ((FragmentSignIn) getActivity()).getAccount().setNumber(number);
                            ((FragmentSignIn) getActivity()).nextFragment();
                            ((FragmentSignIn) getActivity()).updateProgress(85);

                        } else {
                            save.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity().getApplicationContext(), "Account exists!", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    save.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity().getApplicationContext(), "Account exists!", Toast.LENGTH_SHORT).show();

                    error.printStackTrace();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> headers = new HashMap<>();
                    headers.put("accept", "application/json");
                    return headers;
                }
            };

            VolleyController.getInstance().addToRequestQueue(jsonObjectRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
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

