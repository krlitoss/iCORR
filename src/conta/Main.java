package conta;

import java.io.FileInputStream;
import java.util.Date;

import conta.ComplementoPagoCfdi.DoctoRelacionado;
import com.cfdi.exceptions.myXmlParsingException;


/**
 * Clase que genera pruebas
 *
 */
public class Main {

	public static void main(String[] args){
		crearFactura();
//		crearNotaDeCredito();
//		crearPago();
	}
	
	private static void crearFactura(){
		CfdiF33 cfdi = new CfdiF33();
		try{
			//cfdi.setCondicionesDePago("Contado");
			cfdi.setEmisor("SKARTON S.A. de C.V.", "SKA100902AK8", "601");
			cfdi.setFecha(new Date());
			cfdi.setFolio("1234");
			cfdi.setFormaPago("01");
			cfdi.setLugarExpedicion("14100");
			cfdi.setMetodoPago("PPD");
			cfdi.setMoneda("MXN");
			cfdi.setReceptor("YAKULT, S.A. DE C.V.", "YAK800303JA7", "G01");
			cfdi.setTipoCambio(1D);
			cfdi.setTipoRelacion("04");
			cfdi.addRelacionado("6EA9FB58-37CB-40CD-B599-535F12EA4F55");

			
			PartidaCfdi partida = new PartidaCfdi(1D,"PZA","H87","0001","24112500","CHAR YAKULT",100D,5D);
			partida.agregarImpuestoRetenido(100D, ImpuestoCfdi.IVA, ImpuestoCfdi.TASA, 0.16, 16D);
			partida.agregarImpuestoRetenido(100D, ImpuestoCfdi.ISR, ImpuestoCfdi.TASA, 0.10, 10D);
			partida.agregarImpuestoTrasladado(100D, ImpuestoCfdi.IVA, ImpuestoCfdi.TASA, 0.16, 16D);
			partida.agregarImpuestoTrasladado(100D, ImpuestoCfdi.IVA, ImpuestoCfdi.TASA, 0.16, 16D);
			cfdi.agregarPartida(partida);
			
			LbsCrypt crypt = new LbsCrypt(new FileInputStream("ACO560518KW7.key"),"12345678a",new FileInputStream("ACO560518KW7.cer"));
			cfdi.sellar(crypt);
			cfdi.timbrarFinkok("sistemas@skarton.com.mx","Sistemas2011*");
			//Hay que tener cuidado a partir de esta linea ya que el documento se encuentra timbrado desde este momento cualquier problema al guardar no importa el CFDI ya estara registrado en el SAT
			cfdi.saveXML("factura.xml");
			cfdi.savePdf("factura.pdf");
		}catch(Exception e){
			e.printStackTrace();
		} catch (myXmlParsingException e) {
			e.printStackTrace();
		}
	}

