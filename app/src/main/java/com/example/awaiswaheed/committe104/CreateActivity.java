package com.example.awaiswaheed.committe104;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class CreateActivity extends AppCompatActivity {

    private static final String TAG = "CreateActivity";
    private static final String URL_FOR_REGISTRATION = "...Insert URL Here...";
    ProgressDialog progressDialog;

    private EditText CreateInputName, CreateInputMembersCount, CreateInputAmount, CreateInputStartDate, CreateInputTotalMonths;
    private Button btnCreate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        CreateInputName = (EditText) findViewById(R.id.create_input_name);
        CreateInputMembersCount = (EditText) findViewById(R.id.create_input_membercount);
        CreateInputAmount = (EditText) findViewById(R.id.create_input_amount);
        CreateInputStartDate = (EditText) findViewById(R.id.create_input_startDate);
        CreateInputTotalMonths = (EditText) findViewById(R.id.create_input_months);

        btnCreate = (Button) findViewById(R.id.btn_create_committe);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
    }

    private void submitForm() {
        registerUser(CreateInputName.getText().toString(),
                CreateInputMembersCount.getText().toString(),
                CreateInputAmount.getText().toString(),
                CreateInputStartDate.getText().toString(), CreateInputTotalMonths.getText().toString());
    }

    private void registerUser(final String CommitteName,  final String MembersCount, final String Amount,
                              final String StartDate, final String Months) {
        // Tag used to cancel the request
        String cancel_req_tag = "create";
        progressDialog.setMessage("Creating ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_REGISTRATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Create Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("message");
                    if (error.equals("Successfull")) {
                        Toast.makeText(getApplicationContext(), "You successfully created new Committee!", Toast.LENGTH_SHORT).show();
                        // Launch User activity
                        Intent intent = new Intent(
                                CreateActivity.this,
                                UserActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", CommitteName);
                params.put("count", MembersCount);
                params.put("amount", Amount);
                params.put("startdate", StartDate);
                params.put("months", Months);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
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
