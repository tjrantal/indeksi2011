javac -cp "src" ui/Indeksi2011.java -d build/java
javac Analyze/Analyze.java
::javac ui/AnalysisThread.java
::javac Filter/ButterworthCoefficients.java
jar cfm indeksi2011.jar manifest/manifest.mf build/java/ui build/java/ReadWDQ build/java/Filter build/java/Analyze build/java/fft compileIndeksi2011.bat 
::signIndeksi.bat
java -Xmx950m -jar indeksi2011.jar

