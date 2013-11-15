package Controls;

import Connection.Connect;
import Zeichenobjekte.Schlange;
import Zeichenobjekte.Feld;
import Enums.EnumDirection;
import Enums.EnumGameStatus;
import Enums.EnumSchwierigkeit;
import MessagePackage.Enums.MessageType;
import MessagePackage.Message;
import Standardpackage.Anmeldung;
import Standardpackage.GameStatus;
import Standardpackage.MainGUI;
import Standardpackage.Punkte;
import Standardpackage.Schwierigkeit;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author Jan Koschke
 */
public class Control {

    private final MainGUI mgui;
    private Schlange sSnake;
    private Feld[][] Spielfeld;
    private final Graphics zeichenflaeche;
    private Feld aktuellesZiel;
    int spielfeldX, spielfeldY;
    int pixelgroese;
    private Timer timer = null;
    private Punkte pkt = null;
    private final Zeichencontrol zcnt;
    private final ThemeControl tcnt;
    private Schwierigkeit schwierigkeit;
    private GameStatus gamestatus;
    private SoundController scnt;
    private boolean ton = true;
    private static String sessionkey = "";
    private static String user;
    private static String pw;

    public Control(MainGUI mGUI) {
        this.mgui = mGUI;

        this.zeichenflaeche = this.mgui.getCanvas().getGraphics();

        spielfeldX = this.mgui.getCanvas().getWidth();
        spielfeldY = this.mgui.getCanvas().getHeight();
        pixelgroese = spielfeldX / 30;

        tcnt = new ThemeControl("resources//images//theme");
        zcnt = new Zeichencontrol(zeichenflaeche, pixelgroese, spielfeldX, spielfeldY, tcnt.getAktuellesTheme(), this);
        scnt = new SoundController(this);
        String[] amld = readAnmeldedaten();
        user = amld[0];
        pw = amld[1];

    }

    public static String[] getAnmeldedaten() {
        String[] s = new String[2];
        s[0] = user;
        s[1] = pw;
        return s;
    }

    public String getAuthKey() {
        return sessionkey;
    }

    public static void setAuthKey(String auth) {
        sessionkey = auth;
    }

