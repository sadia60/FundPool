package com.example.awaiswaheed.committe104;
import android.app.Application;
import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class CommitteManager extends Application {
    private Context context;
    private static CommitteManager mInstance;
    private List<Committe> committies;
    public CommitteManager(Context c) {
        this.context = c;
    }

    public static CommitteManager getInstance(Context c) {
        if (mInstance == null) {mInstance = new CommitteManager(c);}
        return mInstance;
    }

    public List<Committe> getCommittee(JSONArray result) {
        Committe committe = null;
        if (committies != null) {//if not null then clear previous one
            committies.clear();
        }
        committies = new ArrayList<Committe>();
        for (int i = 0; i< result.length(); i++) {
            JSONObject details = null;
            try {
                details = result.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                committe = new Committe(details.getString("c_name"),
                        details.getString("owner_name"),
                        details.getInt("c_id"));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            committies.add(committe);
        }
        return  committies;
    }
}