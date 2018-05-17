/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * usuarios.java
 *
 * Created on 17/01/2010, 09:38:56 PM
 */
package conta;

import java.awt.Desktop;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import jxl.*;
import jxl.write.*;
import org.apache.poi.hssf.record.formula.functions.Replace;

/**
 *
 * @author IVONNE
 */
public class sae_imp_remisiones_facturadas extends javax.swing.JInternalFrame {

    Connection conn = null;
    DefaultTableModel modelot1 = null;
    ResultSet rs0;
    DecimalFormat moneda2decimales = new DecimalFormat("$ #,###,##0.00");
    DecimalFormat fijo2decimales = new DecimalFormat("######0.00");
    DecimalFormat fijo0decimales = new DecimalFormat("######0");
    DecimalFormat estandarentero = new DecimalFormat("#,###,##0");
    DecimalFormat estandar1decimal=new DecimalFormat("#,###,##0.0");
    SimpleDateFormat fechacorta = new SimpleDateFormat("dd-MMM-yy");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechamascorta = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private Properties conf;
    ListSelectionModel modelot1seleccion;
    String valor_privilegio="1";
    Connection conex_sql=null;
    SimpleDateFormat fechahora_importar = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Calendar calendarvencimiento = new GregorianCalendar();
    String user = "";


