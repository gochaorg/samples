/*
 * The MIT License
 *
 * Copyright 2017 user.
 *
 * Данная лицензия разрешает, безвозмездно, лицам, получившим копию данного программного
 * обеспечения и сопутствующей документации (в дальнейшем именуемыми "Программное Обеспечение"), 
 * использовать Программное Обеспечение без ограничений, включая неограниченное право на 
 * использование, копирование, изменение, объединение, публикацию, распространение, сублицензирование 
 * и/или продажу копий Программного Обеспечения, также как и лицам, которым предоставляется 
 * данное Программное Обеспечение, при соблюдении следующих условий:
 *
 * Вышеупомянутый копирайт и данные условия должны быть включены во все копии 
 * или значимые части данного Программного Обеспечения.
 *
 * ДАННОЕ ПРОГРАММНОЕ ОБЕСПЕЧЕНИЕ ПРЕДОСТАВЛЯЕТСЯ «КАК ЕСТЬ», БЕЗ ЛЮБОГО ВИДА ГАРАНТИЙ, 
 * ЯВНО ВЫРАЖЕННЫХ ИЛИ ПОДРАЗУМЕВАЕМЫХ, ВКЛЮЧАЯ, НО НЕ ОГРАНИЧИВАЯСЬ ГАРАНТИЯМИ ТОВАРНОЙ ПРИГОДНОСТИ, 
 * СООТВЕТСТВИЯ ПО ЕГО КОНКРЕТНОМУ НАЗНАЧЕНИЮ И НЕНАРУШЕНИЯ ПРАВ. НИ В КАКОМ СЛУЧАЕ АВТОРЫ 
 * ИЛИ ПРАВООБЛАДАТЕЛИ НЕ НЕСУТ ОТВЕТСТВЕННОСТИ ПО ИСКАМ О ВОЗМЕЩЕНИИ УЩЕРБА, УБЫТКОВ 
 * ИЛИ ДРУГИХ ТРЕБОВАНИЙ ПО ДЕЙСТВУЮЩИМ КОНТРАКТАМ, ДЕЛИКТАМ ИЛИ ИНОМУ, ВОЗНИКШИМ ИЗ, ИМЕЮЩИМ 
 * ПРИЧИНОЙ ИЛИ СВЯЗАННЫМ С ПРОГРАММНЫМ ОБЕСПЕЧЕНИЕМ ИЛИ ИСПОЛЬЗОВАНИЕМ ПРОГРАММНОГО ОБЕСПЕЧЕНИЯ 
 * ИЛИ ИНЫМИ ДЕЙСТВИЯМИ С ПРОГРАММНЫМ ОБЕСПЕЧЕНИЕМ.
 */
package xyz.cofe.demo.oracledbchnotif;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleDriver;
import oracle.jdbc.dcn.DatabaseChangeEvent;
import oracle.jdbc.dcn.DatabaseChangeListener;
import oracle.jdbc.dcn.DatabaseChangeRegistration;
import xyz.cofe.gui.swing.bean.Binder;

/**
 * Пример использоания Oracle database change notify
 * @author gochaorg
 */
