package Controls;

import static Enums.EnumDirection.HOCH;
import static Enums.EnumDirection.LINKS;
import static Enums.EnumDirection.RECHTS;
import static Enums.EnumDirection.RUNTER;
import Standardpackage.Punkte;
import Zeichenobjekte.CButton;
import Zeichenobjekte.Feld;
import Zeichenobjekte.Images;
import Zeichenobjekte.Schlange;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Jan Koschke
 */
public class Zeichencontrol {

    private Graphics zeichenflaeche;
    private int pixelgroese, spielfeldX, spielfeldY;
    private Images img;
    private Control cnt;

    public Zeichencontrol(Graphics zfl, int pxl, int sfX, int sfY, Images img, Control cnt) {
        this.zeichenflaeche = zfl;
        this.pixelgroese = pxl;
        this.spielfeldX = sfX;
        this.spielfeldY = sfY;
        this.img = img;
        this.cnt = cnt;
    }

    public void setImg(Images img) {
        this.img = img;
    }

    public void zeichneFeld(Feld[][] Spielfeld) {
        loescheAnzeigekomplett();
        zeichneUndsetzeRand(Spielfeld);
    }

    public void loescheAnzeigekomplett() {
        zeichenflaeche.clearRect(0, 0, spielfeldX, spielfeldY);
    }

    private void zeichneUndsetzeRand(Feld[][] Spielfeld) {
        zeichenflaeche.setColor(Color.BLACK);
        Graphics2D g2d = (Graphics2D) zeichenflaeche;
        for (int i = 0; i < spielfeldX / pixelgroese - 1; i++) {
            if (i == 0 || i == Spielfeld[i].length - 1) {
                for (int i2 = 0; i2 <= Spielfeld[i].length - 1; i2++) {
                    Spielfeld[i][i2].activate();
                    g2d.drawImage(img.getWall(), Spielfeld[i][i2].getX(), Spielfeld[i][i2].getY(), null);
                }

            } else {
                Spielfeld[i][0].activate();
                g2d.drawImage(img.getWall(), Spielfeld[i][0].getX(), Spielfeld[i][0].getY(), null);
                Spielfeld[i][Spielfeld[i].length - 1].activate();
                g2d.drawImage(img.getWall(), Spielfeld[i][Spielfeld[i].length - 1].getX(), Spielfeld[i][Spielfeld[i].length - 1].getY(), null);
            }
        }
    }

    public void loescheAnzeigeSchlange(ArrayList<Feld> schlangenliste) {
        for (Feld f : schlangenliste) {
            zeichenflaeche.clearRect(f.getX(), f.getY(), pixelgroese, pixelgroese);
        }

    }

    public void loescheAnzeigeSchlange(Feld letztesFeld) {
        zeichenflaeche.clearRect(letztesFeld.getX(), letztesFeld.getY(), pixelgroese, pixelgroese);
    }

    public void loescheButton(CButton bt) {
        zeichenflaeche.clearRect(bt.getX(), bt.getY(), bt.getWidth() + 1, bt.getHeight() + 1);
    }

    public void zeichneSchlange(Schlange sSnake) {
        Graphics2D g2d = (Graphics2D) zeichenflaeche;
        int sz = sSnake.getSchlangenliste().size();
        loescheAnzeigeSchlange(sSnake.getSchlangenliste());
        for (int i = 1; i < sSnake.getSchlangenliste().size() - 1; i++) {
            g2d.drawImage(getSchlangenteil(sSnake.getSchlangenliste(), i), sSnake.getSchlangenliste().get(i).getX(), sSnake.getSchlangenliste().get(i).getY(), null);
        }
        g2d.drawImage(getSchlangenkopf(sSnake.getDirection()), sSnake.getKopf().getX(), sSnake.getKopf().getY(), null);
        g2d.drawImage(getSchlangenschwanz(sSnake.getSchlangenliste().get(sz - 1), sSnake.getSchlangenliste().get(sz - 2)), sSnake.getSchlangenliste().get(sz - 1).getX(), sSnake.getSchlangenliste().get(sz - 1).getY(), null);
    }

    public void zeichneSchlangenrumpf(ArrayList<Feld> schlangenliste) {

    }

    private Image getSchlangenkopf(Enums.EnumDirection dir) {
        Image erg = null;
        switch (dir) {
            case HOCH:
                erg = img.getkHoch();
                break;
            case RUNTER:
                erg = img.getkRunter();
                break;
            case LINKS:
                erg = img.getkLinks();
                break;
            case RECHTS:
                erg = img.getkRechts();
                break;
        }

        return erg;
    }

    private Image getSchlangenschwanz(Feld schwanzfeld, Feld davor) {
        Image erg = null;
        if (davor.getX() < schwanzfeld.getX()) {
            erg = img.getsRechts();
        }
        if (davor.getX() > schwanzfeld.getX()) {
            erg = img.getsLinks();
        }
        if (davor.getY() < schwanzfeld.getY()) {
            erg = img.getsRunter();
        }
        if (davor.getY() > schwanzfeld.getY()) {
            erg = img.getsHoch();
        }

        return erg;
    }