    public String[] readAnmeldedaten() {
        String[] erg = new String[2];
        File f = new File("resources//login");
        try {
            if (f.exists()) {
                BufferedReader bfr = new BufferedReader(new FileReader(f));
                erg[0] = bfr.readLine();
                erg[1] = bfr.readLine();
            } else {
                Anmeldung a = new Anmeldung();
                JOptionPane.showMessageDialog(null, a);
                erg[0] = a.getEmail();
                erg[1] = a.getPW();
                //READ Anmeldedaten
                PrintWriter pw = new PrintWriter(f);
                pw.println(erg[0]);
                pw.println(erg[1]);
                pw.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return erg;
    }

    public void init() {
        gamestatus = new GameStatus(this);
    }

    public boolean getTon() {
        return this.ton;
    }

    public void setTon(boolean ton) {
        this.ton = ton;
    }

    public ThemeControl getThemeControl() {
        return this.tcnt;
    }

    public void toggleTon() {
        this.ton = !this.ton;
        zcnt.zeichneTonIcon(new Point(Spielfeld[Spielfeld.length - 1][Spielfeld.length - 1].getX(), Spielfeld[Spielfeld.length - 1][Spielfeld.length - 1].getY()));
        System.out.println(this.getClass().toString() + ": Tonwechsel");
    }

    public int getFeldgroese() {
        return this.pixelgroese;
    }

    public int getWidth() {
        return this.spielfeldX;
    }

    public int getHeight() {
        return this.spielfeldY;
    }

    public Schlange getSchlange() {
        return this.sSnake;
    }

    public Feld[][] getSpielfeld() {
        return this.Spielfeld;
    }

    public Graphics getZeichenflaeche() {
        return this.zeichenflaeche;
    }

    public Feld getAktuelleZiel() {
        return this.aktuellesZiel;
    }

    public Zeichencontrol getZeichenControl() {
        return zcnt;
    }

    public void neuesSpiel() {
        if (timer != null) {
            timer.stop();
        }
        Spielfeld = new Feld[spielfeldX / pixelgroese - 1][spielfeldX / pixelgroese - 1];
        for (int i = 0; i < spielfeldX / pixelgroese - 1; i++) {
            for (int i2 = 0; i2 < spielfeldX / pixelgroese - 1; i2++) {
                Spielfeld[i][i2] = new Feld(i * pixelgroese, i2 * pixelgroese, false);
            }
        }
        zcnt.zeichneFeld(Spielfeld);
        sSnake = new Schlange(Spielfeld[(int) (Spielfeld.length / 2)][(int) (Spielfeld.length / 2)], this);
        zcnt.zeichneSchlange(sSnake);
        aktuellesZiel = generiereNeuesZiel();
        zcnt.zeichneZiel(aktuellesZiel);
        pkt = new Punkte();

        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                letSnakeRun();
            }
        });
        schwierigkeit = new Schwierigkeit(pkt, timer, EnumSchwierigkeit.NORMAL);
        zcnt.zeichneTonIcon(new Point(Spielfeld[Spielfeld.length - 1][Spielfeld.length - 1].getX(), Spielfeld[Spielfeld.length - 1][Spielfeld.length - 1].getY()));
    }

    public void gameOver() {
        gamestatus.GameOver();
    }

    public SoundController getScnt() {
        return scnt;
    }

    public void setScnt(SoundController scnt) {
        this.scnt = scnt;
    }

    public void SpielfeldClicked(java.awt.event.MouseEvent evt) {
        gamestatus.clicked(evt);
    }

    public void zielwurdegefressen() {
        scnt.playFood();
        aktuellesZiel = generiereNeuesZiel();
        pkt.ZielGefressen();

        zcnt.zeichnePunkte(pkt, Spielfeld);
        zcnt.zeichneZiel(aktuellesZiel);
        schwierigkeit.zielGefressen();
    }

    public void changeDirection(EnumDirection dir) {
        if (sSnake != null) {
            sSnake.setDirection(dir);
        }
    }

    public void start() {
        if (timer != null) {
            timer.start();
        }
        if (pkt != null) {
            pkt.TimerStart();
        }
    }

    public void pause() {
        if (timer != null) {
            timer.stop();
        }
        if (pkt != null) {
            pkt.TimerStop();
        }
    }

    public void restart() {
        neuesSpiel();
        gamestatus.setGameStatus(EnumGameStatus.PLAYING);
        start();
    }

    public void pauseCommand() {
        if (!gamestatus.equals(null)) {
            if (gamestatus.getGameStatus() == EnumGameStatus.PAUSE) {
                gamestatus.resume();
            } else {
                if (gamestatus.getGameStatus() == EnumGameStatus.PLAYING) {
                    gamestatus.Pause();
                }
            }

        }
    }

    public void pauseCommand(boolean set) {
        if (set) {
            gamestatus.Pause();
        } else {
            gamestatus.resume();
        }
    }

    public void stop() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
        if (pkt != null) {
            pkt.TimerStop();
        }
    }

    public Punkte getPunkte() {
        return pkt;
    }

    private Feld generiereNeuesZiel() {
        Feld erg = null;

        do {
            int rndx = (int) (Math.random() * (Spielfeld.length - 1 - 1) + 1);
            int rndy = (int) (Math.random() * (Spielfeld.length - 1 - 1) + 1);
            erg = Spielfeld[rndx][rndy];

        } while (!istZielfeldOK(erg));

        return erg;
    }

    private boolean istZielfeldOK(Feld fld) {
        boolean erg = true;

        for (int i = 0; i < Spielfeld.length; i++) {
            for (int i2 = 0; i2 < Spielfeld[i].length; i2++) {
                if (Spielfeld[i][i2].getX() == fld.getX() && Spielfeld[i][i2].getY() == fld.getY()) {
                    if (Spielfeld[i][i2].getBlocked()) {
                        erg = false;
                        break;
                    }
                }
            }
        }

        return erg;
    }

    public Feld gibAnliegendesFeld(Feld aFeld, EnumDirection dir) {
        int zaehler1 = 0, zaehler2 = 0;
        boolean gefunden = false;
        Feld erg = aFeld;
        for (int i = 0; i < Spielfeld.length; i++) {
            for (int i2 = 0; i2 < Spielfeld[i].length; i2++) {
                if (Spielfeld[i][i2].equals(aFeld)) {
                    zaehler1 = i;
                    zaehler2 = i2;
                    break;
                }
            }
            if (gefunden) {
                break;
            }
        }

        switch (dir) {
            case HOCH:
                zaehler2--;
                break;
            case RUNTER:
                zaehler2++;
                break;
            case LINKS:
                zaehler1--;
                break;
            case RECHTS:
                zaehler1++;
                break;
        }
        erg = Spielfeld[zaehler1][zaehler2];
        return erg;
    }

    public void letSnakeRun() {
        getSchlange().move();
        scnt.move();
    }
}
