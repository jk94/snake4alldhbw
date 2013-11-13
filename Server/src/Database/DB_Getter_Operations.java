package Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Jan Koschke
 */
public class DB_Getter_Operations {

    public static boolean isUserValid(DB_Connect dbc, String email, String pw) {
        PreparedStatement stmt = null;
        boolean eerg = false;
        try {
            stmt = dbc.getTheConnection().prepareStatement("SELECT * FROM snakedhbw.u_user WHERE u_email = ?");
            stmt.setString(1, email);

            ResultSet erg = dbc.executeSQLQuery(stmt);

            if (erg != null) {
                erg.first();

                if (erg.getString("u_pw").equals(pw)) {
                    System.out.println("User: " + erg.getString("u_email"));
                    System.out.println("Pass OK");
                    eerg = true;
                } else {
                    System.err.println("Pass not OK");
                }
            }
        } catch (SQLException ex) {
        }
        return eerg;
    }

    public static int getUserID(DB_Connect dbc, String user, String pw) {
        PreparedStatement stmt = null;
        int eerg = -1;
        try {
            stmt = dbc.getTheConnection().prepareStatement("SELECT u_id FROM snakedhbw.u_user WHERE u_email = ? AND snakedhbw.u_pw = ?");
            stmt.setString(1, user);
            stmt.setString(2, pw);

            ResultSet erg = dbc.executeSQLQuery(stmt);

            if (erg != null) {
                erg.first();
                System.out.println(erg.getInt("u_id"));
                eerg = erg.getInt("u_id");
            }
        } catch (SQLException ex) {
        }
        return eerg;
    }

}