    private Image getSchlangenteil(ArrayList<Feld> schlangenliste, int index) {
        Image erg = null;
        Feld davor = schlangenliste.get(index - 1), danach = schlangenliste.get(index + 1), selber = schlangenliste.get(index);
        if ((davor.getX() < selber.getX() && danach.getX() > selber.getX()) || (davor.getX() > selber.getX() && danach.getX() < selber.getX())) {
            erg = img.getWaagrecht();
        }
        if (((davor.getY() < selber.getY()) && danach.getY() > selber.getY()) || ((davor.getY() > selber.getY()) && danach.getY() < selber.getY())) {
            erg = img.getSenkrecht();
        }

        if ((davor.getX() < selber.getX() && danach.getY() < selber.getY()) || (davor.getY() < selber.getY() && danach.getX() < selber.getX())) {
            erg = img.getrLinksOben();
        }
        if ((davor.getX() < selber.getX() && danach.getY() > selber.getY()) || (davor.getY() > selber.getY() && danach.getX() < selber.getX())) {
            erg = img.getrLinksUnten();
        }
        if ((davor.getX() > selber.getX() && danach.getY() < selber.getY()) || (davor.getY() < selber.getY() && danach.getX() > selber.getX())) {
            erg = img.getrRechtsOben();
        }
        if ((davor.getX() > selber.getX() && danach.getY() > selber.getY()) || (davor.getY() > selber.getY() && danach.getX() > selber.getX())) {
            erg = img.getrRechtsUnten();
        }
        return erg;
    }

    public void zeichneZiel(Feld aktuellesZiel) {
        Graphics2D g2d = (Graphics2D) zeichenflaeche;
        g2d.drawImage(img.getZiel(), aktuellesZiel.getX(), aktuellesZiel.getY(), pixelgroese, pixelgroese, null);

    }

    public void zeichnePunkte(Punkte pkt, Feld[][] Spielfeld) {
        loescheAnzeigePunkte(Spielfeld);
        zeichenflaeche.setColor(Color.WHITE);
        Font f = new Font("serif", Font.BOLD, 10);
        zeichenflaeche.setFont(f);
        zeichenflaeche.drawString("Punkte: " + pkt.getPunktezaehler(), Spielfeld[0][Spielfeld[0].length - 1].getX() + 10, Spielfeld[0][Spielfeld[0].length - 1].getY() + 10);
    }

    public void loescheAnzeigePunkte(Feld[][] Spielfeld) {
        Graphics2D g2d = (Graphics2D) zeichenflaeche;
        Feld[] unterezeile = new Feld[Spielfeld.length - 3];
        for (int i = 0; i < unterezeile.length; i++) {
            unterezeile[i] = Spielfeld[i][Spielfeld.length - 1];
            zeichenflaeche.clearRect(unterezeile[i].getX(), unterezeile[i].getY(), pixelgroese, pixelgroese);
            g2d.drawImage(img.getWall(), unterezeile[i].getX(), unterezeile[i].getY(), pixelgroese, pixelgroese, null);
        }
    }

    public void zeichneGameOverMessage(Punkte pkt) {
        zeichenflaeche.setColor(Color.WHITE);
        Font f = new Font("serif", Font.BOLD, 50);
        zeichenflaeche.setFont(f);
        int x = zeichenflaeche.getFontMetrics().stringWidth("GAME OVER!");
        zeichenflaeche.drawString("GAME OVER!", spielfeldX / 2 - x / 2, 100);

        f = new Font("serif", Font.BOLD, 30);
        zeichenflaeche.setFont(f);
        x = zeichenflaeche.getFontMetrics().stringWidth("Erreichte Punktzahl: " + pkt.getPunktezaehler());
        zeichenflaeche.drawString("Erreichte Punktzahl: " + pkt.getPunktezaehler(), spielfeldX / 2 - x / 2, spielfeldY / 2 - 30);
    }

    public void zeichneButton(CButton bt) {
        zeichenflaeche.clearRect(bt.getX(), bt.getY(), bt.getWidth(), bt.getHeight());
        bt.draw(zeichenflaeche);
    }

    public void zeichneStartUp() {
        Graphics2D g2d = (Graphics2D) zeichenflaeche;
        ImageIcon tb_src = new ImageIcon("resources//images//Titelbild.png");
        Image img = tb_src.getImage();
        double y = img.getHeight(null);
        double x = img.getWidth(null);
        g2d.drawImage(img, spielfeldX / 4, 200, spielfeldX / 2, (int) (y / x * spielfeldX / 2), null);
    }

    public void zeichnePauseMessage() {
        zeichenflaeche.setColor(Color.WHITE);
        Font f = new Font("serif", Font.BOLD, 50);
        zeichenflaeche.setFont(f);
        int x = zeichenflaeche.getFontMetrics().stringWidth("PAUSE");
        zeichenflaeche.drawString("PAUSE", spielfeldX / 2 - x / 2, 100);
    }

    public void repaintSpielfeld() {
        this.zeichneFeld(cnt.getSpielfeld());
        this.zeichneZiel(cnt.getAktuelleZiel());
        this.zeichnePunkte(cnt.getPunkte(), cnt.getSpielfeld());
        this.zeichneSchlange(cnt.getSchlange());
    }

    public void zeichneTonIcon(Point pt) {
        Graphics2D g2d = (Graphics2D) zeichenflaeche;
        Image bild = null;
        if (this.cnt.getTon()) {
            //ic_ton = new ImageIcon("resources//images//Ton_an.png");
            bild = img.getTonan();
        } else {
            //ic_ton = new ImageIcon("resources//images//Ton_aus.png");
            bild = img.getTonaus();
        }
        zeichenflaeche.setColor(Color.black);
        //TODO Hier muss dann der Rand neu gezeichnet werden.
        zeichenflaeche.fillRect(pt.x, pt.y, 16, 16);
        g2d.drawImage(bild, pt.x, pt.y, 16, 16, null);
    }
}
