package conta;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.w3c.dom.NodeList;

import com.cfdi.exceptions.myXmlParsingException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.ImgRaw;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Constructor de PDF
 * 
 * 
 * @author alberto
 *
 */
public class LibPdfBuilder extends PdfPageEventHelper{
	
	Image bgImage;
	Document document = null;
	ByteArrayOutputStream dataArray;
	PdfWriter writer;
	PdfContentByte cb = null;
	//Fonts
    BaseFont baseFontNormal;
    BaseFont baseFontSmall;
    BaseFont baseFontBold;
    Font.FontFamily fontFamily;
    Integer fontSize=11;
    DecimalFormat formateador = new DecimalFormat("$###,###,###,##0.00");
    
    Cfdi33 cfdi;

    public LibPdfBuilder(Cfdi33 cfdi, String bg) throws myXmlParsingException, MalformedURLException, IOException, DocumentException {
		this.cfdi=cfdi;
    	if (bg!=null && bg.length()!=0)
            bgImage=Image.getInstance(bg);
		
		
		Rectangle rec = PageSize.LETTER;
		document = new Document(rec, 20F, 15F, 238F, 245F);
		
		//Fonts
		baseFontNormal = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1252", false);
    	fontFamily = Font.FontFamily.TIMES_ROMAN;
    	
    	
		dataArray = new ByteArrayOutputStream();
		writer = PdfWriter.getInstance(document,dataArray);
		writer.setPageEvent(this);
		document.open();
		cb = writer.getDirectContent();
		
    	construirPartidas();
    	agregarTraslados();
    	agregarRetenciones();
    	agregarRelacionados();
    	document.add(new Paragraph(""));
    	
    	
    	document.close();
    }
    
    
    //Todo lo obligatorio por pagina, fondo, cavecera, pie, etc...
    @Override
    public void onEndPage (PdfWriter writer, Document document) {
        /*
         * Imagen de fondo
         */
    //cargamos la plantilla de fondo
        if (bgImage!=null){
//	        	bgImage.scaleAbsoluteHeight(789);
//				bgImage.scaleAbsoluteWidth(613);
            bgImage.setAbsolutePosition(1, 1);
            try {
				document.add(bgImage);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
        }
        
        
        imprimirDatosGenerales();
        imprimirDatos();
        
        
        
        //Codigo de barras bidimencional
		NumberFormat qrformatter = new DecimalFormat("0000000000.000000");
		Image qrCode=null;
		try {
			qrCode = generateQRCodeImage("https://verificacfdi.facturaelectronica.sat.gob.mx/default.aspx?id="+cfdi.get("Complemento/tfd:TimbreFiscalDigital/UUID")
			+"&re="+cfdi.get("Emisor/Rfc")+"&rr="+cfdi.get("Receptor/Rfc")+"&tt="+qrformatter.format(new Double(cfdi.get("Total")))
			+"&fe="+cfdi.get("Complemento/tfd:TimbreFiscalDigital/SelloCFD").substring(cfdi.get("Complemento/tfd:TimbreFiscalDigital/SelloCFD").length()-8),140,140);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int x,y;
		x=460;
		y=15;
		
			
		qrCode.setAbsolutePosition(x, y);
		try {
			document.add(qrCode);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
        super.onEndPage(writer, document);
    }
    
    
    private void imprimirDatosGenerales(){
    	cb.beginText();
    	cb.setFontAndSize(baseFontNormal, fontSize);
    	
    	
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "TipoComprobante:", 341, 760, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "Folio:", 341, 745, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "Folio Fiscal:", 341, 730, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "Certificado SAT:", 341, 702, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, SatTools.parseSat("TIPO_CFDI", cfdi.get("TipoDeComprobante")), 440, 760, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, cfdi.get("Serie")+cfdi.get("Folio"), 440, 745, 0);
    	cb.setFontAndSize(baseFontNormal, 8);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, cfdi.get("Complemento/tfd:TimbreFiscalDigital/UUID"), 440, 731, 0);
    	cb.setFontAndSize(baseFontNormal, fontSize);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, cfdi.get("Complemento/tfd:TimbreFiscalDigital/NoCertificadoSAT"), 440, 702, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, cfdi.get("NoCertificado"), 440, 716, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, cfdi.get("Emisor/Nombre"), 20, 671, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "R.F.C:", 20, 655, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, cfdi.get("Emisor/Rfc"), 55, 655, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "Regimen: "+cfdi.get("Emisor/RegimenFiscal")+" "+SatTools.parseSat("REGIMEN", cfdi.get("Emisor/RegimenFiscal")), 20, 640, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, cfdi.get("LugarExpedicion"), 440, 688, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, cfdi.get("Fecha"), 440, 673, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, cfdi.get("Complemento/tfd:TimbreFiscalDigital/FechaTimbrado"), 440, 659, 0);


    	
    	
    	cb.setFontAndSize(baseFontNormal, 7);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "Sello Digital CFDI", 22, 153, 0);
    	agregarCaja(cfdi.get("Complemento/tfd:TimbreFiscalDigital/SelloCFD"),20,110,445,150,7,7);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "Sello Digital SAT", 22, 108, 0);
    	agregarCaja(cfdi.get("Complemento/tfd:TimbreFiscalDigital/SelloSAT"),20,65,445,106,7,7);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "Cadena original del complemento de certificación digital del SAT", 22, 65, 0);
    	agregarCaja(cfdi.getCadenaTimbre(),20,15,445,62,7,7);
    	cb.setFontAndSize(baseFontNormal, fontSize);
    	
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "Receptor:", 20, 590, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, cfdi.get("Receptor/Nombre"), 71, 590, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "R.F.C:", 20, 574, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, cfdi.get("Receptor/Rfc"), 60, 574, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "No. CSD:", 341, 716, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "Uso CFDI:", 20, 560, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, cfdi.get("Receptor/UsoCFDI")+" "+SatTools.parseSat("USO", cfdi.get("Receptor/UsoCFDI")), 74, 560, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "Lugar de expedicion:", 341, 688, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "Fecha expedicion:", 341, 673, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "Fecha certificación:", 341, 659, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "R.F.C. PAC:", 341, 644, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, cfdi.get("Complemento/tfd:TimbreFiscalDigital/RfcProvCertif"), 440, 644, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "Tipo relación:", 341, 629, 0);
    	cb.setFontAndSize(baseFontNormal, 7);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, cfdi.get("CfdiRelacionados/TipoRelacion")+" "+SatTools.parseSat("RELACION", cfdi.get("CfdiRelacionados/TipoRelacion")), 440, 629, 0);
    	cb.setFontAndSize(baseFontNormal, fontSize);
    	
    	
    	

    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "Importe con letra:", 24, 195, 0);
    	cb.showTextAligned(PdfPCell.ALIGN_LEFT, "CFDI relacionados:", 341, 615, 0);
    	

    	cb.setFontAndSize(baseFontNormal, 7);
    	cb.showTextAligned(PdfPCell.ALIGN_CENTER, cfdi.getLeyenda(), 280, 12, 0);
    	
    	cb.endText();
    }
    
    
    protected void imprimirDatos(){}
    
    
    protected void agregarCaja(String texto, float llx, float lly, float urx, float ury, float leading, Integer fontSize){
    	ColumnText ct = new ColumnText(cb);
    	Font font = new Font(fontFamily, fontSize, Font.NORMAL);
    	Phrase myText = new Phrase(texto, font);
        ct.setSimpleColumn(myText, 
        		llx, 
        		lly, 
        		urx, 
        		ury, 
        		leading, PdfPCell.ALIGN_LEFT);
        try {
			ct.go();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
    }
    
    
    private void construirPartidas() throws DocumentException{
    	PdfPTable table = new PdfPTable(7);
    	table.setHeaderRows(1);
    	Font font = new Font(fontFamily, fontSize, Font.BOLD);
    	float widths[] = {0.1F,0.15F,0.1F,0.25F,0.1F,0.1F,0.1F};
    	
    	PdfPCell c = new PdfPCell(new Phrase("Cantidad",font));
		c.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		c.setBorder(0);
		table.addCell(c);
		c = new PdfPCell(new Phrase("Codigo",font));
		c.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		c.setBorder(0);
		table.addCell(c);
		c = new PdfPCell(new Phrase("Medida",font));
		c.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		c.setBorder(0);
		table.addCell(c);
		c = new PdfPCell(new Phrase("Descripcion",font));
		c.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		c.setBorder(0);
		table.addCell(c);
		c = new PdfPCell(new Phrase("Unitario",font));
		c.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		c.setBorder(0);
		table.addCell(c);
		c = new PdfPCell(new Phrase("Descuento",font));
		c.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		c.setBorder(0);
		table.addCell(c);
		c = new PdfPCell(new Phrase("Total",font));
		c.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		c.setBorder(0);
		table.addCell(c);

		font = new Font(fontFamily, fontSize, Font.NORMAL);
		
		NodeList partidas = cfdi.getNodes("Conceptos/Concepto");
		for (Integer x=0; x<partidas.getLength(); x++){
			c = new PdfPCell(new Phrase(partidas.item(x).getAttributes().getNamedItem("Cantidad").getNodeValue(),font));
			c.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			c.setBorder(0);
			table.addCell(c);
			if (partidas.item(x).getAttributes().getNamedItem("ClaveProdServ")==null)
				c = new PdfPCell(new Phrase("",font));
			else
				c = new PdfPCell(new Phrase(partidas.item(x).getAttributes().getNamedItem("ClaveProdServ").getNodeValue(),font));
			c.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			c.setBorder(0);
			table.addCell(c);
			if (partidas.item(x).getAttributes().getNamedItem("Unidad")==null)
				c = new PdfPCell(new Phrase("",font));
			else
				c = new PdfPCell(new Phrase(partidas.item(x).getAttributes().getNamedItem("Unidad").getNodeValue(),font));
			c.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			c.setBorder(0);
			table.addCell(c);
			c = new PdfPCell(new Phrase(partidas.item(x).getAttributes().getNamedItem("Descripcion").getNodeValue(),font));
			c.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			c.setBorder(0);
			table.addCell(c);
			c = new PdfPCell(new Phrase(formateador.format(new Double(partidas.item(x).getAttributes().getNamedItem("ValorUnitario").getNodeValue())),font));
			c.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			c.setBorder(0);
			table.addCell(c);
			if (partidas.item(x).getAttributes().getNamedItem("Descuento")==null)
				c = new PdfPCell(new Phrase("",font));
			else
				c = new PdfPCell(new Phrase(formateador.format(new Double(partidas.item(x).getAttributes().getNamedItem("Descuento").getNodeValue())),font));
			c.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			c.setBorder(0);
			table.addCell(c);
			c = new PdfPCell(new Phrase(formateador.format(new Double(partidas.item(x).getAttributes().getNamedItem("Importe").getNodeValue())),font));
			c.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			c.setBorder(0);
			table.addCell(c);
		}
		
		table.setWidthPercentage(100);
		table.setWidths(widths);
		
		
		document.add(table);
    }
    
    
    
    private void agregarTraslados() throws DocumentException{
    	NodeList partidas = cfdi.getNodes("Impuestos/Traslados/Traslado");
    	if (partidas==null || partidas.getLength()==0)
    		return;
    	
    	
    	PdfPTable table = new PdfPTable(2);
    	Font font = new Font(fontFamily, 9, Font.NORMAL);
    	float widths[] = {0.5F,0.5F};
    	

    	PdfPCell c;
		for (Integer x=0; x<partidas.getLength(); x++){
			c = new PdfPCell(new Phrase(SatTools.parseSat("IMPUESTO",partidas.item(x).getAttributes().getNamedItem("Impuesto").getNodeValue())+" "+partidas.item(x).getAttributes().getNamedItem("TasaOCuota").getNodeValue(),font));
			c.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			c.setBorder(0);
			c.setLeading(0f, 0.75f);
			table.addCell(c);
			c = new PdfPCell(new Phrase(formateador.format(new Double(partidas.item(x).getAttributes().getNamedItem("Importe").getNodeValue())),font));
			c.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			c.setBorder(0);
			c.setLeading(0f, 0.75f);
			table.addCell(c);
		}
		
		table.setWidthPercentage(100);
		table.setWidths(widths);
    	
    	table.setTotalWidth(160);
    	table.writeSelectedRows(0, -1, 432, 222, writer.getDirectContent());
    }
    
    private void agregarRetenciones() throws DocumentException{
    	NodeList partidas = cfdi.getNodes("Impuestos/Retenciones/Retencion");
    	if (partidas==null || partidas.getLength()==0)
    		return;
    	
    	
    	PdfPTable table = new PdfPTable(2);
    	Font font = new Font(fontFamily, 9, Font.NORMAL);
    	float widths[] = {0.5F,0.5F};
    	

    	PdfPCell c;
		for (Integer x=0; x<partidas.getLength(); x++){
			c = new PdfPCell(new Phrase("Ret. "+SatTools.parseSat("IMPUESTO",partidas.item(x).getAttributes().getNamedItem("Impuesto").getNodeValue()),font));
			c.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			c.setBorder(0);
			c.setLeading(0f, 0.75f);
			table.addCell(c);
			c = new PdfPCell(new Phrase(formateador.format(new Double(partidas.item(x).getAttributes().getNamedItem("Importe").getNodeValue())),font));
			c.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			c.setBorder(0);
			c.setLeading(0f, 0.75f);
			table.addCell(c);
		}
		
		table.setWidthPercentage(100);
		table.setWidths(widths);
    	
    	table.setTotalWidth(160);
    	table.writeSelectedRows(0, -1, 432, 199, writer.getDirectContent());
    }
    
    private void agregarRelacionados() throws DocumentException{
    	NodeList partidas = cfdi.getNodes("CfdiRelacionados/CfdiRelacionado");
    	if (partidas==null || partidas.getLength()==0)
    		return;
    	
    	
    	PdfPTable table = new PdfPTable(1);
    	Font font = new Font(fontFamily, 9, Font.NORMAL);
    	float widths[] = {1F};
    	

    	PdfPCell c;
		for (Integer x=0; x<partidas.getLength(); x++){
			c = new PdfPCell(new Phrase(partidas.item(x).getAttributes().getNamedItem("UUID").getNodeValue(),font));
			c.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			c.setBorder(0);
			c.setLeading(0f, 0.75f);
			table.addCell(c);
		}
		
		table.setWidthPercentage(100);
		table.setWidths(widths);
    	
    	table.setTotalWidth(200);
    	table.writeSelectedRows(0, -1, 400, 610, writer.getDirectContent());
    }
    
	/*
	 * Generacion de Imagen QR
	 */
	public static Image generateQRCodeImage(String code, int width, int height) throws Exception { 
	    if (code == null || code.length() == 0) 
	            throw new Exception("Code unspecified"); 
	    if (width <= 0 || height <= 0) 
	            throw new Exception("Invalid width: " + width + " or height " + height); 
	    if (width != height) 
	            throw new Exception("width " + width + " and height " + height + " are not the same");
	    
//	    return generate1dImage(code,width,height);
	    
	    QRCode qrcode = new QRCode(); 
	    Encoder.encode(code, ErrorCorrectionLevel.L, qrcode);
	    
	    int qrSize = qrcode.getMatrixWidth(); 
	    int margin = 4; 
	    int imageSize = qrSize + 2 * margin; // includes quiet zone 
	    if (width < imageSize) width = imageSize; 
	    int magnify = width / imageSize; 
	    int remaining = width % imageSize; 
	    int topLeftPosition =  ((remaining > 0) ? remaining / 2 : magnify) + margin * magnify; 
	    int size = width; 

	    byte[] rawData = new byte[size*size];
	    for (int x=0;x<size*size;x++)
	    	rawData[x]=(byte) 255;
	    
	    for (int i = 0; i < qrSize; i ++) { 
            for (int j = 0; j < qrSize; j ++) { 
                    if (qrcode.at(i, j) == 1){
                            fillRectSlim(rawData,topLeftPosition + i * magnify, topLeftPosition + j * magnify, magnify, magnify, size);
                    }
            } 
	    } 
	    Image img = new ImgRaw(size,size,1,8,rawData);
	    return img;
	}
	
	//Emulacion de funcion grafica no disponible en el APP Engine
	public static void fillRectSlim(byte[] data, int xin,int yin,int xlenin,int ylenin, int size){
		int x=xin+1, y=yin+1, xlen=xlenin-1, ylen=ylenin-1; //Ajuste para dejar todo mas delgado
		int xRawStart=(y*size)+x, xRawEnd=(y*size)+x+xlen;
		for (int ycount=0;ycount<=ylen;ycount++){
			for (int cont=xRawStart;cont<=xRawEnd;cont++)
				data[cont]=(byte) 0;
			xRawStart+=size;
			xRawEnd+=size;
		}
	}
}
