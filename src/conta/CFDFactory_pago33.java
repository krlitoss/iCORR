/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conta;

import com.cfdi.exceptions.myXmlParsingException;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Date;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante;

/**
 *
 * @author Skarton
 */

/**CLASE PARA GENERAR COMPLEMENTO DE PAGO**/
public final class CFDFactory_pago33 {    
    
    
    public static Comprobante createComprobante(Connection conec, String documento) throws Exception{
        
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
			
			ComplementoPagoCfdi.DoctoRelacionado docto = cp.new DoctoRelacionado();
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
        return null;
	}
}