package com.myapp.mypet.controller.sign;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myapp.mypet.R;
import com.myapp.mypet.controller.volley.VolleyController;
import com.myapp.mypet.controller.volley.VolleyMultipartRequest;
import com.myapp.mypet.model.Account;
import com.myapp.mypet.model.Pet;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FragmentSignInValidate extends Fragment {

    LinearLayout validate = null;

    EditText validateA = null;
    EditText validateB = null;
    EditText validateC = null;
    EditText validateD = null;

    ProgressBar progressBar = null;

    private static final String petNameString = "MyPet";

    SmsVerifyCatcher smsVerifyCatcher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in_validate, container, false);

        validate = v.findViewById(R.id.validate);

        validateA = v.findViewById(R.id.validateA);
        validateB = v.findViewById(R.id.validateB);
        validateC = v.findViewById(R.id.validateC);
        validateD = v.findViewById(R.id.validateD);

        progressBar = v.findViewById(R.id.progressBar);

        smsVerifyCatcher = new SmsVerifyCatcher(getActivity(), new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);
                System.out.println(code);
                setCode(code);
            }
        });
        smsVerifyCatcher.onStart();

        saveListener();

        focusListener(validateA, validateB);
        focusListener(validateB, validateC);
        focusListener(validateC, validateD);

        return v;

    }

    @Override
    public void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{4}\\b");     // for 6 digits number
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }

    void setCode(String code) {
        if (code != null && code.length() == 4) {
            validateA.setText(String.valueOf(code.charAt(0)));
            validateB.setText(String.valueOf(code.charAt(1)));
            validateC.setText(String.valueOf(code.charAt(2)));
            validateD.setText(String.valueOf(code.charAt(3)));
        }
    }

    void focusListener(EditText focus1, EditText focus2) {
        StringBuilder sb = new StringBuilder();

        focus1.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (sb.length() == 0 & focus1.length() == 1) {
                    sb.append(s);
                    focus1.clearFocus();
                    focus2.requestFocus();
                    focus2.setCursorVisible(true);

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if (sb.length() == 1) {

                    sb.deleteCharAt(0);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (sb.length() == 0) {

                    focus1.requestFocus();
                }

            }
        });
    }

    void saveListener() {
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateA.requestFocus();
                validateB.requestFocus();
                validateC.requestFocus();
                validateD.requestFocus();

                if (validateA.getText().toString().equals("")) {
                    validateA.setError("Fill the code.");
                    return;
                } else if (validateB.getText().toString().equals("")) {
                    validateB.setError("Fill the code.");
                    return;
                } else if (validateC.getText().toString().equals("")) {
                    validateC.setError("Fill the code.");
                    return;
                } else if (validateD.getText().toString().equals("")) {
                    validateD.setError("Fill the code.");
                    return;
                }

                closeKeyboard();

                validate.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                postRequest(((FragmentSignIn) getActivity()).getAccount());

            }
        });
    }

    private void postRequest(Account account) {

        try {
            String URL = "https://marcossan.dev/mypet/pages/signup_validate.php";
            JSONObject jsonBody = new JSONObject();

            String code = validateA.getText().toString()+validateB.getText().toString()+validateC.getText().toString()+validateD.getText().toString();

            System.out.println("my code is: "+code);

            jsonBody.put("number", account.getNumber());
            jsonBody.put("code", code);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    Toast.makeText(getActivity().getApplicationContext(), "Invalid code! 3", Toast.LENGTH_SHORT).show();

                    try {
                        if (response.getInt("return") == 200) {
                            //((FragmentSignIn) getActivity()).nextFragment();
                            ((FragmentSignIn) getActivity()).updateProgress(100);

                        } else {
                            validate.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity().getApplicationContext(), "Invalid code! 2", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    validate.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity().getApplicationContext(), "Invalid code! 1", Toast.LENGTH_SHORT).show();

                    error.printStackTrace();
                }
            }) {
                //@Override
                //public Map<String, String> getHeaders() throws AuthFailureError {
                //   // final Map<String, String> headers = new HashMap<>();
                //    //headers.put("Authorization", "Basic " + "c2FnYXJAa2FydHBheS5jb206cnMwM2UxQUp5RnQzNkQ5NDBxbjNmUDgzNVE3STAyNzI=");//put your token here
                //    return headers;
                //}
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
