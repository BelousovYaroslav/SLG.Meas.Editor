/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slg.meas.editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JProgressBar;
import org.apache.log4j.Logger;

/**
 *
 * @author yaroslav
 */
public class LoadFileRunnable implements Runnable {

    static Logger logger = Logger.getLogger( LoadFileRunnable.class);
    
    private SLG_ME_MainFrame m_pParent;
    public void SetParent( SLG_ME_MainFrame pParent) { m_pParent = pParent;}
    
    private String m_strFilePathName;
    public void SetFilePathName( String strFilePathName) { m_strFilePathName = strFilePathName;}
    
    private int m_nFile;
    public void SetFileN( int nFile) { m_nFile = nFile;}
    public int GetFileN() { return m_nFile;}
    
    @Override
    public void run() {
        
        switch( m_nFile) {
            case 0: m_pParent.m_dblFile1Duration = Double.NaN; break;
            case 1: m_pParent.m_dblFile2Duration = Double.NaN; break;
            case 2: m_pParent.m_dblFile3Duration = Double.NaN; break;
            case 3: m_pParent.m_dblFile4Duration = Double.NaN; break;
            case 4: m_pParent.m_dblFile5Duration = Double.NaN; break;
        }
        
        try {
            File file = new File( m_strFilePathName);
            long lLen = file.length();
            long lProgress = 0;
            if( file.exists()) {
                
                JProgressBar bar = null;
                switch( m_nFile) {
                    case 1: bar = m_pParent.prgrsbarFile02; break;
                    case 2: bar = m_pParent.prgrsbarFile03; break;
                    case 3: bar = m_pParent.prgrsbarFile04; break;
                    case 4: bar = m_pParent.prgrsbarFile05; break;
                    default: bar = m_pParent.prgrsbarFile01; break;
                }                
                bar.setValue( 0);
                
                FileInputStream fis = new FileInputStream( file);

                double dblDuration = 0;
                do {
                    byte [] bts = new byte[10];
                    fis.read( bts);
                    int nL = bts[8]; int nH = bts[9];
                    
                    int nDuration = ( nH << 8) + nL;
                    //logger.debug( "nDuration=" + nDuration);
                    
                    dblDuration += nDuration / 2611250.;
                            
                    switch( m_nFile) {
                        case 0: m_pParent.m_dblFile1Duration = dblDuration; break;
                        case 1: m_pParent.m_dblFile2Duration = dblDuration; break;
                        case 2: m_pParent.m_dblFile3Duration = dblDuration; break;
                        case 3: m_pParent.m_dblFile4Duration = dblDuration; break;
                        case 4: m_pParent.m_dblFile5Duration = dblDuration; break;
                    }
                    
                    lProgress += 10;
                    
                    bar.setValue( ( int) ( ( double) lProgress / ( double) lLen * 100.));
                    
                } while( fis.available() > 10);
            }
        } catch ( FileNotFoundException ex) {
            logger.error( "FileNotFoundException", ex);
        } catch (IOException ex) {
            logger.error( "IOException", ex);
        }
    }
    
    public LoadFileRunnable( String strFilePathName, SLG_ME_MainFrame pParent, int nFile) {
        m_strFilePathName = strFilePathName;
        m_nFile = nFile;
        m_pParent = pParent;
    }
}
