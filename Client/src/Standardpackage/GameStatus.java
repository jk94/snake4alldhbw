package Standardpackage;

import Controls.Control;
import Enums.EnumGameStatus;
import Zeichenobjekte.CButton;
import Zeichenobjekte.Feld;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Jan Koschke
 */
public class GameStatus {

    private Enums.EnumGameStatus GS;
    private Control cnt;
    private CButton neuesSpiel, exit, themewechseln, resume;

    public GameStatus(Control ccnt) {
        this.cnt = ccnt;
        this.GS = Enums.EnumGameStatus.START;
        startup();
    }

    public Enums.EnumGameStatus getGameStatus() {
        return GS;
    }

    public void setGameStatus(Enums.EnumGameStatus gs) {
        this.GS = gs;
    }

    public void clicked(java.awt.event.MouseEvent evt) {
        if (evt.getButton() == 1) {
            switch (GS) {
                case GAMEOVER:
                    if (!neuesSpiel.equals(null)) {
                        if (neuesSpiel.clicked(evt)) {
                            cnt.neuesSpiel();
                            this.GS = EnumGameStatus.PLAYING;
                            cnt.start();
                            break;
                        }
                    }
                    if (!exit.equals(null)) {
                        if (exit.clicked(evt)) {
                            System.exit(0);
                            break;
                        }
                    }
                    if (!themewechseln.equals(null)) {
                        if (themewechseln.clicked(evt)) {
                            cnt.getThemeControl().nextTheme();
                            cnt.getZeichenControl().setImg(cnt.getThemeControl().getAktuellesTheme());
                            cnt.getZeichenControl().loescheButton(themewechseln);
                            themewechseln = getThemeWechsel();
                            cnt.getZeichenControl().zeichneButton(themewechseln);
                            break;
                        }
                    }
                    clickTestTon(evt);
                    break;
                case PAUSE:
                    if (!resume.equals(null)) {
                        if (resume.clicked(evt)) {
                            resume();
                            break;
                        }
                    }
                    if (!exit.equals(null)) {
                        if (exit.clicked(evt)) {
                            System.exit(0);
                            break;
                        }
                    }
                    if (!themewechseln.equals(null)) {
                        if (themewechseln.clicked(evt)) {
                            cnt.getThemeControl().nextTheme();
                            cnt.getZeichenControl().setImg(cnt.getThemeControl().getAktuellesTheme());
                            cnt.getZeichenControl().loescheButton(themewechseln);
                            themewechseln = getThemeWechsel();
                            cnt.getZeichenControl().zeichneButton(themewechseln);
                            break;
                        }
                    }
                    clickTestTon(evt);
                    break;
                case PLAYING:
                    clickTestTon(evt);
                    break;
                case START:
                    if (!neuesSpiel.equals(null)) {
                        if (neuesSpiel.clicked(evt)) {
                            cnt.neuesSpiel();
                            this.GS = EnumGameStatus.PLAYING;
                            cnt.start();
                            break;
                        }
                    }
                    if (!exit.equals(null)) {
                        if (exit.clicked(evt)) {
                            System.exit(0);
                            break;
                        }

                    }
                    if (!themewechseln.equals(null)) {
                        if (themewechseln.clicked(evt)) {
                            cnt.getThemeControl().nextTheme();
                            cnt.getZeichenControl().setImg(cnt.getThemeControl().getAktuellesTheme());
                            cnt.getZeichenControl().loescheButton(themewechseln);
                            themewechseln = getThemeWechsel();
                            cnt.getZeichenControl().zeichneButton(themewechseln);
                            break;
                        }
                    }
                    clickTestTon(evt);
                    break;
                default:
            }
        }
    }

    private void clickTestTon(java.awt.event.MouseEvent evt) {
        try {
            Feld[][] Spielfeld = cnt.getSpielfeld();
            Point pt = new Point(Spielfeld[Spielfeld.length - 1][Spielfeld.length - 1].getX(), Spielfeld[Spielfeld.length - 1][Spielfeld.length - 1].getY());
            if (evt.getX() >= pt.x && evt.getX() <= pt.x + cnt.getFeldgroese()
                    && evt.getY() >= pt.y && evt.getY() <= pt.y + cnt.getFeldgroese()) {
                cnt.toggleTon();
            }
        } catch (Exception ex) {

        }
    }

    public void Pause() {
        cnt.pause();
        cnt.getZeichenControl().zeichnePauseMessage();
        cnt.getPunkte().TimerStop();
        GS = EnumGameStatus.PAUSE;
        exit = getExitButton();
        themewechseln = getThemeWechsel();
        resume = getResumeSpielButton();

        cnt.getZeichenControl().zeichneButton(themewechseln);
        cnt.getZeichenControl().zeichneButton(resume);
        cnt.getZeichenControl().zeichneButton(exit);
    }

