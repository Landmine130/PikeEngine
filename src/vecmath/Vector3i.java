package vecmath;

import java.io.Serializable;

public class Vector3i extends Tuple3i implements Serializable {
	
	private static final long serialVersionUID = 1166336911274143984L;
	
	  /**
     * Constructs and initializes a Vector3i from the specified xyz coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
   public Vector3i(int x, int y, int z) {
	super(x, y, z);
   }

   /**
     * Constructs and initializes a Vector3i from the specified array of length 3.
     * @param v the array of length 3 containing xyz in order
     */
   public Vector3i(int v[]) {
	super(v);
   }

   /**
     * Constructs and initializes a Vector3i from the specified Vector3i.
     * @param v1 the Vector3i containing the initialization x y z data
     */
   public Vector3i(Vector3i v1) {
	super(v1);
   }

   /**
     * Constructs and initializes a Vector3i to (0,0,0).
     */
   public Vector3i() {
	super();
   }

   public Vector3i(Vector3f v) {
	   super(v);
}

/**
     * Returns the squared length of this vector.
     * @return the squared length of this vector
     */
   public final float lengthSquared() {
	return x*x + y*y + z*z;
   }

   /**
     * Returns the length of this vector.
     * @return the length of this vector
     */
     public final float length() {
	  return (float)Math.sqrt(lengthSquared());
     }

   /**
     * Sets this vector to be the vector cross product of vectors v1 and v2.
     * @param v1 the first vector
     * @param v2 the second vector
     */
   public final void cross(Vector3i v1, Vector3i v2) {
	// store in tmp once for aliasing-safty
	// i.e. safe when a.cross(a, b)
	set(
	    v1.y*v2.z - v1.z*v2.y,
	    v1.z*v2.x - v1.x*v2.z,
	    v1.x*v2.y - v1.y*v2.x
	    );
   }
   
     
   /**
     * Computes the dot product of the this vector and vector v1.
     * @param  v1 the other vector
     */
   public final float dot(Vector3i v1) {
	return x*v1.x + y*v1.y + z*v1.z;
   }

   /**
     * Sets the value of this vector to the normalization of vector v1.
     * @param v1 the un-normalized vector
     */
   public final void normalize(Vector3i v1) {
	set(v1);
	normalize();
   }

   /**
     * Normalizes this vector in place.
     */
   public final void normalize() {
	double d = length();

	// zero-div may occur.
	x /= d;
	y /= d;
	z /= d;
   }
   
   public final Vector3f toVector3f() {
   	return new Vector3f(this);
   }
}
