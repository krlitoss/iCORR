<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:cfdi="http://www.sat.gob.mx/cfd/3" xmlns:cce11="http://www.sat.gob.mx/ComercioExterior11" xmlns:donat="http://www.sat.gob.mx/donat" xmlns:divisas="http://www.sat.gob.mx/divisas" xmlns:implocal="http://www.sat.gob.mx/implocal" xmlns:leyendasFisc="http://www.sat.gob.mx/leyendasFiscales" xmlns:pfic="http://www.sat.gob.mx/pfic" xmlns:tpe="http://www.sat.gob.mx/TuristaPasajeroExtranjero" xmlns:nomina12="http://www.sat.gob.mx/nomina12" xmlns:registrofiscal="http://www.sat.gob.mx/registrofiscal" xmlns:pagoenespecie="http://www.sat.gob.mx/pagoenespecie" xmlns:aerolineas="http://www.sat.gob.mx/aerolineas" xmlns:valesdedespensa="http://www.sat.gob.mx/valesdedespensa" xmlns:consumodecombustibles="http://www.sat.gob.mx/consumodecombustibles" xmlns:notariospublicos="http://www.sat.gob.mx/notariospublicos" xmlns:vehiculousado="http://www.sat.gob.mx/vehiculousado" xmlns:servicioparcial="http://www.sat.gob.mx/servicioparcialconstruccion" xmlns:decreto="http://www.sat.gob.mx/renovacionysustitucionvehiculos" xmlns:destruccion="http://www.sat.gob.mx/certificadodestruccion" xmlns:obrasarte="http://www.sat.gob.mx/arteantiguedades" xmlns:ine="http://www.sat.gob.mx/ine" xmlns:iedu="http://www.sat.gob.mx/iedu" xmlns:ventavehiculos="http://www.sat.gob.mx/ventavehiculos" xmlns:terceros="http://www.sat.gob.mx/terceros" xmlns:pago10="http://www.sat.gob.mx/Pagos">

  <!-- Con el siguiente método se establece que la salida deberá ser en texto -->
  <xsl:output method="text" version="1.0" encoding="UTF-8" indent="no"/>
  <!--
		En esta sección se define la inclusión de las plantillas de utilerías para colapsar espacios
	-->
<!--  Manejador de datos requeridos  -->
<xsl:template name="Requerido">
<xsl:param name="valor"/>|<xsl:call-template name="ManejaEspacios">
<xsl:with-param name="s" select="$valor"/>
</xsl:call-template>
</xsl:template>
<!--  Manejador de datos opcionales  -->
<xsl:template name="Opcional">
<xsl:param name="valor"/>
<xsl:if test="$valor">|<xsl:call-template name="ManejaEspacios">
<xsl:with-param name="s" select="$valor"/>
</xsl:call-template>
</xsl:if>
</xsl:template>
<!--  Normalizador de espacios en blanco  -->
<xsl:template name="ManejaEspacios">
<xsl:param name="s"/>
<xsl:value-of select="normalize-space(string($s))"/>
</xsl:template>



<!--  Manejador de nodos tipo implocal  -->
<xsl:template match="implocal:ImpuestosLocales">
<!--
Iniciamos el tratamiento de los atributos de ImpuestosLocales 
-->
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@version"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@TotaldeRetenciones"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@TotaldeTraslados"/>
</xsl:call-template>
<xsl:for-each select="implocal:RetencionesLocales">
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@ImpLocRetenido"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@TasadeRetencion"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@Importe"/>
</xsl:call-template>
</xsl:for-each>
<xsl:for-each select="implocal:TrasladosLocales">
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@ImpLocTrasladado"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@TasadeTraslado"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@Importe"/>
</xsl:call-template>
</xsl:for-each>
</xsl:template>



<xsl:template match="nomina12:Nomina">
<!-- Manejador de nodos tipo Nomina -->
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@Version"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@TipoNomina"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@FechaPago"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@FechaInicialPago"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@FechaFinalPago"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@NumDiasPagados"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@TotalPercepciones"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@TotalDeducciones"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@TotalOtrosPagos"/>
</xsl:call-template>
<!--
  Iniciamos el manejo de los elementos hijo en la secuencia 
