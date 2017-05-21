package cofe.xyz;

import java.awt.*;

import xyz.cofe.gui.swing.properties.PropertySheet;

/**
 * Hello world!
 *
 */
public class App extends javax.swing.JFrame
{
	public App(){
		setTitle("Test PropertySheet");
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		getContentPane().setLayout( new BorderLayout() );

		PropertySheet propertySheet1 = new PropertySheet();
		getContentPane().add( propertySheet1 );

		propertySheet1.edit( this );

        initSizeLocation();
    }

    public void initSizeLocation(){
        setMinimumSize(new Dimension(200,300));
        pack();
        setLocationRelativeTo(null);
    }

    public static void main( String[] args )
    {
        System.out.println( "Start sample" );

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).
                log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).
                log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).
                log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).
                log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new App().setVisible(true);
            }
        });
    }
}
