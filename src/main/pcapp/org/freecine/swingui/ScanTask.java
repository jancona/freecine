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

import org.freecine.filmscan.FilmMover;
import org.freecine.filmscan.FilmMoverException;
import org.freecine.filmscan.Project;
import org.freecine.filmscan.ScanAnalysisListener;
import org.freecine.filmscan.ScanStrip;
import org.freecine.sane.FixedPointNumber;
import org.freecine.sane.SaneDevice;
import org.freecine.sane.SaneException;
import org.freecine.sane.ScanParameter;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferUShort;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RasterFactory;
import javax.media.jai.TiledImage;
import javax.media.jai.operator.ConstantDescriptor;
import javax.media.jai.operator.TransposeDescriptor;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;

/**
 SwingWorker background task for scanning film. 
 */
public class ScanTask extends Task<Void, Void> implements ScanAnalysisListener {

    private static Logger log = Logger.getLogger(ScanTask.class.getName() );
    private SaneDevice dev;
    private Rectangle2D scanArea;
    private Project prj;
    private FilmMover filmMover;

    /**
     Creates a new ScanTask object
     @param dev The scanner that will be used
     @param scanArea Scan area 
     @param prj the project in which scanned frames are added
     */
    public ScanTask( SaneDevice dev, FilmMover mover, Rectangle2D scanArea, Project prj ) {
        super( Application.getInstance() );
        this.dev = dev;
        this.scanArea = scanArea;
        this.prj = prj;
        this.filmMover = mover;
    }
    /**
    Width of the tiles used for storing the scanned iamge
     */
    static final int TILE_WIDTH = 256;
    /**
    Height of the tiles used for storing the scanned image
     */
    static final int TILE_HEIGHT = 256;
    TiledImage image;

    public RenderedImage getImage() {
        return image;
    }
    
    int stripCount = 0;

    /**
     Main loop for this background task. Loop until error happens or the task 
     is cancelled
     @return
     @throws fi.kaimio.sane.SaneException If scanning fails
     @throws java.io.IOException If saving project changes fails
     */
    protected Void doInBackground() throws SaneException, IOException {

        initScanner();
        while ( true ) {
            PlanarImage img = scan();
            saveImage(img, new File( "/tmp/testimg.tif" ) );
            img = TransposeDescriptor.create( img, TransposeDescriptor.ROTATE_90, null );
            if ( isCancelled() ) {
                img.dispose();
                break;
            }
            ScanStrip strip = ScanStrip.create( img, this );
            if ( isCancelled() ) {
                strip.dispose();
                img.dispose();
                break;
            }
            prj.addScanStrip( strip );
            prj.save();
            strip.dispose();
            img.dispose();
            System.gc();
            System.gc();
            System.gc();
            message( "movingFilmMsg" );
            try {
                filmMover.moveFilm();
            } catch (FilmMoverException e ) {
                message( "filmMoveFailedMsg", e.getMessage() );
                break;
            }
            
            stripCount++;   
        }
        return null;
    }
    
    /**
     Initializes the scanner before scanning the first strip
     @throws fi.kaimio.sane.SaneException If initialization fails
     */
    private void initScanner() throws SaneException {
        // Setup the scanner
        message( "initMessage" );
        dev.setOption( "mode", "Color" );
        dev.setOption( "depth", 16 );
        FixedPointNumber top = FixedPointNumber.valueOf( scanArea.getMinY() );
        FixedPointNumber left = FixedPointNumber.valueOf( scanArea.getMinX() );
        FixedPointNumber bot = FixedPointNumber.valueOf( scanArea.getMaxY() );
        FixedPointNumber right = FixedPointNumber.valueOf( scanArea.getMaxX() );
        
//        top = new FixedPointNumber( 48 << 16 );
//        left = new FixedPointNumber( 30 << 16 );
//        bot = new FixedPointNumber( 55 << 16 );
//        right = new FixedPointNumber( 190 << 16 );

        dev.setOption( "tl-x", left );
        dev.setOption( "tl-y", top );
        dev.setOption( "br-x", right );
        dev.setOption( "br-y", bot );
        dev.setOption( "resolution", 4800 );
        dev.setOption( "source", "Transparency Unit" );

        // Set gamma correction
        dev.setOption( "gamma-correction", "User defined" );
        int[] gammaTable = new int[256];
        for ( int n = 0; n < gammaTable.length; n++ ) {
            gammaTable[n] = n;
        }
        dev.setOption( "red-gamma-table", gammaTable );
        dev.setOption( "blue-gamma-table", gammaTable );
        dev.setOption( "green-gamma-table", gammaTable );
        
    }
    
