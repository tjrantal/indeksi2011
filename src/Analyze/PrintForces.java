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

/*Class for debugging. Prints out a file with data*/

package Analyze;
import ReadWDQ.*;
import java.io.*;

public class PrintForces{
		//Print out the file
	public static void printFile(ReadWDQ readWDQ){
		try{
			DataInputStream inFile = new DataInputStream(new BufferedInputStream(new FileInputStream(readWDQ.fileIn)));
			inFile.skip((int) readWDQ.dataInHeader);
			BufferedWriter writer;
			writer = new BufferedWriter(new FileWriter("D:/UserData/ratimo/Oma/Deakin/INDEKSI2011/testiResults/voimat.xls",false));	//Overwrite saveName file

			int column = 0;
			double value;
			for (int j = 0;j<(int)readWDQ.dataAmount/(2*readWDQ.channelNo);++j){
				value = ((double) Short.reverseBytes(inFile.readShort()))*readWDQ.scalings[column]*0.25;
				writer.write(Double.toString(value));
				++column;
				if (column==readWDQ.channelNo){
					writer.write("\n");
					column = 0;
				}else{
					writer.write("\t");
				}
				
			}
			inFile.close();			
			writer.close();
		} catch (Exception err) {System.out.println("Can't read "+err.getMessage());}
	}
	
	public static void printArray(double[] dataArray, String suffix){
		try{
			BufferedWriter writer;
			writer = new BufferedWriter(new FileWriter("D:/UserData/ratimo/Oma/Deakin/INDEKSI2011/testiResults/voimatCh"+suffix+".xls",false));	//Overwrite saveName file

			for (int j = 0;j<dataArray.length;++j){
				writer.write(dataArray[j]+"\n");
			}
			writer.close();
		} catch (Exception err) {System.out.println("Can't read "+err.getMessage());}
	}
}