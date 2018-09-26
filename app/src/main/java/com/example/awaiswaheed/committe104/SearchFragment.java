package com.example.awaiswaheed.committe104;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class SearchFragment extends Fragment implements ItemClickListener {
    public RecyclerView SearchrecyclerView;
    public CommitteAdapter mAdapter;
    private List<Committe> mCommitteList = new ArrayList<>();
    private List<Committe> CommitteList = new ArrayList<>();
    Activity context;
    private static final String URL_FOR_SEARCH = "https://majid-compose1.mybluemix.net/committee/search_committee";
//    private static final String URL_FOR_SEARCH = "https://majid-compose1.mybluemix.net/committee/search_committee";
    ProgressDialog progressDialog;

    private EditText SearchInputCID;
    private Button btn_search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=getActivity();
        super.onCreate(savedInstanceState);

        View SearchView = inflater.inflate(R.layout.search, container, false);
        // Progress dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        SearchInputCID = (EditText) SearchView.findViewById(R.id.search_input_committee_name);
        btn_search     = (Button) SearchView.findViewById(R.id.btn_search);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        }

        );

        SearchrecyclerView = (RecyclerView) SearchView.findViewById(R.id.searchlist);

        mAdapter = new CommitteAdapter(mCommitteList , getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        SearchrecyclerView.setLayoutManager(mLayoutManager);
        SearchrecyclerView.setItemAnimator(new DefaultItemAnimator());
        SearchrecyclerView.setAdapter(mAdapter);

        return SearchView;
    }


    private void submitForm() {
        SearchCommittee();
    }

    private void SearchCommittee() {
        //Tag used to cancel the request
        String cancel_req_tag = "search";
        progressDialog.setMessage("Searching...");
        final String CommitteName = SearchInputCID.getText().toString();
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_SEARCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.d(TAG, "Register Response: " + response.toString());

                    String error = jObj.getString("message");
                    Log.d(TAG, "Register Response: " + error);
                    if (error.equals("successful")) {
                        Log.d(TAG, "Register Response: " + error);
                        JSONArray result = jObj.getJSONArray("result");

                        CommitteList = CommitteManager.getInstance(context).getCommittee(result);
                        mCommitteList.clear();

                        for(int i=0; i< CommitteList.size(); i++){
                            Log.d(TAG, "Name: " + CommitteList.get(i).name);
                            Committe com = new Committe(CommitteList.get(i).name , CommitteList.get(i).owner_name , CommitteList.get(i).id);
                            mCommitteList.add(com);
                        }




                        mAdapter.notifyDataSetChanged();
                        mAdapter.setClickListener(SearchFragment.this);

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
                hideDialog();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", CommitteName);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(context).addToRequestQueue(strReq,cancel_req_tag);
    }


    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onClick(View view, int position) {
        final Committe committe = mCommitteList.get(position);
        Intent i = new Intent(this.getActivity(), SearchResultDetailsActivity.class);
        i.putExtra("id", committe.id);
        startActivity(i);
    }

}



/*package com.example.awaiswaheed.committe104;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class SearchFragment extends Fragment implements ItemClickListener {
    public RecyclerView recyclerView;
    public CommitteAdapter mAdapter;
//    private List<Committe> mCommitteList = new ArrayList<>();
    private List<Committe> CommitteList = new ArrayList<>();


    Activity context;
    private static final String URL_FOR_SEARCH = "https://majid-compose1.mybluemix.net/committee/search_committee";
    ProgressDialog progressDialog;
    private EditText SearchInputCID;
    private Button btn_search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=getActivity();
        super.onCreate(savedInstanceState);
        View SearchView = inflater.inflate(R.layout.search, container, false);
        // Progress dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);

        SearchInputCID = (EditText) SearchView.findViewById(R.id.search_input_committee_name);
        btn_search = (Button) SearchView.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

        recyclerView = (RecyclerView) SearchView.findViewById(R.id.searchlist);

        mAdapter = new CommitteAdapter(CommitteList , getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        return SearchView;
    }


    private void submitForm() {
       SearchCommittee();
    }

    private void SearchCommittee() {
        //Tag used to cancel the request
        String cancel_req_tag = "search";
        progressDialog.setMessage("Searching...");

        final String CommitteName = "committee-5";

                //SearchInputCID.getText().toString();
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_SEARCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.d(TAG, "Register Response: " + );
                Log.d(ContentValues.TAG, "Post Search results in searcg fragment : " + response.toString());

                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    String error = jObj.getString("message");
                    Log.d(TAG, "Register Response: " + error);
                    if (error.equals("successful")) {
                        Log.d(TAG, "Register Response: " + error);
                        JSONArray result = jObj.getJSONArray("result");
                        Log.d(ContentValues.TAG, "RESULT Post Search results in search fragment : " + result.toString());
                        CommitteList = CommitteManager.getInstance(context).getCommittee(result);
                        //mCommitteList.clear();

                        for(int i=0; i< CommitteList.size(); i++){
                            Log.d(TAG, "Name traverse: " + CommitteList.get(i).name);
                            Committe com = new Committe(CommitteList.get(i).name , CommitteList.get(i).owner_name , CommitteList.get(i).id);
                            CommitteList.add(com);
                        }
                        mAdapter.notifyDataSetChanged();
                        mAdapter.setClickListener(SearchFragment.this);

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
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", CommitteName);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(context).addToRequestQueue(strReq,cancel_req_tag);
    }


    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onClick(View view, int position) {
        final Committe committe = CommitteList.get(position);
        Intent i = new Intent(context, SearchResultDetailsActivity.class);
        i.putExtra("id", committe.id);
        startActivity(i);
    }

}
*/