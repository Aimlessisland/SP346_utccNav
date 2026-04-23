package com.example.sp346_utccnav;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<HistoryEntry> historyList;

    public HistoryAdapter(List<HistoryEntry> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryEntry entry = historyList.get(position);
        holder.tvDestination.setText("สถานที่ : " + (entry.getDestination() != null ? entry.getDestination() : "---"));
        holder.tvStartPoint.setText("จุดเริ่มต้น : " + (entry.getStartingPoint() != null ? entry.getStartingPoint() : "---"));
        
        String locStr = "---";
        if (entry.getLatitude() != null && entry.getLongitude() != null) {
            locStr = entry.getLatitude() + ", " + entry.getLongitude();
        }
        holder.tvLocation.setText("โลเคชั่น : " + locStr);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public void setHistoryList(List<HistoryEntry> historyList) {
        this.historyList = historyList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDestination, tvStartPoint, tvLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvStartPoint = itemView.findViewById(R.id.tvStartPoint);
            tvLocation = itemView.findViewById(R.id.tvLocation);
        }
    }
}