javac -cp ".:" ui/Indeksi2011.java
#javac Analyze/Analyze.java
#javac ui/AnalysisThread.java
#javac Filter/ButterworthCoefficients.java
jar cfm indeksi2011.jar manifest.mf ui ReadWDQ Filter Analyze compileIndeksi2011.bat 
#signIndeksi.bat
java -Xmx950m -jar indeksi2011.jar
