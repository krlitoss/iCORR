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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author IVONNE
 */
public final class datos_contizaciones extends javax.swing.JDialog {
    DefaultTableModel modelot1=null;
    DecimalFormat fijo0decimales=new DecimalFormat("######0");
    DecimalFormat fijo2decimales=new DecimalFormat("######0.00");
    DecimalFormat fijo5decimales=new DecimalFormat("######0.00000");
    DecimalFormat moneda2decimales=new DecimalFormat("$ #,###,##0.00");
    DecimalFormat estandarentero=new DecimalFormat("#,###,##0");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechamediana = new SimpleDateFormat("dd-MMM-yyyy");
    SimpleDateFormat fechacorta = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Connection connj;
    ResultSet rs0=null,rs1=null;
    Double valoriva=0.0,valorieps=0.0;
    int banderabuscaart=0;
    String terminacion="M.N.";
    int cambia=1;
    Calendar calendarvencimiento = new GregorianCalendar();
    private Properties conf;
    

    /** Creates new form datos_usuarios */
    public datos_contizaciones(java.awt.Frame parent, boolean modal,Connection conn,String clavemodifica) {
        super(parent, modal);
        initComponents();
        connj=conn;
        conf=conexion.archivoInicial();
        modelot1=(DefaultTableModel) Tabladatos.getModel();
        Tabladatos.setModel(modelot1);
        ajusteTabladatos();
        estados();
        condiciones();
        fecha.setDate(new Date());
        ejecutivo.setText(conf.getProperty("User"));
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
    public void ajusteTabladatos() {
        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(250);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(3).setPreferredWidth(60);
        Tabladatos.getColumnModel().getColumn(4).setPreferredWidth(75);
        Tabladatos.getColumnModel().getColumn(5).setPreferredWidth(55);
        Tabladatos.getColumnModel().getColumn(6).setPreferredWidth(65);
        Tabladatos.getColumnModel().getColumn(7).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(8).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(9).setPreferredWidth(50);
        Tabladatos.getColumnModel().getColumn(10).setPreferredWidth(70);
        Tabladatos.getColumnModel().getColumn(11).setPreferredWidth(80);
        Tabladatos.getColumnModel().getColumn(12).setPreferredWidth(90);
    }

    public void sumatoria(){
        int filas=Tabladatos.getRowCount();
        if(filas<=0){

        }else{
            Double sumaimportes=0.0;
            for(int i=0;i<=(filas-1);i=i+1){
                Double valimporte=(Double) Tabladatos.getValueAt(i, 2);///obtiene el importe
                sumaimportes=sumaimportes+valimporte;
            }
            importe.setText(fijo2decimales.format(sumaimportes));
        }
    }
    public void consultamodifica(String clavemodifica){
        if(clavemodifica.equals("")){
            /**si la clave esta vacia significa que es un registro nuevo*/
        }else{
            id.setEditable(false);
            id.setFocusable(false);
            btnaceptar.setEnabled(false);
            rs0=null;
            try{
                String senSQL="SELECT contrarecibos_detalle.*,contrarecibos.*,clientes.nombre FROM contrarecibos_detalle LEFT JOIN (contrarecibos LEFT JOIN clientes ON contrarecibos.id_clientes=clientes.id_clientes) ON contrarecibos_detalle.id_contrarecibo=contrarecibos.id_contrarecibo WHERE (contrarecibos.estatus='Act' AND contrarecibos.id_contrarecibo='"+clavemodifica+"');";
                rs0=conexion.consulta(senSQL,connj);
                while(rs0.next()){
                    /*id.setText(rs0.getString("id_contrarecibo"));
                    contrarecibo.setText(rs0.getString("contrarecibo"));
                    fecha.setDate(rs0.getDate("fecha"));
                    fechapago.setDate(rs0.getDate("fecha_pago"));
                    clavecliente.setText(rs0.getString("id_clientes"));
                    String nombrec=rs0.getString("nombre");
                    nombrecliente.setText(nombrec);
                    Object datos[]={rs0.getString("factura_serie"),fechamediana.format(rs0.getDate("fechavencimiento")),rs0.getDouble("saldo")};
                    modelot1.addRow(datos);
                    sumatoria();*/
                }
                if(rs0!=null) {   rs0.close();   }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
    }
    public void estados(){
        rs0=null;
        try{
            String senSQL="SELECT * FROM estados";
            rs0=conexion.consulta(senSQL,connj);
            while(rs0.next()){
                comboestados.addItem(rs0.getString("estado"));

            }
            if(rs0!=null) {  rs0.close(); }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
    }
    public void condiciones(){
        rs0=null;
        try{
            String senSQL="SELECT * FROM condiciones_cotizacion";
            rs0=conexion.consulta(senSQL,connj);
            while(rs0.next()){
                combocondiciones.addItem(rs0.getString("condicion"));

            }
            if(rs0!=null) {  rs0.close(); }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
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
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabladatos = new javax.swing.JTable();
        btnremover = new javax.swing.JButton();
        btnadd = new javax.swing.JButton();
        lbiva = new javax.swing.JLabel();
        importe = new javax.swing.JFormattedTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        fecha = new com.toedter.calendar.JDateChooser();
        id = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        ejecutivo = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        notas = new javax.swing.JTextArea();
        atn = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        clavecliente = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        nombrecliente = new javax.swing.JTextField();
        btnclientes = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        direccion = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        municipio = new javax.swing.JTextField();
        pais = new javax.swing.JTextField();
        comboestados = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        telefono = new javax.swing.JTextField();
        fax = new javax.swing.JTextField();
        correo = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        combocondiciones = new javax.swing.JComboBox();
        btncondiciones = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtcondiciones = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        btncopy = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_contizaciones.class);
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(278, 278, 278)
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(284, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jLabel6.setFont(resourceMap.getFont("jLabel6.font")); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        Tabladatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Descripcion", "Largo", "Ancho", "Alto", "Corrugado", "Flauta", "Resis.", "Linner", "Estilo", "Tintas", "Embalaje", "Cantidad", "Precio x Millar"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
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

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

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

        jLabel16.setText(resourceMap.getString("jLabel16.text")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N

        ejecutivo.setText(resourceMap.getString("ejecutivo.text")); // NOI18N
        ejecutivo.setName("ejecutivo"); // NOI18N
        ejecutivo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ejecutivoFocusGained(evt);
            }
        });

        jLabel17.setText(resourceMap.getString("jLabel17.text")); // NOI18N
        jLabel17.setName("jLabel17"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        notas.setColumns(20);
        notas.setFont(resourceMap.getFont("notas.font")); // NOI18N
        notas.setLineWrap(true);
        notas.setRows(4);
        notas.setName("notas"); // NOI18N
        notas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                notasFocusGained(evt);
            }
        });
        jScrollPane2.setViewportView(notas);

        atn.setText(resourceMap.getString("atn.text")); // NOI18N
        atn.setName("atn"); // NOI18N
        atn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                atnFocusGained(evt);
            }
        });

        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(atn))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ejecutivo, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 681, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16)
                        .addComponent(ejecutivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(atn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(73, 73, 73))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addContainerGap())))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel5.TabConstraints.tabTitle"), jPanel5); // NOI18N

        jPanel2.setName("jPanel2"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        clavecliente.setEditable(false);
        clavecliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        clavecliente.setText(resourceMap.getString("clavecliente.text")); // NOI18N
        clavecliente.setFocusable(false);
        clavecliente.setName("clavecliente"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        nombrecliente.setText(resourceMap.getString("nombrecliente.text")); // NOI18N
        nombrecliente.setName("nombrecliente"); // NOI18N
        nombrecliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nombreclienteFocusGained(evt);
            }
        });

        btnclientes.setIcon(resourceMap.getIcon("btnclientes.icon")); // NOI18N
        btnclientes.setText(resourceMap.getString("btnclientes.text")); // NOI18N
        btnclientes.setName("btnclientes"); // NOI18N
        btnclientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnclientesActionPerformed(evt);
            }
        });

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        direccion.setText(resourceMap.getString("direccion.text")); // NOI18N
        direccion.setName("direccion"); // NOI18N
        direccion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                direccionFocusGained(evt);
            }
        });

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        municipio.setText(resourceMap.getString("municipio.text")); // NOI18N
        municipio.setName("municipio"); // NOI18N
        municipio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                municipioFocusGained(evt);
            }
        });

        pais.setText(resourceMap.getString("pais.text")); // NOI18N
        pais.setName("pais"); // NOI18N
        pais.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                paisFocusGained(evt);
            }
        });

        comboestados.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Selecciona" }));
        comboestados.setName("comboestados"); // NOI18N

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        telefono.setText(resourceMap.getString("telefono.text")); // NOI18N
        telefono.setName("telefono"); // NOI18N
        telefono.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                telefonoFocusGained(evt);
            }
        });

        fax.setText(resourceMap.getString("fax.text")); // NOI18N
        fax.setName("fax"); // NOI18N
        fax.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                faxFocusGained(evt);
            }
        });

        correo.setText(resourceMap.getString("correo.text")); // NOI18N
        correo.setName("correo"); // NOI18N
        correo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                correoFocusGained(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clavecliente, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnclientes, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(comboestados, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pais, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(municipio, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fax, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                            .addComponent(telefono, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                            .addComponent(correo, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)))
                    .addComponent(direccion, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
                    .addComponent(nombrecliente, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(nombrecliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnclientes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(clavecliente)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(direccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(municipio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(telefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(comboestados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(fax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(pais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(correo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel2.TabConstraints.tabTitle"), jPanel2); // NOI18N

        jPanel3.setName("jPanel3"); // NOI18N

        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        combocondiciones.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Selecciona" }));
        combocondiciones.setName("combocondiciones"); // NOI18N

        btncondiciones.setText(resourceMap.getString("btncondiciones.text")); // NOI18N
        btncondiciones.setName("btncondiciones"); // NOI18N
        btncondiciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncondicionesActionPerformed(evt);
            }
        });

        jLabel15.setText(resourceMap.getString("jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        txtcondiciones.setColumns(20);
        txtcondiciones.setFont(resourceMap.getFont("txtcondiciones.font")); // NOI18N
        txtcondiciones.setLineWrap(true);
        txtcondiciones.setRows(5);
        txtcondiciones.setName("txtcondiciones"); // NOI18N
        txtcondiciones.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtcondicionesFocusGained(evt);
            }
        });
        jScrollPane3.setViewportView(txtcondiciones);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(combocondiciones, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btncondiciones, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE))
                .addGap(232, 232, 232))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(combocondiciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btncondiciones))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel3.TabConstraints.tabTitle"), jPanel3); // NOI18N

        jButton1.setIcon(resourceMap.getIcon("jButton1.icon")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setToolTipText(resourceMap.getString("jButton1.toolTipText")); // NOI18N
        jButton1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton1.setName("jButton1"); // NOI18N

        btncopy.setIcon(resourceMap.getIcon("btncopy.icon")); // NOI18N
        btncopy.setText(resourceMap.getString("btncopy.text")); // NOI18N
        btncopy.setToolTipText(resourceMap.getString("btncopy.toolTipText")); // NOI18N
        btncopy.setName("btncopy"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbiva, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(importe, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 543, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                        .addComponent(btncopy, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnadd, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnremover, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnadd, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnremover, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btncopy, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(importe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbiva))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:
       /* int filas=Tabladatos.getRowCount();
        int columnas=Tabladatos.getColumnCount();
        if(contrarecibo.getText().equals("")||fecha.getDate()==null||clavecliente.getText().equals("")||filas<=0){
            JOptionPane.showMessageDialog(this, "VERIFICA HAY CAMPOS VACIOS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            if (Tabladatos.getCellEditor() != null) {//finaliza el editor
                Tabladatos.getCellEditor().stopCellEditing();
            }
            ///revisar que no existan campos vacios
            int camposvacios=0;
            for(int i=0;i<=(filas-1);i=i+1){
                for(int j=0;j<=(columnas-1);j=j+1){
                    if(modelot1.getValueAt(i, j) == null||modelot1.getValueAt(i, j).equals("")){
                        camposvacios=1;
                    }

                }
                //funcion que verifica que no quede en ceros la cantidad
                if((Double)modelot1.getValueAt(i, 2) ==0.0){
                    camposvacios=1;
                }
            }///fin de revisar los campos vacios
            sumatoria();

            if(camposvacios==1){
                String erro="LA TABLA DE DETALLE TIENE CAMPOS VACIOS\nBORRE LAS FILAS VACIAS";
                JOptionPane.showMessageDialog(this, erro, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }else{

                if(id.getText().equals("")){
                    if (JOptionPane.showConfirmDialog(this,"ESTA SEGURO QUE DESEA CONTINUAR!!"," I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        String senSQL="INSERT INTO contrarecibos(estatus, fecha, id_clientes, contrarecibo, fecha_pago, fechareal) VALUES ('Act', '"+fechainsertarhora.format(fecha.getDate())+"', '"+clavecliente.getText()+"', '"+contrarecibo.getText()+"', '"+fechainsertarhora.format(fechapago.getDate())+"', '"+fechainsertarhora.format(new Date())+"');";
                        conexion.modifica(senSQL,connj);
                        int claveidmax=0;
                        rs0=null;
                        try{
                            senSQL="SELECT max(id_contrarecibo) as contramax FROM contrarecibos";
                            rs0=conexion.consulta(senSQL,connj);
                            if(rs0.next()){
                                claveidmax=rs0.getInt("contramax");
                                id.setText(""+claveidmax);
                            }
                            if(rs0!=null) {  rs0.close(); }
                        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

                        //funcion que registra todos los movimientos de la orden de compra
                        for(int i=0;i<=(filas-1);i=i+1){
                            String senSQLmov="INSERT INTO contrarecibos_detalle(id_contrarecibo, factura_serie, fechavencimiento,saldo) VALUES ('"+id.getText()+"', '"+modelot1.getValueAt(i, 0)+"', '"+modelot1.getValueAt(i, 1)+"', '"+modelot1.getValueAt(i, 2)+"');";
                            conexion.modificamov(senSQLmov,connj);
                            //cambia las fechas de vencimiento por la nueva
                            senSQLmov = "UPDATE docxcob SET fechavencimiento='"+fechainsertarhora.format(fechapago.getDate())+"' WHERE factura_serie='" + modelot1.getValueAt(i, 0) + "';";
                            conexion.modificamov(senSQLmov, connj);
                        }
                    }
                }
                salir();

            }
        }*/
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
        if(filas<15){
            if(fecha.getDate()==null){
                JOptionPane.showMessageDialog(this, "HAY CAMPOS VACIOS LLENA LOS PRIMEROS CAMPOS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }else{
                btnclientes.setEnabled(false);
                Object datos[]={null};
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
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_F7)
        {
            int filano=Tabladatos.getSelectedRow();

            datos_cotizaciones_detalle datos_cotizaciones_detalle = new datos_cotizaciones_detalle(null,true,connj,"");
            datos_cotizaciones_detalle.setLocationRelativeTo(this);
            datos_cotizaciones_detalle.setVisible(true);
            Tabladatos.setValueAt(datos_cotizaciones_detalle.getText(), filano, 0);
            String valorfiltro=datos_cotizaciones_detalle.getText();
            datos_cotizaciones_detalle=null;

            if(valorfiltro.equals("")){
                Tabladatos.setValueAt(null, filano, 1);
            }else{

                rs0=null;
                try{
                    String senSQL="SELECT remisiones.remision,remisiones.fechahora,remisiones.um,remisiones.cantidad,(remisiones.cantidad-COALESCE(facturado.facturacantidad,0)) as cantrempendiente,remisiones.preciounitario,remisiones.id_op,remisiones.clavearticulo,remisiones.ordencompra,articulos.articulo,articulos.kg,articulos.m2,clientes.nombre,COALESCE(facturado.facturacantidad,0) as facturacantidad,COALESCE(facturado.facturaimporte,0) as facturaimporte FROM ((remisiones LEFT JOIN clientes ON remisiones.id_clientes=clientes.id_clientes)  LEFT JOIN articulos ON remisiones.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT facturas_detalle.remision,sum(facturas_detalle.cantidad) as facturacantidad,sum(facturas_detalle.cantidad*facturas_detalle.preciounitario) as facturaimporte FROM facturas_detalle WHERE (facturas_detalle.estatus='1') GROUP BY facturas_detalle.remision) as facturado ON remisiones.remision=facturado.remision WHERE (remisiones.estatus<>'Can' AND remisiones.remision='"+valorfiltro+"' AND facturado='false') ORDER BY remisiones.fechahora;";
                    rs0=conexion.consulta(senSQL,connj);
                    if(rs0.next()){
                        Tabladatos.setValueAt(rs0.getString("um"), filano, 2);
                        Tabladatos.setValueAt(rs0.getString("id_op"), filano, 3);
                    }
                    if(rs0!=null) {   rs0.close(); }
                } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
            }
            Tabladatos.changeSelection(filano, 1, false, false);
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
                    direccion.setText(rs0.getString("calle")+" "+rs0.getString("numeroext")+" "+rs0.getString("colonia"));
                    municipio.setText(rs0.getString("municipio"));
                    comboestados.setSelectedItem(rs0.getString("estado"));
                    pais.setText(rs0.getString("pais"));
                    telefono.setText(rs0.getString("telefono1"));
                    fax.setText(rs0.getString("telefono2"));
                }
                if(rs0!=null) {   rs0.close(); }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }            

        }
    }//GEN-LAST:event_btnclientesActionPerformed

    private void ejecutivoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ejecutivoFocusGained
        // TODO add your handling code here:
        ejecutivo.selectAll();
    }//GEN-LAST:event_ejecutivoFocusGained

    private void atnFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_atnFocusGained
        // TODO add your handling code here:
        atn.selectAll();
    }//GEN-LAST:event_atnFocusGained

    private void notasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_notasFocusGained
        // TODO add your handling code here:
        notas.selectAll();
    }//GEN-LAST:event_notasFocusGained

    private void nombreclienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nombreclienteFocusGained
        // TODO add your handling code here:
        nombrecliente.selectAll();
    }//GEN-LAST:event_nombreclienteFocusGained

    private void direccionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_direccionFocusGained
        // TODO add your handling code here:
        direccion.selectAll();
    }//GEN-LAST:event_direccionFocusGained

    private void municipioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_municipioFocusGained
        // TODO add your handling code here:
        municipio.selectAll();
    }//GEN-LAST:event_municipioFocusGained

    private void paisFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_paisFocusGained
        // TODO add your handling code here:
        pais.selectAll();
    }//GEN-LAST:event_paisFocusGained

    private void telefonoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_telefonoFocusGained
        // TODO add your handling code here:
        telefono.selectAll();
    }//GEN-LAST:event_telefonoFocusGained

    private void faxFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_faxFocusGained
        // TODO add your handling code here:
        fax.selectAll();
    }//GEN-LAST:event_faxFocusGained

    private void correoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_correoFocusGained
        // TODO add your handling code here:
        correo.selectAll();
    }//GEN-LAST:event_correoFocusGained

    private void txtcondicionesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtcondicionesFocusGained
        // TODO add your handling code here:
        txtcondiciones.selectAll();
    }//GEN-LAST:event_txtcondicionesFocusGained

    private void btncondicionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncondicionesActionPerformed
        // TODO add your handling code here:
        String con=""+combocondiciones.getSelectedItem();
        if(!con.equals("Selecciona")){
            if(txtcondiciones.getText().equals("")){
                txtcondiciones.setText(txtcondiciones.getText()+con);
            }else{
                txtcondiciones.setText(txtcondiciones.getText()+"\n"+con);
            }
        }
    }//GEN-LAST:event_btncondicionesActionPerformed

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabladatos;
    private javax.swing.JTextField atn;
    private javax.swing.JButton btnaceptar;
    private javax.swing.JButton btnadd;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btnclientes;
    private javax.swing.JButton btncondiciones;
    private javax.swing.JButton btncopy;
    private javax.swing.JButton btnremover;
    private javax.swing.JTextField clavecliente;
    private javax.swing.JComboBox combocondiciones;
    private javax.swing.JComboBox comboestados;
    private javax.swing.JTextField correo;
    private javax.swing.JTextField direccion;
    private javax.swing.JTextField ejecutivo;
    private javax.swing.JTextField fax;
    private com.toedter.calendar.JDateChooser fecha;
    private javax.swing.JTextField id;
    private javax.swing.JFormattedTextField importe;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lbiva;
    private javax.swing.JTextField municipio;
    private javax.swing.JTextField nombrecliente;
    private javax.swing.JTextArea notas;
    private javax.swing.JTextField pais;
    private javax.swing.JTextField telefono;
    private javax.swing.JTextArea txtcondiciones;
    // End of variables declaration//GEN-END:variables

}
