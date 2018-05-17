 /*
 * CONTAApp.java
 */

package conta;

import java.util.Properties;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import org.pushingpixels.substance.api.*;

/**
 * The main class of the application.
 */
public class CONTAApp extends SingleFrameApplication {
    private Properties conf;

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        
       final CONTAApp g=this;
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {

                try {
                    conf=conexion.archivoInicial();
                    String lf=conf.getProperty("Tema");
                    
                    /**revisa si existe la fuente*/
                    int fontencontrada=0;
                    java.awt.GraphicsEnvironment e = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
                    String[] fontnames = e.getAvailableFontFamilyNames();
                    for (int i = 0; i < fontnames.length; i++){
                      if(fontnames[i].toUpperCase().equals("SEGOE UI"))
                          fontencontrada=1;
                    }
                    /**definicion de fuentes*/
                    java.awt.Font FontPlain=null;
                    java.awt.Font FontBold =null;
                    java.awt.Font Font11Bold=null;
                    if(fontencontrada==1){
                        System.out.println("Fuente encontrada");
                        FontPlain = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11);
                        FontBold = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11);
                        Font11Bold = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11);
                    }else{
                        FontPlain = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, getClass().getResourceAsStream("/fonts/segoeui.ttf")).deriveFont(11f);
                        FontBold = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, getClass().getResourceAsStream("/fonts/segoeuib.ttf")).deriveFont(11f);
                        Font11Bold = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, getClass().getResourceAsStream("/fonts/segoeuib.ttf")).deriveFont(11f);
                    }

                    UIManager.put("controlFont", FontPlain); // NOI18N
                    UIManager.put("Button.font", FontBold); // NOI18N
                    UIManager.put("ToggleButton.font", FontPlain); // NOI18N
                    UIManager.put("RadioButton.font", FontPlain); // NOI18N
                    UIManager.put("CheckBox.font", FontPlain); // NOI18N
                    UIManager.put("ColorChooser.font", FontPlain); // NOI18N
                    UIManager.put("ComboBox.font", FontPlain); // NOI18N
                    UIManager.put("Label.font", FontPlain); // NOI18N
                    UIManager.put("List.font", FontPlain); // NOI18N
                    UIManager.put("MenuBar.font", FontBold); // NOI18N
                    UIManager.put("MenuItem.font", FontPlain); // NOI18N
                    UIManager.put("MenuItem.acceleratorFont", FontPlain); // NOI18N
                    UIManager.put("RadioButtonMenuItem.font", FontPlain); // NOI18N
                    UIManager.put("CheckBoxMenuItem.font", FontPlain); // NOI18N
                    UIManager.put("Menu.font", FontBold); // NOI18N
                    UIManager.put("PopupMenu.font", FontPlain); // NOI18N
                    UIManager.put("OptionPane.font", FontPlain); // NOI18N
                    UIManager.put("Panel.font", FontPlain); // NOI18N
                    UIManager.put("ProgressBar.font", FontPlain); // NOI18N
                    UIManager.put("ScrollPane.font", FontPlain); // NOI18N
                    UIManager.put("Viewport.font", FontPlain); // NOI18N
                    UIManager.put("TabbedPane.font", FontPlain); // NOI18N
                    UIManager.put("Table.font", FontPlain); // NOI18N
                    UIManager.put("TableHeader.font", FontBold); // NOI18N
                    UIManager.put("TextField.font", FontPlain); // NOI18N
                    UIManager.put("PasswordField.font", FontPlain); // NOI18N
                    UIManager.put("TextArea.font", FontPlain); // NOI18N
                    UIManager.put("TextPane.font", FontPlain); // NOI18N
                    UIManager.put("EditorPane.font", FontPlain); // NOI18N
                    UIManager.put("TitledBorder.font", FontPlain); // NOI18N
                    UIManager.put("ToolBar.font", FontPlain); // NOI18N
                    UIManager.put("ToolTip.font", FontPlain); // NOI18N
                    UIManager.put("Tree.font", FontPlain); // NOI18N
                    UIManager.put("InternalFrame.titleFont", Font11Bold); // NOI18N
                    UIManager.put("windowTitleFont", FontBold); // NOI18N
                    /**final de definicion de fuentes*/

                    if(lf.equals("default")){
                        //UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                    }
                    if(lf.equals("syn")){
                        //UIManager.put("Synthetica.window.decoration", Boolean.TRUE);
                        //UIManager.setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaWhiteVisionLookAndFeel");
                        //UIManager.setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel");
                    }
                    if(lf.equals("blueice")){
                        //UIManager.put("Synthetica.window.decoration", Boolean.TRUE);
                        //UIManager.setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaBlueIceLookAndFeel");
                    }
                    if(lf.equals("clasico")){
                        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
                    }
                    if(!lf.equals("clasico") && !lf.equals("syn") && !lf.equals("default") && !lf.equals("blueice")){
                        JFrame.setDefaultLookAndFeelDecorated(true);
                        JDialog.setDefaultLookAndFeelDecorated(true);
                        SubstanceLookAndFeel.setSkin(lf);
                        
                    }

                }catch(Exception e){e.printStackTrace();}
                show(new login());
            }
        });

    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of CONTAApp
     */
    public static CONTAApp getApplication() {
        return Application.getInstance(CONTAApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(CONTAApp.class, args);
    }
}

/*SynthLookAndFeel laf = new SynthLookAndFeel();
laf.load(new File("prueba.xml").toURL());
UIManager.setLookAndFeel(laf);*/

//subtance 4.3 y 5.2
//SubstanceLookAndFeel.setCurrentTheme( "org.jvnet.substance.theme.SubstanceCremeTheme" );
//SubstanceLookAndFeel.setCurrentWatermark("org.jvnet.substance.watermark.SubstanceBinaryWatermark");

//subtance 6.0
//SubstanceLookAndFeel.setSkin("org.pushingpixels.substance.api.skin.CremeSkin");

//synthetica
/*import de.javasoft.plaf.synthetica.SyntheticaBlackMoonLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaWhiteVisionLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaBlueIceLookAndFeel;*/