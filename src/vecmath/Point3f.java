/*
   Copyright (C) 1997,1998,1999
   Kenji Hiranabe, Eiwa System Management, Inc.

   This program is free software.
   Implemented by Kenji Hiranabe(hiranabe@esm.co.jp),
   conforming to the Java(TM) 3D API specification by Sun Microsystems.

   Permission to use, copy, modify, distribute and sell this software
   and its documentation for any purpose is hereby granted without fee,
   provided that the above copyright notice appear in all copies and
   that both that copyright notice and this permission notice appear
   in supporting documentation. Kenji Hiranabe and Eiwa System Management,Inc.
   makes no representations about the suitability of this software for any
   purpose.  It is provided "AS IS" with NO WARRANTY.
*/
package vecmath;

import java.io.Serializable;

/**
  * A 3 element point that is represented by single precision
  * floating point x,y,z coordinates.
  * @version specification 1.1, implementation $Revision: 1.8 $, $Date: 1999/10/05 07:03:50 $
  * @author Kenji hiranabe
  */
public class Point3f extends Tuple3f implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = -844414292024934868L;

/*
 * $Log: Point3f.java,v $
 * Revision 1.8  1999/10/05  07:03:50  hiranabe
 * copyright change
 *
 * Revision 1.8  1999/10/05  07:03:50  hiranabe
 * copyright change
 *
 * Revision 1.7  1999/03/04  09:16:33  hiranabe
 * small bug fix and copyright change
 *
 * Revision 1.6  1998/10/14  00:49:10  hiranabe
 * API1.1 Beta02
 *
 * Revision 1.5  1998/04/10  04:52:14  hiranabe
 * API1.0 -> API1.1 (added constructors, methods)
 *
 * Revision 1.4  1998/04/09  08:18:15  hiranabe
 * minor comment change
 *
 * Revision 1.3  1998/04/09  07:05:18  hiranabe
 * API 1.1
 *
 * Revision 1.2  1998/01/05  06:29:31  hiranabe
 * copyright 98
 *
 * Revision 1.1  1997/11/26  03:00:44  hiranabe
 * Initial revision
 *
 */


    /**
      * Constructs and initializes a Point3f from the specified xy coordinates.
      * @param x the x coordinate
      * @param y the y coordinate
      * @param z the z coordinate
      */
    public Point3f(float x, float y, float z) {
	super(x, y, z);
    }

    /**
      * Constructs and initializes a Point3f from the specified array.
      * @param p the array of length 3 containing xyz in order
      */
    public Point3f(float p[]) {
	super(p);
    }

    /**
      * Constructs and initializes a Point3f from the specified Point3f.
      * @param p1 the Point3f containing the initialization x y z data
      */
    public Point3f(Point3f p1) {
	super(p1);
    }

    /**
      * Constructs and initializes a Point3f from the specified Point3d.
      * @param p1 the Point3f containing the initialization x y z data
      */
    public Point3f(Point3d p1) {
	super(p1);
    }

    /**
      * Constructs and initializes a Point3f from the specified Tuple3f.
      * @param t1 the Tuple3f containing the initialization x y z data
      */
    public Point3f(Tuple3f t1) {
	super(t1);
    }

    /**
      * Constructs and initializes a Point3f from the specified Tuple3d.
      * @param t1 the Tuple3d containing the initialization x y z data
      */
    public Point3f(Tuple3d t1) {
	super(t1);
    }

    /**
      * Constructs and initializes a Point3f to (0,0,0).
      */
    public Point3f() {
	super();
    }

    /**
      * Computes the square of the distance between this point and point p1.
      * @param  p1 the other point
      * @return the square of distance between these two points as a float
      */
    public final float distanceSquared(Point3f p1) {
	double dx = x - p1.x;
	double dy = y - p1.y;
	double dz = z - p1.z;
	return (float)(dx*dx + dy*dy + dz*dz);
    }

    /**
      * Returns the distance between this point and point p1.
      * @param p1 the other point
      * @return the distance between these two points
      */
    public final float distance(Point3f p1) {
	return (float)Math.sqrt(distanceSquared(p1));
    }

    /**
      * Computes the L-1 (Manhattan) distance between this point and point p1.
      * The L-1 distance is equal to abs(x1-x2) + abs(y1-y2) + abs(z1-z2) .
      * @param p1 the other point
      */
    public final float distanceL1(Point3f p1) {
	return Math.abs(x-p1.x) + Math.abs(y-p1.y) + Math.abs(z-p1.z);
    }

    /**
      * Computes the L-infinite distance between this point and point p1.
      * The L-infinite distance is equal to MAX[abs(x1-x2), abs(y1-y2), abs(z1-z2)].
      * @param p1 the other point
      */
    public final float distanceLinf(Point3f p1) {
	return Math.max(Math.max(Math.abs(x-p1.x), Math.abs(y-p1.y)), Math.abs(z-p1.z));
    }

    /**
      * Multiplies each of the x,y,z components of the Point4f parameter
      * by 1/w and places the projected values into this point.
      * @param p1 the source Point4d, which is not modified
      */
     public final void project(Point4f p1) {
	 // zero div may occur.
	 x = p1.x/p1.w;
	 y = p1.y/p1.w;
	 z = p1.z/p1.w;
     }

}
