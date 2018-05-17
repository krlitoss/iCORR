/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * datos_usuarios.java
 *
 * Created on 21/01/2010, 10:56:30 PM
 */

package conta;

import java.awt.Component;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author IVONNE
 */
public final class datos_pagos extends javax.swing.JDialog {
    DefaultTableModel modelot1=null;
    DecimalFormat fijo0decimales=new DecimalFormat("######0");
    DecimalFormat fijo2decimales=new DecimalFormat("######0.00");
    DecimalFormat fijo5decimales=new DecimalFormat("######0.00000");
    DecimalFormat fijo4decimales=new DecimalFormat("######0.0000");
    DecimalFormat moneda2decimales=new DecimalFormat("$ #,###,##0.00");
    DecimalFormat estandarentero=new DecimalFormat("#,###,##0");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechamediana = new SimpleDateFormat("dd-MMM-yyyy");
    SimpleDateFormat fechacorta = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Connection connj;
    ResultSet rs0=null,rs1=null,rs2=null;
    Double valoriva=0.0,valorieps=0.0;
    int banderabuscaart=0;
    String terminacion="M.N.";
    int cambia=1;
    String valor_privilegio="1";

    /** Creates new form datos_usuarios */
    public datos_pagos(java.awt.Frame parent, boolean modal,Connection conn,String clavemodifica,String valor_privilegio_t) {
        super(parent, modal);
        initComponents();
        connj=conn;
        valor_privilegio=valor_privilegio_t;
        modelot1=(DefaultTableModel) Tabladatos.getModel();
        Tabladatos.setModel(modelot1);
        tipoabono();
        ajusteTabladatos();
        fecha.setDate(new Date());
        consultamodifica(clavemodifica);
        //cambia la tecla enter por un tab
        java.util.HashSet conj = new java.util.HashSet(this.getFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        conj.add(java.awt.AWTKeyStroke.getAWTKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, conj);

        modelot1.addTableModelListener(new TableModelListener() {
          public void tableChanged(TableModelEvent te) {
                try{
                    int f = te.getLastRow();//Fila modificada
                    int c = te.getColumn();//columna
                    TableModel model = (TableModel) te.getSource();//Modelo de la tabla
                    if (c == 0) { //obtiene los datos de la OP
                        String clavefac=""+model.getValueAt(f, c);
                        if(!clavefac.equals("") && !clavefac.equals("null")){
                            rs0=null;
                            try{
                                String senSQL="SELECT docxcob.factura_serie,clientes.id_clientes,clientes.nombre,docxcob.fechaemision,docxcob.fechavencimiento,docxcob.importe,(docxcob.importe-COALESCE(notascreditorealizadas.importenotacredito,0)-COALESCE(pagosrealizados.importepago,0)) as saldo,COALESCE(notascreditorealizadas.importenotacredito,0) as importenotacredito,COALESCE(pagosrealizados.importepago,0) as importepago FROM ((docxcob LEFT JOIN clientes ON docxcob.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT notas_credito_detalle.factura_serie,sum(notas_credito_detalle.importe) as importenotacredito FROM notas_credito_detalle WHERE (notas_credito_detalle.estatus='1') GROUP BY notas_credito_detalle.factura_serie) as notascreditorealizadas ON docxcob.factura_serie=notascreditorealizadas.factura_serie ) LEFT JOIN (SELECT pagos_detalle.factura_serie,sum(pagos_detalle.importe+pagos_detalle.importe_factoraje) as importepago FROM pagos_detalle LEFT JOIN pagos ON pagos_detalle.id_pagos =pagos.id_pagos WHERE (pagos_detalle.estatus<>'Can') GROUP BY pagos_detalle.factura_serie) as pagosrealizados ON docxcob.factura_serie=pagosrealizados.factura_serie WHERE (docxcob.estatus<>'Can' AND docxcob.factura_serie='"+clavefac.toUpperCase()+"' AND docxcob.id_clientes='"+clavecliente.getText()+"' AND (docxcob.importe-COALESCE(notascreditorealizadas.importenotacredito,0)-COALESCE(pagosrealizados.importepago,0))>0)";
                                rs0=conexion.consulta(senSQL,connj);
                                if(rs0.next()){
                                    Double saldo=rs0.getDouble("saldo");
                                    cambia=0;
                                    model.setValueAt(saldo, f, 6);
                                    cambia=1;
                                    model.setValueAt(fechamediana.format(rs0.getDate("fechavencimiento")), f, 1);
                                    model.setValueAt(rs0.getString("nombre"), f, 2);
                                    model.setValueAt(saldo, f, 3);
                                    model.setValueAt(0.0, f, 4);
                                    model.setValueAt(saldo, f, 5);
                                }
                                else{
                                    JOptionPane.showMessageDialog(null,"LA NUMERO DE FACTURA NO EXISTE O YA FUE COBRADA","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                                    model.setValueAt(0.0, f, 4);
                                    model.setValueAt(0.0, f, 5);
                                    model.setValueAt(0.0, f, 6);
                                    model.setValueAt("", f, 1);
                                    model.setValueAt("", f, 2);
                                    model.setValueAt(0.0, f, 3);
                                    model.setValueAt("", f, 0);
                                }
                                if(rs0!=null) {  rs0.close(); }
                            } catch (Exception ex) {JOptionPane.showMessageDialog(null,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                        }
                        sumatoria();
                    }
                    if (c == 4 && cambia==1) {
                        String imp_f=""+model.getValueAt(f, c);
                        String clavefac=""+model.getValueAt(f, 0);
                        if(!imp_f.equals("") && !imp_f.equals("null") && !clavefac.equals("") && !clavefac.equals("null")){
                            Double imp_factoraje=(Double)model.getValueAt(f, c);
                            if(imp_factoraje>0.0){
                                    rs1=null;
                                    try{
                                        String senSQL="SELECT docxcob.factura_serie,clientes.id_clientes,clientes.nombre,docxcob.fechaemision,docxcob.fechavencimiento,docxcob.importe,(docxcob.importe-COALESCE(notascreditorealizadas.importenotacredito,0)-COALESCE(pagosrealizados.importepago,0)) as saldo,COALESCE(notascreditorealizadas.importenotacredito,0) as importenotacredito,COALESCE(pagosrealizados.importepago,0) as importepago FROM ((docxcob LEFT JOIN clientes ON docxcob.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT notas_credito_detalle.factura_serie,sum(notas_credito_detalle.importe) as importenotacredito FROM notas_credito_detalle WHERE (notas_credito_detalle.estatus='1') GROUP BY notas_credito_detalle.factura_serie) as notascreditorealizadas ON docxcob.factura_serie=notascreditorealizadas.factura_serie ) LEFT JOIN (SELECT pagos_detalle.factura_serie,sum(pagos_detalle.importe+pagos_detalle.importe_factoraje) as importepago FROM pagos_detalle LEFT JOIN pagos ON pagos_detalle.id_pagos =pagos.id_pagos WHERE (pagos_detalle.estatus<>'Can') GROUP BY pagos_detalle.factura_serie) as pagosrealizados ON docxcob.factura_serie=pagosrealizados.factura_serie WHERE (docxcob.estatus<>'Can' AND docxcob.factura_serie='"+clavefac.toUpperCase()+"' AND (docxcob.importe-COALESCE(notascreditorealizadas.importenotacredito,0)-COALESCE(pagosrealizados.importepago,0))>0)";
                                        rs1=conexion.consulta(senSQL,connj);
                                        if(rs1.next()){
                                            Double saldo=rs1.getDouble("saldo")-imp_factoraje;
                                            model.setValueAt(saldo, f, 5);
                                            model.setValueAt(saldo, f, 6);

                                        }else{
                                            model.setValueAt(0.0, f, 5);
                                            model.setValueAt(0.0, f, 6);
                                        }
                                        if(rs1!=null) {  rs1.close(); }
                                    } catch (Exception ex) {JOptionPane.showMessageDialog(null,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

                            }
                        }
                        sumatoria();
                    }
                    if (c == 6 && cambia==1) {
                        String imp=""+model.getValueAt(f, c);
                        String clavefac=""+model.getValueAt(f, 0);
                        if(!imp.equals("") && !imp.equals("null") && !clavefac.equals("") && !clavefac.equals("null")){
                            Double impfin=(Double)model.getValueAt(f, c);
                            Double imp_factoraje=(Double)model.getValueAt(f, 4);
                            if(impfin>0.0){
                                    rs1=null;
                                    try{
                                        String senSQL="SELECT docxcob.factura_serie,clientes.id_clientes,clientes.nombre,docxcob.fechaemision,docxcob.fechavencimiento,docxcob.importe,(docxcob.importe-COALESCE(notascreditorealizadas.importenotacredito,0)-COALESCE(pagosrealizados.importepago,0)) as saldo,COALESCE(notascreditorealizadas.importenotacredito,0) as importenotacredito,COALESCE(pagosrealizados.importepago,0) as importepago FROM ((docxcob LEFT JOIN clientes ON docxcob.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT notas_credito_detalle.factura_serie,sum(notas_credito_detalle.importe) as importenotacredito FROM notas_credito_detalle WHERE (notas_credito_detalle.estatus='1') GROUP BY notas_credito_detalle.factura_serie) as notascreditorealizadas ON docxcob.factura_serie=notascreditorealizadas.factura_serie ) LEFT JOIN (SELECT pagos_detalle.factura_serie,sum(pagos_detalle.importe+pagos_detalle.importe_factoraje) as importepago FROM pagos_detalle LEFT JOIN pagos ON pagos_detalle.id_pagos =pagos.id_pagos WHERE (pagos_detalle.estatus<>'Can') GROUP BY pagos_detalle.factura_serie) as pagosrealizados ON docxcob.factura_serie=pagosrealizados.factura_serie WHERE (docxcob.estatus<>'Can' AND docxcob.factura_serie='"+clavefac.toUpperCase()+"' AND (docxcob.importe-COALESCE(notascreditorealizadas.importenotacredito,0)-COALESCE(pagosrealizados.importepago,0))>0)";
                                        rs1=conexion.consulta(senSQL,connj);
                                        if(rs1.next()){
                                            Double saldo=rs1.getDouble("saldo")-imp_factoraje;
                                            if(impfin>saldo){
                                                JOptionPane.showMessageDialog(null,"EL IMPORTE ES MAYOR AL SALDO","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                                                model.setValueAt(0.0, f, 6);
                                            }

                                        }else{
                                            model.setValueAt(0.0, f, 6);
                                        }
                                        if(rs1!=null) {  rs1.close(); }
                                    } catch (Exception ex) {JOptionPane.showMessageDialog(null,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                                    
                            }
                        }
                        sumatoria();
                    }
                    

                }catch(Exception e){
                    JOptionPane.showMessageDialog(null, "NO SE PUEDE CALCULAR EL IMPORTE"+e, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                }

          }
        });

    }


    public void salir(){
        if(connj!=null){
            connj=null;
            System.out.println("Conexion en null...............");
        }
        dispose();
        this.setVisible(false);
    }
    public void ajusteTabladatos() {
        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(200);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(70);
    }

    public void sumatoria(){
        int filas=Tabladatos.getRowCount();
        if(filas<=0){

        }else{
            Double sumaimportes=0.0;
            Double sumaimportes_fac=0.0;
            for(int i=0;i<=(filas-1);i=i+1){
                Double valimporte=(Double) Tabladatos.getValueAt(i, 6);///obtiene el importe
                sumaimportes=sumaimportes+valimporte;
                Double valimportef=(Double) Tabladatos.getValueAt(i, 4);///obtiene el importe
                sumaimportes_fac=sumaimportes_fac+valimportef;
            }
            importe.setText(fijo2decimales.format(sumaimportes));
            importe_factorajet.setText(fijo2decimales.format(sumaimportes_fac));
        }
    }
    public void tipoabono(){
        rs0=null;
        try{
            String senSQL="SELECT * FROM tipo_abono";
            rs0=conexion.consulta(senSQL,connj);
            while(rs0.next()){
                tipopago.addItem(rs0.getString("descripcion"));
            }
            if(rs0!=null) {
                rs0.close();
            }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
    }
    public void consultamodifica(String clavemodifica){
        if(clavemodifica.equals("")){
            /**si la clave esta vacia significa que es un registro nuevo*/
        }else{
            id.setEditable(false);
            id.setFocusable(false);
            btnaceptar.setEnabled(false);
            btneliminar.setEnabled(true);
            rs0=null;
            try{
                String senSQL="SELECT pagos.id_pagos,pagos.id_pagos_anticipos,pagos.documento,pagos.fechapago,pagos.id_clientes,pagos.tipo_abono,pagos.importe as importetotal,pagos.id_cuenta,clientes.nombre,cuentasbancarias.numerocuenta,pagos_detalle.factura_serie,pagos_detalle.importe,pagos_detalle.importe_factoraje,docxcob.fechavencimiento FROM ((pagos LEFT JOIN clientes ON pagos.id_clientes=clientes.id_clientes) LEFT JOIN cuentasbancarias ON pagos.id_cuenta=cuentasbancarias.id_cuenta) INNER JOIN (pagos_detalle LEFT JOIN docxcob ON pagos_detalle.factura_serie=docxcob.factura_serie) ON pagos.id_pagos=pagos_detalle.id_pagos WHERE (pagos.estatus='Act' AND pagos.id_pagos='"+clavemodifica+"') ORDER BY pagos.fechapago;";
                rs0=conexion.consulta(senSQL,connj);
                while(rs0.next()){
                    id.setText(rs0.getString("id_pagos"));
                    documento.setText(rs0.getString("documento"));
                    tipopago.setSelectedItem(rs0.getString("tipo_abono"));
                    fecha.setDate(rs0.getDate("fechapago"));
                    clavecliente.setText(rs0.getString("id_clientes"));
                    String nombrec=rs0.getString("nombre");
                    nombrecliente.setText(nombrec);
                    importeini.setText(fijo2decimales.format(rs0.getDouble("importetotal")));
                    cuenta.setText(rs0.getString("id_cuenta"));
                    descripcion.setText(rs0.getString("numerocuenta"));
                    id_anticipo.setText(rs0.getString("id_pagos_anticipos"));
                    Object datos[]={rs0.getString("factura_serie"),fechamediana.format(rs0.getDate("fechavencimiento")),nombrec,0.0,rs0.getDouble("importe_factoraje"),0.0,rs0.getDouble("importe")};
                    modelot1.addRow(datos);
                    sumatoria();
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
    }
    public void actualiza_anticipo(String id_an){
        ////////////////actualiza la orden de produccion
        if(id_anticipo.getText().equals("0") || id_anticipo.getText().equals("")){

        }else{

            rs2=null;
            try{
                String senSQL="SELECT pagos_anticipos.*,clientes.nombre,cuentasbancarias.numerocuenta,COALESCE(pagos_normal.importe_pagos,0) as importe_pagos FROM ((pagos_anticipos LEFT JOIN (SELECT pagos.id_pagos_anticipos,sum(pagos.importe) as importe_pagos FROM pagos WHERE pagos.estatus<>'Can' GROUP BY id_pagos_anticipos) as pagos_normal ON pagos_anticipos.id_pagos_anticipos=pagos_normal.id_pagos_anticipos) LEFT JOIN clientes ON pagos_anticipos.id_clientes=clientes.id_clientes) LEFT JOIN cuentasbancarias ON pagos_anticipos.id_cuenta=cuentasbancarias.id_cuenta WHERE (pagos_anticipos.estatus='Act' AND pagos_anticipos.id_pagos_anticipos='"+id_an+"') ORDER BY pagos_anticipos.fechapago;";
                rs2=conexion.consulta(senSQL,connj);
                if(rs2.next()){
                    Double saldoactual=rs2.getDouble("importe")-rs2.getDouble("importe_pagos");
                    if(saldoactual<=0.0){
                        String senSQLmov = "UPDATE pagos_anticipos SET estatus='Ter' WHERE id_pagos_anticipos='" + id_an + "';";
                        conexion.modificamov_p(senSQLmov, connj,valor_privilegio);
                    }else{
                        String senSQLmov = "UPDATE pagos_anticipos SET estatus='Act' WHERE id_pagos_anticipos='" + id_an + "';";
                        conexion.modificamov_p(senSQLmov, connj,valor_privilegio);
                    }
                }else{
                    String senSQLmov = "UPDATE pagos_anticipos SET estatus='Act' WHERE id_pagos_anticipos='" + id_an + "';";
                    conexion.modificamov_p(senSQLmov, connj,valor_privilegio);
                }
                if(rs2!=null) {  rs2.close(); }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

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

        jPanel1 = new javax.swing.JPanel();
        btnaceptar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        btneliminar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        fecha = new com.toedter.calendar.JDateChooser();
        id = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tipopago = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        documento = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        clavecliente = new javax.swing.JTextField();
        btnclientes = new javax.swing.JButton();
        nombrecliente = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        importeini = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        cuenta = new javax.swing.JTextField();
        btncuenta = new javax.swing.JButton();
        descripcion = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        id_anticipo = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabladatos = new javax.swing.JTable();
        btnremover = new javax.swing.JButton();
        btnadd = new javax.swing.JButton();
        lbiva = new javax.swing.JLabel();
        importe = new javax.swing.JFormattedTextField();
        importe_factorajet = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_pagos.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setName("jPanel1"); // NOI18N

        btnaceptar.setIcon(resourceMap.getIcon("btnaceptar.icon")); // NOI18N
        btnaceptar.setText(resourceMap.getString("btnaceptar.text")); // NOI18N
        btnaceptar.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btnaceptar.setName("btnaceptar"); // NOI18N
        btnaceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaceptarActionPerformed(evt);
            }
        });

        btncancelar.setIcon(resourceMap.getIcon("btncancelar.icon")); // NOI18N
        btncancelar.setText(resourceMap.getString("btncancelar.text")); // NOI18N
        btncancelar.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btncancelar.setName("btncancelar"); // NOI18N
        btncancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncancelarActionPerformed(evt);
            }
        });

        btneliminar.setIcon(resourceMap.getIcon("btneliminar.icon")); // NOI18N
        btneliminar.setText(resourceMap.getString("btneliminar.text")); // NOI18N
        btneliminar.setEnabled(false);
        btneliminar.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btneliminar.setMaximumSize(new java.awt.Dimension(73, 31));
        btneliminar.setMinimumSize(new java.awt.Dimension(73, 31));
        btneliminar.setName("btneliminar"); // NOI18N
        btneliminar.setPreferredSize(new java.awt.Dimension(73, 31));
        btneliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(177, 177, 177)
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btneliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(187, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btneliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setFont(resourceMap.getFont("jLabel6.font")); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jPanel5.setName("jPanel5"); // NOI18N

        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        fecha.setDateFormatString(resourceMap.getString("fecha.dateFormatString")); // NOI18N
        fecha.setName("fecha"); // NOI18N
        fecha.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fechaFocusGained(evt);
            }
        });

        id.setEditable(false);
        id.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id.setText(resourceMap.getString("id.text")); // NOI18N
        id.setFocusable(false);
        id.setName("id"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        tipopago.setName("tipopago"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        documento.setText(resourceMap.getString("documento.text")); // NOI18N
        documento.setName("documento"); // NOI18N

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        clavecliente.setEditable(false);
        clavecliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        clavecliente.setText(resourceMap.getString("clavecliente.text")); // NOI18N
        clavecliente.setFocusable(false);
        clavecliente.setName("clavecliente"); // NOI18N

        btnclientes.setIcon(resourceMap.getIcon("btnclientes.icon")); // NOI18N
        btnclientes.setText(resourceMap.getString("btnclientes.text")); // NOI18N
        btnclientes.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btnclientes.setName("btnclientes"); // NOI18N
        btnclientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnclientesActionPerformed(evt);
            }
        });

        nombrecliente.setEditable(false);
        nombrecliente.setText(resourceMap.getString("nombrecliente.text")); // NOI18N
        nombrecliente.setFocusable(false);
        nombrecliente.setName("nombrecliente"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        importeini.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.00"))));
        importeini.setText(resourceMap.getString("importeini.text")); // NOI18N
        importeini.setName("importeini"); // NOI18N
        importeini.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                importeiniFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                importeiniFocusLost(evt);
            }
        });

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        cuenta.setEditable(false);
        cuenta.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cuenta.setText(resourceMap.getString("cuenta.text")); // NOI18N
        cuenta.setFocusable(false);
        cuenta.setName("cuenta"); // NOI18N

        btncuenta.setIcon(resourceMap.getIcon("btncuenta.icon")); // NOI18N
        btncuenta.setText(resourceMap.getString("btncuenta.text")); // NOI18N
        btncuenta.setMargin(new java.awt.Insets(2, 0, 2, 0));
        btncuenta.setName("btncuenta"); // NOI18N
        btncuenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncuentaActionPerformed(evt);
            }
        });

        descripcion.setEditable(false);
        descripcion.setText(resourceMap.getString("descripcion.text")); // NOI18N
        descripcion.setFocusable(false);
        descripcion.setName("descripcion"); // NOI18N

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        id_anticipo.setEditable(false);
        id_anticipo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id_anticipo.setText(resourceMap.getString("id_anticipo.text")); // NOI18N
        id_anticipo.setFocusable(false);
        id_anticipo.setName("id_anticipo"); // NOI18N

        jButton2.setIcon(resourceMap.getIcon("jButton2.icon")); // NOI18N
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(documento, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clavecliente, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnclientes, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(importeini, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cuenta, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btncuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tipopago, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id_anticipo, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(nombrecliente, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                    .addComponent(descripcion, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(tipopago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)
                        .addComponent(id_anticipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(documento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(fecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(clavecliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnclientes, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nombrecliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(importeini, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)
                        .addComponent(cuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btncuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        Tabladatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Factura", "Fecha Vencimiento", "Cliente", "Saldo", "Factoraje", "Saldo Nvo.", "Abono"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tabladatos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tabladatos.setColumnSelectionAllowed(true);
        Tabladatos.setName("Tabladatos"); // NOI18N
        Tabladatos.setRowHeight(22);
        Tabladatos.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        Tabladatos.getTableHeader().setReorderingAllowed(false);
        Tabladatos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TabladatosKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TabladatosKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(Tabladatos);

        btnremover.setIcon(resourceMap.getIcon("btnremover.icon")); // NOI18N
        btnremover.setText(resourceMap.getString("btnremover.text")); // NOI18N
        btnremover.setToolTipText(resourceMap.getString("btnremover.toolTipText")); // NOI18N
        btnremover.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnremover.setName("btnremover"); // NOI18N
        btnremover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnremoverActionPerformed(evt);
            }
        });

        btnadd.setIcon(resourceMap.getIcon("btnadd.icon")); // NOI18N
        btnadd.setText(resourceMap.getString("btnadd.text")); // NOI18N
        btnadd.setToolTipText(resourceMap.getString("btnadd.toolTipText")); // NOI18N
        btnadd.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnadd.setName("btnadd"); // NOI18N
        btnadd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddActionPerformed(evt);
            }
        });

        lbiva.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbiva.setText(resourceMap.getString("lbiva.text")); // NOI18N
        lbiva.setName("lbiva"); // NOI18N

        importe.setBackground(resourceMap.getColor("importe.background")); // NOI18N
        importe.setEditable(false);
        importe.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#####0.00"))));
        importe.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        importe.setText(resourceMap.getString("importe.text")); // NOI18N
        importe.setFocusable(false);
        importe.setName("importe"); // NOI18N

        importe_factorajet.setBackground(resourceMap.getColor("importe_factorajet.background")); // NOI18N
        importe_factorajet.setEditable(false);
        importe_factorajet.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#####0.00"))));
        importe_factorajet.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        importe_factorajet.setText(resourceMap.getString("importe_factorajet.text")); // NOI18N
        importe_factorajet.setName("importe_factorajet"); // NOI18N

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(importe_factorajet, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbiva, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(importe, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnadd, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnremover, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnadd, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnremover, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(importe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbiva)
                    .addComponent(importe_factorajet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:
        int filas=Tabladatos.getRowCount();
        int columnas=Tabladatos.getColumnCount();
        if(cuenta.getText().equals("")||documento.getText().equals("")||fecha.getDate()==null||clavecliente.getText().equals("")||importeini.getText().equals("0.00")||importeini.getText().equals("")||filas<=0){
            JOptionPane.showMessageDialog(this, "VERIFICA HAY CAMPOS VACIOS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            if (Tabladatos.getCellEditor() != null) {//finaliza el editor
                Tabladatos.getCellEditor().stopCellEditing();
            }
            /**revisar que no existan campos vacios*/
            int camposvacios=0;
            int importeerror=1;
            for(int i=0;i<=(filas-1);i=i+1){
                for(int j=0;j<=(columnas-1);j=j+1){
                    if(modelot1.getValueAt(i, j) == null||modelot1.getValueAt(i, j).equals("")){
                        camposvacios=1;
                    }

                }
                /**funcion que verifica que no quede en ceros la cantidad*/
                if((Double)modelot1.getValueAt(i, 6) ==0.0){
                    camposvacios=1;
                }
            }/**fin de revisar los campos vacios*/
            sumatoria();
            Double im1=Double.parseDouble(importeini.getText());
            Double im2=Double.parseDouble(importe.getText());
            //System.err.println(""+im1+"--------"+im2);
            if(im1.equals(im2)){
                importeerror=0;
            }

            if(camposvacios==1 || importeerror==1){
                String erro="LA TABLA DE DETALLE TIENE CAMPOS VACIOS\nBORRE LAS FILAS VACIAS";
                if(importeerror==1)
                    erro="LOS IMPORTES TOTALES NO SON IGUALES";
                JOptionPane.showMessageDialog(this, erro, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }else{
                
                if(id.getText().equals("")){
                    //GUARDA LA PARIDA PRINCIPAL
                    String senSQL="INSERT INTO pagos(documento, id_clientes, fecha, estatus, tipo_abono,id_cuenta, importe, fechapago,id_pagos_anticipos) VALUES ('"+documento.getText()+"', '"+clavecliente.getText()+"', '"+fechainsertarhora.format(new Date())+"', 'Act', '"+tipopago.getSelectedItem()+"', '"+cuenta.getText()+"', '"+importeini.getText()+"', '"+fechainsertar.format(fecha.getDate())+"', '"+id_anticipo.getText()+"');";
                    conexion.modifica_p(senSQL,connj,valor_privilegio);
                    int claveidmax=0;
                    rs0=null;
                    try{
                        senSQL="SELECT max(id_pagos) as pagomax FROM pagos";
                        rs0=conexion.consulta(senSQL,connj);
                        if(rs0.next()){
                            claveidmax=rs0.getInt("pagomax");
                            id.setText(""+claveidmax);
                        }
                        if(rs0!=null) {
                            rs0.close();
                        }
                    } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                    /**funcion que registra todos los movimientos de la orden de compra*/
                    for(int i=0;i<=(filas-1);i=i+1){
                        String facserie=""+modelot1.getValueAt(i, 0);
                        String senSQLmov="INSERT INTO pagos_detalle(factura_serie, importe,importe_factoraje, estatus, anticipo, fecha,id_pagos) VALUES ('"+facserie.toUpperCase()+"', '"+modelot1.getValueAt(i, 6)+"', '"+modelot1.getValueAt(i, 4)+"', 'Act', 'false', '"+fechainsertar.format(fecha.getDate())+"', '"+id.getText()+"');";
                        conexion.modificamov_p(senSQLmov,connj,valor_privilegio);
                    }
                }
                actualiza_anticipo(id_anticipo.getText());
                salir();

            }
        }
}//GEN-LAST:event_btnaceptarActionPerformed

    private void btncancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarActionPerformed
        // TODO add your handling code here:
        salir();
}//GEN-LAST:event_btncancelarActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formWindowClosing

    private void btnaddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddActionPerformed
        // TODO add your handling code here:
        int filas=Tabladatos.getRowCount();
        if(filas<70){
            if(cuenta.getText().equals("")||documento.getText().equals("")||fecha.getDate()==null||clavecliente.getText().equals("")||importeini.getText().equals("0.00")||importeini.getText().equals("")){
                JOptionPane.showMessageDialog(this, "HAY CAMPOS VACIOS LLENA LOS PRIMEROS CAMPOS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }else{
                btnclientes.setEnabled(false);
                Object datos[]={"","","",0.0,0.0,0.0,0.0};
                modelot1.addRow(datos);
            }
        }else{
            JOptionPane.showMessageDialog(this, "YA NO PUEDES AGREGAR MAS FILAS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }

}//GEN-LAST:event_btnaddActionPerformed

    private void btnremoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnremoverActionPerformed
        // TODO add your handling code here:
        int filano=Tabladatos.getSelectedRow();
        if(filano<0){
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            int respuesta=JOptionPane.showConfirmDialog(this,"ESTA SEGURO DE ELIMINAR LA FILA  "+(filano+1)+" ?????"," L I S T O !!!!!", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                modelot1.removeRow(filano);
                sumatoria();
            }else{

            }
        }
    }//GEN-LAST:event_btnremoverActionPerformed

    private void fechaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fechaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_fechaFocusGained

    private void TabladatosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TabladatosKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_ENTER)
        {
            evt.setKeyCode(java.awt.event.KeyEvent.VK_TAB);
        }
    }//GEN-LAST:event_TabladatosKeyPressed

    private void TabladatosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TabladatosKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_TabladatosKeyReleased

    private void btnclientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnclientesActionPerformed
        // TODO add your handling code here:
        busca_clientes busca_clientes = new busca_clientes(null,true,connj,"");
        busca_clientes.setLocationRelativeTo(this);
        busca_clientes.setVisible(true);
        clavecliente.setText(busca_clientes.getText());//obtiene el valor seleccionado
        busca_clientes=null;
        if(clavecliente.getText().equals("")){

        }else{
            rs0=null;
            try{
                String senSQL="SELECT * FROM clientes WHERE id_clientes='"+clavecliente.getText()+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    nombrecliente.setText(rs0.getString("nombre"));
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
    }//GEN-LAST:event_btnclientesActionPerformed

    private void btncuentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncuentaActionPerformed
        // TODO add your handling code here:
        busca_cuentas busca_cuentas = new busca_cuentas(null,true,connj,"");
        busca_cuentas.setLocationRelativeTo(this);
        busca_cuentas.setVisible(true);
        cuenta.setText(busca_cuentas.getText());//obtiene el valor seleccionado
        busca_cuentas=null;
        if(cuenta.getText().equals("")){

        }else{
            rs0=null;
            try{
                String senSQL="SELECT * FROM cuentasbancarias WHERE id_cuenta='"+cuenta.getText()+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    descripcion.setText(rs0.getString("numerocuenta"));
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
    }//GEN-LAST:event_btncuentaActionPerformed

    private void btneliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarActionPerformed
        // TODO add your handling code here:
        if (JOptionPane.showConfirmDialog(this,"ESTA SEGURO QUE DESEA CANCELAR!!"," I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            String motivoelimina=""+JOptionPane.showInputDialog(this, "INTRODUCE EL MOTIVO DE CANCELACION:", "P R E G U N T A !!!!!!!!!!", JOptionPane.QUESTION_MESSAGE);
            if(!motivoelimina.equals("") && !motivoelimina.equals("null")){
                String senSQLmov = "UPDATE pagos SET estatus='Can' WHERE id_pagos='" + id.getText() + "';";
                conexion.modificamov_p(senSQLmov, connj,valor_privilegio);
                senSQLmov = "UPDATE pagos_detalle SET estatus='Can' WHERE id_pagos='" + id.getText() + "';";
                conexion.modificaupdate_p(senSQLmov, connj,valor_privilegio);
                actualiza_anticipo(id_anticipo.getText());
                salir();
            }
        }
    }//GEN-LAST:event_btneliminarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        busca_pagos_anticipos busca_pagos_anticipos = new busca_pagos_anticipos(null,true,connj,"");
        busca_pagos_anticipos.setLocationRelativeTo(this);
        busca_pagos_anticipos.setVisible(true);
        id_anticipo.setText(busca_pagos_anticipos.getText());//obtiene el valor seleccionado
        busca_pagos_anticipos=null;
        if(id_anticipo.getText().equals("0") || id_anticipo.getText().equals("")){

        }else{
            //deshabilita los campos por que ya fueron llenados en el pago anticipo
            documento.setEditable(false);
            tipopago.setEnabled(false);
            btnclientes.setEnabled(false);
            btncuenta.setEnabled(false);

            rs0=null;
            try{
                String senSQL="SELECT pagos_anticipos.*,clientes.nombre,cuentasbancarias.numerocuenta,COALESCE(pagos_normal.importe_pagos,0) as importe_pagos FROM ((pagos_anticipos LEFT JOIN (SELECT pagos.id_pagos_anticipos,sum(pagos.importe) as importe_pagos FROM pagos WHERE pagos.estatus<>'Can' GROUP BY id_pagos_anticipos) as pagos_normal ON pagos_anticipos.id_pagos_anticipos=pagos_normal.id_pagos_anticipos) LEFT JOIN clientes ON pagos_anticipos.id_clientes=clientes.id_clientes) LEFT JOIN cuentasbancarias ON pagos_anticipos.id_cuenta=cuentasbancarias.id_cuenta WHERE (pagos_anticipos.estatus='Act' AND pagos_anticipos.id_pagos_anticipos='"+id_anticipo.getText()+"') ORDER BY pagos_anticipos.fechapago;";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    documento.setText(rs0.getString("documento"));
                    tipopago.setSelectedItem(rs0.getString("tipo_abono"));
                    fecha.setDate(rs0.getDate("fechapago"));
                    clavecliente.setText(rs0.getString("id_clientes"));
                    String nombrec=rs0.getString("nombre");
                    nombrecliente.setText(nombrec);
                    importeini.setText(fijo2decimales.format(rs0.getDouble("importe")-rs0.getDouble("importe_pagos")));
                    cuenta.setText(rs0.getString("id_cuenta"));
                    descripcion.setText(rs0.getString("numerocuenta"));
                }
                if(rs0!=null) { rs0.close();     }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void importeiniFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_importeiniFocusLost
        // TODO add your handling code here:
        if(id_anticipo.getText().equals("0") || id_anticipo.getText().equals("")){

        }else{
            //comprueba que el importe no exceda el saldo actual
            rs0=null;
            try{
                String senSQL="SELECT pagos_anticipos.*,clientes.nombre,cuentasbancarias.numerocuenta,COALESCE(pagos_normal.importe_pagos,0) as importe_pagos FROM ((pagos_anticipos LEFT JOIN (SELECT pagos.id_pagos_anticipos,sum(pagos.importe) as importe_pagos FROM pagos WHERE pagos.estatus<>'Can' GROUP BY id_pagos_anticipos) as pagos_normal ON pagos_anticipos.id_pagos_anticipos=pagos_normal.id_pagos_anticipos) LEFT JOIN clientes ON pagos_anticipos.id_clientes=clientes.id_clientes) LEFT JOIN cuentasbancarias ON pagos_anticipos.id_cuenta=cuentasbancarias.id_cuenta WHERE (pagos_anticipos.estatus='Act' AND pagos_anticipos.id_pagos_anticipos='"+id_anticipo.getText()+"') ORDER BY pagos_anticipos.fechapago;";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    Double saldoactual=rs0.getDouble("importe")-rs0.getDouble("importe_pagos");
                    Double importeini_t=Double.parseDouble(importeini.getText());
                    if(importeini_t>saldoactual){
                        JOptionPane.showMessageDialog(this, "IMPORTE MAYOR AL SALDO DEL ANTICIPO", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                        importeini.setText("0.00");
                        importeini.requestFocus();
                    }

                }
                if(rs0!=null) { rs0.close();     }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
    }//GEN-LAST:event_importeiniFocusLost

    private void importeiniFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_importeiniFocusGained
        // TODO add your handling code here:
        importeini.selectAll();
    }//GEN-LAST:event_importeiniFocusGained

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabladatos;
    private javax.swing.JButton btnaceptar;
    private javax.swing.JButton btnadd;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btnclientes;
    private javax.swing.JButton btncuenta;
    private javax.swing.JButton btneliminar;
    private javax.swing.JButton btnremover;
    private javax.swing.JTextField clavecliente;
    private javax.swing.JTextField cuenta;
    private javax.swing.JTextField descripcion;
    private javax.swing.JTextField documento;
    private com.toedter.calendar.JDateChooser fecha;
    private javax.swing.JTextField id;
    private javax.swing.JTextField id_anticipo;
    private javax.swing.JFormattedTextField importe;
    private javax.swing.JFormattedTextField importe_factorajet;
    private javax.swing.JFormattedTextField importeini;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbiva;
    private javax.swing.JTextField nombrecliente;
    private javax.swing.JComboBox tipopago;
    // End of variables declaration//GEN-END:variables

}
