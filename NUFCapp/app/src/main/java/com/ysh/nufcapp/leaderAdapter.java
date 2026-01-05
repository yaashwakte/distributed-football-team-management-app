package com.ysh.nufcapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class leaderAdapter extends RecyclerView.Adapter<leaderAdapter.ViewHolder_two>
{
    Context context;
    List<Player> playerList;
    private leaderboardClickListener leaderboardClickListener;

    public leaderAdapter( Context context, List<Player> playerList,leaderboardClickListener leaderboardClickListener)
    {
        this.context = context;
        this.playerList = playerList;
        this.leaderboardClickListener = leaderboardClickListener;
    }

    @NonNull
    @Override
    public ViewHolder_two onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View leader_layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_list,parent,false);
        return new ViewHolder_two(leader_layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_two holder, int position)
    {
        Player player = playerList.get(position);
        holder.nu.setText(player.getNum());
        holder.name.setText(player.getNam());
        holder.league.setText(player.getLeague());
        holder.xp.setText(player.getXp());
        holder.goals.setText(player.getGoals());
        holder.assists.setText(player.getAssists());
        holder.saves.setText(player.getSaves());
        Glide.with(context).load(player.getImageurl()).into(holder.profile);
    }

    public interface leaderboardClickListener
    {
        void onClick(View v,int position);
    }

    @Override
    public int getItemCount()
    {
        return playerList.size();
    }

    public  class ViewHolder_two extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView name,league,xp,goals,assists,saves,nu;
        ImageView profile;
        public ViewHolder_two(@NonNull View itemView)
        {
            super(itemView);
            nu = itemView.findViewById(R.id.number_txt);
            name = itemView.findViewById(R.id.name_txt);
            league = itemView.findViewById(R.id.league_txt);
            xp = itemView.findViewById(R.id.xp_txt);
            goals = itemView.findViewById(R.id.goals_txt);
            assists = itemView.findViewById(R.id.assists_txt);
            saves = itemView.findViewById(R.id.saves_txt);
            profile = itemView.findViewById(R.id.leaderboard_profile);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            leaderboardClickListener.onClick(itemView, getAdapterPosition());
        }
    }
}
