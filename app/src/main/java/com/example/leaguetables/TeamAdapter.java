package com.example.leaguetables;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class TeamAdapter extends ListAdapter<Team, TeamAdapter.TeamViewHolder> {

    public TeamAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Team> DIFF_CALLBACK = new DiffUtil.ItemCallback<Team>() {
        @Override
        public boolean areItemsTheSame(@NonNull Team oldItem, @NonNull Team newItem) {
            return oldItem.getLeague().equals(newItem.getLeague())
                    && oldItem.getName().equals(newItem.getName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Team oldItem, @NonNull Team newItem) {
            return oldItem.getPoints() == newItem.getPoints()
                    && oldItem.getGoalfor() == newItem.getGoalfor()
                    && oldItem.getGoalagaisnt() == newItem.getGoalagaisnt()
                    && oldItem.getGoaldiff() == newItem.getGoaldiff();
        }
    };

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_team, parent, false);
        return new TeamViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        Team team = getItem(position);
        holder.bind(team);
    }

    static class TeamViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvPoints;
        private final TextView tvFor;
        private final TextView tvAgainst;
        private final TextView tvDiff;

        TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvTeamName);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            tvFor = itemView.findViewById(R.id.tvGoalsFor);
            tvAgainst = itemView.findViewById(R.id.tvGoalsAgainst);
            tvDiff = itemView.findViewById(R.id.tvGoalDifference);
        }

        void bind(Team team) {
            tvName.setText(team.getName());
            tvPoints.setText(String.valueOf(team.getPoints()));
            tvFor.setText(String.valueOf(team.getGoalfor()));
            tvAgainst.setText(String.valueOf(team.getGoalagaisnt()));
            tvDiff.setText(String.valueOf(team.getGoaldiff()));
        }
    }
}
