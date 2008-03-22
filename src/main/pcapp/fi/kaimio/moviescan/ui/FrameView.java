/*
 * FrameView.java
 *
 * Created on 21. maaliskuuta 2008, 11:56
 */

package fi.kaimio.moviescan.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.ColorModel;
import java.awt.image.ColorModel;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.operator.ColorConvertDescriptor;
import javax.media.jai.operator.LookupDescriptor;
import javax.media.jai.operator.ScaleDescriptor;

/**
 Simple viewer component for movie frames.
 
 @author  harri
 */
public class FrameView extends javax.swing.JPanel {
    
    /** Creates new form FrameView */
    public FrameView() {
        initComponents();
        calcLUT();
    }
 
    /**
     The image to show
     */
    RenderedImage img;
    
    /**
     Cached copy scaled to the screen resolution.
     */
    RenderedImage scaledImage = null;
    
    /**
     Set the image displayed in this window
     @param img The image to display
     */
    public void setImage( RenderedImage img ) {
        this.img = img;
        scaledImage = null;
        repaint();
    }
    
    /**
     Get the currently displayed image.
     @return
     */
    public RenderedImage getImage() {
        return img;
    }

    
    private int white = 0xffff;
    
    private int black = 0;

    /**
     Get the current white point
     @return
     */
    public int getWhite() {
        return white;
    }

    /**
     Set white point
     @param white
     */
    public void setWhite( int white ) {
        this.white = white;
        calcLUT();
        scaledImage = null;
        repaint();
    }

    /**
     Get current point
     @return
     */
    public int getBlack() {
        return black;
    }

    /**
     Set black point
     @param black
     */
    public void setBlack( int black ) {
        this.black = black;
        calcLUT();
        scaledImage = null;
        repaint();
    }
    
    /**
     Lookup table for correcting exposure in linear color space
     */
    
    short[] lut = new short[0x10000];

    
    /**
     Calculate the LUT for exposure correction
     */
    private void calcLUT() {

        for ( int n = 0; n < lut.length; n++ ) {
            double src = ((double) n) / 65535.0;
            double dst = ((double) (n - black)) / ((double) (white - black));
            int dstInt = (int) (65535.0 * dst);
            dstInt = Math.max( 0, dstInt );
            dstInt = Math.min( 65535, dstInt );
            lut[n] = (short) dstInt;
        }
    }
    
    
    /**
     Paint the current frame
     @param g
     */
    @Override
    public void paint( Graphics g ) {
        super.paint(g);
        if ( img == null ) {
            return;
        }
        if ( scaledImage == null ) {
            float scaleH = (float)getWidth()/(float)img.getWidth();
            float scaleV = (float)getHeight()/(float)img.getHeight();
            float scale = Math.min( scaleV, scaleH );
            scaledImage = ScaleDescriptor.create( img, scale, scale, 0.0f, 0.0f, null, null );
            
            // Apply LUT to correct black and white poitns
            LookupTableJAI jailut = new LookupTableJAI( lut, true );
            scaledImage = LookupDescriptor.create( scaledImage, jailut, null );

            ColorSpace srgb = ColorSpace.getInstance( ColorSpace.CS_sRGB );
            ColorModel cm =
                    new ComponentColorModel( ColorSpace.getInstance( ColorSpace.CS_sRGB ),
                    false, false, ColorModel.OPAQUE, DataBuffer.TYPE_USHORT );

            scaledImage = ColorConvertDescriptor.create( scaledImage, cm, null );
        }
        ((Graphics2D)g).drawRenderedImage(scaledImage, AffineTransform.getScaleInstance(1.0, 1.0));
    }
    
    /** This method is called from within the constructor to
     initialize the form.
     WARNING: Do NOT modify this code. The content of this method is
     always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
