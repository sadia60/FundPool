package com.example.awaiswaheed.committe104;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VerificationActivity extends AppCompatActivity {
    private static final String TAG = "VerificationActivity";
    private static final String URL_FOR_VERIFY = "https://majid-compose1.mybluemix.net/user/verify";
    ProgressDialog progressDialog;
    private EditText VerificationCode;
    private Button btnVerify;
    String error;
    Integer MAX_LENGTH = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        VerificationCode = (EditText) findViewById(R.id.login_input_password);
        VerificationCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_LENGTH)});
        btnVerify = (Button) findViewById(R.id.btn_login);
        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                VerifyUser(VerificationCode.getText().toString());
            }
        });
    }

    private void VerifyUser( final String code) {
        // Tag used to cancel the request
        String cancel_req_tag = "verify";
        progressDialog.setMessage("Verifying...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_VERIFY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    error = jObj.getString("message");

                    if (error.equals("Successful")) {
                        Toast.makeText(getApplicationContext(), "Registered Succesfully.", Toast.LENGTH_SHORT).show();
                        // Launch Login activity
                        Intent intent = new Intent(
                                VerificationActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        Toast.makeText(getApplicationContext(),
                                error, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Verification Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("code", code);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq,cancel_req_tag);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
