/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Zeichenobjekte;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 *
 * @author Jan Koschke
 */
public class CButton {
    
    private int x, y, width, height, border;
    private String text;
    private Font fo;
    private Color textcolor = Color.BLACK, bordercolor = Color.BLACK, backgroundcolor = null;
    
    public CButton(String text, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
    }
    
    public int getX() {
        return x;
    }
    
    public int getTextX(Graphics zf) {
        return (int) ((x + getWidth() / 2) - (getTextWidth(zf) / 2));
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getTextY() {
        return (int) y + height - 10;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getTextWidth(Graphics zeichenflaeche) {
        return zeichenflaeche.getFontMetrics(getFo()).stringWidth(text);
    }
    
    public void setWidth(int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
    
    public int getBorder() {
        return border;
    }
    
    public void setBorder(int border) {
        this.border = border;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public Font getFo() {
        return fo;
    }
    
    public void setFo(Font fo) {
        this.fo = fo;
    }
    
    public Color getTextcolor() {
        return textcolor;
    }
    
    public void setTextcolor(Color textcolor) {
        this.textcolor = textcolor;
    }
    
    public Color getBordercolor() {
        return bordercolor;
    }
    
    public void setBordercolor(Color bordercolor) {
        this.bordercolor = bordercolor;
    }
    
    public Color getBackgroundcolor() {
        return backgroundcolor;
    }
    
    public void setBackgroundcolor(Color backgroundcolor) {
        this.backgroundcolor = backgroundcolor;
    }
    
    public boolean clicked(java.awt.event.MouseEvent evt) {
        boolean erg = false;
        if (evt.getX() >= getX() && evt.getX() <= getX() + getWidth()
                && evt.getY() >= getY() && evt.getY() <= getY() + getHeight()) {
            erg = true;
        }
        return erg;
    }
    
    public void draw(Graphics zeichenflaeche) {
        zeichenflaeche.setFont(fo);
        zeichenflaeche.setColor(textcolor);
        zeichenflaeche.drawString(getText(), getTextX(zeichenflaeche), getTextY());
        zeichenflaeche.setColor(bordercolor);
        zeichenflaeche.drawRect(x, y, width, height);
    }
    
}
