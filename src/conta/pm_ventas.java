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
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import jxl.*;
import jxl.write.*;
//xml
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 *
 * @author IVONNE
 */
public class pm_ventas extends javax.swing.JInternalFrame {

    Connection conn = null;
    DefaultTableModel modelot1 = null;
    ListSelectionModel modeloseleccion;
    ResultSet rs0 = null, rs1 = null, rs2 = null;
    private Properties conf;
    DecimalFormat estandarentero = new DecimalFormat("#,###,##0");
    DecimalFormat fijo0decimales = new DecimalFormat("######0");
    DecimalFormat fijo1decimales = new DecimalFormat("######0.0");
    DecimalFormat fijo2decimales = new DecimalFormat("######0.00");
    DecimalFormat moneda2decimales = new DecimalFormat("$ #,###,##0.00");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechamediana = new SimpleDateFormat("dd-MMM-yyyy");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    String usuariorem = "";
    String valor_privilegio = "1";
    String cc_iva_por_pagar = "", cc_iva_provisionado = "", cc_ventas = "", cc_iva_retenido = "";

    /** Creates new form usuarios */
    public pm_ventas(Connection connt, String valor_privilegio_t) {
        initComponents();
        /**da formato al jframe cambiando el icono y centrandolo*/
        conf = conexion.archivoInicial();
        usuariorem = conf.getProperty("UserID");
        conn = connt;
        modelot1 = (DefaultTableModel) Tabladatos.getModel();
        Tabladatos.setModel(modelot1);
        modeloseleccion = Tabladatos.getSelectionModel();
        ajusteTabladatos();
        datos_parametros();
        valor_privilegio = valor_privilegio_t;

        modeloseleccion.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.isSelectionEmpty()) {
                } else {
                    //Busca los indices solo filas es decir te dice que filas estÃ¡n selecionadas
                    Double sumatotaldebe = 0.0, sumatotalhaber = 0.0;
                    int indiceMenor = lsm.getMinSelectionIndex();// a partir del minimo seleccionado
                    int indiceMayor = lsm.getMaxSelectionIndex();
                    for (int i = indiceMenor; i <= indiceMayor; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            if (Tabladatos.getValueAt(i, 3) != null && !Tabladatos.getValueAt(i, 3).equals("")) { //suma los metros lineales
                                if (Tabladatos.getValueAt(i, 2) != null && !Tabladatos.getValueAt(i, 2).equals("")) { //suma los metros lineales
                                    if (Tabladatos.getValueAt(i, 3).toString().toUpperCase().equals("D")) {
                                        sumatotaldebe += Double.parseDouble("" + Tabladatos.getValueAt(i, 2));
                                    } else {
                                        sumatotalhaber += Double.parseDouble("" + Tabladatos.getValueAt(i, 2));
                                    }
                                }
                            }
                        }
                    }
                    noregistros.setText("DEBE: " + moneda2decimales.format(sumatotaldebe) + "             HABER: " + moneda2decimales.format(sumatotalhaber));
                }
            }
        });

    }

    public void salir() {
        if (conn != null) {
            conn = null;
        }
        dispose();
        this.setVisible(false);
    }

    public void ajusteTabladatos() {
        Tabladatos.getColumnModel().getColumn(0).setPreferredWidth(100);
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(260);
        Tabladatos.getColumnModel().getColumn(2).setPreferredWidth(100);
    }

    public void datos_parametros() {
        rs0 = null;
        try {
            String senSQL = "SELECT * FROM parametros WHERE id_parametros='1'";
            rs0 = conexion.consulta(senSQL, conn);
            if (rs0.next()) {
                cc_ventas = rs0.getString("cuenta_contable_ventas");
                cc_iva_por_pagar = rs0.getString("cuenta_contable_iva_por_pagar");
                cc_iva_provisionado = rs0.getString("cuenta_contable_iva_provisionado");
                cc_iva_retenido = rs0.getString("cuenta_contable_iva_retenido_provisionado");

            }
            if (rs0 != null) {
                rs0.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "ERROR AL EJECUTAR LA CONSULTA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void limpiatabla() {
        modelot1.setNumRows(0);
    }

    public void generapol() {
        int filas = Tabladatos.getRowCount();
        if (filas > 0) {
            try {
                //Create instance of DocumentBuilderFactory
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                //Get the DocumentBuilder
                DocumentBuilder docBuilder = factory.newDocumentBuilder();
                //Create blank DOM Document
                Document doc = docBuilder.newDocument();
                //doc.setXmlStandalone(true);//quita el estandalone
                //create the root element
                Element root = doc.createElement("DATAPACKET");
                doc.appendChild(root);
                root.setAttribute("Version", "2.0");
                Element metadata = doc.createElement("METADATA");
                root.appendChild(metadata);
                Element fields = doc.createElement("FIELDS");
                metadata.appendChild(fields);
                Element field1 = doc.createElement("FIELD");
                fields.appendChild(field1);
                field1.setAttribute("attrname", "VersionCOI");
                field1.setAttribute("fieldtype", "i2");
                Element field2 = doc.createElement("FIELD");
                fields.appendChild(field2);
                field2.setAttribute("attrname", "TipoPoliz");
                field2.setAttribute("fieldtype", "string");
                field2.setAttribute("WIDTH", "2");
                Element field3 = doc.createElement("FIELD");
                fields.appendChild(field3);
                field3.setAttribute("attrname", "DiaPoliz");
                field3.setAttribute("fieldtype", "string");
                field3.setAttribute("WIDTH", "2");
                Element field4 = doc.createElement("FIELD");
                fields.appendChild(field4);
                field4.setAttribute("attrname", "ConcepPoliz");
                field4.setAttribute("fieldtype", "string");
                field4.setAttribute("WIDTH", "120");
                Element field5 = doc.createElement("FIELD");
                fields.appendChild(field5);
                field5.setAttribute("attrname", "Partidas");
                field5.setAttribute("fieldtype", "nested");
                //field dentro de ields
                Element fields2 = doc.createElement("FIELDS");
                field5.appendChild(fields2);
                Element field21 = doc.createElement("FIELD");
                fields2.appendChild(field21);
                field21.setAttribute("attrname", "Cuenta");
                field21.setAttribute("fieldtype", "string");
                field21.setAttribute("WIDTH", "21");
                Element field22 = doc.createElement("FIELD");
                fields2.appendChild(field22);
                field22.setAttribute("attrname", "Depto");
                field22.setAttribute("fieldtype", "i4");
                Element field23 = doc.createElement("FIELD");
                fields2.appendChild(field23);
                field23.setAttribute("attrname", "ConceptoPol");
                field23.setAttribute("fieldtype", "string");
                field23.setAttribute("WIDTH", "120");
                Element field24 = doc.createElement("FIELD");
                fields2.appendChild(field24);
                field24.setAttribute("attrname", "Monto");
                field24.setAttribute("fieldtype", "r8");
                Element field25 = doc.createElement("FIELD");
                fields2.appendChild(field25);
                field25.setAttribute("attrname", "TipoCambio");
                field25.setAttribute("fieldtype", "r8");
                Element field26 = doc.createElement("FIELD");
                fields2.appendChild(field26);
                field26.setAttribute("attrname", "DebeHaber");
                field26.setAttribute("fieldtype", "string");
                field26.setAttribute("WIDTH", "1");

                Element params2 = doc.createElement("PARAMS");
                field5.appendChild(params2);

                Element params1 = doc.createElement("PARAMS");
                metadata.appendChild(params1);

                Element rowdata = doc.createElement("ROWDATA");
                root.appendChild(rowdata);
                Element row = doc.createElement("ROW");
                rowdata.appendChild(row);
                row.setAttribute("VersionCOI", "50");
                row.setAttribute("TipoPoliz", "Dr");
                row.setAttribute("DiaPoliz", "31");
                row.setAttribute("ConcepPoliz", JOptionPane.showInputDialog("CONCEPTO DE LA POLIZA MODELO"));

                Element partidas = doc.createElement("Partidas");
                row.appendChild(partidas);

                for (int i = 0; i < (Tabladatos.getRowCount()); i = i + 1) {
                    //create child element
                    Element childElement = doc.createElement("ROWPartidas");
                    String cuentaprincipal = "" + Tabladatos.getValueAt(i, 0);
                    childElement.setAttribute("Cuenta", cuentaprincipal);
                    partidas.appendChild(childElement);
                    childElement.setAttribute("Depto", fijo0decimales.format(0.0));
                    partidas.appendChild(childElement);
                    childElement.setAttribute("ConceptoPol", "" + Tabladatos.getValueAt(i, 1));
                    partidas.appendChild(childElement);
                    childElement.setAttribute("Monto", fijo2decimales.format(Double.parseDouble("" + Tabladatos.getValueAt(i, 2))));
                    partidas.appendChild(childElement);
                    childElement.setAttribute("TipoCambio", fijo0decimales.format(1.0));
                    partidas.appendChild(childElement);
                    childElement.setAttribute("DebeHaber", "" + Tabladatos.getValueAt(i, 3));
                    partidas.appendChild(childElement);

                }
                /**fin de revisar los campos vacios*/
                File nuevo = new File(System.getProperty("user.home") + "/pol_ventas.pol");
                guardatxt.setSelectedFile(nuevo);
                if (guardatxt.showDialog(this, null) == JFileChooser.APPROVE_OPTION) {//cuando regresa un uno no guarda nada
                    String rutafinal = String.valueOf(guardatxt.getSelectedFile());
                    TransformerFactory tranFactory = TransformerFactory.newInstance();
                    Transformer aTransformer = tranFactory.newTransformer();
                    Source src = new DOMSource(doc);

                    File file = new File(rutafinal);
                    Result result = new StreamResult(file);
                    aTransformer.transform(src, result);
                    JOptionPane.showMessageDialog(this, "ARCHIVO CREADO CORRECTAMENTE", " L I S T O !!!!!", JOptionPane.INFORMATION_MESSAGE);
                }


            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ERROR AL CREAR XML\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(this, "LA TABLA NO TIENE DATOS", "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
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

        guardatxt = new javax.swing.JFileChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabladatos = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jButton4 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jButton3 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jPanel1 = new javax.swing.JPanel();
        buscar = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        noregistros = new javax.swing.JLabel();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(pm_ventas.class);
        guardatxt.setDialogTitle(resourceMap.getString("guardatxt.dialogTitle")); // NOI18N
        guardatxt.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        guardatxt.setName("guardatxt"); // NOI18N

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
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
                "Cuenta_Contable", "Concepto", "Importe", "Debe_Haber"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
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
        });
        jScrollPane1.setViewportView(Tabladatos);

        jToolBar1.setBackground(resourceMap.getColor("jToolBar1.background")); // NOI18N
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        jSeparator4.setName("jSeparator4"); // NOI18N
        jToolBar1.add(jSeparator4);

        jButton4.setIcon(resourceMap.getIcon("jButton4.icon")); // NOI18N
        jButton4.setText(resourceMap.getString("jButton4.text")); // NOI18N
        jButton4.setToolTipText(resourceMap.getString("jButton4.toolTipText")); // NOI18N
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton4.setMaximumSize(new java.awt.Dimension(70, 48));
        jButton4.setMinimumSize(new java.awt.Dimension(70, 48));
        jButton4.setName("jButton4"); // NOI18N
        jButton4.setPreferredSize(new java.awt.Dimension(70, 48));
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jSeparator5.setName("jSeparator5"); // NOI18N
        jToolBar1.add(jSeparator5);

        jButton3.setIcon(resourceMap.getIcon("jButton3.icon")); // NOI18N
        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setToolTipText(resourceMap.getString("jButton3.toolTipText")); // NOI18N
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton3.setMaximumSize(new java.awt.Dimension(70, 48));
        jButton3.setMinimumSize(new java.awt.Dimension(70, 48));
        jButton3.setName("jButton3"); // NOI18N
        jButton3.setPreferredSize(new java.awt.Dimension(70, 48));
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jSeparator3.setName("jSeparator3"); // NOI18N
        jToolBar1.add(jSeparator3);

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 48));
        jPanel1.setMinimumSize(new java.awt.Dimension(100, 48));
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(556, 48));

        buscar.setText(resourceMap.getString("buscar.text")); // NOI18N
        buscar.setToolTipText(resourceMap.getString("buscar.toolTipText")); // NOI18N
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

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setIcon(resourceMap.getIcon("jLabel1.icon")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(398, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jToolBar1.add(jPanel1);

        jPanel2.setName("jPanel2"); // NOI18N

        noregistros.setFont(resourceMap.getFont("noregistros.font")); // NOI18N
        noregistros.setForeground(resourceMap.getColor("noregistros.foreground")); // NOI18N
        noregistros.setText(resourceMap.getString("noregistros.text")); // NOI18N
        noregistros.setName("noregistros"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(noregistros)
                .addContainerGap(822, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(noregistros)
                .addContainerGap(2, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 837, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 837, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 513, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosing

    private void buscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarKeyReleased
        // TODO add your handling code here:
        if (buscar.getText().equals("")) {
            Tabladatos.setRowSorter(null);
            buscar.setText("");
            Tabladatos.setAutoCreateRowSorter(true);
        } else {
            TableRowSorter orden = new TableRowSorter(modelot1);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)" + buscar.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tabladatos.setRowSorter(orden);
        }
    }//GEN-LAST:event_buscarKeyReleased

    private void TabladatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabladatosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_TabladatosMouseClicked

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formInternalFrameClosing

    private void buscarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarFocusGained
        // TODO add your handling code here:
        buscar.selectAll();
    }//GEN-LAST:event_buscarFocusGained

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        File rutaarchivo = new File(System.getProperty("user.home") + "/pm_ventas.xls");
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

            int filainicial = 5;

            //titulo del documento
            sheet.addImage(new jxl.write.WritableImage(0, 0, 3, 4, new java.io.File(conexion.Directorio() + "/logoempresa.png")));
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
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        try {
            busca_periodo busca_periodo = new busca_periodo(null, true, "reporte");
            busca_periodo.setLocationRelativeTo(this);
            busca_periodo.setVisible(true);
            String estado = busca_periodo.getEstado();
            /**obtiene si se pulso aceptar o calcelar en busca_fechas*/
            if (estado.equals("cancelar")) {
            } else {
                //convierte las fechas
                limpiatabla();
                int ano = busca_periodo.getano();
                int mes = busca_periodo.getmes();
                if (ano >= 0 && mes >= 0) {
                    Calendar calFin = Calendar.getInstance();
                    calFin.set(ano, mes, 1);
                    Date fechaIni = calFin.getTime();
                    calFin.set(ano, mes, calFin.getActualMaximum(Calendar.DAY_OF_MONTH));
                    Date fechaFin = calFin.getTime();
                    //facturas activas
                    rs0 = null;
                    Double sumatotal = 0.0;
                    Double sumasubtotal = 0.0;
                    Double sumaiva = 0.0;
                    Double sumaivaretenido = 0.0;
                    String senSQL_t = "SELECT clientes.nombre,clientes.rfc,clientes.cuenta_contable_skarton,clientes.cuenta_contable,facturas.factura,facturas.serie,folios.aprobacion,facturas.fecha,facturas.total,facturas.estatus,facturas.iva,facturas.ivaretenido,facturas.subtotal FROM (facturas LEFT JOIN clientes ON facturas.id_clientes=clientes.id_clientes) LEFT JOIN folios ON facturas.id_folios=folios.id_folio WHERE (facturas.fecha>='" + fechainsertar.format(fechaIni) + " 00:00:00' AND facturas.fecha<='" + fechainsertar.format(fechaFin) + " 23:59:59') ORDER BY facturas.factura";
                    rs0 = conexion.consulta(senSQL_t, conn);
                    while (rs0.next()) {
                        String est = "" + rs0.getString("estatus");
                        Double total = 0.0;
                        Double ivaret = 0.0;
                        String facserf = rs0.getString("serie") + rs0.getString("factura");
                        String con = "CANCELADA";
                        String ccf = rs0.getString("cuenta_contable_skarton");
                        if (est.equals("1")) {
                            
                            Double ivaretx = Double.parseDouble(fijo2decimales.format(rs0.getDouble("ivaretenido")));
                            if (ccf.equals("1150-013-004-000") && ivaretx>0.0) {
                                ivaret = ivaretx;
                                sumaivaretenido +=Double.parseDouble(fijo2decimales.format(rs0.getDouble("subtotal")));
                                //sumasubtotal +=ivaret; la liena que esta en esta parte de abajo fue la afectada
                                sumaiva+=ivaretx;
                            } else {
                                sumaiva += Double.parseDouble(fijo2decimales.format(rs0.getDouble("iva")));
                                sumasubtotal += Double.parseDouble(fijo2decimales.format(rs0.getDouble("subtotal")));
                            }

                            total = Double.parseDouble(fijo2decimales.format(rs0.getDouble("total")));
                            con = rs0.getString("nombre");
                        }
                        Object datos[] = {ccf, facserf + " " + con, total, "D"};
                        modelot1.addRow(datos);
                        if (ivaret > 0.0) {
                            Object datos2[] = {cc_iva_retenido, "IVA RETENIDO PROVISIONADO" + facserf, ivaret, "D"};
                            modelot1.addRow(datos2);
                        }
                    }
                    if (rs0 != null) {
                        rs0.close();
                    }

                    //utlimos datos de IVAS y subtotales
                    Object datos3[] = {cc_ventas, "VENTAS", Double.parseDouble(fijo2decimales.format(sumasubtotal)), "H"};
                    modelot1.addRow(datos3);
                    if (sumaivaretenido > 0) {
                        Object datos5[] = {"4100-005-000-000-000", "DESPERDICIO", Double.parseDouble(fijo2decimales.format(sumaivaretenido)), "H"};
                        modelot1.addRow(datos5);
                    }
                    Object datos4[] = {cc_iva_provisionado, "IVA PROVISIONADO", Double.parseDouble(fijo2decimales.format(sumaiva)), "H"};
                    modelot1.addRow(datos4);

                    generapol();
                }
            }
            busca_periodo = null;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "ERROR AL EXPORTAR POLIZA\n" + ex, "E R R O R !!!!!!!!!!", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jButton4ActionPerformed
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabladatos;
    private javax.swing.JTextField buscar;
    private javax.swing.JFileChooser guardatxt;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel noregistros;
    // End of variables declaration//GEN-END:variables
    private JDialog datos_pagos;
}
