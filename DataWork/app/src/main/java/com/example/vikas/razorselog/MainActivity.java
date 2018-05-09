package com.example.vikas.razorselog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText Name , Password;
    CheckBox remember;
    String name , password;
    Button btn;
    Boolean saveLogin;
    TextView register;
    String url = "http://192.168.0.118/login.php";
    AlertDialog.Builder builder;
    SharedPreferences preferences, log_preferences;
    SharedPreferences.Editor editor, log_editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        builder = new AlertDialog.Builder(MainActivity.this);
        btn = (Button)findViewById(R.id.btnLogin);
        Name = (EditText)findViewById(R.id.ET1);
        Password = (EditText)findViewById(R.id.ET2);
        log_preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        log_editor = log_preferences.edit();
        remember = (CheckBox)findViewById(R.id.CB);
        register = (TextView)findViewById(R.id.reg);


        saveLogin = log_preferences.getBoolean("saveLogin", false);
        if (saveLogin) {
            Name.setText(log_preferences.getString("username", ""));
            Password.setText(log_preferences.getString("password", ""));
            remember.setChecked(true);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = Name.getText().toString();
                password = Password.getText().toString();


                if((name.equals("")))
                {
                    builder.setTitle("something error");
                    displayAlert("Enter A Valid User Name");

                }


                else if((password.equals("")))
                {
                    builder.setTitle("something error");
                    displayAlert("Enter A Valid Password");
                }

                else
                {

                    final ProgressDialog progress = ProgressDialog.show(MainActivity.this,
                            "", "Signing In....", true, false);

                    final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                progress.dismiss();
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonobject = jsonArray.getJSONObject(0);
                                String code = jsonobject.getString("code");

                                if(code.equals("login_failed"))
                                {
                                    builder.setTitle("login Error");
                                    displayAlert(jsonobject.getString("message"));
                                }


                                else
                                {

                                    preferences = PreferenceManager.
                                            getDefaultSharedPreferences(MainActivity.this);
                                    editor = preferences.edit();

                                    editor.apply();

                                    //remember me, TO REMEMBER LAST LOGIN
                                    if (remember.isChecked()) {
                                        log_editor.putBoolean("saveLogin", true);
                                        log_editor.putString("username", name);
                                        log_editor.putString("password", password);
                                        log_editor.apply();
                                    } else {
                                        log_editor.clear();
                                        log_editor.apply();
                                    }

                                    if(jsonobject.getString("type").equals("user"))
                                    {
                                        Intent i =new Intent(MainActivity.this,register.class);

                                        // Bundle bundle = new Bundle();
                                        i.putExtra("name",jsonobject.getString("name"));
                                        i.putExtra("contact",jsonobject.getString("contact"));

                                        startActivity(i);
                                    }


                                    else
                                    {
                                        Intent i = new Intent(MainActivity.this,adminact.class);
                                        startActivity(i);
                                    }


                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            progress.dismiss();
                            Toast.makeText(MainActivity.this,"Error in Connection", Toast.LENGTH_LONG).show();

                            error.printStackTrace();
                        }
                    })

                    {


                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("name" , name);
                            params.put("password" , password);

                            return params;
                        }
                    };

                    MySigleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
                }
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,empreg.class);
                startActivity(i);
            }
        });


    }
    public void displayAlert(String message)
    {
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Name.setText("");
                Password.setText("");

            }
        });

        AlertDialog alertdialog = builder.create();
        alertdialog.show();

    }




    @Override
    protected void onStop() {
        Name.setText("");
        Password.setText("");
        super.onStop();
    }
}
