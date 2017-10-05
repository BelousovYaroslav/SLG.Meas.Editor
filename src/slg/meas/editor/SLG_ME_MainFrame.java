/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slg.meas.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;



/**
 *
 * @author yaroslav
 */
public class SLG_ME_MainFrame extends javax.swing.JFrame {   
    
    /**
     * Inner class for FileOpen dialog filter
     */
    class MyXMLFilter extends FileFilter {

        @Override
        public boolean accept( File f) {
            if (f.isDirectory()) {
                return true;
            }
            
            String extension = Utils.getExtension( f);
            if( extension != null) {
                if( extension.equals( Utils.raw))
                    return true;
                else
                    return false;
            }

            return false;
        }

        @Override
        public String getDescription() {
            return "RAW";
        }
        
    }
    
    static Logger logger = Logger.getLogger( SLG_ME_MainFrame.class);
    
    LoadFileRunnable m_pLoaderRunnable;
    Thread m_pLoaderThread;
    
    MainActionRunnable m_pMainActionRunnable;
    Thread m_pMainActionThread;
    
    Timer m_tRefreshStates;
    Timer m_tRefreshValues;
    
    File m_pFile1, m_pFile2, m_pFile3, m_pFile4, m_pFile5;
    
    Double m_dblFile1Duration = Double.NaN;
    Double m_dblFile2Duration = Double.NaN;
    Double m_dblFile3Duration = Double.NaN;
    Double m_dblFile4Duration = Double.NaN;
    Double m_dblFile5Duration = Double.NaN;
    
    double m_dblF1Tstart, m_dblF1Tstop;
    double m_dblF2Tstart, m_dblF2Tstop;
    double m_dblF3Tstart, m_dblF3Tstop;
    double m_dblF4Tstart, m_dblF4Tstop;
    double m_dblF5Tstart, m_dblF5Tstop;
    
