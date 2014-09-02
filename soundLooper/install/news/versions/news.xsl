<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ph="http://soundLooper.org/news">

<xsl:output method="html"/>

<xsl:template match="ph:index">
   <html>
      <head>
		<title>
			<xsl:value-of select="ph:title"/>
		</title>
		</head>
		<body>
			<xsl:apply-templates/>
		</body>
   </html>
</xsl:template>

<xsl:template match="ph:index/ph:title">
   <h1><xsl:apply-templates/></h1>
</xsl:template>

<xsl:template match="entry">
   <!--img src="{concat(.,'.jpg')}" align="right"/-->
   <xsl:variable name="endOfFileName" select="substring(text(),13)"/>
   <H2><b>********** V<xsl:value-of select="substring($endOfFileName, 0, string-length($endOfFileName) - 3)"/> **********</b></H2>
   <xsl:apply-templates select="document(.)"/>
   <!--br clear="right"/-->
</xsl:template>

<xsl:template match="news/add">
   <h3><b>- Ajouts -</b></h3>
   <xsl:apply-templates/>
</xsl:template>
<xsl:template match="news/correction">
   <h3><b>- Corrections -</b></h3>
   <xsl:apply-templates/>
</xsl:template>

<xsl:template match="content">
   - <xsl:value-of select="text()" /><BR/>
</xsl:template>

</xsl:stylesheet>