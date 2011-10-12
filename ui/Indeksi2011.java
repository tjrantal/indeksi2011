/*
	This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

	N.B.  the above text was copied from http://www.gnu.org/licenses/gpl.html
	unmodified. I have not attached a copy of the GNU license to the source...

    Copyright (C) 2011 Timo Rantalainen, tjrantal@gmail.com
*/

/*Program for analysing activity indices from vertical GRFs collected
from underneath animal housing. Only useful for University of Jyvaskyla, who
actually have such measurement apparatus. The WDQ file reading may be of use
to DataQ or old Codas users.
*/

package ui;
import javax.swing.*;		//GUI komennot swing
import java.awt.event.*; 	//Eventit ja Actionlistener
import java.io.*;				//File IO
import java.lang.Math;
import java.awt.*;
import java.awt.geom.Line2D;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.Vector;
import java.util.Enumeration;
import java.awt.font.*;
import java.text.*;
import java.util.StringTokenizer; //Reading CSV
import ReadWDQ.*;
import Analyze.*;
import Filter.*;
import ui.*;
//import Randomize.*;		//Randomization

/*implements AL antaa mahdollisuuden kayttaa eventtteja koneelta. Kayttis toteuttaa...*/
/*extends = inherit, voi peria vain yhden*/
public class Indeksi2011 extends JPanel implements ActionListener {	
	public JButton calibrationToOpen;
	public JButton fileToOpen;
	public JButton fileToSave;
	public JButton openFile;
	public JTextField lowPass;
	public JLabel status;
	public JLabel analysisFileStatus;
	public File selectedFile;
	public File calibrationFile;
	public String savePath;
	public String initPath;
	public Vector<String[]> calibrations;
	public int calibrationFileNo;
	public Indeksi2011(){
		selectedFile = null;
		/*Preset path*/
		String imageSourceString =new String("");
		String iSavePath = new String("");
		File vali = new File("user.dir");
		boolean current = true;	//true = using current, false = preset path
		if (!current){
			vali = new File("C:/Oma/Deakin/TMS_KIDGELL2011");
		}
		/*CURRENT PATH*/
		initPath = new String();
		if (current){
			initPath = System.getProperty("user.dir");
		}else{
			initPath = vali.getAbsolutePath();
		}
		savePath = null;
		/*Add buttons and textfield...*/
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(6,2,5,5));	/*Set button layout...*/
		calibrationToOpen= new JButton("Calibration file to Open");
		calibrationToOpen.setMnemonic(KeyEvent.VK_C);
		calibrationToOpen.setActionCommand("calibrationFile");
		calibrationToOpen.addActionListener(this);
		calibrationToOpen.setToolTipText("Press to select file.");
		buttons.add(new JLabel(new String("Calibration file to use")));
		buttons.add(calibrationToOpen);
		
		fileToOpen= new JButton("WDQ file to Open");
		fileToOpen.setMnemonic(KeyEvent.VK_W);
		fileToOpen.setActionCommand("fileToOpen");
		fileToOpen.addActionListener(this);
		fileToOpen.setToolTipText("Press to select file.");
		buttons.add(new JLabel(new String("File to Open")));
		buttons.add(fileToOpen);
		
		fileToSave= new JButton("Result save Path");
		fileToSave.setMnemonic(KeyEvent.VK_R);
		fileToSave.setActionCommand("fileToSave");
		fileToSave.addActionListener(this);
		fileToSave.setToolTipText("Press to select savePath.");
		buttons.add(new JLabel(new String("Select Save Path")));
		buttons.add(fileToSave);

		
		openFile = new JButton("Indeksi2011");
		openFile.setMnemonic(KeyEvent.VK_I);
		openFile.setActionCommand("openFile");
		openFile.addActionListener(this);
		openFile.setToolTipText("Press to Open file.");
		buttons.add(new JLabel(new String("Click to Open File")));
		buttons.add(openFile);
		
		lowPass = new JTextField("5.0",4);
		buttons.add(new JLabel("Low pass limit"));
		buttons.add(lowPass);
		