	private static void crearNotaDeCredito(){
		CfdiNC33 cfdi = new CfdiNC33();
		try{
			//cfdi.setCondicionesDePago("Contado");
			cfdi.setEmisor("Skartonpruebas S.A. de C.V.", "ACO560518KW7", "601");
			cfdi.setFecha(new Date());
			cfdi.setFolio("1234");
			cfdi.setFormaPago("01");
			cfdi.setLugarExpedicion("14100");
			cfdi.setMetodoPago("PUE");
			cfdi.setMoneda("MXN");
			cfdi.setReceptor("Publico en general", "XAXX010101000", "G01");
			cfdi.setTipoCambio(1D);
			cfdi.setTipoRelacion("01");
			cfdi.addRelacionado("6EA9FB58-37CB-40CD-B599-535F12EA4F55");
			cfdi.addRelacionado("6EA9FB58-37CB-40CD-B599-535F12EA4F55");
			cfdi.addRelacionado("6EA9FB58-37CB-40CD-B599-535F12EA4F55");
			
			PartidaCfdi partida = new PartidaCfdi(1D,"PZA","H87","0001","26101766","Regulador",100D,5D);
			partida.agregarImpuestoRetenido(100D, ImpuestoCfdi.IVA, ImpuestoCfdi.TASA, 0.16, 16D);
			partida.agregarImpuestoRetenido(100D, ImpuestoCfdi.ISR, ImpuestoCfdi.TASA, 0.10, 10D);
			partida.agregarImpuestoTrasladado(100D, ImpuestoCfdi.IVA, ImpuestoCfdi.TASA, 0.16, 16D);
			partida.agregarImpuestoTrasladado(100D, ImpuestoCfdi.IVA, ImpuestoCfdi.TASA, 0.16, 16D);
			cfdi.agregarPartida(partida);
			
			LbsCrypt crypt = new LbsCrypt(new FileInputStream("ACO560518KW7.key"),"12345678a",new FileInputStream("ACO560518KW7.cer"));
			cfdi.sellar(crypt);
			cfdi.timbrarFinkok("sistemas@skarton.com.mx","Sistemas2011*");
			//Hay que tener cuidado a partir de esta linea ya que el documento se encuentra timbrado desde este momento cualquier problema al guardar no importa el CFDI ya estara registrado en el SAT
			cfdi.saveXML("Notacredito.xml");
			cfdi.savePdf("Notacredito.pdf");
		}catch(Exception e){
			e.printStackTrace();
		} catch (myXmlParsingException e) {
			e.printStackTrace();
		}
	}
	
	private static void crearPago(){
		CfdiP33 cfdi = new CfdiP33();
		try{
			//Datos generales del CFDI
			cfdi.setEmisor("Skartonpruebas S.A. de C.V.", "ACO560518KW7", "601");
			cfdi.setFecha(new Date());
			cfdi.setFolio("1234");
			cfdi.setLugarExpedicion("14100");
			cfdi.setReceptor("Publico en general", "XAXX010101000", "P01");
			
			
			//Complemento de pago
			ComplementoPagoCfdi cp = new ComplementoPagoCfdi();
			cp.setFechaPago(new Date());
			cp.setFormaDePagoP("01");
			cp.setMonedaP("MXN");
//			cp.setTipoCambioP(1D); //Este campo es requerido solo si ModenaP es diferente a MXN
			cp.setMonto(100D);
//			cp.setCtaOrdenante("1234567890123456"); //Campo opcional
//			cp.setCtaBeneficiario("1234567890123456"); //Campo opcional
			
			DoctoRelacionado docto = cp.new DoctoRelacionado();
			docto.setIdDocumento("6EA9FB58-37CB-40CD-B599-535F12EA4F55");
			docto.setSerie("P");
			docto.setFolio("00012");
			docto.setMonedaDR("MXN");
//			docto.setTipoCambioDR(1D); //Este campo es requerido solo si MonedaDR es diferente a MXN
			docto.setMetodoDePagoDR("PPD");
			docto.setNumParcialidad(1);
			docto.setImpSaldoAnt(100D);
			docto.setImpPagado(100D);
			docto.setImpSaldoInsoluto(0D);
			cp.addDocumento(docto);
			
			cfdi.setComplementoPago(cp);
			
			LbsCrypt crypt = new LbsCrypt(new FileInputStream("ACO560518KW7.key"),"12345678a",new FileInputStream("ACO560518KW7.cer"));
			cfdi.sellar(crypt);
			cfdi.timbrarFinkok("sistemas@skarton.com.mx","Sistemas2011*");
			//Hay que tener cuidado a partir de esta linea ya que el documento se encuentra timbrado desde este momento cualquier problema al guardar no importa el CFDI ya estara registrado en el SAT
			cfdi.saveXML("pago.xml");
			cfdi.savePdf("pago.pdf");
		}catch(Exception e){
			e.printStackTrace();
		} catch (myXmlParsingException e) {
			e.printStackTrace();
		}
	}
}
