package td.warp.com.warptd;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class UserStatsAdapter extends RecyclerView.Adapter<UserStatsAdapter.ViewHolder> {

    public List<UserInfo> usersList;

    public UserStatsAdapter(List<UserInfo> usersList){
        this.usersList = usersList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stats_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.mBioMass.setText("Bio mass: " + usersList.get(i).getBiomass());
        viewHolder.mKills.setText("Kills: " + usersList.get(i).getKills());
        viewHolder.mWarps.setText("Warps: " + usersList.get(i).getWarps());
        viewHolder.mGamesPlayed.setText("Games played: " + usersList.get(i).getGamesPlayed());
        viewHolder.mWins.setText("Wins: " + usersList.get(i).getWins());

        double ratio = (usersList.get(i).getWins() * 1.0) / usersList.get(i).getGamesPlayed();
        viewHolder.mRatio.setText("Warps: " + ratio);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public TextView mBioMass;
        public TextView mKills;
        public TextView mWarps;
        public TextView mGamesPlayed;
        public TextView mWins;
        public TextView mRatio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            mBioMass = (TextView) mView.findViewById(R.id.txtDescription);
            mKills = (TextView) mView.findViewById(R.id.txtName);
            mWarps = (TextView) mView.findViewById(R.id.txtWarps);
            mGamesPlayed = (TextView) mView.findViewById(R.id.txtGamesPlayed);
            mWins = (TextView) mView.findViewById(R.id.txtWins);
            mRatio = (TextView) mView.findViewById(R.id.txtRatio);

        }
    }
}
