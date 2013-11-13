package Zeichenobjekte;

/**
*
* @author Jan Koschke
*
*/

public class Feld{
	
	private boolean blocked;
	private int x, y;
	
	public Feld(int x, int y, boolean pactive){
		this.x = x;
		this.y = y;
		this.blocked = pactive;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public boolean getBlocked(){
		return blocked;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public void setBlocked(boolean pactive){
		this.blocked = pactive;
	}
	
	public void activate(){
		this.blocked = true;
	}
	
	public void deactivate(){
		this.blocked = false;
	}
	
	
}