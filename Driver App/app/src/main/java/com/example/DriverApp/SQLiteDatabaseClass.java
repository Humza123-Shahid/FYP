package com.example.DriverApp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class SQLiteDatabaseClass extends SQLiteOpenHelper {

    private static final String DB_NAME = "JOTPT";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "Customer";
    private static final String ID_COL = "Id";
    private static final String NAME_COL = "Username";
    private static final String Email_COL = "Emailaddress";
    private static final String Gender_COL = "Gender";
    private static final String Password_COL = "Password";
    private static final String Phonenumber_COL = "Phonenumber";
    private static final String Dob_COL = "DOB";
    private SQLiteDatabase db;

    public SQLiteDatabaseClass(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + Email_COL + " TEXT,"
                + Gender_COL + " BIT,"
                + Password_COL + " TEXT,"
                + Phonenumber_COL + " TEXT,"
                + Dob_COL + " DATE)";

        db.execSQL(query);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void addNewCustomer(String name, String email, boolean gender, String password, String phone, String dob) {

        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(NAME_COL, name);
        values.put(Email_COL, email);
        values.put(Gender_COL, gender);
        values.put(Password_COL,password);
        values.put(Phonenumber_COL, phone);
        values.put(Dob_COL, dob);

        db.insert(TABLE_NAME, null, values);

        db.close();
    }
    public String getData() {
        db = this.getReadableDatabase();
        Cursor c=db.rawQuery("Select * from Customer",null);
        String data=" ";
        int IRow=c.getColumnIndex(ID_COL);
        int IName=c.getColumnIndex(NAME_COL);
        int IEmail=c.getColumnIndex(Email_COL);
        int IGender=c.getColumnIndex(Gender_COL);
        int IPass=c.getColumnIndex(Password_COL);
        int IPhone=c.getColumnIndex(Phonenumber_COL);
        int IDob=c.getColumnIndex(Dob_COL);

        for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
        {
            data=data+c.getString(IRow)+c.getString(IName)+c.getString(IEmail)+c.getString(IGender)+c.getString(IPass)+c.getString(IPhone)+c.getString(IDob);
            data=data+"nextrecord";
        }
        return data;

    }
    public String getCustdata(int id) {
        db = this.getReadableDatabase();
        String[] column=new String[]{ID_COL,NAME_COL,Email_COL,Gender_COL,Password_COL,Phonenumber_COL,Dob_COL};
        Cursor c=db.query(TABLE_NAME,column,ID_COL+"="+id,null,null,null,null);
        if(c!=null)
        {
            c.moveToFirst();
            String data=c.getString(0)+c.getString(1)+c.getString(2)+c.getString(3)+c.getString(4)+c.getString(5)+c.getString(6);
            return data;
        }
        return null;
    }
    public String getCustName(int id) {
        db = this.getReadableDatabase();
        String[] column=new String[]{ID_COL,NAME_COL,Email_COL,Gender_COL,Password_COL,Phonenumber_COL,Dob_COL};
        Cursor c=db.query(TABLE_NAME,column,ID_COL+"="+id,null,null,null,null);
        if(c!=null)
        {
            c.moveToFirst();
            String data=c.getString(1);
            return data;
        }
        return null;
    }
    public Boolean getCustName(String user) {
        db = this.getReadableDatabase();
        Cursor c=db.rawQuery("Select * from Customer where " + NAME_COL + "='" + user + "'",null);
        if(c.getCount() <= 0) {
            c.close();
            db.close();
            return false;
        } else {
            c.close();
            db.close();
            return true;
        }
    }
    public String getCustEmail(String user) {
        db = this.getReadableDatabase();
        Cursor c=db.rawQuery("Select * from Customer where " + NAME_COL + "='" + user + "'",null);
        int IEmail=c.getColumnIndex(Email_COL);
        if(c!=null)
        {
            c.moveToFirst();
            String data=c.getString(IEmail);
            return data;
        }

        return null;
    }
    public Boolean cmprcust_userpass(String user,String pass) {
        String[] columns = {
                ID_COL
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = NAME_COL + " = ?" + " AND " + Password_COL + " = ?";
        String[] selectionArgs = {user, pass};
        Cursor cursor = db.query(TABLE_NAME, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }
    public void updateCustomer(int id,String name, String email, boolean gender, String password, String phone, String dob) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME_COL, name);
        cv.put(Email_COL, email);
        cv.put(Gender_COL, gender);
        cv.put(Password_COL,password);
        cv.put(Phonenumber_COL, phone);
        cv.put(Dob_COL, dob);
        db.update(TABLE_NAME, cv, ID_COL+"="+id,null);
        db.close();
    }
    public void deleteCustomer(int id) {
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID_COL+"="+id,null);
        db.close();
    }


}