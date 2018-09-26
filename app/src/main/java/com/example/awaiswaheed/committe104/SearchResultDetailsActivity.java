package com.example.awaiswaheed.committe104;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchResultDetailsActivity extends Activity {
    TextView SearchResultCName ,SearchResultCID, SearchResultOwnerName, SearchResultAmount,
            SearchResultParticipants, SearchResultDuration, SearchResultStartDate;
    public RecyclerView recyclerView;
    public CommitteAdapter mAdapter;
    private List<Committe> mCommitteList = new ArrayList<>();
    //private List<Committe> CommitteList = new ArrayList<>();

    Activity context;
    private static final String URL_FOR_MEMBERS_NAME = "https://majid-compose1.mybluemix.net/committee/search_committee";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_result_detail);
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setCancelable(false);

        SearchResultCName =(TextView)findViewById(R.id.search_result_c_name);
        SearchResultCID =(TextView)findViewById(R.id.search_result_c_id);
        SearchResultOwnerName =(TextView)findViewById(R.id.search_result_owner_name);
        SearchResultAmount =(TextView)findViewById(R.id.search_result_amount);
        SearchResultParticipants =(TextView)findViewById(R.id.search_result_participants);
        SearchResultDuration =(TextView)findViewById(R.id.search_result_duration);
        SearchResultStartDate =(TextView)findViewById(R.id.search_result_start_date);
        recyclerView = (RecyclerView) findViewById(R.id.searchlist);


        mAdapter = new CommitteAdapter(mCommitteList , getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
}
