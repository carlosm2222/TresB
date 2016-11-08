package com.example.fdope.tresb.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

/**
 * Created by SS on 04-10-2016.
 */

public class DB implements Runnable {

    private Connection c;
    private String host = "192.168.1.31";
    private String db = "TresB";
    private int port = 5432;
    private String user = "postgres";
    private String pass = "tallerbd";
    private String url = "jdbc:postgresql://%s:%d/%s";

    public DB() {
        super();
        this.url = String.format(this.url, this.host, this.port, this.db);

        this.conectarBd();
        // this.desconectarBd();
    }

    @Override
    public void run() {
        try {
            Class.forName("org.postgresql.Driver");
            this.c = DriverManager.getConnection(this.url, this.user, this.pass);
        } catch (Exception e) {

        }
    }


    public Connection connect(){
        return this.c;
    }

    private void conectarBd(){
        Thread thread = new Thread(this);
        thread.start();

        try {
            thread.join();

        }catch (Exception e){

        }

    }
    public void desconectarBd(){
        if (this.c != null)
        {
            try {
                this.c.close();
            }catch (Exception e){

            }finally {
                this.c = null;
            }

        }
    }

    public ResultSet select(String query){
        this.conectarBd();
        ResultSet resultSet = null;
        try {
            resultSet = new ExecuteDB(this.c,query).execute().get();
        }catch (Exception e){

        }
        return  resultSet;
    }

    public ResultSet execute(String query){
        this.conectarBd();
        ResultSet resultSet = null;
        try {
            resultSet = new ExecuteDB(this.c,query).execute().get();
        }catch (Exception e){

        }
        return  resultSet;
    }
}
