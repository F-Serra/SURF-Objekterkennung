package plane;

import org.opencv.features2d.KeyPoint;

public class KPMatch {
	
	KeyPoint objKP;
	KeyPoint sceneKP;
	float dist;
	
	public KPMatch(KeyPoint kp1, KeyPoint kp2,float distance){
		this.objKP = kp1;
		this.sceneKP = kp2;
		this.dist = distance;
	}

	public KeyPoint getObjKP() {
		return objKP;
	}

	public void setObjKP(KeyPoint objKP) {
		this.objKP = objKP;
	}

	public KeyPoint getSceneKP() {
		return sceneKP;
	}

	public void setSceneKP(KeyPoint sceneKP) {
		this.sceneKP = sceneKP;
	}

	public float getDist() {
		return dist;
	}

	public void setDist(float dist) {
		this.dist = dist;
	}

}
