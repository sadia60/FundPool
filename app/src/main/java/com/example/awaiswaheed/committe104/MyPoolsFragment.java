package com.example.awaiswaheed.committe104;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;


public class MyPoolsFragment extends Fragment implements ItemClickListener{
    public static final String PREFS_NAME = "LoginPrefs";
    public RecyclerView mypoolsrecyclerView;
    public CommitteAdapter newAdapter;

    String phone;
    private List<Committe> list1 = new ArrayList<>();
    private List<Committe> list2 = new ArrayList<>();
    Activity context = getActivity();
    private static final String URL_FOR_SHOW = "https://majid-compose1.mybluemix.net/committee/print_committee";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mypools, container, false);
        SharedPreferences settings = this.getActivity().getSharedPreferences(PREFS_NAME, 0);
        phone = settings.getString("id", null);
        mypoolsrecyclerView = (RecyclerView) view.findViewById(R.id.mypoolslist);

        newAdapter = new CommitteAdapter(list1, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mypoolsrecyclerView.setLayoutManager(mLayoutManager);
        mypoolsrecyclerView.setItemAnimator(new DefaultItemAnimator());
        mypoolsrecyclerView.setAdapter(newAdapter);
        submitForm();
        return  view;
    }

    private void submitForm() {
        GetCommittees();
    }

    private void GetCommittees() {
        //Tag used to cancel the request
        String cancel_req_tag = "search";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_SHOW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("message");
                    Log.d(TAG, "Register Response: " + error);
                    if (error.equals("successful")) {
                        Log.d(TAG, "Register Response: " + error);
                        JSONArray result = jObj.getJSONArray("result");
                        list2 = CommitteManager.getInstance(context).getCommittee(result);
                        list1.clear();
                        for(int i = 0; i< list2.size(); i++){
                            Log.d(TAG, "Name: " + list2.get(i).name);
                            Committe com = new Committe(list2.get(i).name , list2.get(i).owner_name , list2.get(i).id);
                            list1.add(com);

                        }
                        newAdapter.notifyDataSetChanged();
                        newAdapter.setClickListener(MyPoolsFragment.this);

                    } else {

                        Toast.makeText(context,
                                error, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Search Error: " + error.getMessage());
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();

                params.put("id", phone);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(context).addToRequestQueue(strReq,cancel_req_tag);
    }


    @Override
    public void onClick(View view, int position) {
        final Committe committe = list1.get(position);
        Intent i = new Intent(this.getActivity(), SearchResultDetailsActivity.class);
        i.putExtra("id", committe.id);
        startActivity(i);
    }
}
