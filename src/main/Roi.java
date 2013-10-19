package main;

import java.awt.Rectangle;

import processing.core.PApplet;

public class Roi {
  private Rectangle roiArea;
  boolean addWhatLOL;

  private PApplet p;

  public Roi( PApplet _p ) {
    this.p = _p;
    addWhatLOL = true;
    roiArea = new Rectangle( 0, 0, p.width, p.height );
  }

  public boolean isFinished() {
    if ( roiArea != null ) {
      return true;
    } else {
      return false;
    }
  }

  public void draw() {
    if ( isFinished() ) {
      p.noFill();
      p.stroke( 255, 0, 0 );
      p.rect( roiArea.x, roiArea.y, roiArea.width, roiArea.height );
    }
  }

  public void resetRoi() {
    roiArea = null;
  }


  public void add( int _x, int _y ) {
    if ( addWhatLOL ) {
      setStartX( _x );
      setStartY( _y );
    } else {
      setEndX( _x );
      setEndY( _y );
    }

    addWhatLOL = !addWhatLOL;
  }

  public void setStartX( int _x ) {
    this.roiArea.x = _x;
  }

  public void setStartY( int _y ) {
    this.roiArea.y = _y;
  }

  public void setEndX( int _x ) {
    this.roiArea.width = _x - this.roiArea.x;
  }

  public void setEndY( int _y ) {
    this.roiArea.height = _y - this.roiArea.y;
  }

  public int getStartX() {
    return roiArea.x;
  }

  public int getStartY() {
    return roiArea.y;
  }

  public int getEndX() {
    return ( roiArea.x + roiArea.width );
  }

  public int getEndY() {
    return ( roiArea.y + roiArea.height );
  }

}
