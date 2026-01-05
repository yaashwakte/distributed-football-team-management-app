package com.ysh.nufcapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class activity_login extends AppCompatActivity {

    EditText number_input;
    Button verify;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            number_input = findViewById(R.id.player_number_input);
            verify = findViewById(R.id.verify_btn);
            verify.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (number_input.length() == 10)
                    {
                        verify_player();
                    }
                    else
                    {
                        Toast.makeText(activity_login.this, "Enter 10-Digit Mobile Number",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(activity_login.this, e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(this,activity_login.class);
        startActivity(intent);
    }

    public void verify_player()

    {
        Loading_Box_activity loading = new Loading_Box_activity(this);
        loading.loading_box();
        /// verify player
        String url ="https://nufcfootballclub.000webhostapp.com/nufc/verify.php";
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
                            JSONObject p_object = new JSONObject(response);
                            String num = p_object.getString("number");
                            String tp = p_object.getString("type");
                            String st = p_object.getString("status");
                            String nm = p_object.getString("name");
                            // Checking Player
                            if (num != "null")
                            {
                                /// Player Found
                                SharedPreferences sp = getSharedPreferences("player_pref",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("number",num);
                                editor.putString("type",tp);
                                editor.putString("status",st);
                                editor.putString("name",nm);
                                Player.Number = sp.getString("number","");
                                Player.Name = sp.getString("name","");
                                Player.Type = sp.getString("type","");
                                Player.Status = sp.getString("status","");
                                editor.commit();
                                MainActivity main = new MainActivity();
                                main.recreate_main();
                                loading.stop_loading();
                                finish();
                            }
                            else
                            {
                                loading.stop_loading();
                                Toast.makeText(activity_login.this, "Verification Failed!",Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e)
                        {
                            Toast.makeText(activity_login.this, e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // Something went wrong
                Toast.makeText(activity_login.this, "Error Occurred while verifying",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<String, String>();
                data.put("player_number",number_input.getText().toString().trim());
                return data;
            }
        };
        // Add the request to the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(activity_login.this);
        queue.add(stringRequest);

        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout()
            {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount()
            {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
            }
        });
    }
}