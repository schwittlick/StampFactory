package main;

import java.util.Arrays;
import java.util.List;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.video.Capture;

public class CameraInput {

  private Capture cam;
  private PApplet p;

  public CameraInput( PApplet p ) {
    this.p = p;
  }


  public void initialize(String input) {
    System.out.println(input);
    cam = new Capture( p, input );
    cam.start();
  }

  public PImage getImage() {
    PImage newImage;

    if ( cam.available() ) {
      cam.read();
      return cam.get();
    } else {
      newImage = new PImage( cam.width, cam.height, PConstants.RGB );
    }

    return newImage;
  }

  static List< String > getAvailableCameras() {
    return Arrays.asList( Capture.list() );
  }

  public void close() throws NullPointerException {
    try {
      cam.stop();
    } catch ( NullPointerException e ) {
      throw new NullPointerException( );
    }
  }
}
