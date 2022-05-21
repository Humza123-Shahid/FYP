package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.myapplication.data.Result;
import com.example.myapplication.data.model.LoggedInUser;
import com.example.myapplication.ui.login.LoginActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
    EditText et;
    EditText et1;
    EditText et2;
    EditText et3;
    EditText et4;
    RadioButton rd;
    RadioButton rd1;
    RadioGroup rg;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    String name;
    String emailaddress;
    boolean gender;
    String password;
    String phonenumber;
    String dob1;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Calendar calendar = Calendar.getInstance();
        int year =calendar.get(Calendar.YEAR);
        int month =calendar.get(Calendar.MONTH);
        int day =calendar.get(Calendar.DAY_OF_MONTH);

        et=findViewById(R.id.editTextDate);
        et1=findViewById(R.id.editTextTextPersonName);
        et2=findViewById(R.id.editTextTextEmailAddress);
        et3=findViewById(R.id.editTextTextPassword);
        et4=findViewById(R.id.editTextPhone);
        rd=findViewById(R.id.radioButton);
        rd1=findViewById(R.id.radioButton2);
        rg=findViewById(R.id.radioGroup);
        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        onDateSetListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1=i1+1;
                dob1=i2+"/"+i1+"/"+i;
                et.setText(dob1);
            }
        };
    }


    public void insert_record(View v) {

        try {
        name=et1.getText().toString();
        emailaddress=et2.getText().toString();
        password=et3.getText().toString();
        phonenumber=et4.getText().toString();
        dob1= et.getText().toString();
        String regex = "^[A-Za-z0-9_-]{5,15}$";
        String regex2 = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=\\S+$).{6,20}$";
        String regex3 = "^\\d{4}-\\d{7}";
        String regex4 = "^\\d{1,2}/\\d{1,2}/\\d{4}$" ;
        SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        Date dateObj = calendar.getTime();
        String formattedDate = dtf.format(dateObj);
        String[] arr = formattedDate.split("/");


        Pattern p = Pattern.compile(regex);
        Pattern p2 = Pattern.compile(regex2);
        Pattern p3 = Pattern.compile(regex3);
        Pattern p4 = Pattern.compile(regex4);

            Matcher m = p.matcher(name);
            Matcher m2 = p2.matcher(password);
            Matcher m3 = p3.matcher(phonenumber);
            Matcher m4=p4.matcher(dob1);
            if(!m.matches())
            {
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                dlgAlert.setMessage("UserName is not valid.It can contain upper and lower case letters with minimum 5 and maximum 15 characters.");
                dlgAlert.setTitle("Error!!!");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                et1.setText("");
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(emailaddress).matches())
            {
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                dlgAlert.setMessage("Email Address is not valid.It can contain upper and lower case letters,numbers.");
                dlgAlert.setTitle("Error!!!");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                et2.setText("");
                return;
            }
            if(!m2.matches())
            {
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                dlgAlert.setMessage("Password is not valid.It must contain a single upper case,single lower case letters and a single number with minimum length of 6 and maximum length of 20.");
                dlgAlert.setTitle("Error!!!");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                et3.setText("");
                return;
            }
            if(!m3.matches())
            {
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                dlgAlert.setMessage("Phone Number is not valid.It must Follow this format XXXX-XXXXXXX");
                dlgAlert.setTitle("Error!!!");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                et4.setText("");
                return;
            }


            if(rg.getCheckedRadioButtonId()==-1)
            {
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                dlgAlert.setMessage("Must Select a Gender.");
                dlgAlert.setTitle("Error!!!");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                return;
            }
            else if(rd1.isSelected())
            {
                int selectedId = rg.getCheckedRadioButtonId();
                if((RadioButton)findViewById(selectedId)==rd)
                {
                    gender=true;
                }
                else
                {
                    gender=false;
                }

            }
            if(!m4.matches())
            {
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                dlgAlert.setMessage("Date Of Birth is not valid.It must Follow this format DD/MM/YYYY");
                dlgAlert.setTitle("Error!!!");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                et.setText("");
                return;
            }

            String[] arr1 = dob1.split("/");

            int age=Integer.parseInt(arr[2])-Integer.parseInt(arr1[2]);
            if(Integer.parseInt(arr1[1])>Integer.parseInt(arr[1]))
            {
                age--;
            }
            if(Integer.parseInt(arr1[1])==Integer.parseInt(arr[1]) && Integer.parseInt(arr1[0])>Integer.parseInt(arr[0]))
            {
                age--;
            }

            if(age<18)
            {
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                dlgAlert.setMessage("Sorry!!!Your Age must greater than or equal to 18.");
                dlgAlert.setTitle("Error!!!");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                et.setText("");
                return;
            }


//            SQLiteDatabaseClass con = new SQLiteDatabaseClass(this);
//            if(con.getCustName(name))
//            {
//                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
//                dlgAlert.setMessage("Please try another username,this username is already in use.");
//                dlgAlert.setTitle("Error!!!");
//                dlgAlert.setPositiveButton("OK", null);
//                dlgAlert.setCancelable(true);
//                dlgAlert.create().show();
//                return;
//            }
//            con.addNewCustomer(name,emailaddress,gender,password,phonenumber,dob1);
            Query checkUser= FirebaseDatabase.getInstance().getReference("Customer").orderByChild("name").equalTo(name);
            // TODO: handle loggedInUser authentication
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Result<LoggedInUser> obj;
                    if(snapshot.exists()){
                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(MainActivity.this);
                        dlgAlert.setMessage("Please try another username,this username is already in use.");
                        dlgAlert.setTitle("Error!!!");
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                    }
                    else
                    {
                        rootNode=FirebaseDatabase.getInstance();
                        reference=rootNode.getReference("Customer");
                        HelperClass hpc=new HelperClass(name,emailaddress,password,phonenumber,gender,dob1);
                        reference.child(name).setValue(hpc);
                        Toast.makeText(MainActivity.this,"Added successfully",Toast.LENGTH_LONG).show();
                        Intent mint=new Intent(MainActivity.this, MainActivity2.class);
                        mint.putExtra("name",name);
                        mint.putExtra("email",emailaddress);
                        MainActivity.this.startActivity(mint);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                }

            });


        } catch (Exception e) {
            Toast.makeText(this,"Exception. Please check your code and database."+e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
    }
}