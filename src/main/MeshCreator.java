package main;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;
import toxi.geom.Vec3D;
import toxi.geom.mesh.WETriangleMesh;

public class MeshCreator {
  private PApplet p;

  private WETriangleMesh mesh;
  private PShape shape;
  private PImage baseImage;
  private int resolution, threshold;

  public MeshCreator( PApplet p ) {
    this.p = p;
    this.resolution = 1;
    this.threshold = 20;

    this.mesh = new WETriangleMesh( "stampMesh" );
  }

  public void addImage( PImage im ) {
    try {
      this.baseImage = ( PImage ) im.clone();
      this.baseImage.filter( PConstants.INVERT );
    } catch ( CloneNotSupportedException e ) {
      e.printStackTrace();
    }
  }

  public void createMesh() {
    shape = p.createShape();
    shape.beginShape( PConstants.TRIANGLES );
    shape.noStroke();
    shape.fill( 255 );
    baseImage.loadPixels();
    
    for ( int i = 0; i < baseImage.height - resolution; i += resolution ) {
      for ( int j = 0; j < baseImage.width - resolution; j += resolution ) {
        shape.vertex( j, i, p.red( baseImage.pixels[ i * baseImage.width + j ] ) / threshold );
        shape.vertex( j + resolution, i,
            p.red( baseImage.pixels[ i * baseImage.width + j + resolution ] ) / threshold );
        shape.vertex( j + resolution, i + resolution,
            p.red( baseImage.pixels[ ( i + resolution ) * baseImage.width + j + resolution ] )
                / threshold );

        shape.vertex( j, i, p.red( baseImage.pixels[ i * baseImage.width + j ] ) / threshold );
        shape.vertex( j, i + resolution,
            p.red( baseImage.pixels[ ( i + resolution ) * baseImage.width + j ] ) / threshold );
        shape.vertex( j + resolution, i + resolution,
            p.red( baseImage.pixels[ ( i + resolution ) * baseImage.width + j + resolution ] )
                / threshold );

      }
      System.out.println( i + "/" + ( baseImage.height - 2 ) + " resolution done" );
    }

    shape.vertex( 0, 0, 0 );
    shape.vertex( 0, baseImage.height - resolution, 0 );
    shape.vertex( 0, baseImage.height - resolution, -30 );

    shape.vertex( 0, 0, 0 );
    shape.vertex( 0, 0, -30 );
    shape.vertex( 0, baseImage.height - resolution, -30 );

    // second
    shape.vertex( baseImage.width - resolution, 0, 0 );
    shape.vertex( baseImage.width - resolution, baseImage.height - resolution, 0 );
    shape.vertex( baseImage.width - resolution, baseImage.height - resolution, -30 );

    shape.vertex( baseImage.width - resolution, 0, 0 );
    shape.vertex( baseImage.width - resolution, 0, -30 );
    shape.vertex( baseImage.width - resolution, baseImage.height - resolution, -30 );

    // third
    shape.vertex( baseImage.width - resolution, 0, 0 );
    shape.vertex( 0, 0, 0 );
    shape.vertex( 0, 0, -30 );

    shape.vertex( 0, 0, -30 );
    shape.vertex( baseImage.width - resolution, 0, 0 );
    shape.vertex( baseImage.width - resolution, 0, -30 );

    // fourth
    shape.vertex( baseImage.width - resolution, baseImage.height - resolution, 0 );
    shape.vertex( 0, baseImage.height - resolution, 0 );
    shape.vertex( 0, baseImage.height - resolution, -30 );

    shape.vertex( 0, baseImage.height - resolution, -30 );
    shape.vertex( baseImage.width - resolution, baseImage.height - resolution, 0 );
    shape.vertex( baseImage.width - resolution, baseImage.height - resolution, -30 );

    // closing
    shape.vertex( 0, 0, -30 );
    shape.vertex( baseImage.width - resolution, 0, -30 );
    shape.vertex( 0, baseImage.height - resolution, -30 );

    shape.vertex( baseImage.width - resolution, baseImage.height - resolution, -30 );
    shape.vertex( baseImage.width - resolution, 0, -30 );
    shape.vertex( 0, baseImage.height - resolution, -30 );

    shape.endShape();

    System.out.println( "VERTEX COUNT: " + shape.getVertexCount() );

    for ( int i = 0; i < shape.getVertexCount(); i += 3 ) {
      PVector v1 = shape.getVertex( i );
      PVector v2 = shape.getVertex( i + 1 );
      PVector v3 = shape.getVertex( i + 2 );
      mesh.addFace( new Vec3D( v1.x, v1.y, v1.z ), new Vec3D( v2.x, v2.y, v2.z ), new Vec3D( v3.x,
          v3.y, v3.z ) );
    }
    this.mesh.faceOutwards();
    this.mesh.computeFaceNormals();
    this.mesh.computeVertexNormals();

    System.out.println( "CREATED MESH" );
  }

  public PShape getShape() {
    return this.shape;
  }

  public WETriangleMesh getMesh() {
    return this.mesh;
  }

  public void save() {
    String fileName = "stamp_" + StampFactoryApplet.timestamp() + ".stl";
    this.mesh.saveAsSTL( fileName );
  }
}