public class MainFrame extends javax.swing.JFrame {
    private static void log( Throwable ex, String message ){
        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE,message,ex);
    }
    private static void log( Object ... args ){
        Logger log = Logger.getLogger(MainFrame.class.getName());
        StringBuilder sb = new StringBuilder();
        for( Object arg : args ){
            sb.append(arg);
        }
        log.info(sb.toString());
    }
    
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                disconnect();
            }
            @Override
            public void windowClosing(WindowEvent e) {
                disconnect();
            }
        });
        
        oracleConnectPanel1.getConnectButton().addActionListener(this::onConnectPressed);
        oracleConnectPanel1.getDisconnectButton().addActionListener(this::onDisconnectPressed);
        
        oracleConnectPanel1.getDisconnectButton().setEnabled(false);
        Binder.bean(this).listen("oracleConnection",(OracleConnection oraConn) -> {
            oracleConnectPanel1.getDisconnectButton().setEnabled(oraConn!=null);
            oracleConnectPanel1.getConnectButton().setEnabled(oraConn==null);
        }).start();
    }
    
    private volatile OracleConnection oracleConnection;
    protected void setOracleConnection(OracleConnection conn){
        OracleConnection old, cur;
        synchronized(connectSync){
            old = this.oracleConnection;
            this.oracleConnection = conn;
            cur = this.oracleConnection;
        }
        firePropertyChange("oracleConnection", old, cur);
    }
    public OracleConnection getOracleConnection(){
        synchronized(connectSync){
            return oracleConnection;
        }
    }
    
    private DatabaseChangeRegistration dbChangeReg;
    private DatabaseChangeRegistration createDatabaseChangeRegistration(OracleConnection conn) throws SQLException{
        if( conn==null )throw new IllegalArgumentException("conn == null");
        Properties prop = new Properties();
        
        prop.put(OracleConnection.DCN_NOTIFY_ROWIDS, "true");
        prop.put(OracleConnection.DCN_QUERY_CHANGE_NOTIFICATION, "true");
        DatabaseChangeRegistration reg = conn.registerDatabaseChangeNotification(prop);
        return reg;
    }
    private final Set<DatabaseChangeListener> dbChangeListeners = new LinkedHashSet<>();
    protected void listenChanges(DatabaseChangeRegistration dbChangeReg){
        if(dbChangeReg==null)throw new IllegalArgumentException("dbChangeReg == null");
        DatabaseChangeListener dbChangeListener = new DatabaseChangeListener() {
            @Override
            public void onDatabaseChangeNotification(DatabaseChangeEvent e) {
                if( e==null )return;
                StringBuilder sb = new StringBuilder();
                
                String linesep = System.lineSeparator();
                sb.append("DatabaseChangeEvent:").append(linesep);
                sb.append(e.toString());
                eventListenerPanel1.log(sb.toString()+linesep);
            }
        };
        try {
            dbChangeReg.addListener(dbChangeListener);
            dbChangeListeners.add(dbChangeListener);
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String getJdbcURL(){
        return "jdbc:oracle:thin:@"+oracleConnectPanel1.getHost()+":"+oracleConnectPanel1.getPort()+":"+oracleConnectPanel1.getSid();
    }
    
    private final Object connectSync = new Object();
    public void connect(){
        synchronized(connectSync){
            try {
                disconnect();
                
                // create connection
                OracleDriver oraDriver = new OracleDriver();
                Properties connProps = new Properties();
                connProps.setProperty("user", oracleConnectPanel1.getLogin());
                connProps.setProperty("password", new String(oracleConnectPanel1.getPassword()));
                OracleConnection oraConn = (OracleConnection)oraDriver.connect(getJdbcURL(), connProps);                
                setOracleConnection(oraConn);
                
                // assign reg
                dbChangeReg = createDatabaseChangeRegistration(oraConn);
                listenChanges(dbChangeReg);
                
                statementExecPanel1.setDbChangeReg(dbChangeReg);
                statementExecPanel1.setOracleConnection(oracleConnection);
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }    
    public void disconnect(){
        synchronized(connectSync){
            if( oracleConnection!=null ){
                try {
                    if( dbChangeReg!=null ){
                        dbChangeListeners.forEach( ls -> {
                            try{
                                dbChangeReg.removeListener(ls);
                            }catch(SQLException ex){
                                log(ex,"removeListener");
                            }
                        });
                        dbChangeListeners.clear();
                        
                        oracleConnection.unregisterDatabaseChangeNotification(dbChangeReg);
                        dbChangeReg = null;
                    }
                    
                    if( !oracleConnection.isClosed() ){
                        oracleConnection.close();
                    }                    
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                setOracleConnection(null);
                                
                statementExecPanel1.setDbChangeReg(null);
                statementExecPanel1.setOracleConnection(null);
            }
        }
    }
    
    protected void onConnectPressed(ActionEvent e){
        connect();
    }
    protected void onDisconnectPressed(ActionEvent e){
        disconnect();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        oracleConnectPanel1 = new xyz.cofe.demo.oracledbchnotif.OracleConnectPanel();
        statementExecPanel1 = new xyz.cofe.demo.oracledbchnotif.StatementExecPanel();
        eventListenerPanel1 = new xyz.cofe.demo.oracledbchnotif.EventListenerPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Oracle DB Change Notifier");
        setLocationByPlatform(true);

        jTabbedPane1.addTab("Connect", oracleConnectPanel1);
        jTabbedPane1.addTab("Query", statementExecPanel1);
        jTabbedPane1.addTab("Listener log", eventListenerPanel1);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private xyz.cofe.demo.oracledbchnotif.EventListenerPanel eventListenerPanel1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private xyz.cofe.demo.oracledbchnotif.OracleConnectPanel oracleConnectPanel1;
    private xyz.cofe.demo.oracledbchnotif.StatementExecPanel statementExecPanel1;
    // End of variables declaration//GEN-END:variables
}