    /**
     Scan a single strip with settings set in initScanner()
     @return The scanned image
     @throws fi.kaimio.sane.SaneException If an error occurred during scanning.
     */
    private PlanarImage scan() throws SaneException {
        // Scan image
        ScanParameter params = dev.getScanParameter();
        dev.startScan();
        params = dev.getScanParameter();

        // Read 1 strip of tiles at time
        int readSize =
                params.getBytesPerLine() * TILE_HEIGHT * 8 / params.getDepth();
        short[] data = new short[readSize];
        DataBufferUShort db = new DataBufferUShort( data, readSize );
        SampleModel tileSampleModel =
                RasterFactory.createPixelInterleavedSampleModel(
                DataBuffer.TYPE_USHORT,
                TILE_WIDTH, TILE_HEIGHT,
                3, 3 * TILE_WIDTH, new int[]{0, 1, 2} );
        ColorModel tileColorModel = PlanarImage.createColorModel( tileSampleModel );
        
        
                
        image =
                new TiledImage( 0, 0,
                params.getPixelsPerLine(), params.getLines(),
                0, 0, tileSampleModel, tileColorModel );

        ImageLayout layout = new ImageLayout( image );
        RenderingHints hints = new RenderingHints( JAI.KEY_IMAGE_LAYOUT, layout );
        RenderedImage constImg = ConstantDescriptor.create((float)params.getPixelsPerLine(), (float)params.getLines(), new Short[]{0,0,0}, hints );
        image.set( constImg );
        
        SampleModel scanSampleModel =
                RasterFactory.createPixelInterleavedSampleModel(
                DataBuffer.TYPE_USHORT,
                params.getPixelsPerLine(), TILE_HEIGHT,
                3, 3 * params.getPixelsPerLine(), new int[]{0, 1, 2} );

        int line = 0;
        int totalLines = params.getLines();
        setProgress( line, 0, totalLines );
        message( "scanProgressMessage", stripCount, line, totalLines );
        while ( line < totalLines ) {
            int linesLeft = params.getLines() - line;
            int samplesLeft = linesLeft * params.getBytesPerLine() * 8 / params.getDepth();
            dev.read( data, Math.min( data.length, samplesLeft ) );
            Raster raster =
                    RasterFactory.createWritableRaster( scanSampleModel, db,
                    new Point( 0, line ) ); 
            image.setData( raster );
            line += TILE_HEIGHT;
            setProgress(line < totalLines ? line : totalLines, 0, totalLines);
            message( "scanProgressMessage", stripCount, 
                    line < totalLines ? line : totalLines, totalLines );
        }

        return image;
        
    }
    private void saveImage( RenderedImage img, File file ) {
        // Find a writer for that file extensions
        // Try to determine the file type based on extension
        String ftype = "jpg";
        String imageFname = file.getName();
        int extIndex = imageFname.lastIndexOf( "." ) + 1;
        if ( extIndex > 0 ) {
            ftype = imageFname.substring( extIndex );
        }

        ImageWriter writer = null;
        Iterator iter = ImageIO.getImageWritersBySuffix( ftype );
        writer = (ImageWriter) iter.next();

        if ( writer != null ) {
            ImageOutputStream ios = null;
            try {
                // Prepare output file
                ios = ImageIO.createImageOutputStream( file );
                writer.setOutput( ios );
                // Set some parameters
                ImageWriteParam param = writer.getDefaultWriteParam();
                writer.write( null, new IIOImage( img, null, null ), param );

                // Cleanup
                ios.flush();

            } catch ( IOException ex ) {
                log.severe( "Error saving image " + file.getAbsolutePath() + ": " 
                        + ex.getMessage() );
            } finally {
                if ( ios != null ) {
                    try {
                        ios.close();
                    } catch ( IOException e ) {
                        log.severe( "Error closing output stream: " + e.getMessage() );
                    }
                }
                writer.dispose();
            }
        }
    }
    /**
     Callback that is called by {@link ScanStrip} to inforrm about progress
     @param currentLine The last pixel line analyzed
     @param totalLines Total nubmer of lines
     */
    public void scanAnalysisProgress( int currentLine, int totalLines ) {
        message( "analysisProgressMessage", stripCount, currentLine, totalLines );
        setProgress( currentLine, 0, totalLines);
    }

    /**
     Callback that is called by {@link ScanStrip} After the analysis is ready
     
     @param perfCount Number of perforations found
     */
    public void scanAnalysisComplete( int perfCount ) {
        message( "analysisCompleteMessage", stripCount, perfCount );
    }
    
}
