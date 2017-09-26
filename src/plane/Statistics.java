package plane;

import java.util.LinkedList;

public class Statistics {
	
	int n = 0;
	int meanX;
	int meanY;
	double stdev = 1000d;
	
	public void getStats(LinkedList<KPMatch> matchlist, double trsh){
		
		int sumX = 0;
		int sumY = 0;
		n = 0;
		for(int i = 0; i<matchlist.size();i++){
			KPMatch kp = matchlist.get(i);
			if (kp.dist<=trsh){
		    n++;
			sumX = sumX + (int) kp.getSceneKP().pt.x;
			sumY = sumY + (int) kp.getSceneKP().pt.y;
			}			
		}
		
		if(n>0){
		meanX = (int) (sumX/n);
		meanY = (int) (sumY/n);
		
		double varsum = 0d;
		for(int i = 0; i<matchlist.size();i++){
			KPMatch kp = matchlist.get(i);
			if (kp.dist<=trsh){
			int xPos = (int)kp.getSceneKP().pt.x;
			int yPos = (int)kp.getSceneKP().pt.y;
			double distance = Math.pow((meanX - xPos),2) + Math.pow((meanY - yPos),2);
			varsum = varsum + distance;	
			}
		}
		stdev = Math.sqrt(varsum/n);
		}
		else{stdev = 500;}
	}

}
