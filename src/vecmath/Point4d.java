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
  * A 4 element point that is represented by double precision
  * floating point x,y,z,w coordinates.
  * @version specification 1.1, implementation $Revision: 1.10 $, $Date: 1999/10/05 07:03:50 $
  * @author Kenji hiranabe
  */
public class Point4d extends Tuple4d implements Serializable {
/*
 * $Log: Point4d.java,v $
 * Revision 1.10  1999/10/05  07:03:50  hiranabe
 * copyright change
 *
 * Revision 1.10  1999/10/05  07:03:50  hiranabe
 * copyright change
 *
 * Revision 1.9  1999/10/05  04:55:37  hiranabe
 * Java3D 1.2 support
 * Point4d(Tuple3d t1) constructor
 * set(Tuple3d t1)
 *
 * Revision 1.8  1999/03/04  09:16:33  hiranabe
 * small bug fix and copyright change
 *
 * Revision 1.7  1999/02/28  01:55:03  hiranabe
 * bug in distanceSquared
 * 	double dw = z - p1.w; fixed
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
      * Constructs and initializes a Point4d from the specified xyzw coordinates.
      * @param x the x coordinate
      * @param y the y coordinate
      * @param z the z coordinate
      * @param w the w coordinate
      */
    public Point4d(double x, double y, double z, double w) {
	super(x, y, z, w);
    }

    /**
      * Constructs and initializes a Point4d from the specified array.
      * @param p the array of length 4 containing xyzw in order
      */
    public Point4d(double p[]) {
	super(p);
    }

    /**
      * Constructs and initializes a Point4d from the specified Point4f.
      * @param p1 the Point4f containing the initialization x y z w data
      */
    public Point4d(Point4f p1) {
	super(p1);
    }

    /**
      * Constructs and initializes a Point4d from the specified Point4d.
      * @param p1 the Point4d containing the initialization x y z w data
      */
    public Point4d(Point4d p1) {
	super(p1);
    }

    /**
      * Constructs and initializes a Point4d from the specified Tuple4d.
      * @param t1 the Tuple4d containing the initialization x y z w data
      */
    public Point4d(Tuple4d t1) {
	super(t1);
    }

    /**
      * Constructs and initializes a Point4d from the specified Tuple4f.
      * @param t1 the Tuple4f containing the initialization x y z w data
      */
    public Point4d(Tuple4f t1) {
	super(t1);
    }

    /**
      * Constructs and initializes a Point4d to (0,0,0,0).
      */
    public Point4d() {
	// super(); called implicitly.
    }

    /**
     * Constructs and initializes a Point4d from the specified Tuple3d.
     * The x,y,z  components of this point are set to the corresponding
     * components
     * of tuple t1. The w component of this point is set to 1.
     *
     * @param t1 the tuple to be copied
     * @since Java3D 1.2
     */
    public Point4d(Tuple3d t1) {
        super(t1.x, t1.y, t1.z, 1);
    }

    /**
     * Sets the x,y,z components of this point to the corresponding
     * components of tuple t1. The w component of this point is set to 1.
     *
     * @param t1 the tuple to be copied
     * @since Java3D 1.2
     */
    public final void set(Tuple3d t1) {
        set(t1.x, t1.y, t1.z, 1);
    }

    /**
      * Computes the square of the distance between this point and point p1.
      * @param  p1 the other point
      * @return the square of distance between this point and p1
      */
    public final double distanceSquared(Point4d p1) {
	double dx = x - p1.x;
	double dy = y - p1.y;
	double dz = z - p1.z;
	double dw = w - p1.w;
	return (float)(dx*dx + dy*dy + dz*dz + dw*dw);
    }

    /**
      * Returns the distance between this point and point p1.
      * @param p1 the other point
      * @return the distance between this point and point p1.
      */
    public final double distance(Point4d p1) {
	return Math.sqrt(distanceSquared(p1));
    }

    /**
      * Computes the L-1 (Manhattan) distance between this point and point p1.
      * The L-1 distance is equal to abs(x1-x2) + abs(y1-y2)
      * + abs(z1-z2) + abs(w1-w2).
      * @param p1 the other point
      * @return L-1 distance
      */
    public final double distanceL1(Point4d p1) {
	// return type changed from float to double as of API1.1 Beta02
	return Math.abs(x-p1.x) + Math.abs(y-p1.y)
	    + Math.abs(z-p1.z) + Math.abs(w-p1.w);
    }

    /**
      * Computes the L-infinite distance between this point and point p1.
      * The L-infinite distance is equal to MAX[abs(x1-x2), abs(y1-y2), abs(z1-z2), abs(w1-w2)].
      * @param p1 the other point
      * @return L-infinite distance
      */
    public final double distanceLinf(Point4d p1) {
	// return type changed from float to double as of API1.1 Beta02
	return Math.max(Math.max(Math.abs(x-p1.x), Math.abs(y-p1.y)),
			Math.max(Math.abs(z-p1.z), Math.abs(w-p1.w)));
    }

    /**
      * Multiplies each of the x,y,z components of the Point4d parameter by 1/w,
      * places the projected values into this point, and places a 1 as the w
      * parameter of this point.
      * @param p1 the source Point4d, which is not modified
      */
     public final void project(Point4d p1) {
	 // zero div may occur.
	 x = p1.x/p1.w;
	 y = p1.y/p1.w;
	 z = p1.z/p1.w;
	 w = 1.0;
     }

}
