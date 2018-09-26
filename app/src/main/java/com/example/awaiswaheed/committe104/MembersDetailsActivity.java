package com.example.awaiswaheed.committe104;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class MembersDetailsActivity extends AppCompatActivity {
    ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
    TextView MembersDetailsName ,MembersDetailsID, MembersDetailsLevel;
    private static final String URL_FOR_MD = "...Insert URL Here...";
    Integer id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_details);
        MembersDetailsName =(TextView)findViewById(R.id.MD_name);
        MembersDetailsID =(TextView)findViewById(R.id.MD_id);
        MembersDetailsLevel =(TextView)findViewById(R.id.MD_level);
        submitForm();
    }
    private void submitForm() {
        MemberDetails();
    }

    private void MemberDetails() {
        // Tag used to cancel the request
        String cancel_req_tag = "MD";
        progressDialog.setMessage("Getting Details ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_MD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("message");
                    JSONArray result = jObj.getJSONArray("result");
                    JSONObject details = result.getJSONObject(1);
                    hideDialog();
                    if (error.equals("Successful")) {
                        MembersDetailsName.setText(details.getString("name"));
                        MembersDetailsID.setText(details.getString("id"));
                        MembersDetailsLevel.setText(details.getString("level"));
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
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", String.valueOf(id));
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
