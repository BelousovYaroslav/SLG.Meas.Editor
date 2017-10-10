/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slg.meas.editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import javax.swing.JCheckBox;
import org.apache.log4j.Logger;

/**
 *
 * @author yaroslav
 */
public class MainActionRunnable implements Runnable {

    static Logger logger = Logger.getLogger( MainActionRunnable.class);
    
    private SLG_ME_MainFrame m_pParent;
    public void SetParent( SLG_ME_MainFrame pParent) { m_pParent = pParent;}
    
    @Override
    public void run() {
        double dblTotalTimeRunning = 0.;
        double dblTotalTimeGoal = 0.;
        if( m_pParent.chkFile1.isSelected())
            dblTotalTimeGoal += m_pParent.m_dblF1Tstop - m_pParent.m_dblF1Tstart;
        if( m_pParent.chkFile2.isSelected())
            dblTotalTimeGoal += m_pParent.m_dblF2Tstop - m_pParent.m_dblF2Tstart;
        if( m_pParent.chkFile3.isSelected())
            dblTotalTimeGoal += m_pParent.m_dblF3Tstop - m_pParent.m_dblF3Tstart;
        if( m_pParent.chkFile4.isSelected())
            dblTotalTimeGoal += m_pParent.m_dblF4Tstop - m_pParent.m_dblF4Tstart;
        if( m_pParent.chkFile5.isSelected())
            dblTotalTimeGoal += m_pParent.m_dblF5Tstop - m_pParent.m_dblF5Tstart;
        
        String strSLGRootEnvVar = System.getenv( "SLG_ROOT");
        File fileW = new File( strSLGRootEnvVar + "/data/paste.bin");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream( fileW, false);
        } catch( FileNotFoundException ex) {
            logger.warn( "Output. FileNotFoundException", ex);
            return;
        }
        
        for( int i=0; i<5; i++) {
            JCheckBox chk;
            File file;
            double dblTMin, dblTMax;
            switch( i) {
                case 0:  chk = m_pParent.chkFile1; file = m_pParent.m_pFile1; dblTMin = m_pParent.m_dblF1Tstart; dblTMax = m_pParent.m_dblF1Tstop; break;
                case 1:  chk = m_pParent.chkFile2; file = m_pParent.m_pFile2; dblTMin = m_pParent.m_dblF2Tstart; dblTMax = m_pParent.m_dblF2Tstop; break;
                case 2:  chk = m_pParent.chkFile3; file = m_pParent.m_pFile3; dblTMin = m_pParent.m_dblF3Tstart; dblTMax = m_pParent.m_dblF3Tstop; break;
                case 3:  chk = m_pParent.chkFile4; file = m_pParent.m_pFile4; dblTMin = m_pParent.m_dblF4Tstart; dblTMax = m_pParent.m_dblF4Tstop; break;
                default: chk = m_pParent.chkFile5; file = m_pParent.m_pFile5; dblTMin = m_pParent.m_dblF5Tstart; dblTMax = m_pParent.m_dblF5Tstop; break;
            }
            
            if( chk.isSelected()) {
                try {
                    FileInputStream fis = new FileInputStream( file);
                    double dblDuration = 0.;
                    do {
                        byte [] bts = new byte[10];
                        fis.read( bts);
                        int nL = bts[8]; int nH = bts[9];
                    
                        int nDuration = ( nH << 8) + nL;
                        //logger.debug( "nDuration=" + nDuration);
                    
                        dblDuration += nDuration / 2611250.;
                        if( dblDuration > dblTMin) {
                            dblTotalTimeRunning += nDuration / 2611250.;
                            fos.write( bts);
                        }
                        
                        m_pParent.prgrsbarMain.setValue( ( int) ( dblTotalTimeRunning / dblTotalTimeGoal * 100.));
                        
                        if( dblDuration > dblTMax)
                            break;
                        
                    } while( fis.available() > 0);
                    
                } catch(FileNotFoundException ex) {
                    logger.warn( "i=" + i + ". FileNotFoundException", ex);
                } catch (IOException ex) {
                    logger.warn( "i=" + i + ". IOException", ex);
                }
                
                    
            }
            
            m_pParent.prgrsbarMain.setValue( 0);
            
        }
        
    }
    
    public MainActionRunnable( SLG_ME_MainFrame pParent) {
        m_pParent = pParent;
    }
}
