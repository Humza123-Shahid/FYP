package com.example.DriverApp.data;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.DriverApp.data.model.LoggedInUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource{
    boolean log=false;
    public void login(Context context, String username, String password, @NonNull LoginRepository.SimpleCallback<Result<LoggedInUser>> finishedCallback) {

        try {
            Query checkUser= FirebaseDatabase.getInstance().getReference("Driver").orderByChild("name").equalTo(username);
            // TODO: handle loggedInUser authentication
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Result<LoggedInUser> obj;
                    if(snapshot.exists()){
                        String systemPassword = snapshot.child(username).child("password").getValue(String.class);
                        if(systemPassword.equals(password)){
                            LoggedInUser User = new LoggedInUser(username,password);
                            obj= new Result.Success<>(User);
                            finishedCallback.callback(obj);
                        }
                        else {
                            obj=new Result.Error(new IOException("Error logging in"));
                            finishedCallback.callback(obj);
                        }
                    }
                    else {
                        obj=new Result.Error(new IOException("Error logging in"));
                        finishedCallback.callback(obj);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
                }

            });
//            SQLiteDatabaseClass db=new SQLiteDatabaseClass(context);
//            if(db.cmprcust_userpass(username,password))
//            {
//                LoggedInUser fakeUser =
//                        new LoggedInUser(
//                                username,
//                                password);
//                return new Result.Success<>(fakeUser);
//            }
//
//            return new Result.Error(new IOException("Error logging in"));

        } catch (Exception e) {
            Result<LoggedInUser> obj1;
            obj1=new Result.Error(new IOException("Error logging in",e));
            finishedCallback.callback(obj1);
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }

}