/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * busca_agentes.java
 *
 * Created on 26/01/2010, 05:27:05 PM
 */

package conta;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

/**
 *
 * @author ANGEL
 */
public class ACorrugadora_herramienta_combina extends javax.swing.JDialog {
    Connection connj;
    Connection concontrol=null;
    ResultSet rs0=null;
    DefaultTableModel modelot1=null;
    String clavebusca="";
    int piezasart2=0;
    DecimalFormat fijo1decimales=new DecimalFormat("######0.0");
    DecimalFormat fijo2decimales=new DecimalFormat("######0.00");
    DecimalFormat fijo3decimales=new DecimalFormat("######0.000");
    DecimalFormat fijo4decimales=new DecimalFormat("######0.0000");
    DecimalFormat fijo5decimales=new DecimalFormat("######0.00000");
    SimpleDateFormat fechamascorta = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechacm = new SimpleDateFormat("MM/dd/yyyy");
    Calendar calendariniciosem = new GregorianCalendar();
    Calendar calendarfinsem = new GregorianCalendar();
    Boolean cancelado=false;
    String fi="";
    String ff="";

    barraespera barraespera = new barraespera(null,true,"Buscando Combinaciones...");

    /** Creates new form busca_agentes */
    public ACorrugadora_herramienta_combina(java.awt.Frame parent, boolean modal,Connection conn,String claveart,String opf) {
        super(parent, modal);
        initComponents();
        /**da formato al jframe cambiando el icono y centrandolo*/
        this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/imagenes/logo.png")).getImage());
        this.setLocationRelativeTo(null);
        connj=conn;
        modelot1=(DefaultTableModel) Tabladatos.getModel();
        Tabladatos.setModel(modelot1);
        ajusteTabladatos();
        conexioncontrolmaestro();
        consultamodifica_art(claveart);
        consultamodifica(opf);

        /**funcion para ordenar datos*/
        TableRowSorter<TableModel> elQueOrdena = new TableRowSorter<TableModel>(modelot1);
        Tabladatos.setRowSorter(elQueOrdena);

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
        if(concontrol!=null){       ////****eliminar para el control maestro*******////////
            concontrol=null;
        }
        dispose();
        this.setVisible(false);
    }
    public void conexioncontrolmaestro(){       ////****eliminar para el control maestro*******////////
        if(concontrol!=null){

        }else{
            concontrol=conexion.abrirconexioncontrol();
        }
    }
    class TareaPrueba extends javax.swing.SwingWorker<Void, Void> {
        public TareaPrueba(){
            setProgress(0);
        }