    /**
     * Creates new form SLG_ME_MainFrame
     */
    public SLG_ME_MainFrame() {
        initComponents();
        
        m_tRefreshStates = new Timer( 100, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean bLoaderInAction = m_pLoaderRunnable != null && m_pLoaderThread.isAlive();
                prgrsbarFile01.setVisible( bLoaderInAction && m_pLoaderRunnable.GetFileN() == 0);
                prgrsbarFile02.setVisible( bLoaderInAction && m_pLoaderRunnable.GetFileN() == 1);
                prgrsbarFile03.setVisible( bLoaderInAction && m_pLoaderRunnable.GetFileN() == 2);
                prgrsbarFile04.setVisible( bLoaderInAction && m_pLoaderRunnable.GetFileN() == 3);
                prgrsbarFile05.setVisible( bLoaderInAction && m_pLoaderRunnable.GetFileN() == 4);
                
                chkFile1.setEnabled( !bLoaderInAction);
                chkFile2.setEnabled( !bLoaderInAction);
                chkFile3.setEnabled( !bLoaderInAction);
                chkFile4.setEnabled( !bLoaderInAction);
                chkFile5.setEnabled( !bLoaderInAction);
                
                edtFile1TStartValue.setEnabled( !bLoaderInAction);
                edtFile1TStopValue.setEnabled(  !bLoaderInAction);
                edtFile2TStartValue.setEnabled( !bLoaderInAction);
                edtFile2TStopValue.setEnabled(  !bLoaderInAction);
                edtFile3TStartValue.setEnabled( !bLoaderInAction);
                edtFile3TStopValue.setEnabled(  !bLoaderInAction);
                edtFile4TStartValue.setEnabled( !bLoaderInAction);
                edtFile4TStopValue.setEnabled(  !bLoaderInAction);
                edtFile5TStartValue.setEnabled( !bLoaderInAction);
                edtFile5TStopValue.setEnabled(  !bLoaderInAction);
                
                if( bLoaderInAction)
                    btnProcess.setEnabled( false);
                else {
                    btnProcess.setEnabled( 
                            chkFile1.isSelected() || chkFile2.isSelected() || 
                            chkFile3.isSelected() || chkFile4.isSelected() || 
                            chkFile5.isSelected());
                }
            }
            
        });
        m_tRefreshStates.start();
        
        m_tRefreshValues = new Timer( 100, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int nHou, nMin, nSec, nMillis;
                
                if( !Double.isNaN( m_dblFile1Duration)) {
                    nHou = ( int) ( m_dblFile1Duration.doubleValue() / 3600.);
                    nMin = ( int) ( m_dblFile1Duration.longValue() % 3600 / 60);
                    nSec = ( int) ( m_dblFile1Duration.longValue() % 60);
                    nMillis = ( int) ( ( m_dblFile1Duration - Math.floor( m_dblFile1Duration)) * 1000.);
                    
                    lblFile1Duration.setText( String.format( "%.2f = %02d:%02d:%02d.%03d", m_dblFile1Duration, nHou, nMin, nSec, nMillis));
                
                }
                else {
                    lblFile1Duration.setText("");
                }
                
                if( !Double.isNaN( m_dblFile2Duration)) {
                    nHou = ( int) ( m_dblFile2Duration.doubleValue() / 3600.);
                    nMin = ( int) ( m_dblFile2Duration.longValue() % 3600 / 60);
                    nSec = ( int) ( m_dblFile2Duration.longValue() % 60);
                    nMillis = ( int) ( ( m_dblFile2Duration - Math.floor( m_dblFile2Duration)) * 1000.);
                    
                    lblFile2Duration.setText( String.format( "%.2f = %02d:%02d:%02d.%03d", m_dblFile2Duration, nHou, nMin, nSec, nMillis));
                
                }
                else {
                    lblFile2Duration.setText("");
                }
                
                if( !Double.isNaN( m_dblFile3Duration)) {
                    nHou = ( int) ( m_dblFile3Duration.doubleValue() / 3600.);
                    nMin = ( int) ( m_dblFile3Duration.longValue() % 3600 / 60);
                    nSec = ( int) ( m_dblFile3Duration.longValue() % 60);
                    nMillis = ( int) ( ( m_dblFile3Duration - Math.floor( m_dblFile3Duration)) * 1000.);
                    
                    lblFile3Duration.setText( String.format( "%.2f = %02d:%02d:%02d.%03d", m_dblFile3Duration, nHou, nMin, nSec, nMillis));
                
                }
                else {
                    lblFile3Duration.setText("");
                }
                
                if( !Double.isNaN( m_dblFile4Duration)) {
                    nHou = ( int) ( m_dblFile4Duration.doubleValue() / 3600.);
                    nMin = ( int) ( m_dblFile4Duration.longValue() % 3600 / 60);
                    nSec = ( int) ( m_dblFile4Duration.longValue() % 60);
                    nMillis = ( int) ( ( m_dblFile4Duration - Math.floor( m_dblFile4Duration)) * 1000.);
                    
                    lblFile4Duration.setText( String.format( "%.2f = %02d:%02d:%02d.%03d", m_dblFile4Duration, nHou, nMin, nSec, nMillis));
                
                }
                else {
                    lblFile4Duration.setText("");
                }
                
                if( !Double.isNaN( m_dblFile5Duration)) {
                    nHou = ( int) ( m_dblFile5Duration.doubleValue() / 3600.);
                    nMin = ( int) ( m_dblFile5Duration.longValue() % 3600 / 60);
                    nSec = ( int) ( m_dblFile5Duration.longValue() % 60);
                    nMillis = ( int) ( ( m_dblFile5Duration - Math.floor( m_dblFile5Duration)) * 1000.);
                    
                    lblFile5Duration.setText( String.format( "%.2f = %02d:%02d:%02d.%03d", m_dblFile5Duration, nHou, nMin, nSec, nMillis));
                
                }
                else {
                    lblFile5Duration.setText("");
                }
            }
            
        });
        m_tRefreshValues.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        chkFile1 = new javax.swing.JCheckBox();
        lblFile1Value = new javax.swing.JLabel();
        lblFile1Duration = new javax.swing.JLabel();
        prgrsbarFile01 = new javax.swing.JProgressBar();
        lblFile1TStart = new javax.swing.JLabel();
        edtFile1TStartValue = new javax.swing.JTextField();
        lblFile1TStartSec = new javax.swing.JLabel();
        lblFile1TStop = new javax.swing.JLabel();
        edtFile1TStopValue = new javax.swing.JTextField();
        lblFile1TStopSec = new javax.swing.JLabel();
        chkFile2 = new javax.swing.JCheckBox();
        lblFile2Value = new javax.swing.JLabel();
        lblFile2Duration = new javax.swing.JLabel();
        prgrsbarFile02 = new javax.swing.JProgressBar();
        lblFile2TStart = new javax.swing.JLabel();
        edtFile2TStartValue = new javax.swing.JTextField();
        lblFile2TStartSec = new javax.swing.JLabel();
        lblFile2TStop = new javax.swing.JLabel();
        edtFile2TStopValue = new javax.swing.JTextField();
        lblFile2TStopSec = new javax.swing.JLabel();
        chkFile3 = new javax.swing.JCheckBox();
        lblFile3Value = new javax.swing.JLabel();
        lblFile3Duration = new javax.swing.JLabel();
        prgrsbarFile03 = new javax.swing.JProgressBar();
        lblFile3TStart = new javax.swing.JLabel();
        edtFile3TStartValue = new javax.swing.JTextField();
        lblFile3TStartSec = new javax.swing.JLabel();
        lblFile3TStop = new javax.swing.JLabel();
        edtFile3TStopValue = new javax.swing.JTextField();
        lblFile3TStopSec = new javax.swing.JLabel();
        chkFile4 = new javax.swing.JCheckBox();
        lblFile4Value = new javax.swing.JLabel();
        lblFile4Duration = new javax.swing.JLabel();
        prgrsbarFile04 = new javax.swing.JProgressBar();
        lblFile4TStart = new javax.swing.JLabel();
        edtFile4TStartValue = new javax.swing.JTextField();
        lblFile4TStartSec = new javax.swing.JLabel();
        lblFile4TStop = new javax.swing.JLabel();
        edtFile4TStopValue = new javax.swing.JTextField();
        lblFile4TStopSec = new javax.swing.JLabel();
        chkFile5 = new javax.swing.JCheckBox();
        lblFile5Value = new javax.swing.JLabel();
        lblFile5Duration = new javax.swing.JLabel();
        prgrsbarFile05 = new javax.swing.JProgressBar();
        lblFile5TStart = new javax.swing.JLabel();
        edtFile5TStartValue = new javax.swing.JTextField();
        lblFile5TStartSec = new javax.swing.JLabel();
        lblFile5TStop = new javax.swing.JLabel();
        edtFile5TStopValue = new javax.swing.JTextField();
        lblFile5TStopSec = new javax.swing.JLabel();
        prgrsbarMain = new javax.swing.JProgressBar();
        btnProcess = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Утилита склейки-нарезки bin-файлов измерения МЛГ. 2017.10.05.14-30");
        setPreferredSize(new java.awt.Dimension(650, 550));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(null);

        chkFile1.setText("Файл 1");
        chkFile1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkFile1ActionPerformed(evt);
            }
        });
        getContentPane().add(chkFile1);
        chkFile1.setBounds(10, 10, 80, 30);

        lblFile1Value.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(lblFile1Value);
        lblFile1Value.setBounds(90, 10, 360, 30);

        lblFile1Duration.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFile1Duration.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(lblFile1Duration);
        lblFile1Duration.setBounds(460, 10, 180, 30);
        getContentPane().add(prgrsbarFile01);
        prgrsbarFile01.setBounds(10, 42, 630, 6);

        lblFile1TStart.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFile1TStart.setText("c");
        getContentPane().add(lblFile1TStart);
        lblFile1TStart.setBounds(90, 50, 30, 30);

        edtFile1TStartValue.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(edtFile1TStartValue);
        edtFile1TStartValue.setBounds(120, 50, 70, 30);

        lblFile1TStartSec.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFile1TStartSec.setText("сек");
        getContentPane().add(lblFile1TStartSec);
        lblFile1TStartSec.setBounds(190, 50, 30, 30);

        lblFile1TStop.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFile1TStop.setText("по");
        getContentPane().add(lblFile1TStop);
        lblFile1TStop.setBounds(460, 50, 30, 30);

        edtFile1TStopValue.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(edtFile1TStopValue);
        edtFile1TStopValue.setBounds(490, 50, 70, 30);

        lblFile1TStopSec.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFile1TStopSec.setText("сек");
        getContentPane().add(lblFile1TStopSec);
        lblFile1TStopSec.setBounds(560, 50, 30, 30);

        chkFile2.setText("Файл 2");
        chkFile2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkFile2ActionPerformed(evt);
            }
        });
        getContentPane().add(chkFile2);
        chkFile2.setBounds(10, 100, 80, 30);

        lblFile2Value.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(lblFile2Value);
        lblFile2Value.setBounds(90, 100, 360, 30);

        lblFile2Duration.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFile2Duration.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(lblFile2Duration);
        lblFile2Duration.setBounds(460, 100, 180, 30);
        getContentPane().add(prgrsbarFile02);
        prgrsbarFile02.setBounds(10, 132, 630, 6);

        lblFile2TStart.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFile2TStart.setText("c");
        getContentPane().add(lblFile2TStart);
        lblFile2TStart.setBounds(90, 140, 30, 30);

        edtFile2TStartValue.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(edtFile2TStartValue);
        edtFile2TStartValue.setBounds(120, 140, 70, 30);

        lblFile2TStartSec.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFile2TStartSec.setText("сек");
        getContentPane().add(lblFile2TStartSec);
        lblFile2TStartSec.setBounds(190, 140, 30, 30);

        lblFile2TStop.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFile2TStop.setText("по");
        getContentPane().add(lblFile2TStop);
        lblFile2TStop.setBounds(460, 140, 30, 30);

        edtFile2TStopValue.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(edtFile2TStopValue);
        edtFile2TStopValue.setBounds(490, 140, 70, 30);

        lblFile2TStopSec.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFile2TStopSec.setText("сек");
        getContentPane().add(lblFile2TStopSec);
        lblFile2TStopSec.setBounds(560, 140, 30, 30);

        chkFile3.setText("Файл 3");
        chkFile3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkFile3ActionPerformed(evt);
            }
        });
        getContentPane().add(chkFile3);
        chkFile3.setBounds(10, 190, 80, 30);

        lblFile3Value.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(lblFile3Value);
        lblFile3Value.setBounds(90, 190, 360, 30);

        lblFile3Duration.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFile3Duration.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(lblFile3Duration);
        lblFile3Duration.setBounds(460, 190, 180, 30);
        getContentPane().add(prgrsbarFile03);
        prgrsbarFile03.setBounds(10, 222, 630, 6);

        lblFile3TStart.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFile3TStart.setText("c");
        getContentPane().add(lblFile3TStart);
        lblFile3TStart.setBounds(90, 230, 30, 30);

        edtFile3TStartValue.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(edtFile3TStartValue);
        edtFile3TStartValue.setBounds(120, 230, 70, 30);

        lblFile3TStartSec.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFile3TStartSec.setText("сек");
        getContentPane().add(lblFile3TStartSec);
        lblFile3TStartSec.setBounds(190, 230, 30, 30);

        lblFile3TStop.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFile3TStop.setText("по");
        getContentPane().add(lblFile3TStop);
        lblFile3TStop.setBounds(460, 230, 30, 30);

        edtFile3TStopValue.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(edtFile3TStopValue);
        edtFile3TStopValue.setBounds(490, 230, 70, 30);

        lblFile3TStopSec.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFile3TStopSec.setText("сек");
        getContentPane().add(lblFile3TStopSec);
        lblFile3TStopSec.setBounds(560, 230, 30, 30);

        chkFile4.setText("Файл 4");
        chkFile4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkFile4ActionPerformed(evt);
            }
        });
        getContentPane().add(chkFile4);
        chkFile4.setBounds(10, 280, 80, 30);

        lblFile4Value.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(lblFile4Value);
        lblFile4Value.setBounds(90, 280, 360, 30);

        lblFile4Duration.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFile4Duration.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(lblFile4Duration);
        lblFile4Duration.setBounds(460, 280, 180, 30);
        getContentPane().add(prgrsbarFile04);
        prgrsbarFile04.setBounds(10, 312, 630, 6);

        lblFile4TStart.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFile4TStart.setText("c");
        getContentPane().add(lblFile4TStart);
        lblFile4TStart.setBounds(90, 320, 30, 30);

        edtFile4TStartValue.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(edtFile4TStartValue);
        edtFile4TStartValue.setBounds(120, 320, 70, 30);

        lblFile4TStartSec.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFile4TStartSec.setText("сек");
        getContentPane().add(lblFile4TStartSec);
        lblFile4TStartSec.setBounds(190, 320, 30, 30);

        lblFile4TStop.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFile4TStop.setText("по");
        getContentPane().add(lblFile4TStop);
        lblFile4TStop.setBounds(460, 320, 30, 30);

        edtFile4TStopValue.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(edtFile4TStopValue);
        edtFile4TStopValue.setBounds(490, 320, 70, 30);

        lblFile4TStopSec.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFile4TStopSec.setText("сек");
        getContentPane().add(lblFile4TStopSec);
        lblFile4TStopSec.setBounds(560, 320, 30, 30);

        chkFile5.setText("Файл 5");
        chkFile5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkFile5ActionPerformed(evt);
            }
        });
        getContentPane().add(chkFile5);
        chkFile5.setBounds(10, 360, 80, 30);

        lblFile5Value.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(lblFile5Value);
        lblFile5Value.setBounds(90, 360, 360, 30);

        lblFile5Duration.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFile5Duration.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(lblFile5Duration);
        lblFile5Duration.setBounds(460, 360, 180, 30);
        getContentPane().add(prgrsbarFile05);
        prgrsbarFile05.setBounds(10, 392, 630, 6);

        lblFile5TStart.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFile5TStart.setText("c");
        getContentPane().add(lblFile5TStart);
        lblFile5TStart.setBounds(90, 400, 30, 30);

        edtFile5TStartValue.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(edtFile5TStartValue);
        edtFile5TStartValue.setBounds(120, 400, 70, 30);

        lblFile5TStartSec.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFile5TStartSec.setText("сек");
        getContentPane().add(lblFile5TStartSec);
        lblFile5TStartSec.setBounds(190, 400, 30, 30);

        lblFile5TStop.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFile5TStop.setText("по");
        getContentPane().add(lblFile5TStop);
        lblFile5TStop.setBounds(460, 400, 30, 30);

        edtFile5TStopValue.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(edtFile5TStopValue);
        edtFile5TStopValue.setBounds(490, 400, 70, 30);

        lblFile5TStopSec.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFile5TStopSec.setText("сек");
        getContentPane().add(lblFile5TStopSec);
        lblFile5TStopSec.setBounds(560, 400, 30, 30);
        getContentPane().add(prgrsbarMain);
        prgrsbarMain.setBounds(10, 450, 630, 20);

        btnProcess.setText("Запустить обработку");
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        getContentPane().add(btnProcess);
        btnProcess.setBounds(10, 480, 630, 40);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chkFile1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFile1ActionPerformed
        if( chkFile1.isSelected()) {
            final JFileChooser fc = new JFileChooser();
            fc.setFileFilter( new MyXMLFilter());

            String strSLGRootEnvVar = System.getenv( "SLG_ROOT");
            if( strSLGRootEnvVar != null)
                fc.setCurrentDirectory( new File( strSLGRootEnvVar + "/data"));

            int returnVal = fc.showOpenDialog( this);
            if( returnVal == JFileChooser.APPROVE_OPTION) {
                //This is where a real application would open the file.
                m_pFile1 = fc.getSelectedFile();
                lblFile1Value.setText( m_pFile1.getName());
                m_pLoaderRunnable = new LoadFileRunnable( m_pFile1.getAbsolutePath(), this, 0);
                m_pLoaderThread = new Thread( m_pLoaderRunnable);
                m_pLoaderThread.start();

            } else {
                logger.info("LoadFile cancelled.");
                chkFile1.setSelected( false);
            }
        }
        else {
            m_pFile1 = null;
            lblFile1Value.setText("");
            m_dblFile1Duration = Double.NaN;
            edtFile1TStartValue.setText( "");
            edtFile1TStopValue.setText(  "");
        }
    }//GEN-LAST:event_chkFile1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if( m_pLoaderThread != null && m_pLoaderThread.isAlive()) {
            m_pLoaderThread.interrupt();
        }
        m_tRefreshStates.stop();
        m_tRefreshValues.stop();
    }//GEN-LAST:event_formWindowClosing

    private void chkFile2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFile2ActionPerformed
        if( chkFile2.isSelected()) {
            final JFileChooser fc = new JFileChooser();
            fc.setFileFilter( new MyXMLFilter());

            String strSLGRootEnvVar = System.getenv( "SLG_ROOT");
            if( strSLGRootEnvVar != null)
                fc.setCurrentDirectory( new File( strSLGRootEnvVar + "/data"));

            int returnVal = fc.showOpenDialog( this);
            if( returnVal == JFileChooser.APPROVE_OPTION) {
                //This is where a real application would open the file.
                m_pFile2 = fc.getSelectedFile();
                lblFile2Value.setText( m_pFile2.getName());
                m_pLoaderRunnable = new LoadFileRunnable( m_pFile2.getAbsolutePath(), this, 1);
                m_pLoaderThread = new Thread( m_pLoaderRunnable);
                m_pLoaderThread.start();

            } else {
                logger.info("LoadFile cancelled.");
                chkFile2.setSelected( false);
            }
        }
        else {
            m_pFile2 = null;
            lblFile2Value.setText("");
            m_dblFile2Duration = Double.NaN;
            edtFile2TStartValue.setText( "");
            edtFile2TStopValue.setText(  "");
        }
    }//GEN-LAST:event_chkFile2ActionPerformed

    private void chkFile3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFile3ActionPerformed
        if( chkFile3.isSelected()) {
            final JFileChooser fc = new JFileChooser();
            fc.setFileFilter( new MyXMLFilter());

            String strSLGRootEnvVar = System.getenv( "SLG_ROOT");
            if( strSLGRootEnvVar != null)
                fc.setCurrentDirectory( new File( strSLGRootEnvVar + "/data"));

            int returnVal = fc.showOpenDialog( this);
            if( returnVal == JFileChooser.APPROVE_OPTION) {
                //This is where a real application would open the file.
                m_pFile3 = fc.getSelectedFile();
                lblFile3Value.setText( m_pFile3.getName());
                m_pLoaderRunnable = new LoadFileRunnable( m_pFile1.getAbsolutePath(), this, 2);
                m_pLoaderThread = new Thread( m_pLoaderRunnable);
                m_pLoaderThread.start();

            } else {
                logger.info("LoadFile cancelled.");
                chkFile3.setSelected( false);
            }
        }
        else {
            m_pFile3 = null;
            lblFile3Value.setText("");
            m_dblFile3Duration = Double.NaN;
            edtFile3TStartValue.setText( "");
            edtFile3TStopValue.setText(  "");
        }
    }//GEN-LAST:event_chkFile3ActionPerformed

    private void chkFile4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFile4ActionPerformed
        if( chkFile4.isSelected()) {
            final JFileChooser fc = new JFileChooser();
            fc.setFileFilter( new MyXMLFilter());

            String strSLGRootEnvVar = System.getenv( "SLG_ROOT");
            if( strSLGRootEnvVar != null)
                fc.setCurrentDirectory( new File( strSLGRootEnvVar + "/data"));

            int returnVal = fc.showOpenDialog( this);
            if( returnVal == JFileChooser.APPROVE_OPTION) {
                //This is where a real application would open the file.
                m_pFile4 = fc.getSelectedFile();
                lblFile4Value.setText( m_pFile4.getName());
                m_pLoaderRunnable = new LoadFileRunnable( m_pFile4.getAbsolutePath(), this, 3);
                m_pLoaderThread = new Thread( m_pLoaderRunnable);
                m_pLoaderThread.start();

            } else {
                logger.info("LoadFile cancelled.");
                chkFile4.setSelected( false);
            }
        }
        else {
            m_pFile4 = null;
            lblFile4Value.setText("");
            m_dblFile4Duration = Double.NaN;
            edtFile4TStartValue.setText( "");
            edtFile4TStopValue.setText(  "");
        }
    }//GEN-LAST:event_chkFile4ActionPerformed

    private void chkFile5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFile5ActionPerformed
        if( chkFile5.isSelected()) {
            final JFileChooser fc = new JFileChooser();
            fc.setFileFilter( new MyXMLFilter());

            String strSLGRootEnvVar = System.getenv( "SLG_ROOT");
            if( strSLGRootEnvVar != null)
                fc.setCurrentDirectory( new File( strSLGRootEnvVar + "/data"));

            int returnVal = fc.showOpenDialog( this);
            if( returnVal == JFileChooser.APPROVE_OPTION) {
                //This is where a real application would open the file.
                m_pFile5 = fc.getSelectedFile();
                lblFile5Value.setText( m_pFile5.getName());
                m_pLoaderRunnable = new LoadFileRunnable( m_pFile5.getAbsolutePath(), this, 4);
                m_pLoaderThread = new Thread( m_pLoaderRunnable);
                m_pLoaderThread.start();

            } else {
                logger.info("LoadFile cancelled.");
                chkFile5.setSelected( false);
            }
        }
        else {
            m_pFile5 = null;
            lblFile5Value.setText("");
            m_dblFile5Duration = Double.NaN;
            edtFile5TStartValue.setText( "");
            edtFile5TStopValue.setText(  "");
        }
    }//GEN-LAST:event_chkFile5ActionPerformed

    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        boolean bOk;
        
        //FILE1
        if( chkFile1.isSelected()) {
            
            bOk = true;
            if( edtFile1TStartValue.getText().isEmpty())
                edtFile1TStartValue.setText( "0.00");
                        
            try { m_dblF1Tstart = Double.parseDouble( edtFile1TStartValue.getText()); }
            catch( NumberFormatException ex) {
                logger.warn( "NumberFormatException for T1START", ex);
                bOk = false;
            }
            
            if( m_dblF1Tstart < 0 || m_dblF1Tstart > m_dblFile1Duration) bOk = false;
            
            if( bOk == false) {
                MessageBoxError( "Некорректно задано начальное время для файла 1", "SLG_ME");
                return;
            }
            
            
            bOk = true;
            if( edtFile1TStopValue.getText().isEmpty()) {
                String str = String.format( "%.02f", m_dblFile1Duration);
                str = str.replace( ',', '.');
                edtFile1TStopValue.setText( str);
            }
            
            try { m_dblF1Tstop = Double.parseDouble( edtFile1TStopValue.getText()); }
            catch( NumberFormatException ex) {
                logger.warn( "NumberFormatException for T1STOP", ex);
                bOk = false;
            }
            
            if( m_dblF1Tstop < 0 || m_dblF1Tstop > m_dblFile1Duration) bOk = false;
            if( m_dblF1Tstart > m_dblF1Tstop) bOk = false;
            
            if( bOk == false) {
                MessageBoxError( "Некорректно задано конечное время для файла 1", "SLG_ME");
                return;
            }
        }
        
        //FILE2
        if( chkFile2.isSelected()) {
            
            bOk = true;
            if( edtFile2TStartValue.getText().isEmpty())
                edtFile2TStartValue.setText( "0.00");
                        
            try { m_dblF2Tstart = Double.parseDouble( edtFile2TStartValue.getText()); }
            catch( NumberFormatException ex) {
                logger.warn( "NumberFormatException for T2START", ex);
                bOk = false;
            }
            
            if( m_dblF2Tstart < 0 || m_dblF2Tstart > m_dblFile2Duration) bOk = false;
            
            if( bOk == false) {
                MessageBoxError( "Некорректно задано начальное время для файла 2", "SLG_ME");
                return;
            }
            
            
            bOk = true;
            if( edtFile2TStopValue.getText().isEmpty()) {
                String str = String.format( "%.02f", m_dblFile2Duration);
                str = str.replace( ',', '.');
                edtFile2TStopValue.setText( str);
            }
            
            try { m_dblF2Tstop = Double.parseDouble( edtFile2TStopValue.getText()); }
            catch( NumberFormatException ex) {
                logger.warn( "NumberFormatException for T2STOP", ex);
                bOk = false;
            }
            
            if( m_dblF2Tstop < 0 || m_dblF2Tstop > m_dblFile2Duration) bOk = false;
            if( m_dblF2Tstart > m_dblF2Tstop) bOk = false;
            
            if( bOk == false) {
                MessageBoxError( "Некорректно задано конечное время для файла 2", "SLG_ME");
                return;
            }
        }
        
        //FILE3
        if( chkFile3.isSelected()) {
            
            bOk = true;
            if( edtFile3TStartValue.getText().isEmpty())
                edtFile3TStartValue.setText( "0.00");
                        
            try { m_dblF3Tstart = Double.parseDouble( edtFile3TStartValue.getText()); }
            catch( NumberFormatException ex) {
                logger.warn( "NumberFormatException for T3START", ex);
                bOk = false;
            }
            
            if( m_dblF3Tstart < 0 || m_dblF3Tstart > m_dblFile3Duration) bOk = false;
            
            if( bOk == false) {
                MessageBoxError( "Некорректно задано начальное время для файла 3", "SLG_ME");
                return;
            }
            
            
            bOk = true;
            if( edtFile3TStopValue.getText().isEmpty()) {
                String str = String.format( "%.02f", m_dblFile3Duration);
                str = str.replace( ',', '.');
                edtFile3TStopValue.setText( str);
            }
            
            try { m_dblF3Tstop = Double.parseDouble( edtFile3TStopValue.getText()); }
            catch( NumberFormatException ex) {
                logger.warn( "NumberFormatException for T3STOP", ex);
                bOk = false;
            }
            
            if( m_dblF3Tstop < 0 || m_dblF3Tstop > m_dblFile3Duration) bOk = false;
            if( m_dblF3Tstart > m_dblF3Tstop) bOk = false;
            
            if( bOk == false) {
                MessageBoxError( "Некорректно задано конечное время для файла 3", "SLG_ME");
                return;
            }
        }
        
        //FILE4
        if( chkFile4.isSelected()) {
            
            bOk = true;
            if( edtFile4TStartValue.getText().isEmpty())
                edtFile4TStartValue.setText( "0.00");
                        
            try { m_dblF4Tstart = Double.parseDouble( edtFile4TStartValue.getText()); }
            catch( NumberFormatException ex) {
                logger.warn( "NumberFormatException for T4START", ex);
                bOk = false;
            }
            
            if( m_dblF4Tstart < 0 || m_dblF4Tstart > m_dblFile4Duration) bOk = false;
            
            if( bOk == false) {
                MessageBoxError( "Некорректно задано начальное время для файла 4", "SLG_ME");
                return;
            }
            
            
            bOk = true;
            if( edtFile4TStopValue.getText().isEmpty()) {
                String str = String.format( "%.02f", m_dblFile4Duration);
                str = str.replace( ',', '.');
                edtFile4TStopValue.setText( str);
            }
            
            try { m_dblF4Tstop = Double.parseDouble( edtFile4TStopValue.getText()); }
            catch( NumberFormatException ex) {
                logger.warn( "NumberFormatException for T4STOP", ex);
                bOk = false;
            }
            
            if( m_dblF4Tstop < 0 || m_dblF4Tstop > m_dblFile4Duration) bOk = false;
            if( m_dblF4Tstart > m_dblF4Tstop) bOk = false;
            
            if( bOk == false) {
                MessageBoxError( "Некорректно задано конечное время для файла 4", "SLG_ME");
                return;
            }
        }
        
        //FILE5
        if( chkFile5.isSelected()) {
            
            bOk = true;
            if( edtFile5TStartValue.getText().isEmpty())
                edtFile5TStartValue.setText( "0.00");
                        
            try { m_dblF5Tstart = Double.parseDouble( edtFile5TStartValue.getText()); }
            catch( NumberFormatException ex) {
                logger.warn( "NumberFormatException for T5START", ex);
                bOk = false;
            }
            
            if( m_dblF5Tstart < 0 || m_dblF5Tstart > m_dblFile5Duration) bOk = false;
            
            if( bOk == false) {
                MessageBoxError( "Некорректно задано начальное время для файла 5", "SLG_ME");
                return;
            }
            
            
            bOk = true;
            if( edtFile5TStopValue.getText().isEmpty()) {
                String str = String.format( "%.02f", m_dblFile5Duration);
                str = str.replace( ',', '.');
                edtFile5TStopValue.setText( str);
            }
            
            try { m_dblF5Tstop = Double.parseDouble( edtFile5TStopValue.getText()); }
            catch( NumberFormatException ex) {
                logger.warn( "NumberFormatException for T5STOP", ex);
                bOk = false;
            }
            
            if( m_dblF5Tstop < 0 || m_dblF5Tstop > m_dblFile5Duration) bOk = false;
            if( m_dblF5Tstart > m_dblF5Tstop) bOk = false;
            
            if( bOk == false) {
                MessageBoxError( "Некорректно задано конечное время для файла 5", "SLG_ME");
                return;
            }
        }
        
        m_pMainActionRunnable = new MainActionRunnable( this);
        m_pMainActionThread = new Thread( m_pMainActionRunnable);
        m_pMainActionThread.start();
    }//GEN-LAST:event_btnProcessActionPerformed

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
            java.util.logging.Logger.getLogger(SLG_ME_MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SLG_ME_MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SLG_ME_MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SLG_ME_MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        String strSLGRootEnvVar = System.getenv( "SLG_ROOT");
        if( strSLGRootEnvVar == null) {
            MessageBoxError( "Не задана переменная окружения SLG_ROOT!", "SLG_ME");
            return;
        }
        
        String strlog4jPropertiesFile = strSLGRootEnvVar + "/etc/log4j.meas.edit.properties";
        File file = new File( strlog4jPropertiesFile);
        if(!file.exists()) {
            System.out.println("It is not possible to load the given log4j properties file :" + file.getAbsolutePath());
            BasicConfigurator.configure();
        }
        else
            PropertyConfigurator.configure( file.getAbsolutePath());
        
        logger.info( "SLG_ME_MainFrame::main(): in. Start point!");
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SLG_ME_MainFrame().setVisible(true);
            }
        });
    }

    /**
     * Функция для сообщения пользователю информационного сообщения
     * @param strMessage сообщение
     * @param strTitleBar заголовок
     */
    public static void MessageBoxInfo( String strMessage, String strTitleBar)
    {
        JOptionPane.showMessageDialog( null, strMessage, strTitleBar, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Функция для сообщения пользователю сообщения об ошибке
     * @param strMessage сообщение
     * @param strTitleBar заголовок
     */
    public static void MessageBoxError( String strMessage, String strTitleBar)
    {
        JOptionPane.showMessageDialog( null, strMessage, strTitleBar, JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Функция для опроса пользователя с ответом ДА - НЕТ
     * @param strMessage сообщение
     * @param strTitleBar заголовок
     * @return JOptionPane.YES_OPTION<br>или<br>
     * JOptionPane.NO_OPTION
     */
    public static int MessageBoxYesNo( String strMessage, String strTitleBar)
    {
        UIManager.put("OptionPane.noButtonText", "Нет");
        UIManager.put("OptionPane.okButtonText", "Согласен");
        UIManager.put("OptionPane.yesButtonText", "Да");
        
        int nReply = JOptionPane.showConfirmDialog( null, strMessage, strTitleBar, JOptionPane.YES_NO_OPTION);
        return nReply;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnProcess;
    public javax.swing.JCheckBox chkFile1;
    public javax.swing.JCheckBox chkFile2;
    public javax.swing.JCheckBox chkFile3;
    public javax.swing.JCheckBox chkFile4;
    public javax.swing.JCheckBox chkFile5;
    private javax.swing.JTextField edtFile1TStartValue;
    private javax.swing.JTextField edtFile1TStopValue;
    private javax.swing.JTextField edtFile2TStartValue;
    private javax.swing.JTextField edtFile2TStopValue;
    private javax.swing.JTextField edtFile3TStartValue;
    private javax.swing.JTextField edtFile3TStopValue;
    private javax.swing.JTextField edtFile4TStartValue;
    private javax.swing.JTextField edtFile4TStopValue;
    private javax.swing.JTextField edtFile5TStartValue;
    private javax.swing.JTextField edtFile5TStopValue;
    private javax.swing.JLabel lblFile1Duration;
    private javax.swing.JLabel lblFile1TStart;
    private javax.swing.JLabel lblFile1TStartSec;
    private javax.swing.JLabel lblFile1TStop;
    private javax.swing.JLabel lblFile1TStopSec;
    private javax.swing.JLabel lblFile1Value;
    private javax.swing.JLabel lblFile2Duration;
    private javax.swing.JLabel lblFile2TStart;
    private javax.swing.JLabel lblFile2TStartSec;
    private javax.swing.JLabel lblFile2TStop;
    private javax.swing.JLabel lblFile2TStopSec;
    private javax.swing.JLabel lblFile2Value;
    private javax.swing.JLabel lblFile3Duration;
    private javax.swing.JLabel lblFile3TStart;
    private javax.swing.JLabel lblFile3TStartSec;
    private javax.swing.JLabel lblFile3TStop;
    private javax.swing.JLabel lblFile3TStopSec;
    private javax.swing.JLabel lblFile3Value;
    private javax.swing.JLabel lblFile4Duration;
    private javax.swing.JLabel lblFile4TStart;
    private javax.swing.JLabel lblFile4TStartSec;
    private javax.swing.JLabel lblFile4TStop;
    private javax.swing.JLabel lblFile4TStopSec;
    private javax.swing.JLabel lblFile4Value;
    private javax.swing.JLabel lblFile5Duration;
    private javax.swing.JLabel lblFile5TStart;
    private javax.swing.JLabel lblFile5TStartSec;
    private javax.swing.JLabel lblFile5TStop;
    private javax.swing.JLabel lblFile5TStopSec;
    private javax.swing.JLabel lblFile5Value;
    public javax.swing.JProgressBar prgrsbarFile01;
    public javax.swing.JProgressBar prgrsbarFile02;
    public javax.swing.JProgressBar prgrsbarFile03;
    public javax.swing.JProgressBar prgrsbarFile04;
    public javax.swing.JProgressBar prgrsbarFile05;
    public javax.swing.JProgressBar prgrsbarMain;
    // End of variables declaration//GEN-END:variables
}
