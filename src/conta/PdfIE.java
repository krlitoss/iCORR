package conta;

import java.io.IOException;
import java.net.MalformedURLException;

import com.cfdi.exceptions.myXmlParsingException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPCell;

public class PdfIE extends LibPdfBuilder {

	public PdfIE(Cfdi33 cfdi) throws myXmlParsingException, MalformedURLException, IOException, DocumentException {
		super(cfdi,"I33.png");
	}

	protected void imprimirDatos(){
    	cb.beginText();
    	cb.setFontAndSize(baseFontNormal, fontSize);
    	
    	
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "Forma de pago:", 24, 234, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, cfdi.get("FormaPago")+" "+SatTools.parseSat("FORMA_PAGO", cfdi.get("FormaPago")), 110, 233, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "Metodo de pago:", 24, 221, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, cfdi.get("MetodoPago")+" "+SatTools.parseSat("METODO_PAGO", cfdi.get("MetodoPago")), 110, 220, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "Condiciones:", 24, 206, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, cfdi.get("CondicionesDePago"), 110, 206, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "Moneda:", 24, 181, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, cfdi.get("Moneda"), 65, 181, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "Tipo Cambio:", 110, 181, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, cfdi.get("TipoCambio"), 173, 181, 0);


		cb.showTextAligned(PdfPCell.ALIGN_RIGHT, "Sub Total", 508, 233, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_RIGHT, formateador.format(new Double(cfdi.get("SubTotal"))), 591, 233, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_RIGHT, "Descuento", 508, 222, 0);
    	if (cfdi.get("Descuento")!=null && !cfdi.get("Descuento").isEmpty())
    		cb.showTextAligned(PdfPCell.ALIGN_RIGHT, formateador.format(new Double(cfdi.get("Descuento"))), 591, 222, 0);
    	else
    		cb.showTextAligned(PdfPCell.ALIGN_RIGHT, formateador.format(new Double(0)), 591, 222, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_RIGHT, "Total", 508, 163, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_RIGHT, formateador.format(new Double(cfdi.get("Total"))), 592, 163, 0);
    	
    	
    	cb.setFontAndSize(baseFontNormal, 7);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, NumberToLetterConverter.Num2Text(new Double(cfdi.get("Total")), cfdi.get("Moneda")), 110, 195, 0);
    	
    	cb.endText();
	}
}
