package main;

import java.util.Calendar;

import peasy.PeasyCam;
import processing.core.PApplet;
import processing.video.Capture;
import toxi.geom.mesh.LaplacianSmooth;

public class StampFactoryApplet extends PApplet {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @SuppressWarnings( "unused" )
  private ControlFrame controls;

  private ImageProcessor improc;
  private MeshCreator meshCreator;
  private PeasyCam cam;
  Capture cap;

  public void init() {
    super.init();
  }

  public void setup() {
    size( 1200, 800, P3D );

    improc = new ImageProcessor( this );
    improc.setRoi( new Roi( this ) );

    controls = ControlFrame.createControlFrame( this, "CONTROLS", 600, 500 );

    meshCreator = new MeshCreator( this );

    cam = new PeasyCam( this, 500 );
  }

  public void draw() {
    background( 0 );
    lights();

    try {
      shape( meshCreator.getShape() );
    } catch ( NullPointerException e ) {
      cam.beginHUD();
      improc.drawEdited();
      improc.getRoi().draw();
      cam.endHUD();
    }
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
      case 'f':
        new LaplacianSmooth().filter( meshCreator.getMesh(), 1 );
        break;
    }
  }

  protected void addImage( String path ) {
    improc.addOriginalImage( loadImage( path ) );
  }

  protected void setImageFromCamera() {
    improc.useCameraInput();
  }

  protected void createMesh() {
    meshCreator.addImage( improc.getSelectedImageRoi() );
    meshCreator.createMesh();
  }

  protected void saveMesh() {
    meshCreator.save();
  }

  protected void setCameraInput( String input ) {
    this.improc.initCameraInput( input );
  }

  protected ImageProcessor getImageProcessor() {
    return improc;
  }

  public static String timestamp() {
    Calendar now = Calendar.getInstance();
    return String.format( "%1$ty%1$tm%1$td_%1$tH%1$tM%1$tS", now );
  }

  public static void main( String[ ] args ) {
    PApplet.main( new String[ ] { "main.StampFactoryApplet" } );
  }
}
