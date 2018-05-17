package conta;

import java.io.IOException;
import java.net.MalformedURLException;

import org.w3c.dom.NodeList;

import com.cfdi.exceptions.myXmlParsingException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class PdfP extends LibPdfBuilder {

	public PdfP(Cfdi33 cfdi) throws myXmlParsingException, MalformedURLException, IOException, DocumentException {
		super(cfdi,"P33.png");
		
	}

	
    protected void imprimirDatos(){
    	cb.beginText();
    	cb.setFontAndSize(baseFontNormal, fontSize);
    	
    	
    	//Pago
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "Cuenta Beneficiario:", 25, 472, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, cfdi.get("Complemento/pago10:Pagos/pago10:Pago/CtaBeneficiario"), 131, 472, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "Cuenta Ordenante:", 25, 458, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, cfdi.get("Complemento/pago10:Pagos/pago10:Pago/CtaOrdenante"), 131, 458, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "Fecha de Pago:", 25, 445, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, cfdi.get("Complemento/pago10:Pagos/pago10:Pago/FechaPago"), 131, 445, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "Forma de Pago:", 25, 432, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, cfdi.get("Complemento/pago10:Pagos/pago10:Pago/FormaDePagoP")+" "+SatTools.parseSat("FORMA_PAGO", cfdi.get("Complemento/pago10:Pagos/pago10:Pago/FormaDePagoP")), 131, 432, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "Moneda:", 351, 472, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, cfdi.get("Complemento/pago10:Pagos/pago10:Pago/MonedaP"), 470, 472, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "Monto:", 351, 458, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, cfdi.get("Complemento/pago10:Pagos/pago10:Pago/Monto"), 470, 458, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "Numero de Operación:", 351, 445, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, cfdi.get("Complemento/pago10:Pagos/pago10:Pago/NumOperacion"), 470, 445, 0);
    	try {
			agregarPagados();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
    	
    	
    	cb.showTextAligned(PdfPCell.ALIGN_RIGHT, "Total", 508, 200, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_RIGHT, formateador.format(new Double(cfdi.get("Complemento/pago10:Pagos/pago10:Pago/Monto"))), 592, 200, 0);
    	
    	
    	cb.setFontAndSize(baseFontNormal, 7);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, NumberToLetterConverter.Num2Text(new Double(cfdi.get("Complemento/pago10:Pagos/pago10:Pago/Monto")), cfdi.get("Complemento/pago10:Pagos/pago10:Pago/MonedaP")), 110, 195, 0);

    	cb.endText();
    }
    
    

    private void agregarPagados() throws DocumentException{
    	NodeList partidas = cfdi.getNodes("cfdi:Complemento/pago10:Pagos/pago10:Pago/pago10:DoctoRelacionado");
    	if (partidas==null || partidas.getLength()==0)
    		return;
    	
    	
    	PdfPTable table = new PdfPTable(6);
    	table.setHeaderRows(1);
    	float widths[] = {0.35F,0.1F,0.1F,0.15F,0.15F,0.15F};
    	
    	Font font = new Font(fontFamily, 10, Font.BOLD);
    	PdfPCell c = new PdfPCell(new Phrase("Documento",font));
		c.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		c.setBorder(0);
		table.addCell(c);
		c = new PdfPCell(new Phrase("Folio",font));
		c.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		c.setBorder(0);
		table.addCell(c);
		c = new PdfPCell(new Phrase("Parcialidad",font));
		c.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		c.setBorder(0);
		table.addCell(c);
		c = new PdfPCell(new Phrase("Saldo ant.",font));
		c.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		c.setBorder(0);
		table.addCell(c);
		c = new PdfPCell(new Phrase("Importe",font));
		c.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		c.setBorder(0);
		table.addCell(c);
		c = new PdfPCell(new Phrase("Saldo",font));
		c.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		c.setBorder(0);
		table.addCell(c);
    	
    	
		font = new Font(fontFamily, 9, Font.NORMAL);
		for (Integer x=0; x<partidas.getLength(); x++){
			c = new PdfPCell(new Phrase(partidas.item(x).getAttributes().getNamedItem("IdDocumento").getNodeValue(),font));
			c.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			c.setBorder(0);
//			c.setLeading(0f, 0.75f);
			table.addCell(c);
			c = new PdfPCell(new Phrase(partidas.item(x).getAttributes().getNamedItem("Folio").getNodeValue(),font));
			c.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			c.setBorder(0);
//			c.setLeading(0f, 0.75f);
			table.addCell(c);
			c = new PdfPCell(new Phrase(partidas.item(x).getAttributes().getNamedItem("NumParcialidad").getNodeValue(),font));
			c.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			c.setBorder(0);
//			c.setLeading(0f, 0.75f);
			table.addCell(c);
			c = new PdfPCell(new Phrase(formateador.format(new Double(partidas.item(x).getAttributes().getNamedItem("ImpSaldoAnt").getNodeValue())),font));
			c.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			c.setBorder(0);
//			c.setLeading(0f, 0.75f);
			table.addCell(c);
			c = new PdfPCell(new Phrase(formateador.format(new Double(partidas.item(x).getAttributes().getNamedItem("ImpPagado").getNodeValue())),font));
			c.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			c.setBorder(0);
//			c.setLeading(0f, 0.75f);
			table.addCell(c);
			c = new PdfPCell(new Phrase(formateador.format(new Double(partidas.item(x).getAttributes().getNamedItem("ImpSaldoInsoluto").getNodeValue())),font));
			c.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			c.setBorder(0);
//			c.setLeading(0f, 0.75f);
			table.addCell(c);
		}
		
		table.setWidthPercentage(100);
		table.setWidths(widths);
    	
    	table.setTotalWidth(570);
    	table.writeSelectedRows(0, -1, 20, 372, writer.getDirectContent());
    }
    
}
