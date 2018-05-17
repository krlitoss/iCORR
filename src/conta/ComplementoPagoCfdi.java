package conta;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ComplementoPagoCfdi {

	private Date FechaPago;
	private String FormaDePagoP;
	private String MonedaP;
	private Double TipoCambioP;
	private Double Monto;
	private String NumOperacion;
	private String RfcEmisorCtaOrd;
	private String NomBancoOrdExt;
	private String CtaOrdenante;
	private String RfcEmisorCtaBen;
	private String CtaBeneficiario;
	private String TipoCadPago;
	private String CertPago;
	private String CadPago;
	private String SelloPago;

	private List<DoctoRelacionado> documentos = new ArrayList<DoctoRelacionado>();


	public Date getFechaPago() {
		return FechaPago;
	}



	public void setFechaPago(Date fechaPago) {
		FechaPago = fechaPago;
	}



	public String getFormaDePagoP() {
		return FormaDePagoP;
	}



	public void setFormaDePagoP(String formaDePagoP) {
		FormaDePagoP = formaDePagoP;
	}



	public String getMonedaP() {
		return MonedaP;
	}



	public void setMonedaP(String monedaP) {
		MonedaP = monedaP;
	}



	public Double getTipoCambioP() {
		return TipoCambioP;
	}



	public void setTipoCambioP(Double tipoCambioP) {
		TipoCambioP = tipoCambioP;
	}



	public Double getMonto() {
		return Monto;
	}



	public void setMonto(Double monto) {
		Monto = monto;
	}



	public String getNumOperacion() {
		return NumOperacion;
	}



	public void setNumOperacion(String numOperacion) {
		NumOperacion = numOperacion;
	}



	public String getRfcEmisorCtaOrd() {
		return RfcEmisorCtaOrd;
	}



	public void setRfcEmisorCtaOrd(String rfcEmisorCtaOrd) {
		RfcEmisorCtaOrd = rfcEmisorCtaOrd;
	}



	public String getNomBancoOrdExt() {
		return NomBancoOrdExt;
	}



	public void setNomBancoOrdExt(String nomBancoOrdExt) {
		NomBancoOrdExt = nomBancoOrdExt;
	}



	public String getCtaOrdenante() {
		return CtaOrdenante;
	}



	public void setCtaOrdenante(String ctaOrdenante) {
		CtaOrdenante = ctaOrdenante;
	}



	public String getRfcEmisorCtaBen() {
		return RfcEmisorCtaBen;
	}



	public void setRfcEmisorCtaBen(String rfcEmisorCtaBen) {
		RfcEmisorCtaBen = rfcEmisorCtaBen;
	}



	public String getCtaBeneficiario() {
		return CtaBeneficiario;
	}



	public void setCtaBeneficiario(String ctaBeneficiario) {
		CtaBeneficiario = ctaBeneficiario;
	}



	public String getTipoCadPago() {
		return TipoCadPago;
	}



	public void setTipoCadPago(String tipoCadPago) {
		TipoCadPago = tipoCadPago;
	}



	public String getCertPago() {
		return CertPago;
	}



	public void setCertPago(String certPago) {
		CertPago = certPago;
	}



	public String getCadPago() {
		return CadPago;
	}



	public void setCadPago(String cadPago) {
		CadPago = cadPago;
	}



	public String getSelloPago() {
		return SelloPago;
	}



	public void setSelloPago(String selloPago) {
		SelloPago = selloPago;
	}
	
	
	
	public List<DoctoRelacionado> getDocumentos() {
		return documentos;
	}



	public void setDocumentos(List<DoctoRelacionado> documentos) {
		this.documentos = documentos;
	}

	public void addDocumento(DoctoRelacionado d){
		documentos.add(d);
	}


	public class DoctoRelacionado{
	
		private String IdDocumento;
		private String Serie;
		private String Folio;
		private String MonedaDR;
		private Double TipoCambioDR;
		private String MetodoDePagoDR;
		private Integer NumParcialidad;
		private Double ImpSaldoAnt;
		private Double ImpPagado;
		private Double ImpSaldoInsoluto;
		
		
		
		public String getIdDocumento() {
			return IdDocumento;
		}
		public void setIdDocumento(String idDocumento) {
			IdDocumento = idDocumento;
		}
		public String getSerie() {
			return Serie;
		}
		public void setSerie(String serie) {
			Serie = serie;
		}
		public String getFolio() {
			return Folio;
		}
		public void setFolio(String folio) {
			Folio = folio;
		}
		public String getMonedaDR() {
			return MonedaDR;
		}
		public void setMonedaDR(String monedaDR) {
			MonedaDR = monedaDR;
		}
		public Double getTipoCambioDR() {
			return TipoCambioDR;
		}
		public void setTipoCambioDR(Double tipoCambioDR) {
			TipoCambioDR = tipoCambioDR;
		}
		public String getMetodoDePagoDR() {
			return MetodoDePagoDR;
		}
		public void setMetodoDePagoDR(String metodoDePagoDR) {
			MetodoDePagoDR = metodoDePagoDR;
		}
		public Integer getNumParcialidad() {
			return NumParcialidad;
		}
		public void setNumParcialidad(Integer numParcialidad) {
			NumParcialidad = numParcialidad;
		}
		public Double getImpSaldoAnt() {
			return ImpSaldoAnt;
		}
		public void setImpSaldoAnt(Double impSaldoAnt) {
			ImpSaldoAnt = impSaldoAnt;
		}
		public Double getImpPagado() {
			return ImpPagado;
		}
		public void setImpPagado(Double impPagado) {
			ImpPagado = impPagado;
		}
		public Double getImpSaldoInsoluto() {
			return ImpSaldoInsoluto;
		}
		public void setImpSaldoInsoluto(Double impSaldoInsoluto) {
			ImpSaldoInsoluto = impSaldoInsoluto;
		}
	}
}
