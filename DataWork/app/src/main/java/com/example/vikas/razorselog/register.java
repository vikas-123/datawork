package com.example.vikas.razorselog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class register extends AppCompatActivity {

    TextView Name, Contact ,dates;
    String s,  contact ,str ,str1 ;

    Button logout, fill, show;
    private int pYear;
    private int pMonth;
    private int pDay;



    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Name = (TextView)findViewById(R.id.editText);
        Contact = (TextView)findViewById(R.id.editText1);
        logout = (Button)findViewById(R.id.button2);
        fill = (Button)findViewById(R.id.fillent);
        show =(Button)findViewById(R.id.button4);

        dates =(TextView)findViewById(R.id.date);

        Bundle extras = getIntent().getExtras();
        s = extras.getString("name");
       s= s.toUpperCase();
        contact = extras.getString("contact");
        Name.setText("Welcome" + "  " + "Mr." + " " + s);
        Contact.setText("Contact No. is" + " "  + contact);

        Calendar cal = Calendar.getInstance();

        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);
        pMonth = pMonth + 1;


        StringBuilder sb1 = new StringBuilder();
        sb1.append("Cuurent Date is :  ");
                sb1.append(pDay);
        sb1.append("-");
        sb1.append(pMonth);
        sb1.append("-");
        sb1.append(pYear);
        sb1.append(" ");
        str = sb1.toString();


        StringBuilder sb = new StringBuilder();
        sb.append(pDay);
        sb.append("-");
        sb.append(pMonth);
        sb.append("-");
        sb.append(pYear);

        str1 = sb.toString();



        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        //String inputDateStr=str1;
        Date date = null;
        try {
            date = inputFormat.parse(str1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);
        str1 = outputDateStr.toString();
        dates.setText(str1);


       System.out.println(outputDateStr);



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor e = sp.edit();
                e.clear();
                e.commit();

                startActivity(new Intent(register.this, MainActivity.class));
                finish();   //finish current activity

            }
        });


        fill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(register.this,entryfill.class);
                i.putExtra("name", s);
                i.putExtra("date", str1);
                startActivity(i);

            }
        });



        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(register.this,checkdata.class);
                i.putExtra("name",s) ;
                startActivity(i);
            }
        });



    }
}