package com.example.myapplication.data;

import android.content.Context;

import com.example.myapplication.data.model.LoggedInUser;
import com.example.myapplication.ui.login.LoginViewModel;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;
    private boolean log;
    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }
    public interface SimpleCallback<T> {
        void callback(T data);
    }
    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if(instance == null){
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public void login(Context context, String username, String password, LoginViewModel.SimpleCallback2<Result<LoggedInUser>> finished) {
        // handle login
        dataSource.login(context,username,password,new SimpleCallback<Result<LoggedInUser>>() {
            @Override
            public void callback(Result<LoggedInUser> result) {
                if (result instanceof Result.Success) {
                    setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
                }
                finished.callback12(result);
            }

        });


    }

}