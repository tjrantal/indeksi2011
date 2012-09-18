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

/*
Reads WDQ file header. Data is saved in binary
format after 1156 byte long header
*/
package ReadWDQ;
import java.io.*;	//File IO
import java.util.*;	//Date
import java.text.*;	//DateFormat
public class ReadWDQ{
	long fileLength;
	public byte[] memoryData;
	public byte[] headerData;
	public short channelBytes;
	public short offset;
	public short dataInHeader;
	public long dataAmount;
	public byte channelNo;
	public double[] scalings;
	public double[] intercepts;
	public double calibrationScaling;
	public double calibrationIntercept;
	public double samplingInterval;
	public short[] channels;
	public String measurementInit;
	public String measurementStop;
	public String fileName;
	public File fileIn;
	public ReadWDQ(File fileIn){
		fileName = fileIn.getName();
		this.fileIn = fileIn;
		fileLength = fileIn.length();
		headerData = new byte[1156];
		//memoryData = new byte[1];		//Allocate memory for reading the file into memory
		//int dataRead;
		int headerRead;
		try{
			DataInputStream inFile = new DataInputStream(new BufferedInputStream(new FileInputStream(fileIn)));
			headerRead =inFile.read(headerData,0,1156);
			readHeader();
			inFile.close();
			
		} catch (Exception err) {System.out.println("Can't read "+err.getMessage());}
		
	}
	
	public void readHeader(){
		//READ HEADER INFO
		channelNo = (byte) (headerData[0] & 0x1F);	//Number of channels given in first 5 bits -> 1F...
		//System.out.println("Channels in header "+channelNo);
		int position = 4;
		offset = (short) (0xFF & headerData[position]); //4
		position += 1;
		channelBytes = (short) (0xFF & headerData[position]); //4
		position += 1;
		dataInHeader = (short) ((((int)headerData[position+1]) & 0xFF)<<8 | (((int)headerData[position]) & 0xFF));//5
		position += 2;
		dataAmount = (long) (
									((long)(0xFF & ((int)headerData[position+3])))<<24 |
									((long)(0xFF & ((int)headerData[position+2])))<<16 |
									((long)(0xFF & ((int)headerData[position+1])))<<8 | 
									((long)(0xFF & ((int)headerData[position])))
									);//7
		//System.out.println("dataAmount "+dataAmount);
		position += 4;
		position += 16;							
		long tempTime =(long) (
									((long)(0xFF & ((int)headerData[position+7])))<<56 |
									((long)(0xFF & ((int)headerData[position+6])))<<48 |
									((long)(0xFF & ((int)headerData[position+5])))<<40 | 
									((long)(0xFF & ((int)headerData[position+4])))<<32 |
									((long)(0xFF & ((int)headerData[position+3])))<<24 |
									((long)(0xFF & ((int)headerData[position+2])))<<16 |
									((long)(0xFF & ((int)headerData[position+1])))<<8 | 
									((long)(0xFF & ((int)headerData[position])))
									);//27
		samplingInterval = Double.longBitsToDouble(tempTime);
		position += 8;
		long iTime = (long) (
									((long)(0xFF & ((int)headerData[position+3])))<<24 |
									((long)(0xFF & ((int)headerData[position+2])))<<16 |
									((long)(0xFF & ((int)headerData[position+1])))<<8 | 
									((long)(0xFF & ((int)headerData[position])))
									);//7
		position += 4;
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG,Locale.GERMANY);
		TimeZone tz = TimeZone.getTimeZone("Finland/Helsinki");
		df.setTimeZone(tz);
		measurementInit = df.format(new Date(iTime*1000));
		//System.out.println("Init "+measurementInit);
		long sTime = (long) (
									((long)(0xFF & ((int)headerData[position+3])))<<24 |
									((long)(0xFF & ((int)headerData[position+2])))<<16 |
									((long)(0xFF & ((int)headerData[position+1])))<<8 | 
									((long)(0xFF & ((int)headerData[position])))
									);//7
		position += 4;
		measurementStop = df.format(new Date(sTime*1000));
		//System.out.println("Stop "+measurementStop);
		
		position += 4;
		position += 4;
		position += 4;
		position += 4;
		position += 2;
		position += 2;
		position += 1;
		position += 1;
		position += 1;
		position += 1;
		channels = new short[32];
		for (int i = 0; i< 32;++i){
			channels[i] =(short) (0xFF & ((int)headerData[position]));
			++position;
		}
		position += 2;
		position += 2;
		position += 1;
		position += 1;
		position += 2;
		position += 1;
		position += 1;
		//System.out.println("Position "+position);
		//Channel infos
		scalings = new double[32];
		intercepts = new double[32];
		for (int ii = 0;ii<(dataInHeader-(3+offset-1))/channelBytes;ii++) {
			position += 4;
			position += 4;
			long tempScaling =(long) (
									((long)(0xFF & ((int)headerData[position+7])))<<56 |
									((long)(0xFF & ((int)headerData[position+6])))<<48 |
									((long)(0xFF & ((int)headerData[position+5])))<<40 | 
									((long)(0xFF & ((int)headerData[position+4])))<<32 |
									((long)(0xFF & ((int)headerData[position+3])))<<24 |
									((long)(0xFF & ((int)headerData[position+2])))<<16 |
									((long)(0xFF & ((int)headerData[position+1])))<<8 | 
									((long)(0xFF & ((int)headerData[position])))
									);//27
			scalings[ii] = Double.longBitsToDouble(tempScaling);
			position += 8;
			tempScaling =(long) (
									((long)(0xFF & ((int)headerData[position+7])))<<56 |
									((long)(0xFF & ((int)headerData[position+6])))<<48 |
									((long)(0xFF & ((int)headerData[position+5])))<<40 | 
									((long)(0xFF & ((int)headerData[position+4])))<<32 |
									((long)(0xFF & ((int)headerData[position+3])))<<24 |
									((long)(0xFF & ((int)headerData[position+2])))<<16 |
									((long)(0xFF & ((int)headerData[position+1])))<<8 | 
									((long)(0xFF & ((int)headerData[position])))
									);//27
			intercepts[ii] = Double.longBitsToDouble(tempScaling);
			position += 8;
			position += 6;
			position += 1;
			position += 1;
			position += 1;
			position += 1;
			position += 2;
		}
		/*
		for (int ii = 0; ii<(dataInHeader-(3+offset-1))/channelBytes;++ii){
			System.out.println("scalings "+ii+" "+scalings[ii]+" intercept "+intercepts[ii]);
		}
		*/	
	}
}
