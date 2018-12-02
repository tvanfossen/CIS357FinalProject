package td.warp.com.warptd;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AchievementsAdapter extends RecyclerView.Adapter<AchievementsAdapter.ViewHolder> {

    public List<Achievement> achievementList;

    public AchievementsAdapter(List<Achievement> achievementList){
        this.achievementList = achievementList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.achievement_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.mName.setText(achievementList.get(i).getName());
        viewHolder.mDescription.setText("Description: " + achievementList.get(i).getDescription());

    }

    @Override
    public int getItemCount() {
        return achievementList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public TextView mName;
        public TextView mDescription;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            mName = (TextView) mView.findViewById(R.id.txtName);
            mDescription = (TextView) mView.findViewById(R.id.txtDescription);

        }
    }
}
