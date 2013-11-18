/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author User
 */
public class DB_Setter_Operations {

    public static void submitHighscore(DB_Connect dbc, int u_id, int punkte) {
        System.out.println("SubmitHighscore");
        PreparedStatement stmt = null;
        try {
            System.out.println(punkte);
            stmt = dbc.getTheConnection().prepareStatement("INSERT INTO snakedhbw.highscores (score, user_id) VALUES (?,?)");
            stmt.setInt(1, punkte);
            stmt.setInt(2, u_id);
        } catch (SQLException ex) {
        }
        dbc.executeSQLInsert(stmt);
    }

    public static void setLoginKey(DB_Connect dbc, int u_id, String auth) {
        System.out.println("SetLoginKey");
        PreparedStatement stmt = null;
        try {
            stmt = dbc.getTheConnection().prepareStatement("UPDATE snakedhbw.u_user SET u_gamekey = ? WHERE u_id = ? LIMIT 1");
            stmt.setString(1, auth);
            stmt.setInt(2, u_id);
        } catch (SQLException ex) {
            System.err.println("LogInKeySet: " + ex.getMessage());
        }
        dbc.executeSQLInsert(stmt);
    }

}
