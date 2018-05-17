package conta;


/**
 * Se utiliza esta clase para convertir claves SAT en texto
 * 
 * 
 * @author alberto
 *
 */
public class SatTools {

	   
	   public static String parseSat(String tipo, String clave){
		   if (tipo.equals("IMPUESTO")){
			   if (clave.equals("001"))
				   return "ISR";
			   else if (clave.equals("002"))
				   return "IVA";
			   else if (clave.equals("003"))
				   return "IEPS";
		   }
		   if (tipo.equals("USO")){
			   if (clave.equals("G01"))
				   return "Adquisición de mercancias";
			   else if (clave.equals("G02"))
				   return "Devoluciones, descuentos o bonificaciones";
			   else if (clave.equals("G03"))
				   return "Gastos en general";
			   else if (clave.equals("I01"))
				   return "Construcciones";
			   else if (clave.equals("I02"))
				   return "Mobilario y equipo de oficina por inversiones";
			   else if (clave.equals("I03"))
				   return "Equipo de transporte";
			   else if (clave.equals("I04"))
				   return "Equipo de computo y accesorios";
			   else if (clave.equals("I05"))
				   return "Dados, troqueles, moldes, matrices y herramental";
			   else if (clave.equals("I06"))
				   return "Comunicaciones telefónicas";
			   else if (clave.equals("I07"))
				   return "Comunicaciones satelitales";
			   else if (clave.equals("I08"))
				   return "Otra maquinaria y equipo";
			   else if (clave.equals("D01"))
				   return "Honorarios médicos, dentales y gastos hospitalarios.";
			   else if (clave.equals("D02"))
				   return "Gastos médicos por incapacidad o discapacidad";
			   else if (clave.equals("D03"))
				   return "Gastos funerales.";
			   else if (clave.equals("D04"))
				   return "Donativos.";
			   else if (clave.equals("D05"))
				   return "Intereses reales efectivamente pagados por créditos hipotecarios (casa habitación).";
			   else if (clave.equals("D06"))
				   return "Aportaciones voluntarias al SAR.";
			   else if (clave.equals("D07"))
				   return "Primas por seguros de gastos médicos.";
			   else if (clave.equals("D08"))
				   return "Gastos de transportación escolar obligatoria.";
			   else if (clave.equals("D09"))
				   return "Depósitos en cuentas para el ahorro, primas que tengan como base planes de pensiones.";
			   else if (clave.equals("D10"))
				   return "Pagos por servicios educativos (colegiaturas)";
			   else if (clave.equals("P01"))
				   return "Por definir";
		   }
		   
		   if (tipo.equals("RELACION")){
			   if (clave.equals("01"))
				   return "Nota de crédito de los documentos relacionados";
			   if (clave.equals("02"))
				   return "Nota de débito de los documentos relacionados";
			   if (clave.equals("03"))
				   return "Devolución de mercancía sobre facturas o traslados previos";
			   if (clave.equals("04"))
				   return "Sustitución de los CFDI previos";
			   if (clave.equals("05"))
				   return "Traslados de mercancias facturados previamente";
			   if (clave.equals("06"))
				   return "Factura generada por los traslados previos";
			   if (clave.equals("07"))
				   return "CFDI por aplicación de anticipo";
		   }
		   
		   if (tipo.equals("TIPO_CFDI")){
			   if (clave.equals("I"))
				   return "Ingreso";
			   if (clave.equals("E"))
				   return "Egreso";
			   if (clave.equals("T"))
				   return "Traslado";
			   if (clave.equals("N"))
				   return "Nómina";
			   if (clave.equals("P"))
				   return "Pago";
		   }
		   
		   if (tipo.equals("REGIMEN")){
			   if (clave.equals("601"))
				   return "General de Ley Personas Morales";
			   if (clave.equals("603"))
				   return "Personas Morales con Fines no Lucrativos";
			   if (clave.equals("605"))
				   return "Sueldos y Salarios e Ingresos Asimilados a Salarios";
			   if (clave.equals("606"))
				   return "Arrendamiento";
			   if (clave.equals("608"))
				   return "Demás ingresos";
			   if (clave.equals("609"))
				   return "Consolidación";
			   if (clave.equals("610"))
				   return "Residentes en el Extranjero sin Establecimiento Permanente en México";
			   if (clave.equals("611"))
				   return "Ingresos por Dividendos (socios y accionistas)";
			   if (clave.equals("612"))
				   return "Personas Físicas con Actividades Empresariales y Profesionales";
			   if (clave.equals("614"))
				   return "Ingresos por intereses";
			   if (clave.equals("616"))
				   return "Sin obligaciones fiscales";
			   if (clave.equals("620"))
				   return "Sociedades Cooperativas de Producción que optan por diferir sus ingresos";
			   if (clave.equals("621"))
				   return "Incorporación Fiscal";
			   if (clave.equals("622"))
				   return "Actividades Agrícolas, Ganaderas, Silvícolas y Pesqueras";
			   if (clave.equals("623"))
				   return "Opcional para Grupos de Sociedades";
			   if (clave.equals("624"))
				   return "Coordinados";
			   if (clave.equals("628"))
				   return "Hidrocarburos";
			   if (clave.equals("607"))
				   return "Régimen de Enajenación o Adquisición de Bienes";
			   if (clave.equals("629"))
				   return "De los Regímenes Fiscales Preferentes y de las Empresas Multinacionales";
			   if (clave.equals("630"))
				   return "Enajenación de acciones en bolsa de valores";
			   if (clave.equals("615"))
				   return "Régimen de los ingresos por obtención de premios";
		   }
		   
		   if (tipo.equals("METODO_PAGO")){
			   if (clave.equals("PUE"))
				   return "Pago en una sola exhibición";
			   if (clave.equals("PPD"))
				   return "Pago en parcialidades o diferido";
		   }
		   
		   if (tipo.equals("FORMA_PAGO")){
			   if (clave.equals("01"))
				   return "Efectivo";
			   if (clave.equals("02"))
				   return "Cheque nominativo";
			   if (clave.equals("03"))
				   return "Transferencia electrónica de fondos";
			   if (clave.equals("04"))
				   return "Tarjeta de crédito";
			   if (clave.equals("05"))
				   return "Monedero electrónico";
			   if (clave.equals("06"))
				   return "Dinero electrónico";
			   if (clave.equals("08"))
				   return "Vales de despensa";
			   if (clave.equals("12"))
				   return "Dación en pago";
			   if (clave.equals("13"))
				   return "Pago por subrogación";
			   if (clave.equals("14"))
				   return "Pago por consignación";
			   if (clave.equals("15"))
				   return "Condonación";
			   if (clave.equals("17"))
				   return "Compensación";
			   if (clave.equals("23"))
				   return "Novación";
			   if (clave.equals("24"))
				   return "Confusión";
			   if (clave.equals("25"))
				   return "Remisión de deuda";
			   if (clave.equals("26"))
				   return "Prescripción o caducidad";
			   if (clave.equals("27"))
				   return "A satisfacción del acreedor";
			   if (clave.equals("28"))
				   return "Tarjeta de débito";
			   if (clave.equals("29"))
				   return "Tarjeta de servicios";
			   if (clave.equals("30"))
				   return "Aplicación de anticipos";
			   if (clave.equals("99"))
				   return "Por definir";
		   }
		   
		   return "";
	   }
}
