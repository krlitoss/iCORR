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
import java.awt.Desktop;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

/**
 *
 * @author IVONNE
 */
public class datos_articulos extends javax.swing.JDialog {
    Connection connj;
    ResultSet rs0=null;
    DecimalFormat fijo1decimales=new DecimalFormat("######0.0");
    DecimalFormat fijo2decimales=new DecimalFormat("######0.00");
    DecimalFormat fijo3decimales=new DecimalFormat("######0.000");
    DecimalFormat fijo4decimales=new DecimalFormat("######0.0000");
    DecimalFormat fijo5decimales=new DecimalFormat("######0.00000");
    DecimalFormat moneda2decimales=new DecimalFormat("$ #,###,##0.00");
    DecimalFormat estandarentero=new DecimalFormat("#,###,##0");

    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    SimpleDateFormat fechamostrarhora = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
    SimpleDateFormat fechamostrarhoracorta = new SimpleDateFormat("dd-MMM-yy HH:mm");
    Double pesom2resis=0.0,pesom2resisreal=0.0;
    DefaultTableModel modelot1=null;
    DefaultTableModel modelot2=null;
    DefaultTableModel modelot3=null;
    Double valormoneda=0.0;
    private Properties conf;

    String valor_privilegio="1";

    /** Creates new form datos_usuarios */
    public datos_articulos(java.awt.Frame parent, boolean modal,Connection conn,String clavemodifica,String ver,String valor_privilegio_t) {
        super(parent, modal);
        initComponents();
        connj=conn;
        valor_privilegio=valor_privilegio_t;
        conf=conexion.archivoInicial();
        modelot1=(DefaultTableModel) Tablamaquinas.getModel();
        Tablamaquinas.setModel(modelot1);
        modelot2=(DefaultTableModel) Tablatintas.getModel();
        Tablatintas.setModel(modelot2);
        modelot3=(DefaultTableModel) Tablajuegos.getModel();
        Tablajuegos.setModel(modelot3);
        ajusteTabladatos();
        disenos_flautas();
        consultamodifica(clavemodifica);
        if(!ver.equals("")){
            btnaceptar.setEnabled(false);
            preciomillar.setText("");
        }

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
        Tablamaquinas.getColumnModel().getColumn(0).setPreferredWidth(70);
        Tablamaquinas.getColumnModel().getColumn(1).setPreferredWidth(180);
        Tablamaquinas.getColumnModel().getColumn(2).setPreferredWidth(60);
        Tablamaquinas.getColumnModel().getColumn(3).setPreferredWidth(65);
        Tablamaquinas.getColumnModel().getColumn(4).setPreferredWidth(60);

        Tablatintas.getColumnModel().getColumn(0).setPreferredWidth(100);
        Tablatintas.getColumnModel().getColumn(1).setPreferredWidth(280);
        Tablatintas.getColumnModel().getColumn(2).setPreferredWidth(70);
        Tablatintas.getColumnModel().getColumn(0).setCellRenderer(new DoubleRenderert());//formato para doubles
        Tablatintas.getColumnModel().getColumn(1).setCellRenderer(new DoubleRenderert());
        Tablatintas.getColumnModel().getColumn(2).setCellRenderer(new DoubleRenderert());

        Tablajuegos.getColumnModel().getColumn(0).setPreferredWidth(100);
        Tablajuegos.getColumnModel().getColumn(1).setPreferredWidth(280);
        Tablajuegos.getColumnModel().getColumn(2).setPreferredWidth(70);
        Tablajuegos.getColumnModel().getColumn(0).setCellRenderer(new DoubleRendererj());//formato para doubles
        Tablajuegos.getColumnModel().getColumn(1).setCellRenderer(new DoubleRendererj());
        Tablajuegos.getColumnModel().getColumn(2).setCellRenderer(new DoubleRendererj());
    }

    public class DoubleRenderert extends SubstanceDefaultTableCellRenderer {//modificar los valores de doubles y formatearlos

            public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
                JLabel rend = (JLabel)super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                String text = ""+value;

                if(column==2){
                    text = fijo4decimales.format( Double.parseDouble(""+value) );
                    modelot2.setValueAt(Double.parseDouble(text), row, column);
                    rend.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                }

