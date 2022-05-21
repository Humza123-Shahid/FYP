package com.example.DriverApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.DriverApp.data.Result;
import com.example.DriverApp.data.model.LoggedInUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
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
        String regex = "^[a-zA-Z][a-zA-Z ]+[a-zA-Z]{5,29}$";
        String regex1 = "^[A-Za-z0-9+_.-]+@(.+)$";
        String regex2 = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=\\S+$).{6,20}$";
        String regex3 = ("\\d{4}[-\\.\\s]\\d{7}");
        SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        Date dateObj = calendar.getTime();
        String formattedDate = dtf.format(dateObj);
        String[] arr = formattedDate.split("/");
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

        Pattern p = Pattern.compile(regex);
        Pattern p1 = Pattern.compile(regex1);
        Pattern p2 = Pattern.compile(regex2);
        Pattern p3 = Pattern.compile(regex3);
        if (name == null) {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("UserName cannot be null.");
            dlgAlert.setTitle("Error!!!");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
            return;
        }
        if (emailaddress == null) {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("EmailAddress cannot be null.");
            dlgAlert.setTitle("Error!!!");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
            return;
        }
        if (password == null) {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Password cannot be null.");
            dlgAlert.setTitle("Error!!!");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
            return;
        }
        if (phonenumber == null) {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("PhoneNumber cannot be null.");
            dlgAlert.setTitle("Error!!!");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
            return;
        }
        if (dob1 == null) {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Date of Birth cannot be null.");
            dlgAlert.setTitle("Error!!!");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
            return;
        }



        Matcher m = p.matcher(name);
        Matcher m1 = p1.matcher(emailaddress);
        Matcher m2 = p2.matcher(password);
        Matcher m3 = p3.matcher(phonenumber);
        if(!m.matches())
        {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("UserName is not valid.It can contain upper and lower case letters with minimum 5 and maximum 28 characters.");
            dlgAlert.setTitle("Error!!!");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
            return;
        }
        if(!m1.matches())
        {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Email Address is not valid.It can contain upper and lower case letters,numbers.");
            dlgAlert.setTitle("Error!!!");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
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
            return;
        }
        if(age<18)
        {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Sorry!!!Your Age must greater than or equal to 18.");
            dlgAlert.setTitle("Error!!!");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
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
            Query checkUser= FirebaseDatabase.getInstance().getReference("Driver").orderByChild("name").equalTo(name);
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
                        reference=rootNode.getReference("Driver");
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