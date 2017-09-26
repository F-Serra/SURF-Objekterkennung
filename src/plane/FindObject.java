package plane;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.LinkedList;
import java.util.List;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class FindObject {
	
	float min;
	float max;
	float mean;
	float stdev;
	float matchThresh = 1f;
	double ransacThresh = 10d;
	Image objectImage;
	Image sceneImage;
	Mat objectMat;
	Mat sceneMat;
	LinkedList<KPMatch> kpMatches;
	LinkedList<KPMatch> goodMatches;
	double cannytrsh1 = 2.0;
	double cannytrsh2 = 2.0;
	
	
	public void createMatchList(String object, String scene, boolean useCanny) {
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
	    Mat img_object = new Mat();
	    Mat img_scene = new Mat();
	    if(useCanny){
	    	Mat grey_object = Highgui.imread(object, 0);
	    	Mat grey_scene = Highgui.imread(scene, 0);
	    	int meanGrey = (int) Core.mean(grey_scene).val[0];
	    	Mat grey_object_blur = new Mat();
	    	Mat grey_scene_blur = new Mat();
	    	
	    	Imgproc.blur(grey_object, grey_object_blur, new Size(3,3));
	    	Imgproc.blur(grey_scene, grey_scene_blur, new Size(3,3));
	    	Imgproc.Canny(grey_object_blur, img_object, cannytrsh1*meanGrey, cannytrsh2*meanGrey);
	    	Imgproc.Canny(grey_scene_blur, img_scene, cannytrsh1*meanGrey, cannytrsh2*meanGrey);
	    }
	    else{
	    img_object = Highgui.imread(object, 0); //0 = CV_LOAD_IMAGE_GRAYSCALE
	    img_scene = Highgui.imread(scene, 0);}
	    
	      
	    objectImage = toBufferedImage(img_object);
	    sceneImage = toBufferedImage(img_scene);
	    objectMat = img_object;
	    sceneMat = img_scene;
	    
	    FeatureDetector detector = FeatureDetector.create(FeatureDetector.SURF); //4 = SURF 

	    MatOfKeyPoint keypoints_object = new MatOfKeyPoint();
	    MatOfKeyPoint keypoints_scene  = new MatOfKeyPoint();

	    detector.detect(img_object, keypoints_object);
	    detector.detect(img_scene, keypoints_scene);
	    
	    DescriptorExtractor extractor = DescriptorExtractor.create(2); //2 = SURF;

	    Mat descriptor_object = new Mat();
	    Mat descriptor_scene = new Mat() ;

	    extractor.compute(img_object, keypoints_object, descriptor_object);
	    extractor.compute(img_scene, keypoints_scene, descriptor_scene);

	    DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
	    MatOfDMatch matches = new MatOfDMatch();

	    matcher.match(descriptor_object, descriptor_scene, matches);
	    List<DMatch> matchesList = matches.toList();
	    List<KeyPoint> kpObjectList = keypoints_object.toList();
	    List<KeyPoint> kpSceneList = keypoints_scene.toList();

	    this.max = 0f;
	    this.min = 100f;
	    
	    LinkedList<KPMatch> kpMatchesL = new LinkedList<KPMatch>();	    

	    for(int i = 0; i < keypoints_object.rows(); i++){
	    	KeyPoint objKP = kpObjectList.get(i);
	    	KeyPoint sceneKP = kpSceneList.get(matchesList.get(i).trainIdx);
	    	float distance = matchesList.get(i).distance;
	    	kpMatchesL.addLast(new KPMatch(objKP,sceneKP,distance));
	    	if(distance < min) min = distance;
	        if(distance > max) max = distance;
	       
	    }
	    this.kpMatches = kpMatchesL;
	    
	}
	
	public Image toBufferedImage(Mat m){
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if ( m.channels() > 1 ) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels()*m.cols()*m.rows();
        byte [] b = new byte[bufferSize];
        m.get(0,0,b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);  
        return image;

    }
	
	public LinkedList<KPMatch> getHits(){
		LinkedList<KPMatch> result = new LinkedList<KPMatch>();
		int maxSize = 0;
		
		for(int i = 0; i < this.kpMatches.size();i++){
			KeyPoint objectPt = this.kpMatches.get(i).getObjKP();
			KeyPoint scenePt = this.kpMatches.get(i).getSceneKP();
			int rotation = normRotation(Math.round(scenePt.angle - objectPt.angle));
			LinkedList<KPMatch> candidates = new LinkedList<KPMatch>();
			
			for(int j = i; j < this.kpMatches.size();j++){
				KeyPoint cObjectPt = this.kpMatches.get(j).getObjKP();
				KeyPoint cScenePt = this.kpMatches.get(j).getSceneKP();	
				int cRotation = normRotation(Math.round(cScenePt.angle - cObjectPt.angle));
			
				if(rotation == cRotation){
					candidates.add(this.kpMatches.get(j));
				}
			}
			
			if(candidates.size()>maxSize){
				result = candidates;
				maxSize = candidates.size();
			}
			
		}
		
		
		return result;
	}
	
	public int normRotation(int rot){
		int result;
		if(rot<0){
			result = 360-rot;
		}
		else result = rot;
		
		return result;
	}
	
	public LinkedList<Point> getHitbox(){
		LinkedList<Point> result = new LinkedList<Point>();
		LinkedList<Point> objList = new LinkedList<Point>();
	    LinkedList<Point> sceneList = new LinkedList<Point>();
		
	    if (goodMatches.size()>=4){
		for(int i = 0; i < goodMatches.size();i++){
			KPMatch match  = goodMatches.get(i);
			objList.add(match.getObjKP().pt);
			sceneList.add(match.getSceneKP().pt);
		}
		
		MatOfPoint2f obj = new MatOfPoint2f();
	    obj.fromList(objList);

	    MatOfPoint2f scene = new MatOfPoint2f();
	    scene.fromList(sceneList);
		
	    Mat hg = Calib3d.findHomography(obj, scene,Calib3d.RANSAC,ransacThresh);

	    Mat obj_corners = new Mat(4,1,CvType.CV_32FC2);
	    Mat scene_corners = new Mat(4,1,CvType.CV_32FC2);
	    	    
	    obj_corners.put(0, 0, new double[] {0,0});
	    obj_corners.put(1, 0, new double[] {objectMat.cols(),0});
	    obj_corners.put(2, 0, new double[] {objectMat.cols(),objectMat.rows()});
	    obj_corners.put(3, 0, new double[] {0,objectMat.rows()});
		
	    Core.perspectiveTransform(obj_corners,scene_corners, hg);
	    
	    Point upperleft = new Point(scene_corners.get(0, 0));
	    Point upperright = new Point(scene_corners.get(1, 0));
	    Point lowerleft = new Point(scene_corners.get(3, 0));
	    Point lowerright = new Point(scene_corners.get(2, 0));
	    result.add(upperleft);result.add(upperright);result.add(lowerright);result.add(lowerleft);
	    }
		return result;
	}
	
	public void getGoodMatches(){
		goodMatches = new LinkedList<KPMatch>();
		float thresh = this.min + matchThresh*(this.max - this.min);
		
		for(int i = 0; i < this.kpMatches.size();i++){
			if (this.kpMatches.get(i).getDist()< thresh){
				goodMatches.add(this.kpMatches.get(i));
			}
		}
		
	}
	
	public void alternative(String object, String scene){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat img_object = new Mat();
		Mat img_scene = new Mat();
		img_object = Highgui.imread(object, 0); //0 = CV_LOAD_IMAGE_GRAYSCALE
	    img_scene = Highgui.imread(scene, 0);	
	    Mat img_scene_norm = new Mat();
	    Imgproc.equalizeHist(img_scene, img_scene_norm);
	    Mat img_scene_blur = new Mat();
	    Imgproc.blur(img_scene, img_scene_blur, new Size(5,5));
	    Mat img_scene_binary = new Mat();
	    double mean = Core.mean(img_scene_blur).val[0];
	    Imgproc.threshold(img_scene_blur, img_scene_binary, 1.8*mean, 255, Imgproc.THRESH_BINARY);
	    LinkedList<MatOfPoint> cont= new LinkedList<MatOfPoint>();
	    Mat for_cont = img_scene_binary.clone();
	    Imgproc.findContours(for_cont, cont, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
	    MatOfPoint2f approxCurve = new MatOfPoint2f();
	    LinkedList<Rect> rects = new LinkedList<Rect>();
	    for (int i=0; i<cont.size(); i++)
        {
            //Convert contours(i) from MatOfPoint to MatOfPoint2f
            MatOfPoint2f contour2f = new MatOfPoint2f( cont.get(i).toArray() );
            //Processing on mMOP2f1 which is in type MatOfPoint2f
            double approxDistance = Imgproc.arcLength(contour2f, true)*0.02;
            Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);

            //Convert back to MatOfPoint
            MatOfPoint points = new MatOfPoint( approxCurve.toArray() );

            // Get bounding rect of contour
            Rect rect = Imgproc.boundingRect(points);
            rects.add(rect);
        }
	    LinkedList<Rect> goodrects = new LinkedList<Rect>();
	    for (int i=0; i<rects.size(); i++){
	    	Rect r = rects.get(i);
	    	if(r.width > 75 && r.width < 200 && r.height > 75 && r.height < 200){
	    	goodrects.add(r);	
	    	}
	    }
	    LinkedList<Integer> sizeList = new LinkedList<Integer>();
	    Mat for_rot = img_scene_binary;
	    for(int i = 0; i< goodrects.size();i++){
	    	Mat sm = for_rot.submat(goodrects.get(i));
	    	Mat sm_resize = new Mat();
	    	Imgproc.resize(sm, sm_resize, new Size(170,170));
	    	Mat rot = Imgproc.getRotationMatrix2D(new Point(85, 85), 1, 1);
	    	Mat rot_src = sm_resize;
	    	int maxsum = 0;
	    	for(int a = 0; a<360; a++){
	    	Mat rot_dest = new Mat();	
	    	Imgproc.warpAffine(rot_src, rot_dest, rot, new Size(170,170));
	    	int sum = 0;
	    	for(int m = 0; m < 170; m++){
	    		for(int n = 0; n < 170; n++){
	    			if(((int) rot_dest.get(m, n)[0]) == 255) sum++;
	    		}
	    	}
	    	if (sum > maxsum){ maxsum = sum;}
	    	rot_src = rot_dest;
	    	}
	    	sizeList.add(maxsum);	    	
	    }
	    Mat img_scene_col = new Mat();
	    Imgproc.cvtColor(img_scene_binary, img_scene_col, Imgproc.COLOR_GRAY2RGB);
	    for(int i = 0; i< goodrects.size();i++){
	    	if (sizeList.get(i)> 1000){
	    		Core.rectangle(img_scene_col, goodrects.get(i).br(), goodrects.get(i).tl(), new Scalar(0,255,0));
	    		System.out.print("draw");
	    	}
	    }
	    
	    
	    objectImage = toBufferedImage(img_object);
	    sceneImage = toBufferedImage(img_scene_col);
	  
	}

}
