<?php  header("Content-type: application/x-java-jnlp-file");
echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";?>

<!-- Test for pQCT Web Start Deployment -->
<jnlp
  spec="1.0+"
  codebase="http://densitydistribution.comli.com/indeksi2011/"
  href="indeksi2011.php">
  <information>
	<vendor>Timo Rantalainen</vendor>
    <title>Index analysis</title>
    <description>Analysis of movement from GRF data</description>
    <offline-allowed/>
  </information>
  <security>
    <all-permissions/>
  </security>
  <resources>
    <j2se href="http://java.sun.com/products/autodl/j2se" version="1.6+"  initial-heap-size="950M" max-heap-size="950M" />
    <jar href="indeksi2011_signed.jar"/>
  </resources>
  <application-desc
  	main-class="ui.Indeksi2011"/>
</jnlp>
