package com.example.fdope.tresb.DB;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * Created by SS on 04-10-2016.
 */

public class ExecuteDB extends AsyncTask<String,Void,ResultSet> {

    private Connection c;
    private String query;

    public ExecuteDB(Connection c, String query) {
        this.c = c;
        this.query = query;
    }

    @Override
    protected ResultSet doInBackground(String... params) {
        ResultSet resultSet = null;
        try {
                resultSet = c.prepareStatement(query).executeQuery();
        }catch (Exception e){

        }finally {
            try
            {
                c.close();
            }catch (Exception ex){

            }
        }
        return resultSet ;
    }
}
