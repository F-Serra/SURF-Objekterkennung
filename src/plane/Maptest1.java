package plane;
import java.awt.Component;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
public class Maptest1 {

	public static void main(String[] args) throws IOException{
		JFrame test = new JFrame("Map Test");
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		//Mat mat = Mat.eye( 3, 3, CvType.CV_8UC1 );
	    //System.out.println( "mat = " + mat.dump() );

		
		
		try {
            String imageUrl = "http://maps.googleapis.com/maps/api/staticmap?" +
            		"center=48.352428,11.793859" +
            		"&zoom=16" +            		
            		"&size=700x600" +
            		"&format=png32" +
            		"&maptype=satellite" +
            		"&sensor=false";
            String destinationFile = "examples/zoom16full/48.352428,11.793859.png";
            URL url = new URL(imageUrl);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(destinationFile);

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        JLabel mylabel = new JLabel(new ImageIcon((new ImageIcon("examples/zoom16full/48.352428,11.793859.png")).getImage()));
        
        test.add(mylabel);
        test.setVisible(true);
        test.pack();


	}
}