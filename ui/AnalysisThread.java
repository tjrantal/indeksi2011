
package ui;
import ui.*;
import Analyze.*;
import ReadWDQ.*;
class AnalysisThread implements Runnable{
	public Indeksi2011 mainProgram;
	AnalysisThread(Indeksi2011 indIn){
		mainProgram = indIn;
	}
	
	public void run(){
		mainProgram.status.setText(new String("Reading file..."));
		ReadWDQ readWDQ = new ReadWDQ(mainProgram.selectedFile);
		mainProgram.status.setText(new String("Analyzing..."));
		Analyze analyze = new Analyze(readWDQ,mainProgram);
		mainProgram.status.setText(new String("ReadyToRumble"));
		mainProgram.calibrationToOpen.setEnabled(true);
		mainProgram.fileToOpen.setEnabled(true);
		mainProgram.openFile.setEnabled(true);
		mainProgram.fileToSave.setEnabled(true);
	}
}