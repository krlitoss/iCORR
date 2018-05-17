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

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author IVONNE
 */
public class datos_traspaso_ops extends javax.swing.JDialog {
    Connection connj;
    ResultSet rs0=null;
    DecimalFormat fijo2decimales=new DecimalFormat("######0.00");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    DecimalFormat estandarentero=new DecimalFormat("#,###,##0");
    int cantidadapasar=0,pzaspaq=1;
    Double inlargo=0.0,inancho=0.0,inalto=0.0;
    Double inlargo2=0.0,inancho2=0.0,inalto2=0.0;
    String valor_privilegio="1";

    /** Creates new form datos_usuarios */
    public datos_traspaso_ops(java.awt.Frame parent, boolean modal,Connection conn,String clavemodifica,String otraop,String valor_privilegio_t) {
        super(parent, modal);
        initComponents();
        connj=conn;
        valor_privilegio=valor_privilegio_t;
        fecha.setDate(new Date());
        consultamodifica(clavemodifica);
        //cambia la tecla enter por un tab
        java.util.HashSet conj = new java.util.HashSet(this.getFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        conj.add(java.awt.AWTKeyStroke.getAWTKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0));
        this.setFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, conj);
    }
    public void salir(){
        if(connj!=null){
            connj=null;
            System.out.println("Conexion en null...............");
        }
        dispose();
        this.setVisible(false);
    }
    public void consultamodifica(String clavemodifica){
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        id = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btnaceptar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        fecha = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        articulo = new javax.swing.JTextField();
        op = new javax.swing.JTextField();
        clavearticulo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cantidad = new javax.swing.JFormattedTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        op2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        clavearticulo2 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        articulo2 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_traspaso_ops.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        id.setEditable(false);
        id.setFont(resourceMap.getFont("id.font")); // NOI18N
        id.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id.setFocusable(false);
        id.setName("id"); // NOI18N

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(108, 108, 108)
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(108, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        fecha.setEnabled(false);
        fecha.setName("fecha"); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel2.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel2.border.titleFont"))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        articulo.setEditable(false);
        articulo.setFocusable(false);
        articulo.setName("articulo"); // NOI18N
        articulo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                articuloFocusGained(evt);
            }
        });

        op.setName("op"); // NOI18N
        op.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                opFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                opFocusLost(evt);
            }
        });

        clavearticulo.setEditable(false);
        clavearticulo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        clavearticulo.setText(resourceMap.getString("clavearticulo.text")); // NOI18N
        clavearticulo.setFocusable(false);
        clavearticulo.setName("clavearticulo"); // NOI18N
        clavearticulo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                clavearticuloFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                clavearticuloFocusLost(evt);
            }
        });

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        cantidad.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0"))));
        cantidad.setText(resourceMap.getString("cantidad.text")); // NOI18N
        cantidad.setName("cantidad"); // NOI18N
        cantidad.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cantidadFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cantidadFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(op, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(clavearticulo))
                            .addComponent(articulo, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(op, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(clavearticulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(articulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel3.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel3.border.titleFont"))); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        op2.setText(resourceMap.getString("op2.text")); // NOI18N
        op2.setName("op2"); // NOI18N
        op2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                op2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                op2FocusLost(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        clavearticulo2.setEditable(false);
        clavearticulo2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        clavearticulo2.setText(resourceMap.getString("clavearticulo2.text")); // NOI18N
        clavearticulo2.setFocusable(false);
        clavearticulo2.setName("clavearticulo2"); // NOI18N
        clavearticulo2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                clavearticulo2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                clavearticulo2FocusLost(evt);
            }
        });

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        articulo2.setEditable(false);
        articulo2.setText(resourceMap.getString("articulo2.text")); // NOI18N
        articulo2.setFocusable(false);
        articulo2.setName("articulo2"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(op2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clavearticulo2))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(articulo2, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(op2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(clavearticulo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(articulo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanel2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:
        int errorcantidad=0;
        int errorarticulos=0;
        int cant=Integer.parseInt(cantidad.getText());
        if(cant>cantidadapasar){
            errorcantidad=1;
        }
        if(!clavearticulo.getText().equals(clavearticulo2.getText())){
            if(inlargo!=inlargo2 || inancho!=inancho || inalto!=inalto){
                errorarticulos=0;
            }
        }

        if(fecha.getDate()==null||op.getText().equals("")||clavearticulo.getText().equals("")||cantidad.getText().equals("")||cantidad.getText().equals("0")||op2.getText().equals("")||clavearticulo2.getText().equals("")||errorcantidad==1||errorarticulos==1){
            String err="VERIFICA HAY CAMPOS VACIOS";
            if(errorcantidad==1)
                err="LA CANTIDAD ES MAYOR DE LA EXISTENTE EN ALMACEN DE PT";
            if(errorarticulos==1)
                err="LOS ARTICULOS NO SON COMPATIBLES";

            JOptionPane.showMessageDialog(this, err, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{

            
            if (JOptionPane.showConfirmDialog(this,"ESTA SEGURO QUE DESEA GUARDAR!!"," I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                String senSQL="INSERT INTO traspasos_op(estatus,fecha, op, clavearticulo, cantidadpzas, op2, clavearticulo2) VALUES ('Act', '"+fechainsertar.format(fecha.getDate())+"', '"+op.getText()+"', '"+clavearticulo.getText()+"', '"+cantidad.getText()+"', '"+op2.getText()+"', '"+clavearticulo2.getText()+"');";
                conexion.modifica_p(senSQL,connj,valor_privilegio);
                //actauliza los datos al almacen de embarques
                rs0=null;
                try{
                    senSQL="SELECT * FROM almacen_pt WHERE (op='"+op.getText()+"' AND clavearticulo='"+clavearticulo.getText()+"')";
                    rs0=conexion.consulta(senSQL,connj);
                    if(rs0.next()){
                        int nuevacantidad=rs0.getInt("cantidadpzas")-Integer.parseInt(cantidad.getText());
                        senSQL = "UPDATE almacen_pt SET cantidadpzas='"+nuevacantidad+"' WHERE (op='"+op.getText()+"' AND clavearticulo='"+clavearticulo.getText()+"');";
                        conexion.modificamov_p(senSQL, connj,valor_privilegio);
                    }
                    if(rs0!=null) {
                        rs0.close();
                    }
                } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                //actauliza los datos al almacen de embarques
                rs0=null;
                try{
                    senSQL="SELECT * FROM almacen_pt WHERE (op='"+op2.getText()+"' AND clavearticulo='"+clavearticulo2.getText()+"')";
                    rs0=conexion.consulta(senSQL,connj);
                    if(rs0.next()){
                        int nuevacantidad=rs0.getInt("cantidadpzas")+Integer.parseInt(cantidad.getText());
                        senSQL = "UPDATE almacen_pt SET cantidadpzas='"+nuevacantidad+"' WHERE (op='"+op2.getText()+"' AND clavearticulo='"+clavearticulo2.getText()+"');";
                        conexion.modificamov_p(senSQL, connj,valor_privilegio);
                    }else{
                        senSQL="INSERT INTO almacen_pt(fecha, op, clavearticulo, cantidadpzas, pzaspaquete, almacen)VALUES ('"+fechainsertar.format(new Date())+"', '"+op2.getText()+"', '"+clavearticulo2.getText()+"', '"+cantidad.getText()+"', '"+pzaspaq+"', '1');";
                        conexion.modificamov_p(senSQL,connj,valor_privilegio);
                    }
                    if(rs0!=null) {
                        rs0.close();
                    }
                } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

                //elimina los ceros del inventario
                senSQL="DELETE FROM almacen_pt WHERE (cantidadpzas<1) ";
                conexion.modificamov_p(senSQL, connj,valor_privilegio);


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

    private void opFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_opFocusGained
        // TODO add your handling code here:
        op.selectAll();
}//GEN-LAST:event_opFocusGained

    private void articuloFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_articuloFocusGained
        // TODO add your handling code here:
        articulo.selectAll();
}//GEN-LAST:event_articuloFocusGained

    private void cantidadFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cantidadFocusGained
        // TODO add your handling code here:
        cantidad.selectAll();
}//GEN-LAST:event_cantidadFocusGained

    private void opFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_opFocusLost
        // TODO add your handling code here:
        op.setText(op.getText().toUpperCase());
        if(op.getText().equals("")){
            clavearticulo.setEditable(false);
            clavearticulo.setFocusable(false);
        }else{
            if(op.getText().equals("STOCK")){
                clavearticulo.setEditable(true);
                clavearticulo.setFocusable(true);
                clavearticulo.requestFocus();
            }else{
                rs0=null;
                cantidadapasar=0;
                inlargo=0.0;
                inancho=0.0;
                inalto=0.0;
                pzaspaq=0;
                try{
                    String senSQL="SELECT almacen_pt.*,articulos.articulo,articulos.inlargo,articulos.inancho,articulos.inalto FROM almacen_pt LEFT JOIN articulos ON almacen_pt.clavearticulo=articulos.clavearticulo WHERE (almacen_pt.op='"+op.getText()+"')";
                    rs0=conexion.consulta(senSQL,connj);
                    if(rs0.next()){
                        clavearticulo.setEditable(false);
                        clavearticulo.setFocusable(false);
                        clavearticulo.setText(rs0.getString("clavearticulo"));
                        articulo.setText(rs0.getString("articulo"));
                        cantidadapasar=rs0.getInt("cantidadpzas");
                        inlargo=rs0.getDouble("inlargo");
                        inancho=rs0.getDouble("inancho");
                        inalto=rs0.getDouble("inalto");
                        pzaspaq=rs0.getInt("pzaspaquete");
                    }else{
                        JOptionPane.showMessageDialog(this,"LA OP NO EXISTE EN EL ALMACEN DE PT","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                        clavearticulo.setEditable(false);
                        clavearticulo.setFocusable(false);
                        op.setText("");
                        clavearticulo.setText("");
                        articulo.setText("");
                        op.requestFocus();
                    }
                    if(rs0!=null) {
                        rs0.close();
                    }
                } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

            }
        }
}//GEN-LAST:event_opFocusLost

    private void clavearticuloFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clavearticuloFocusGained
        // TODO add your handling code here:
        clavearticulo.selectAll();
    }//GEN-LAST:event_clavearticuloFocusGained

    private void cantidadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cantidadFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_cantidadFocusLost

    private void clavearticuloFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clavearticuloFocusLost
        // TODO add your handling code here:
        if(clavearticulo.getText().equals("")){

        }else{
            rs0=null;
            cantidadapasar=0;
            inlargo=0.0;
            inancho=0.0;
            inalto=0.0;
            try{
                String senSQL="SELECT almacen_pt.*,articulos.articulo,articulos.inlargo,articulos.inancho,articulos.inalto FROM almacen_pt LEFT JOIN articulos ON almacen_pt.clavearticulo=articulos.clavearticulo WHERE (almacen_pt.op='"+op.getText()+"' AND almacen_pt.clavearticulo='"+clavearticulo.getText()+"')";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    articulo.setText(rs0.getString("articulo"));
                    cantidadapasar=rs0.getInt("cantidadpzas");
                    inlargo=rs0.getDouble("inlargo");
                    inancho=rs0.getDouble("inancho");
                    inalto=rs0.getDouble("inalto");
                    pzaspaq=rs0.getInt("pzaspaquete");
                }else{
                    JOptionPane.showMessageDialog(this,"LA OP NO EXISTE EN EL ALMACEN DE PT","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                    clavearticulo.setText("");
                    articulo.setText("");
                    clavearticulo.requestFocus();
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
    }//GEN-LAST:event_clavearticuloFocusLost

    private void op2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_op2FocusGained
        // TODO add your handling code here:
        op2.selectAll();
    }//GEN-LAST:event_op2FocusGained

    private void clavearticulo2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clavearticulo2FocusGained
        // TODO add your handling code here:
        clavearticulo2.selectAll();
    }//GEN-LAST:event_clavearticulo2FocusGained

    private void op2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_op2FocusLost
        // TODO add your handling code here:
        op2.setText(op2.getText().toUpperCase());
        if(op2.getText().equals("")){
            clavearticulo2.setEditable(false);
            clavearticulo2.setFocusable(false);
        }else{
            if(op2.getText().equals("STOCK")){
                clavearticulo2.setEditable(true);
                clavearticulo2.setFocusable(true);
                clavearticulo2.requestFocus();
            }else{
                rs0=null;
                inlargo2=0.0;
                inancho2=0.0;
                inalto2=0.0;
                try{
                    String senSQL="SELECT ops.*,articulos.articulo,articulos.inlargo,articulos.inancho,articulos.inalto FROM ops LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo WHERE (ops.op='"+op2.getText()+"')";
                    rs0=conexion.consulta(senSQL,connj);
                    if(rs0.next()){
                        clavearticulo2.setEditable(false);
                        clavearticulo2.setFocusable(false);
                        clavearticulo2.setText(rs0.getString("clavearticulo"));
                        articulo2.setText(rs0.getString("articulo"));
                        inlargo2=rs0.getDouble("inlargo");
                        inancho2=rs0.getDouble("inancho");
                        inalto2=rs0.getDouble("inalto");
                    }else{
                        JOptionPane.showMessageDialog(this,"LA OP NO EXISTE EN EL ALMACEN DE PT","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                        clavearticulo2.setEditable(false);
                        clavearticulo2.setFocusable(false);
                        op2.setText("");
                        clavearticulo2.setText("");
                        articulo2.setText("");
                        op2.requestFocus();
                    }
                    if(rs0!=null) {
                        rs0.close();
                    }
                } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

            }
        }
    }//GEN-LAST:event_op2FocusLost

    private void clavearticulo2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clavearticulo2FocusLost
        // TODO add your handling code here:
        if(clavearticulo2.getText().equals("")){

        }else{
            rs0=null;
            inlargo2=0.0;
            inancho2=0.0;
            inalto2=0.0;
            try{
                String senSQL="SELECT articulos.articulo,articulos.inlargo,articulos.inancho,articulos.inalto FROM articulos WHERE (articulos.clavearticulo='"+clavearticulo2.getText()+"')";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    articulo2.setText(rs0.getString("articulo"));
                    inlargo2=rs0.getDouble("inlargo");
                    inancho2=rs0.getDouble("inancho");
                    inalto2=rs0.getDouble("inalto");
                }else{
                    JOptionPane.showMessageDialog(this,"NO EXISTE EL ARTICULO","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                    clavearticulo2.setText("");
                    articulo2.setText("");
                    clavearticulo2.requestFocus();
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
    }//GEN-LAST:event_clavearticulo2FocusLost

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField articulo;
    private javax.swing.JTextField articulo2;
    private javax.swing.JButton btnaceptar;
    private javax.swing.JButton btncancelar;
    private javax.swing.JFormattedTextField cantidad;
    private javax.swing.JTextField clavearticulo;
    private javax.swing.JTextField clavearticulo2;
    private com.toedter.calendar.JDateChooser fecha;
    private javax.swing.JTextField id;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField op;
    private javax.swing.JTextField op2;
    // End of variables declaration//GEN-END:variables

}