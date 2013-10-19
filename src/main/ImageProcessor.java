package main;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

public class ImageProcessor {

  private PApplet p;
  private Roi roi;
  private CameraInput camerInput;
  private PImage originalImage;
  private PImage editedImage;

  private boolean doThreshold;
  private float contrast, brightness, threshold;

  public ImageProcessor( PApplet p ) {
    this.p = p;
    doThreshold = false;
    this.contrast = 1;

    camerInput = new CameraInput( p );
  }

  public static List< String > getImagesInFolder( PApplet p, String path ) {

    File folder = new File( p.sketchPath( path ) );
    FilenameFilter imageFileFormatFilter = new java.io.FilenameFilter() {
      public boolean accept( File dir, String name ) {
        return name.toLowerCase().endsWith( ".jpg" ) || name.toLowerCase().endsWith( ".gif" )
            || name.toLowerCase().endsWith( ".png" );
      }
    };

    String[ ] files = folder.list( imageFileFormatFilter );
    try {
      return Arrays.asList( files );
    } catch ( NullPointerException e ) {
      System.err.println( "No Image File found in " + p.sketchPath( path ) );
      return new ArrayList< String >();
    }
  }

  public void brightness( PImage input, PImage output, float bright ) {
    int w = input.width;
    int h = input.height;

    // our assumption is the image sizes are the same
    // so test this here and if it's not true just return with a warning
    if ( w != output.width || h != output.height ) {
      System.out.println( "error: image dimensions must agree" );
      return;
    }

    // this is required before manipulating the image pixels directly
    input.loadPixels();
    output.loadPixels();

    for ( int y = getRoi().getStartY(); y < getRoi().getEndY(); y++ ) {
      for ( int x = getRoi().getStartX(); x < getRoi().getEndX(); x++ ) {
        int i = x + y * w;
        int inColor = input.pixels[ i ];

        int r = ( inColor >> 16 ) & 0xFF;
        int g = ( inColor >> 8 ) & 0xFF;
        int b = inColor & 0xFF;

        r = ( int ) ( r + bright );
        g = ( int ) ( g + bright );
        b = ( int ) ( b + bright );

        r = r < 0 ? 0 : r > 255 ? 255 : r;
        g = g < 0 ? 0 : g > 255 ? 255 : g;
        b = b < 0 ? 0 : b > 255 ? 255 : b;

        output.pixels[ i ] = 0xff000000 | ( r << 16 ) | ( g << 8 ) | b; // this does the same but

      }
    }


    // so that we can display the new image we must call this for each image
    input.updatePixels();
    output.updatePixels();
  }

  public void contrast( PImage input, PImage output, float cont ) {
    int w = input.width;
    int h = input.height;

    // our assumption is the image sizes are the same
    // so test this here and if it's not true just return with a warning
    if ( w != output.width || h != output.height ) {
      System.out.println( "error: image dimensions must agree" );
      return;
    }

    input.loadPixels();
    output.loadPixels();
    for ( int y = getRoi().getStartY(); y < getRoi().getEndY(); y++ ) {
      for ( int x = getRoi().getStartX(); x < getRoi().getEndX(); x++ ) {
        int i = x + y * w;

        int inColor = input.pixels[ i ];

        int r = ( inColor >> 16 ) & 0xFF;
        int g = ( inColor >> 8 ) & 0xFF;
        int b = inColor & 0xFF;

        r = ( int ) ( r * cont );
        g = ( int ) ( g * cont );
        b = ( int ) ( b * cont );

        r = r < 0 ? 0 : r > 255 ? 255 : r;
        g = g < 0 ? 0 : g > 255 ? 255 : g;
        b = b < 0 ? 0 : b > 255 ? 255 : b;

        output.pixels[ i ] = 0xff000000 | ( r << 16 ) | ( g << 8 ) | b;


      }
    }
    /*
     * // loop through all pixels in the image for ( int i = 0; i < w * h; i++ ) { // get color
     * values from the current pixel (which are stored as a list of type 'color') int inColor =
     * input.pixels[ i ];
     * 
     * // slow version for illustration purposes - calling a function inside this loop // is a big
     * no no, it will be very slow, plust we need an extra cast // as this loop is being called w *
     * h times, that can be a million times or more! // so comment this version and use the one
     * below // int r = (int) red(input.pixels[i]); // int g = (int) green(input.pixels[i]); // int
     * b = (int) blue(input.pixels[i]);
     * 
     * // here the much faster version (uses bit-shifting) - uncomment to try int r = ( inColor >>
     * 16 ) & 0xFF; // like calling the function red(), but faster int g = ( inColor >> 8 ) & 0xFF;
     * int b = inColor & 0xFF;
     * 
     * // apply contrast (multiplcation) and brightness (addition)
     * 
     * r = ( int ) ( r * cont ); // floating point aritmetic so convert back to int with a //
     * cast(i.e. '(int)'); g = ( int ) ( g * cont ); b = ( int ) ( b * cont );
     * 
     * // slow but absolutely essential - check that we don't overflow (i.e. r,g and b must be in
     * the // range of 0 to 255) // to explain: this nest two statements, sperately it would be r =
     * r < 0 ? 0 : r; and r = r > // 255 ? 255 : 0; // you can also do this with if statements and
     * it would do the same just take up more space r = r < 0 ? 0 : r > 255 ? 255 : r; g = g < 0 ? 0
     * : g > 255 ? 255 : g; b = b < 0 ? 0 : b > 255 ? 255 : b;
     * 
     * // and again in reverse for illustration - calling the color function is slow so use the //
     * bit-shifting version below // output.pixels[i] = color(r ,g,b); output.pixels[ i ] =
     * 0xff000000 | ( r << 16 ) | ( g << 8 ) | b; // this does the same but // faster
     * 
     * }
     */
    // so that we can display the new image we must call this for each image
    input.updatePixels();
    output.updatePixels();
  }

