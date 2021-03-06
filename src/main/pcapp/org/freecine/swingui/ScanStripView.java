/*
Copyright (C) 2008 Harri Kaimio
 
This file is part of Freecine
 
Freecine is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by the Free 
Software Foundation; either version 3 of the License, or (at your option) 
any later version.
 
This program is distributed in the hope that it will be useful, but WITHOUT 
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with 
this program; if not, see <http://www.gnu.org/licenses>.
 
Additional permission under GNU GPL version 3 section 7
 
If you modify this Program, or any covered work, by linking or combining it 
with Java Advanced Imaging (or a modified version of that library), containing 
parts covered by the terms of Java Distribution License, or leJOS, containing 
parts covered by the terms of Mozilla Public License, the licensors of this 
Program grant you additional permission to convey the resulting work. 
 */

package org.freecine.swingui;

import org.freecine.filmscan.ScanStrip;
import org.freecine.filmscan.Perforation;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 ScanStripView shows the whole scanned strip as a small iamge and allows 
 navigation to any frame in it.
 */
public class ScanStripView extends javax.swing.JPanel {
    
    /** Creates new form ScanStripView */
    public ScanStripView() {
        initComponents();
    }

    private ScanStrip strip;
    
    
    /** This method is called from within the constructor to
     initialize the form.
     WARNING: Do NOT modify this code. The content of this method is
     always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                formMouseExited(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 46, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 523, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        mouseOverPerf = getFrameAt( evt.getX(), evt.getY() );
        repaint();
    }//GEN-LAST:event_formMouseMoved

    private void formMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseExited
        mouseOverPerf = -1;
        repaint();
    }//GEN-LAST:event_formMouseExited

    
    
    /**
     Get the strip currently shown
     @return The current strip
     */
    public ScanStrip getStrip() {
        return strip;
    }

    /**
     Set the strip
     @param strip The strip to be shown
     */
    public void setStrip( ScanStrip strip ) {
        if ( strip != this.strip ) {
        this.strip = strip;
        }
        repaint();
    }
    
    public void setSelectedFrame( int n ) {
        selectedFrame = n;
        selectedPerf = -1;
    }
    
    public int getSelectedFrame() {
        return selectedFrame;
    }
    
    public void setSelectedPerforation( int n ) {
        selectedPerf = n;
        selectedFrame = -1;
    }
    
    public int getSelectedPerforation() {
        return selectedPerf;
    }
    
    private int getFrameAt( int x, int y ) {
        if ( x > stripDrawWidth ) {
            return -1;
        }
        int closestDist = Integer.MAX_VALUE;
        int perf = -1;
        for ( int n = 0 ; n < perfCoordY.length && perfCoordY[n] >= 0 ; n++ ) {
            int d = Math.abs( y-perfCoordY[n] );
            if (d < closestDist) {
                perf = n;
                closestDist = d;
            }
        }
        return perf;
    }
    
    /**
     Width of the strip drawn
     */
    int stripDrawWidth = 0;
    
    int[] perfCoordY = new int[50];
    
    int selectedPerf = -1;
    
    int selectedFrame = -1;
    
    int mouseOverPerf = -1;
    
    /**
     Paint the strip.
     @param g
     */
    @Override
    public void paint( Graphics g ) {
        super.paint( g );
        if ( strip == null ) {
            return;
        }
        // First, calculate the scaling needed for the strip
        int compWidth = getWidth();
        int compHeight = getHeight();

        int stripWidth = strip.getStripImage().getWidth();
        int stripHeight = strip.getStripImage().getHeight();

        double scaleH = ((double) compWidth) / ((double) stripWidth);
        double scaleV = ((double) compHeight) / ((double) stripHeight);
        double scale = Math.min( scaleH, scaleV );
        stripDrawWidth = (int) (scale * stripWidth);
        int stripDrawHeight = (int) (scale * stripHeight);
        
        Graphics2D g2 = (Graphics2D) ((Graphics2D) g).create();
        g2.setPaint( Color.BLACK );
        g2.fillRect( 0, 0, (int) (scale * stripWidth), (int) (scale * stripHeight) );

        // Draw the perforations

        // Dimensions of S8 perforation
        int perfHeight = (int) (200 * scale);
        int perfWidth = (int) (160 * scale);
        int frameHeight = (int)(800.0 * scale );
        
        for ( int n = 0; n < strip.getPerforationCount(); n++ ) {
            Perforation p = strip.getPerforation( n );
            int perfX = (int) ((p.x - 140) * scale);
            int perfY = (int) ((p.y - 100) * scale);
            perfCoordY[n] = perfY;
            g2.setPaint( selectedPerf == n ? Color.RED : Color.WHITE );
            g2.fillRect( perfX, perfY, perfWidth, perfHeight );
            
            // Decorate the frame if needed

            
            int frame = n;
            int fx = perfX + 2;
            int fy = perfY - frameHeight / 2;
            int fw = Math.min( stripDrawWidth - fx - 2, stripDrawWidth * 4 / 3 );
            int fh = frameHeight;
            if ( frame >= 0 && selectedFrame == frame ) {
                // This frame is selected
                g2.setColor( new Color( 192, 64, 64, 128 ) );
                g2.fillRect( fx, fy, fw, fh );
            }
            if ( strip.getFirstUsable() > frame || strip.getLastUsable() < frame ) {
                // This frame is not usable (either set as such or incomplete)
                g2.setColor( Color.RED );
                g2.drawLine( fx, fy, fx + fw, fy + fh );
                g2.drawLine( fx+fw, fy, fx, fy + fh );
            }
        }
        perfCoordY[strip.getPerforationCount()] = -1;
        
        if ( mouseOverPerf >= 0 ) {
            // Highlight the area where mouse is
            int top = (mouseOverPerf == 0) ? 
                0 : 
                (perfCoordY[mouseOverPerf] + perfCoordY[mouseOverPerf - 1]) / 2;
            int bot = perfCoordY[mouseOverPerf + 1] < 0 ? 
                stripDrawHeight : 
                (perfCoordY[mouseOverPerf + 1] + perfCoordY[mouseOverPerf]) / 2;
            g2.setPaint( new Color( 128, 128, 128, 128 ) );
            g2.fillRect( 0, top, stripDrawWidth, bot - top );
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
