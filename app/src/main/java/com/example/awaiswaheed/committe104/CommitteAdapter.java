package com.example.awaiswaheed.committe104;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CommitteAdapter extends RecyclerView.Adapter<CommitteAdapter.ViewHolder>{

    private List<Committe> committes;
    private Context mContext;
    private ItemClickListener clickListener;

    public CommitteAdapter(List<Committe> committes, Context context) {
        this.committes = committes;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_search_layout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Committe committe1 = committes.get(position);
        viewHolder.committeName.setText(committe1.name);
        viewHolder.committeID.setText(committe1.id + "");
        viewHolder.ownerName.setText(committe1.owner_name);
    }

    @Override
    public int getItemCount() {
        return committes == null ? 0 : committes.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView committeName;
        public TextView committeID;
        public TextView ownerName;

        public ViewHolder(View itemView) {
            super(itemView);
            committeName = (TextView) itemView.findViewById(R.id.RVS_name);
            committeID = (TextView) itemView.findViewById(R.id.RVS_id);
            ownerName = (TextView) itemView.findViewById(R.id.RVS_Owner);

            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
}