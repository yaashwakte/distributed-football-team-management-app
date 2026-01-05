package com.ysh.nufcapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import org.json.JSONArray;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public String [] names = {"Home","Leaderboards"};
    Menu my_menu;
    FragmentOne fg;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            SharedPreferences sp = getSharedPreferences("player_pref",MODE_PRIVATE);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            if (sp.contains("number"))
            {
                Player.Type = sp.getString("type","");
                Player.Number = sp.getString("number","");
                Player.Name = sp.getString("name","");
                Player.Status = sp.getString("status","");
                TabLayout tabLayout = findViewById(R.id.tabLayout);
                ViewPager2 viewPager2 = findViewById(R.id.viewpager2);
                PageAdapter pageAdapter = new PageAdapter(this);
                viewPager2.setAdapter(pageAdapter);
                new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> tab.setText(names[position])).attach();
            }
            else
            {
                Intent intent = new Intent(this,activity_login.class);
                startActivity(intent);
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this,e.toString(), Toast.LENGTH_LONG).show();
        }
    }


    public void reload_fg1()
    {
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager2 = findViewById(R.id.viewpager2);
        PageAdapter pageAdapter = new PageAdapter(this);
        viewPager2.setAdapter(pageAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> tab.setText(names[position])).attach();
    }

    public void reset_day()
    {
        try
        {
            JSONArray json_players = new JSONArray();
            for (String json_p_names : Player.players_avail)
            {
                json_players.put(json_p_names);
            }
            String url ="https://nufcfootballclub.000webhostapp.com/nufc/resetday.php";
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                                SharedPreferences sp = getSharedPreferences("player_pref",0);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("status","out");
                                editor.commit();
                                Toast.makeText(MainActivity.this, response.trim(),Toast.LENGTH_SHORT).show();
                                reload_fg1();
                        }
                    }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    // Something went wrong
                    Toast.makeText(MainActivity.this, "Error :"+"\n"+ error.toString(),Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<String, String>();
                    data.put("available_p",json_players.toString());
                    return data;
                }
            };
            // Add the request to the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
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
        catch (Exception e)
        {
            Toast.makeText(MainActivity.this, "Error:"+e.toString(),Toast.LENGTH_LONG).show();
        }

    }

    public void recreate_main()
    {
       this.recreate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.nufc_menu,menu);
        my_menu = menu;
        MenuItem item = menu.findItem(R.id.add_player_btn);
        MenuItem ite = menu.findItem(R.id.reset_day_btn);
        MenuItem itm = menu.findItem(R.id.reset_xp_btn);
        if (Player.Type != null)
        {
            if (Player.Type.equals("Regular"))
            {
                item.setVisible(false);
                ite.setVisible(false);
                itm.setVisible(false);
            }
            else
            {
                item.setVisible(true);
                ite.setVisible(true);
                itm.setVisible(true);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int item_id = item.getItemId();

        if (item_id == R.id.add_player_btn)
        {
            Intent intent = new Intent(this,add_player_activity.class);
            startActivity(intent);
        }
        else if (item_id == R.id.refresh_btn)
        {
            reload_fg1();
        }
        else if (item_id == R.id.reset_day_btn)
        {
           reset_day();
        }
        else if (item_id == R.id.reset_xp_btn)
        {
            reset_xp();
        }
        else if (item_id == R.id.exit_btn)
        {
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }

    private void reset_xp()
    {
        String url ="https://nufcfootballclub.000webhostapp.com/nufc/resetxp.php";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        // Get Player Data
                        Toast.makeText(MainActivity.this, "XP Reset Done",Toast.LENGTH_SHORT).show();
                        reload_fg1();
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // Something went wrong
                Toast.makeText(MainActivity.this, "Error :"+"\n"+ error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){

        };
        // Add the request to the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
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
}