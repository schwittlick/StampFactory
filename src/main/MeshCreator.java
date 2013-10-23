package main;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;
import toxi.geom.Vec3D;
import toxi.geom.mesh.WETriangleMesh;
import toxi.processing.ToxiclibsSupport;

public class MeshCreator {
  private PApplet p;
  private ToxiclibsSupport gfx;

  private WETriangleMesh mesh;
  private PShape shape;
  private PImage baseImage;
  private int resolution, threshold;
  private boolean finishedCreatingMesh;

  public MeshCreator( PApplet p ) {
    this.p = p;
    this.gfx = new ToxiclibsSupport( p );

    this.resolution = 1;
    this.threshold = 20;
    this.finishedCreatingMesh = false;
  }

  public void addImage( PImage im ) {
    try {
      this.baseImage = ( PImage ) im.clone();
      this.baseImage.filter( PConstants.INVERT );
    } catch ( CloneNotSupportedException e ) {
      e.printStackTrace();
    }
  }

  private void adjustResolution() {
    int count = 0;
    for ( int i = 0; i < baseImage.height - resolution; i += resolution ) {
      for ( int j = 0; j < baseImage.width - resolution; j += resolution ) {
        count+=6;
      }
    }

    if ( count > 500000 ) {
      this.resolution = 3;
      System.out.println("Adjusted resolution to " + resolution);
    }
  }