                rend.setText( text );
                return rend;
            }

    }
    public class DoubleRendererj extends SubstanceDefaultTableCellRenderer {//modificar los valores de doubles y formatearlos

            public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
                JLabel rend = (JLabel)super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                String text = ""+value;

                if(column==2){
                    text = fijo1decimales.format( Double.parseDouble(""+value) );
                    modelot3.setValueAt(Double.parseDouble(text), row, column);
                    rend.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                }

                rend.setText( text );
                return rend;
            }

    }

    public void consultamodifica(String clavemodifica){
        if(clavemodifica.equals("")){
            /**si la clave esta vacia significa que es un registro nuevo*/
        }else{
            clave.setEditable(false);
            
            
            descripcion.requestFocus();
            destino.requestFocus();
            String claveart="";
            rs0=null;
            try{
                String senSQL="SELECT articulos.*,resistencias.corrugado,resistencias.color,resistencias.pesom2,resistencias.pesom2real,monedas.valor FROM (articulos LEFT JOIN resistencias ON articulos.claveresistencia=resistencias.clave) LEFT JOIN monedas ON articulos.id_moneda=monedas.id_moneda WHERE articulos.clavearticulo='"+clavemodifica+"'";
                rs0=conexion.consulta(senSQL,connj);
                while(rs0.next()){
                    id.setText(rs0.getString("id_articulo"));
                    claveart=rs0.getString("clavearticulo");
                    clave.setText(claveart);
                    descripcion.setText(rs0.getString("articulo"));
                    clavecliente.setText(rs0.getString("id_clientes"));
                    destino.setText(rs0.getString("destino"));
                    diseno.setSelectedItem(rs0.getString("diseno"));
                    aplicaciones_especiales.setSelectedItem(rs0.getString("aplicaciones_especiales")); //Campo agregado 2017
                    cobb.setText (rs0.getString("cobb"));  //Campo agregado 2017
                    resistencia.setText(rs0.getString("claveresistencia"));
                    corrugado.setText(rs0.getString("corrugado"));
                    color.setText(rs0.getString("color"));
                    tipo_flauta.setSelectedItem(rs0.getString("flauta_art"));
                    inlargo.setText(rs0.getString("inlargo"));
                    inancho.setText(rs0.getString("inancho"));
                    inalto.setText(rs0.getString("inalto"));
                    largo.setText(rs0.getString("largo"));
                    ancho.setText(rs0.getString("ancho"));
                    piezas.setText(rs0.getString("piezas"));
                    scores.setText(rs0.getString("scores"));
                    Double pesokgt=rs0.getDouble("kg");
                    pesokg.setText(fijo3decimales.format(pesokgt));
                    pesokgreal.setText(rs0.getString("kgreal"));
                    aream2.setText(rs0.getString("m2"));
                    Double precim=rs0.getDouble("preciomillar");
                    preciomillar.setText(fijo2decimales.format(precim));
                    moneda.setText(rs0.getString("id_moneda"));
                    valormoneda=rs0.getDouble("valor");
                    pk.setText(moneda2decimales.format(((precim*valormoneda)/1000)/pesokgt));
                    Date fuv=rs0.getTimestamp("fechaultima");
                    if(!(""+fuv).equals("null"))
                        fechaventa.setText(fechamostrarhoracorta.format(fuv));

                    cantventa.setText(estandarentero.format(rs0.getDouble("cantidadultima")));
                    Date fact=rs0.getTimestamp("fechaactualizado");
                    if(!(""+fact).equals("null"))
                        actualizado.setText(fechamostrarhoracorta.format(fact));
                    
                    obsoleto.setSelectedItem(rs0.getString("obsoleto"));
                    pesom2resis=rs0.getDouble("pesom2");
                    pesom2resisreal=rs0.getDouble("pesom2real");

                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

            //selecciona el suaje
            rs0=null;
            try{
                String senSQL="SELECT suajes.* FROM suajes WHERE (suajes.clavearticulo='"+claveart+"' AND suajes.obsoleto='No') ORDER BY suajes.fecha";
                rs0=conexion.consulta(senSQL,connj);
                while(rs0.next()){
                    suaje.setText(rs0.getString("clave_suaje"));
                }
                if(rs0!=null) {     rs0.close();       }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

            //selecciona el suaje
            rs0=null;
            try{
                String senSQL="SELECT grabados.* FROM grabados WHERE (grabados.clavearticulo='"+claveart+"' AND grabados.obsoleto='No') ORDER BY grabados.fecha";
                rs0=conexion.consulta(senSQL,connj);
                while(rs0.next()){
                    grabado.setText(rs0.getString("clave_grabado"));
                }
                if(rs0!=null) {     rs0.close();       }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }


            //selecciona las maquinas
            rs0=null;
            try{
                String senSQL="SELECT articulos_maquinas.clave,articulos_maquinas.arreglo,articulos_maquinas.velocidad,maquinas.descripcion,maquinas.costokg FROM articulos_maquinas LEFT JOIN maquinas ON articulos_maquinas.clave=maquinas.clave WHERE articulos_maquinas.clavearticulo='"+claveart+"'";
                rs0=conexion.consulta(senSQL,connj);
                while(rs0.next()){
                    Object datos[]={rs0.getString("clave"),rs0.getString("descripcion"),rs0.getInt("arreglo"),rs0.getInt("velocidad"),rs0.getDouble("costokg")};
                    modelot1.addRow(datos);
                }
                if(rs0!=null) { rs0.close();}
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

            //selecciona los datos de tintas
            rs0=null;
            try{
                String senSQL="SELECT articulos_tintas.*,productos.descripcion FROM articulos_tintas LEFT JOIN productos ON articulos_tintas.claveproducto=productos.clave WHERE articulos_tintas.clavearticulo='"+claveart+"'";
                rs0=conexion.consulta(senSQL,connj);
                while(rs0.next()){
                    Object datos[]={rs0.getString("claveproducto"),rs0.getString("descripcion"),rs0.getDouble("porcentaje"),rs0.getDouble("gramos")};
                    modelot2.addRow(datos);
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
            //selecciona los datos de ajuego
            rs0=null;
            try{
                String senSQL="SELECT articulos_juegos.*,articulos.articulo FROM articulos_juegos LEFT JOIN articulos ON articulos_juegos.clavearticulo1=articulos.clavearticulo WHERE articulos_juegos.clavearticulo='"+claveart+"'";
                rs0=conexion.consulta(senSQL,connj);
                while(rs0.next()){
                    Object datos[]={rs0.getString("clavearticulo1"),rs0.getString("articulo"),rs0.getDouble("piezas")};
                    modelot3.addRow(datos);
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
    }
    public void disenos_flautas(){
        
        try{
            rs0=null;
            String senSQL="SELECT * FROM disenos_cajas";
            rs0=conexion.consulta(senSQL,connj);
            while(rs0.next()){
                diseno.addItem(rs0.getString("descripcion"));
            }
            if(rs0!=null) { rs0.close(); }
            
            
            rs0=null;
            senSQL="SELECT * FROM flautas";
            rs0=conexion.consulta(senSQL,connj);
            while(rs0.next()){
                tipo_flauta.addItem(rs0.getString("tipo_flauta"));
            }
            if(rs0!=null) { rs0.close(); }
            
            /*modificacion para insertar aplicaciones especiales para lamina 2017*/
            rs0=null;
            senSQL="SELECT * FROM aplicaciones_cajas";
            rs0=conexion.consulta(senSQL,connj);
            while(rs0.next()){
                aplicaciones_especiales.addItem(rs0.getString("descripcion"));
            }
            if(rs0!=null) { rs0.close(); }
            
            

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
        jButton6 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        id = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        clavecliente = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        descripcion = new javax.swing.JTextField();
        clave = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        destino = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        diseno = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        resistencia = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        corrugado = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        color = new javax.swing.JTextField();
        tipo_flauta = new javax.swing.JComboBox();
        jLabel31 = new javax.swing.JLabel();
        aplicaciones_especiales = new javax.swing.JComboBox();
        jLabel32 = new javax.swing.JLabel();
        cobb = new javax.swing.JFormattedTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        suaje = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        grabado = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        preciomillar = new javax.swing.JFormattedTextField();
        jLabel20 = new javax.swing.JLabel();
        moneda = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        largo = new javax.swing.JFormattedTextField();
        jLabel15 = new javax.swing.JLabel();
        ancho = new javax.swing.JFormattedTextField();
        jLabel16 = new javax.swing.JLabel();
        piezas = new javax.swing.JFormattedTextField();
        jLabel17 = new javax.swing.JLabel();
        scores = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        inlargo = new javax.swing.JFormattedTextField();
        jLabel11 = new javax.swing.JLabel();
        inancho = new javax.swing.JFormattedTextField();
        jLabel12 = new javax.swing.JLabel();
        inalto = new javax.swing.JFormattedTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        cantventa = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        aream2 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        pesokg = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        pesokgreal = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        fechaventa = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        obsoleto = new javax.swing.JComboBox();
        jLabel27 = new javax.swing.JLabel();
        actualizado = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        pk = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tablamaquinas = new javax.swing.JTable();
        btnmenosmaq = new javax.swing.JButton();
        btnmasmaq = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        Tablatintas = new javax.swing.JTable();
        btnmenostin = new javax.swing.JButton();
        btnmastin = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        Tablajuegos = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_articulos.class);
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

        jButton6.setIcon(resourceMap.getIcon("jButton6.icon")); // NOI18N
        jButton6.setText(resourceMap.getString("jButton6.text")); // NOI18N
        jButton6.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton6.setName("jButton6"); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(220, 220, 220)
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel13.setFont(resourceMap.getFont("jLabel13.font")); // NOI18N
        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        jPanel2.setBackground(resourceMap.getColor("jPanel2.background")); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        id.setEditable(false);
        id.setFont(resourceMap.getFont("id.font")); // NOI18N
        id.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id.setText(resourceMap.getString("id.text")); // NOI18N
        id.setFocusable(false);
        id.setName("id"); // NOI18N

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        clavecliente.setEditable(false);
        clavecliente.setFont(resourceMap.getFont("clavecliente.font")); // NOI18N
        clavecliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        clavecliente.setText(resourceMap.getString("clavecliente.text")); // NOI18N
        clavecliente.setFocusable(false);
        clavecliente.setName("clavecliente"); // NOI18N

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        descripcion.setFont(resourceMap.getFont("descripcion.font")); // NOI18N
        descripcion.setName("descripcion"); // NOI18N
        descripcion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                descripcionFocusGained(evt);
            }
        });

        clave.setFont(resourceMap.getFont("clave.font")); // NOI18N
        clave.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        clave.setText(resourceMap.getString("clave.text")); // NOI18N
        clave.setName("clave"); // NOI18N
        clave.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                claveFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                claveFocusLost(evt);
            }
        });

        jButton3.setIcon(resourceMap.getIcon("jButton3.icon")); // NOI18N
        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        destino.setFont(resourceMap.getFont("destino.font")); // NOI18N
        destino.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        destino.setName("destino"); // NOI18N
        destino.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                destinoFocusGained(evt);
            }
        });

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText(resourceMap.getString("jLabel29.text")); // NOI18N
        jLabel29.setName("jLabel29"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(destino, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clave, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clavecliente, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(clave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(clavecliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(destino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29))
                .addContainerGap())
        );

        jPanel8.setName("jPanel8"); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel3.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), resourceMap.getColor("jPanel3.border.titleColor"))); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        diseno.setName("diseno"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        resistencia.setEditable(false);
        resistencia.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        resistencia.setText(resourceMap.getString("resistencia.text")); // NOI18N
        resistencia.setFocusable(false);
        resistencia.setName("resistencia"); // NOI18N

        jButton1.setIcon(resourceMap.getIcon("jButton1.icon")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        corrugado.setEditable(false);
        corrugado.setText(resourceMap.getString("corrugado.text")); // NOI18N
        corrugado.setFocusable(false);
        corrugado.setName("corrugado"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        color.setEditable(false);
        color.setText(resourceMap.getString("color.text")); // NOI18N
        color.setFocusable(false);
        color.setName("color"); // NOI18N

        tipo_flauta.setName("tipo_flauta"); // NOI18N
        tipo_flauta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipo_flautaActionPerformed(evt);
            }
        });

        jLabel31.setName("jLabel31"); // NOI18N

        aplicaciones_especiales.setName("aplicaciones_especiales"); // NOI18N
        aplicaciones_especiales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aplicaciones_especialesActionPerformed(evt);
            }
        });

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel32.setName("jLabel32"); // NOI18N

        cobb.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        cobb.setText(resourceMap.getString("cobb.text")); // NOI18N
        cobb.setName("cobb"); // NOI18N
        cobb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cobbMouseClicked(evt);
            }
        });
        cobb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cobbActionPerformed(evt);
            }
        });
        cobb.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cobbFocusGained(evt);
            }
        });

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel33.setName("jLabel33"); // NOI18N

        jLabel34.setText(resourceMap.getString("jLabel34.text")); // NOI18N
        jLabel34.setName("jLabel34"); // NOI18N

        jLabel35.setText(resourceMap.getString("jLabel35.text")); // NOI18N
        jLabel35.setName("jLabel35"); // NOI18N

        jLabel36.setText(resourceMap.getString("jLabel36.text")); // NOI18N
        jLabel36.setName("jLabel36"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(color, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tipo_flauta, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(diseno, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(corrugado)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(resistencia)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(aplicaciones_especiales, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(cobb, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(diseno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(resistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(corrugado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(color, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tipo_flauta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(jLabel35)
                    .addComponent(aplicaciones_especiales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel33)
                    .addComponent(jLabel34)
                    .addComponent(cobb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel6.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), resourceMap.getColor("jPanel6.border.titleColor"))); // NOI18N
        jPanel6.setName("jPanel6"); // NOI18N

        jLabel18.setText(resourceMap.getString("jLabel18.text")); // NOI18N
        jLabel18.setName("jLabel18"); // NOI18N

        suaje.setEditable(false);
        suaje.setText(resourceMap.getString("suaje.text")); // NOI18N
        suaje.setFocusable(false);
        suaje.setName("suaje"); // NOI18N
        suaje.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                suajeFocusGained(evt);
            }
        });

        jLabel19.setText(resourceMap.getString("jLabel19.text")); // NOI18N
        jLabel19.setName("jLabel19"); // NOI18N

        grabado.setEditable(false);
        grabado.setText(resourceMap.getString("grabado.text")); // NOI18N
        grabado.setFocusable(false);
        grabado.setName("grabado"); // NOI18N
        grabado.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                grabadoFocusGained(evt);
            }
        });

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        preciomillar.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.00"))));
        preciomillar.setName("preciomillar"); // NOI18N
        preciomillar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                preciomillarMouseClicked(evt);
            }
        });
        preciomillar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                preciomillarFocusGained(evt);
            }
        });

        jLabel20.setText(resourceMap.getString("jLabel20.text")); // NOI18N
        jLabel20.setName("jLabel20"); // NOI18N

        moneda.setEditable(false);
        moneda.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        moneda.setText(resourceMap.getString("moneda.text")); // NOI18N
        moneda.setFocusable(false);
        moneda.setName("moneda"); // NOI18N

        jButton2.setIcon(resourceMap.getIcon("jButton2.icon")); // NOI18N
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(preciomillar)
                    .addComponent(suaje, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(grabado, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(moneda)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(preciomillar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel20)
                        .addComponent(moneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(suaje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(grabado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel5.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), resourceMap.getColor("jPanel5.border.titleColor"))); // NOI18N
        jPanel5.setName("jPanel5"); // NOI18N

        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        largo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.0"))));
        largo.setText(resourceMap.getString("largo.text")); // NOI18N
        largo.setName("largo"); // NOI18N
        largo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                largoFocusGained(evt);
            }
        });

        jLabel15.setText(resourceMap.getString("jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        ancho.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.0"))));
        ancho.setText(resourceMap.getString("ancho.text")); // NOI18N
        ancho.setName("ancho"); // NOI18N
        ancho.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                anchoFocusGained(evt);
            }
        });

        jLabel16.setText(resourceMap.getString("jLabel16.text")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N

        piezas.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.0"))));
        piezas.setText(resourceMap.getString("piezas.text")); // NOI18N
        piezas.setName("piezas"); // NOI18N
        piezas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                piezasFocusGained(evt);
            }
        });

        jLabel17.setText(resourceMap.getString("jLabel17.text")); // NOI18N
        jLabel17.setName("jLabel17"); // NOI18N

        scores.setText(resourceMap.getString("scores.text")); // NOI18N
        scores.setName("scores"); // NOI18N
        scores.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                scoresFocusGained(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(largo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ancho, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scores, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(piezas, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(largo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(ancho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(piezas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(scores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel4.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), resourceMap.getColor("jPanel4.border.titleColor"))); // NOI18N
        jPanel4.setName("jPanel4"); // NOI18N

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        inlargo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.0"))));
        inlargo.setText(resourceMap.getString("inlargo.text")); // NOI18N
        inlargo.setName("inlargo"); // NOI18N
        inlargo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                inlargoFocusGained(evt);
            }
        });

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        inancho.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.0"))));
        inancho.setText(resourceMap.getString("inancho.text")); // NOI18N
        inancho.setName("inancho"); // NOI18N
        inancho.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                inanchoFocusGained(evt);
            }
        });

        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        inalto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.0"))));
        inalto.setText(resourceMap.getString("inalto.text")); // NOI18N
        inalto.setName("inalto"); // NOI18N
        inalto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                inaltoFocusGained(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inlargo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inancho, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inalto, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(inlargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(inancho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(inalto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel9.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), resourceMap.getColor("jPanel9.border.titleColor"))); // NOI18N
        jPanel9.setName("jPanel9"); // NOI18N

        jLabel21.setText(resourceMap.getString("jLabel21.text")); // NOI18N
        jLabel21.setName("jLabel21"); // NOI18N

        cantventa.setEditable(false);
        cantventa.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cantventa.setText(resourceMap.getString("cantventa.text")); // NOI18N
        cantventa.setFocusable(false);
        cantventa.setName("cantventa"); // NOI18N

        jLabel22.setText(resourceMap.getString("jLabel22.text")); // NOI18N
        jLabel22.setName("jLabel22"); // NOI18N

        aream2.setEditable(false);
        aream2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        aream2.setText(resourceMap.getString("aream2.text")); // NOI18N
        aream2.setFocusable(false);
        aream2.setName("aream2"); // NOI18N

        jLabel23.setText(resourceMap.getString("jLabel23.text")); // NOI18N
        jLabel23.setName("jLabel23"); // NOI18N

        pesokg.setEditable(false);
        pesokg.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pesokg.setText(resourceMap.getString("pesokg.text")); // NOI18N
        pesokg.setFocusable(false);
        pesokg.setName("pesokg"); // NOI18N

        jLabel24.setText(resourceMap.getString("jLabel24.text")); // NOI18N
        jLabel24.setName("jLabel24"); // NOI18N

        pesokgreal.setEditable(false);
        pesokgreal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pesokgreal.setText(resourceMap.getString("pesokgreal.text")); // NOI18N
        pesokgreal.setFocusable(false);
        pesokgreal.setName("pesokgreal"); // NOI18N

        jLabel25.setText(resourceMap.getString("jLabel25.text")); // NOI18N
        jLabel25.setName("jLabel25"); // NOI18N

        fechaventa.setEditable(false);
        fechaventa.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fechaventa.setText(resourceMap.getString("fechaventa.text")); // NOI18N
        fechaventa.setFocusable(false);
        fechaventa.setName("fechaventa"); // NOI18N

        jLabel26.setText(resourceMap.getString("jLabel26.text")); // NOI18N
        jLabel26.setName("jLabel26"); // NOI18N

        obsoleto.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No", "Si" }));
        obsoleto.setName("obsoleto"); // NOI18N

        jLabel27.setText(resourceMap.getString("jLabel27.text")); // NOI18N
        jLabel27.setName("jLabel27"); // NOI18N

        actualizado.setEditable(false);
        actualizado.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        actualizado.setText(resourceMap.getString("actualizado.text")); // NOI18N
        actualizado.setFocusable(false);
        actualizado.setName("actualizado"); // NOI18N

        jLabel28.setText(resourceMap.getString("jLabel28.text")); // NOI18N
        jLabel28.setName("jLabel28"); // NOI18N

        pk.setEditable(false);
        pk.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pk.setText(resourceMap.getString("pk.text")); // NOI18N
        pk.setFocusable(false);
        pk.setName("pk"); // NOI18N

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(obsoleto, 0, 120, Short.MAX_VALUE)
                    .addComponent(aream2, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(pesokg, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(pesokgreal, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(fechaventa, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(cantventa, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(actualizado, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(pk)))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(obsoleto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(aream2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(pesokg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(pesokgreal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(fechaventa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(cantventa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(actualizado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(pk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanel7.setName("jPanel7"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        Tablamaquinas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Clave (F7)", "Descripción", "Arreglo", "Velocidad", "Costo Kg"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tablamaquinas.setToolTipText(resourceMap.getString("Tablamaquinas.toolTipText")); // NOI18N
        Tablamaquinas.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tablamaquinas.setColumnSelectionAllowed(true);
        Tablamaquinas.setName("Tablamaquinas"); // NOI18N
        Tablamaquinas.setRowHeight(22);
        Tablamaquinas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        Tablamaquinas.getTableHeader().setReorderingAllowed(false);
        Tablamaquinas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TablamaquinasKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(Tablamaquinas);
        Tablamaquinas.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (Tablamaquinas.getColumnModel().getColumnCount() > 0) {
            Tablamaquinas.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("Tablamaquinas.columnModel.title0")); // NOI18N
            Tablamaquinas.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("Tablamaquinas.columnModel.title1")); // NOI18N
            Tablamaquinas.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("Tablamaquinas.columnModel.title2")); // NOI18N
            Tablamaquinas.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("Tablamaquinas.columnModel.title3")); // NOI18N
            Tablamaquinas.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("Tablamaquinas.columnModel.title4")); // NOI18N
        }

        btnmenosmaq.setIcon(resourceMap.getIcon("btnmenosmaq.icon")); // NOI18N
        btnmenosmaq.setText(resourceMap.getString("btnmenosmaq.text")); // NOI18N
        btnmenosmaq.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnmenosmaq.setName("btnmenosmaq"); // NOI18N
        btnmenosmaq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmenosmaqActionPerformed(evt);
            }
        });

        btnmasmaq.setIcon(resourceMap.getIcon("btnmasmaq.icon")); // NOI18N
        btnmasmaq.setText(resourceMap.getString("btnmasmaq.text")); // NOI18N
        btnmasmaq.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnmasmaq.setName("btnmasmaq"); // NOI18N
        btnmasmaq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmasmaqActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 597, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(btnmasmaq)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnmenosmaq)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnmenosmaq)
                    .addComponent(btnmasmaq))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel7.TabConstraints.tabTitle"), resourceMap.getIcon("jPanel7.TabConstraints.tabIcon"), jPanel7); // NOI18N

        jPanel10.setName("jPanel10"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        Tablatintas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Clave (F7)", "Descripción", "% Uso", "Gramos"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tablatintas.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tablatintas.setColumnSelectionAllowed(true);
        Tablatintas.setName("Tablatintas"); // NOI18N
        Tablatintas.setRowHeight(22);
        Tablatintas.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        Tablatintas.getTableHeader().setReorderingAllowed(false);
        Tablatintas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TablatintasKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(Tablatintas);
        Tablatintas.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (Tablatintas.getColumnModel().getColumnCount() > 0) {
            Tablatintas.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("Tablatintas.columnModel.title0")); // NOI18N
            Tablatintas.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("Tablatintas.columnModel.title1")); // NOI18N
            Tablatintas.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("Tablatintas.columnModel.title2")); // NOI18N
            Tablatintas.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("Tablatintas.columnModel.title3")); // NOI18N
        }

        btnmenostin.setIcon(resourceMap.getIcon("btnmenostin.icon")); // NOI18N
        btnmenostin.setText(resourceMap.getString("btnmenostin.text")); // NOI18N
        btnmenostin.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnmenostin.setName("btnmenostin"); // NOI18N
        btnmenostin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmenostinActionPerformed(evt);
            }
        });

        btnmastin.setIcon(resourceMap.getIcon("btnmastin.icon")); // NOI18N
        btnmastin.setText(resourceMap.getString("btnmastin.text")); // NOI18N
        btnmastin.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnmastin.setName("btnmastin"); // NOI18N
        btnmastin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmastinActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 597, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(btnmastin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnmenostin)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnmenostin)
                    .addComponent(btnmastin))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel10.TabConstraints.tabTitle"), resourceMap.getIcon("jPanel10.TabConstraints.tabIcon"), jPanel10); // NOI18N

        jPanel11.setName("jPanel11"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        Tablajuegos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Clave Articulo", "Articulo", "Piezas x Juego"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tablajuegos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tablajuegos.setColumnSelectionAllowed(true);
        Tablajuegos.setName("Tablajuegos"); // NOI18N
        Tablajuegos.setRowHeight(22);
        Tablajuegos.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        Tablajuegos.getTableHeader().setReorderingAllowed(false);
        Tablajuegos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TablajuegosKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(Tablajuegos);

        jButton4.setIcon(resourceMap.getIcon("jButton4.icon")); // NOI18N
        jButton4.setText(resourceMap.getString("jButton4.text")); // NOI18N
        jButton4.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton4.setName("jButton4"); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setIcon(resourceMap.getIcon("jButton5.icon")); // NOI18N
        jButton5.setText(resourceMap.getString("jButton5.text")); // NOI18N
        jButton5.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton5.setName("jButton5"); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 597, Short.MAX_VALUE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel11.TabConstraints.tabTitle"), resourceMap.getIcon("jPanel11.TabConstraints.tabIcon"), jPanel11); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTabbedPane1))
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTabbedPane1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:
        clave.setText(clave.getText().toUpperCase());
        //revisa y guarda datos de las maquinas y/o procesos
        if (Tablamaquinas.getCellEditor() != null) {//finaliza el editor
            Tablamaquinas.getCellEditor().stopCellEditing();
        }
        if (Tablatintas.getCellEditor() != null) {//finaliza el editor
            Tablatintas.getCellEditor().stopCellEditing();
        }
        if (Tablajuegos.getCellEditor() != null) {//finaliza el editor
            Tablajuegos.getCellEditor().stopCellEditing();
        }

        int filasm=Tablamaquinas.getRowCount();
        int columnasm=Tablamaquinas.getColumnCount();
        int camposvaciosmaquinas=0;
        for(int i=0;i<=(filasm-1);i=i+1){
            for(int j=0;j<=(columnasm-1);j=j+1){
                if(modelot1.getValueAt(i, j) == null||modelot1.getValueAt(i, j).equals("")){
                    camposvaciosmaquinas=1;
                }
            }
        }/**fin de revisar los campos vacios*/
        int filast=Tablatintas.getRowCount();
        int columnast=Tablatintas.getColumnCount();
        int camposvaciostintas=0;
        int notintas=0;
        for(int i=0;i<=(filast-1);i=i+1){
            for(int j=0;j<=(columnast-1);j=j+1){
                if(modelot2.getValueAt(i, j) == null||modelot2.getValueAt(i, j).equals("")){
                    camposvaciostintas=1;
                }
            }
            notintas++;
        }/**fin de revisar los campos vacios*/
        int filasj=Tablajuegos.getRowCount();
        int columnasj=Tablajuegos.getColumnCount();
        int camposvaciosjuegos=0;
        for(int i=0;i<=(filasj-1);i=i+1){
            for(int j=0;j<=(columnasj-1);j=j+1){
                if(modelot3.getValueAt(i, j) == null||modelot3.getValueAt(i, j).equals("")){
                    camposvaciosjuegos=1;
                }
            }
        }/**fin de revisar los campos vacios*/



        if(clave.getText().equals("")||descripcion.getText().equals("")||clavecliente.getText().equals("")||cobb.getText().equals("")||resistencia.getText().equals("")||inlargo.getText().equals("")||inancho.getText().equals("")||inalto.getText().equals("")||largo.getText().equals("")||ancho.getText().equals("")||piezas.getText().equals("")||preciomillar.getText().equals("")||moneda.getText().equals("")||camposvaciosmaquinas==1||camposvaciostintas==1||camposvaciosjuegos==1){
            String err="VERIFICA HAY CAMPOS VACIOS";
            if(camposvaciosmaquinas==1)
                err="LA TABLA DE MAQUINAS O PROCESOS TIENE CAMPOS VACIOS";
            if(camposvaciostintas==1)
                err="LA TABLA DE TINTAS TIENE CAMPOS VACIOS";
            if(camposvaciosjuegos==1)
                err="LA TABLA DE JUEGOS TIENE CAMPOS VACIOS";
            
            JOptionPane.showMessageDialog(this, err, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            //revisa que la flauta sea igual al tipo de corrugado
            int errorflautas=0;
            String flauta_t=""+tipo_flauta.getSelectedItem();
            if(corrugado.getText().toUpperCase().equals("SENCILLO")){
                if(flauta_t.length()!=1)
                    errorflautas=1;
            }else{
                if(flauta_t.length()!=2)
                    errorflautas=1;
            }
            if(errorflautas==1){
                JOptionPane.showMessageDialog(this, "LA FLAUTA NO CORRESPONDE CON EL TIPO DE CORRUGADO", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }else{

                //calcula el area y peso del articulo
                int area1=(int) (Double.parseDouble(largo.getText())*Double.parseDouble(ancho.getText()) * 10);
                Double area=(Double.parseDouble(""+area1)/100000/Double.parseDouble(piezas.getText()));
                aream2.setText(fijo4decimales.format(area));

                Double pesom2=((pesom2resis*area));//rencondea hacia arriba solo 3 decimales
                Double pesom2r=((pesom2resisreal*area));

                pesokg.setText(fijo3decimales.format(pesom2));
                pesokgreal.setText(fijo3decimales.format(pesom2r));

                pk.setText(moneda2decimales.format( ((Double.parseDouble(preciomillar.getText())*valormoneda)/1000)/Double.parseDouble(pesokg.getText()) ) );
                //fin de calcular los datos de peso y area del articulo

                int respuesta=JOptionPane.showConfirmDialog(this,"ESTA SEGURO QUE DESEA GUARDAR!!\n<html><font size=5 color=#DC143C><b>Articulo: </b>"+clave.getText()+"<br><b>Precio Millar: </b>"+moneda2decimales.format(Double.parseDouble(preciomillar.getText()))+"<br><b>P.K.: </b>"+pk.getText()+"<br><b>Peso (kg): </b>"+pesokg.getText()+"<br><b>Area (m2): </b>"+aream2.getText()+"<br></font></html>"," I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION);

                if (respuesta == JOptionPane.YES_OPTION) {

                    String senSQL="UPDATE articulos SET clavearticulo='"+clave.getText()+"', clave_prodserv='24112500', articulo='"+descripcion.getText()+"', id_clientes='"+clavecliente.getText()+"', destino='"+destino.getText()+"', diseno='"+(String)diseno.getSelectedItem()+"', aplicaciones_especiales='"+(String)aplicaciones_especiales.getSelectedItem()+"', cobb='"+cobb.getText()+"', claveresistencia='"+resistencia.getText()+"', inlargo='"+inlargo.getText()+"', inancho='"+inancho.getText()+"', inalto='"+inalto.getText()+"', largo='"+largo.getText()+"', ancho='"+ancho.getText()+"', piezas='"+piezas.getText()+"', scores='"+scores.getText()+"', kg='"+pesokg.getText()+"', kgreal='"+pesokgreal.getText()+"', m2='"+aream2.getText()+"', preciomillar='"+preciomillar.getText()+"', id_moneda='"+moneda.getText()+"', fechaactualizado='"+fechainsertarhora.format(new Date())+"', obsoleto='"+(String)obsoleto.getSelectedItem()+"', tintas='"+notintas+"',flauta_art='"+(String)tipo_flauta.getSelectedItem()+"' WHERE id_articulo='"+id.getText()+"';";
                    if(id.getText().equals("")){
                        senSQL="INSERT INTO articulos(clavearticulo, clave_prodserv, articulo, id_clientes, diseno, aplicaciones_especiales, cobb, claveresistencia, inlargo, inancho, inalto, largo, ancho, piezas, scores, kg, kgreal, m2, preciomillar, id_moneda, fechaultima, cantidadultima, fechaactualizado, obsoleto,tintas,flauta_art,destino,clave_unidadmedida) VALUES ('"+clave.getText()+"', '24112500', '"+descripcion.getText()+"', '"+clavecliente.getText()+"', '"+(String)diseno.getSelectedItem()+"', '"+(String)aplicaciones_especiales.getSelectedItem()+"', '"+cobb.getText()+"',  '"+resistencia.getText()+"', '"+inlargo.getText()+"', '"+inancho.getText()+"', '"+inalto.getText()+"', '"+largo.getText()+"', '"+ancho.getText()+"', '"+piezas.getText()+"', '"+scores.getText()+"', '"+pesokg.getText()+"', '"+pesokgreal.getText()+"', '"+aream2.getText()+"', '"+preciomillar.getText()+"', '"+moneda.getText()+"', '"+fechainsertarhora.format(new Date())+"', '"+cantventa.getText()+"', '"+fechainsertarhora.format(new Date())+"', '"+(String)obsoleto.getSelectedItem()+"', '"+notintas+"', '"+(String)tipo_flauta.getSelectedItem()+"','"+destino.getText()+"', 'H87'  );";
                    }
                    conexion.modifica_p(senSQL,connj,valor_privilegio);

                    //guardamos los datos de las maquinas
                    String senSQLmaquinadel="DELETE FROM articulos_maquinas WHERE clavearticulo='"+clave.getText()+"';";
                    conexion.modificamov_p(senSQLmaquinadel,connj,valor_privilegio);
                    for(int i=0;i<=(filasm-1);i=i+1){
                        String senSQLmaquina="INSERT INTO articulos_maquinas(clavearticulo, clave, arreglo, velocidad) VALUES ('"+clave.getText()+"', '"+modelot1.getValueAt(i, 0)+"', '"+modelot1.getValueAt(i, 2)+"', '"+modelot1.getValueAt(i, 3)+"');";
                        conexion.modificamov_p(senSQLmaquina,connj,valor_privilegio);
                    }/**fin de revisar los campos vacios*/

                    //guardamos los datos de las tintas
                    String senSQLtintadel="DELETE FROM articulos_tintas WHERE clavearticulo='"+clave.getText()+"';";
                    conexion.modificamov_p(senSQLtintadel,connj,valor_privilegio);
                    for(int i=0;i<=(filast-1);i=i+1){
                        Double porm2=Double.parseDouble(aream2.getText())*Double.parseDouble(""+modelot2.getValueAt(i, 2));
                        String senSQLtinta="INSERT INTO articulos_tintas(clavearticulo, claveproducto, porcentaje, m2porcentaje, gramos) VALUES ('"+clave.getText()+"', '"+modelot2.getValueAt(i, 0)+"', '"+modelot2.getValueAt(i, 2)+"', '"+fijo4decimales.format(porm2)+"', '"+modelot2.getValueAt(i, 3)+"');";
                        conexion.modificamov_p(senSQLtinta,connj,valor_privilegio);
                    }/**fin de revisar los campos vacios*/

                    //guardamos los datos de los juegos
                    String senSQLjuegosdel="DELETE FROM articulos_juegos WHERE clavearticulo='"+clave.getText()+"';";
                    conexion.modificamov_p(senSQLjuegosdel,connj,valor_privilegio);
                    for(int i=0;i<=(filasj-1);i=i+1){
                        String senSQLjuego="INSERT INTO articulos_juegos(clavearticulo, clavearticulo1,piezas) VALUES ('"+clave.getText()+"', '"+modelot3.getValueAt(i, 0)+"', '"+modelot3.getValueAt(i, 2)+"');";
                        conexion.modificamov_p(senSQLjuego,connj,valor_privilegio);
                    }/**fin de revisar los campos vacios*/

                    salir();
                }else{
                    //no guarda los datos
                }
            }//final de las flautas
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

    private void claveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_claveFocusGained
        // TODO add your handling code here:
        clave.selectAll();
}//GEN-LAST:event_claveFocusGained

    private void descripcionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_descripcionFocusGained
        // TODO add your handling code here:
        descripcion.selectAll();
}//GEN-LAST:event_descripcionFocusGained

    private void claveFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_claveFocusLost
        // TODO add your handling code here:
        clave.setText(clave.getText().toUpperCase());
        if(clave.getText().equals("")){

        }else{
            rs0=null;
            try{
                String senSQL="SELECT * FROM articulos WHERE (id_articulo<>'"+id.getText()+"' and clavearticulo='"+clave.getText()+"')";
                if(id.getText().equals("")){
                    senSQL="SELECT * FROM articulos WHERE clavearticulo='"+clave.getText()+"'";
                }

                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    JOptionPane.showMessageDialog(this,"LA CLAVE DE ARTICULO YA EXISTE","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                    clave.setText("");
                    clave.requestFocus();
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
    }//GEN-LAST:event_claveFocusLost

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        busca_resistencias busca_resistencias = new busca_resistencias(null,true,connj,"");
        busca_resistencias.setLocationRelativeTo(this);
        busca_resistencias.setVisible(true);
        resistencia.setText(busca_resistencias.getText());//obtiene el valor seleccionado
        busca_resistencias=null;
        //valores utilizados para verificar el peso del articulo
        pesom2resis=0.0;
        pesom2resisreal=0.0;
        if(resistencia.getText().equals("")){

        }else{
            rs0=null;
            try{
                String senSQL="SELECT * FROM resistencias WHERE clave='"+resistencia.getText()+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    corrugado.setText(rs0.getString("corrugado"));
                    color.setText(rs0.getString("color"));
                    pesom2resis=rs0.getDouble("pesom2");
                    pesom2resisreal=rs0.getDouble("pesom2real");
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        busca_clientes busca_clientes = new busca_clientes(null,true,connj,"");
        busca_clientes.setLocationRelativeTo(this);
        busca_clientes.setVisible(true);
        clavecliente.setText(busca_clientes.getText());//obtiene el valor seleccionado
        busca_clientes=null;
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        busca_monedas busca_monedas = new busca_monedas(null,true,connj,"");
        busca_monedas.setLocationRelativeTo(this);
        busca_monedas.setVisible(true);
        moneda.setText(busca_monedas.getText());//obtiene el valor seleccionado
        busca_monedas=null;
        //valores utilizados para verificar el peso del articulo
        valormoneda=0.0;
        if(resistencia.getText().equals("")){

        }else{
            rs0=null;
            try{
                String senSQL="SELECT * FROM monedas WHERE id_moneda='"+moneda.getText()+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    valormoneda=rs0.getDouble("valor");
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void inlargoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inlargoFocusGained
        // TODO add your handling code here:
        inlargo.selectAll();
    }//GEN-LAST:event_inlargoFocusGained

    private void inanchoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inanchoFocusGained
        // TODO add your handling code here:
        inancho.selectAll();
    }//GEN-LAST:event_inanchoFocusGained

    private void inaltoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inaltoFocusGained
        // TODO add your handling code here:
        inalto.selectAll();
    }//GEN-LAST:event_inaltoFocusGained

    private void largoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_largoFocusGained
        // TODO add your handling code here:
        largo.selectAll();
    }//GEN-LAST:event_largoFocusGained

    private void anchoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_anchoFocusGained
        // TODO add your handling code here:
        ancho.selectAll();
    }//GEN-LAST:event_anchoFocusGained

    private void piezasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_piezasFocusGained
        // TODO add your handling code here:
        piezas.selectAll();
    }//GEN-LAST:event_piezasFocusGained

    private void scoresFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_scoresFocusGained
        // TODO add your handling code here:
        scores.selectAll();
    }//GEN-LAST:event_scoresFocusGained

    private void suajeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_suajeFocusGained
        // TODO add your handling code here:
        suaje.selectAll();
    }//GEN-LAST:event_suajeFocusGained

    private void grabadoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_grabadoFocusGained
        // TODO add your handling code here:
        grabado.selectAll();
    }//GEN-LAST:event_grabadoFocusGained

    private void preciomillarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_preciomillarFocusGained
        // TODO add your handling code here:
        preciomillar.selectAll();
    }//GEN-LAST:event_preciomillarFocusGained

    private void btnmasmaqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmasmaqActionPerformed
        // TODO add your handling code here:
        int filas=Tablamaquinas.getRowCount();
        if(filas<15){
            Object datos[]={"","",0,0,0.0};
            modelot1.addRow(datos);
        }else{
            JOptionPane.showMessageDialog(this, "YA NO PUEDES AGREGAR MAS FILAS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }
}//GEN-LAST:event_btnmasmaqActionPerformed

    private void TablamaquinasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TablamaquinasKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_ENTER)
        {
            evt.setKeyCode(java.awt.event.KeyEvent.VK_TAB);
        }
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_F7)
        {
                int filano=Tablamaquinas.getSelectedRow();
                int colno=Tablamaquinas.getSelectedColumn();
                if(colno==0){ //columna de la clave del articulo
                    if (Tablamaquinas.getCellEditor() != null) {
                        Tablamaquinas.getCellEditor().stopCellEditing();
                    }
                    String valorfiltro=(String)Tablamaquinas.getValueAt(filano, colno);
                    busca_maquinas busca_maquinas = new busca_maquinas(null,true,connj,"");
                    busca_maquinas.setLocationRelativeTo(this);
                    busca_maquinas.setVisible(true);
                    Tablamaquinas.setValueAt(busca_maquinas.getText(), filano, colno);
                    valorfiltro=(String)Tablamaquinas.getValueAt(filano, colno);
                    busca_maquinas=null;

                    if(valorfiltro.equals("")){

                    }else{
                        rs0=null;
                        try{
                            String senSQL="SELECT * FROM maquinas WHERE clave='"+valorfiltro+"'";
                            rs0=conexion.consulta(senSQL,connj);
                            if(rs0.next()){
                                Tablamaquinas.setValueAt(rs0.getString("descripcion"), filano, 1);
                                Tablamaquinas.setValueAt(rs0.getInt("arreglo"), filano, 2);
                                Tablamaquinas.setValueAt(rs0.getInt("velocidad"), filano, 3);
                                Tablamaquinas.setValueAt(rs0.getDouble("costokg"), filano,4);
                            }
                            if(rs0!=null) {
                                rs0.close();
                            }
                        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
                    }
                    Tablamaquinas.changeSelection(filano, 2, false, false);
                }
            }
    }//GEN-LAST:event_TablamaquinasKeyPressed

    private void btnmenosmaqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmenosmaqActionPerformed
        // TODO add your handling code here:
        int filano=Tablamaquinas.getSelectedRow();
        if(filano<0){
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            int respuesta=JOptionPane.showConfirmDialog(this,"ESTA SEGURO DE ELIMINAR LA FILA  "+(filano+1)+" ?????"," I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                modelot1.removeRow(filano);
            }else{
            }
        }
}//GEN-LAST:event_btnmenosmaqActionPerformed

    private void btnmenostinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmenostinActionPerformed
        // TODO add your handling code here:
        int filano=Tablatintas.getSelectedRow();
        if(filano<0){
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            int respuesta=JOptionPane.showConfirmDialog(this,"ESTA SEGURO DE ELIMINAR LA FILA  "+(filano+1)+" ?????"," I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                modelot2.removeRow(filano);
            }else{

            }
        }
    }//GEN-LAST:event_btnmenostinActionPerformed

    private void btnmastinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmastinActionPerformed
        // TODO add your handling code here:
        int filas=Tablatintas.getRowCount();
        if(filas<4){
            Object datos[]={"","",0.0};
            modelot2.addRow(datos);
        }else{
            JOptionPane.showMessageDialog(this, "YA NO PUEDES AGREGAR MAS FILAS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnmastinActionPerformed

    private void TablatintasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TablatintasKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_ENTER)
        {
            evt.setKeyCode(java.awt.event.KeyEvent.VK_TAB);
        }
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_F7)
        {
                int filano=Tablatintas.getSelectedRow();
                int colno=Tablatintas.getSelectedColumn();
                if(colno==0){ //columna de la clave del articulo
                    if (Tablatintas.getCellEditor() != null) {
                        Tablatintas.getCellEditor().stopCellEditing();
                    }
                    String valorfiltro=(String)Tablatintas.getValueAt(filano, colno);
                    busca_productos busca_productos = new busca_productos(null,true,connj,"","","1");
                    busca_productos.setLocationRelativeTo(this);
                    busca_productos.setVisible(true);
                    Tablatintas.setValueAt(busca_productos.getText(), filano, colno);
                    valorfiltro=(String)Tablatintas.getValueAt(filano, colno);
                    busca_productos=null;

                    if(valorfiltro.equals("")){

                    }else{
                        rs0=null;
                        try{
                            String senSQL="SELECT * FROM productos WHERE clave='"+valorfiltro+"'";
                            rs0=conexion.consulta(senSQL,connj);
                            if(rs0.next()){
                                Tablatintas.setValueAt(rs0.getString("descripcion"), filano, 1);
                            }
                            if(rs0!=null) {
                                rs0.close();
                            }
                        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

                    }
                    Tablatintas.changeSelection(filano, 2, false, false);
                }
            }
    }//GEN-LAST:event_TablatintasKeyPressed

    private void TablajuegosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TablajuegosKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_ENTER)
        {
            evt.setKeyCode(java.awt.event.KeyEvent.VK_TAB);
        }
        if (evt.getKeyCode()==java.awt.event.KeyEvent.VK_F7)
        {
                int filano=Tablajuegos.getSelectedRow();
                int colno=Tablajuegos.getSelectedColumn();
                if(colno==0){ //columna de la clave del articulo
                    if (Tablajuegos.getCellEditor() != null) {
                        Tablajuegos.getCellEditor().stopCellEditing();
                    }
                    String valorfiltro=(String)Tablajuegos.getValueAt(filano, colno);
                    busca_articulos busca_articulos = new busca_articulos(null,true,connj,"",clavecliente.getText());
                    busca_articulos.setLocationRelativeTo(this);
                    busca_articulos.setVisible(true);
                    Tablajuegos.setValueAt(busca_articulos.getText(), filano, colno);
                    valorfiltro=(String)Tablajuegos.getValueAt(filano, colno);
                    busca_articulos=null;

                    if(valorfiltro.equals("")){

                    }else{
                        rs0=null;
                        try{
                            String senSQL="SELECT * FROM articulos WHERE clavearticulo='"+valorfiltro+"'";
                            rs0=conexion.consulta(senSQL,connj);
                            if(rs0.next()){
                                Tablajuegos.setValueAt(rs0.getString("articulo"), filano, 1);
                            }
                            if(rs0!=null) {
                                rs0.close();
                            }
                        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

                    }

                    Tablajuegos.changeSelection(filano, 2, false, false);
                }

            }
}//GEN-LAST:event_TablajuegosKeyPressed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        int filano=Tablajuegos.getSelectedRow();
        if(filano<0){
            JOptionPane.showMessageDialog(this, "TIENES QUE SELECCIONAR UNA FILA", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            int respuesta=JOptionPane.showConfirmDialog(this,"ESTA SEGURO DE ELIMINAR LA FILA  "+(filano+1)+" ?????"," I N F O R M A C I O N !!!!!", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                modelot3.removeRow(filano);
            }else{

            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        int filas=Tablajuegos.getRowCount();
        if(filas<10){
            Object datos[]={"","",0.0};
            modelot3.addRow(datos);
        }else{
            JOptionPane.showMessageDialog(this, "YA NO PUEDES AGREGAR MAS FILAS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void preciomillarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_preciomillarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_preciomillarMouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        String clavemodifica=clave.getText();
        if(!clavemodifica.equals("")){
            File rutaarchivo=new File(conf.getProperty("Rutaplanos")+"/"+clavemodifica+".pdf");
            if (rutaarchivo.exists()) { //verifica que la ruta exista
                try {
                  Desktop.getDesktop().open(rutaarchivo);
                } catch (Exception ex) { JOptionPane.showMessageDialog(null,"NO SE PUEDE ABRIR EL ARCHIVO\n"+ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE); }
            }else{
                JOptionPane.showMessageDialog(this, "EL ARCHIVO NO EXISTE", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }
        }

    }//GEN-LAST:event_jButton6ActionPerformed

    private void destinoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_destinoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_destinoFocusGained

    private void cobbMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cobbMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cobbMouseClicked

    private void cobbFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cobbFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_cobbFocusGained

    private void cobbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cobbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cobbActionPerformed

    private void aplicaciones_especialesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aplicaciones_especialesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_aplicaciones_especialesActionPerformed

    private void tipo_flautaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipo_flautaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tipo_flautaActionPerformed

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tablajuegos;
    private javax.swing.JTable Tablamaquinas;
    private javax.swing.JTable Tablatintas;
    private javax.swing.JTextField actualizado;
    private javax.swing.JFormattedTextField ancho;
    private javax.swing.JComboBox aplicaciones_especiales;
    private javax.swing.JTextField aream2;
    private javax.swing.JButton btnaceptar;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btnmasmaq;
    private javax.swing.JButton btnmastin;
    private javax.swing.JButton btnmenosmaq;
    private javax.swing.JButton btnmenostin;
    private javax.swing.JTextField cantventa;
    private javax.swing.JTextField clave;
    private javax.swing.JTextField clavecliente;
    private javax.swing.JFormattedTextField cobb;
    private javax.swing.JTextField color;
    private javax.swing.JTextField corrugado;
    private javax.swing.JTextField descripcion;
    private javax.swing.JTextField destino;
    private javax.swing.JComboBox diseno;
    private javax.swing.JTextField fechaventa;
    private javax.swing.JTextField grabado;
    private javax.swing.JTextField id;
    private javax.swing.JFormattedTextField inalto;
    private javax.swing.JFormattedTextField inancho;
    private javax.swing.JFormattedTextField inlargo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JFormattedTextField largo;
    private javax.swing.JTextField moneda;
    private javax.swing.JComboBox obsoleto;
    private javax.swing.JTextField pesokg;
    private javax.swing.JTextField pesokgreal;
    private javax.swing.JFormattedTextField piezas;
    private javax.swing.JTextField pk;
    private javax.swing.JFormattedTextField preciomillar;
    private javax.swing.JTextField resistencia;
    private javax.swing.JTextField scores;
    private javax.swing.JTextField suaje;
    private javax.swing.JComboBox tipo_flauta;
    // End of variables declaration//GEN-END:variables

}