    public void GameOver() {
        cnt.stop();
        cnt.getZeichenControl().zeichneGameOverMessage(cnt.getPunkte());
        GS = EnumGameStatus.GAMEOVER;
        neuesSpiel = getNeuesSpielButton();
        exit = getExitButton();
        themewechseln = getThemeWechsel();

        cnt.getZeichenControl().zeichneButton(themewechseln);
        cnt.getZeichenControl().zeichneButton(neuesSpiel);
        cnt.getZeichenControl().zeichneButton(exit);
        cnt.getPunkte().gameOver(cnt.getAnmeldedaten());
    }

    public void startup() {
        cnt.getZeichenControl().zeichneStartUp();

        neuesSpiel = getNeuesSpielButton();
        exit = getExitButton();
        themewechseln = getThemeWechsel();
        this.GS = EnumGameStatus.START;
        cnt.getZeichenControl().zeichneButton(themewechseln);
        cnt.getZeichenControl().zeichneButton(neuesSpiel);
        cnt.getZeichenControl().zeichneButton(exit);
        //cnt.getZeichenControl().zeichneFeld(cnt.getSpielfeld());
    }

    public void resume() {
        cnt.getPunkte().TimerStart();
        this.GS = EnumGameStatus.PLAYING;
        cnt.getZeichenControl().repaintSpielfeld();
        cnt.start();

    }

    private CButton getNeuesSpielButton() {
        //spielfeldX / 2 - x / 2
        CButton erg = null;

        erg = new CButton("Neues Spiel", 0, 0, 0, 0);
        erg.setFo(new Font("serif", Font.BOLD, 20));
        erg.setBorder(1);
        erg.setTextcolor(Color.WHITE);
        erg.setBordercolor(Color.WHITE);
        erg.setWidth(cnt.getZeichenflaeche().getFontMetrics(erg.getFo()).stringWidth("Neues Spiel") + 10);
        erg.setHeight(cnt.getZeichenflaeche().getFontMetrics(erg.getFo()).getHeight() + 10);
        erg.setX(cnt.getWidth() / 2 - erg.getWidth() / 2 - 5);
        erg.setY((int) (cnt.getHeight() / 2) + 50);

        return erg;
    }

    private CButton getResumeSpielButton() {
        //spielfeldX / 2 - x / 2
        CButton erg = null;

        erg = new CButton("Fortsetzen", 0, 0, 0, 0);
        erg.setFo(new Font("serif", Font.BOLD, 20));
        erg.setBorder(1);
        erg.setTextcolor(Color.WHITE);
        erg.setBordercolor(Color.WHITE);
        erg.setWidth(cnt.getZeichenflaeche().getFontMetrics(erg.getFo()).stringWidth("Fortsetzen") + 10);
        erg.setHeight(cnt.getZeichenflaeche().getFontMetrics(erg.getFo()).getHeight() + 10);
        erg.setX(cnt.getWidth() / 2 - erg.getWidth() / 2 - 5);
        erg.setY((int) (cnt.getHeight() / 2) + 50);

        return erg;
    }

    private CButton getExitButton() {
        CButton erg = null;

        erg = new CButton("Exit", 0, 0, 0, 0);
        erg.setFo(new Font("serif", Font.BOLD, 20));
        erg.setBorder(1);
        erg.setBordercolor(Color.WHITE);
        erg.setTextcolor(Color.WHITE);
        erg.setWidth(neuesSpiel.getWidth());
        erg.setHeight(cnt.getZeichenflaeche().getFontMetrics(erg.getFo()).getHeight() + 10);
        erg.setX(cnt.getWidth() / 2 - erg.getWidth() / 2 - 5);
        erg.setY((int) (cnt.getHeight() / 2) + 50 + getNeuesSpielButton().getHeight() + 10);

        return erg;
    }

    private CButton getThemeWechsel() {
        CButton erg = null;

        erg = new CButton("Theme wechseln (" + cnt.getThemeControl().getAktuellesTheme().getThemename() + ")", 0, 0, 0, 0);
        erg.setFo(new Font("serif", Font.BOLD, 20));
        erg.setBorder(1);
        erg.setBordercolor(Color.WHITE);
        erg.setTextcolor(Color.WHITE);
        erg.setWidth(erg.getTextWidth(cnt.getZeichenflaeche()) + 10);
        erg.setHeight(cnt.getZeichenflaeche().getFontMetrics(erg.getFo()).getHeight() + 10);
        erg.setX(20);
        erg.setY(cnt.getHeight() - erg.getHeight() - 2 * cnt.getFeldgroese() - 10);

        return erg;
    }
}