  public void createMesh() {
    shape = p.createShape();
    shape.beginShape( PConstants.TRIANGLES );
    shape.noStroke();
    shape.fill( 255 );
    baseImage.loadPixels();
    this.mesh = new WETriangleMesh( "stampMesh" );

    adjustResolution();

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

        // Vec3D p1 = new Vec3D(j, i, p.red(baseImage.pixels[i*baseImage.width+j])/threshold);
        // Vec3D p2 = new Vec3D(j+resolution, i,
        // p.red(baseImage.pixels[i*baseImage.width+j+resolution])/threshold);
        // Vec3D p3 = new Vec3D(j+resolution, i+resolution,
        // p.red(baseImage.pixels[(i+resolution)*baseImage.width+j+resolution])/threshold);
        // Vec3D p4 = new Vec3D(j, i, p.red(baseImage.pixels[i*baseImage.width+j])/threshold);
        // Vec3D p5 = new Vec3D(j, i+resolution,
        // p.red(baseImage.pixels[(i+resolution)*baseImage.width+j])/threshold);
        // Vec3D p6 = new Vec3D(j+resolution, i+resolution,
        // p.red(baseImage.pixels[(i+resolution)*baseImage.width+j+resolution])/threshold);
        // mesh.addFace(p1, p2, p3);
        // mesh.addFace(p4, p5, p6);

      }
      System.out.println( i + "/" + ( baseImage.height - 2 ) + " resolution done" );
    }

    shape.endShape();

    PShape boxShape = p.createShape();
    boxShape.beginShape( PConstants.TRIANGLES );
    boxShape.noStroke();
    boxShape.fill( 255 );

    boxShape.vertex( 0, 0, 0 );
    boxShape.vertex( 0, baseImage.height - resolution, 0.05f );
    boxShape.vertex( 0, baseImage.height - resolution, -30 );

    boxShape.vertex( 0, 0, 0 );
    boxShape.vertex( 0, 0, -30 );
    boxShape.vertex( 0, baseImage.height - resolution, -30 );

    // second
    boxShape.vertex( baseImage.width - resolution, 0, 0.05f );
    boxShape.vertex( baseImage.width - resolution, baseImage.height - resolution, 0.05f );
    boxShape.vertex( baseImage.width - resolution, baseImage.height - resolution, -30 );

    boxShape.vertex( baseImage.width - resolution, 0, 0.05f );
    boxShape.vertex( baseImage.width - resolution, 0, -30 );
    boxShape.vertex( baseImage.width - resolution, baseImage.height - resolution, -30 );

    // third
    boxShape.vertex( baseImage.width - resolution, 0, 0.05f );
    boxShape.vertex( 0, 0, 0.05f );
    boxShape.vertex( 0, 0, -30 );

    boxShape.vertex( 0, 0, -30 );
    boxShape.vertex( baseImage.width - resolution, 0, 0.05f );
    boxShape.vertex( baseImage.width - resolution, 0, -30 );

    // fourth
    boxShape.vertex( baseImage.width - resolution, baseImage.height - resolution, 0.05f );
    boxShape.vertex( 0, baseImage.height - resolution, 0.05f );
    boxShape.vertex( 0, baseImage.height - resolution, -30 );

    boxShape.vertex( 0, baseImage.height - resolution, -30 );
    boxShape.vertex( baseImage.width - resolution, baseImage.height - resolution, 0.05f );
    boxShape.vertex( baseImage.width - resolution, baseImage.height - resolution, -30 );

    // closing
    boxShape.vertex( 0, 0, -30 );
    boxShape.vertex( baseImage.width - resolution, 0, -30 );
    boxShape.vertex( 0, baseImage.height - resolution, -30 );

    boxShape.vertex( baseImage.width - resolution, baseImage.height - resolution, -30 );
    boxShape.vertex( baseImage.width - resolution, 0, -30 );
    boxShape.vertex( 0, baseImage.height - resolution, -30 );

    boxShape.endShape();

    System.out.println( "VERTEX COUNT: " + ( shape.getVertexCount() + boxShape.getVertexCount() ) );

    WETriangleMesh toFaceOutwards = new WETriangleMesh();
    WETriangleMesh dontFaceOutwards = new WETriangleMesh();

    for ( int i = 0; i < shape.getVertexCount(); i += 3 ) {
      PVector v1 = shape.getVertex( i );
      PVector v2 = shape.getVertex( i + 1 );
      PVector v3 = shape.getVertex( i + 2 );
      // the main plane, where the normals go wrong
      if ( ( v1.z == 12.75f && v2.z == 12.75f && v3.z == 12.75f ) ) {
        toFaceOutwards.addFace( new Vec3D( v1.x, v1.y, v1.z ), new Vec3D( v2.x, v2.y, v2.z ),
            new Vec3D( v3.x, v3.y, v3.z ), new Vec3D( 0, 0, 1 ) );
      } else {
        dontFaceOutwards.addFace( new Vec3D( v1.x, v1.y, v1.z ), new Vec3D( v2.x, v2.y, v2.z ),
            new Vec3D( v3.x, v3.y, v3.z ), new Vec3D( 0, 0, 1 ) );
      }
     // System.out.println( i + " / " + shape.getVertexCount() + " done." );
    }

    for ( int i = 0; i < boxShape.getVertexCount(); i += 3 ) {
      PVector v1 = boxShape.getVertex( i );
      PVector v2 = boxShape.getVertex( i + 1 );
      PVector v3 = boxShape.getVertex( i + 2 );
      toFaceOutwards.addFace( new Vec3D( v1.x, v1.y, v1.z ), new Vec3D( v2.x, v2.y, v2.z ),
          new Vec3D( v3.x, v3.y, v3.z ) );
    }

    dontFaceOutwards.computeFaceNormals();
    dontFaceOutwards.computeVertexNormals();
    // boxMesh.faceOutwards();

    toFaceOutwards.computeFaceNormals();
    toFaceOutwards.computeVertexNormals();
    toFaceOutwards.faceOutwards();

    this.mesh.addMesh( toFaceOutwards );
    this.mesh.addMesh( dontFaceOutwards );

    System.out.println( "CREATED MESH" );
    finishedCreatingMesh = true;
  }

  public PShape getShape() {
    return this.shape;
  }

  public WETriangleMesh getMesh() {
    return this.mesh;
  }

  public void drawMesh() {
    if ( finishedCreatingMesh ) {
      gfx.mesh( getMesh() );
    }
  }

  public void reset() {
    this.shape = null;
    this.mesh = null;
  }

  public void save() {
    String directory = "stamps/";
    String fileName = "stamp_" + StampFactoryApplet.timestamp() + ".stl";
    this.mesh.saveAsSTL( directory + fileName );
  }
}
