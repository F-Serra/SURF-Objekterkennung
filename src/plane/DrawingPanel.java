package plane;

import javax.swing.*;

import org.opencv.features2d.KeyPoint;
import java.awt.*;
import java.util.LinkedList;

public class DrawingPanel extends JPanel{
	
	Image img1 = null;
	Image img2 = null;
	int borderx = 8;
	int bordery = 14;
	LinkedList<KPMatch> matchL = new LinkedList<KPMatch>();
	LinkedList<KPMatch> goodmatchL = new LinkedList<KPMatch>();
	LinkedList<org.opencv.core.Point> hitL = new LinkedList<org.opencv.core.Point>();
	double threshold = 1;
	Statistics stats = new Statistics();
	int minN = 200;
	double varTrsh = 500;
	boolean hitbox = true;
	
	public DrawingPanel(){}

	public void paintComponent(Graphics g) {
	     super.paintComponent(g);
	     //draw images
	     if (!(img1==null||img2==null)){	    	
	     g.drawImage(this.img1, borderx, bordery, this);
	     g.drawImage(this.img2, borderx+img1.getWidth(this), bordery, this);
	     }
	     //draw matches
	     for(int i = 0; i<matchL.size(); i++){
	    	// System.out.println(""+matchL.get(i).getObjKP().angle);
	    	int objx = (int) matchL.get(i).getObjKP().pt.x;
	    	int objy = (int) matchL.get(i).getObjKP().pt.y;
	    	int scenex = (int) matchL.get(i).getSceneKP().pt.x;
	    	int sceney = (int) matchL.get(i).getSceneKP().pt.y;
	    	float dist = matchL.get(i).getDist();
	    	if(dist<=threshold){
	    	g.setColor(distanceToColor(dist));
	    	g.drawLine(borderx+objx, bordery+objy, borderx+img1.getWidth(this)+scenex, bordery+sceney);
	    	}
	     }
	     //draw hitbox
	     if(hitbox && (hitL.size()>0)){
	     g.setColor(Color.GREEN);
	     g.drawLine(borderx+img1.getWidth(this) + (int)hitL.get(0).x,bordery+(int) hitL.get(0).y,
	    		 borderx+img1.getWidth(this) + (int)hitL.get(1).x, bordery+(int)hitL.get(1).y);
	     g.drawLine(borderx+img1.getWidth(this) + (int)hitL.get(1).x, bordery+(int)hitL.get(1).y,
	    		 borderx+img1.getWidth(this) + (int)hitL.get(2).x, bordery+(int)hitL.get(2).y);
	     g.drawLine(borderx+img1.getWidth(this) + (int)hitL.get(2).x, bordery+(int)hitL.get(2).y,
	    		 borderx+img1.getWidth(this) + (int)hitL.get(3).x, bordery+(int)hitL.get(3).y);
	     g.drawLine(borderx+img1.getWidth(this) + (int)hitL.get(3).x,bordery+ (int)hitL.get(3).y,
	    		 borderx+img1.getWidth(this) + (int)hitL.get(0).x, bordery+(int)hitL.get(0).y);
	     
	     for(int i =0; i<goodmatchL.size();i++){
	    	 int objx = (int) goodmatchL.get(i).getObjKP().pt.x;
	    	 int objy = (int) goodmatchL.get(i).getObjKP().pt.y;
	    	 int scenex = (int) goodmatchL.get(i).getSceneKP().pt.x;
	    	 int sceney = (int) goodmatchL.get(i).getSceneKP().pt.y;
	    	 g.setColor(Color.GREEN);
		     g.drawLine(borderx+objx, bordery+objy, borderx+img1.getWidth(this)+scenex, bordery+sceney);
	     }
	     }
	     
	}
	
	public void setImages(Image obj,Image scene){
		this.img1 = obj;
		this.img2 = scene;
		repaint();
	}
	
	public void drawMatches(LinkedList<KPMatch> matchList){
		this.matchL = matchList;
		repaint();
	}
	
	public void setThreshold(double th){
		this.threshold = th;
		repaint();
	}
	
	private Color distanceToColor(float dist){
		float f;
		if (dist >0.5f) f = 0.5f;
		else f = dist/0.5f;
		int red = (int) (255*(1-f));
		int blue = (int) (255*f);
		Color col = new Color(red,0,blue);
		return col;
	}
}
