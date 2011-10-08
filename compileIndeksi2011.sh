javac -cp ".:" ui/Indeksi2011.java
#javac Analyze/Analyze.java
#javac ui/AnalysisThread.java
#javac Filter/ButterworthCoefficients.java
jar cfm Indeksi2011.jar manifest.mf ui ReadWDQ Filter Analyze compileIndeksi2011.bat 
#signIndeksi.bat
java -Xmx950m -jar Indeksi2011.jar