-->
<xsl:for-each select="./nomina12:Emisor">
<xsl:apply-templates select="."/>
</xsl:for-each>
<xsl:for-each select="./nomina12:Receptor">
<xsl:apply-templates select="."/>
</xsl:for-each>
<xsl:for-each select="./nomina12:Percepciones">
<xsl:apply-templates select="."/>
</xsl:for-each>
<xsl:for-each select="./nomina12:Deducciones">
<xsl:apply-templates select="."/>
</xsl:for-each>
<xsl:for-each select="./nomina12:OtrosPagos">
<xsl:apply-templates select="."/>
</xsl:for-each>
<xsl:for-each select="./nomina12:Incapacidades">
<xsl:apply-templates select="."/>
</xsl:for-each>
</xsl:template>
<xsl:template match="nomina12:Emisor">
<!-- Manejador de nodos tipo nomina12:Emisor -->
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@Curp"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@RegistroPatronal"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@RfcPatronOrigen"/>
</xsl:call-template>
<!--
  Iniciamos el tratamiento de los atributos de nomina12:EntidadSNCF
-->
<xsl:for-each select="./nomina12:EntidadSNCF">
<xsl:apply-templates select="."/>
</xsl:for-each>
</xsl:template>
<!--
  Iniciamos el manejo de los elementos hijo en la secuencia EntidadSNCF
-->
<xsl:template match="nomina12:EntidadSNCF">
<!--   Iniciamos el manejo de los nodos dependientes  -->
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@OrigenRecurso"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@MontoRecursoPropio"/>
</xsl:call-template>
</xsl:template>
<xsl:template match="nomina12:Receptor">
<!-- Manejador de nodos tipo nomina12:Receptor -->
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@Curp"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@NumSeguridadSocial"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@FechaInicioRelLaboral"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@Antigüedad"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@TipoContrato"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@Sindicalizado"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@TipoJornada"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@TipoRegimen"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@NumEmpleado"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@Departamento"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@Puesto"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@RiesgoPuesto"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@PeriodicidadPago"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@Banco"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@CuentaBancaria"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@SalarioBaseCotApor"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@SalarioDiarioIntegrado"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@ClaveEntFed"/>
</xsl:call-template>
<!--
  Iniciamos el tratamiento de los atributos de nomina12:SubContratacion
-->
<xsl:for-each select="./nomina12:SubContratacion">
<xsl:apply-templates select="."/>
</xsl:for-each>
</xsl:template>
<!--
  Iniciamos el manejo de los elementos hijo en la secuencia SubContratacion
-->
<xsl:template match="nomina12:SubContratacion">
<!--   Iniciamos el manejo de los nodos dependientes  -->
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@RfcLabora"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@PorcentajeTiempo"/>
</xsl:call-template>
</xsl:template>
<xsl:template match="nomina12:Percepciones">
<!-- Manejador de nodos tipo nomina12:Percepciones -->
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@TotalSueldos"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@TotalSeparacionIndemnizacion"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@TotalJubilacionPensionRetiro"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@TotalGravado"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@TotalExento"/>
</xsl:call-template>
<!--
  Iniciamos el tratamiento de los atributos de nomina12:Percepcion
-->
<xsl:for-each select="./nomina12:Percepcion">
<xsl:apply-templates select="."/>
</xsl:for-each>
<!--
  Iniciamos el tratamiento de los atributos de nomina12:JubilacionPensionRetiro
-->
<xsl:for-each select="./nomina12:JubilacionPensionRetiro">
<xsl:apply-templates select="."/>
</xsl:for-each>
<!--
  Iniciamos el tratamiento de los atributos de nomina12:SeparacionIndemnizacion
-->
<xsl:for-each select="./nomina12:SeparacionIndemnizacion">
<xsl:apply-templates select="."/>
</xsl:for-each>
</xsl:template>
<!--
  Iniciamos el manejo de los elementos hijo en la secuencia Percepcion
-->
<xsl:template match="nomina12:Percepcion">
<!-- Manejador de nodos tipo nomina12:Percepcion -->
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@TipoPercepcion"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@Clave"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@Concepto"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@ImporteGravado"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@ImporteExento"/>
</xsl:call-template>
<!--
  Iniciamos el tratamiento de los atributos de nomina12:AccionesOTitulos
-->
<xsl:for-each select="./nomina12:AccionesOTitulos">
<xsl:apply-templates select="."/>
</xsl:for-each>
<!--
  Iniciamos el tratamiento de los atributos de nomina12:HorasExtra
