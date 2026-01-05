package com.ysh.nufcapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentTwo extends Fragment
{
    RecyclerView recyclerView;
    leaderAdapter leaderAdapter;
    List<Player> playerList;
    leaderAdapter.leaderboardClickListener leaderboardClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View fg_two =  inflater.inflate(R.layout.fragment_two, container, false);

        recyclerView = fg_two.findViewById(R.id.leader_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        playerList= new ArrayList<>();
        get_leaderboard();
        return fg_two;
    }

    private void get_leaderboard()
    {
        /// get player data
        String url ="https://nufcfootballclub.000webhostapp.com/nufc/getleaderboards.php";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        // Get Player Data
                        try
                        {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i =0; i< jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String number = jsonObject.getString("number");
                                String name = jsonObject.getString("name");
                                String lg = jsonObject.getString("league");
                                String xp = jsonObject.getString("xp");
                                String goals = jsonObject.getString("goals");
                                String saves = jsonObject.getString("saves");
                                String ass = jsonObject.getString("assists");
                                String p_img = jsonObject.getString("photo");
                                String player_profile = "https://nufcfootballclub.000webhostapp.com/nufc/Images/"+p_img;

                                Player player = new Player();
                                player.setNum(number);
                                player.setNam(name);
                                player.setLeague(lg);
                                player.setXp(xp);
                                player.setGoals(goals);
                                player.setAssists(ass);
                                player.setSaves(saves);
                                player.setImageurl(player_profile);
                                playerList.add(player);
                            }

                            setOnClickListener();
                            leaderAdapter leaderAdapter = new leaderAdapter(getContext(),playerList,leaderboardClickListener);
                            recyclerView.setAdapter(leaderAdapter);
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getContext(), e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // Something went wrong
                Toast.makeText(getContext(), "No Internet connection",Toast.LENGTH_SHORT).show();
            }
        }){

        };
        // Add the request to the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);

        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout()
            {
                return 90000;
            }

            @Override
            public int getCurrentRetryCount()
            {
                return 90000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
            }
        });
    }

    private void setOnClickListener()
    {
        leaderboardClickListener = new leaderAdapter.leaderboardClickListener()
        {
            @Override
            public void onClick(View v, int position)
            {
                Intent intent = new Intent(getContext(),player_details.class);
                intent.putExtra("number",playerList.get(position).getNum());
                intent.putExtra("name",playerList.get(position).getNam());
                intent.putExtra("league",playerList.get(position).getLeague());
                intent.putExtra("xp",playerList.get(position).getXp());
                intent.putExtra("goals",playerList.get(position).getGoals());
                intent.putExtra("saves",playerList.get(position).getSaves());
                intent.putExtra("assists",playerList.get(position).getAssists());
                intent.putExtra("photo",playerList.get(position).getImageurl());
                startActivity(intent);
            }
        };
    }
}