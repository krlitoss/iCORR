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

import java.math.RoundingMode;
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
public class datos_programas_conversion extends javax.swing.JDialog {
    Connection connj;
    ResultSet rs0=null;
    String clavemodificaf="";
    SimpleDateFormat fechacorta = new SimpleDateFormat("dd-MMM-yy");
    SimpleDateFormat fechainsertar = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat fechainsertarhora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    DecimalFormat estandarentero=new DecimalFormat("#,###,##0");
    DecimalFormat fijo0decimales=new DecimalFormat("######0");
    DecimalFormat horasminutos=new DecimalFormat("##########00");

    int piezasxlamina=1;
    String sonlaminas="";
    int cantsolicitada=0;

    int cantidad_prog_corr=0;
    int program_corr_final=0;

    String noautorizado="Act";
    String valor_privilegio="1";

    /** Creates new form datos_usuarios */
    public datos_programas_conversion(java.awt.Frame parent, boolean modal,Connection conn,String clavemodifica,String clavemaq,String programaconversion,String claveart_stock,int cantidad_prog,int programa_corr,String autorizado,String valor_privilegio_t) {
        super(parent, modal);
        initComponents();
        connj=conn;
        valor_privilegio=valor_privilegio_t;
        horasminutos.setRoundingMode(RoundingMode.DOWN);
        clavemodificaf=clavemodifica;
        program.setVisible(false);
        fechaprod.setDate(new Date());
        op.setText(clavemodifica);
        clavemaquina.setText(clavemaq);
        cantidad_prog_corr=cantidad_prog;
        program_corr_final=programa_corr;
        noautorizado=autorizado;
        buscamaquina();
        if(programaconversion.equals("")){ //si no hay progrma hacemos uno nuevo
            if(op.getText().equals("STOCK")){
                claveart.setEditable(true);
                claveart.setFocusable(true);
                if(!claveart_stock.equals("")){
                    claveart.setText(claveart_stock);
                    busca_articulo();
                }
            }else{
                compruebaop();
            }
            cantidad.setText(fijo0decimales.format(cantidad_prog));
            horaprograma();

        }else{
            modificaprograma(programaconversion);
        }
        //dandera que revisa si son piezas o algo diferente
        //para todo lo diferente divide las piezas entre las piezas ppor pliego para obtener laminas o golpes
        if(!sonlaminas.equals("PIEZAS")){
            lbcant.setText("Cantidad Golpes:");
            lbpedidos.setText("Golpes pedidos");
            lbprogram.setText("Golpes Program.");
            lbprod.setText("Golpes Prod.");
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
    public void horaprograma(){
        //obtiene el tiempo en horas
        if(!(cantidad.getText().equals("")||cantidad.getText().equals("0")||velocidad.getText().equals("")||velocidad.getText().equals("0")||arreglo.getText().equals("")||arreglo.getText().equals("0")))
        {
            Double totalminutos=(Double.parseDouble(cantidad.getText())/Double.parseDouble(velocidad.getText()))+Double.parseDouble(arreglo.getText());
            horas.setText(horasminutos.format(totalminutos/60)+":"+horasminutos.format(totalminutos%60));
        }
    }
    public void compruebaop(){
        String clavemaq=clavemaquina.getText();
        if(clavemaq.equals("") || op.getText().equals("")){
            JOptionPane.showMessageDialog(this, "VERIFICA TIENES QUE SELECCIONAR UNA OP Y CLAVE MAQUINA", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            //ingresas las ordenes de produccion faltantes por programar
            piezasxlamina=1;
            rs0=null;
            try{
                String senSQL="SELECT articulos_maquinas.arreglo,articulos_maquinas.velocidad,ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,articulos.clavearticulo,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,clientes.bloqueado,clientes.observaciones,prod.fechaprod,COALESCE(prod.prodml,0) as prodml,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas,0) as prodcantkgpiezas,program.fechaprogram,COALESCE(program.programcantidad,0) as programcantidad,COALESCE(program.programcantidadpiezas,0) as programcantidadpiezas,COALESCE(program.programcantkgpiezas) as programcantkgpiezas,articulos_maquinas.clave FROM  ( (((ops LEFT JOIN articulos ON ops.clavearticulo=articulos.clavearticulo) LEFT JOIN clientes ON ops.id_clientes=clientes.id_clientes) LEFT JOIN (SELECT conversion_captura.op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.prodml) as prodml,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaq+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.op) as prod ON ops.op=prod.op ) LEFT JOIN (SELECT programa_conversion.op,max(programa_conversion.fechaproduccion) as fechaprogram,sum(programa_conversion.cantidad) as programcantidad,sum(programa_conversion.cantidadpiezas) as programcantidadpiezas,sum(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas  FROM programa_conversion LEFT JOIN articulos ON programa_conversion.clavearticulo=articulos.clavearticulo  WHERE (programa_conversion.clavemaquina='"+clavemaq+"' AND programa_conversion.estatus<>'Can') GROUP BY programa_conversion.op) as program ON ops.op=program.op) INNER JOIN articulos_maquinas ON articulos_maquinas.clavearticulo=ops.clavearticulo  WHERE ( ops.op='"+op.getText()+"' AND ops.estatus<>'Can' AND articulos_maquinas.clave='"+clavemaq+"') ORDER BY ops.fechaentrega,ops.op;";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    if(rs0.getString("bloqueado").equals("0")){
                        btnaceptar.setEnabled(false);
                        JOptionPane.showMessageDialog(this,"EL CLIENTE ESTA BLOQUEADO NO PUEDES PROGRAMAR\n"+rs0.getString("observaciones"),"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                    }else{
                        claveart.setText(rs0.getString("clavearticulo"));
                        articulo.setText(rs0.getString("articulo"));
                        cliente.setText(rs0.getString("nombre"));
                        fechaentrega.setText(fechacorta.format(rs0.getDate("fechaentrega")));
                        //verificar si es laminas o piezas
                        cantsolicitada=rs0.getInt("cantidad");
                        if(!sonlaminas.equals("PIEZAS")){
                            cantsolicitada=rs0.getInt("laminaspedidas");
                        }
                        golpesped.setText(estandarentero.format(cantsolicitada)); //verificar si son piezas o golpes
                        golpesprogram.setText(estandarentero.format(rs0.getInt("programcantidad")));
                        golpesprod.setText(estandarentero.format(rs0.getInt("prodcantidad")));
                        arreglo.setText(fijo0decimales.format(rs0.getInt("arreglo")));
                        velocidad.setText(fijo0decimales.format(rs0.getInt("velocidad")));
                        piezasxlamina=rs0.getInt("piezas");
                    }
                }
                if(rs0!=null) {   rs0.close();  }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }

    }
    public void modificaprograma(String programaconversion){
        piezasxlamina=1;
        rs0=null;
        try{
            String senSQL="SELECT programa_conversion.id_programa_conversion,programa_conversion.id_numero,programa_conversion.op,programa_conversion.clavearticulo,programa_conversion.fechaproduccion as fechaprogram,programa_conversion.cantidad as programcantidad,programa_conversion.cantidadpiezas as programcantidadpiezas,(programa_conversion.cantidadpiezas*articulos.kg) as programcantkgpiezas,programa_conversion.imprimir, ops.op,ops.fechaentrega,ops.maquila,ops.cantidad,(ops.cantidad/articulos.piezas) as laminaspedidas,articulos.articulo,articulos.claveresistencia,articulos.kg,articulos.m2,articulos.piezas,clientes.nombre,clientes.bloqueado,prod.fechaprod,COALESCE(prod.prodcantidad,0) as prodcantidad,COALESCE(prod.prodcantidadpiezas,0) as prodcantidadpiezas,COALESCE(prod.prodcantkgpiezas,0) as prodcantkgpiezas,articulos_maquinas.clave,programa_conversion.arreglo,programa_conversion.velocidad FROM (((programa_conversion LEFT JOIN ops ON programa_conversion.op=ops.op) LEFT JOIN (articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes) ON programa_conversion.clavearticulo=articulos.clavearticulo) LEFT JOIN (SELECT conversion_captura.id_programa_conversion,max(conversion_captura.op) as op,max(conversion_captura.fechareal)as fechaprod,sum(conversion_captura.cantidad) as prodcantidad,sum(conversion_captura.cantidadpiezas) as prodcantidadpiezas,sum(conversion_captura.cantidadpiezas*articulos.kg) as prodcantkgpiezas FROM conversion_captura LEFT JOIN articulos ON conversion_captura.clavearticulo=articulos.clavearticulo WHERE (conversion_captura.clave='"+clavemaquina.getText()+"' AND conversion_captura.estatus<>'Can') GROUP BY conversion_captura.id_programa_conversion) as prod ON programa_conversion.id_programa_conversion=prod.id_programa_conversion) LEFT JOIN articulos_maquinas ON (programa_conversion.clavearticulo=articulos_maquinas.clavearticulo AND programa_conversion.clavemaquina=articulos_maquinas.clave) WHERE (programa_conversion.id_programa_conversion='"+programaconversion+"' AND programa_conversion.estatus='Act' AND programa_conversion.clavemaquina='"+clavemaquina.getText()+"') ORDER BY programa_conversion.fechaproduccion,programa_conversion.id_numero;";
            rs0=conexion.consulta(senSQL,connj);
            if(rs0.next()){
                Date fechaentregat=rs0.getDate("fechaentrega");
                String sfe="";
                if(!(""+fechaentregat).equals("null")){
                    sfe=fechacorta.format(fechaentregat);
                }
                program.setText(rs0.getString("id_programa_conversion"));
                fechaprod.setDate(rs0.getDate("fechaprogram"));

                claveart.setText(rs0.getString("clavearticulo"));
                articulo.setText(rs0.getString("articulo"));
                cliente.setText(rs0.getString("nombre"));
                fechaentrega.setText(sfe);
                //verificar si es laminas o piezas
                cantsolicitada=rs0.getInt("cantidad");
                if(!sonlaminas.equals("PIEZAS")){
                    cantsolicitada=rs0.getInt("laminaspedidas");
                }
                int programados=rs0.getInt("programcantidad");
                cantidad.setText(fijo0decimales.format(programados));

                golpesped.setText(estandarentero.format(cantsolicitada)); //verificar si son piezas o golpes
                piezasxlamina=rs0.getInt("piezas");
                arreglo.setText(fijo0decimales.format(rs0.getInt("arreglo")));
                velocidad.setText(fijo0decimales.format(rs0.getInt("velocidad")));

                //horas
                horaprograma();
                
            }
            if(rs0!=null) {      rs0.close();   }
        } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
    }
    public void buscamaquina(){
        if(clavemaquina.getText().equals("")){

        }else{
            rs0=null;
            try{
                String senSQL="SELECT * FROM maquinas WHERE clave='"+clavemaquina.getText()+"'";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    sonlaminas=rs0.getString("unidadcapacidad").toUpperCase();
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
        }

    }
    public void busca_articulo(){
        int errormaquina=1;
            //revisa si el articulo tiene la maquina que se captura en su ruta de procesos
            rs0=null;
            try{
                String senSQL="SELECT articulos_maquinas.*,maquinas.arreglo,maquinas.velocidad FROM articulos_maquinas LEFT JOIN maquinas ON articulos_maquinas.clave=maquinas.clave WHERE (articulos_maquinas.clave='"+clavemaquina.getText()+"' AND articulos_maquinas.clavearticulo='"+claveart.getText()+"')";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    errormaquina=0;
                    arreglo.setText(fijo0decimales.format(rs0.getInt("arreglo")));
                    velocidad.setText(fijo0decimales.format(rs0.getInt("velocidad")));
                }
                if(rs0!=null) {  rs0.close(); }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }
            if(errormaquina==1){
                String msg="LA MAQUINA NO ESTA EN LA RUTA DE PROCESOS DEL ARTICULO NO LA PUEDES PROGRAMAR AQUI";
                JOptionPane.showMessageDialog(this,msg, "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                claveart.setText("");
                articulo.setText("");
                arreglo.setText("");
                velocidad.setText("");
                claveart.requestFocus();
            }else{

                rs0=null;
                try{
                    String senSQL="SELECT articulos.*,clientes.nombre,clientes.bloqueado FROM articulos LEFT JOIN clientes ON articulos.id_clientes=clientes.id_clientes WHERE (obsoleto='No' and clavearticulo='"+claveart.getText()+"')";
                    rs0=conexion.consulta(senSQL,connj);
                    if(rs0.next()){
                        if(rs0.getString("bloqueado").equals("0")){
                            btnaceptar.setEnabled(false);
                            JOptionPane.showMessageDialog(this,"EL CLIENTE ESTA BLOQUEADO NO PUEDES PROGRAMAR\n"+rs0.getString("observaciones"),"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                        }else{
                            articulo.setText(rs0.getString("articulo"));
                            cliente.setText(rs0.getString("nombre"));
                            piezasxlamina=rs0.getInt("piezas");
                        }
                    }else{
                        JOptionPane.showMessageDialog(this,"LA CLAVE DE ARTICULO NO EXISTE","E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
                        claveart.setText("");
                        articulo.setText("");
                        claveart.requestFocus();
                    }
                    if(rs0!=null) {      rs0.close();  }
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

        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        op = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btnaceptar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        btnmaquina = new javax.swing.JButton();
        clavemaquina = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        fechaprod = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        claveart = new javax.swing.JTextField();
        articulo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cliente = new javax.swing.JTextField();
        lbcant = new javax.swing.JLabel();
        cantidad = new javax.swing.JFormattedTextField();
        paneldatos = new javax.swing.JPanel();
        lbpedidos = new javax.swing.JLabel();
        golpesped = new javax.swing.JTextField();
        lbprogram = new javax.swing.JLabel();
        golpesprogram = new javax.swing.JTextField();
        lbprod = new javax.swing.JLabel();
        golpesprod = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        fechaentrega = new javax.swing.JTextField();
        program = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        arreglo = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        velocidad = new javax.swing.JFormattedTextField();
        horas = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(conta.CONTAApp.class).getContext().getResourceMap(datos_programas_conversion.class);
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

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        op.setEditable(false);
        op.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        op.setFocusable(false);
        op.setName("op"); // NOI18N
        op.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                opFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                opFocusLost(evt);
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
                .addGap(130, 130, 130)
                .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnaceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnmaquina.setIcon(resourceMap.getIcon("btnmaquina.icon")); // NOI18N
        btnmaquina.setText(resourceMap.getString("btnmaquina.text")); // NOI18N
        btnmaquina.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnmaquina.setName("btnmaquina"); // NOI18N
        btnmaquina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmaquinaActionPerformed(evt);
            }
        });

        clavemaquina.setEditable(false);
        clavemaquina.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        clavemaquina.setText(resourceMap.getString("clavemaquina.text")); // NOI18N
        clavemaquina.setFocusable(false);
        clavemaquina.setName("clavemaquina"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        fechaprod.setName("fechaprod"); // NOI18N

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        claveart.setEditable(false);
        claveart.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        claveart.setText(resourceMap.getString("claveart.text")); // NOI18N
        claveart.setFocusable(false);
        claveart.setName("claveart"); // NOI18N
        claveart.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                claveartFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                claveartFocusLost(evt);
            }
        });

        articulo.setEditable(false);
        articulo.setText(resourceMap.getString("articulo.text")); // NOI18N
        articulo.setFocusable(false);
        articulo.setName("articulo"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        cliente.setEditable(false);
        cliente.setText(resourceMap.getString("cliente.text")); // NOI18N
        cliente.setFocusable(false);
        cliente.setName("cliente"); // NOI18N

        lbcant.setText(resourceMap.getString("lbcant.text")); // NOI18N
        lbcant.setName("lbcant"); // NOI18N

        cantidad.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0"))));
        cantidad.setName("cantidad"); // NOI18N
        cantidad.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cantidadFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cantidadFocusLost(evt);
            }
        });

        paneldatos.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("paneldatos.border.title"), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("paneldatos.border.titleFont"))); // NOI18N
        paneldatos.setName("paneldatos"); // NOI18N

        lbpedidos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbpedidos.setText(resourceMap.getString("lbpedidos.text")); // NOI18N
        lbpedidos.setName("lbpedidos"); // NOI18N

        golpesped.setBackground(resourceMap.getColor("golpesped.background")); // NOI18N
        golpesped.setEditable(false);
        golpesped.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        golpesped.setText(resourceMap.getString("golpesped.text")); // NOI18N
        golpesped.setFocusable(false);
        golpesped.setName("golpesped"); // NOI18N

        lbprogram.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbprogram.setText(resourceMap.getString("lbprogram.text")); // NOI18N
        lbprogram.setName("lbprogram"); // NOI18N

        golpesprogram.setBackground(resourceMap.getColor("golpesprogram.background")); // NOI18N
        golpesprogram.setEditable(false);
        golpesprogram.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        golpesprogram.setText(resourceMap.getString("golpesprogram.text")); // NOI18N
        golpesprogram.setFocusable(false);
        golpesprogram.setName("golpesprogram"); // NOI18N

        lbprod.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbprod.setText(resourceMap.getString("lbprod.text")); // NOI18N
        lbprod.setName("lbprod"); // NOI18N

        golpesprod.setBackground(resourceMap.getColor("golpesprod.background")); // NOI18N
        golpesprod.setEditable(false);
        golpesprod.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        golpesprod.setText(resourceMap.getString("golpesprod.text")); // NOI18N
        golpesprod.setFocusable(false);
        golpesprod.setName("golpesprod"); // NOI18N

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        fechaentrega.setBackground(resourceMap.getColor("fechaentrega.background")); // NOI18N
        fechaentrega.setEditable(false);
        fechaentrega.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fechaentrega.setText(resourceMap.getString("fechaentrega.text")); // NOI18N
        fechaentrega.setFocusable(false);
        fechaentrega.setName("fechaentrega"); // NOI18N

        javax.swing.GroupLayout paneldatosLayout = new javax.swing.GroupLayout(paneldatos);
        paneldatos.setLayout(paneldatosLayout);
        paneldatosLayout.setHorizontalGroup(
            paneldatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneldatosLayout.createSequentialGroup()
                .addGroup(paneldatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fechaentrega, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paneldatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(golpesped, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbpedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paneldatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(golpesprogram, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbprogram, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paneldatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(golpesprod, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbprod, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        paneldatosLayout.setVerticalGroup(
            paneldatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneldatosLayout.createSequentialGroup()
                .addGroup(paneldatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(paneldatosLayout.createSequentialGroup()
                        .addComponent(lbprod)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(golpesprod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(paneldatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(paneldatosLayout.createSequentialGroup()
                            .addGroup(paneldatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lbpedidos)
                                .addComponent(jLabel11))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(paneldatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(golpesped, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(fechaentrega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(paneldatosLayout.createSequentialGroup()
                            .addComponent(lbprogram)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(golpesprogram, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        program.setEditable(false);
        program.setText(resourceMap.getString("program.text")); // NOI18N
        program.setFocusable(false);
        program.setName("program"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        arreglo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0"))));
        arreglo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        arreglo.setText(resourceMap.getString("arreglo.text")); // NOI18N
        arreglo.setName("arreglo"); // NOI18N
        arreglo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                arregloFocusLost(evt);
            }
        });

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        velocidad.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0"))));
        velocidad.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        velocidad.setText(resourceMap.getString("velocidad.text")); // NOI18N
        velocidad.setName("velocidad"); // NOI18N
        velocidad.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                velocidadFocusLost(evt);
            }
        });

        horas.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        horas.setText(resourceMap.getString("horas.text")); // NOI18N
        horas.setName("horas"); // NOI18N

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(arreglo, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(velocidad, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(horas, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(lbcant, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(program, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cliente))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(op, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(claveart))
                            .addComponent(articulo)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fechaprod, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clavemaquina, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnmaquina, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(paneldatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fechaprod, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(clavemaquina))
                    .addComponent(btnmaquina, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(op, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(claveart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(articulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbcant)
                    .addComponent(cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(program, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(arreglo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(velocidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(horas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(paneldatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnaceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaceptarActionPerformed
        // TODO add your handling code here:
        if(fechaprod.getDate()==null || clavemaquina.getText().equals("") || op.getText().equals("") || cantidad.getText().equals("") || cantidad.getText().equals("0")|| arreglo.getText().equals("") || arreglo.getText().equals("0")|| velocidad.getText().equals("") || velocidad.getText().equals("0")){
            JOptionPane.showMessageDialog(this, "VERIFICA HAY CAMPOS VACIOS", "E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
        }else{
            int cp=Integer.parseInt(cantidad.getText());
            if(cp>(cantsolicitada*1.1) && !op.getText().equals("STOCK")){
                JOptionPane.showMessageDialog(this,"EXCEDE MAS DEL 10% DE LA CANTIDAD A ENTREGAR\n<html><font size=5 color=blue><b>LA OP: "+op.getText()+"</b></font><br><font size=5 color=#DC143C><b>Cantidad Programada: </b>"+cp+"<br><b>Cantidad solicitada: </b>"+cantsolicitada,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);
            }
            
            String senSQL="";
            /**funcion que regresa el numero maximo de programa conversion*/
            int programamax=1;
            rs0=null;
            try{
                senSQL="SELECT max(id_programa_conversion) as programamax FROM programa_conversion";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    programamax=rs0.getInt("programamax")+1;
                }
                if(rs0!=null) {   rs0.close();      }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

            /**funcion que regresa el numero maximo de programa conversion*/
            int programamaxmaquina=1;
            rs0=null;
            try{
                senSQL="SELECT max(id_numero) as programamaxmaquina FROM programa_conversion WHERE (clavemaquina='"+clavemaquina.getText()+"' AND estatus='Act')";
                rs0=conexion.consulta(senSQL,connj);
                if(rs0.next()){
                    programamaxmaquina=rs0.getInt("programamaxmaquina")+1;
                }
                if(rs0!=null) {
                    rs0.close();
                }
            } catch (Exception ex) {JOptionPane.showMessageDialog(this,"ERROR AL EJECUTAR LA CONSULTA\n"+ ex,"E R R O R !!!!!!!!!!",JOptionPane.ERROR_MESSAGE);      }

            int cantidadpzas=Integer.parseInt(cantidad.getText()); //verifica si sol golpes o piezas
            if(!sonlaminas.equals("PIEZAS")){
                cantidadpzas=cantidadpzas*piezasxlamina; //convierte las laminasa piezas
            }
            if(program.getText().equals("")){
                if(cantidad_prog_corr>0){
                    senSQL="INSERT INTO programa_conversion(id_programa_conversion, id_numero, estatus, fechaproduccion, clavemaquina, op, clavearticulo, cantidad, cantidadpiezas,imprimir,arreglo,velocidad,fecha,id_programa_corr) VALUES ('"+programamax+"', '"+programamaxmaquina+"', '"+noautorizado+"', '"+fechainsertar.format(fechaprod.getDate())+"', '"+clavemaquina.getText()+"', '"+op.getText()+"', '"+claveart.getText()+"', '"+cantidad.getText()+"', '"+cantidadpzas+"','false','"+arreglo.getText()+"','"+velocidad.getText()+"','"+fechainsertar.format(new Date())+"','"+program_corr_final+"');";
                    conexion.modificamov_p(senSQL,connj,valor_privilegio);
                    salir();
                }else{
                    if (JOptionPane.showConfirmDialog(this,"LISTO PARA GUARDAR\nDESEA CONTINUAR ?????"," L I S T O !!!!!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        senSQL="INSERT INTO programa_conversion(id_programa_conversion, id_numero, estatus, fechaproduccion, clavemaquina, op, clavearticulo, cantidad, cantidadpiezas,imprimir,arreglo,velocidad,fecha,id_programa_corr) VALUES ('"+programamax+"', '"+programamaxmaquina+"', '"+noautorizado+"', '"+fechainsertar.format(fechaprod.getDate())+"', '"+clavemaquina.getText()+"', '"+op.getText()+"', '"+claveart.getText()+"', '"+cantidad.getText()+"', '"+cantidadpzas+"','false','"+arreglo.getText()+"','"+velocidad.getText()+"','"+fechainsertar.format(new Date())+"','"+program_corr_final+"');";
                        conexion.modifica_p(senSQL,connj,valor_privilegio);
                        salir();
                    }

                }
                
            }else{//funcion para actualizar los datos
                if (JOptionPane.showConfirmDialog(this,"LISTO PARA ACTUALIZAR\nDESEA CONTINUAR ?????"," L I S T O !!!!!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    senSQL="UPDATE programa_conversion SET fechaproduccion='"+fechainsertar.format(fechaprod.getDate())+"',cantidad='"+cantidad.getText()+"', cantidadpiezas='"+cantidadpzas+"', arreglo='"+arreglo.getText()+"', velocidad='"+velocidad.getText()+"', clavemaquina = '"+clavemaquina.getText()+"' WHERE id_programa_conversion='"+program.getText()+"';";
                    conexion.modificaupdate_p(senSQL,connj,valor_privilegio);
                    salir();
                }
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

    private void opFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_opFocusLost
        // TODO add your handling code here:
        compruebaop();
}//GEN-LAST:event_opFocusLost

    private void btnmaquinaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmaquinaActionPerformed
        // TODO add your handling code here:
        busca_maquinas busca_maquinas = new busca_maquinas(null,true,connj,"");
        busca_maquinas.setLocationRelativeTo(this);
        busca_maquinas.setVisible(true);
        clavemaquina.setText(busca_maquinas.getText());
        busca_maquinas=null;
        buscamaquina();
    }//GEN-LAST:event_btnmaquinaActionPerformed

    private void opFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_opFocusGained
        // TODO add your handling code here:
        op.selectAll();
    }//GEN-LAST:event_opFocusGained

    private void cantidadFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cantidadFocusGained
        // TODO add your handling code here:
        cantidad.selectAll();
    }//GEN-LAST:event_cantidadFocusGained

    private void cantidadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cantidadFocusLost
        // TODO add your handling code here:
        //horas
        horaprograma();
    }//GEN-LAST:event_cantidadFocusLost

    private void claveartFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_claveartFocusGained
        // TODO add your handling code here:
        claveart.selectAll();
    }//GEN-LAST:event_claveartFocusGained

    private void claveartFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_claveartFocusLost
        // TODO add your handling code here:
        if(claveart.getText().equals("")){

        }else{
            busca_articulo();
        }
    }//GEN-LAST:event_claveartFocusLost

    private void arregloFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_arregloFocusLost
        // TODO add your handling code here:
        //horas
        horaprograma();
    }//GEN-LAST:event_arregloFocusLost

    private void velocidadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_velocidadFocusLost
        // TODO add your handling code here:
        //horas
        horaprograma();
    }//GEN-LAST:event_velocidadFocusLost

    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField arreglo;
    private javax.swing.JTextField articulo;
    private javax.swing.JButton btnaceptar;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btnmaquina;
    private javax.swing.JFormattedTextField cantidad;
    private javax.swing.JTextField claveart;
    private javax.swing.JTextField clavemaquina;
    private javax.swing.JTextField cliente;
    private javax.swing.JTextField fechaentrega;
    private com.toedter.calendar.JDateChooser fechaprod;
    private javax.swing.JTextField golpesped;
    private javax.swing.JTextField golpesprod;
    private javax.swing.JTextField golpesprogram;
    private javax.swing.JTextField horas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbcant;
    private javax.swing.JLabel lbpedidos;
    private javax.swing.JLabel lbprod;
    private javax.swing.JLabel lbprogram;
    private javax.swing.JTextField op;
    private javax.swing.JPanel paneldatos;
    private javax.swing.JTextField program;
    private javax.swing.JFormattedTextField velocidad;
    // End of variables declaration//GEN-END:variables

}
