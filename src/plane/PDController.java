package plane;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PDController{
	
	PDView view;
	FindObject finder;
		
	public PDController(){
		view = new PDView(this);
		finder = new FindObject();
		addListeners();
	}
	
	private void addListeners() {
		view.addListeners(new PDActionListener(), new PDChangeListener());	
	}
	
	
	class PDActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Apply")) {
				String objzoom = view.objectZC.getSelectedItem().toString();
				String obj = view.objectIC.getSelectedItem().toString();
				String scenezoom = view.imageZC.getSelectedItem().toString();
				String scene = view.imageIC.getSelectedItem().toString();
				String objFile = "planes/zoom" + objzoom + "/" + obj + ".png";
				String sceneFile = "examples/zoom" + scenezoom + "/" + scene + ".png";
				finder.createMatchList(objFile, sceneFile, view.useCannyRB.isSelected());
				LinkedList<KPMatch> matchList = finder.kpMatches;
				if(view.showOriginalRB.isSelected()){
					Image img1 = Toolkit.getDefaultToolkit().getImage(objFile);
					Image img2 = Toolkit.getDefaultToolkit().getImage(sceneFile);
					view.setNewImages(img1, img2, matchList);
				}
				else view.setNewImages(finder.objectImage, finder.sceneImage, matchList);
				view.jview.hitbox = view.showHitboxyesRB.isSelected();
				finder.getGoodMatches();
				view.jview.goodmatchL = finder.goodMatches;
				view.jview.hitL = finder.getHitbox();
				view.setMinMax(finder.min, finder.max);
				Statistics stats = new Statistics();
				stats.getStats(matchList,view.jview.threshold);
				view.setStats(stats);
				
			}
			else if(e.getActionCommand().equals("Alternative")) {
				String objzoom = view.objectZC.getSelectedItem().toString();
				String obj = view.objectIC.getSelectedItem().toString();
				String scenezoom = view.imageZC.getSelectedItem().toString();
				String scene = view.imageIC.getSelectedItem().toString();
				String objFile = "planes/zoom" + objzoom + "/" + obj + ".png";
				String sceneFile = "examples/zoom" + scenezoom + "/" + scene + ".png";
				finder.alternative(objFile, sceneFile);
				LinkedList<KPMatch> matchList = new LinkedList<KPMatch>();
				if(view.showOriginalRB.isSelected()){
					Image img1 = Toolkit.getDefaultToolkit().getImage(objFile);
					Image img2 = Toolkit.getDefaultToolkit().getImage(sceneFile);
					view.setNewImages(img1, img2, matchList);
				}
				else view.setNewImages(finder.objectImage, finder.sceneImage, matchList);
			}
		}
	}
	
	class PDChangeListener implements ChangeListener{

		
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider)e.getSource();
			if(source.getName().equals("distS")){
				if (!source.getValueIsAdjusting()){
					view.changedistSValueLabel(source.getValue());
					view.setThreshold((double) (source.getValue()/100d));
				}
			}
			if(source.getName().equals("canny1S")){
				if (!source.getValueIsAdjusting()){
					view.changecanny1SValueLabel(source.getValue());
					finder.cannytrsh1 = source.getValue()/100.0;
				}
			}
			if(source.getName().equals("canny2S")){
				if (!source.getValueIsAdjusting()){
					view.changecanny2SValueLabel(source.getValue());
					finder.cannytrsh2 = source.getValue()/100.0;
				}
			}
			if(source.getName().equals("minNS")){
				if (!source.getValueIsAdjusting()){
					view.changeminNSValueLabel(source.getValue());
					finder.matchThresh = source.getValue()/100f;
				}
			}
			if(source.getName().equals("stdevS")){
				if (!source.getValueIsAdjusting()){
					view.changestdevSValueLabel(source.getValue());
					finder.ransacThresh = source.getValue();
				}
			}
			
			
		}
		
	}

}