-->
<xsl:for-each select="./nomina12:HorasExtra">
<xsl:apply-templates select="."/>
</xsl:for-each>
</xsl:template>
<!--
  Iniciamos el manejo de los elementos hijo en la secuencia AccionesOTitulos
-->
<xsl:template match="nomina12:AccionesOTitulos">
<!--   Iniciamos el manejo de los nodos dependientes  -->
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@ValorMercado"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@PrecioAlOtorgarse"/>
</xsl:call-template>
</xsl:template>
<!--
  Iniciamos el manejo de los elementos hijo en la secuencia HorasExtra
-->
<xsl:template match="nomina12:HorasExtra">
<!--   Iniciamos el manejo de los nodos dependientes  -->
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@Dias"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@TipoHoras"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@HorasExtra"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@ImportePagado"/>
</xsl:call-template>
</xsl:template>
<!--
  Iniciamos el manejo de los elementos hijo en la secuencia JubilacionPensionRetiro
-->
<xsl:template match="nomina12:JubilacionPensionRetiro">
<!--
Manejador de nodos tipo nomina12:JubilacionPensionRetiro
-->
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@TotalUnaExhibicion"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@TotalParcialidad"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@MontoDiario"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@IngresoAcumulable"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@IngresoNoAcumulable"/>
</xsl:call-template>
</xsl:template>
<!--
  Iniciamos el manejo de los elementos hijo en la secuencia SeparacionIndemnizacion
-->
<xsl:template match="nomina12:SeparacionIndemnizacion">
<!--
Manejador de nodos tipo nomina12:JubilacionPensionRetiro
-->
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@TotalPagado"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@NumAñosServicio"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@UltimoSueldoMensOrd"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@IngresoAcumulable"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@IngresoNoAcumulable"/>
</xsl:call-template>
</xsl:template>
<xsl:template match="nomina12:Deducciones">
<!-- Manejador de nodos tipo nomina12:Deducciones -->
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@TotalOtrasDeducciones"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@TotalImpuestosRetenidos"/>
</xsl:call-template>
<!--
  Iniciamos el tratamiento de los atributos de nomina12:Deduccion
-->
<xsl:for-each select="./nomina12:Deduccion">
<xsl:apply-templates select="."/>
</xsl:for-each>
</xsl:template>
<!--
  Iniciamos el manejo de los elementos hijo en la secuencia Deduccion
-->
<xsl:template match="nomina12:Deduccion">
<!-- Manejador de nodos tipo nomina12:Deduccion -->
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@TipoDeduccion"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@Clave"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@Concepto"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@Importe"/>
</xsl:call-template>
</xsl:template>
<xsl:template match="nomina12:OtrosPagos">
<!--
  Iniciamos el tratamiento de los atributos de nomina12:OtroPago
-->
<xsl:for-each select="./nomina12:OtroPago">
<xsl:apply-templates select="."/>
</xsl:for-each>
</xsl:template>
<!--
  Iniciamos el manejo de los elementos hijo en la secuencia OtroPago
-->
<xsl:template match="nomina12:OtroPago">
<!-- Manejador de nodos tipo nomina12:OtroPago -->
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@TipoOtroPago"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@Clave"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@Concepto"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@Importe"/>
</xsl:call-template>
<!--
  Iniciamos el tratamiento de los atributos de nomina12:SubsidioAlEmpleo
-->
<xsl:for-each select="./nomina12:SubsidioAlEmpleo">
<xsl:apply-templates select="."/>
</xsl:for-each>
<!--
  Iniciamos el tratamiento de los atributos de nomina12:CompensacionSaldosAFavor
-->
<xsl:for-each select="./nomina12:CompensacionSaldosAFavor">
<xsl:apply-templates select="."/>
</xsl:for-each>
</xsl:template>
<!--
  Iniciamos el manejo de los elementos hijo en la secuencia SubsidioAlEmpleo
-->
<xsl:template match="nomina12:SubsidioAlEmpleo">
<!-- Manejador de nodos tipo nomina12:SubsidioAlEmpleo -->
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@SubsidioCausado"/>
</xsl:call-template>
</xsl:template>
<!--
  Iniciamos el manejo de los elementos hijo en la secuencia CompensacionSaldosAFavor