    /** Creates new form usuarios */
    public sae_imp_remisiones_facturadas(Connection connt) {
        initComponents();
        /**da formato al jframe cambiando el icono y centrandolo*/
        conf = conexion.archivoInicial();
        conn = connt;
        conexion_sqlserver();
        modelot1 = (DefaultTableModel) Tabladatos.getModel();
        Tabladatos.setModel(modelot1);
        modelot1seleccion = Tabladatos.getSelectionModel();
        ajusteTabladatos();
        datos_privilegios();

        /**funcion para ordenar datos*/
        TableRowSorter<TableModel> elQueOrdena = new TableRowSorter<TableModel>(modelot1);
        Tabladatos.setRowSorter(elQueOrdena);
        limpiatabla();
        datos("","");
        sumaregistros();

        user = conf.getProperty("UserID");

        modelot1seleccion.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.isSelectionEmpty()) {
                } else {
                }
            }
        });
    }

    public void conexion_sqlserver() {       ////****eliminar para el control maestro*******////////
        if (conex_sql != null) {
        } else {
            conex_sql = conexion.abrirconexion_sqlserver();
        }

    }

    public void salir() {
        if (conn != null) {
            conn = null;
        }
        dispose();
        this.setVisible(false);
    }
    public void datos_privilegios(){
        valor_privilegio=conexion.obtener_privilegios(conn,"Importar Remisiones Facturadas");
    }

    public void ajusteTabladatos() {

        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(5);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(120);
        Tabladatos.getColumnModel().getColumn(4).setPreferredWidth(100);

        Tabladatos.getColumnModel().getColumn(7).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(8).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(9).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(10).setPreferredWidth(100);

    }

    public void limpiatabla() {
        modelot1.setNumRows(0);
    }

    public void datos(String fecha_ini,String fecha_fin){
        rs0=null;
        if(fecha_ini.equals("")){
            fecha_ini = conf.getProperty("FechaIni");
        }
        if(fecha_fin.equals("")){
            fecha_fin = conf.getProperty("FechaFin");
        }
        fecha_ini = fecha_ini.replace("-", "").replace("/","");
        fecha_fin = fecha_fin.replace("-", "").replace("/","");

        try{
            String senSQL = "SELECT factura.serie,factura.folio, "
            +"(CONVERT(VARCHAR(10), factura.FECHAELAB, 103) + ' ' + CONVERT(VARCHAR(8), factura.FECHAELAB, 108)) AS 'fechaelba', "
            +"RTRIM(LTRIM(DOC_ANT)) as remision, "
            +"REPLACE(ROUND(fac_detalle.CANT,4),',','') AS cantidad, "
            +"fac_detalle.uni_venta, "
            +"(CAST(factura.FOLIO AS VARCHAR) + '' + factura.SERIE) AS factura_serie, "
            +"cliente_lib.CAMPLIB1 AS clave_cliente, "
            +"factura.CVE_PEDI AS factura_supedido, "
            +"REPLACE(ROUND(factura.CAN_TOT,4),',','') AS subtotal, "
            +"REPLACE(ROUND(factura.IMP_TOT4,4),',','') AS iva, "
            +"REPLACE(ROUND(factura.DES_TOT,4),',','') AS descuento, "
            +"observaciones.STR_OBS AS comentarios, "
            +"factura.NUM_ALMA AS clave_almacen, "
            +"factura.ENLAZADO AS factura_enlazado, "
            +"factura.TIP_DOC_E AS doc_enlazado, "
            +"factura.NUM_MONED AS clave_moneda, "
            +"factura.TIPCAMB AS valor_moneda, "
            +"REPLACE(ROUND(factura.IMPORTE,4),',','') AS importe_total, "
            +"factura.METODODEPAGO AS metodo_pago, "
            +"factura.NUMCTAPAGO AS cuenta_pago, "
            +"fac_detalle.CVE_ART AS clave_articulo, "
            +"REPLACE(ROUND(fac_detalle.PREC,4),',','') AS precio_unitario, "
            +"REPLACE(ROUND(fac_detalle.TOT_PARTIDA,4),',','') AS total_partida "
            +"FROM FACTF03 AS factura "
            +"INNER JOIN CLIE03 AS cliente ON factura.CVE_CLPV=cliente.CLAVE "
            +"INNER JOIN CLIE_CLIB03 AS cliente_lib ON factura.CVE_CLPV=cliente_lib.CVE_CLIE "
            +"INNER JOIN PAR_FACTF03 AS fac_detalle ON factura.cve_doc=fac_detalle.cve_doc "
            +"LEFT JOIN OBS_DOCF03 AS observaciones on factura.CVE_OBS=observaciones.CVE_OBS "
            +"LEFT JOIN OBS_DOCF03 AS observaciones_d on fac_detalle.CVE_OBS=observaciones_d.CVE_OBS "
            +"WHERE factura.STATUS='E' "
            +"AND factura.FECHAELAB>='"+fecha_ini+" 00:00:00' AND factura.FECHAELAB<='"+fecha_fin+" 23:59:59' "
            +"ORDER BY factura.CVE_DOC;";
            
            
            rs0=conexion.consulta(senSQL,conex_sql);
            if(rs0!=null){
                while(rs0.next()){
                    
                    String no_remision=rs0.getString("remision");

                    //valida que la remision exista y este por facturar
                    senSQL = "SELECT remisiones.remision,remisiones.fechahora,remisiones.um,remisiones.cantidad,(remisiones.cantidad-COALESCE(facturado.facturacantidad,0)) as cantrempendiente,remisiones.preciounitario,remisiones.id_op,remisiones.clavearticulo,articulos.articulo,articulos.kg,articulos.m2,clientes.nombre,COALESCE(facturado.facturacantidad,0) as facturacantidad,COALESCE(facturado.facturaimporte,0) as facturaimporte FROM ((remisiones LEFT JOIN clientes ON remisiones.id_clientes=clientes.id_clientes)  LEFT JOIN articulos ON remisiones.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT facturas_detalle.remision,sum(facturas_detalle.cantidad) as facturacantidad,sum(facturas_detalle.cantidad*facturas_detalle.preciounitario) as facturaimporte FROM facturas_detalle WHERE (facturas_detalle.estatus='1') GROUP BY facturas_detalle.remision) as facturado ON remisiones.remision=facturado.remision WHERE (remisiones.estatus<>'Can' AND remisiones.remision='" + no_remision + "' AND facturado='false') ORDER BY remisiones.fechahora;";
                    ResultSet remision_info = conexion.consulta(senSQL, conn);
                    if (remision_info.next()) {
                        Object datos[]={true,rs0.getString("serie"),rs0.getString("folio"),rs0.getString("fechaelba"),no_remision,rs0.getString("cantidad"),rs0.getString("uni_venta"),rs0.getString("clave_cliente"),rs0.getString("clave_articulo"),rs0.getString("precio_unitario"),rs0.getString("total_partida")};
                        modelot1.addRow(datos);
                    }
                }
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

    }

    public void modificar() {
        int filano = Tabladatos.getSelectedRow();
        if (filano < 0) {
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        } else {
        }
    }

    public void sumaregistros() {
        noregistros.setText("Registros: " + Tabladatos.getRowCount());
    }

    private void importar_facturas(){
        if (JOptionPane.showConfirmDialog(this, "ESTA SEGURO QUE DESEA CONTINUAR!!", " I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {

                /********************************************* Validacion de datos ************************************************/
                String error = "";
                int i = 1;
                int filas = Tabladatos.getRowCount();
                for (int r = 0; r <= (filas - 1); r = r + 1) {
                    String p_serie = (String) Tabladatos.getValueAt(r, 1), p_folio = (String) Tabladatos.getValueAt(r, 2), p_factura_serie =  (String) Tabladatos.getValueAt(r, 2)+(String) Tabladatos.getValueAt(r, 1), p_fecha = (String) Tabladatos.getValueAt(r, 3), p_remision = (String) Tabladatos.getValueAt(r, 4), p_cantidad = (String) Tabladatos.getValueAt(r, 5), p_um = (String) Tabladatos.getValueAt(r, 6);
                    java.lang.Boolean check = (java.lang.Boolean)Tabladatos.getValueAt(r, 0);
                    //Validamos campos vacios
                    if (p_remision.equals("") || p_factura_serie.equals("") || p_cantidad.equals("") || p_fecha.equals("") || p_um.equals("")) {
                        error += "Linea (" + i + ") Existen campos vacios\n";
                    } else {
                        //Valida que la remision exista
                        String senSQL = "SELECT remisiones.remision,remisiones.fechahora,remisiones.um,remisiones.cantidad,(remisiones.cantidad-COALESCE(facturado.facturacantidad,0)) as cantrempendiente,remisiones.preciounitario,remisiones.id_op,remisiones.clavearticulo,articulos.articulo,articulos.kg,articulos.m2,clientes.nombre,COALESCE(facturado.facturacantidad,0) as facturacantidad,COALESCE(facturado.facturaimporte,0) as facturaimporte FROM ((remisiones LEFT JOIN clientes ON remisiones.id_clientes=clientes.id_clientes)  LEFT JOIN articulos ON remisiones.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT facturas_detalle.remision,sum(facturas_detalle.cantidad) as facturacantidad,sum(facturas_detalle.cantidad*facturas_detalle.preciounitario) as facturaimporte FROM facturas_detalle WHERE (facturas_detalle.estatus='1') GROUP BY facturas_detalle.remision) as facturado ON remisiones.remision=facturado.remision WHERE (remisiones.estatus<>'Can' AND remisiones.remision='" + p_remision + "' AND facturado='false') ORDER BY remisiones.fechahora;";
                        ResultSet remision_info = conexion.consulta(senSQL, conn);
                        if (remision_info.next()) {
                            Double cantidad_pendiente = remision_info.getDouble("cantrempendiente");
                            Double cantidad_remision = Double.parseDouble(p_cantidad);
                            //Valida que la cantidad factura no supere la pendiente por facturar
                            if (cantidad_remision > cantidad_pendiente) {
                                error += "Linea (" + i + ") La cantidad (" + estandarentero.format(cantidad_remision) + ") de la remision " + p_remision + " es mayor a lo pendiente de remisionar (" + estandarentero.format(cantidad_pendiente) + ")\n";
                            }
                        } else {
                            error += "Linea (" + i + ") La remision " + p_remision + " no existe\n";
                        }
                    }
                    i++;
                }
                /********************************************* Fin validacion de datos ************************************************/
                if (!error.equals("")) { //Validamos si hay errores
                    JOptionPane.showMessageDialog(this, "Existen errores en el archivo\n" + error, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
                } else {

                    String partidas[][] = new String[50][7]; //Variable temporal para guardar por factura
                    boolean nueva_factura = false; //Bandera para validar si cambia de factura
                    String factura_serie_ant = ""; //Validar cambio de factura
                    int fac_i = 0;

                    /******************************************** Recorrer archivo por lineas *******************************************/
                    for (int r = 0; r <= (filas - 1); r = r + 1) {

                        
                        String p_serie = (String) Tabladatos.getValueAt(r, 1), p_folio = (String) Tabladatos.getValueAt(r, 2), p_factura_serie =  (String) Tabladatos.getValueAt(r, 2)+(String) Tabladatos.getValueAt(r, 1), p_fecha = (String) Tabladatos.getValueAt(r, 3), p_remision = (String) Tabladatos.getValueAt(r, 4), p_cantidad = (String) Tabladatos.getValueAt(r, 5), p_um = (String) Tabladatos.getValueAt(r, 6);
                        java.lang.Boolean check = (java.lang.Boolean)Tabladatos.getValueAt(r, 0);

                        nueva_factura = false; //Cuando ya cambio de factura guardo el registro
                        if (factura_serie_ant.equals("")) { //Iniciamos variable para primera partida
                            factura_serie_ant = p_factura_serie;
                        }

                        //Leemos ordenado todas las remisiones hasta que cambia de folio
                        if (!factura_serie_ant.equals(p_factura_serie)) {
                            nueva_factura = true;
                        }

                        /****************************************** Nueva Factura *****************************************/
                        if (nueva_factura) {
                            int j = 0;
                            String senSQL = "";

                            //Variables cabecera de factura
                            String fecha = "";
                            String clave_cliente = "";
                            String serie = "";
                            String folio = "";
                            String factura_serie = "";
                            Double subtotal_general = 0.0;
                            Double fletes = 0.0;
                            Double descuento = 0.0;
                            Double iva = 0.0;
                            Double retiva = 0.0;
                            Double retisr = 0.0;

                            for (j = 0; j < partidas.length; j++) { //Recorer la variable temporal para detalles de factura

                                if (partidas[j][2] != null) { //Validamos que la fecha no este vacia

                                    //Variables de encabezado
                                    serie = partidas[j][0];
                                    folio = partidas[j][1];
                                    factura_serie = partidas[j][2];
                                    fecha = partidas[j][3];

                                    //Varibales por cada partida
                                    String remision = partidas[j][4];
                                    Double cantidad = Double.parseDouble(partidas[j][5]);
                                    String um = partidas[j][6]; //unidad de medida
                                    Double pu = 0.0; //precio unitario
                                    String id_op = ""; //op
                                    String clave_articulo = ""; //clave articulo
                                    String descripcion_articulo = ""; //descripcion articulo
                                    if (um.toUpperCase().equals("MILLAR")) { //para el caso de millar hace ajustes de cantidad
                                        //cantidad = cantidad*1000;
                                        //pu = pu/1000;
                                    }

                                    //Consultamos la remision para actualizar datos
                                    senSQL = "SELECT remisiones.remision,remisiones.fechahora,remisiones.um,remisiones.cantidad,(remisiones.cantidad-COALESCE(facturado.facturacantidad,0)) as cantrempendiente,remisiones.preciounitario,remisiones.id_op,remisiones.clavearticulo,articulos.articulo,articulos.kg,articulos.m2,remisiones.id_clientes,clientes.nombre,COALESCE(facturado.facturacantidad,0) as facturacantidad,COALESCE(facturado.facturaimporte,0) as facturaimporte FROM ((remisiones LEFT JOIN clientes ON remisiones.id_clientes=clientes.id_clientes)  LEFT JOIN articulos ON remisiones.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT facturas_detalle.remision,sum(facturas_detalle.cantidad) as facturacantidad,sum(facturas_detalle.cantidad*facturas_detalle.preciounitario) as facturaimporte FROM facturas_detalle WHERE (facturas_detalle.estatus='1') GROUP BY facturas_detalle.remision) as facturado ON remisiones.remision=facturado.remision WHERE (remisiones.estatus<>'Can' AND remisiones.remision='" + remision + "' AND remisiones.facturado='false') ORDER BY remisiones.fechahora;";
                                    ResultSet remision_info = conexion.consulta(senSQL, conn);
                                    if (remision_info.next()) {
                                        clave_cliente = remision_info.getString("id_clientes"); //Variable de encabezado
                                        pu = remision_info.getDouble("preciounitario");
                                        id_op = remision_info.getString("id_op");
                                        clave_articulo = remision_info.getString("clavearticulo");
                                        descripcion_articulo = remision_info.getString("articulo");
                                    }
                                    if (remision_info != null) {
                                        remision_info.close();
                                    }

                                    //CalculaSubtotales
                                    Double subtotal = cantidad * pu;
                                    subtotal_general += subtotal;

                                    //Guarda la partida
                                    senSQL = "INSERT INTO facturas_detalle(factura_serie, estatus, factura, serie, remision, id_op, clavearticulo, descripcion, cantidad, um, preciounitario, subtotal) VALUES ('" + factura_serie + "', '1', '" + folio + "', '" + serie + "', '" + remision + "', '" + id_op + "', '" + clave_articulo + "', '" + descripcion_articulo + "', '" + cantidad + "', '" + um + "', '" + pu + "', '" + subtotal + "');";
                                    conexion.modificamov_p(senSQL, conn, valor_privilegio);

                                    //Actualiza remision como facturada si esta completa
                                    senSQL = "SELECT remisiones.remision,remisiones.fechahora,remisiones.um,remisiones.cantidad,(remisiones.cantidad-COALESCE(facturado.facturacantidad,0)) as cantrempendiente,remisiones.preciounitario,remisiones.id_op,remisiones.clavearticulo,articulos.articulo,articulos.kg,articulos.m2,clientes.nombre,COALESCE(facturado.facturacantidad,0) as facturacantidad,COALESCE(facturado.facturaimporte,0) as facturaimporte FROM ((remisiones LEFT JOIN clientes ON remisiones.id_clientes=clientes.id_clientes)  LEFT JOIN articulos ON remisiones.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT facturas_detalle.remision,sum(facturas_detalle.cantidad) as facturacantidad,sum(facturas_detalle.cantidad*facturas_detalle.preciounitario) as facturaimporte FROM facturas_detalle WHERE (facturas_detalle.estatus='1') GROUP BY facturas_detalle.remision) as facturado ON remisiones.remision=facturado.remision WHERE (remisiones.estatus<>'Can' AND remisiones.remision='" + remision + "');";
                                    remision_info = conexion.consulta(senSQL, conn);
                                    if (remision_info.next()) {
                                        Double cantidadporrem = remision_info.getDouble("cantrempendiente");
                                        if (cantidadporrem <= 0.0) {
                                            senSQL = "UPDATE remisiones SET facturado='TRUE' WHERE remision='" + remision + "';";
                                            conexion.modificamov_p(senSQL, conn, valor_privilegio);
                                        }
                                    }
                                    if (remision_info != null) {
                                        remision_info.close();
                                    }
                                }
                            }//FIN Detalles de factura

                            //Partida detalle adicional por fletes
                            if (fletes > 0) {
                                senSQL = "INSERT INTO facturas_detalle(factura_serie, estatus, factura, serie, remision, id_op, clavearticulo, descripcion, cantidad, um, preciounitario, subtotal) VALUES ('" + factura_serie + "', '1', '" + folio + "', '" + serie + "', '0001', '0', 'FLT', 'FLETE FECHA " + fechainsertar.format(new Date()) + "', '1', 'ser','" + fletes + "','" + fletes + "');";
                                conexion.modificamov_p(senSQL, conn, valor_privilegio);
                            }

                            //Variables cabecera de factura
                            int diasc = 0;

                            //Datos del cliente
                            senSQL = "SELECT clientes.* FROM clientes WHERE id_clientes='" + clave_cliente + "' ";
                            ResultSet customer_info = conexion.consulta(senSQL, conn);
                            if (customer_info.next()) {
                                diasc = customer_info.getInt("dias");
                            }


                            //Variables cabecera de factura
                            Date fechacreado = fechahora_importar.parse(fecha); //Fecha factura
                            if (diasc > 0) {
                                calendarvencimiento.add(Calendar.DATE, diasc); //Fecha vencimiento
                            }
                            Date fechaven = calendarvencimiento.getTime(); //Fecha vencimiento
                            int id_folio = 6; //Creado para los folios 1A que seran del SAE y para el ENNOVI 1F
                            String oc = ""; //Este lo obtenemos de todas las op de las remisiones
                            int id_lugar = 1; //Lugar de emision
                            int id_moneda = 1; //Es para MXN pero hay que revisar si todo sera en pesos
                            Double valormoneda = 1.0;
                            String moneda = "PESOS";
                            String terminacion = "M.N.";
                            senSQL = "SELECT * FROM monedas WHERE id_moneda='" + id_moneda + "'";
                            ResultSet moneda_info = conexion.consulta(senSQL, conn);
                            if (moneda_info.next()) { //Actualiza informacion de moneda
                                valormoneda = moneda_info.getDouble("valor");
                                moneda = moneda_info.getString("descripcion");
                                terminacion = moneda_info.getString("terminacion");
                            }
                            int id_impuestos = 1; // Esta la tasa de impuestos normal de IVA
                            Double tasaiva = 0.0;
                            Double tasaretiva = 0.0;
                            Double tasaretisr = 0.0;
                            senSQL = "SELECT * FROM impuestos WHERE id_impuestos='" + id_impuestos + "'";
                            ResultSet impuesto_info = conexion.consulta(senSQL, conn);
                            if (impuesto_info.next()) { //Actualiza informacion de impuestos
                                tasaiva = impuesto_info.getDouble("iva");
                                tasaretiva = impuesto_info.getDouble("ivaretenido");
                                tasaretisr = impuesto_info.getDouble("isrretenido");
                            }
                            String notas = ""; //Notas de la factura
                            int id_formapago = 7; // Lo dejamos como no identificado
                            String ctapago = ""; // Cuenta de pago vacio


                            //Calculos finales
                            Double importe = subtotal_general + fletes - descuento;
                            iva = (tasaiva / 100) * importe;
                            retiva = (tasaretiva / 100) * importe;
                            retisr = (tasaretisr / 100) * importe;
                            Double total = importe + iva - retiva - retisr;
                            //Importe con letra
                            String total_str = fijo2decimales.format(total);
                            int dotPos = total_str.lastIndexOf(".") + 1;
                            String partedecimal = total_str.substring(dotPos);
                            String parteentero = total_str.substring(0, (dotPos - 1));
                            String numeroletra = "(" + new numerosletras().convertirLetras(Integer.parseInt(parteentero)) + " " + moneda + " " + partedecimal + "/100 " + terminacion + ")";
                            if (parteentero.equals("1")) {
                                numeroletra = "(UN PESO " + partedecimal + "/100 " + terminacion + ")";
                            }
                            numeroletra = numeroletra.toUpperCase();

                            //Guarda registros
                            senSQL = "INSERT INTO facturas(factura_serie, factura, serie, fecha, estatus, id_folios, id_moneda, id_clientes, id_lugaremision, id_impuestos, formadepago, ordencompra, valortipocambio, subtotal, descuento, fletes, iva, ieps, ivaretenido, isrretenido, total, importeletra, pagada, usuario, observaciones,id_formapago,cuentapago) VALUES ('"
                                    + factura_serie + "', '" + folio + "', '" + serie + "', '" + fechainsertarhora.format(fechacreado) + "', '1', '" + id_folio + "', '" + id_moneda + "', '" + clave_cliente + "', '" + id_lugar + "', '" + id_impuestos + "', 'Pago en una sola exhibición', '" + oc + "', '" + valormoneda + "', '" + fijo2decimales.format(importe) + "', '" + fijo2decimales.format(descuento) + "', '" + fijo2decimales.format(fletes) + "', '" + fijo2decimales.format(iva) + "', '0', '" + fijo2decimales.format(retiva) + "', '" + fijo2decimales.format(retisr) + "', '" + fijo2decimales.format(total) + "', '" + numeroletra + "', 'FALSE', '" + user + "', '" + notas + "', '" + id_formapago + "', '" + ctapago + "');";
                            conexion.modificamov_p(senSQL, conn, valor_privilegio);
                            senSQL = "INSERT INTO docxcob(factura_serie, fechaemision, fechavencimiento, importe, estatus, id_clientes) VALUES ('"
                                    + factura_serie + "', '" + fechainsertarhora.format(fechacreado) + "', '" + fechainsertarhora.format(fechaven) + "', '" + fijo2decimales.format(total) + "', 'Act', '" + clave_cliente + "');";
                            conexion.modificamov_p(senSQL, conn, valor_privilegio);

                            //Actualiza folios ultimo registro
                            senSQL = "UPDATE folios SET folioactual='" + (Integer.parseInt(folio) + 1) + "' WHERE id_folio='" + id_folio + "';";
                            conexion.modificamov_p(senSQL, conn, valor_privilegio);

                        }
                        /****************************************** FIN Nueva Factura *****************************************/

                        //Reinicio de variable temporal
                        if (!factura_serie_ant.equals(p_factura_serie)) {
                            fac_i = 0;
                            partidas = new String[50][7];
                            factura_serie_ant = p_factura_serie;
                        }
                        partidas[fac_i][0] = p_serie;
                        partidas[fac_i][1] = p_folio;
                        partidas[fac_i][2] = p_factura_serie;
                        partidas[fac_i][3] = p_fecha;
                        partidas[fac_i][4] = p_remision;
                        partidas[fac_i][5] = p_cantidad;
                        partidas[fac_i][6] = p_um;
                        fac_i++;

                    }
                    /******************************************** FIN Recorrer archivo por lineas *******************************************/

                    /************************************* Creamos ultima fila ******************************************/
                    int j = 0;
                    String senSQL = "";

                    //Variables cabecera de factura
                    String fecha = "";
                    String clave_cliente = "";
                    String serie = "";
                    String folio = "";
                    String factura_serie = "";
                    Double subtotal_general = 0.0;
                    Double fletes = 0.0;
                    Double descuento = 0.0;
                    Double iva = 0.0;
                    Double retiva = 0.0;
                    Double retisr = 0.0;

                    for (j = 0; j < partidas.length; j++) { //Recorer la variable temporal para detalles de factura

                        if (partidas[j][2] != null) { //Validamos que la fecha no este vacia

                            //Variables de encabezado
                            serie = partidas[j][0];
                            folio = partidas[j][1];
                            factura_serie = partidas[j][2];
                            fecha = partidas[j][3];

                            //Varibales por cada partida
                            String remision = partidas[j][4];
                            Double cantidad = Double.parseDouble(partidas[j][5]);
                            String um = partidas[j][6]; //unidad de medida
                            Double pu = 0.0; //precio unitario
                            String id_op = ""; //op
                            String clave_articulo = ""; //clave articulo
                            String descripcion_articulo = ""; //descripcion articulo
                            if (um.toUpperCase().equals("MILLAR")) { //para el caso de millar hace ajustes de cantidad
                                //cantidad = cantidad*1000;
                                //pu = pu/1000;
                            }

                            //Consultamos la remision para actualizar datos
                            senSQL = "SELECT remisiones.remision,remisiones.fechahora,remisiones.um,remisiones.cantidad,(remisiones.cantidad-COALESCE(facturado.facturacantidad,0)) as cantrempendiente,remisiones.preciounitario,remisiones.id_op,remisiones.clavearticulo,articulos.articulo,articulos.kg,articulos.m2,remisiones.id_clientes,clientes.nombre,COALESCE(facturado.facturacantidad,0) as facturacantidad,COALESCE(facturado.facturaimporte,0) as facturaimporte FROM ((remisiones LEFT JOIN clientes ON remisiones.id_clientes=clientes.id_clientes)  LEFT JOIN articulos ON remisiones.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT facturas_detalle.remision,sum(facturas_detalle.cantidad) as facturacantidad,sum(facturas_detalle.cantidad*facturas_detalle.preciounitario) as facturaimporte FROM facturas_detalle WHERE (facturas_detalle.estatus='1') GROUP BY facturas_detalle.remision) as facturado ON remisiones.remision=facturado.remision WHERE (remisiones.estatus<>'Can' AND remisiones.remision='" + remision + "' AND remisiones.facturado='false') ORDER BY remisiones.fechahora;";
                            ResultSet remision_info = conexion.consulta(senSQL, conn);
                            if (remision_info.next()) {
                                clave_cliente = remision_info.getString("id_clientes"); //Variable de encabezado
                                pu = remision_info.getDouble("preciounitario");
                                id_op = remision_info.getString("id_op");
                                clave_articulo = remision_info.getString("clavearticulo");
                                descripcion_articulo = remision_info.getString("articulo");
                            }
                            if (remision_info != null) {
                                remision_info.close();
                            }

                            //CalculaSubtotales
                            Double subtotal = cantidad * pu;
                            subtotal_general += subtotal;

                            //Guarda la partida
                            senSQL = "INSERT INTO facturas_detalle(factura_serie, estatus, factura, serie, remision, id_op, clavearticulo, descripcion, cantidad, um, preciounitario, subtotal) VALUES ('" + factura_serie + "', '1', '" + folio + "', '" + serie + "', '" + remision + "', '" + id_op + "', '" + clave_articulo + "', '" + descripcion_articulo + "', '" + cantidad + "', '" + um + "', '" + pu + "', '" + subtotal + "');";
                            conexion.modificamov_p(senSQL, conn, valor_privilegio);

                            //Actualiza remision como facturada si esta completa
                            senSQL = "SELECT remisiones.remision,remisiones.fechahora,remisiones.um,remisiones.cantidad,(remisiones.cantidad-COALESCE(facturado.facturacantidad,0)) as cantrempendiente,remisiones.preciounitario,remisiones.id_op,remisiones.clavearticulo,articulos.articulo,articulos.kg,articulos.m2,clientes.nombre,COALESCE(facturado.facturacantidad,0) as facturacantidad,COALESCE(facturado.facturaimporte,0) as facturaimporte FROM ((remisiones LEFT JOIN clientes ON remisiones.id_clientes=clientes.id_clientes)  LEFT JOIN articulos ON remisiones.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT facturas_detalle.remision,sum(facturas_detalle.cantidad) as facturacantidad,sum(facturas_detalle.cantidad*facturas_detalle.preciounitario) as facturaimporte FROM facturas_detalle WHERE (facturas_detalle.estatus='1') GROUP BY facturas_detalle.remision) as facturado ON remisiones.remision=facturado.remision WHERE (remisiones.estatus<>'Can' AND remisiones.remision='" + remision + "');";
                            remision_info = conexion.consulta(senSQL, conn);
                            if (remision_info.next()) {
                                Double cantidadporrem = remision_info.getDouble("cantrempendiente");
                                if (cantidadporrem <= 0.0) {
                                    senSQL = "UPDATE remisiones SET facturado='TRUE' WHERE remision='" + remision + "';";
                                    conexion.modificamov_p(senSQL, conn, valor_privilegio);
                                }
                            }
                            if (remision_info != null) {
                                remision_info.close();
                            }
                        }
                    }//FIN Detalles de factura

                    //Partida detalle adicional por fletes
                    if (fletes > 0.0) {
                        senSQL = "INSERT INTO facturas_detalle(factura_serie, estatus, factura, serie, remision, id_op, clavearticulo, descripcion, cantidad, um, preciounitario, subtotal) VALUES ('" + factura_serie + "', '1', '" + folio + "', '" + serie + "', '0001', '0', 'FLT', 'FLETE FECHA " + fechainsertar.format(new Date()) + "', '1', 'ser','" + fletes + "','" + fletes + "');";
                        conexion.modificamov_p(senSQL, conn, valor_privilegio);
                    }

                    //Variables cabecera de factura
                    int diasc = 0;

                    //Datos del cliente
                    senSQL = "SELECT clientes.* FROM clientes WHERE id_clientes='" + clave_cliente + "' ";
                    ResultSet customer_info = conexion.consulta(senSQL, conn);
                    if (customer_info.next()) {
                        diasc = customer_info.getInt("dias");
                    }


                    //Variables cabecera de factura
                    Date fechacreado = fechahora_importar.parse(fecha); //Fecha factura
                    if (diasc > 0) {
                        calendarvencimiento.add(Calendar.DATE, diasc); //Fecha vencimiento
                    }
                    Date fechaven = calendarvencimiento.getTime(); //Fecha vencimiento
                    int id_folio = 6; //Creado para los folios 1A que seran del SAE y para el ENNOVI 1F
                    String oc = ""; //Este lo obtenemos de todas las op de las remisiones
                    int id_lugar = 1; //Lugar de emision
                    int id_moneda = 1; //Es para MXN pero hay que revisar si todo sera en pesos
                    Double valormoneda = 1.0;
                    String moneda = "PESOS";
                    String terminacion = "M.N.";
                    senSQL = "SELECT * FROM monedas WHERE id_moneda='" + id_moneda + "'";
                    ResultSet moneda_info = conexion.consulta(senSQL, conn);
                    if (moneda_info.next()) { //Actualiza informacion de moneda
                        valormoneda = moneda_info.getDouble("valor");
                        moneda = moneda_info.getString("descripcion");
                        terminacion = moneda_info.getString("terminacion");
                    }
                    int id_impuestos = 1; // Esta la tasa de impuestos normal de IVA
                    Double tasaiva = 0.0;
                    Double tasaretiva = 0.0;
                    Double tasaretisr = 0.0;
                    senSQL = "SELECT * FROM impuestos WHERE id_impuestos='" + id_impuestos + "'";
                    ResultSet impuesto_info = conexion.consulta(senSQL, conn);
                    if (impuesto_info.next()) { //Actualiza informacion de impuestos
                        tasaiva = impuesto_info.getDouble("iva");
                        tasaretiva = impuesto_info.getDouble("ivaretenido");
                        tasaretisr = impuesto_info.getDouble("isrretenido");
                    }
                    String notas = ""; //Notas de la factura
                    int id_formapago = 7; // Lo dejamos como no identificado
                    String ctapago = ""; // Cuenta de pago vacio


                    //Calculos finales
                    Double importe = subtotal_general + fletes - descuento;
                    iva = (tasaiva / 100) * importe;
                    retiva = (tasaretiva / 100) * importe;
                    retisr = (tasaretisr / 100) * importe;
                    Double total = importe + iva - retiva - retisr;
                    //Importe con letra
                    String total_str = fijo2decimales.format(total);
                    int dotPos = total_str.lastIndexOf(".") + 1;
                    String partedecimal = total_str.substring(dotPos);
                    String parteentero = total_str.substring(0, (dotPos - 1));
                    String numeroletra = "(" + new numerosletras().convertirLetras(Integer.parseInt(parteentero)) + " " + moneda + " " + partedecimal + "/100 " + terminacion + ")";
                    if (parteentero.equals("1")) {
                        numeroletra = "(UN PESO " + partedecimal + "/100 " + terminacion + ")";
                    }
                    numeroletra = numeroletra.toUpperCase();

                    //Guarda registros
                    senSQL = "INSERT INTO facturas(factura_serie, factura, serie, fecha, estatus, id_folios, id_moneda, id_clientes, id_lugaremision, id_impuestos, formadepago, ordencompra, valortipocambio, subtotal, descuento, fletes, iva, ieps, ivaretenido, isrretenido, total, importeletra, pagada, usuario, observaciones,id_formapago,cuentapago) VALUES ('"
                            + factura_serie + "', '" + folio + "', '" + serie + "', '" + fechainsertarhora.format(fechacreado) + "', '1', '" + id_folio + "', '" + id_moneda + "', '" + clave_cliente + "', '" + id_lugar + "', '" + id_impuestos + "', 'Pago en una sola exhibición', '" + oc + "', '" + valormoneda + "', '" + fijo2decimales.format(importe) + "', '" + fijo2decimales.format(descuento) + "', '" + fijo2decimales.format(fletes) + "', '" + fijo2decimales.format(iva) + "', '0', '" + fijo2decimales.format(retiva) + "', '" + fijo2decimales.format(retisr) + "', '" + fijo2decimales.format(total) + "', '" + numeroletra + "', 'FALSE', '" + user + "', '" + notas + "', '" + id_formapago + "', '" + ctapago + "');";
                    conexion.modificamov_p(senSQL, conn, valor_privilegio);
                    senSQL = "INSERT INTO docxcob(factura_serie, fechaemision, fechavencimiento, importe, estatus, id_clientes) VALUES ('"
                            + factura_serie + "', '" + fechainsertarhora.format(fechacreado) + "', '" + fechainsertarhora.format(fechaven) + "', '" + fijo2decimales.format(total) + "', 'Act', '" + clave_cliente + "');";
                    conexion.modificamov_p(senSQL, conn, valor_privilegio);

                    //Actualiza folios ultimo registro
                    senSQL = "UPDATE folios SET folioactual='" + (Integer.parseInt(folio) + 1) + "' WHERE id_folio='" + id_folio + "';";
                    conexion.modificamov_p(senSQL, conn, valor_privilegio);

                    /************************************* FIN Creamos ultima fila **************************************/


                    JOptionPane.showMessageDialog(this, "IMPORTACION DE DATOS CORRECTAMENTE", " L I S T O !!!!!", JOptionPane.INFORMATION_MESSAGE);

                    //Cargar tabla nuevamente
                    limpiatabla();

                } //Final de si valida

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        Tabladatos = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        noregistros = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        btn_importar = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btn_procesar = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btn_excel = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jPanel2 = new javax.swing.JPanel();
        buscar = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(sae_imp_remisiones_facturadas.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setFrameIcon(resourceMap.getIcon("Form.frameIcon")); // NOI18N
        setName("Form"); // NOI18N
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        Tabladatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sel","Serie", "Folio", "Fecha", "Remision", "Cantidad", "UM","Clave Cliente", "Clave Articulo", "Precio Unitario", "Total Partida"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true,false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tabladatos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tabladatos.setName("Tabladatos"); // NOI18N
        Tabladatos.setRowHeight(22);
        Tabladatos.getTableHeader().setReorderingAllowed(false);
        Tabladatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabladatosMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                TabladatosMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(Tabladatos);

        jPanel1.setName("jPanel1"); // NOI18N

        noregistros.setName("noregistros"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(noregistros)
                .addContainerGap(762, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(noregistros)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        jSeparator4.setName("jSeparator4"); // NOI18N
        jToolBar1.add(jSeparator4);

        btn_importar.setIcon(resourceMap.getIcon("btn_importar.icon")); // NOI18N
        btn_importar.setText(resourceMap.getString("btn_importar.text")); // NOI18N
        btn_importar.setFocusable(false);
        btn_importar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_importar.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btn_importar.setMaximumSize(new java.awt.Dimension(70, 48));
        btn_importar.setMinimumSize(new java.awt.Dimension(70, 48));
        btn_importar.setName("btn_importar"); // NOI18N
        btn_importar.setPreferredSize(new java.awt.Dimension(70, 48));
        btn_importar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_importar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_importarActionPerformed(evt);
            }
        });
        jToolBar1.add(btn_importar);

        jSeparator1.setName("jSeparator1"); // NOI18N
        jToolBar1.add(jSeparator1);

        btn_procesar.setIcon(resourceMap.getIcon("btn_procesar.icon")); // NOI18N
        btn_procesar.setText(resourceMap.getString("btn_procesar.text")); // NOI18N
        btn_procesar.setToolTipText(resourceMap.getString("btn_procesar.toolTipText")); // NOI18N
        btn_procesar.setFocusable(false);
        btn_procesar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_procesar.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btn_procesar.setMaximumSize(new java.awt.Dimension(70, 48));
        btn_procesar.setMinimumSize(new java.awt.Dimension(70, 48));
        btn_procesar.setName("btn_procesar"); // NOI18N
        btn_procesar.setPreferredSize(new java.awt.Dimension(70, 48));
        btn_procesar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_procesar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_procesarActionPerformed(evt);
            }
        });
        jToolBar1.add(btn_procesar);

        jSeparator3.setName("jSeparator3"); // NOI18N
        jToolBar1.add(jSeparator3);

        btn_excel.setIcon(resourceMap.getIcon("btn_excel.icon")); // NOI18N
        btn_excel.setText(resourceMap.getString("btn_excel.text")); // NOI18N
        btn_excel.setFocusable(false);
        btn_excel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_excel.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btn_excel.setMaximumSize(new java.awt.Dimension(70, 48));
        btn_excel.setMinimumSize(new java.awt.Dimension(70, 48));
        btn_excel.setName("btn_excel"); // NOI18N
        btn_excel.setPreferredSize(new java.awt.Dimension(70, 48));
        btn_excel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_excel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_excelActionPerformed(evt);
            }
        });
        jToolBar1.add(btn_excel);

        jSeparator2.setName("jSeparator2"); // NOI18N
        jToolBar1.add(jSeparator2);

        jPanel2.setMaximumSize(new java.awt.Dimension(32767, 48));
        jPanel2.setMinimumSize(new java.awt.Dimension(100, 48));
        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(556, 48));

        buscar.setName("buscar"); // NOI18N
        buscar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscarFocusGained(evt);
            }
        });
        buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscarKeyReleased(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setIcon(resourceMap.getIcon("jLabel2.icon")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(277, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jToolBar1.add(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 792, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 792, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formWindowClosing

    private void TabladatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabladatosMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            
        }
    }//GEN-LAST:event_TabladatosMouseClicked

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formInternalFrameClosing

    private void TabladatosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabladatosMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_TabladatosMouseReleased

    private void btn_importarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_importarActionPerformed
        // TODO add your handling code here:
        busca_fechas busca_fechas = new busca_fechas(null, true);
        busca_fechas.setLocationRelativeTo(this);
        busca_fechas.setVisible(true);
        String estado = busca_fechas.getEstado();
        /**obtiene si se pulso aceptar o calcelar en busca_fechas*/
        if (estado.equals("cancelar")) {
        } else {
            limpiatabla();
            datos(fechainsertar.format(busca_fechas.getFechaini()),fechainsertar.format(busca_fechas.getFechafin()));

        }
        busca_fechas = null;
}//GEN-LAST:event_btn_importarActionPerformed

    private void btn_excelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_excelActionPerformed
        // TODO add your handling code here:
        File rutaarchivo = new File(System.getProperty("user.home") + "/remisiones_fact.xls");
        try {
            //Se crea el libro Excel
            WritableWorkbook workbook = Workbook.createWorkbook(rutaarchivo);
            //Se crea una nueva hoja dentro del libro
            WritableSheet sheet = workbook.createSheet("Datos", 0);
            //formatos de texto
            WritableFont arial12b = new WritableFont(WritableFont.createFont("Arial"), 12, WritableFont.BOLD, false);
            WritableFont arial10b = new WritableFont(WritableFont.createFont("Arial"), 10, WritableFont.BOLD, false);
            WritableFont arial9 = new WritableFont(WritableFont.createFont("Arial"), 9, WritableFont.NO_BOLD, false);

            WritableCellFormat arial10fsupertitulo = new WritableCellFormat(arial12b);

            WritableCellFormat arial10ftitulo = new WritableCellFormat(arial10b);
            arial10ftitulo.setBackground(Colour.LIME);//.SKY_BLUE
            arial10ftitulo.setAlignment(Alignment.CENTRE);
            arial10ftitulo.setVerticalAlignment(VerticalAlignment.CENTRE);

            WritableCellFormat arial10fdetalle = new WritableCellFormat(arial9);

            int filainicial = 0;

            //titulo del documento
            sheet.addCell(new jxl.write.Label(0, filainicial, "" + this.getTitle(), arial10fsupertitulo));

            //escribimos los datos en las celdas (columnas,filas)
            filainicial++;//incrementa las filas
            for (int j = 0; j < (Tabladatos.getColumnCount()); j = j + 1) {
                String titu = "" + Tabladatos.getColumnName(j);
                int dotPos = titu.lastIndexOf(">") + 1;//le quita el html de los titulos
                if (titu.contains("<html>")) {
                    titu = titu.substring(dotPos);
                }
                sheet.addCell(new jxl.write.Label(j, filainicial, titu, arial10ftitulo));
                sheet.setColumnView(j, ((Tabladatos.getColumnModel().getColumn(j).getPreferredWidth()) / 6));
            }

            filainicial++;//incrementa las filas
            for (int i = 0; i < (Tabladatos.getRowCount()); i = i + 1) {
                for (int j = 0; j < (Tabladatos.getColumnCount()); j = j + 1) {
                    if (Tabladatos.getValueAt(i, j) != null) {
                        if (Tabladatos.getValueAt(i, j) instanceof String) {
                            String dato = (String) Tabladatos.getValueAt(i, j);
                            int dotPos = dato.lastIndexOf(">") + 1;//le quita el html de los titulos
                            if (dato.contains("<html>")) {
                                dato = dato.substring(dotPos);
                            }
                            sheet.addCell(new jxl.write.Label(j, i + filainicial, dato, arial10fdetalle));
                        } else if (Tabladatos.getValueAt(i, j) instanceof java.lang.Number) {
                            sheet.addCell(new jxl.write.Number(j, i + filainicial, Double.parseDouble(Tabladatos.getValueAt(i, j).toString()), arial10fdetalle));
                        } else if (Tabladatos.getValueAt(i, j) instanceof java.util.Date) {
                            sheet.addCell(new jxl.write.DateTime(j, i + filainicial, (java.util.Date) Tabladatos.getValueAt(i, j), jxl.write.DateTime.GMT));
                        } else {
                            sheet.addCell(new jxl.write.Boolean(j, i + filainicial, (java.lang.Boolean) Tabladatos.getValueAt(i, j), arial10fdetalle));
                        }
                    }
                }
            }
            /**fin de revisar los campos vacios*/
            //Escribimos los resultados al fichero Excel
            workbook.write();
            workbook.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "EL ARCHIVO ESTA ABIERTO O NO SE PUEDE CREAR" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        } catch (WriteException exe) {
            JOptionPane.showMessageDialog(this, "ERROR AL EXPORTAR DATOS" + exe, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }


        //abrir el documento creado
        try {
            Desktop.getDesktop().open(rutaarchivo);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "NO SE PUEDE ABRIR EL ARCHIVO\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }
}//GEN-LAST:event_btn_excelActionPerformed

    private void buscarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarFocusGained
        // TODO add your handling code here:
        buscar.selectAll();
}//GEN-LAST:event_buscarFocusGained

    private void buscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarKeyReleased
        // TODO add your handling code here:
        if (buscar.getText().equals("")) {
            Tabladatos.setRowSorter(null);
            buscar.setText("");
            Tabladatos.setAutoCreateRowSorter(true);
            limpiatabla();
            datos("","");
        } else {
            TableRowSorter orden = new TableRowSorter(modelot1);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)" + buscar.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tabladatos.setRowSorter(orden);
            //numero de registros en la consulta
            sumaregistros();//funcion que contabiliza los registros
        }
}//GEN-LAST:event_buscarKeyReleased

    private void btn_procesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_procesarActionPerformed
        // TODO add your handling code here:
        importar_facturas();
    }//GEN-LAST:event_btn_procesarActionPerformed
    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabladatos;
    private javax.swing.JButton btn_excel;
    private javax.swing.JButton btn_importar;
    private javax.swing.JButton btn_procesar;
    private javax.swing.JTextField buscar;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel noregistros;
    // End of variables declaration//GEN-END:variables

}
