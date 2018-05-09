package com.example.vikas.razorselog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class empreg extends AppCompatActivity {


    Button btn;
    EditText Name ,Contact_no , Password,Conpass;
    String name , contact ,password,conpass;
    AlertDialog.Builder builder;
    TextView login_wala;
    String url = "http://192.168.0.118/register.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empreg);


        btn = (Button)findViewById(R.id.btnRegister);
        Name = (EditText)findViewById(R.id.reg_fullname);
        Contact_no = (EditText)findViewById(R.id.cont);
        Password = (EditText)findViewById(R.id.reg_password);
        Conpass = (EditText)findViewById(R.id.conpa);
        login_wala = (TextView)findViewById(R.id.ltol);
        builder = new AlertDialog.Builder(empreg.this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = Name.getText().toString();
                contact = Contact_no.getText().toString();
                password = Password.getText().toString();
                conpass = Conpass.getText().toString();

                if (isValidate())

                {


                    if (!(password.equals(conpass))) {
                        builder.setTitle("something wrong....");
                        builder.setMessage("Password doesn't Match");
                        displayAlert("input_error");
                    } else {


                        StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONArray jsonarray = new JSONArray(response);
                                    JSONObject jsonobject = jsonarray.getJSONObject(0);
                                    String code = jsonobject.getString("code");
                                    String message = jsonobject.getString("message");

                                    //Toast.makeText(getApplicationContext(), "Error: " + code , Toast.LENGTH_LONG).show();
                                    builder.setTitle("server response");
                                    builder.setMessage(message);
                                    displayAlert(code);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {


                            }
                        }) {

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("name", name);
                                params.put("contact", contact);
                                params.put("password", password);
                                params.put("conpass", conpass);
                                params.put("type" ,"user");


                                return params;
                            }
                        };

                        MySigleton.getInstance(empreg.this).addToRequestQueue(stringrequest);

                    }



                }


            }



        });

        login_wala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                startActivity(new Intent(empreg.this,MainActivity.class));
            }


        });

    }


    public boolean isValidate() {

        String MobilePattern = "[0-9]{10}";
        if (Name.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(empreg.this,"Please enter A User Name" , Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (Contact_no.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(empreg.this,"Please enter A Mobile No." , Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!Contact_no.getText().toString().matches(MobilePattern)) {
            Toast.makeText(empreg.this,"Please enter A 10 Digit Mobile No." , Toast.LENGTH_SHORT).show();
            Contact_no.setText("");
            return false;
        } else if (Password.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(empreg.this,"Please enter A Password" , Toast.LENGTH_SHORT).show();
            return false;
        } else if (Conpass.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(empreg.this,"Again Enter A Password" , Toast.LENGTH_SHORT).show();
            return false;
        }  else
            return true;
    }

    public void displayAlert(final String code) {

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(code.equals("input_error"))
                {
                    Password.setText("");
                    Conpass.setText("");
                }

                else {
                    if (code.equals("reg_success")) {
                        finish();
                    } else if (code.equals("reg_failed")) {
                        Name.setText("");
                        Contact_no.setText("");
                        Password.setText("");
                        Conpass.setText(" ");
                    }
                }
            }
        }) ;

        AlertDialog alertdialog = builder.create();
        alertdialog.show();
    }




}
