/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conta;

import java.sql.Connection;
import java.sql.ResultSet;
//import java.text.DecimalFormat;
import javax.swing.JOptionPane;



import java.sql.SQLException;


/**
 *
 * @author Skarton
 */
/**
 * CLASE PARA GENERAR FACTURA POR REMISIONES*
 */
public final class CFDFactory33 {

    private static ResultSet rs0 = null;


    public static CfdiF33 createComprobante(Connection conec, String documento) throws Exception {

        CfdiF33 cfdi = new CfdiF33();
        rs0 = null;
        try {
            //cfdi.setCondicionesDePago("Contado"); //Este campo aun no se ha habilitado//

            // Lugar Expedicion
            try {
                String senSQL = "SELECT facturas.factura_serie,lugaresemision.* FROM facturas LEFT JOIN lugaresemision ON facturas.id_lugaremision=lugaresemision.id_lugaremision WHERE factura_serie='" + documento + "';";
                rs0 = conexion.consulta(senSQL, conec);
                if (rs0.next()) {
                    cfdi.setLugarExpedicion(CFDIconexion.remove1(rs0.getString("cod_postal")));
                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "ERROR AL CONSULTAR DATOS DE CODIGO POSTAL" + e, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }

            //fecha y hora
            try {
                String senSQL = "SELECT facturas.*, folios.serie, folios.aprobacion, folios.anoaprobacion FROM facturas  LEFT JOIN folios ON facturas.id_folios = folios.id_folio WHERE factura_serie = '" + documento + "';";
                rs0 = conexion.consulta(senSQL, conec);
                if (rs0.next()) {
                    cfdi.setFecha(rs0.getTimestamp("fecha"));
                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "ERROR AL CONSULTAR FECHA" + e, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }

            //Folio
            try {
                String senSQL = "SELECT facturas.*, folios.serie, folios.aprobacion, folios.anoaprobacion FROM facturas  LEFT JOIN folios ON facturas.id_folios = folios.id_folio WHERE factura_serie = '" + documento + "';";
                rs0 = conexion.consulta(senSQL, conec);
                if (rs0.next()) {
                    cfdi.setFolio(CFDIconexion.remove1(rs0.getString("factura")));
                    cfdi.setSerie(CFDIconexion.remove1(rs0.getString("serie")));
                    
                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "ERROR AL CONSULTAR FOLIO" + e, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }

            //forma de pago
            try {
                String senSQL = "SELECT facturas.*, folios.serie, folios.aprobacion, folios.anoaprobacion FROM facturas  LEFT JOIN folios ON facturas.id_folios = folios.id_folio WHERE factura_serie = '" + documento + "';";
                rs0 = conexion.consulta(senSQL, conec);
                if (rs0.next()) {
                    cfdi.setFormaPago(CFDIconexion.remove1(rs0.getString("clave_formapago")));
                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "ERROR AL CONSULTAR FORMA DE PAGO" + e, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }

            //metodo de pago
            try {
                String senSQL = "SELECT facturas.*, folios.serie, folios.aprobacion, folios.anoaprobacion FROM facturas  LEFT JOIN folios ON facturas.id_folios = folios.id_folio WHERE factura_serie = '" + documento + "';";
                rs0 = conexion.consulta(senSQL, conec);
                if (rs0.next()) {
                    cfdi.setMetodoPago(CFDIconexion.remove1(rs0.getString("clave_metodopago")));
                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "ERROR AL CONSULTAR METODO DE PAGO" + e, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }

            // Relacionados
//            cfdi.setTipoRelacion("04");
//            cfdi.addRelacionado("6EA9FB58-37CB-40CD-B599-535F12EA4F55");

            // Emisor
            try {
                String senSQL = "SELECT * FROM empresa WHERE id='1';";
                rs0 = conexion.consulta(senSQL, conec);
                if (rs0.next()) {
                    cfdi.setEmisor(CFDIconexion.remove1(rs0.getString("nombre")), (rs0.getString("rfc")), (rs0.getString("clave_regimenfiscal")));
                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ERROR AL CONSULTAR DATOS DEL EMISOR" + e, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }

            // Receptor
            try {
                String senSQL = "SELECT facturas.factura_serie,facturas.clave_usocomprobante,clientes.* FROM facturas LEFT JOIN clientes ON facturas.id_clientes=clientes.id_clientes WHERE factura_serie='" + documento + "';";
                rs0 = conexion.consulta(senSQL, conec);
                if (rs0.next()) {
                    cfdi.setReceptor(CFDIconexion.remove1(rs0.getString("nombre")), (rs0.getString("rfc")), (rs0.getString("clave_usocomprobante")));
                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ERROR AL CONSULTAR DATOS DEL CLIENTE" + e, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }
            
            // Impuestos
            Double tasa_iva = 0.0;
            Double tasa_ivaretenido = 0.0;
            try {
                String senSQL = "SELECT facturas.*,impuestos.iva as tasaiva,impuestos.ivaretenido as tasaivaretenido FROM facturas LEFT JOIN impuestos ON facturas.id_impuestos=impuestos.id_impuestos WHERE factura_serie='" + documento + "';";
                rs0 = conexion.consulta(senSQL, conec);
                if (rs0.next()) {
                    tasa_iva = rs0.getDouble("tasaiva") / 100;
                    tasa_ivaretenido = rs0.getDouble("tasaivaretenido") / 100;
                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }

            // Conceptos
            try {
                String senSQL = "SELECT facturas_detalle.id_factura_detalle, facturas_detalle.factura_serie, facturas_detalle.estatus, facturas_detalle.factura, "
                        + "facturas_detalle.serie, facturas_detalle.remision, facturas_detalle.id_op, facturas_detalle.clavearticulo, facturas_detalle.descripcion, "
                        + "facturas_detalle.clave_prodserv, facturas_detalle.um, facturas_detalle.clave_unidadmedida, "
                        + "CAST ( CASE WHEN facturas_detalle.um = 'millar' OR facturas_detalle.um = 'MILLAR' THEN ( facturas_detalle.cantidad / 1000 ) ELSE facturas_detalle.cantidad END AS DECIMAL (9, 4)) AS cantidad, "
                        + "( CASE WHEN facturas_detalle.um = 'millar' OR facturas_detalle.um = 'MILLAR' THEN ( facturas_detalle.preciounitario * 1000 ) ELSE facturas_detalle.preciounitario END ) AS preciounitario, "
                        + "facturas_detalle.subtotal FROM facturas_detalle WHERE factura_serie = '" + documento + "';";
                rs0 = conexion.consulta(senSQL, conec);
                while (rs0.next()) {
                    Double cantidad = rs0.getDouble("cantidad");
                    Double precio_unitario = rs0.getDouble("preciounitario");
                    PartidaCfdi partida = new PartidaCfdi(cantidad, (rs0.getString("um")), (rs0.getString("clave_unidadmedida")), (rs0.getString("clavearticulo")), (rs0.getString("clave_prodserv")), (rs0.getString("descripcion")), precio_unitario, 0D);
                    // Impuestos    
                    //partida.agregarImpuestoRetenido(100D, ImpuestoCfdi.IVA, ImpuestoCfdi.TASA, 0.16, 16D);
                    //partida.agregarImpuestoRetenido(100D, ImpuestoCfdi.ISR, ImpuestoCfdi.TASA, 0.10, 10D);
                    //partida.agregarImpuestoTrasladado(100D, ImpuestoCfdi.IVA, ImpuestoCfdi.TASA, 0.16, 16D);
                    Double base = cantidad * precio_unitario;
                    Double importe = base * tasa_iva;
                    partida.agregarImpuestoTrasladado(base, ImpuestoCfdi.IVA, ImpuestoCfdi.TASA, tasa_iva, importe);
                    if(tasa_ivaretenido>0){
                        Double importe_retenido = base * tasa_ivaretenido;
                        partida.agregarImpuestoRetenido(base, ImpuestoCfdi.IVA, ImpuestoCfdi.TASA, tasa_ivaretenido, importe_retenido);
                    }
                    cfdi.agregarPartida(partida);

                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ERROR AL CONSULTAR PARTIDAS" + e, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }

            // Moneda
            cfdi.setMoneda("MXN");
            // Tipo de cambio
            cfdi.setTipoCambio(1D);

            return cfdi;

           
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR AL CREAR XML\n" + e, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}