  public void addOriginalImage( PImage image ) {
    // this.originalImage = new PImage(image.width, image.height, image.format);
    try {
      this.originalImage = ( PImage ) image.clone();
    } catch ( CloneNotSupportedException e ) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // this.originalImage = image;
    this.originalImage.resize( 0, p.height );
    // his.editedImage =
    // new PImage( this.originalImage.width, this.originalImage.height, PConstants.RGB );
    // this.editedImage = image;
    try {
      this.editedImage = ( PImage ) this.originalImage.clone();
    } catch ( CloneNotSupportedException e ) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void initCameraInput( String input ) {
    try {
      camerInput.close();
    } catch ( NullPointerException e ) {
      System.out.println( "CLOSING CAMERA INPUT" );
    }

    camerInput.initialize( input );
  }

  public void useCameraInput() {
    System.out.println( "TAKING PHOTO!" );
    addOriginalImage( camerInput.getImage() );
  }

  public PImage getCameraInput() {
    return camerInput.getImage();
  }

  public void applyBrightnessChange( float value ) {
    try {
      this.editedImage = ( PImage ) this.originalImage.clone();
    } catch ( CloneNotSupportedException e ) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    this.brightness = value;

    brightness( editedImage, editedImage, this.brightness );
    contrast( editedImage, editedImage, this.contrast );

    if ( doThreshold ) {
      editedImage.filter( PConstants.THRESHOLD, this.threshold );
    }
  }

  public void applyContrastChange( float value ) {
    try {
      this.editedImage = ( PImage ) this.originalImage.clone();
    } catch ( CloneNotSupportedException e ) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    this.contrast = value;

    brightness( editedImage, editedImage, this.brightness );
    contrast( editedImage, editedImage, this.contrast );

    if ( doThreshold ) {
      editedImage.filter( PConstants.THRESHOLD, this.threshold );
    }
  }

  public void applyThreshold( float value ) {
    try {
      this.editedImage = ( PImage ) this.originalImage.clone();
    } catch ( CloneNotSupportedException e ) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    this.doThreshold = true;
    this.threshold = value;

    brightness( editedImage, editedImage, this.brightness );
    contrast( editedImage, editedImage, this.contrast );
    editedImage.filter( PConstants.THRESHOLD, this.threshold );
  }

  public void dilate() {
    editedImage.filter( PConstants.DILATE );
  }

  public void erode() {
    editedImage.filter( PConstants.ERODE );
  }

  protected PImage getSelectedImageRoi() {
    PImage returnImage = new PImage( editedImage.width, editedImage.height, editedImage.format );

    returnImage.copy( editedImage, roi.getStartX(), roi.getStartY(), roi.getWidth(),
        roi.getHeight(), 0, 0, roi.getWidth(), roi.getHeight() );

    return returnImage;
  }

  public void drawEdited() {
    try {
      p.image( editedImage, 0, 0 );
    } catch ( NullPointerException e ) {}
  }

  public Roi getRoi() {
    return roi;
  }

  public void setRoi( Roi roi ) {
    this.roi = roi;
  }
}
