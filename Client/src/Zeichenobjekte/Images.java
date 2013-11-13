package Zeichenobjekte;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author Jan Koschke
 */
public class Images {

    private Image waagrecht, senkrecht, kHoch, kRunter, kLinks, kRechts;
    private Image sHoch, sRunter, sLinks, sRechts;
    private Image rLinksUnten, rRechtsUnten, rRechtsOben, rLinksOben;
    private Image tonan, tonaus, wall, ziel;

    
    private String themename = "";

    public Images(String name, String pfad) {
        this.themename = name;
        waagrecht = (new ImageIcon(pfad + "//gerade_links_rechts.png")).getImage();
        senkrecht = (new ImageIcon(pfad + "/gerade_oben_unten.png")).getImage();
        kHoch = (new ImageIcon(pfad + "//kopf_oben.png")).getImage();
        kRunter = (new ImageIcon(pfad + "//kopf_unten.png")).getImage();
        kLinks = (new ImageIcon(pfad + "//kopf_links.png")).getImage();
        kRechts = (new ImageIcon(pfad + "//kopf_rechts.png")).getImage();
        sHoch = (new ImageIcon(pfad + "//schwanz_unten.png")).getImage();
        sRunter = (new ImageIcon(pfad + "//schwanz_oben.png")).getImage();
        sLinks = (new ImageIcon(pfad + "//schwanz_Rechts.png")).getImage();
        sRechts = (new ImageIcon(pfad + "//schwanz_Links.png")).getImage();
        rLinksUnten = (new ImageIcon(pfad + "//kurve_links_unten.png")).getImage();
        rRechtsUnten = (new ImageIcon(pfad + "//kurve_rechts_unten.png")).getImage();
        rRechtsOben = (new ImageIcon(pfad + "//kurve_rechts_oben.png")).getImage();
        rLinksOben = (new ImageIcon(pfad + "//kurve_links_oben.png")).getImage();
        tonan = (new ImageIcon(pfad + "//sound_on.png")).getImage();
        tonaus = (new ImageIcon(pfad + "//sound_off.png")).getImage();
        wall = (new ImageIcon(pfad + "//wall.png")).getImage();
        ziel = (new ImageIcon(pfad + "//goal.png")).getImage();
        System.out.println("Themepack: " + themename +  " geladen..");
    }

    public Image getTonan() {
        return tonan;
    }

    public Image getTonaus() {
        return tonaus;
    }

    public String getThemename() {
        return themename;
    }

    public Image getWaagrecht() {
        return waagrecht;
    }

    public Image getZiel() {
        return ziel;
    }

    public Image getSenkrecht() {
        return senkrecht;
    }

    public Image getkHoch() {
        return kHoch;
    }

    public Image getkRunter() {
        return kRunter;
    }

    public Image getkLinks() {
        return kLinks;
    }

    public Image getkRechts() {
        return kRechts;
    }

    public Image getsHoch() {
        return sHoch;
    }

    public Image getsRunter() {
        return sRunter;
    }

    public Image getsLinks() {
        return sLinks;
    }

    public Image getsRechts() {
        return sRechts;
    }

    public Image getrLinksUnten() {
        return rLinksUnten;
    }

    public Image getrRechtsUnten() {
        return rRechtsUnten;
    }

    public Image getrRechtsOben() {
        return rRechtsOben;
    }

    public Image getrLinksOben() {
        return rLinksOben;
    }
    
    public Image getWall() {
        return wall;
    }

}
