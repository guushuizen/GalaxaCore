package me.iamguus.galaxacore.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

/**
 * Created by Guus on 3-2-2016.
 */
public class MySQLHandler {

    private static MySQLHandler instance;

    private Connection conn;

    public void setup() {
        String ip = ConfigUtil.get().getConfig().getString("mysql.ip");
        String port = ConfigUtil.get().getConfig().getString("mysql.port");
        String db = ConfigUtil.get().getConfig().getString("mysql.database");
        String usr = ConfigUtil.get().getConfig().getString("mysql.username");
        String pass = ConfigUtil.get().getConfig().getString("mysql.password");

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + db, usr, pass);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("Successfully connected to the database!");
    }

    public Connection getConnection() {
        return conn;
    }

    public static MySQLHandler get() {
        if (instance == null) {
            instance = new MySQLHandler();
        }

        return instance;
    }
}
