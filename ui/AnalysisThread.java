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
package ui;
import ui.*;
import Analyze.*;
import ReadWDQ.*;
import java.io.*;
class AnalysisThread implements Runnable{
	public Indeksi2011 mainProgram;
	AnalysisThread(Indeksi2011 indIn){
		mainProgram = indIn;
	}
	
	public void run(){
		if (mainProgram.selectedFile.isDirectory()){/*Go through each file, if folder was selected*/
			File[] listOfFiles = mainProgram.selectedFile.listFiles();
			for (int i = 0; i < listOfFiles.length; ++i){/*Loop through each file*/
				if(listOfFiles[i].getName().toLowerCase().indexOf(".wdq") > -1){
					mainProgram.analysisFileStatus.setText(listOfFiles[i].getName()+" "+(i+1)+"/"+listOfFiles.length);
					mainProgram.calibrationFileNo =0;
					boolean	continueAnalysis = false;
					while (listOfFiles[i].getName().equalsIgnoreCase(mainProgram.calibrations.get(mainProgram.calibrationFileNo)[0])==false){
						++mainProgram.calibrationFileNo;
						if (mainProgram.calibrationFileNo == mainProgram.calibrations.size()-1){break;}
					}
					//System.out.println(listOfFiles[i].getName()+" Kalibraatio "+calibrationFile);
					if (listOfFiles[i].getName().equalsIgnoreCase(mainProgram.calibrations.get(mainProgram.calibrationFileNo)[0])==true) {
						continueAnalysis = true;
					}else{
						mainProgram.status.setText(new String("No matching file found from calibration"));
					}
					if (continueAnalysis){
						mainProgram.status.setText(new String("Reading file..."));
						ReadWDQ readWDQ = new ReadWDQ(listOfFiles[i]);
						mainProgram.status.setText(new String("Analyzing..."));
						Analyze analyze = new Analyze(readWDQ,mainProgram);
					}
				}
			}
			
		}else{
		/*Check calibration file line*/
			mainProgram.calibrationFileNo =0;
			boolean	continueAnalysis = false;
			while (mainProgram.selectedFile.getName().equalsIgnoreCase(mainProgram.calibrations.get(mainProgram.calibrationFileNo)[0])==false){
				++mainProgram.calibrationFileNo;
				if (mainProgram.calibrationFileNo == mainProgram.calibrations.size()-1){break;}
			}
			//System.out.println(listOfFiles[i].getName()+" Kalibraatio "+calibrationFile);
			if (mainProgram.selectedFile.getName().equalsIgnoreCase(mainProgram.calibrations.get(mainProgram.calibrationFileNo)[0])==true) {
				continueAnalysis = true;
			}else{
				mainProgram.status.setText(new String("No matching file found from calibration"));
			}
			if (continueAnalysis){
				mainProgram.analysisFileStatus.setText(mainProgram.selectedFile.getName());
				mainProgram.status.setText(new String("Reading file..."));
				ReadWDQ readWDQ = new ReadWDQ(mainProgram.selectedFile);
				mainProgram.status.setText(new String("Analyzing..."));
				Analyze analyze = new Analyze(readWDQ,mainProgram);
			}
		}
		
		mainProgram.status.setText(new String("ReadyToRumble"));
		mainProgram.calibrationToOpen.setEnabled(true);
		mainProgram.fileToOpen.setEnabled(true);
		mainProgram.openFile.setEnabled(true);
		mainProgram.fileToSave.setEnabled(true);
	}
}
