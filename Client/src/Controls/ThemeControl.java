/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controls;

import Zeichenobjekte.Images;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Jan Koschke
 */
public class ThemeControl {
    
    String path = "resource//theme//";
    ArrayList<Images> themelist;
    Images aktuellesTheme = null;
    
    public ThemeControl(String themepfad) {
        themelist = new ArrayList<>();
        path = themepfad;
        try {
            ThemesEinlesen();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Es sind nicht alle Spieledateien vorhanden.\nBitte laden Sie sich das Spiel erneut herunter!", "Fehlende Dateien", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        };
        
        if (themelist.size() > 0) {
            aktuellesTheme = themelist.get(0);
        } else {
            JOptionPane.showMessageDialog(null, "Es sind nicht alle Spieledateien vorhanden.\nBitte laden Sie sich das Spiel erneut herunter!", "Fehlende Dateien", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
    
    public void ThemesEinlesen() {
        File a = new File(path);
        for (String ab : a.list()) {
            File b = new File(path + "//" + ab);
            if (b.isDirectory()) {
                Images img = new Images(ab, b.getPath());
                themelist.add(img);
            }
        }
        
    }
    
    public void nextTheme() {
        int index = themelist.indexOf(aktuellesTheme);
        if (index >= themelist.size() - 1) {
            index = 0;
        } else {
            index++;
        }
        aktuellesTheme = themelist.get(index);
        
    }
    
    public String getPath() {
        return path;
    }
    
    public ArrayList<Images> getThemelist() {
        return themelist;
    }
    
    public Images getAktuellesTheme() {
        return aktuellesTheme;
    }
    
}