-->
<xsl:template match="nomina12:CompensacionSaldosAFavor">
<!--
Manejador de nodos tipo nomina12:CompensacionSaldosAFavor
-->
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@SaldoAFavor"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@Año"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@RemanenteSalFav"/>
</xsl:call-template>
</xsl:template>
<xsl:template match="nomina12:Incapacidades">
<!--
  Iniciamos el tratamiento de los atributos de nomina12:Incapacidades
-->
<xsl:for-each select="./nomina12:Incapacidad">
<xsl:apply-templates select="."/>
</xsl:for-each>
</xsl:template>
<!--
  Iniciamos el manejo de los elementos hijo en la secuencia Incapacidad
-->
<xsl:template match="nomina12:Incapacidad">
<!-- Manejador de nodos tipo nomina12:Incapacidad -->
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@DiasIncapacidad"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@TipoIncapacidad"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@ImporteMonetario"/>
</xsl:call-template>
</xsl:template>



<xsl:template match="pago10:Pagos">
<!-- Manejador de Atributos Pagos -->
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@Version"/>
</xsl:call-template>
<!--
  Iniciamos el manejo de los elementos hijo en la secuencia 
-->
<xsl:for-each select="./pago10:Pago">
<xsl:apply-templates select="."/>
</xsl:for-each>
</xsl:template>
<xsl:template match="pago10:Pago">
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@FechaPago"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@FormaDePagoP"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@MonedaP"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@TipoCambioP"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@Monto"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@NumOperacion"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@RfcEmisorCtaOrd"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@NomBancoOrdExt"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@CtaOrdenante"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@RfcEmisorCtaBen"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@CtaBeneficiario"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@TipoCadPago"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@CertPago"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@CadPago"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@SelloPago"/>
</xsl:call-template>
<!--
  Iniciamos el tratamiento de los atributos de pago10:DocumentoRelacionado
