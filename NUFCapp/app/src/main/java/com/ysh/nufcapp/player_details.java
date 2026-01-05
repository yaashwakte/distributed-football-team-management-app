package com.ysh.nufcapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class player_details extends AppCompatActivity {

    ImageView profile_pic;
    TextView name;
    Bitmap bitmap;
    EditText goals,assists,saves,xp;
    AutoCompleteTextView league;
    Boolean image_selected = false;
    String encrypted_image, this_number;
    public String[] type_options = {"Bronze","Silver","Gold","Sapphire","Ruby","Emerald","Amethyst","Pearl","Obsidian","Diamond"};
    Button update,remove;
    ImageButton select_img,update_img;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_details);
        update = findViewById(R.id.button2);
        remove = findViewById(R.id.button3);
        profile_pic = findViewById(R.id.imageView3);
        name = findViewById(R.id.name_textview);
        goals = findViewById(R.id.goals_txtbox);
        assists = findViewById(R.id.assists_txtbox);
        saves = findViewById(R.id.saves_txtbox);
        xp = findViewById(R.id.xp_txtbox);
        league = findViewById(R.id.league_txtbox);
        update_img = findViewById(R.id.update_profile_btn);
        select_img = findViewById(R.id.select_profile_btn);

        Bundle extra = getIntent().getExtras();
        this_number = extra.getString("number");
        name.setText(extra.getString("name"));
        league.setText(extra.getString("type"));
        goals.setText(extra.getString("goals"));
        assists.setText(extra.getString("assists"));
        saves.setText(extra.getString("saves"));
        xp.setText(extra.getString("xp"));
        league.setText(extra.getString("league"));
        Glide.with(this).load(extra.getString("photo")).into(profile_pic);
        ArrayAdapter<String> adp = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, type_options);
        league.setAdapter(adp);
        try
        {
            ///Button Logic
            if (Player.Type.equals("Regular")){

                update.setVisibility(View.INVISIBLE);
                remove.setVisibility(View.INVISIBLE);

                if (Player.Number.equals(this_number))
                {

                }
                else
                {
                    select_img.setVisibility(View.INVISIBLE);
                    update_img.setVisibility(View.INVISIBLE);
                }
            }
        }
        catch (Exception e)
        {
            Toast.makeText(player_details.this, e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    public void update_profile(View view)
    {
       if (image_selected == true)
       {
           Loading_Box_activity loading = new Loading_Box_activity(this);
           loading.loading_box();
           /// verify player
           String url ="https://nufcfootballclub.000webhostapp.com/nufc/updateprofile.php";
           // Request a string response from the provided URL.
           StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                   new Response.Listener<String>()
                   {
                       @Override
                       public void onResponse(String response)
                       {
                           // Get Player Data
                           if (response.equals("1"))
                           {
                               loading.stop_loading();
                               MainActivity mainActivity = new MainActivity();
                               mainActivity.reload_fg1();
                           }
                           else
                           {
                               loading.stop_loading();
                               Toast.makeText(player_details.this, "Error "+response,Toast.LENGTH_LONG).show();
                           }
                       }
                   }, new Response.ErrorListener()
           {
               @Override
               public void onErrorResponse(VolleyError error)
               {
                   // Something went wrong
                   Toast.makeText(player_details.this, "Error :"+"\n"+ error.toString(),Toast.LENGTH_SHORT).show();
               }
           }){
               @Override
               protected Map<String, String> getParams() throws AuthFailureError {
                   Map<String, String> data = new HashMap<String, String>();
                   data.put("image",encrypted_image);
                   data.put("player_number",this_number);
                   return data;
               }
           };
           // Add the request to the RequestQueue.
           RequestQueue queue = Volley.newRequestQueue(player_details.this);
           queue.add(stringRequest);

           stringRequest.setRetryPolicy(new RetryPolicy() {
               @Override
               public int getCurrentTimeout()
               {
                   return 70000;
               }

               @Override
               public int getCurrentRetryCount()
               {
                   return 70000;
               }

               @Override
               public void retry(VolleyError error) throws VolleyError {
               }
           });
       }
       else
       {
           Toast.makeText(player_details.this, "No Image Selected",Toast.LENGTH_SHORT).show();
       }
    }

    public void select_profile(View view)
    {
        Dexter.withContext(player_details.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener()
                {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse)
                    {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent,"Select Player Image"),1);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null)
        {
            try
            {
                Uri f_path = data.getData();
                InputStream inputStream = getContentResolver().openInputStream(f_path);
                bitmap = BitmapFactory.decodeStream(inputStream);
                /// Process image
                profile_pic.setImageBitmap(bitmap);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,50,stream);
                byte[] img_byte = stream.toByteArray();
                encrypted_image = android.util.Base64.encodeToString(img_byte, Base64.DEFAULT);
                image_selected = true;
            }
            catch (Exception e)
            {
                image_selected = false;
                Toast.makeText(player_details.this, e.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void remove_player(View view)
    {
        if (!this_number.equals("9175854258"))
        {
            Loading_Box_activity loading = new Loading_Box_activity(this);
            new AlertDialog.Builder(this).setMessage("Remove "+name.getText()+" ?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    loading.loading_box();
                    /// verify player
                    String url ="https://nufcfootballclub.000webhostapp.com/nufc/removeplayer.php";
                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response)
                                {
                                    // Get Player Data
                                    if (response.equals("1"))
                                    {
                                        loading.stop_loading();
                                        MainActivity mainActivity = new MainActivity();
                                        mainActivity.reload_fg1();
                                    }
                                    else
                                    {
                                        loading.stop_loading();
                                        Toast.makeText(player_details.this, "Error ",Toast.LENGTH_LONG).show();
                                    }
                                }
                            }, new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            // Something went wrong
                            Toast.makeText(player_details.this, "No Internet connection",Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> data = new HashMap<String, String>();
                            data.put("player_number",this_number);
                            return data;
                        }
                    };
                    // Add the request to the RequestQueue.
                    RequestQueue queue = Volley.newRequestQueue(player_details.this);
                    queue.add(stringRequest);

                    stringRequest.setRetryPolicy(new RetryPolicy() {
                        @Override
                        public int getCurrentTimeout()
                        {
                            return 70000;
                        }

                        @Override
                        public int getCurrentRetryCount()
                        {
                            return 70000;
                        }

                        @Override
                        public void retry(VolleyError error) throws VolleyError {
                        }
                    });
                }
            }).setNegativeButton("No",null).show();
        }
       else
        {
            Toast.makeText(player_details.this, "You cannot remove Developer!",Toast.LENGTH_SHORT).show();
        }
    }

    public void update_stats(View view)
    {
        String goal = goals.getText().toString().trim();
        String assi = assists.getText().toString().trim();
        String sav = saves.getText().toString().trim();
        String xpp = xp.getText().toString().trim();
        String lea = league.getText().toString().trim();
        if (!goal.trim().equals("") && !assi.trim().equals("") && !sav.trim().equals("") && !xpp.trim().equals("") && lea.matches("Bronze|Silver|Gold|Sapphire|Ruby|Emerald|Amethyst|Pearl|Obsidian|Diamond"))
        {
            Loading_Box_activity loading = new Loading_Box_activity(this);
            loading.loading_box();
            /// verify player
            String url ="https://nufcfootballclub.000webhostapp.com/nufc/updatestats.php";
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                            // Get Player Data
                            if (response.equals("1"))
                            {
                                loading.stop_loading();
                                MainActivity mainActivity = new MainActivity();
                                mainActivity.reload_fg1();
                            }
                            else
                            {
                                loading.stop_loading();
                                Toast.makeText(player_details.this, "Error "+response,Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    // Something went wrong
                    Toast.makeText(player_details.this, "No Internet connection",Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<String, String>();
                    data.put("player_goal",goal);
                    data.put("player_assist",assi);
                    data.put("player_save",sav);
                    data.put("player_xp",xpp);
                    data.put("player_league",lea);
                    data.put("player_number",this_number);
                    return data;
                }
            };
            // Add the request to the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(player_details.this);
            queue.add(stringRequest);

            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout()
                {
                    return 70000;
                }

                @Override
                public int getCurrentRetryCount()
                {
                    return 70000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {
                }
            });
        }
        else
        {
            Toast.makeText(player_details.this, "Assists,Goals,Saves,XP should not be empty"+"\n"+"Type should be Admin or Regular",Toast.LENGTH_LONG).show();
        }
    }

    public void showxp_info(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Every Goal or Save will add 50 XP to your Profile."+"\n\n"+"Every Assist will add 30 XP to your Profile."+"\n\n"+"Based on your XP you will be ranked in the leaderboard");
        builder.setTitle("XP");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });
        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showleague_info(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String leagues = "\nBronze\nSilver\nGold\nSapphire\nRuby\nEmerald\nAmethyst\nPearl\nObsidian\nDiamond";
        builder.setMessage("You will Advance to next League only if you secure your Position among the Top 7 every week"+"\n\n"+"The Players who will Fail to secure the Position in Top 7 will remain in the same League\n\nLeagues :\n"+leagues);
        builder.setTitle("League");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });
        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}