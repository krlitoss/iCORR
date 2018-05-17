/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package conta;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

import mx.bigdata.sat.cfdi.v32.schema.Comprobante;
import mx.bigdata.sat.cfdi.v32.schema.ObjectFactory;
import mx.bigdata.sat.cfdi.v32.schema.TUbicacion;
import mx.bigdata.sat.cfdi.v32.schema.TUbicacionFiscal;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante.Conceptos;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante.Emisor;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante.Emisor.RegimenFiscal;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante.Impuestos;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante.Receptor;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante.Conceptos.Concepto;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante.Impuestos.Retenciones;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante.Impuestos.Retenciones.Retencion;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante.Impuestos.Traslados;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante.Impuestos.Traslados.Traslado;

/**
 *
 * @author IVONNE
 */
public final class CFDFactory32 {

    private static ResultSet rs0 = null;
    private static DecimalFormat fijo2decimales = new DecimalFormat("######0.00");
    private static DecimalFormat fijo3decimales = new DecimalFormat("######0.000");
    private static DecimalFormat fijo5decimales = new DecimalFormat("######0.00000");

    public static Comprobante createComprobante(Connection conec, String documento) throws Exception {

        ObjectFactory of = new ObjectFactory();
        Comprobante comp = of.createComprobante();
        comp.setVersion("3.2");

        //ingresa todos los datos de la factura
        rs0 = null;
        try {
            String senSQL = "SELECT facturas.*, folios.serie, folios.aprobacion, folios.anoaprobacion FROM facturas  LEFT JOIN folios ON facturas.id_folios = folios.id_folio WHERE factura_serie = '" + documento + "';";
            rs0 = conexion.consulta(senSQL, conec);
            if (rs0.next()) {
                Date date = rs0.getTimestamp("fecha");
                //resta 5 minutos a fi fecha
                Calendar calendarDate = Calendar.getInstance();
                calendarDate.setTime(date);
                calendarDate.add(Calendar.MINUTE, -5);
                date = calendarDate.getTime();

                comp.setFecha(date);
                String serief = "" + rs0.getString("serie");
                if (!serief.equals("")) {
                    comp.setSerie(serief);
                }

                //consultamos los datos para agregar metodo de pago
                String metodopag = "" + rs0.getString("clave_formapago");
                String ctapag = "" + rs0.getString("cuentapago");
                if (metodopag.trim().equals("") || metodopag.trim().equals("null")) {
                    comp.setMetodoDePago("NA"); //campos para cfd 2016
                }else{
                    comp.setMetodoDePago(metodopag); //campos para cfd 2016
                }
                if (!ctapag.trim().equals("") && !ctapag.trim().equals("null")) {
                    comp.setNumCtaPago(ctapag); //campos para cfd 2016
                }



                comp.setFolio(rs0.getString("factura"));
                //comp.setNoAprobacion(new BigInteger(rs0.getString("aprobacion")));
                //comp.setAnoAprobacion(new BigInteger(rs0.getString("anoaprobacion")));
                comp.setFormaDePago("PAGO EN UNA SOLA EXHIBICION");
                comp.setSubTotal(new BigDecimal(fijo2decimales.format(rs0.getDouble("subtotal"))));
                comp.setTotal(new BigDecimal(fijo2decimales.format(rs0.getDouble("total"))));
                comp.setDescuento(new BigDecimal(fijo2decimales.format(rs0.getDouble("descuento"))));
            }
            if (rs0 != null) {
                rs0.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }

        comp.setTipoDeComprobante("ingreso");
        //agrega el lugar de expedidision
        rs0 = null;
        try {
            String senSQL = "SELECT facturas.factura_serie,lugaresemision.* FROM facturas LEFT JOIN lugaresemision ON facturas.id_lugaremision=lugaresemision.id_lugaremision WHERE factura_serie='" + documento + "';";
            rs0 = conexion.consulta(senSQL, conec);
            if (rs0.next()) {
                comp.setLugarExpedicion(CFDIconexion.remove1(rs0.getString("municipio") + ", " + rs0.getString("estado") + ", " + rs0.getString("pais")));
            }
            if (rs0 != null) {
                rs0.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }

        comp.setEmisor(createEmisor(of, conec, documento));
        //comp.setEmisor(createEmisor_prueba(of, conec, documento));
        
        comp.setReceptor(createReceptor(of, conec, documento));
        comp.setConceptos(createConceptos(of, conec, documento));
        comp.setImpuestos(createImpuestos(of, conec, documento));
        return comp;
    }

    private static Emisor createEmisor(ObjectFactory of, Connection conec, String documento) {
        Emisor emisor = of.createComprobanteEmisor();
        rs0 = null;
        try {
            String senSQL = "SELECT * FROM empresa WHERE id='1';";
            rs0 = conexion.consulta(senSQL, conec);
            
            if (rs0.next()) {
                
                emisor.setNombre(CFDIconexion.remove1(rs0.getString("nombre")));
                emisor.setRfc(rs0.getString("rfc"));
                //agregamos regimen fiscal
                RegimenFiscal rf = of.createComprobanteEmisorRegimenFiscal();
                rf.setRegimen(CFDIconexion.remove1(rs0.getString("regimenfiscal")));
                emisor.getRegimenFiscal().add(rf);
                
                TUbicacionFiscal uf = of.createTUbicacionFiscal();
                uf.setCalle(CFDIconexion.remove1(rs0.getString("calle")));
                String ref = "" + rs0.getString("referencia");
                if (!ref.equals("") && !ref.equals("null")) {
                    uf.setReferencia(CFDIconexion.remove1(ref));
                }
                uf.setCodigoPostal(rs0.getString("cod_postal"));
                uf.setColonia(CFDIconexion.remove1(rs0.getString("colonia")));
                String loc = "" + rs0.getString("localidad");
                if (!loc.equals("") && !loc.equals("null")) {
                    uf.setLocalidad(CFDIconexion.remove1(loc));
                }
                uf.setMunicipio(CFDIconexion.remove1(rs0.getString("municipio")));
                uf.setEstado(CFDIconexion.remove1(rs0.getString("estado")));
                uf.setNoExterior(rs0.getString("numeroext"));
                String noint = "" + rs0.getString("numeroint");
                if (!noint.equals("") && !noint.equals("null")) {
                    uf.setNoInterior(noint);
                }
                uf.setPais(CFDIconexion.remove1(rs0.getString("pais")));
                emisor.setDomicilioFiscal(uf);

            }
            if (rs0 != null) {
                rs0.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }
        //agrega el lugar de expedidision
        rs0 = null;
        try {
            String senSQL = "SELECT facturas.factura_serie,lugaresemision.* FROM facturas LEFT JOIN lugaresemision ON facturas.id_lugaremision=lugaresemision.id_lugaremision WHERE factura_serie='" + documento + "';";
            rs0 = conexion.consulta(senSQL, conec);
            if (rs0.next()) {
                TUbicacion le = of.createTUbicacion();
                le.setMunicipio(CFDIconexion.remove1(rs0.getString("municipio")));
                le.setEstado(CFDIconexion.remove1(rs0.getString("estado")));
                le.setPais(CFDIconexion.remove1(rs0.getString("pais")));
                emisor.setExpedidoEn(le);
            }
            if (rs0 != null) {
                rs0.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }

        //regresa el emisor
        return emisor;
    }

    private static Emisor createEmisor_pruebaxxx(ObjectFactory of, Connection conec, String documento) {
        Emisor emisor = of.createComprobanteEmisor();
        rs0 = null;
        try {
            String senSQL = "SELECT * FROM empresa2 WHERE id='2';";
            rs0 = conexion.consulta(senSQL, conec);
            if (rs0.next()) {
                emisor.setNombre(CFDIconexion.remove1(rs0.getString("nombre")));
                emisor.setRfc(rs0.getString("rfc"));
                //agregamos regimen fiscal
                RegimenFiscal rf = of.createComprobanteEmisorRegimenFiscal();
                rf.setRegimen(CFDIconexion.remove1(rs0.getString("regimenfiscal")));
                emisor.getRegimenFiscal().add(rf);

                TUbicacionFiscal uf = of.createTUbicacionFiscal();
                uf.setCalle(CFDIconexion.remove1(rs0.getString("calle")));
                String ref = "" + rs0.getString("referencia");
                if (!ref.equals("") && !ref.equals("null")) {
                    uf.setReferencia(CFDIconexion.remove1(ref));
                }
                uf.setCodigoPostal(rs0.getString("cod_postal"));
                uf.setColonia(CFDIconexion.remove1(rs0.getString("colonia")));
                String loc = "" + rs0.getString("localidad");
                if (!loc.equals("") && !loc.equals("null")) {
                    uf.setLocalidad(CFDIconexion.remove1(loc));
                }
                uf.setMunicipio(CFDIconexion.remove1(rs0.getString("municipio")));
                uf.setEstado(CFDIconexion.remove1(rs0.getString("estado")));
                uf.setNoExterior(rs0.getString("numeroext"));
                String noint = "" + rs0.getString("numeroint");
                if (!noint.equals("") && !noint.equals("null")) {
                    uf.setNoInterior(noint);
                }
                uf.setPais(CFDIconexion.remove1(rs0.getString("pais")));
                emisor.setDomicilioFiscal(uf);

            }
            if (rs0 != null) {
                rs0.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "ERROR AL EJECUTAR LA CONSULTA XXX\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }
        //agrega el lugar de expedidision
        rs0 = null;
        try {
            String senSQL = "SELECT facturas.factura_serie,lugaresemision.* FROM facturas LEFT JOIN lugaresemision ON facturas.id_lugaremision=lugaresemision.id_lugaremision WHERE factura_serie='" + documento + "';";
            rs0 = conexion.consulta(senSQL, conec);
            if (rs0.next()) {
                TUbicacion le = of.createTUbicacion();
                le.setMunicipio(CFDIconexion.remove1(rs0.getString("municipio")));
                le.setEstado(CFDIconexion.remove1(rs0.getString("estado")));
                le.setPais(CFDIconexion.remove1(rs0.getString("pais")));
                emisor.setExpedidoEn(le);
            }
            if (rs0 != null) {
                rs0.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "ERROR AL EJECUTAR LA CONSULTA YYY\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }

        //regresa el emisor
        return emisor;
    }

    private static Receptor createReceptor(ObjectFactory of, Connection conec, String documento) {
        Receptor receptor = of.createComprobanteReceptor();
        //agrega datos del cliente
        rs0 = null;
        try {
            String senSQL = "SELECT facturas.factura_serie,clientes.* FROM facturas LEFT JOIN clientes ON facturas.id_clientes=clientes.id_clientes WHERE factura_serie='" + documento + "';";
            rs0 = conexion.consulta(senSQL, conec);
            if (rs0.next()) {
                receptor.setNombre(CFDIconexion.remove1(rs0.getString("nombre")));
                receptor.setRfc(rs0.getString("rfc"));
                TUbicacion uf = of.createTUbicacion();
                uf.setCalle(CFDIconexion.remove1(rs0.getString("calle")));
                String ref = rs0.getString("referencia");
                if (!ref.equals("") && !ref.equals("null")) {
                    uf.setReferencia(CFDIconexion.remove1(ref));
                }
                uf.setCodigoPostal(rs0.getString("cod_postal"));
                uf.setColonia(CFDIconexion.remove1(rs0.getString("colonia")));
                String loc = rs0.getString("localidad");
                if (!loc.equals("") && !loc.equals("null")) {
                    uf.setLocalidad(CFDIconexion.remove1(loc));
                }
                uf.setMunicipio(CFDIconexion.remove1(rs0.getString("municipio")));
                uf.setEstado(CFDIconexion.remove1(rs0.getString("estado")));
                uf.setNoExterior(rs0.getString("numeroext"));
                String noint = "" + rs0.getString("numeroint");
                if (!noint.equals("") && !noint.equals("null")) {
                    uf.setNoInterior(noint);
                }
                uf.setPais(CFDIconexion.remove1(rs0.getString("pais")));
                receptor.setDomicilio(uf);
            }
            if (rs0 != null) {
                rs0.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }

        return receptor; //regresa el nodo completo
    }

    private static Conceptos createConceptos(ObjectFactory of, Connection conec, String documento) {
        Conceptos cps = of.createComprobanteConceptos();
        List<Concepto> list = cps.getConcepto();

        //agrega datos del cliente
        rs0 = null;
        try {
            //ajuste 29/09/2014
            String senSQL = "SELECT facturas_detalle.id_factura_detalle, facturas_detalle.factura_serie, facturas_detalle.estatus, facturas_detalle.factura, facturas_detalle.serie, "
            +"facturas_detalle.remision, facturas_detalle.id_op, facturas_detalle.clavearticulo, facturas_detalle.descripcion, facturas_detalle.um, "
            +"(CASE WHEN facturas_detalle.um='millar' OR facturas_detalle.um='MILLAR' THEN (facturas_detalle.cantidad/1000) ELSE facturas_detalle.cantidad END ) AS cantidad,  "
            +"(CASE WHEN facturas_detalle.um='millar' OR facturas_detalle.um='MILLAR' THEN (facturas_detalle.preciounitario*1000) ELSE facturas_detalle.preciounitario END ) AS preciounitario,  "
            +"facturas_detalle.subtotal FROM facturas_detalle WHERE factura_serie='" + documento + "';";
            rs0 = conexion.consulta(senSQL, conec);
            while (rs0.next()) {
                Concepto c1 = of.createComprobanteConceptosConcepto();
                c1.setUnidad(rs0.getString("um"));
                String rm = rs0.getString("remision");
                if (!rm.equals("") || !rm.equals("null")) {
                    c1.setNoIdentificacion(rm);
                }

                c1.setImporte(new BigDecimal(fijo2decimales.format(rs0.getDouble("subtotal"))));
                c1.setCantidad(new BigDecimal(fijo3decimales.format(rs0.getDouble("cantidad"))));
                c1.setDescripcion(CFDIconexion.remove1(rs0.getString("descripcion")));
                c1.setValorUnitario(new BigDecimal(fijo5decimales.format(rs0.getDouble("preciounitario"))));
                list.add(c1);
            }

            if (rs0 != null) {
                rs0.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }

        return cps;
    }

    private static Impuestos createImpuestos(ObjectFactory of, Connection conec, String documento) {
        Impuestos imps = of.createComprobanteImpuestos();
        //genera el nodo de impuestos trasladados
        Traslados trs = of.createComprobanteImpuestosTraslados();
        List<Traslado> list = trs.getTraslado();
        //genera el node de retenciones
        Retenciones ret = of.createComprobanteImpuestosRetenciones();
        List<Retencion> listret = ret.getRetencion();
        Double sumaivas = 0.0;
        Double sumaret = 0.0;
        //agrega datos los datos de impuestos
        rs0 = null;
        try {
            String senSQL = "SELECT facturas.*,impuestos.iva as tasaiva FROM facturas LEFT JOIN impuestos ON facturas.id_impuestos=impuestos.id_impuestos WHERE factura_serie='" + documento + "';";
            rs0 = conexion.consulta(senSQL, conec);
            if (rs0.next()) {
                //por cada tasa de iva genera un nodo
                Double ivas = rs0.getDouble("iva");
                Traslado t1 = of.createComprobanteImpuestosTrasladosTraslado();
                t1.setImporte(new BigDecimal(fijo2decimales.format(ivas)));
                t1.setImpuesto("IVA");
                t1.setTasa(new BigDecimal(rs0.getString("tasaiva")));
                list.add(t1);
                sumaivas += ivas;
                Double ivaret = rs0.getDouble("ivaretenido");
                Double isrret = rs0.getDouble("isrretenido");
                sumaret = ivaret + isrret;
                if (ivaret > 0.0) {
                    //retencion de iva
                    Retencion r1 = of.createComprobanteImpuestosRetencionesRetencion();
                    r1.setImporte(new BigDecimal(fijo2decimales.format(ivaret)));
                    r1.setImpuesto("IVA");
                    listret.add(r1);
                }
                if (isrret > 0.0) {
                    //retencion de iva
                    Retencion r2 = of.createComprobanteImpuestosRetencionesRetencion();
                    r2.setImporte(new BigDecimal(fijo2decimales.format(isrret)));
                    r2.setImpuesto("ISR");
                    listret.add(r2);
                }
            }
            if (rs0 != null) {
                rs0.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }

        imps.setTotalImpuestosTrasladados(new BigDecimal(fijo2decimales.format(sumaivas)));
        imps.setTotalImpuestosRetenidos(new BigDecimal(fijo2decimales.format(sumaret)));
        imps.setTraslados(trs);
        if (sumaret > 0.0)//si las retenciones son mayor a 0 ingresa los nodos
        {
            imps.setRetenciones(ret);
        }


        return imps;
    }
}
