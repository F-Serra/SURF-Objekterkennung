package plane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import plane.PDController.PDActionListener;

public class PDView extends JFrame{
	
	DrawingPanel jview = new DrawingPanel();
	JPanel jctrl = new JPanel();
	JPanel objctrl = new JPanel();
	JPanel scenectrl = new JPanel();
	JPanel surfctrl = new JPanel();
	JPanel surf2ctrl = new JPanel();
	JPanel surf3ctrl = new JPanel();
	JPanel useImagectrl = new JPanel();
	JPanel showImagectrl = new JPanel();
	JPanel showHitboxctrl = new JPanel();
	JPanel canny1ctrl = new JPanel();
	JPanel canny2ctrl = new JPanel();
	JPanel minNctrl = new JPanel();
	JPanel stdevctrl = new JPanel();
	JPanel buttonctrl = new JPanel();
	JButton applyB = new JButton("Apply");
	JSlider distS = new JSlider();
	JSlider canny1S = new JSlider();
	JSlider canny2S = new JSlider();
	JSlider minNS = new JSlider();
	JSlider stdevS = new JSlider();
	JComboBox objectZC = new JComboBox();
	JComboBox objectIC = new JComboBox();
	JComboBox imageZC = new JComboBox();
	JComboBox imageIC = new JComboBox();
	JLabel objectL = new JLabel();
	JLabel objectZL = new JLabel();
	JLabel imageL = new JLabel();
	JLabel imageZL = new JLabel();
	JLabel minMaxDL = new JLabel();
	JLabel meanVarL = new JLabel();
	JLabel slidertext = new JLabel();
	JLabel slidervalue = new JLabel();
	JLabel useL = new JLabel();
	JLabel showL = new JLabel();
	JLabel showHitboxL = new JLabel();
	JLabel canny1textL = new JLabel();
	JLabel canny2textL = new JLabel();
	JLabel canny1valueL = new JLabel();
	JLabel canny2valueL = new JLabel();
	JLabel minNtextL = new JLabel();
	JLabel minNvalueL = new JLabel();
	JLabel stdevtextL = new JLabel();
	JLabel stdevvalueL = new JLabel();
	JRadioButton useGreyRB = new JRadioButton("Greyscale");
	JRadioButton useCannyRB = new JRadioButton("Canny");
	JRadioButton showOriginalRB = new JRadioButton("Original");
	JRadioButton showProcessedRB = new JRadioButton("Processed");
	JRadioButton showHitboxyesRB = new JRadioButton("Yes");
	JRadioButton showHitboxnoRB = new JRadioButton("No");
	JButton applyB2 = new JButton("Alternative");
	PDController controller;
	
	
	public PDView(PDController ctrl){
		this.setTitle("Plane Detection");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.controller = ctrl;
		
		TitledBorder viewtitle;
		viewtitle = BorderFactory.createTitledBorder("View");
		jview.setBorder(viewtitle);
		jview.setBackground(Color.WHITE);
		
		TitledBorder ctrltitle;
		ctrltitle = BorderFactory.createTitledBorder("Control");
		jctrl.setBorder(ctrltitle);
				
		objectL.setText("Object:");
		objectZL.setText("Zoom:");
		imageL.setText("Scene:");
		imageZL.setText("Zoom:");
		minMaxDL.setText("Min: --   Max: --");
		meanVarL.setText("N: --   Stdev: --");
		slidertext.setText("  Distance threshold (View): ");
		slidervalue.setText("1.00");
		canny1textL.setText("  Canny threshold 1: ");
		canny2textL.setText("  Canny threshold 2: ");
		canny1valueL.setText("2.00");
		canny2valueL.setText("2.00");
		minNtextL.setText("  Distance threshold (Match): ");
		minNvalueL.setText("1.00");
		stdevtextL.setText("  RANSAC threshold: ");
		stdevvalueL.setText("10");
		useL.setText("Use Images: ");
		showL.setText("Show Images: ");
		showHitboxL.setText("Show Hitbox: ");
		
		String[] zoomList = {"16", "17", "18"};
		String[] objectList = {"bkg_grey", "bkg_black", "bkg_white"};
		String[] imageList = {"51.477427,-0.565538","49.022547,2.482588","49.003871,2.710787",
							"40.14036,116.58921","41.968632,-87.986562","41.969398,-88.043575",
							"41.967994,-88.27034","32.955043,-97.049897","33.00187,-97.052203",
							"32.818738,-97.01108","1.41027,104.003406","1.56755,104.065386",
							"1.5907,104.105533","50.03964,8.543734","50.046888,8.553153",
							"49.015384,2.510891"};
		
		
		objectZC.setModel(new DefaultComboBoxModel(zoomList));
		objectIC.setModel(new DefaultComboBoxModel(objectList));
		imageZC.setModel(new DefaultComboBoxModel(zoomList));
		imageIC.setModel(new DefaultComboBoxModel(imageList));
		
		ButtonGroup useBG = new ButtonGroup();
		useBG.add(useGreyRB);
		useBG.add(useCannyRB);
		useGreyRB.setSelected(true);
		
		ButtonGroup showBG = new ButtonGroup();
		showBG.add(showOriginalRB);
		showBG.add(showProcessedRB);
		showOriginalRB.setSelected(true);
		
		ButtonGroup showHitbox = new ButtonGroup();
		showHitbox.add(showHitboxyesRB);
		showHitbox.add(showHitboxnoRB);
		showHitboxyesRB.setSelected(true);
		
		distS.setName("distS");
		distS.setMinimum(0);
		distS.setMaximum(100);
		distS.setValue(100);
		distS.setMajorTickSpacing(25);
		distS.setMinorTickSpacing(5);
		distS.setPaintTicks(true);
		
		canny1S.setName("canny1S");
		canny1S.setMinimum(0);
		canny1S.setMaximum(200);
		canny1S.setValue(200);
		canny1S.setMajorTickSpacing(50);
		canny1S.setMinorTickSpacing(10);
		canny1S.setPaintTicks(true);
		
		canny2S.setName("canny2S");
		canny2S.setMinimum(0);
		canny2S.setMaximum(200);
		canny2S.setValue(200);
		canny2S.setMajorTickSpacing(50);
		canny2S.setMinorTickSpacing(10);
		canny2S.setPaintTicks(true);
		
		minNS.setName("minNS");
		minNS.setMinimum(0);
		minNS.setMaximum(100);
		minNS.setValue(100);
		minNS.setMajorTickSpacing(25);
		minNS.setMinorTickSpacing(5);
		minNS.setPaintTicks(true);
		
		stdevS.setName("stdevS");
		stdevS.setMinimum(0);
		stdevS.setMaximum(10);
		stdevS.setValue(10);
		stdevS.setMajorTickSpacing(5);
		stdevS.setMinorTickSpacing(1);
		stdevS.setPaintTicks(true);
		
		
		FlowLayout objctrlLayout = new FlowLayout();
		objctrlLayout.setAlignment(FlowLayout.LEFT);
		objctrl.setLayout(objctrlLayout);		
		objctrl.add(objectL);
		objctrl.add(objectIC);
		objctrl.add(objectZL);
		objctrl.add(objectZC);
		
		FlowLayout scenectrlLayout = new FlowLayout();
		scenectrlLayout.setAlignment(FlowLayout.LEFT);
		scenectrl.setLayout(scenectrlLayout);
		scenectrl.add(imageL);
		scenectrl.add(imageIC);
		scenectrl.add(imageZL);
		scenectrl.add(imageZC);
		
		FlowLayout surfctrlLayout = new FlowLayout();
		surfctrlLayout.setAlignment(FlowLayout.LEFT);
		surfctrl.setLayout(surfctrlLayout);
		surfctrl.add(minMaxDL);
		
		
		FlowLayout surf2ctrlLayout = new FlowLayout();
		surf2ctrlLayout.setAlignment(FlowLayout.LEFT);
		surf2ctrl.setLayout(surf2ctrlLayout);
		surf2ctrl.add(meanVarL);
		
		BorderLayout canny1ctrlLayout = new BorderLayout();
		canny1ctrl.setLayout(canny1ctrlLayout);
		canny1ctrl.add(canny1textL,BorderLayout.WEST);
		canny1ctrl.add(canny1S,BorderLayout.SOUTH);
		canny1ctrl.add(canny1valueL,BorderLayout.CENTER);
		
		BorderLayout canny2ctrlLayout = new BorderLayout();
		canny2ctrl.setLayout(canny2ctrlLayout);
		canny2ctrl.add(canny2textL,BorderLayout.WEST);
		canny2ctrl.add(canny2S,BorderLayout.SOUTH);
		canny2ctrl.add(canny2valueL,BorderLayout.CENTER);
		
		BorderLayout surf3ctrlLayout = new BorderLayout();
		surf3ctrl.setLayout(surf3ctrlLayout);
		surf3ctrl.add(slidertext,BorderLayout.WEST);
		surf3ctrl.add(distS,BorderLayout.SOUTH);
		surf3ctrl.add(slidervalue,BorderLayout.CENTER);
		
		BorderLayout minNctrlLayout = new BorderLayout();
		minNctrl.setLayout(minNctrlLayout);
		minNctrl.add(minNtextL,BorderLayout.WEST);
		minNctrl.add(minNS,BorderLayout.SOUTH);
		minNctrl.add(minNvalueL,BorderLayout.CENTER);
		
		BorderLayout stdevctrlLayout = new BorderLayout();
		stdevctrl.setLayout(stdevctrlLayout);
		stdevctrl.add(stdevtextL,BorderLayout.WEST);
		stdevctrl.add(stdevS,BorderLayout.SOUTH);
		stdevctrl.add(stdevvalueL,BorderLayout.CENTER);
		
		FlowLayout usectrlLayout = new FlowLayout();
		usectrlLayout.setAlignment(FlowLayout.LEFT);
		useImagectrl.setLayout(usectrlLayout);
		useImagectrl.add(useL);
		useImagectrl.add(useGreyRB);
		useImagectrl.add(useCannyRB);
		
		FlowLayout showctrlLayout = new FlowLayout();
		showctrlLayout.setAlignment(FlowLayout.LEFT);
		showImagectrl.setLayout(showctrlLayout);
		showImagectrl.add(showL);
		showImagectrl.add(showOriginalRB);
		showImagectrl.add(showProcessedRB);
		
		FlowLayout showHitboxctrlLayout = new FlowLayout();
		showHitboxctrlLayout.setAlignment(FlowLayout.LEFT);
		showHitboxctrl.setLayout(showctrlLayout);
		showHitboxctrl.add(showHitboxL);
		showHitboxctrl.add(showHitboxyesRB);
		showHitboxctrl.add(showHitboxnoRB);		
		
		buttonctrl.add(applyB);
		//buttonctrl.add(applyB2);
		
		GridLayout ctrlLayout = new GridLayout(15,1);
		jctrl.setLayout(ctrlLayout);
		jctrl.add(objctrl);
		jctrl.add(scenectrl);
		jctrl.add(useImagectrl);
		jctrl.add(showImagectrl);
		jctrl.add(showHitboxctrl);
		jctrl.add(canny1ctrl);
		jctrl.add(canny2ctrl);
		jctrl.add(surf3ctrl);
		jctrl.add(minNctrl);
		jctrl.add(stdevctrl);
		jctrl.add(surfctrl);
		jctrl.add(surf2ctrl);		
		jctrl.add(buttonctrl);		
		this.add(jview, BorderLayout.CENTER);		
		this.add(jctrl, BorderLayout.WEST);
		
		this.pack();
		this.setVisible(true);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	public void setNewImages(Image object,Image scene,LinkedList<KPMatch> matchL){
		jview.setImages(object, scene);
		jview.drawMatches(matchL);		
	}
	
	public void setMinMax(float min, float max){
		minMaxDL.setText("Min: " + String.valueOf(min) + "   Max: " + String.valueOf(max));
		
	}

	public void addListeners(ActionListener pdActionListener, ChangeListener pdChangeListener) {
		applyB.addActionListener(pdActionListener);
		applyB2.addActionListener(pdActionListener);
		distS.addChangeListener(pdChangeListener);
		canny1S.addChangeListener(pdChangeListener);
		canny2S.addChangeListener(pdChangeListener);
		minNS.addChangeListener(pdChangeListener);
		stdevS.addChangeListener(pdChangeListener);
	}

	public void changedistSValueLabel(int value) {
		double sliderv = value/100d;
		DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
		dfs.setDecimalSeparator('.');
		DecimalFormat f = new DecimalFormat("#0.00",dfs);
		slidervalue.setText(""+f.format(sliderv));		
	}
	
	public void changecanny1SValueLabel(int value) {
		double sliderv = value/100d;
		DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
		dfs.setDecimalSeparator('.');
		DecimalFormat f = new DecimalFormat("#0.00",dfs);
		canny1valueL.setText(""+f.format(sliderv));		
	}
	
	public void changecanny2SValueLabel(int value) {
		double sliderv = value/100d;
		DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
		dfs.setDecimalSeparator('.');
		DecimalFormat f = new DecimalFormat("#0.00",dfs);
		canny2valueL.setText(""+f.format(sliderv));	
	}
	
	public void changeminNSValueLabel(int value) {
		double sliderv = value/100d;
		DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
		dfs.setDecimalSeparator('.');
		DecimalFormat f = new DecimalFormat("#0.00",dfs);
		minNvalueL.setText(""+f.format(sliderv));		
	}
	
	public void changestdevSValueLabel(int value) {
		stdevvalueL.setText(""+value);		
	}

	public void setThreshold(double d) {
		jview.setThreshold(d);		
	}

	public void setStats(Statistics stats) {
		jview.stats = stats;
		DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
		dfs.setDecimalSeparator('.');
		DecimalFormat f = new DecimalFormat("#0.00",dfs);
		meanVarL.setText("N: "+stats.n+"   Stdev: "+f.format(stats.stdev));
		
	}
	

}
