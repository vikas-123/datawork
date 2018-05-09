package com.example.vikas.razorselog;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class checkdata extends AppCompatActivity {

    TableLayout t1;
    TableRow tr;
    TextView nmae;
    String s , startdate = "" , enddate = "";
    String url = "http://192.168.0.118/show.php";
    Button  stdbtn ,endbtn,show;
    private static final String TAG = "checkdata";
   private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListener1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkdata);
        nmae = (TextView)findViewById(R.id.name);
        stdbtn = (Button)findViewById(R.id.button3);
        endbtn = (Button)findViewById(R.id.button5);
        show = (Button)findViewById(R.id.button6);
        t1 = (TableLayout)findViewById(R.id.table);

        Bundle extras = getIntent().getExtras();
        s = extras.getString("name");
        nmae.setText("Hello "  +  "Mr.  " + s);

        stdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        checkdata.this,
                        android.R.style.Theme_DeviceDefault_Dialog,
                        mDateSetListener,
                        year, month, day);
                dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + month + "/" + day + "/" + year);

                startdate = day + "-" + month + "-" + year;

                DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                //String inputDateStr=str1;
                Date date = null;
                try {
                    date = inputFormat.parse(startdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String outputDateStr = outputFormat.format(date);
                startdate = outputDateStr.toString();
                // Log.d(TAG ,"start date" + startdate);
                //  Toast.makeText(register.this,date1 ,Toast.LENGTH_SHORT).show();
            }

        };

        endbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year1 = cal.get(Calendar.YEAR);
                int month1 = cal.get(Calendar.MONTH);
                int day1 = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        checkdata.this,
                        android.R.style.Theme_DeviceDefault_Dialog,
                        mDateSetListener1,
                        year1, month1, day1);
                // dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                dialog.getDatePicker().setMinDate(cal.getTimeInMillis());

                // dialog.getDatePicker().setMinDate();
//                    long s2  = cal.getTimeInMillis();
//                Log.d(TAG, s2);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + month + "/" + day + "/" + year);

                enddate = day + "-" + month + "-" + year;
                DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                //String inputDateStr=str1;
                Date date = null;
                try {
                    date = inputFormat.parse(enddate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String outputDateStr = outputFormat.format(date);
                enddate = outputDateStr.toString();
                // Toast.makeText(register.this,date ,Toast.LENGTH_SHORT).show();
            }
        };


        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (startdate.length() == 0) {
                    Toast.makeText(checkdata.this, "Enter Start Date", Toast.LENGTH_SHORT).show();
                } else if (enddate.length() == 0) {
                    Toast.makeText(checkdata.this, "Enter End Date", Toast.LENGTH_SHORT).show();


                } else {


                    t1.removeAllViews();


                    addHeaders();


                    t1.setVisibility(View.VISIBLE);


                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonObject = null;
                                        final String[] str1 = new String[jsonArray.length()];
                                        final String[] str2 = new String[jsonArray.length()];

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            jsonObject = jsonArray.getJSONObject(i);
                                            str1[i] = jsonObject.getString("date");
                                            str2[i] = jsonObject.getString("work");


                                            tr = new TableRow(checkdata.this);
                                            tr.setLayoutParams(new TableRow.LayoutParams(
                                                    TableRow.LayoutParams.MATCH_PARENT,
                                                    TableRow.LayoutParams.MATCH_PARENT));

                                            TextView companyTV = new TextView(checkdata.this);
                                            companyTV.setText(str1[i]);
                                            companyTV.setBackgroundResource(R.drawable.tags_rounded_corners);
                                            companyTV.setGravity(Gravity.LEFT);
                                            companyTV.setTextColor(Color.BLACK);
                                            companyTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                                            companyTV.setPadding(50, 5, 100, 0);
                                            tr.addView(companyTV);  // Adding textView to tablerow.


                                            TextView valueTV = new TextView(checkdata.this);
                                            valueTV.setText(str2[i]);
                                            valueTV.setGravity(Gravity.RIGHT);
                                            valueTV.setBackgroundResource(R.drawable.tags_rounded_corners);
                                            valueTV.setTextColor(Color.BLACK);

                                            valueTV.setPadding(5, 5, 50, 0);
                                            valueTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                                            tr.addView(valueTV);

                                            t1.addView(tr, new TableLayout.LayoutParams(
                                                    TableRow.LayoutParams.WRAP_CONTENT,
                                                    TableRow.LayoutParams.WRAP_CONTENT));
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(checkdata.this, "Error in Connection", Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                        }
                    })

                    {


                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("name", s);
                            params.put("startdate", startdate);
                            params.put("enddate", enddate);

                            return params;
                        }
                    };

                    MySigleton.getInstance(checkdata.this).addToRequestQueue(stringRequest);


                    //end of setonclicklistener


                }
            }

        });


    }


       public void addHeaders(){

        tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));

        TextView tarikh = new TextView(this);
        tarikh.setText("DATE");
        tarikh.setBackgroundColor(Color.GRAY);
        tarikh.setGravity(Gravity.CENTER);
        tarikh.setTextColor(Color.BLACK);
        tarikh.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tarikh.setPadding(50, 5, 100, 0);
        tr.addView(tarikh);  // Adding textView to tablerow.


        TextView work = new TextView(this);
        work.setText("WORK");
        work.setGravity(Gravity.CENTER);

        work.setTextColor(Color.BLACK);
        work.setBackgroundColor(Color.GRAY);

        work.setPadding(5, 5, 50, 0);
        work.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(work);

        t1.addView(tr, new TableLayout.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
    }


    @Override
    protected void onStop() {

        while (t1.getChildCount() > 1) {
            TableRow row =  (TableRow)t1.getChildAt(1);
            t1.removeView(row);
        }
        super.onStop();
        t1.setVisibility(View.GONE);


        //Toast.makeText(getApplicationContext(), "onStop called", Toast.LENGTH_LONG).show();
    }

}
