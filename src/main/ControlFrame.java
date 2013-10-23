package main;

import java.awt.Frame;
import java.util.List;

import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.DropdownList;
import processing.core.PApplet;

public class ControlFrame extends PApplet {

  private static final long serialVersionUID = 1L;
  private ControlP5 cp;
  DropdownList imageDDList, availableCameraDDList;
  List< String > availableImages;

  private StampFactoryApplet parent;
  private int w, h;

  public ControlP5 control() {
    return cp;
  }

  public ControlFrame( PApplet theParent, int theWidth, int theHeight ) {
    parent = ( StampFactoryApplet ) theParent;
    setW( theWidth );
    setH( theHeight );


  }

  public void setup() {
    size( 600, 500 );

    cp = new ControlP5( this );
    cp.setBroadcast( false );

    imageDDList = cp.addDropdownList( "IMAGES" ).setPosition( 10, 30 ).setSize( 300, 300 );
    availableImages = ImageProcessor.getImagesInFolder( this, "data" );

    cp.addSlider( "THRESHOLD" ).setPosition( 10, 180 ).setSize( 300, 20 ).setValue( 0 )
        .setRange( 0, 1 );
    cp.addSlider( "BRIGHTNESS" ).setPosition( 10, 220 ).setSize( 300, 20 ).setValue( 0 )
        .setRange( -500, 500 );
    cp.addSlider( "CONTRAST" ).setPosition( 10, 260 ).setSize( 300, 20 ).setValue( 0 )
        .setRange( -5, 5 );

    availableCameraDDList = cp.addDropdownList( "CAMS" ).setPosition( 340, 300 ).setSize( 300, 300 );
    availableCameraDDList.addItems( CameraInput.getAvailableCameras() );

    cp.addButton( "DILATE" ).setPosition( 10, 350 ).setSize( 50, 25 );
    cp.addButton( "ERODE" ).setPosition( 80, 350 ).setSize( 50, 25 );
    cp.addButton(  "PHOTO" ).setPosition( 150, 350).setSize( 50, 25 );
    cp.addButton( "MESH" ).setPosition( 220, 350 ).setSize( 50, 25 );
    cp.addButton( "SAVE").setPosition( 290, 350 ).setSize( 50, 25 );

    imageDDList.addItems( availableImages );

    cp.setBroadcast( true );
  }

  public void controlEvent( ControlEvent e ) {
    switch ( e.getName() ) {
      case "IMAGES":
        System.out.println( e );
        int index = ( int ) e.getValue();
        parent.addImage( sketchPath( "data/" + availableImages.get( index ) ) );
        break;
      case "THRESHOLD":
        parent.applyThreshold( e.getValue() );
        break;
      case "BRIGHTNESS":
        parent.applyBrightness( e.getValue() );
        break;
      case "CONTRAST":
        parent.applyContrast( e.getValue() );
        break;
      case "DILATE":
        parent.getImageProcessor().dilate();
        break;
      case "ERODE":
        parent.getImageProcessor().erode();
        break;
      case "PHOTO":
        parent.setImageFromCamera();
        break;
      case "CAMS":
        int selectedCameraInputIndex = ( int ) e.getValue();
        parent.setCameraInput( CameraInput.getAvailableCameras().get( selectedCameraInputIndex ) );
        break;
      case "MESH":
        parent.createMesh();
        break;
      case "SAVE":
        parent.saveMesh();
        break;
    }
  }

  public void draw() {
    background( 0 );
  }

  public int getW() {
    return w;
  }

  public void setW( int w ) {
    this.w = w;
  }

  public int getH() {
    return h;
  }

  public void setH( int h ) {
    this.h = h;
  }

  static ControlFrame createControlFrame( PApplet _p, String theName, int theWidth, int theHeight ) {
    Frame f = new Frame( theName );
    ControlFrame p = new ControlFrame( _p, theWidth, theHeight );
    f.add( p );
    p.init();
    f.setTitle( theName );
    f.setSize( p.getW(), p.getH() );
    f.setResizable( false );
    f.setVisible( true );
    return p;
  }

}
