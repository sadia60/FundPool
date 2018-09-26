package com.example.awaiswaheed.committe104;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import android.content.SharedPreferences;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "LoginPrefs";
    private static final String TAG = "LoginActivity";
    private static final String URL_FOR_LOGIN = "https://majid-compose1.mybluemix.net/user/login";
    ProgressDialog progressDialog;
    private EditText loginInputPhone, loginInputPassword;
    private Button btnlogin;
    private Button btnLinkSignup;
    public static final  String value = "logged";
    String error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginInputPhone = (EditText) findViewById(R.id.login_input_phone);
        loginInputPassword = (EditText) findViewById(R.id.login_input_password);
        btnlogin = (Button) findViewById(R.id.btn_login);
        btnLinkSignup = (Button) findViewById(R.id.btn_link_signup);
        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(loginInputPhone.getText().toString(),
                        loginInputPassword.getText().toString());
            }
        });

        btnLinkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);

            }
        });
    }

    private void loginUser( final String phonenumber, final String password) {
        //Tag used to cancel the request
        String cancel_req_tag = "login";
        progressDialog.setMessage("Logging you in...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    error = jObj.getString("message");
                    Log.d(TAG, "Register Response: " + error);
                    if (error.equals("Successful")) {
                       // Toast.makeText(getApplicationContext(), "Successfull Login", Toast.LENGTH_SHORT).show();
                        // Launch User activity
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(value, "logged");
                        editor.putString("id", jObj.getString("id"));
                        editor.apply();
                        Intent intent = new Intent(
                                LoginActivity.this,
                                UserActivity.class);
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
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
//                params.put("phone", phonenumber);
//                params.put("password", password);

                params.put("phone", "030012345");
                params.put("password", "test1");
//                params.put("phone", "+93455508768");
//                params.put("password", "seecs1231111");

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