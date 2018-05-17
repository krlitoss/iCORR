/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conta;

import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

import java.sql.SQLException;

/**
 *
 * @author Skarton
 */
/**
 * CLASE PARA GENERAR NOTA DE CREDITO*
 */
public final class CFDFactory_nc33 {

    private static ResultSet rs0 = null;

    public static CfdiNC33 createComprobante(Connection conec, String documento) throws Exception {

        CfdiNC33 cfdi = new CfdiNC33();
        rs0 = null;
        try {

            //cfdi.setCondicionesDePago("Contado");
            // Lugar Expedicion
            try {
                String senSQL = "SELECT notas_credito.nota_credito_serie,lugaresemision.* FROM notas_credito LEFT JOIN lugaresemision ON notas_credito.id_lugaremision=lugaresemision.id_lugaremision WHERE nota_credito_serie='" + documento + "';";
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
                String senSQL = "SELECT notas_credito.*, folios.serie, folios.aprobacion, folios.anoaprobacion FROM notas_credito LEFT JOIN folios ON notas_credito.id_folios = folios.id_folio WHERE nota_credito_serie = '" + documento + "';";
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
                String senSQL = "SELECT notas_credito.*, folios.serie, folios.aprobacion, folios.anoaprobacion FROM notas_credito LEFT JOIN folios ON notas_credito.id_folios = folios.id_folio WHERE nota_credito_serie = '" + documento + "';";
                rs0 = conexion.consulta(senSQL, conec);
                if (rs0.next()) {
                    cfdi.setFolio(CFDIconexion.remove1(rs0.getString("nota_credito")));
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
                String senSQL = "SELECT notas_credito.*, folios.serie, folios.aprobacion, folios.anoaprobacion FROM notas_credito LEFT JOIN folios ON notas_credito.id_folios = folios.id_folio WHERE nota_credito_serie = '" + documento + "';";
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
                String senSQL = "SELECT notas_credito.*, folios.serie, folios.aprobacion, folios.anoaprobacion FROM notas_credito LEFT JOIN folios ON notas_credito.id_folios = folios.id_folio WHERE nota_credito_serie = '" + documento + "';";
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
            try {
                String senSQL = "SELECT notas_credito_detalle.* FROM notas_credito_detalle WHERE nota_credito_serie='" + documento + "';";
                rs0 = conexion.consulta(senSQL, conec);
                if (rs0.next()) {
                    cfdi.setTipoRelacion(CFDIconexion.remove1(rs0.getString("clave_relacionado")));
                    cfdi.addRelacionado(CFDIconexion.remove1(rs0.getString("uuid_relacionado")));
                }
                if (rs0 != null) {
                    rs0.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "ERROR AL CONSULTAR RELACIONADOS" + e, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }
            

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
                String senSQL = "SELECT notas_credito.nota_credito_serie, notas_credito.clave_usocomprobante, clientes.nombre, clientes.rfc FROM notas_credito LEFT JOIN clientes ON notas_credito.id_clientes = clientes.id_clientes WHERE nota_credito_serie = '" + documento + "';";
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
                String senSQL = "SELECT notas_credito.*,impuestos.iva as tasaiva FROM notas_credito LEFT JOIN impuestos ON notas_credito.id_impuestos=impuestos.id_impuestos WHERE nota_credito_serie='" + documento + "';";
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
                String senSQL = "SELECT notas_credito_detalle.* FROM notas_credito_detalle WHERE nota_credito_serie='" + documento + "';";
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
