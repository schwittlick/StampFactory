package main;

import processing.core.PApplet;
import processing.video.Capture;

public class StampFactoryApplet extends PApplet {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @SuppressWarnings( "unused" )
  private ControlFrame controls;

  private ImageProcessor improc;
  Capture cap;

  public void init() {
    super.init();
  }

  public void setup() {
    size( 1200, 800, P2D );

    improc = new ImageProcessor( this );
    improc.setRoi( new Roi( this ) );
    controls = ControlFrame.createControlFrame( this, "CONTROLS", 600, 500 );
    
    cap = new Capture( this, 640, 480 );
    cap.start();
  }

  public void draw() {
    background( 0 );
    
    if( cap.available()){
      cap.read();
      image(cap, 0, 0 );
    }
    
    try {
      //image( improc.getCameraInput(), 0, 0 );
    } catch ( NullPointerException e ) {
      //System.out.println("NULLPOIUNTREW");
    }

   // improc.drawEdited();
   // improc.getRoi().draw();
  }

  public void mousePressed() {
    improc.getRoi().add( mouseX, mouseY );
  }

  public void applyBrightness( float value ) {
    this.improc.applyBrightnessChange( value );
  }

  public void applyContrast( float value ) {
    this.improc.applyContrastChange( value );
  }

  public void applyThreshold( float value ) {
    this.improc.applyThreshold( value );
  }

  public void keyPressed() {
    switch ( key ) {
      case 'r':
        improc.getRoi().resetRoi();
        break;
    }
  }

  protected void addImage( String path ) {
    improc.addOriginalImage( loadImage( path ) );
  }

  protected void setImageFromCamera() {
    improc.useCameraInput();
  }

  protected void setCameraInput( String input ) {
    this.improc.initCameraInput( input );
  }

  protected ImageProcessor getImageProcessor() {
    return improc;
  }

  public static void main( String[ ] args ) {
    PApplet.main( new String[ ] { "main.StampFactoryApplet" } );
  }
}
