package com.example.vikas.razorselog;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class entryfill extends AppCompatActivity {
    private static final String TAG = entryfill.class.getSimpleName();
    EditText et;
    String name , date , work ,dates1;
    Button btn;
    Date date1;
   // Toolbar toolbar;
  // private static final String TAG = "entryfill";

    String url = "http://192.168.0.118/subdet.php";
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entryfill);

        et = (EditText)findViewById(R.id.edtInput);
        btn = (Button)findViewById(R.id.button);
        builder = new AlertDialog.Builder(entryfill.this);

//        ActionBar actionBar = getActionBar();
//
//        // Enabling Up / Back navigation
//        actionBar.setDisplayHomeAsUpEnabled(true);
        //toolbar =(Toolbar)findViewById(R.id.toolbar);

        //getActionBar().setDisplayHomeAsUpEnabled(true);

//        toolbar.setNavigationIcon(R.drawable.work);
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(entryfill.this, "Please enter Your Work", Toast.LENGTH_SHORT).show();
                } else {
                    Bundle extras = getIntent().getExtras();
                    name= extras.getString("name");
                    name =name.toLowerCase();

                    date = extras.getString("date");



                    work = et.getText().toString();



                    Log.d(TAG ," response  " + name  + date +work );
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
                            params.put("date", date);
                            params.put("work", work);
                            //params.put("conpass", conpass);
                           // Log.d(TAG , "data is going or not");

                            return params;
                        }
                    };

                    MySigleton.getInstance(entryfill.this).addToRequestQueue(stringrequest);


                }
            }
        });





}

    public void displayAlert(final String code) {

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {




                    if (code.equals("reg_success")) {
                        finish();
                    }
                }


        }) ;

        AlertDialog alertdialog = builder.create();
        alertdialog.show();
    }


}
