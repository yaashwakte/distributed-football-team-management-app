package com.ysh.nufcapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FragmentOne extends Fragment
{
    RecyclerView recyclerView, recyclerViewTeamA, recyclerViewTeamB;
    RecyclerListAdapter recyclerListAdapter, recyclerListAdapterTeamA, recyclerListAdapterTeamB;
    TextView pcount, mcountA, mcountB;
    Button teambutton, syncbutton;
    ScrollView scrollView;
    SharedPreferences sp;
    Switch aSwitch;
    String[] playernames = {"Player1"},Team_one, Team_two, dummy = {"Player2"}, bum ={"Player3"};
    Boolean team_generated = false;
    public FragmentOne()
    {
        // empty
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View Longview =  inflater.inflate(R.layout.fragment_one, container, false);
        try {
            /// Initialize here FRAGMENT_ONE
            recyclerView = Longview.findViewById(R.id.recycleone);
            recyclerViewTeamA = Longview.findViewById(R.id.recycleTeamA);
            recyclerViewTeamB = Longview.findViewById(R.id.recycleTeamB);
            pcount = Longview.findViewById(R.id.playercount);
            teambutton = Longview.findViewById(R.id.generatebutt);
            syncbutton = Longview.findViewById(R.id.syncbutt);
            mcountA = Longview.findViewById(R.id.membersA);
            mcountB = Longview.findViewById(R.id.membersB);
            scrollView = Longview.findViewById(R.id.scrollviewone);
            aSwitch = Longview.findViewById(R.id.switch2);
            button_logic();
            check_status();
            get_players();
            get_teamA();
            get_teamB();
            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                {
                    if (aSwitch.isChecked())
                    {
                        update_status("in");
                    }
                    else
                    {
                        update_status("out");
                    }
                }
            });

            teambutton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    try
                    {
                        if (playernames.length >= 2)
                        {
                            create_teams();
                        }
                        else
                        {
                            Toast.makeText(getContext(), "At least 2 players required",Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getContext(), e.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
            });

            syncbutton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (team_generated == true)
                    {
                        if (Team_one.length >= 1 && Team_two.length >= 1)
                        {
                            Toast.makeText(getContext(), "Syncing.....",Toast.LENGTH_SHORT).show();
                            sync_teamA();
                            sync_teamB();
                        }
                        else
                        {
                            Toast.makeText(getContext(), "At least one player required for each teams",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Generate Teams First",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(),e.toString(), Toast.LENGTH_LONG).show();
        }
        return Longview;

    }

    private void button_logic()
    {
        if (Player.Type.equals("Regular"))
        {
            syncbutton.setVisibility(View.INVISIBLE);
            teambutton.setVisibility(View.INVISIBLE);
        }
    }

    private void get_teamB()
    {
        List<String> b_list = new LinkedList<>();
        /// get player data
        String url ="https://nufcfootballclub.000webhostapp.com/nufc/getB.php";
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
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String name = jsonObject.getString("name");
                                b_list.add(name);
                            }
                            String[] team_b = new String[b_list.size()];
                            b_list.toArray(team_b);
                            recyclerViewTeamB.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerListAdapterTeamB = new RecyclerListAdapter(getContext(),team_b);
                            recyclerViewTeamB.setAdapter(recyclerListAdapterTeamB);
                            mcountB.setText("Players : "+Integer.toString(recyclerListAdapterTeamB.getItemCount()));
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
                Toast.makeText(getContext(), "Error :"+"\n"+ error.toString(),Toast.LENGTH_SHORT).show();
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

    public void get_teamA()
    {
        List<String> a_list = new LinkedList<>();
        /// get player data
        String url ="https://nufcfootballclub.000webhostapp.com/nufc/getA.php";
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
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String name = jsonObject.getString("name");
                                a_list.add(name);
                            }
                            String[] team_a = new String[a_list.size()];
                            a_list.toArray(team_a);
                            recyclerViewTeamA.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerListAdapterTeamA = new RecyclerListAdapter(getContext(), team_a);
                            recyclerViewTeamA.setAdapter(recyclerListAdapterTeamA);
                            mcountA.setText("Players : "+Integer.toString(recyclerListAdapterTeamA.getItemCount()));
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
                Toast.makeText(getContext(), "Error :"+"\n"+ error.toString(),Toast.LENGTH_SHORT).show();
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

    private void sync_teamB()
    {
        JSONArray json_teamB = new JSONArray();
        for (String json_str_teamB : Team_two)
        {
            json_teamB.put(json_str_teamB);
        }
            String url ="https://nufcfootballclub.000webhostapp.com/nufc/syncteamB.php";
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                            Toast.makeText(getContext(), response.trim(),Toast.LENGTH_SHORT).show();

                        }
                    }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    // Something went wrong
                    Toast.makeText(getContext(), "Error :"+"\n"+ error.toString(),Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<String, String>();
                    data.put("players",json_teamB.toString());
                    return data;
                }
            };
            // Add the request to the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(stringRequest);

            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout()
                {
                    return 100000;
                }

                @Override
                public int getCurrentRetryCount()
                {
                    return 100000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {
                }
            });
    }

    private void sync_teamA()
    {
            JSONArray json_teamA = new JSONArray();
            for (String json_str_teamA : Team_one)
            {
                json_teamA.put(json_str_teamA);
            }
            String url ="https://nufcfootballclub.000webhostapp.com/nufc/syncteamA.php";
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                            Toast.makeText(getContext(), response.trim(),Toast.LENGTH_SHORT).show();
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
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<String, String>();
                    data.put("players",json_teamA.toString());
                    return data;
                }
            };
            // Add the request to the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(stringRequest);

            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout()
                {
                    return 100000;
                }

                @Override
                public int getCurrentRetryCount()
                {
                    return 100000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {
                }
            });
    }

    public void get_players()
    {
        List<String> p_list = new LinkedList<>();
        /// get player data
        String url ="https://nufcfootballclub.000webhostapp.com/nufc/getplayers.php";
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
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String name = jsonObject.getString("name").trim();
                                p_list.add(name);
                            }
                            String[] player_names = new String[p_list.size()];
                            p_list.toArray(player_names);
                            playernames = Arrays.copyOf(player_names,player_names.length);
                            Player.players_avail = Arrays.copyOf(player_names,player_names.length);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerListAdapter= new RecyclerListAdapter(getContext(),player_names);
                            recyclerView.setAdapter(recyclerListAdapter);
                            pcount.setText(Integer.toString(recyclerListAdapter.getItemCount()));
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

    private void update_status(String st)
    {
        String url ="https://nufcfootballclub.000webhostapp.com/nufc/updatestatus.php";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        // Get Player Data
                        if (response.equals("Updated"))
                        {
                            try
                            {
                                SharedPreferences sp = getContext().getSharedPreferences("player_pref",0);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("status",st);
                                editor.commit();
                                Toast.makeText(getContext(), "Status Updated",Toast.LENGTH_LONG).show();
                                Player.Status = st;
                                get_players();
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(getContext(), e.toString(),Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Error While updating Status",Toast.LENGTH_LONG).show();
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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<String, String>();
                data.put("player_number",Player.Number);
                data.put("player_status",st);
                return data;
            }
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

    public void check_status()
    {
        SharedPreferences sp = getContext().getSharedPreferences("player_pref",0);
        /// checking status
        Player.Status = sp.getString("status","");
        if (Player.Status.equals("out"))
        {
            aSwitch.setChecked(false);
        }
        else
        {
            aSwitch.setChecked(true);
        }
    }

    public void create_teams()
    {
        if (playernames.length % 2 == 0)  /// IS EVEN
        {
            try
            {
                List<String> playerlist = Arrays.asList(playernames);
                Collections.shuffle(playerlist);
                playerlist.toArray(playernames);
                String[] TeamA = Arrays.copyOf(playernames,playernames.length/2);
                String[] TeamB = new String[playernames.length/2];
                List<String> TrimmedList = new LinkedList<>(Arrays.asList(playernames));
                TrimmedList.removeAll(Arrays.asList(TeamA));
                TrimmedList.toArray(TeamB);
                ///Recycler View TEAM A
                Team_one = Arrays.copyOf(TeamA,TeamA.length);
                recyclerViewTeamA.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerListAdapterTeamA = new RecyclerListAdapter(getContext(), TeamA);
                recyclerViewTeamA.setAdapter(recyclerListAdapterTeamA);
                mcountA.setText("Players : "+Integer.toString(recyclerListAdapterTeamA.getItemCount()));
                /// Recycler View Team B1
                Team_two = Arrays.copyOf(TeamB,TeamB.length);
                recyclerViewTeamB.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerListAdapterTeamB = new RecyclerListAdapter(getContext(), TeamB);
                recyclerViewTeamB.setAdapter(recyclerListAdapterTeamB);
                mcountB.setText("Players : "+Integer.toString(recyclerListAdapterTeamB.getItemCount()));
                team_generated = true;
            }
            catch (Exception e)
            {
                Toast.makeText(getContext(),e.toString(), Toast.LENGTH_LONG).show();
            }
        }
        else /// IS ODD
        {
            if (playernames.length > 1)
            {
                try
                {
                    Random rand = new Random();
                    int R = rand.nextInt(2);
                    if (R == 1)
                    {
                        /// ADD NAME TO TEAM B
                        String[] pnames = Arrays.copyOf(playernames, playernames.length);
                        List<String> playerlist = new LinkedList<>(Arrays.asList(pnames));
                        Collections.shuffle(playerlist);
                        String LastPlayer = playerlist.get(playerlist.size() - 1);
                        playerlist.remove(playerlist.size() - 1);
                        playerlist.toArray(pnames);
                        String[] TeamA = Arrays.copyOf(pnames,pnames.length/2);
                        List<String> TrimmedList = new LinkedList<>(Arrays.asList(pnames));
                        TrimmedList.remove(TrimmedList.size()- 1);
                        TrimmedList.removeAll(Arrays.asList(TeamA));
                        TrimmedList.add(LastPlayer);
                        String[] TeamB = new String[pnames.length/2 + 1];
                        TrimmedList.toArray(TeamB);
                        ///Recycler View TEAM A
                        Team_one = Arrays.copyOf(TeamA,TeamA.length);
                        recyclerViewTeamA.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerListAdapterTeamA = new RecyclerListAdapter(getContext(), TeamA);
                        recyclerViewTeamA.setAdapter(recyclerListAdapterTeamA);
                        mcountA.setText("Players : "+Integer.toString(recyclerListAdapterTeamA.getItemCount()));
                        /// Recycler View Team B1
                        Team_two = Arrays.copyOf(TeamB,TeamB.length);
                        recyclerViewTeamB.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerListAdapterTeamB = new RecyclerListAdapter(getContext(), TeamB);
                        recyclerViewTeamB.setAdapter(recyclerListAdapterTeamB);
                        mcountB.setText("Players : "+Integer.toString(recyclerListAdapterTeamB.getItemCount()));
                        team_generated = true;
                    }
                    else
                    {
                        /// ADD NAME TO TEAM A
                        String[] pnames = Arrays.copyOf(playernames, playernames.length);
                        List<String> playerlist = new LinkedList<>(Arrays.asList(pnames));
                        Collections.shuffle(playerlist);
                        String LastPlayer = playerlist.get(playerlist.size() - 1);
                        playerlist.remove(playerlist.size() - 1);
                        playerlist.toArray(pnames);
                        String[] TeamB = Arrays.copyOf(pnames,pnames.length/2);
                        List<String> TrimmedList = new LinkedList<>(Arrays.asList(pnames));
                        TrimmedList.remove(TrimmedList.size()- 1);
                        TrimmedList.removeAll(Arrays.asList(TeamB));
                        TrimmedList.add(LastPlayer);
                        String[] TeamA = new String[pnames.length/2 + 1];
                        TrimmedList.toArray(TeamA);
                        ///Recycler View TEAM A
                        Team_one = Arrays.copyOf(TeamA,TeamA.length);
                        recyclerViewTeamA.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerListAdapterTeamA = new RecyclerListAdapter(getContext(), TeamA);
                        recyclerViewTeamA.setAdapter(recyclerListAdapterTeamA);
                        mcountA.setText("Players : "+Integer.toString(recyclerListAdapterTeamA.getItemCount()));
                        /// Recycler View Team B1
                        Team_two = Arrays.copyOf(TeamB,TeamB.length);
                        recyclerViewTeamB.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerListAdapterTeamB = new RecyclerListAdapter(getContext(), TeamB);
                        recyclerViewTeamB.setAdapter(recyclerListAdapterTeamB);
                        mcountB.setText("Players : "+Integer.toString(recyclerListAdapterTeamB.getItemCount()));
                        team_generated = true;
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(getContext(),e.toString(), Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(getContext(),"At least 2 Players Required", Toast.LENGTH_LONG).show();
                team_generated = false;
            }
        }
    }
}