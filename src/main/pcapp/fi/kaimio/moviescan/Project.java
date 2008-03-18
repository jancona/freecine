/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fi.kaimio.moviescan;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 Project object keeps strck of all inforation related the project (i.e. 
 information about scan strips and scenes
 */
public class Project {

    private static Log log = LogFactory.getLog( Project.class.getName() );
    /**
     Home directory for the project
     */
    private File dir;
    
    private Map<String, ScanStrip> loadedStrips = new HashMap<String, ScanStrip>();
    
    /**
     The scene into which new scans are added
     */
    private Scene scene;
    
    /**
     Creates a new project
     @param dir The directory of the project
     */
    public Project( File dir ) {
        this.dir = dir;
        scene = new Scene();
    }
    
    /**
    Add a scan strip to the project, saving it to image file if it is not yet 
    saved.
    @param strip
     */
    public void addScanStrip( ScanStrip strip ) {
        if ( strip.getName() == null ) {
            File scanDir = new File( dir, "scan" );
            if ( !scanDir.exists() ) {
                scanDir.mkdir();
            }
            int num = getLastScanFile() + 1;
            String name = String.format( "scan_%04d", num );
            // Save the image
            RenderedImage img = strip.stripImage;
            saveImage( img, new File( scanDir, name + ".tif" ) );
            saveStripInfo( strip, new File( scanDir, name + ".xml" ) );
            strip.setName( name );
        }
        loadedStrips.put( strip.getName(), strip );
        scene.addFrames( strip, 0, strip.getFrameCount() );
    }

    /**
     Get the scan strip with given name
     @param name Name of the strip
     @return The strip with given name or <code>null</code> if no such strip 
     exists
     */
    public ScanStrip getScanStrip( String name ) {
        ScanStrip ret = null;
        if ( loadedStrips.containsKey( name ) ) {
            ret = loadedStrips.get( name );
        } else {
            ret = new ScanStrip( );
            loadedStrips.put(name, ret);
            ret.setName(name);
        }
        return ret;
    }

    public void addScene( Scene s ) {
        scene = s;
    }
    
    /**
     Save the project info in project directory (in file project.xml)
     @throws IOException if the project cannot be saved
     */
    public void save() throws IOException {
        File f = File.createTempFile( "tmproject", ".xml", dir );
        try {
            // Write the strip info to a file
            StreamResult streamResult = new StreamResult( f );
            SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
            TransformerHandler hd = tf.newTransformerHandler();
            Transformer serializer = hd.getTransformer();
            serializer.setOutputProperty( OutputKeys.ENCODING, "ISO-8859-1" );
            serializer.setOutputProperty( OutputKeys.INDENT, "yes" );
            hd.setResult( streamResult );
            hd.startDocument();
            writeXml( hd );
            hd.endDocument();
        } catch ( Exception e ) {
            throw new IOException( "Error saving project: " + e.getMessage(), e );
        }
        f.renameTo( new File( dir, "project.xml" ) );
    }
    
    public void writeXml( TransformerHandler hd ) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        hd.startElement( "", "", "project", atts );
        hd.startElement( "", "", "scenes", atts );
        scene.writeXml( hd );
        hd.endElement( "", "", "scenes" );
        hd.endElement( "", "", "project" );
    }
    
    private static class ScanFileFilter implements FilenameFilter {

        public boolean accept( File dir, String fname ) {
            return fname.matches("^scan_[0-9][0-9][0-9][0-9].tif$");
        }
        
    }

    
    
    private int getLastScanFile() {
        int max = 0;
        File scanDir = new File( dir, "scan" );
        for ( File f : scanDir.listFiles( new ScanFileFilter() ) ) {
            String num = f.getName().substring(5, 9);
            int n = Integer.parseInt( num );
            max = Math.max( max, n );
        }
        return max;
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
                log.error( "Error saving image " + file.getAbsolutePath() + ": " 
                        + ex.getMessage() );
            } finally {
                if ( ios != null ) {
                    try {
                        ios.close();
                    } catch ( IOException e ) {
                        log.error( "Error closing output stream: " + e.getMessage() );
                    }
                }
                writer.dispose();
            }
        }
    }

    private void saveStripInfo( ScanStrip strip, File file ) {
        try {
            StreamResult streamResult = new StreamResult( file );
            SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
            TransformerHandler hd = tf.newTransformerHandler();
            Transformer serializer = hd.getTransformer();
            serializer.setOutputProperty( OutputKeys.ENCODING, "ISO-8859-1" );
            serializer.setOutputProperty( OutputKeys.INDENT, "yes" );
            hd.setResult( streamResult );
            hd.startDocument();
            strip.writeXml( hd );
            hd.endDocument();
        } catch ( SAXException ex ) {
            log.error( "Error saving " + file.getAbsolutePath() + ": " + ex.getMessage() );
        } catch ( TransformerConfigurationException ex ) {
            log.error( "Error saving " + file.getAbsolutePath() + ": " + ex.getMessage() );
        }
    }

}
