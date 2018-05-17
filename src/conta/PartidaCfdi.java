package conta;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una partida de un CFDI
 * @author alberto
 *
 */
public class PartidaCfdi {
	NumberFormat numformat = NumberFormat.getNumberInstance();
        NumberFormat numformat_cant = NumberFormat.getNumberInstance();
	
	private Double cantidad;
	private String medida;
	private String medidaSat;
	private String codigo;
	private String codigoSat;
	private String descripcion;
	private Double impUnit;
	private Double impTot;
	private Double descuento;
	private List<ImpuestoCfdi> impuestos = new ArrayList<ImpuestoCfdi>();

	/**
	 * Constructor.
	 * 
	 * @param cantidad Cantidad del producto o servicio que se esta facturando
	 * @param medida Unidad de medida
	 * @param medidaSat Clave del catalogo Unidades de medida del SAT
	 * @param codigo Codigo del articulo (Opcional)
	 * @param codigoSat Codigo del catalogo de productos del SAT
	 * @param desc  Descripcion de la venta
	 * @param impUnit Precio unitario sin IVA
	 * @param descuento Descuento total de esta partida, debe ser igual o menor a la multiplicacion de: cantidad * impUnit
	 */
	public PartidaCfdi(Double cantidad,String medida, String medidaSat, String codigo, String codigoSat, String desc, Double impUnit, Double descuento){
	    //Formateador de decimales
		numformat.setMinimumFractionDigits(2);
		numformat.setMaximumFractionDigits(2);
		numformat.setGroupingUsed(false);
                
                numformat_cant.setMinimumFractionDigits(4);
		numformat_cant.setMaximumFractionDigits(4);
		numformat_cant.setGroupingUsed(false);
		
		this.cantidad=new Double(numformat_cant.format(cantidad));
		this.medida=medida;
		this.medidaSat=medidaSat;
		this.codigo=codigo;
		this.codigoSat=codigoSat;
		this.descripcion=desc;
		this.impUnit=new Double(numformat.format(impUnit));
		this.setImpTot(new Double(numformat.format(impUnit*cantidad)));
		this.setDescuento(descuento);
	}
	
	/**
	 * Agrega un impuesto Trasladado a la partida.
	 * 
	 * @param base Base gravable
	 * @param impuesto Nombre del impuesto IVA/ISR/IETU
	 * @param tipoFactor Tasa/Couta/Exento
	 * @param tasa Tasa en formato decimal 0.16 para 16%
	 * @param importe	Importe trasladado por el impuesto
	 */
	public void agregarImpuestoTrasladado(Double base, String impuesto, String tipoFactor, Double tasa, Double importe){
		impuestos.add(new ImpuestoCfdi(ImpuestoCfdi.TIPO_TRASLADO,base,impuesto,tipoFactor,tasa,importe));
	}
	
	
	/**
	 * Agrega un impuesto Retenido a la partida.
	 * 
	 * @param base Base gravable
	 * @param impuesto Nombre del impuesto IVA/ISR/IETU
	 * @param tipoFactor Tasa/Couta/Exento
	 * @param tasa Tasa en formato decimal 0.16 para 16%
	 * @param importe Importe trasladado por el impuesto
	 */
	public void agregarImpuestoRetenido(Double base, String impuesto, String tipoFactor, Double tasa, Double importe){
		impuestos.add(new ImpuestoCfdi(ImpuestoCfdi.TIPO_RETENCION,base,impuesto,tipoFactor,tasa,importe));
	}
	
	
	
	
	public Double getCantidad() {
		return cantidad;
	}
	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}
	public String getMedida() {
		return medida;
	}
	public void setMedida(String medida) {
		this.medida = medida;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String desc) {
		this.descripcion = desc;
	}
	public Double getImpUnit() {
		return impUnit;
	}
	public void setImpUnit(Double impUnit) {
		this.impUnit = impUnit;
	}
	public List<ImpuestoCfdi> getImpuestos() {
		return impuestos;
	}

	public String getCodigoSat() {
		return codigoSat;
	}

	public void setCodigoSat(String codigoSat) {
		this.codigoSat = codigoSat;
	}

	public String getMedidaSat() {
		return medidaSat;
	}

	public void setMedidaSat(String medidaSat) {
		this.medidaSat = medidaSat;
	}

	public Double getDescuento() {
		if (descuento!=null)
			return descuento;
		else
			return 0D;
	}

	public void setDescuento(Double descuento) {
		this.descuento = descuento;
	}

	public Double getImpTot() {
		return impTot;
	}

	public void setImpTot(Double impTot) {
		this.impTot = impTot;
	}
	
	
	
	
}