-->
<xsl:for-each select="./pago10:DoctoRelacionado">
<xsl:apply-templates select="."/>
</xsl:for-each>
<xsl:for-each select="./pago10:Impuestos">
<xsl:apply-templates select="."/>
</xsl:for-each>
</xsl:template>
<xsl:template match="pago10:DoctoRelacionado">
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@IdDocumento"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@Serie"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@Folio"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@MonedaDR"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@TipoCambioDR"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@MetodoDePagoDR"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@NumParcialidad"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@ImpSaldoAnt"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@ImpPagado"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@ImpSaldoInsoluto"/>
</xsl:call-template>
</xsl:template>
<xsl:template match="pago10:Impuestos">
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@TotalImpuestosRetenidos"/>
</xsl:call-template>
<xsl:call-template name="Opcional">
<xsl:with-param name="valor" select="./@TotalImpuestosTrasladados"/>
</xsl:call-template>
<xsl:apply-templates select="./pago10:Retenciones"/>
<xsl:apply-templates select="./pago10:Traslados"/>
</xsl:template>
<xsl:template match="pago10:Retenciones">
<xsl:for-each select="./pago10:Retencion">
<xsl:apply-templates select="."/>
</xsl:for-each>
</xsl:template>
<xsl:template match="pago10:Traslados">
<xsl:for-each select="./pago10:Traslado">
<xsl:apply-templates select="."/>
</xsl:for-each>
</xsl:template>
<xsl:template match="pago10:Retencion">
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@Impuesto"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@Importe"/>
</xsl:call-template>
</xsl:template>
<xsl:template match="pago10:Traslado">
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@Impuesto"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@TipoFactor"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@TasaOCuota"/>
</xsl:call-template>
<xsl:call-template name="Requerido">
<xsl:with-param name="valor" select="./@Importe"/>
</xsl:call-template>
</xsl:template>


  <!-- 
		En esta sección se define la inclusión de las demás plantillas de transformación para 
		la generación de las cadenas originales de los complementos fiscales 

  <xsl:include href="http://www.sat.gob.mx/sitio_internet/cfd/EstadoDeCuentaCombustible/ecc11.xslt"/> 
  <xsl:include href="http://www.sat.gob.mx/sitio_internet/cfd/donat/donat11.xslt"/>
  <xsl:include href="http://www.sat.gob.mx/sitio_internet/cfd/divisas/divisas.xslt"/>
  <xsl:include href="http://www.sat.gob.mx/sitio_internet/cfd/leyendasFiscales/leyendasFisc.xslt"/>
  <xsl:include href="http://www.sat.gob.mx/sitio_internet/cfd/pfic/pfic.xslt"/>
  <xsl:include href="http://www.sat.gob.mx/sitio_internet/cfd/TuristaPasajeroExtranjero/TuristaPasajeroExtranjero.xslt"/>
  <xsl:include href="http://www.sat.gob.mx/sitio_internet/cfd/cfdiregistrofiscal/cfdiregistrofiscal.xslt"/>
  <xsl:include href="http://www.sat.gob.mx/sitio_internet/cfd/pagoenespecie/pagoenespecie.xslt"/>
  <xsl:include href="http://www.sat.gob.mx/sitio_internet/cfd/aerolineas/aerolineas.xslt"/>
  <xsl:include href="http://www.sat.gob.mx/sitio_internet/cfd/valesdedespensa/valesdedespensa.xslt"/>
  <xsl:include href="http://www.sat.gob.mx/sitio_internet/cfd/consumodecombustibles/consumodecombustibles.xslt"/>
  <xsl:include href="http://www.sat.gob.mx/sitio_internet/cfd/notariospublicos/notariospublicos.xslt"/>
  <xsl:include href="http://www.sat.gob.mx/sitio_internet/cfd/vehiculousado/vehiculousado.xslt"/>
  <xsl:include href="http://www.sat.gob.mx/sitio_internet/cfd/servicioparcialconstruccion/servicioparcialconstruccion.xslt"/>
  <xsl:include href="http://www.sat.gob.mx/sitio_internet/cfd/renovacionysustitucionvehiculos/renovacionysustitucionvehiculos.xslt"/>
  <xsl:include href="http://www.sat.gob.mx/sitio_internet/cfd/certificadodestruccion/certificadodedestruccion.xslt"/>
  <xsl:include href="http://www.sat.gob.mx/sitio_internet/cfd/arteantiguedades/obrasarteantiguedades.xslt"/>
  <xsl:include href="http://www.sat.gob.mx/sitio_internet/cfd/ComercioExterior11/ComercioExterior11.xslt"/>
  <xsl:include href="http://www.sat.gob.mx/sitio_internet/cfd/ine/ine11.xslt"/>
  <xsl:include href="http://www.sat.gob.mx/sitio_internet/cfd/iedu/iedu.xslt"/>
  <xsl:include href="http://www.sat.gob.mx/sitio_internet/cfd/ventavehiculos/ventavehiculos11.xslt"/>
  <xsl:include href="http://www.sat.gob.mx/sitio_internet/cfd/terceros/terceros11.xslt"/>
	-->
	
  <!-- Aquí iniciamos el procesamiento de la cadena original con su | inicial y el terminador || -->
  <xsl:template match="/">|<xsl:apply-templates select="/cfdi:Comprobante"/>||</xsl:template>
  <!--  Aquí iniciamos el procesamiento de los datos incluidos en el comprobante -->
  <xsl:template match="cfdi:Comprobante">
    <!-- Iniciamos el tratamiento de los atributos de comprobante -->
    <xsl:call-template name="Requerido">
      <xsl:with-param name="valor" select="./@Version"/>
    </xsl:call-template>
    <xsl:call-template name="Opcional">
      <xsl:with-param name="valor" select="./@Serie"/>
    </xsl:call-template>
    <xsl:call-template name="Opcional">
      <xsl:with-param name="valor" select="./@Folio"/>
    </xsl:call-template>
    <xsl:call-template name="Requerido">
      <xsl:with-param name="valor" select="./@Fecha"/>
    </xsl:call-template>
    <xsl:call-template name="Opcional">
      <xsl:with-param name="valor" select="./@FormaPago"/>
    </xsl:call-template>
    <xsl:call-template name="Requerido">
      <xsl:with-param name="valor" select="./@NoCertificado"/>
    </xsl:call-template>
    <xsl:call-template name="Opcional">
      <xsl:with-param name="valor" select="./@CondicionesDePago"/>
    </xsl:call-template>
    <xsl:call-template name="Requerido">
      <xsl:with-param name="valor" select="./@SubTotal"/>
    </xsl:call-template>
    <xsl:call-template name="Opcional">
      <xsl:with-param name="valor" select="./@Descuento"/>
    </xsl:call-template>
    <xsl:call-template name="Requerido">
      <xsl:with-param name="valor" select="./@Moneda"/>
    </xsl:call-template>
    <xsl:call-template name="Opcional">
      <xsl:with-param name="valor" select="./@TipoCambio"/>
    </xsl:call-template>
    <xsl:call-template name="Requerido">
      <xsl:with-param name="valor" select="./@Total"/>
    </xsl:call-template>
    <xsl:call-template name="Requerido">
      <xsl:with-param name="valor" select="./@TipoDeComprobante"/>
    </xsl:call-template>
    <xsl:call-template name="Opcional">
      <xsl:with-param name="valor" select="./@MetodoPago"/>
    </xsl:call-template>
    <xsl:call-template name="Requerido">
      <xsl:with-param name="valor" select="./@LugarExpedicion"/>
    </xsl:call-template>
    <xsl:call-template name="Opcional">
      <xsl:with-param name="valor" select="./@Confirmacion"/>
    </xsl:call-template>
    <!--
			Llamadas para procesar al los sub nodos del comprobante
		-->
    <xsl:apply-templates select="./cfdi:CfdiRelacionados"/>
    <xsl:apply-templates select="./cfdi:Emisor"/>
    <xsl:apply-templates select="./cfdi:Receptor"/>
    <xsl:apply-templates select="./cfdi:Conceptos"/>
    <xsl:apply-templates select="./cfdi:Impuestos"/>
    <xsl:for-each select="./cfdi:Complemento">
      <xsl:apply-templates select="."/>
    </xsl:for-each>
  </xsl:template>

  <!-- Manejador de nodos tipo CFDIRelacionados -->
  <xsl:template match="cfdi:CfdiRelacionados">
    <!-- Iniciamos el tratamiento de los atributos del CFDIRelacionados -->
    <xsl:call-template name="Requerido">
      <xsl:with-param name="valor" select="./@TipoRelacion"/>
    </xsl:call-template>
    <xsl:for-each select="./cfdi:CfdiRelacionado">
      <xsl:call-template name="Requerido">
        <xsl:with-param name="valor" select="./@UUID"/>
      </xsl:call-template>
    </xsl:for-each>
  </xsl:template>

  <!-- Manejador de nodos tipo Emisor -->
  <xsl:template match="cfdi:Emisor">
    <!-- Iniciamos el tratamiento de los atributos del Emisor -->
    <xsl:call-template name="Requerido">
      <xsl:with-param name="valor" select="./@Rfc"/>
    </xsl:call-template>
    <xsl:call-template name="Opcional">
      <xsl:with-param name="valor" select="./@Nombre"/>
    </xsl:call-template>
    <xsl:call-template name="Requerido">
      <xsl:with-param name="valor" select="./@RegimenFiscal"/>
    </xsl:call-template>
  </xsl:template>

  <!-- Manejador de nodos tipo Receptor -->
  <xsl:template match="cfdi:Receptor">
    <!-- Iniciamos el tratamiento de los atributos del Receptor -->
    <xsl:call-template name="Requerido">
      <xsl:with-param name="valor" select="./@Rfc"/>
    </xsl:call-template>
    <xsl:call-template name="Opcional">
      <xsl:with-param name="valor" select="./@Nombre"/>
    </xsl:call-template>
    <xsl:call-template name="Opcional">
      <xsl:with-param name="valor" select="./@ResidenciaFiscal"/>
    </xsl:call-template>
    <xsl:call-template name="Opcional">
      <xsl:with-param name="valor" select="./@NumRegIdTrib"/>
    </xsl:call-template>
    <xsl:call-template name="Requerido">
      <xsl:with-param name="valor" select="./@UsoCFDI"/>
    </xsl:call-template>

  </xsl:template>

  <!-- Manejador de nodos tipo Conceptos -->
  <xsl:template match="cfdi:Conceptos">
    <!-- Llamada para procesar los distintos nodos tipo Concepto -->
    <xsl:for-each select="./cfdi:Concepto">
      <xsl:apply-templates select="."/>
    </xsl:for-each>
  </xsl:template>

  <!--Manejador de nodos tipo Concepto-->
  <xsl:template match="cfdi:Concepto">
    <!-- Iniciamos el tratamiento de los atributos del Concepto -->
    <xsl:call-template name="Requerido">
      <xsl:with-param name="valor" select="./@ClaveProdServ"/>
    </xsl:call-template>
    <xsl:call-template name="Opcional">
      <xsl:with-param name="valor" select="./@NoIdentificacion"/>
    </xsl:call-template>
    <xsl:call-template name="Requerido">
      <xsl:with-param name="valor" select="./@Cantidad"/>
    </xsl:call-template>
    <xsl:call-template name="Requerido">
      <xsl:with-param name="valor" select="./@ClaveUnidad"/>
    </xsl:call-template>
    <xsl:call-template name="Opcional">
      <xsl:with-param name="valor" select="./@Unidad"/>
    </xsl:call-template>
    <xsl:call-template name="Requerido">
      <xsl:with-param name="valor" select="./@Descripcion"/>
    </xsl:call-template>
    <xsl:call-template name="Requerido">
      <xsl:with-param name="valor" select="./@ValorUnitario"/>
    </xsl:call-template>
    <xsl:call-template name="Requerido">
      <xsl:with-param name="valor" select="./@Importe"/>
    </xsl:call-template>
    <xsl:call-template name="Opcional">
      <xsl:with-param name="valor" select="./@Descuento"/>
    </xsl:call-template>

    <!-- Manejo de sub nodos de información Traslado de Conceptos:Concepto:Impuestos:Traslados-->
    <xsl:for-each select="./cfdi:Impuestos/cfdi:Traslados/cfdi:Traslado">
      <xsl:call-template name="Requerido">
        <xsl:with-param name="valor" select="./@Base"/>
      </xsl:call-template>
      <xsl:call-template name="Requerido">
        <xsl:with-param name="valor" select="./@Impuesto"/>
      </xsl:call-template>
      <xsl:call-template name="Requerido">
        <xsl:with-param name="valor" select="./@TipoFactor"/>
      </xsl:call-template>
      <xsl:call-template name="Opcional">
        <xsl:with-param name="valor" select="./@TasaOCuota"/>
      </xsl:call-template>
      <xsl:call-template name="Opcional">
        <xsl:with-param name="valor" select="./@Importe"/>
      </xsl:call-template>
    </xsl:for-each>

    <!-- Manejo de sub nodos de Retencion por cada una de los Conceptos:Concepto:Impuestos:Retenciones-->
    <xsl:for-each select="./cfdi:Impuestos/cfdi:Retenciones/cfdi:Retencion">
      <xsl:call-template name="Requerido">
        <xsl:with-param name="valor" select="./@Base"/>
      </xsl:call-template>
      <xsl:call-template name="Requerido">
        <xsl:with-param name="valor" select="./@Impuesto"/>
      </xsl:call-template>
      <xsl:call-template name="Requerido">
        <xsl:with-param name="valor" select="./@TipoFactor"/>
      </xsl:call-template>
      <xsl:call-template name="Requerido">
        <xsl:with-param name="valor" select="./@TasaOCuota"/>
      </xsl:call-template>
      <xsl:call-template name="Requerido">
        <xsl:with-param name="valor" select="./@Importe"/>
      </xsl:call-template>
    </xsl:for-each>

    <!-- Manejo de los distintos sub nodos de información aduanera de forma indistinta a su grado de dependencia -->
    <xsl:for-each select="./cfdi:InformacionAduanera">
      <xsl:apply-templates select="."/>
    </xsl:for-each>

    <!-- Llamada al manejador de nodos de CuentaPredial en caso de existir -->
    <xsl:if test="./cfdi:CuentaPredial">
      <xsl:apply-templates select="./cfdi:CuentaPredial"/>
    </xsl:if>

    <!-- Llamada al manejador de nodos de ComplementoConcepto en caso de existir -->
    <xsl:if test="./cfdi:ComplementoConcepto">
      <xsl:apply-templates select="./cfdi:ComplementoConcepto"/>
    </xsl:if>

    <!-- Llamada al manejador de nodos de Parte en caso de existir -->
    <xsl:for-each select=".//cfdi:Parte">
      <xsl:apply-templates select="."/>
    </xsl:for-each>
  </xsl:template>

  <!-- Manejador de nodos tipo Información Aduanera -->
  <xsl:template match="cfdi:InformacionAduanera">
    <!-- Manejo de los atributos de la información aduanera -->
    <xsl:call-template name="Requerido">
      <xsl:with-param name="valor" select="./@NumeroPedimento"/>
    </xsl:call-template>
  </xsl:template>

  <!-- Manejador de nodos tipo Información CuentaPredial -->
  <xsl:template match="cfdi:CuentaPredial">
    <xsl:call-template name="Requerido">
      <xsl:with-param name="valor" select="./@Numero"/>
    </xsl:call-template>
  </xsl:template>

  <!-- Manejador de nodos tipo ComplementoConcepto -->
  <xsl:template match="cfdi:ComplementoConcepto">
    <xsl:for-each select="./*">
      <xsl:apply-templates select="."/>
    </xsl:for-each>
  </xsl:template>

  <!-- Manejador de nodos tipo Parte -->
  <xsl:template match="cfdi:Parte">
    <!-- Iniciamos el tratamiento de los atributos de Parte-->
    <xsl:call-template name="Requerido">
      <xsl:with-param name="valor" select="./@ClaveProdServ"/>
    </xsl:call-template>
    <xsl:call-template name="Opcional">
      <xsl:with-param name="valor" select="./@NoIdentificacion"/>
    </xsl:call-template>
    <xsl:call-template name="Requerido">
      <xsl:with-param name="valor" select="./@Cantidad"/>
    </xsl:call-template>
    <xsl:call-template name="Opcional">
      <xsl:with-param name="valor" select="./@Unidad"/>
    </xsl:call-template>
    <xsl:call-template name="Requerido">
      <xsl:with-param name="valor" select="./@Descripcion"/>
    </xsl:call-template>
    <xsl:call-template name="Opcional">
      <xsl:with-param name="valor" select="./@ValorUnitario"/>
    </xsl:call-template>
    <xsl:call-template name="Opcional">
      <xsl:with-param name="valor" select="./@Importe"/>
    </xsl:call-template>

    <!-- Manejador de nodos tipo InformacionAduanera-->
    <xsl:for-each select=".//cfdi:InformacionAduanera">
      <xsl:apply-templates select="."/>
    </xsl:for-each>
  </xsl:template>

  <!-- Manejador de nodos tipo Complemento -->
  <xsl:template match="cfdi:Complemento">
    <xsl:for-each select="./*">
      <xsl:apply-templates select="."/>
    </xsl:for-each>
  </xsl:template>

  <!-- Manejador de nodos tipo Domicilio fiscal -->
  <xsl:template match="cfdi:Impuestos">
    <!-- Manejo de sub nodos de Retencion por cada una de los Impuestos:Retenciones-->
    <xsl:for-each select="./cfdi:Retenciones/cfdi:Retencion">
      <xsl:call-template name="Requerido">
        <xsl:with-param name="valor" select="./@Impuesto"/>
      </xsl:call-template>
      <xsl:call-template name="Requerido">
        <xsl:with-param name="valor" select="./@Importe"/>
      </xsl:call-template>
    </xsl:for-each>
    <!-- Iniciamos el tratamiento de los atributos de TotalImpuestosRetenidos-->
    <xsl:call-template name="Opcional">
      <xsl:with-param name="valor" select="./@TotalImpuestosRetenidos"/>
    </xsl:call-template>
    <!-- Manejo de sub nodos de información Traslado de Impuestos:Traslados-->
    <xsl:for-each select="./cfdi:Traslados/cfdi:Traslado">
      <xsl:call-template name="Requerido">
        <xsl:with-param name="valor" select="./@Impuesto"/>
      </xsl:call-template>
      <xsl:call-template name="Requerido">
        <xsl:with-param name="valor" select="./@TipoFactor"/>
      </xsl:call-template>
      <xsl:call-template name="Requerido">
        <xsl:with-param name="valor" select="./@TasaOCuota"/>
      </xsl:call-template>
      <xsl:call-template name="Requerido">
        <xsl:with-param name="valor" select="./@Importe"/>
      </xsl:call-template>
    </xsl:for-each>
    <!-- Iniciamos el tratamiento de los atributos de TotalImpuestosTrasladados-->
    <xsl:call-template name="Opcional">
      <xsl:with-param name="valor" select="./@TotalImpuestosTrasladados"/>
    </xsl:call-template>
  </xsl:template>
</xsl:stylesheet>
