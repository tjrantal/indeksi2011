javac ui/Indeksi2011.java
javac Analyze/Analyze.java
::javac ui/AnalysisThread.java
::javac Filter/ButterworthCoefficients.java
jar cfe Indeksi2011.jar ui.Indeksi2011 ui ReadWDQ Filter Analyze compileIndeksi2011.bat 
signIndeksi.bat
java -Xms970m -Xmx970m -jar indeksi2011.jar