		status = new JLabel(new String("Ready to Rumble"));
		buttons.add(status);
		analysisFileStatus = new JLabel(new String(""));
		buttons.add(analysisFileStatus);
		add(buttons);
		
	}
	
	public static void initAndShowGU(){
		JFrame f = new JFrame("OpenWDQ");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JComponent newContentPane = new Indeksi2011();
		newContentPane.setOpaque(true); //content panes must be opaque
		f.setContentPane(newContentPane);
		f.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = 300;
		int h = 200;
		f.setLocation(20, 20);
		//f.setLocation(screenSize.width/2 - w/2, screenSize.height/2 - h/2);
		//f.setSize(w, h);
		f.setVisible(true);		
	}
	
	public void actionPerformed(ActionEvent e) {
		if ("calibrationFile".equals(e.getActionCommand())){
			JFileChooser chooser = new JFileChooser(initPath);
			//chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = chooser.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				calibrationFile = chooser.getSelectedFile();
				System.out.println("Calibration file "+calibrationFile.getName());
				initPath = calibrationFile.getAbsolutePath();
				readCalibrationFile(calibrationFile);
				status.setText(new String("CalibrationFileChosen"));
			}
			
		}
	
		if ("fileToOpen".equals(e.getActionCommand())){
			JFileChooser chooser = new JFileChooser(initPath);
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int returnVal = chooser.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				selectedFile = chooser.getSelectedFile();
				System.out.println("File selected "+selectedFile.getName());
				initPath = selectedFile.getAbsolutePath();
				status.setText(new String("FileToAnalyzeChosen"));
			}
		}
		
		
		if ("fileToSave".equals(e.getActionCommand())){
			JFileChooser chooser = new JFileChooser(initPath);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = chooser.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				File savePathFile = chooser.getSelectedFile();
				savePath = savePathFile.getAbsolutePath();
				System.out.println("Save Path "+savePath);
				status.setText(new String("SavePathChosen"));
			}
		}
		if ("openFile".equals(e.getActionCommand())) {
			calibrationToOpen.setEnabled(false);
			fileToOpen.setEnabled(false);
			openFile.setEnabled(false);
			fileToSave.setEnabled(false);
			/*For debugging, don't need to select files...*/
			if (calibrations == null){
				readCalibrationFile(new File("C:/Oma/Deakin/INDEKSI2011/GITSKRIPTI/hiiriTesti.csv"));	//Windows
				//readCalibrationFile(new File("C:/Oma/Deakin/INDEKSI2011/javaScript/testi.csv"));	//Windows
				//readCalibrationFile(new File("/home/timo/Oma/Deakin/INDEKSI2011/javaScript/testi.csv"));	//Linux
			}
			if (selectedFile == null){
				selectedFile = new File("C:/Oma/Deakin/INDEKSI2011/hiiret");	//Windows
				//selectedFile = new File("C:/Oma/Deakin/INDEKSI2011/javaScript/290811_3hiirta.WDQ");	//Windows
				//selectedFile = new File("/home/timo/Oma/Deakin/INDEKSI2011/javaScript/290811_3hiirta.WDQ");	//Linux
			}
			if (savePath == null){
				//savePath = new String("C:/Oma/Deakin/INDEKSI2011/javaScript");
				savePath = new String("C:/Oma/Deakin/INDEKSI2011/GITSKRIPTI/results");
			}
			System.out.println("Open file "+selectedFile.getName());
			
			try{
				AnalysisThread analysisThread = new AnalysisThread(this);
				Thread anaThread = new Thread(analysisThread,"analysisThread");
				anaThread.start();	//All of the analysis needs to be done within this thread from hereafter
				//anaThread.join();
			}catch (Exception err){System.out.println("Failed analysis thread"+err);}
		
			System.gc();	//Try to enforce carbage collection
		}		
	}
	
	public void readCalibrationFile(File calibration){
		System.out.println("Reading calibration"); 
		try {
			BufferedReader br = new BufferedReader( new FileReader(calibration));
			String strLine = "";
			StringTokenizer st = null;
			int lineNumber = 0, tokenNumber = 0;
			calibrations = new Vector<String[]>();
			while( (strLine = br.readLine()) != null){
				++lineNumber;
				st = new StringTokenizer(strLine, ",");
				calibrations.add(new String[6]);							
				tokenNumber = 0;		
				while(st.hasMoreTokens()){
					calibrations.lastElement()[tokenNumber]=st.nextToken();
					tokenNumber++;
				}
				System.out.println(calibrations.lastElement()[0]+" Hiiria "+ Integer.valueOf(calibrations.lastElement()[1])); 
			}
			br.close();
		} catch (Exception err){System.err.println("Error: "+err.getMessage());}
	}
	
	public static void main(String[] args){
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				initAndShowGU();
			}
		}
		);
	}
}


