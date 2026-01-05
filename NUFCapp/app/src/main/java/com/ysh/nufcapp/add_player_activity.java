package com.ysh.nufcapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.BitmapCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class add_player_activity extends AppCompatActivity {

    Button add;
    ImageButton img_btn;
    ImageView player_profile;
    EditText number, name;
    Bitmap bitmap;
    String encrypted_img;
    AutoCompleteTextView type_menu;
    Boolean image_selected = false;
    public String[] type_options = {"Regular","Admin"};
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_player);
            add = findViewById(R.id.add_player_btn);
            number = findViewById(R.id.number_input);
            name = findViewById(R.id.name_input);
            type_menu = findViewById(R.id.autoComplete_one);
            img_btn = findViewById(R.id.add_img_btn);
            player_profile = findViewById(R.id.imageview_insert);
            ArrayAdapter<String> adp = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,type_options);
            type_menu.setAdapter(adp);
        }
        catch (Exception e)
        {
            Toast.makeText(add_player_activity.this, e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    public void add_player(View view)
    {
        if (image_selected == false)
        {
            bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.user);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byte[] img_byte = stream.toByteArray();
            encrypted_img = android.util.Base64.encodeToString(img_byte,Base64.DEFAULT);
        }
        String nam = name.getText().toString().trim();
        String num = number.getText().toString().trim();
        String typ = type_menu.getText().toString().trim();
        if (num.length() == 10 && nam.length() > 0 && typ.equals("Regular") || typ.equals("Admin"))
        {
            Loading_Box_activity loading = new Loading_Box_activity(this);
            loading.loading_box();
            /// verify player
            String url ="https://nufcfootballclub.000webhostapp.com/nufc/addplayer.php";
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                            // Get Player Data
                            if (response.equals("Added"))
                            {
                                Toast.makeText(add_player_activity.this, "Player Added",Toast.LENGTH_LONG).show();
                                name.setText("");
                                number.setText("");
                                type_menu.setText("Regular");
                                loading.stop_loading();
                            }
                            else
                            {
                                loading.stop_loading();
                                Toast.makeText(add_player_activity.this, "Error:"+"\n"+response,Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    // Something went wrong
                    Toast.makeText(add_player_activity.this, "Error :"+"\n"+ error.toString(),Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<String, String>();
                    data.put("player_number",number.getText().toString().trim());
                    data.put("player_name",name.getText().toString().trim());
                    data.put("player_type",type_menu.getText().toString().trim());
                    data.put("image",encrypted_img);
                    return data;
                }
            };
            // Add the request to the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(add_player_activity.this);
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
            Toast.makeText(add_player_activity.this, "Please Make sure :"+"\n"+"Name Should not be Empty."+"\n"+"Number should be 10-Digits."+"\n"+"Type must be Regular or Admin.",Toast.LENGTH_LONG).show();
        }
    }

    public void select_img(View view)
    {
        Dexter.withContext(add_player_activity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
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
                player_profile.setImageBitmap(bitmap);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,50,stream);
                byte[] img_byte = stream.toByteArray();
                encrypted_img = android.util.Base64.encodeToString(img_byte, Base64.DEFAULT);
                image_selected = true;
            }
            catch (Exception e)
            {
                image_selected = false;
                Toast.makeText(add_player_activity.this, e.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}