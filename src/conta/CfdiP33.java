package conta;

import java.text.NumberFormat;

import org.w3c.dom.Element;

import conta.ComplementoPagoCfdi.DoctoRelacionado;

public class CfdiP33 extends Cfdi33 {
	
	Element nodoComplemento;
	Element nodoPagos;
	Element nodoPago;
	String pagoPrefix = "pago10:";
	NumberFormat pagoNumFormat = NumberFormat.getNumberInstance();
	
	public CfdiP33(){
		super(0);
		nodoComprobante.setAttribute("TipoDeComprobante", "P");
		
	    nodoComprobante.setAttribute("xmlns:pago10", "http://www.sat.gob.mx/Pagos");
	    nodoComprobante.setAttribute("xsi:schemaLocation", "http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv32.xsd http://www.sat.gob.mx/Pagos http://www.sat.gob.mx/sitio_internet/cfd/Pagos/Pagos10.xsd");
		
		nodoComplemento = xmlDoc.createElement(domainPrefix + "Complemento");
	    nodoComprobante.appendChild(nodoComplemento);
	    nodoPagos = xmlDoc.createElement(pagoPrefix + "Pagos");
	    nodoComplemento.appendChild(nodoPagos);
	    nodoPagos.setAttribute("Version", "1.0");
	    nodoPago = xmlDoc.createElement(pagoPrefix + "Pago");
	    nodoPagos.appendChild(nodoPago);
		
		setMoneda("XXX");
		
		PartidaCfdi partida = new PartidaCfdi(1D,null,"ACT",null,"84111506","Pago",0D,null);
		agregarPartida(partida);
		
	    //Formateador de decimales
		pagoNumFormat.setMinimumFractionDigits(2);
		pagoNumFormat.setMaximumFractionDigits(2);
		pagoNumFormat.setGroupingUsed(false);
	}
	
	public void setFormaPago(String s){
		throw new RuntimeException("Este campo no debe existir en los comprobantes tipo Pago");
	}
	public void setCondicionesDePago(String s){
		throw new RuntimeException("Este campo no debe existir en los comprobantes tipo Pago");
	}
	public void setTipoCambio(Double s){
		throw new RuntimeException("Este campo no debe existir en los comprobantes tipo Pago");
	}
	public void setMetodoPago(String s){
		throw new RuntimeException("Este campo no debe existir en los comprobantes tipo Pago");
	}
	
	
	public void setComplementoPago(ComplementoPagoCfdi c){
		nodoPago.setAttribute("FechaPago", dformat.format(c.getFechaPago()));
		nodoPago.setAttribute("FormaDePagoP", limpiar(c.getFormaDePagoP()));
		nodoPago.setAttribute("MonedaP",  limpiar(c.getMonedaP()));
		if (c.getTipoCambioP()!=null)
			nodoPago.setAttribute("TipoCambioP", pagoNumFormat.format(c.getTipoCambioP()));
		nodoPago.setAttribute("Monto", pagoNumFormat.format(c.getMonto()));
		if (c.getNumOperacion()!=null)
			nodoPago.setAttribute("NumOperacion", limpiar(c.getNumOperacion()));
		if (c.getRfcEmisorCtaOrd()!=null)
			nodoPago.setAttribute("RfcEmisorCtaOrd", limpiar(c.getRfcEmisorCtaOrd()));
		if (c.getNomBancoOrdExt()!=null)
			nodoPago.setAttribute("NomBancoOrdExt", limpiar(c.getNomBancoOrdExt()));
		if (c.getCtaOrdenante()!=null)
			nodoPago.setAttribute("CtaOrdenante", limpiar(c.getCtaOrdenante()));
		if (c.getRfcEmisorCtaBen()!=null)
			nodoPago.setAttribute("RfcEmisorCtaBen", limpiar(c.getRfcEmisorCtaBen()));
		if (c.getCtaBeneficiario()!=null)
			nodoPago.setAttribute("CtaBeneficiario", limpiar(c.getCtaBeneficiario()));
		if (c.getTipoCadPago()!=null)
			nodoPago.setAttribute("TipoCadPago", limpiar(c.getTipoCadPago()));
		if (c.getCertPago()!=null)
			nodoPago.setAttribute("CertPago", limpiar(c.getCertPago()));
		if (c.getCadPago()!=null)
			nodoPago.setAttribute("CadPago", limpiar(c.getCadPago()));
		if (c.getSelloPago()!=null)
			nodoPago.setAttribute("SelloPago", limpiar(c.getSelloPago()));
		

		Element nodoDoctoRelacionado;
		for (DoctoRelacionado r : c.getDocumentos()){
			nodoDoctoRelacionado = xmlDoc.createElement(pagoPrefix + "DoctoRelacionado");
			nodoPago.appendChild(nodoDoctoRelacionado);
			
			nodoDoctoRelacionado.setAttribute("IdDocumento", limpiar(r.getIdDocumento()));
			if (r.getSerie()!=null)
				nodoDoctoRelacionado.setAttribute("Serie", limpiar(r.getSerie()));
			nodoDoctoRelacionado.setAttribute("Folio", limpiar(r.getFolio()));
			nodoDoctoRelacionado.setAttribute("MonedaDR", limpiar(r.getMonedaDR()));
			if (r.getTipoCambioDR()!=null)
				nodoDoctoRelacionado.setAttribute("TipoCambioDR", pagoNumFormat.format(r.getTipoCambioDR()));
			nodoDoctoRelacionado.setAttribute("MetodoDePagoDR", limpiar(r.getMetodoDePagoDR()));
			nodoDoctoRelacionado.setAttribute("NumParcialidad", r.getNumParcialidad().toString());
			nodoDoctoRelacionado.setAttribute("ImpSaldoAnt", pagoNumFormat.format(r.getImpSaldoAnt()));
			nodoDoctoRelacionado.setAttribute("ImpPagado", pagoNumFormat.format(r.getImpPagado()));
			nodoDoctoRelacionado.setAttribute("ImpSaldoInsoluto", pagoNumFormat.format(r.getImpSaldoInsoluto()));
		}
		
	}
}
