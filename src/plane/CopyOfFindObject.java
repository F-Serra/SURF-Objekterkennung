package plane;

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
import org.opencv.core.Scalar;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;

public class CopyOfFindObject {
	
	LinkedList<KPMatch> kPMatchList;
	
	public static void main(String[] args) {
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    
	    Mat img_object = Highgui.imread("plane.png", 0); //0 = CV_LOAD_IMAGE_GRAYSCALE
	    Mat img_scene = Highgui.imread("image.png", 0);
	    
	    FeatureDetector detector = FeatureDetector.create(4); //4 = SURF 

	    MatOfKeyPoint keypoints_object = new MatOfKeyPoint();
	    MatOfKeyPoint keypoints_scene  = new MatOfKeyPoint();

	    detector.detect(img_object, keypoints_object);
	    detector.detect(img_scene, keypoints_scene);
	    
	    DescriptorExtractor extractor = DescriptorExtractor.create(2); //2 = SURF;

	    Mat descriptor_object = new Mat();
	    Mat descriptor_scene = new Mat() ;

	    extractor.compute(img_object, keypoints_object, descriptor_object);
	    extractor.compute(img_scene, keypoints_scene, descriptor_scene);

	    DescriptorMatcher matcher = DescriptorMatcher.create(1); // 1 = FLANNBASED
	    MatOfDMatch matches = new MatOfDMatch();

	    matcher.match(descriptor_object, descriptor_scene, matches);
	    List<DMatch> matchesList = matches.toList();

	    Double max_dist = 0.0;
	    Double min_dist = 100.0;

	    for(int i = 0; i < descriptor_object.rows(); i++){
	        Double dist = (double) matchesList.get(i).distance;
	        if(dist < min_dist) min_dist = dist;
	        if(dist > max_dist) max_dist = dist;
	    }

	    System.out.println("-- Max dist : " + max_dist);
	    System.out.println("-- Min dist : " + min_dist);  
	    System.out.println("-- keypoints_object: " + keypoints_object.toString());
	    System.out.println("-- descriptor_object: " + descriptor_object.toString());
	    System.out.println("-- keypoints_scene: " + keypoints_scene.toString());
	    System.out.println("-- matches_scene: " + matches.toString());
	    LinkedList<DMatch> good_matches = new LinkedList<DMatch>();
	    MatOfDMatch gm = new MatOfDMatch();

	    for(int i = 0; i < descriptor_object.rows(); i++){
	        if(matchesList.get(i).distance < 2*min_dist){
	            good_matches.addLast(matchesList.get(i));
	        }
	    }

	    gm.fromList(good_matches);

	    Mat img_matches = new Mat();
	    Features2d.drawMatches(
	            img_object,
	            keypoints_object, 
	            img_scene,
	            keypoints_scene, 
	            gm, 
	            img_matches, 
	            new Scalar(255,0,0), 
	            new Scalar(0,0,255), 
	            new MatOfByte(), 
	            4);

	    /*
	    LinkedList<Point> objList = new LinkedList<Point>();
	    LinkedList<Point> sceneList = new LinkedList<Point>();

	    List<KeyPoint> keypoints_objectList = keypoints_object.toList();
	    List<KeyPoint> keypoints_sceneList = keypoints_scene.toList();

	    for(int i = 0; i<good_matches.size(); i++){
	        objList.addLast(keypoints_objectList.get(good_matches.get(i).queryIdx).pt);
	        sceneList.addLast(keypoints_sceneList.get(good_matches.get(i).trainIdx).pt);
	    }

	    MatOfPoint2f obj = new MatOfPoint2f();
	    obj.fromList(objList);

	    MatOfPoint2f scene = new MatOfPoint2f();
	    scene.fromList(sceneList);

	    Mat hg = Calib3d.findHomography(obj, scene);

	    Mat obj_corners = new Mat(4,1,CvType.CV_32FC2);
	    Mat scene_corners = new Mat(4,1,CvType.CV_32FC2);

	    obj_corners.put(0, 0, new double[] {0,0});
	    obj_corners.put(1, 0, new double[] {obj.cols(),0});
	    obj_corners.put(2, 0, new double[] {obj.cols(),obj.rows()});
	    obj_corners.put(3, 0, new double[] {0,obj.rows()});

	    Core.perspectiveTransform(obj_corners,scene_corners, hg);

	    Core.line(scene, new Point(scene_corners.get(0,0)), new Point(scene_corners.get(1,0)), new Scalar(0, 255, 0),4);
	    Core.line(scene, new Point(scene_corners.get(1,0)), new Point(scene_corners.get(2,0)), new Scalar(0, 255, 0),4);
	    Core.line(scene, new Point(scene_corners.get(2,0)), new Point(scene_corners.get(3,0)), new Scalar(0, 255, 0),4);
	    Core.line(scene, new Point(scene_corners.get(3,0)), new Point(scene_corners.get(0,0)), new Scalar(0, 255, 0),4);
	    
	   */
	    //System.out.println(String.format("Writing %s", pathResult));
	    Highgui.imwrite("result.png", img_matches);



	    
	}

}
