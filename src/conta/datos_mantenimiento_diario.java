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
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author IVONNE
 */
public class datos_mantenimiento_diario extends javax.swing.JDialog {
    Connection connj;
    ResultSet rs0=null;
    String clavemodificaf="";
    DecimalFormat fijo0decimales=new DecimalFormat("######0");
    DecimalFormat estandarentero=new DecimalFormat("#,###,##0");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    SimpleDateFormat horacorta = new SimpleDateFormat("HH:mm");
    int minparo=0;
    DefaultTableModel modelot=null;
    String valor_privilegio="1";


    /** Creates new form datos_usuarios */
    public datos_mantenimiento_diario(java.awt.Frame parent, boolean modal,Connection conn,String clavemodifica,String valor_privilegio_t) {
        super(parent, modal);
        initComponents();
        connj=conn;
        valor_privilegio=valor_privilegio_t;
       // modelot=(DefaultTableModel) Tablaproductos.getModel();
       // Tablaproductos.setModel(modelot);
        fecha.setDate(new Date());
        consultamodifica(clavemodifica);
        clavemodificaf=clavemodifica;
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
     

    }
    public void limpiatabla(){
        modelot.setNumRows(0);
    }

    public void consultamodifica(String clavemodifica){
        if(clavemodifica.equals("")){
            /**si la clave esta vacia significa que es un registro nuevo*/
        }else{
            rs0=null;
            btnaceptar.setEnabled(false);
            btnoperador.setEnabled(false);
            btnmaquinas.setEnabled(false);
            btnarea.setEnabled(false);
            rs0=null;
            try{
                String senSQL="SELECT mantenimiento.*,operadores.nombre as nombreoperador FROM mantenimiento LEFT JOIN operadores ON mantenimiento.id_operador=operadores.id_operador  WHERE (mantenimiento.id_matenimiento='"+clavemodifica+"' AND mantenimiento.estatus<>'Can')";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    
                    fecha.setDate(rs0.getDate("fecha"));
                    nombre.setText(rs0.getString("nombre"));
                    turno.setSelectedItem(rs0.getString("turno"));
                    idoperador.setText(rs0.getString("id_operador"));
                    nombreoperador.setText(rs0.getString("nombreoperador"));
                    nombrematerial.setText(rs0.getString("nombrematerial"));
                    cantidad.setText(rs0.getString("cantidad"));
                     herfaltante.setText(rs0.getString("herfaltante"));

                    
                    horainicio.setText(horacorta.format(rs0.getTime("horainicio")));
                    horatermino.setText(horacorta.format(rs0.getTime("horafinal")));
                    horaentrega.setText(horacorta.format(rs0.getTime("horaentrega")));
                    ordentrabajo.setText(rs0.getString("id_ordentrabajo"));
                    clavemaquina.setText(rs0.getString("clavemaquina"));
                    area.setText(rs0.getString("area_empresa"));
                    tipo.setSelectedItem(rs0.getString("tipo"));
                    tipo2.setSelectedItem(rs0.getString("tipo2"));
                    tipo3.setSelectedItem(rs0.getString("tipo3"));
                    medida.setSelectedItem(rs0.getString("medida"));
                   
     
                    Boolean tm=rs0.getBoolean("tiempomuerto");
                    if(!tm){
                        tmno.setSelected(true);
                        tmsi.setSelected(false);
                    }
                    actividad.setText(rs0.getString("actividad"));
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
    }
    public void minutosParo(){
        String turnot=(String)turno.getSelectedItem();
        if(!horainicio.getText().equals("") && !horatermino.getText().equals("")){
                Date horaini=null;
                Date horater=null;
                try {
                    horaini = horacorta.parse(horainicio.getText());
                    horater = horacorta.parse(horatermino.getText());
                    if(!turnot.equals("3") && (horater.before(horaini) || horater.equals(horaini))){
                           JOptionPane.showMessageDialog(this, "LA HORA FINAL ES MENOR QUE LA INICIAL", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                           horatermino.setText("");
                           horatermino.requestFocus();
                           minparo=0;
                    }else{
                        minparo=(int) ( ((horater.getTime()-horaini.getTime())/1000)/60 );
                        if (minparo<0){ //si la hora es negativa el sistema infiere que es el 3 turno
                             minparo=minparo+1440;
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,"ERROR EN LA HORAS\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnaceptar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        fecha = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        turno = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        idoperador = new javax.swing.JTextField();
        btnoperador = new javax.swing.JButton();
        nombreoperador = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        horainicio = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        horatermino = new javax.swing.JFormattedTextField();
        jLabel12 = new javax.swing.JLabel();
        horaentrega = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        ordentrabajo = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        clavemaquina = new javax.swing.JTextField();
        btnmaquinas = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        area = new javax.swing.JTextField();
        btnarea = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        tipo = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        actividad = new javax.swing.JTextArea();
        tipo2 = new javax.swing.JComboBox();
        tmsi = new javax.swing.JRadioButton();
        tmno = new javax.swing.JRadioButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        nombre = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        nombrematerial = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        tipo3 = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        cantidad = new javax.swing.JTextField();
        medida = new javax.swing.JComboBox();
        herfaltante = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_mantenimiento_diario.class);
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
                .addGap(254, 254, 254)
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(365, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        fecha.setDateFormatString(resourceMap.getString("fecha.dateFormatString")); // NOI18N
        fecha.setName("fecha"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        turno.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3" }));
        turno.setName("turno"); // NOI18N

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        idoperador.setEditable(false);
        idoperador.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        idoperador.setText(resourceMap.getString("idoperador.text")); // NOI18N
        idoperador.setFocusable(false);
        idoperador.setName("idoperador"); // NOI18N

        btnoperador.setIcon(resourceMap.getIcon("btnoperador.icon")); // NOI18N
        btnoperador.setText(resourceMap.getString("btnoperador.text")); // NOI18N
        btnoperador.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnoperador.setName("btnoperador"); // NOI18N
        btnoperador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnoperadorActionPerformed(evt);
            }
        });

        nombreoperador.setEditable(false);
        nombreoperador.setText(resourceMap.getString("nombreoperador.text")); // NOI18N
        nombreoperador.setFocusable(false);
        nombreoperador.setName("nombreoperador"); // NOI18N

        jPanel2.setName("jPanel2"); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel3.border.title"), 0, 0, resourceMap.getFont("jPanel3.border.titleFont"), resourceMap.getColor("jPanel3.border.titleColor"))); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        horainicio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        horainicio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        horainicio.setToolTipText(resourceMap.getString("horainicio.toolTipText")); // NOI18N
        horainicio.setName("horainicio"); // NOI18N
        horainicio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                horainicioMouseExited(evt);
            }
        });
        horainicio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                horainicioFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                horainicioFocusLost(evt);
            }
        });

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        horatermino.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        horatermino.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        horatermino.setToolTipText(resourceMap.getString("horatermino.toolTipText")); // NOI18N
        horatermino.setName("horatermino"); // NOI18N
        horatermino.setNextFocusableComponent(horaentrega);
        horatermino.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                horaterminoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                horaterminoFocusLost(evt);
            }
        });

        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        horaentrega.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        horaentrega.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        horaentrega.setToolTipText(resourceMap.getString("horaentrega.toolTipText")); // NOI18N
        horaentrega.setName("horaentrega"); // NOI18N
        horaentrega.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                horaentregaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                horaentregaFocusLost(evt);
            }
        });

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        ordentrabajo.setText(resourceMap.getString("ordentrabajo.text")); // NOI18N
        ordentrabajo.setName("ordentrabajo"); // NOI18N

        jLabel18.setText(resourceMap.getString("jLabel18.text")); // NOI18N
        jLabel18.setName("jLabel18"); // NOI18N

        clavemaquina.setEditable(false);
        clavemaquina.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        clavemaquina.setText(resourceMap.getString("clavemaquina.text")); // NOI18N
        clavemaquina.setFocusable(false);
        clavemaquina.setName("clavemaquina"); // NOI18N

        btnmaquinas.setIcon(resourceMap.getIcon("btnmaquinas.icon")); // NOI18N
        btnmaquinas.setText(resourceMap.getString("btnmaquinas.text")); // NOI18N
        btnmaquinas.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnmaquinas.setName("btnmaquinas"); // NOI18N
        btnmaquinas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmaquinasActionPerformed(evt);
            }
        });

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        area.setEditable(false);
        area.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        area.setText(resourceMap.getString("area.text")); // NOI18N
        area.setFocusable(false);
        area.setName("area"); // NOI18N

        btnarea.setIcon(resourceMap.getIcon("btnarea.icon")); // NOI18N
        btnarea.setText(resourceMap.getString("btnarea.text")); // NOI18N
        btnarea.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnarea.setName("btnarea"); // NOI18N
        btnarea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnareaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(horainicio, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(horatermino, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(horaentrega, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ordentrabajo, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(area, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                            .addComponent(clavemaquina, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnmaquinas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnarea, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(horainicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(horatermino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(horaentrega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(ordentrabajo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel18)
                        .addComponent(clavemaquina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnmaquinas, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(area, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnarea, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel5.border.title"), 0, 0, resourceMap.getFont("jPanel5.border.titleFont"), resourceMap.getColor("jPanel5.border.titleColor"))); // NOI18N
        jPanel5.setName("jPanel5"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        tipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Preventivo", "Correctivo" }));
        tipo.setName("tipo"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        actividad.setColumns(20);
        actividad.setFont(resourceMap.getFont("actividad.font")); // NOI18N
        actividad.setLineWrap(true);
        actividad.setRows(5);
        actividad.setName("actividad"); // NOI18N
        jScrollPane1.setViewportView(actividad);

        tipo2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mecánico", "Eléctrico", "Mecánico y Eléctrico", " " }));
        tipo2.setName("tipo2"); // NOI18N

        buttonGroup1.add(tmsi);
        tmsi.setSelected(true);
        tmsi.setText(resourceMap.getString("tmsi.text")); // NOI18N
        tmsi.setName("tmsi"); // NOI18N

        buttonGroup1.add(tmno);
        tmno.setText(resourceMap.getString("tmno.text")); // NOI18N
        tmno.setName("tmno"); // NOI18N

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(tipo, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tipo2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 156, Short.MAX_VALUE)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tmno)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tmsi))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tipo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tmsi)
                    .addComponent(tmno)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addContainerGap(109, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        nombre.setText(resourceMap.getString("nombre.text")); // NOI18N
        nombre.setName("nombre"); // NOI18N

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel4.border.title"), 0, 0, null, resourceMap.getColor("jPanel4.border.titleColor"))); // NOI18N
        jPanel4.setName("jPanel4"); // NOI18N

        nombrematerial.setText(resourceMap.getString("nombrematerial.text")); // NOI18N
        nombrematerial.setName("nombrematerial"); // NOI18N

        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        tipo3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Selecciona....\t", "Herramientas", "Refacciones" }));
        tipo3.setName("tipo3"); // NOI18N
        tipo3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipo3ActionPerformed(evt);
            }
        });

        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        jLabel15.setText(resourceMap.getString("jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        cantidad.setText(resourceMap.getString("cantidad.text")); // NOI18N
        cantidad.setName("cantidad"); // NOI18N

        medida.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "UNM\t", "pz", "m", "cm", "mm", "\"", "litros" }));
        medida.setName("medida"); // NOI18N

        herfaltante.setText(resourceMap.getString("herfaltante.text")); // NOI18N
        herfaltante.setName("herfaltante"); // NOI18N

        jLabel16.setText(resourceMap.getString("jLabel16.text")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(nombrematerial)
                    .addComponent(tipo3, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(herfaltante, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(medida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(116, 116, 116))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(tipo3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nombrematerial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(medida, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(herfaltante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(turno, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(idoperador, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnoperador, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nombreoperador, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(nombre))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 822, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                        .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(fecha, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(turno, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addComponent(btnoperador, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(idoperador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3))
                    .addComponent(nombreoperador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:

        //if (Tablaproductos.getCellEditor() != null) {//finaliza el editor
            //Tablaproductos.getCellEditor().stopCellEditing();
        //}
       // int filas=Tablaproductos.getRowCount();
       // int columnas=Tablaproductos.getColumnCount();

        //calcula de nuevo los minutos de arreglo y produccion
        minutosParo();

        if(fecha.getDate()==null||nombre.getText().equals("")||actividad.getText().equals("")||horainicio.getText().equals("")||horaentrega.getText().equals("")){
            String err="VERIFICA HAY CAMPOS VACIOS";
            JOptionPane.showMessageDialog(this, err, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            String tmmuerto="false";
            if(tmsi.isSelected())
                tmmuerto="true";
            if(idoperador.getText().equals(""))
                idoperador.setText("0");

            String senSQL="INSERT INTO mantenimiento(estatus, nombre, turno, tipo2, actividad, fecha, id_ordentrabajo, tipo, horainicio, horafinal, horaentrega, minutosparo, area_empresa, clavemaquina, tiempomuerto,id_operador,tipo3,nombrematerial,cantidad,medida,herfaltante) VALUES ('Act', '"+nombre.getText()+"', '"+(String)turno.getSelectedItem()+"', '"+(String)tipo2.getSelectedItem()+"', '"+actividad.getText()+"', '"+fechainsertarhora.format(fecha.getDate())+"', '"+ordentrabajo.getText()+"', '"+(String)tipo.getSelectedItem()+"', '"+horainicio.getText()+"', '"+horatermino.getText()+"', '"+horaentrega.getText()+"', '"+minparo+"', '"+area.getText()+"', '"+clavemaquina.getText()+"', '"+tmmuerto+"','"+idoperador.getText()+"','"+(String)tipo3.getSelectedItem()+"', '"+nombrematerial.getText()+"', '"+cantidad.getText()+"', '"+(String)medida.getSelectedItem()+"', '"+herfaltante.getText()+"');";
            conexion.modifica_p(senSQL,connj,valor_privilegio);

            salir();
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

    private void btnoperadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnoperadorActionPerformed
        // TODO add your handling code here:
        busca_operador busca_operador = new busca_operador(null,true,connj,"");
        busca_operador.setLocationRelativeTo(this);
        busca_operador.setVisible(true);
        idoperador.setText(busca_operador.getText());//obtiene el valor seleccionado
        busca_operador=null;

        if(idoperador.getText().equals("")){

        }else{
            rs0=null;
            try{
                String senSQL="SELECT * FROM operadores WHERE id_operador='"+idoperador.getText()+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    nombreoperador.setText(rs0.getString("nombre"));
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
    }//GEN-LAST:event_btnoperadorActionPerformed

    private void horainicioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_horainicioFocusGained
        // TODO add your handling code here:
        horainicio.selectAll();
}//GEN-LAST:event_horainicioFocusGained

    private void horaterminoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_horaterminoFocusGained
        // TODO add your handling code here:
        horatermino.selectAll();
}//GEN-LAST:event_horaterminoFocusGained

    private void horainicioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_horainicioFocusLost
        // TODO add your handling code here:
        minutosParo();
}//GEN-LAST:event_horainicioFocusLost

    private void horaentregaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_horaentregaFocusGained
        // TODO add your handling code here:
        horaentrega.selectAll();
    }//GEN-LAST:event_horaentregaFocusGained

    private void horaterminoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_horaterminoFocusLost
        // TODO add your handling code here:
        minutosParo();
}//GEN-LAST:event_horaterminoFocusLost

    private void horaentregaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_horaentregaFocusLost
        // TODO add your handling code here:
        minutosParo();
    }//GEN-LAST:event_horaentregaFocusLost

    private void horainicioMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_horainicioMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_horainicioMouseExited

    private void btnmaquinasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmaquinasActionPerformed
        // TODO add your handling code here:
        busca_maquinas busca_maquinas = new busca_maquinas(null,true,connj,"");
        busca_maquinas.setLocationRelativeTo(this);
        busca_maquinas.setVisible(true);
        clavemaquina.setText(busca_maquinas.getText());
        busca_maquinas=null;
        if(clavemaquina.getText().equals("")){

        }else{
            rs0=null;
            try{
                String senSQL="SELECT * FROM maquinas WHERE clave='"+clavemaquina.getText()+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){

                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
}//GEN-LAST:event_btnmaquinasActionPerformed

    private void btnareaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnareaActionPerformed
        // TODO add your handling code here:
        busca_areas_empresa busca_areas_empresa = new busca_areas_empresa(null,true,connj,"");
        busca_areas_empresa.setLocationRelativeTo(this);
        busca_areas_empresa.setVisible(true);
        area.setText(busca_areas_empresa.getText());
        busca_areas_empresa=null;
    }//GEN-LAST:event_btnareaActionPerformed

    private void tipo3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipo3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tipo3ActionPerformed

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea actividad;
    private javax.swing.JTextField area;
    private javax.swing.JButton btnaceptar;
    private javax.swing.JButton btnarea;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btnmaquinas;
    private javax.swing.JButton btnoperador;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField cantidad;
    private javax.swing.JTextField clavemaquina;
    private com.toedter.calendar.JDateChooser fecha;
    private javax.swing.JTextField herfaltante;
    private javax.swing.JFormattedTextField horaentrega;
    private javax.swing.JFormattedTextField horainicio;
    private javax.swing.JFormattedTextField horatermino;
    private javax.swing.JTextField idoperador;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox medida;
    private javax.swing.JTextField nombre;
    private javax.swing.JTextField nombrematerial;
    private javax.swing.JTextField nombreoperador;
    private javax.swing.JTextField ordentrabajo;
    private javax.swing.JComboBox tipo;
    private javax.swing.JComboBox tipo2;
    private javax.swing.JComboBox tipo3;
    private javax.swing.JRadioButton tmno;
    private javax.swing.JRadioButton tmsi;
    private javax.swing.JComboBox turno;
    // End of variables declaration//GEN-END:variables

}
