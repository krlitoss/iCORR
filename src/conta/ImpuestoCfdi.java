package conta;

import java.text.NumberFormat;


public class ImpuestoCfdi {
	NumberFormat numformat = NumberFormat.getNumberInstance();
	
	public static Integer TIPO_RETENCION=0;
	public static Integer TIPO_TRASLADO=1;
	public static String TASA = "Tasa";
	public static String CUOTA = "Cuota";
	public static String EXENTO = "Exento";
	public static String IVA = "002";
	public static String ISR = "001";
	public static String IEPS = "003";
	
	
	private Integer tipo;
	private Double base;
	private String impuesto;
	private String tipoFactor;
	private Double tasaCuota;
	private Double importe;
	
	
	public ImpuestoCfdi(Integer tipo, Double base, String impuesto, String tipoFactor, Double tasaCuota, Double importe){
	    //Formateador de decimales
		numformat.setMinimumFractionDigits(2);
		numformat.setMaximumFractionDigits(2);
		numformat.setGroupingUsed(false);
		
		this.setTipo(tipo);
		this.setBase(base);
		this.setImpuesto(impuesto);
		this.setTipoFactor(tipoFactor);
		this.setTasaCuota(tasaCuota);
		this.setImporte(new Double(numformat.format(importe)));
	}


	public Integer getTipo() {
		return tipo;
	}


	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}


	public Double getBase() {
		return base;
	}


	public void setBase(Double base) {
		this.base = base;
	}


	public String getImpuesto() {
		return impuesto;
	}


	public void setImpuesto(String impuesto) {
		this.impuesto = impuesto;
	}


	public String getTipoFactor() {
		return tipoFactor;
	}


	public void setTipoFactor(String tipoFactor) {
		this.tipoFactor = tipoFactor;
	}


	public Double getTasaCuota() {
		return tasaCuota;
	}


	public void setTasaCuota(Double tasaCuota) {
		this.tasaCuota = tasaCuota;
	}


	public Double getImporte() {
		return importe;
	}


	public void setImporte(Double importe) {
		this.importe = importe;
	}
}