        @Override
        public Void doInBackground() {

            try{
                limpiatabla();
                String tc=""+tipo_consulta.getSelectedItem();
                datos_sola(Double.parseDouble(anchototal.getText()),Double.parseDouble(ancho.getText()));
                if(tc.equals("Pendientes")){
                    datos_pendientes(Double.parseDouble(anchototal.getText()),Double.parseDouble(ancho.getText()));
                }
            }catch (Exception ex) {
                cancel(true);}

            setProgress(100);
            return null;
        }
        @Override
        public void done() {

        }

    }
    public void consultamodifica(String clavemodifica){
        limpiatabla();
        if(clavemodifica.equals("")){

        }else{
            rs0=null;
            try{
                String senSQL="SELECT ops.*,(ops.cantidad-COALESCE(remisionado.cantidadremision,0)) as cantidadpendiente,COALESCE(remisionado.cantidadremision,0) as cantidadremisionado,remisionado.fecharemisionado,articulos.articulo,articulos.diseno,articulos.claveresistencia,articulos.largo,articulos.ancho,articulos.scores,articulos.kg,articulos.m2,articulos.piezas,resistencias.color,resistencias.corrugado,clientes.nombre,lugares_entregas.id_lugarentrega,lugares_entregas.calle,lugares_entregas.numeroext,lugares_entregas.colonia,lugares_entregas.municipio,lugares_entregas.estado,lugares_entregas.contacto FROM (((ops LEFT JOIN (articulos LEFT JOIN resistencias ON articulos.claveresistencia=resistencias.clave) ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) LEFT JOIN lugares_entregas ON ops.id_lugarentrega=lugares_entregas.id_lugarentrega) LEFT JOIN (SELECT remisiones_detalle.op,sum(remisiones_detalle.cantidadpzas) as cantidadremision,max(remisiones.fechahora) as fecharemisionado FROM remisiones_detalle LEFT JOIN remisiones ON remisiones_detalle.remision=remisiones.remision WHERE (remisiones_detalle.estatus='Act') GROUP BY remisiones_detalle.op) as remisionado ON ops.op=remisionado.op WHERE ops.op='"+clavemodifica+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    op.setText(rs0.getString("op"));
                    clave_articulo.setText(rs0.getString("clavearticulo"));
                    articulo.setText(rs0.getString("articulo"));
                    cliente.setText(rs0.getString("nombre"));
                    resis.setText(rs0.getString("claveresistencia"));
                    largo.setText(fijo1decimales.format(rs0.getDouble("largo")));
                    ancho.setText(fijo1decimales.format(rs0.getDouble("ancho")));
                    fecha_entrega.setText(fechamascorta.format(rs0.getDate("fechaentrega")));
                }
                else{
                    JOptionPane.showMessageDialog(this,"LA ORDEN DE PRODUCCIÓN NO EXISTE","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                    op.setText("");
                    op.requestFocus();
                }
                if(rs0!=null) {  rs0.close();  }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
    }
    public void consultamodifica_art(String clavemodifica){
        limpiatabla();
        if(clavemodifica.equals("") || !op.getText().equals("")){

        }else{
            rs0=null;
            try{
                String senSQL="SELECT articulos.*,clientes.nombre FROM articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes WHERE articulos.clavearticulo='"+clavemodifica+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    clave_articulo.setText(rs0.getString("clavearticulo"));
                    articulo.setText(rs0.getString("articulo"));
                    cliente.setText(rs0.getString("nombre"));
                    resis.setText(rs0.getString("claveresistencia"));
                    largo.setText(fijo1decimales.format(rs0.getDouble("largo")));
                    ancho.setText(fijo1decimales.format(rs0.getDouble("ancho")));
                    fecha_entrega.setText("");
                }
                else{
                    JOptionPane.showMessageDialog(this,"LA CLAVE DE ARTICULO NO EXISTE","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                    clave_articulo.setText("");
                    clave_articulo.requestFocus();
                }
                if(rs0!=null) {  rs0.close();  }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }
    }
    public void ajusteTabladatos() {
        //columnas corrugadora
        Tabladatos.getColumnModel().getColumn(1).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(9).setPreferredWidth(90);
        Tabladatos.getColumnModel().getColumn(10).setPreferredWidth(200);
        Tabladatos.getColumnModel().getColumn(11).setPreferredWidth(240);
        Tabladatos.getColumnModel().getColumn(4).setCellRenderer(new DoubleRenderer_pla_corr());

    }
    public void limpiatabla(){
        modelot1.setNumRows(0);
    }
    public class DoubleRenderer_pla_corr extends SubstanceDefaultTableCellRenderer {//modificar los valores de doubles y formatearlos
            public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
                JLabel rend = (JLabel)super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
                String text = ""+value;
                rend.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                if(text.equals("null") || text.equals(""))
                    text="";

                if(!text.equals("")){
                    Double refile=Double.parseDouble(text);
                    if(refile>=2.0 && refile<=5.5)
                        rend.setBackground(new java.awt.Color(124,205,124));
                    text=fijo1decimales.format(refile);
                }
                rend.setText( text );
                return rend;
            }
    }
    public void datos_sola(Double anchototal,Double anchoarticulo1){

        Double refile_min=Double.parseDouble(refile_std_min.getText());
        Double refile_max=Double.parseDouble(refile_std_max.getText());

        // combinada solo
        for(int i=1;i<7;i++){
            if((((anchoarticulo1*i))>=(anchototal-refile_max)) && ((anchoarticulo1*i)<=(anchototal-refile_min))){
                Double refilecombinacion=anchototal-((anchoarticulo1*i));
                Object datos[]={"","",i,null,refilecombinacion,anchoarticulo1,null,null,resis.getText(),"","Combinada sola...",cliente.getText()};
                modelot1.addRow(datos);
            }
        }//fin de primer for
    }
    public void datos_pendientes(Double anchototal,Double anchoarticulo1){

        Double refile_min=Double.parseDouble(refile_std_min.getText());
        Double refile_max=Double.parseDouble(refile_std_max.getText());

        //combina con los pendientes
        rs0=null;
        try{
            String senSQL="SELECT Clientes.Nombre as nom, Articulos.[Clave Articulo], Articulos.Articulo, [OPFechasFinales]![Orden Produccion] AS OPS, OPFechasFinales.Cantidad, OPFechasFinales.Cant, Articulos.[M2]*[Cant] AS M2T, OPFechasFinales.Tkg AS KgT, [Precio Lista]*[Cant]/1000 AS ImpLP, OPFechasFinales.Venta AS ImpV, OPFechasFinales.[Fecha Entrega], OPFechasFinales.[Total Programado] AS Programa, OPFechasFinales.[Total Corrugado] AS Corrg, OPFechasFinales.[Total Impresora] AS Impreso, OPFechasFinales.[Total Almacen Emb] AS Almacen, OPFechasFinales.[Total Remision] AS Rems, OPFechasFinales.[Fecha Programado], OPFechasFinales.[Fecha Corrugado], OPFechasFinales.[Fecha Almacen Emb], OPFechasFinales.[Fecha Remision], OPFechasFinales.[Fecha Impresora], Articulos.Diseño, OPFechasFinales.Pedido, OPFechasFinales.[Su pedido], Clientes.[Clave Cliente], Agentes.Nombre, OPFechasFinales.[Fecha Recepcion], Articulos.[Maquina 1], Articulos.[Maquina 2], Articulos.[Maquina 3], Articulos.[In Largo], Articulos.[In Ancho], Articulos.[In Alto], Articulos.Resistencia, Articulos.Largo, Articulos.Ancho, Diseños.[Tipo de Diseño], OPFechasFinales.[Precio Venta], Sum([Almacen Embarques].Cantidad) AS SumaDeCantidad FROM (Diseños INNER JOIN (Agentes RIGHT JOIN ((Clientes INNER JOIN (SELECT DISTINCTROW [Ordenes Produccion].[Orden Produccion], [Ordenes Produccion].[Clave Cliente], [Ordenes Produccion].[Clave Articulo], [Ordenes Produccion].Status, [Ordenes Produccion].Cantidad, [Ordenes Produccion].[Fecha Entrega], [Ordenes Produccion]![Cantidad]-[Ordenes Produccion]![Total Remision] AS Cant, IIf(Right(([Articulos]![Clave Articulo]),1)='L',[Peso Venta]*[Precio]*1000,[PVenta]) AS Expr1, [Articulos Precios].[Precio Venta], [Expr1]*[Cant]/1000 AS Venta, [Articulos Precios].[Precio Lista], [Cant]*[Peso Venta] AS Tkg, [Ordenes Produccion].[Total Programado], [Ordenes Produccion].[Total Corrugado], [Ordenes Produccion].[Total Impresora], [Ordenes Produccion].[Total Almacen Emb], [Ordenes Produccion].[Total Remision], [Ordenes Produccion].[Fecha Programado], [Ordenes Produccion].[Fecha Corrugado], [Ordenes Produccion].[Fecha Almacen Emb], [Ordenes Produccion].[Fecha Remision], [Ordenes Produccion].[Fecha Impresora], [Ordenes Produccion].Pedido, [Ordenes Produccion].[Su pedido], IIf([Articulos Precios]![Dollar]=-1,[Costos Indirectos]![Dollar]*[Precio venta],[Precio venta]) AS PVenta, [Ordenes Produccion].[Fecha Recepcion] FROM [Costos Indirectos], Articulos INNER JOIN ([Ordenes Produccion] INNER JOIN [Articulos Precios] ON [Ordenes Produccion].[Clave Articulo] = [Articulos Precios].[Clave Articulo]) ON Articulos.[Clave Articulo] = [Articulos Precios].[Clave Articulo] WHERE ((([Ordenes Produccion].Status)<>'Sus' And ([Ordenes Produccion].Status)<>'Can' And ([Ordenes Produccion].Status)<>'NoA') AND (([Ordenes Produccion].[Fecha Entrega])>=#"+fi+"# And ([Ordenes Produccion].[Fecha Entrega])<=#"+ff+"#))) AS OPFechasFinales ON Clientes.[Clave Cliente] = OPFechasFinales.[Clave Cliente]) INNER JOIN Articulos ON OPFechasFinales.[Clave Articulo] = Articulos.[Clave Articulo]) ON Agentes.[Clave Agente] = Clientes.[Clave Agente]) ON Diseños.[Clave Diseño] = Articulos.Diseño) LEFT JOIN [Almacen Embarques] ON Articulos.[Clave Articulo] = [Almacen Embarques].[Clave Articulo] WHERE (((OPFechasFinales.Cant)>0) AND ((OPFechasFinales.Status)<>'Ter')) GROUP BY Clientes.Nombre, Articulos.[Clave Articulo], Articulos.Articulo, [OPFechasFinales]![Orden Produccion], OPFechasFinales.Cantidad, OPFechasFinales.Cant, Articulos.[M2]*[Cant], OPFechasFinales.Tkg, [Precio Lista]*[Cant]/1000, OPFechasFinales.Venta, OPFechasFinales.[Fecha Entrega], OPFechasFinales.[Total Programado], OPFechasFinales.[Total Corrugado], OPFechasFinales.[Total Impresora], OPFechasFinales.[Total Almacen Emb], OPFechasFinales.[Total Remision], OPFechasFinales.[Fecha Programado], OPFechasFinales.[Fecha Corrugado], OPFechasFinales.[Fecha Almacen Emb], OPFechasFinales.[Fecha Remision], OPFechasFinales.[Fecha Impresora], Articulos.Diseño, OPFechasFinales.Pedido, OPFechasFinales.[Su pedido], Clientes.[Clave Cliente], Agentes.Nombre, OPFechasFinales.[Fecha Recepcion], Articulos.[Maquina 1], Articulos.[Maquina 2], Articulos.[Maquina 3], Articulos.[In Largo], Articulos.[In Ancho], Articulos.[In Alto], Articulos.Resistencia, Articulos.Largo, Articulos.Ancho, Diseños.[Tipo de Diseño], OPFechasFinales.[Precio Venta] ORDER BY OPFechasFinales.[Fecha Entrega];";
            rs0=conexion.consulta(senSQL,concontrol);
            int si=0;
            while(rs0.next()){
                String r1=""+rs0.getString("Resistencia");
                String r2=""+resis.getText().charAt(0);
                String r1t=""+r1.charAt(0);

                if(r1t.equals(r2)){
                    Date fechaentrega=rs0.getDate("Fecha Entrega");
                    String sfe="";
                    String color="<html><font color=green><b>";
                    if(!(""+fechaentrega).equals("null")){
                        sfe=fechamascorta.format(fechaentrega);
                        Calendar calendarentrega = new GregorianCalendar();
                        calendarentrega.setTime(fechaentrega);

                        if(calendarentrega.before(calendariniciosem)){
                            color="<html><font color=#DC143C>"; }
                        if(calendarentrega.after(calendarfinsem)){
                            color="<html><font color=#0080FF>"; }
                    }
                    Double anchoarticulo2=rs0.getDouble("Ancho");

                    //funcion para combinar los dos articulos
                    String opt=rs0.getString("OPS");
                    String cat=rs0.getString("Clave Articulo");
                    String artt=rs0.getString("Articulo");
                    String clit=rs0.getString("nom");
                    for(int i=1;i<7;i++){
                        for(int j=1;j<7;j++){
                            if((((anchoarticulo1*i)+(anchoarticulo2*j))>=(anchototal-refile_max)) && (((anchoarticulo1*i)+(anchoarticulo2*j))<=(anchototal-refile_min))){
                                Double refilecombinacion=anchototal-((anchoarticulo1*i)+(anchoarticulo2*j));
                                Object datos[]={opt,color+sfe,i,j,refilecombinacion,anchoarticulo1,anchoarticulo2,rs0.getDouble("Largo"),r1,cat,artt,clit};
                                modelot1.addRow(datos);
                            }
                        }//fin de segundo for
                    }//fin de primer for
                }
            }
            if(rs0!=null) {   rs0.close();       }
        } catch (Exception ex) {
            //JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            System.err.println(""+ex.getMessage());
            System.err.println(""+ex.getCause());
        }


    }
    
    public String getText() {
        return clavebusca;
    }
    public Integer getPliegosxancho() {
        return piezasart2;
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
        buscar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabladatos = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        op = new javax.swing.JTextField();
        articulo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cliente = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        clave_articulo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        fecha_entrega = new javax.swing.JTextField();
        resis = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        largo = new javax.swing.JFormattedTextField();
        ancho = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        anchototal = new javax.swing.JFormattedTextField();
        tipo_consulta = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        refile_std_min = new javax.swing.JFormattedTextField();
        jLabel13 = new javax.swing.JLabel();
        refile_std_max = new javax.swing.JFormattedTextField();
        Combinar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(ACorrugadora_herramienta_combina.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("Form"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setIcon(resourceMap.getIcon("jLabel1.icon")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        buscar.setBackground(resourceMap.getColor("buscar.background")); // NOI18N
        buscar.setName("buscar"); // NOI18N
        buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscarKeyReleased(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        Tabladatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "OP", "Fecha Entrega", "<html><font color=#0080FF>Pliegos 1", "<html><font color=#0080FF>Pliegos 2", "<html><font color=#0080FF>Refile(cm)", "Ancho 1", "Ancho 2", "Largo 2", "Resis.", "Clave Articulo", "Articulo", "Cliente"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tabladatos.setToolTipText(resourceMap.getString("Tabladatos.toolTipText")); // NOI18N
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

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jPanel1.setBackground(resourceMap.getColor("jPanel1.background")); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        op.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        op.setText(resourceMap.getString("op.text")); // NOI18N
        op.setName("op"); // NOI18N
        op.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                opFocusLost(evt);
            }
        });

        articulo.setText(resourceMap.getString("articulo.text")); // NOI18N
        articulo.setName("articulo"); // NOI18N

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        cliente.setText(resourceMap.getString("cliente.text")); // NOI18N
        cliente.setName("cliente"); // NOI18N

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        clave_articulo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        clave_articulo.setText(resourceMap.getString("clave_articulo.text")); // NOI18N
        clave_articulo.setName("clave_articulo"); // NOI18N
        clave_articulo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                clave_articuloFocusLost(evt);
            }
        });

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        fecha_entrega.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fecha_entrega.setText(resourceMap.getString("fecha_entrega.text")); // NOI18N
        fecha_entrega.setName("fecha_entrega"); // NOI18N

        resis.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        resis.setText(resourceMap.getString("resis.text")); // NOI18N
        resis.setName("resis"); // NOI18N

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jButton1.setIcon(resourceMap.getIcon("jButton1.icon")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        largo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.0"))));
        largo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        largo.setText(resourceMap.getString("largo.text")); // NOI18N
        largo.setName("largo"); // NOI18N

        ancho.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.0"))));
        ancho.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ancho.setText(resourceMap.getString("ancho.text")); // NOI18N
        ancho.setName("ancho"); // NOI18N

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        anchototal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.0"))));
        anchototal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        anchototal.setText(resourceMap.getString("anchototal.text")); // NOI18N
        anchototal.setName("anchototal"); // NOI18N

        tipo_consulta.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pendientes" }));
        tipo_consulta.setName("tipo_consulta"); // NOI18N

        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        refile_std_min.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.0"))));
        refile_std_min.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        refile_std_min.setText(resourceMap.getString("refile_std_min.text")); // NOI18N
        refile_std_min.setName("refile_std_min"); // NOI18N

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        refile_std_max.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.0"))));
        refile_std_max.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        refile_std_max.setText(resourceMap.getString("refile_std_max.text")); // NOI18N
        refile_std_max.setName("refile_std_max"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE))
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(op, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clave_articulo, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(articulo, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(refile_std_min, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                            .addComponent(largo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(refile_std_max)
                            .addComponent(ancho, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(fecha_entrega, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(resis, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(anchototal, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tipo_consulta, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cliente, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(op, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(clave_articulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(fecha_entrega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(articulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(largo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ancho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(anchototal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tipo_consulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(refile_std_min, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(refile_std_max, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Combinar.setText(resourceMap.getString("Combinar.text")); // NOI18N
        Combinar.setName("Combinar"); // NOI18N
        Combinar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CombinarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Combinar, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 691, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(Combinar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarKeyReleased
        // TODO add your handling code here:
        if(buscar.getText().equals("")){
            Tabladatos.setRowSorter(null);
            buscar.setText("");
            Tabladatos.setAutoCreateRowSorter(true);
        }else{
            TableRowSorter orden = new TableRowSorter(modelot1);
            java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList();
            filtros.add(RowFilter.regexFilter("(?i)"+buscar.getText()));
            orden.setRowFilter(RowFilter.orFilter(filtros));
            Tabladatos.setRowSorter(orden);
        }
}//GEN-LAST:event_buscarKeyReleased

    private void TabladatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabladatosMouseClicked
        // TODO add your handling code here:
        int filano=Tabladatos.getSelectedRow();
        String claveop=""+Tabladatos.getValueAt(filano, 0);
        if(!claveop.equals("") || !claveop.equals("null")){

            try{
                String senSQL="SELECT [Ordenes Produccion].*,Articulos.Piezas FROM [Ordenes Produccion] LEFT JOIN Articulos ON [Ordenes Produccion].[Clave Articulo]=[Articulos].[Clave Articulo] WHERE [Orden Produccion]='"+claveop+"';";
                rs0=conexion.consulta(senSQL,concontrol);

                if(rs0.next()){
                    Date fechacorr=rs0.getDate("Fecha Corrugado");
                    String fechacorr1="";
                    if(fechacorr != null){
                        fechacorr1=fechamascorta.format(fechacorr);
                    }
                    Date fechaimpri=rs0.getDate("Fecha Impresora");
                    String fechaimpri1="";
                    if(fechaimpri != null){
                        fechaimpri1=fechamascorta.format(fechaimpri);
                    }
                    Date fecharem=rs0.getDate("Fecha Remision");
                    String fecharem1="";
                    if(fecharem != null){
                        fecharem1=fechamascorta.format(fecharem);
                    }
                    JOptionPane.showMessageDialog(this,"OP:  "+claveop+"\nFecha Entrega:  "+fechamascorta.format(rs0.getDate("Fecha Entrega"))+"\nCantidad Pedida:  "+rs0.getInt("Cantidad")+"\nFecha Corrugado:  "+fechacorr1+"\nTotal Corrugado:  "+rs0.getInt("Total Corrugado")+"\nFecha Impresora:  "+fechaimpri1+"\nTotal Impresora:  "+rs0.getInt("Total Impresora")+"\nFecha Remision:  "+fecharem1+"\nTotal Remision:  "+rs0.getInt("Total Remision")+"\n*Las cantidades estan en piezas (Piezas x Lam: "+rs0.getInt("Piezas")+" )","I N F O R M A C I O N !!!!",JOptionPane.INFORMATION_MESSAGE);

                }else{

                }
                if(rs0!=null) {   rs0.close();       }
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this,"ERROR AL CONSULTAR LA OP\n"+ ex,"A D V E R T E N C I A !!!!",JOptionPane.ERROR_MESSAGE);
            }

        }
}//GEN-LAST:event_TabladatosMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formWindowClosing

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        busca_resistencias busca_resistencias = new busca_resistencias(null,true,connj,"");
        busca_resistencias.setLocationRelativeTo(this);
        busca_resistencias.setVisible(true);
        resis.setText(busca_resistencias.getText());//obtiene el valor seleccionado
        busca_resistencias=null;
    }//GEN-LAST:event_jButton1ActionPerformed

    private void opFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_opFocusLost
        // TODO add your handling code here:
        consultamodifica(op.getText());
    }//GEN-LAST:event_opFocusLost

    private void CombinarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CombinarActionPerformed
        // TODO add your handling code here:
        if(anchototal.getText().equals("0") || anchototal.getText().equals("") || resis.getText().equals("")){
            String err="VERIFICA HAY CAMPOS VACIOS";
            JOptionPane.showMessageDialog(this, err, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            busca_fechas busca_fechas = new busca_fechas(null, true);
            busca_fechas.setLocationRelativeTo(this);
            busca_fechas.setVisible(true);
            String estado = busca_fechas.getEstado();
            /**obtiene si se pulso aceptar o calcelar en busca_fechas*/
            if (estado.equals("cancelar")) {
            } else {
                fi=fechacm.format(busca_fechas.getFechaini());
                ff=fechacm.format(busca_fechas.getFechafin());

                cancelado=false;
                final TareaPrueba TareaPrueba = new TareaPrueba();
                TareaPrueba.addPropertyChangeListener(new PropertyChangeListener() {
                        public void propertyChange(PropertyChangeEvent evt) {
                            if ("progress".equals(evt.getPropertyName())) {
                                int completado = (Integer) evt.getNewValue();

                                if (TareaPrueba.isDone()) {
                                    barraespera.setVisible(false);
                                    if(cancelado.equals(false))
                                        JOptionPane.showMessageDialog(null, "SE HA TERMINADO DE BUSCAR COMBINACIONES", " L I S T O !!!!!",JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                            if(TareaPrueba.isCancelled()){
                                barraespera.setVisible(false);
                                JOptionPane.showMessageDialog(null, "ERROR ", " E R R O R !!!!!",JOptionPane.INFORMATION_MESSAGE);
                                cancelado=true;
                            }

                        }
                    });
                    TareaPrueba.execute();
                    barraespera.setLocationRelativeTo(this);
                    barraespera.setVisible(true);
            }


            
        }
    }//GEN-LAST:event_CombinarActionPerformed

    private void clave_articuloFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_clave_articuloFocusLost
        // TODO add your handling code here:
        consultamodifica_art(clave_articulo.getText());
    }//GEN-LAST:event_clave_articuloFocusLost

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Combinar;
    private javax.swing.JTable Tabladatos;
    private javax.swing.JFormattedTextField ancho;
    private javax.swing.JFormattedTextField anchototal;
    private javax.swing.JTextField articulo;
    private javax.swing.JTextField buscar;
    private javax.swing.JTextField clave_articulo;
    private javax.swing.JTextField cliente;
    private javax.swing.JTextField fecha_entrega;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JFormattedTextField largo;
    private javax.swing.JTextField op;
    private javax.swing.JFormattedTextField refile_std_max;
    private javax.swing.JFormattedTextField refile_std_min;
    private javax.swing.JTextField resis;
    private javax.swing.JComboBox tipo_consulta;
    // End of variables declaration//GEN-END:variables

}
