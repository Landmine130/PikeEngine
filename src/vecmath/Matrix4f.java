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

/*
 * Copyright (c) 2002-2008 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package vecmath;

import java.io.Serializable;
import java.nio.FloatBuffer;

/**
 * A single precision floating point 4 by 4 matrix.
 * @version specification 1.1, implementation $Revision: 1.13 $, $Date: 1999/10/05 07:03:50 $
 * @author Kenji hiranabe
 */
public class Matrix4f implements Serializable {
/*
 * $Log: Matrix4f.java,v $
 * Revision 1.13  1999/10/05  07:03:50  hiranabe
 * copyright change
 *
 * Revision 1.13  1999/10/05  07:03:50  hiranabe
 * copyright change
 *
 * Revision 1.12  1999/03/04  09:16:33  hiranabe
 * small bug fix and copyright change
 *
 * Revision 1.11  1998/10/14  00:49:10  hiranabe
 * API1.1 Beta02
 *
 * Revision 1.10  1998/07/27  04:33:08  hiranabe
 * transpose(M m1) bug. It acted as the same as 'set'.
 *
 * Revision 1.9  1998/07/27  04:28:13  hiranabe
 * API1.1Alpha01 ->API1.1Alpha03
 *
 * Revision 1.8  1998/04/17  10:30:46  hiranabe
 * null check for equals
 *
 * Revision 1.7  1998/04/10  04:52:14  hiranabe
 * API1.0 -> API1.1 (added constructors, methods)
 *
 * Revision 1.6  1998/04/09  08:18:15  hiranabe
 * minor comment change
 *
 * Revision 1.5  1998/04/09  07:05:18  hiranabe
 * API 1.1
 *
 * Revision 1.4  1998/04/08  06:01:08  hiranabe
 * bug fix of set(m,t,s). thanks > t.m.child@surveying.salford.ac.uk
 *
 * Revision 1.3  1998/01/05  06:29:31  hiranabe
 * copyright 98
 *
 * Revision 1.2  1997/12/10  06:08:05  hiranabe
 * toString   '\n' -> "line.separator"
 *
 * Revision 1.1  1997/11/26  03:00:44  hiranabe
 * Initial revision
 *
 */

    /**
	 * 
	 */
	private static final long serialVersionUID = 4331699145977311752L;

	/**
      * The first element of the first column.
      */
    public float m00;

    /**
      * The second element of the first column.
      */
    public float m01;

    /**
      * third element of the first column.
      */
    public float m02;

    /**
      * The fourth element of the first column.
      */
    public float m03;

    /**
      * The first element of the second column.
      */
    public float m10;

    /**
      * The second element of the second column.
      */
    public float m11;

    /**
      * The third element of the second column.
      */
    public float m12;

    /**
      * The fourth element of the second column.
      */
    public float m13;

    /**
      * The first element of the third column.
      */
    public float m20;

    /**
      * The second element of the third column.
      */
    public float m21;

    /**
      * The third element of the third column.
      */
    public float m22;

    /**
      * The fourth element of the third column.
      */
    public float m23;

    /**
      * The first element of the fourth column.
      */
    public float m30;

    /**
      * The second element of the fourth column.
      */
    public float m31;

    /**
      * The third element of the fourth column.
      */
    public float m32;

    /**
      * The fourth element of the fourth column.
      */
    public float m33;

    /**
      * 
      * Constructs and initializes a Matrix4f from the specified 16 values.
      * @param m00 the [0][0] element
      * @param m01 the [0][1] element
      * @param m02 the [0][2] element
      * @param m03 the [0][3] element
      * @param m10 the [1][0] element
      * @param m11 the [1][1] element
      * @param m12 the [1][2] element
      * @param m13 the [1][3] element
      * @param m20 the [2][0] element
      * @param m21 the [2][1] element
      * @param m22 the [2][2] element
      * @param m23 the [2][3] element
      * @param m30 the [3][0] element
      * @param m31 the [3][1] element
      * @param m32 the [3][2] element
      * @param m33 the [3][3] element
      */
    public Matrix4f(float m00, float m10, float m20, float m30, 
                    float m01, float m11, float m21, float m31,
                    float m02, float m12, float m22, float m32,
                    float m03, float m13, float m23, float m33)  {
	set(
	    m00, m01, m02, m03,
	    m10, m11, m12, m13,
	    m20, m21, m22, m23,
	    m30, m31, m32, m33
	    );
    }

    /**
      * Constructs and initializes a Matrix4f from the specified 16
      * element array.  this.m00 =v[0], this.m01=v[1], etc.
      * @param  v the array of length 16 containing in order
      */
    public Matrix4f(float v[]) {
    	set(v);
    }

    /**
      * Constructs and initializes a Matrix4f from the quaternion,
      * translation, and scale values; the scale is applied only to the
      * rotational components of the matrix (upper 3x3) and not to the
      * translational components.
      * @param q1  The quaternion value representing the rotational component
      * @param t1  The translational component of the matrix
      * @param s  The scale value applied to the rotational components
      */
    public Matrix4f(Quat4f q1, Vector3f t1, float s) {
    	set(q1, t1, s);
    }

    /**
      * Constructs a new matrix with the same values as the Matrix4d parameter.
      * @param m1 The source matrix.
      */
    public Matrix4f(Matrix4d m1) {
    	set(m1);
    }

    /**
      * Constructs a new matrix with the same values as the Matrix4f parameter.
      * @param m1 The source matrix.
      */
    public Matrix4f(Matrix4f m1) {
    	set(m1);
    }

    /**
     * Constructs a new matrix with the translation specified by the Vector3f parameter.
     * @param m1 The source matrix.
     */
   public Matrix4f(Vector3f v1) {
	   set(v1);
   }
   
    /**
      * Constructs and initializes a Matrix4f from the rotation matrix,
      * translation, and scale values; the scale is applied only to the
      * rotational components of the matrix (upper 3x3) and not to the
      * translational components.
      * @param m1  The rotation matrix representing the rotational components
      * @param t1  The translational components of the matrix
      * @param s  The scale value applied to the rotational components
      */
    public Matrix4f(Matrix3f m1, Vector3f t1, float s) {
    	set(m1);
    	mulRotationScale(s);
    	setTranslation(t1);
    	m33 = 1.0f;
    }

    /**
      * Constructs and initializes a Matrix4f to the identity matrix.
      */
    public Matrix4f() {
    	setIdentity();
    }

    /**
     * Returns a string that contains the values of this Matrix4f.
     * @return the String representation
     */
    public String toString() {
	String nl = System.getProperty("line.separator");
        return  "[" + nl + "  ["+m00+"\t"+m10+"\t"+m20+"\t"+m30+"]" + nl +
                   "  ["+m01+"\t"+m11+"\t"+m21+"\t"+m31+"]" + nl +
                   "  ["+m02+"\t"+m12+"\t"+m22+"\t"+m32+"]" + nl +
                   "  ["+m03+"\t"+m13+"\t"+m23+"\t"+m33+"] ]";
    }

    /**
     * Sets this Matrix4f to identity.
     */
    public final void setIdentity() {
        m00 = 1.0f; m01 = 0.0f; m02 = 0.0f; m03 = 0.0f;
        m10 = 0.0f; m11 = 1.0f; m12 = 0.0f; m13 = 0.0f;
        m20 = 0.0f; m21 = 0.0f; m22 = 1.0f; m23 = 0.0f;
        m30 = 0.0f; m31 = 0.0f; m32 = 0.0f; m33 = 1.0f;
    }

    /**
     * Sets the specified element of this matrix4f to the value provided.
     * @param column  the column number to be modified (zero indexed)
     * @param row  the row number to be modified (zero indexed)
     * @param value the new value
     */
    public final void setElement(int column, int row, float value) {
	if (column == 0)
	    if (row == 0)
		m00 = value;
	    else if (row == 1)
		m01 = value;
	    else if (row == 2)
		m02 = value;
	    else if (row == 3)
		m03 = value;
	    else
		throw new ArrayIndexOutOfBoundsException("row must be 0 to 3 and is " + row);
	else if (column == 1)
	    if (row == 0)
		m10 = value;
	    else if (row == 1)
		m11 = value;
	    else if (row == 2)
		m12 = value;
	    else if (row == 3)
		m13 = value;
	    else
		throw new ArrayIndexOutOfBoundsException("row must be 0 to 3 and is " + row);
	else if (column == 2)
	    if (row == 0)
		m20 = value;
	    else if (row == 1)
		m21 = value;
	    else if (row == 2)
		m22 = value;
	    else if (row == 3)
		m23 = value;
	    else
		throw new ArrayIndexOutOfBoundsException("row must be 0 to 3 and is " + row);
	else if (column == 3)
	    if (row == 0)
		m30 = value;
	    else if (row == 1)
		m31 = value;
	    else if (row == 2)
		m32 = value;
	    else if (row == 3)
		m33 = value;
	    else
		throw new ArrayIndexOutOfBoundsException("row must be 0 to 3 and is " + row);
	else
		throw new ArrayIndexOutOfBoundsException("column must be 0 to 2 and is " + column);
    }

    /**
     * Retrieves the value at the specified column and row of this matrix.
     * @param column  the column number to be retrieved (zero indexed)
     * @param row  the row number to be retrieved (zero indexed)
     * @return the value at the indexed element
     */
    public final float getElement(int column, int row) {
	if (column == 0)
	    if (row == 0)
		return m00;
	    else if (row == 1)
		return m01;
	    else if (row == 2)
		return m02;
	    else if (row == 3)
		return m03;
	    else
		throw new ArrayIndexOutOfBoundsException("row must be 0 to 3 and is " + row);
	else if (column == 1)
	    if (row == 0)
		return m10;
	    else if (row == 1)
		return m11;
	    else if (row == 2)
		return m12;
	    else if (row == 3)
		return m13;
	    else
		throw new ArrayIndexOutOfBoundsException("row must be 0 to 3 and is " + row);
	else if (column == 2)
	    if (row == 0)
		return m20;
	    else if (row == 1)
		return m21;
	    else if (row == 2)
		return m22;
	    else if (row == 3)
		return m23;
	    else
		throw new ArrayIndexOutOfBoundsException("row must be 0 to 3 and is " + row);
	else if (column == 3)
	    if (row == 0)
		return m30;
	    else if (row == 1)
		return m31;
	    else if (row == 2)
		return m32;
	    else if (row == 3)
		return m33;
	    else
		throw new ArrayIndexOutOfBoundsException("row must be 0 to 3 and is " + row);
	else
		throw new ArrayIndexOutOfBoundsException("column must be 0 to 3 and is " + column);
    }

    /**
      * Sets the scale component of the current matrix by factoring out the
      * current scale (by doing an SVD) from the rotational component and
      * multiplying by the new scale.
      * @param scale the new scale amount
      */
    public final void setScale(float scale) {
	SVD(null, this);
	mulRotationScale(scale);
    }


    /**
      * Performs an SVD normalization of this matrix in order to acquire the
      * normalized rotational component; the values are placed into the Matrix3d parameter.
      * @param m1 matrix into which the rotational component is placed
      */
    public final void get(Matrix3d m1) {
	SVD(m1);
    }

    /**
      * Performs an SVD normalization of this matrix in order to acquire the
      * normalized rotational component; the values are placed into the Matrix3f parameter.
      * @param m1 matrix into which the rotational component is placed
      */
    public final void get(Matrix3f m1) {
	SVD(m1, null);
    }

    /**
      * Performs an SVD normalization of this matrix to calculate the rotation
      * as a 3x3 matrix, the translation, and the scale. None of the matrix values are modified.
      * @param m1 The normalized matrix representing the rotation
      * @param t1 The translation component
      * @return The scale component of this transform
      */
    public final float get(Matrix3f m1, Vector3f t1) {
	get(t1);
	return SVD(m1, null);
    }

    /**
      * Performs an SVD normalization of this matrix in order to acquire the
      * normalized rotational component; the values are placed into
      * the Quat4f parameter.
      * @param q1 quaternion into which the rotation component is placed
      */
    public final void get(Quat4f q1) {
    	q1.set(this);
		q1.normalize();
    }

    /**
      * Retrieves the translational components of this matrix.
      * @param trans the vector that will receive the translational component
      */
    public final void get(Vector3f trans) {
    	trans.x = m30;
		trans.y = m31;
		trans.z = m32;
    }

    /**
      * Gets the upper 3x3 values of this matrix and places them into the matrix m1.
      * @param m1 The matrix that will hold the values
      */
    public final void getRotationScale(Matrix3f m1) {
    	m1.m00 = m00; m1.m01 = m01; m1.m02 = m02;
    	m1.m10 = m10; m1.m11 = m11; m1.m12 = m12;
    	m1.m20 = m20; m1.m21 = m21; m1.m22 = m22;
    }

    /**
      * Performs an SVD normalization of this matrix to calculate and return the
      * uniform scale factor. This matrix is not modified.
      * @return the scale factor of this matrix
      */
    public final float getScale() {
	return SVD(null);
    }

    /**
      * Replaces the upper 3x3 matrix values of this matrix with the values in the matrix m1.
      * @param m1 The matrix that will be the new upper 3x3
      */
    public final void setRotationScale(Matrix3f m1) {
	m00 = m1.m00; m01 = m1.m01; m02 = m1.m02;
	m10 = m1.m10; m11 = m1.m11; m12 = m1.m12;
	m20 = m1.m20; m21 = m1.m21; m22 = m1.m22;
    }

    /**
     * Sets the specified row of this matrix4f to the four values provided.
     * @param row  the row number to be modified (zero indexed)
     * @param x the first column element
     * @param y the second column element
     * @param z the third column element
     * @param w the fourth column element
     */
    public final void setRow(int row, float x, float y, float z, float w) {
	if (row == 0) {
	    m00 = x;
	    m10 = y;
	    m20 = z;
	    m30 = w;
	} else if (row == 1) {
	    m01 = x;
	    m11 = y;
	    m21 = z;
	    m31 = w;
	} else if (row == 2) {
	    m02 = x;
	    m12 = y;
	    m22 = z;
	    m32 = w;
	} else if (row == 3) {
	    m03 = x;
	    m13 = y;
	    m23 = z;
	    m33 = w;
	} else {
	    throw new ArrayIndexOutOfBoundsException("row must be 0 to 3 and is " + row);
	}
    }

    /**
     * Sets the specified row of this matrix4f to the Vector provided.
     * @param row the row number to be modified (zero indexed)
     * @param v the replacement row
     */
    public final void setRow(int row, Vector4f v) {
	if (row == 0) {
	    m00 = v.x;
	    m10 = v.y;
	    m20 = v.z;
	    m30 = v.w;
	} else if (row == 1) {
	    m01 = v.x;
	    m11 = v.y;
	    m21 = v.z;
	    m31 = v.w;
	} else if (row == 2) {
	    m02 = v.x;
	    m12 = v.y;
	    m22 = v.z;
	    m32 = v.w;
	} else if (row == 3) {
	    m03 = v.x;
	    m13 = v.y;
	    m23 = v.z;
	    m33 = v.w;
	} else {
	    throw new ArrayIndexOutOfBoundsException("row must be 0 to 3 and is " + row);
	}
    }

    /**
      * Sets the specified row of this matrix4f to the four values provided.
      * @param row the row number to be modified (zero indexed)
      * @param v the replacement row
      */
    public final void setRow(int row, float v[]) {
	if (row == 0) {
	    m00 = v[0];
	    m10 = v[1];
	    m20 = v[2];
	    m30 = v[3];
	} else if (row == 1) {
	    m01 = v[0];
	    m11 = v[1];
	    m21 = v[2];
	    m31 = v[3];
	} else if (row == 2) {
	    m02 = v[0];
	    m12 = v[1];
	    m22 = v[2];
	    m32 = v[3];
	} else if (row == 3) {
	    m03 = v[0];
	    m13 = v[1];
	    m23 = v[2];
	    m33 = v[3];
	} else {
	    throw new ArrayIndexOutOfBoundsException("row must be 0 to 3 and is " + row);
	}
    }

    /**
     * Copies the matrix values in the specified row into the
     * vector parameter.
     * @param row the matrix row
     * @param v The vector into which the matrix row values will be copied
     */
    public final void getRow(int row, Vector4f v) {
	if (row == 0) {
	    v.x = m00;
	    v.y = m10;
	    v.z = m20;
	    v.w = m30;
	} else if (row == 1) {
	    v.x = m01;
	    v.y = m11;
	    v.z = m21;
	    v.w = m31;
	} else if (row == 2) {
	    v.x = m02;
	    v.y = m12;
	    v.z = m22;
	    v.w = m32;
	} else if (row == 3) {
	    v.x = m03;
	    v.y = m13;
	    v.z = m23;
	    v.w = m33;
	} else {
	    throw new ArrayIndexOutOfBoundsException("row must be 0 to 3 and is " + row);
	}
    }

    /**
      * Copies the matrix values in the specified row into the
      * array parameter.
      * @param row the matrix row
      * @param v The array into which the matrix row values will be copied
      */
    public final void getRow(int row, float v[]) {
	if (row == 0) {
	    v[0] = m00;
	    v[1] = m10;
	    v[2] = m20;
	    v[3] = m30;
	} else if (row == 1) {
	    v[0] = m01;
	    v[1] = m11;
	    v[2] = m21;
	    v[3] = m31;
	} else if (row == 2) {
	    v[0] = m02;
	    v[1] = m12;
	    v[2] = m22;
	    v[3] = m32;
	} else if (row == 3) {
	    v[0] = m03;
	    v[1] = m13;
	    v[2] = m23;
	    v[3] = m33;
	} else {
	    throw new ArrayIndexOutOfBoundsException("row must be 0 to 3 and is " + row);
	}
    }

    /**
      * Sets the specified column of this matrix4f to the four values provided.
      * @param  column the column number to be modified (zero indexed)
      * @param x the first row element
      * @param y the second row element
      * @param z the third row element
      * @param w the fourth row element
      */
    public final void setColumn(int column, float x, float y, float z, float w) {
	if (column == 0) {
	    m00 = x;
	    m01 = y;
	    m02 = z;
	    m03 = w;
	}  else if (column == 1) {
	    m10 = x;
	    m11 = y;
	    m12 = z;
	    m13 = w;
	} else if (column == 2) {
	    m20 = x;
	    m21 = y;
	    m22 = z;
	    m23 = w;
	} else if (column == 3) {
	    m30 = x;
	    m31 = y;
	    m32 = z;
	    m33 = w;
	} else {
	    throw new ArrayIndexOutOfBoundsException("column must be 0 to 3 and is " + column);
	}
    }

    /**
      * Sets the specified column of this matrix4f to the vector provided.
      * @param column the column number to be modified (zero indexed)
      * @param v the replacement column
      */
    public final void setColumn(int column, Vector4f v) {
	if (column == 0) {
	    m00 = v.x;
	    m01 = v.y;
	    m02 = v.z;
	    m03 = v.w;
	} else if (column == 1) {
	    m10 = v.x;
	    m11 = v.y;
	    m12 = v.z;
	    m13 = v.w;
	} else if (column == 2) {
	    m20 = v.x;
	    m21 = v.y;
	    m22 = v.z;
	    m23 = v.w;
	} else if (column == 3) {
	    m30 = v.x;
	    m31 = v.y;
	    m32 = v.z;
	    m33 = v.w;
	} else {
	    throw new ArrayIndexOutOfBoundsException("column must be 0 to 3 and is " + column);
	}
    }

    /**
      * Sets the specified column of this matrix4f to the four values provided. 
      * @param column  the column number to be modified (zero indexed) 
      * @param v       the replacement column 
      */
    public final void setColumn(int column,  float v[]) {
	if (column == 0) {
	    m00 = v[0];
	    m01 = v[1];
	    m02 = v[2];
	    m03 = v[3];
	} else if (column == 1) {
	    m10 = v[0];
	    m11 = v[1];
	    m12 = v[2];
	    m13 = v[3];
	} else if (column == 2) {
	    m20 = v[0];
	    m21 = v[1];
	    m22 = v[2];
	    m23 = v[3];
	} else if (column == 3) {
	    m30 = v[0];
	    m31 = v[1];
	    m32 = v[2];
	    m33 = v[3];
	} else {
	    throw new ArrayIndexOutOfBoundsException("column must be 0 to 3 and is " + column);
	}
    }

    /**
     * Copies the matrix values in the specified column into the
     * vector parameter.
     * @param column the matrix column
     * @param v The vector into which the matrix column values will be copied
     */
    public final void getColumn(int column, Vector4f v) {
	if (column == 0) {
	    v.x = m00;
	    v.y = m01;
	    v.z = m02;
	    v.w = m03;
	} else if (column == 1) {
	    v.x = m10;
	    v.y = m11;
	    v.z = m12;
	    v.w = m13;
	} else if (column == 2) {
	    v.x = m20;
	    v.y = m21;
	    v.z = m22;
	    v.w = m23;
	} else if (column == 3) {
	    v.x = m30;
	    v.y = m31;
	    v.z = m32;
	    v.w = m33;
	} else {
	    throw new ArrayIndexOutOfBoundsException("column must be 0 to 3 and is " + column);
	}
    }

    /**
      * Copies the matrix values in the specified column into the
      * array parameter.
      * @param column the matrix column
      * @param v The array into which the matrix column values will be copied
      */
    public final void getColumn(int column, float v[]) {
	if (column == 0) {
	    v[0] = m00;
	    v[1] = m01;
	    v[2] = m02;
	    v[3] = m03;
	} else if (column == 1) {
	    v[0] = m10;
	    v[1] = m11;
	    v[2] = m12;
	    v[3] = m13;
	} else if (column == 2) {
	    v[0] = m20;
	    v[1] = m21;
	    v[2] = m22;
	    v[3] = m23;
	} else if (column == 3) {
	    v[0] = m30;
	    v[1] = m31;
	    v[2] = m32;
	    v[3] = m33;
	} else {
	    throw new ArrayIndexOutOfBoundsException("column must be 0 to 3 and is " + column);
	}
    }

    /**
      * Adds a scalar to each component of this matrix.
      * @param scalar The scalar adder.
      */
    public final void add(float scalar) {
	m00 += scalar; m01 += scalar; m02 += scalar; m03 += scalar;
	m10 += scalar; m11 += scalar; m12 += scalar; m13 += scalar;
	m20 += scalar; m21 += scalar; m22 += scalar; m23 += scalar;
	m30 += scalar; m31 += scalar; m32 += scalar; m33 += scalar;
    }

    /**
      * Adds a scalar to each component of the matrix m1 and places
      * the result into this. Matrix m1 is not modified.
      * @param scalar The scalar adder.
      * @parm m1 The original matrix values.
      */
    public final void add(float scalar, Matrix4f m1) {
	set(m1);
	add(scalar);
    }


    /**
     * Sets the value of this matrix to the matrix sum of matrices m1 and m2. 
     * @param m1 the first matrix 
     * @param m2 the second matrix 
     */
    public final void add(Matrix4f m1, Matrix4f m2) {
	set(m1);
	add(m2);
    }

    /**
     * Sets the value of this matrix to sum of itself and matrix m1. 
     * @param m1 the other matrix 
     */
    public final void add(Matrix4f m1) {
	m00 += m1.m00; m01 += m1.m01; m02 += m1.m02; m03 += m1.m03;
	m10 += m1.m10; m11 += m1.m11; m12 += m1.m12; m13 += m1.m13;
	m20 += m1.m20; m21 += m1.m21; m22 += m1.m22; m23 += m1.m23;
	m30 += m1.m30; m31 += m1.m31; m32 += m1.m32; m33 += m1.m33;
    }

    /**
      * Sets the value of this matrix to the matrix difference
      * of matrices m1 and m2. 
      * @param m1 the first matrix 
      * @param m2 the second matrix 
      */
    public final void sub(Matrix4f m1, Matrix4f m2) {
	// note this is alias safe.
	set(
	    m1.m00 - m2.m00,
	    m1.m01 - m2.m01,
	    m1.m02 - m2.m02,
	    m1.m03 - m2.m03,
	    m1.m10 - m2.m10,
	    m1.m11 - m2.m11,
	    m1.m12 - m2.m12,
	    m1.m13 - m2.m13,
	    m1.m20 - m2.m20,
	    m1.m21 - m2.m21,
	    m1.m22 - m2.m22,
	    m1.m23 - m2.m23,
	    m1.m30 - m2.m30,
	    m1.m31 - m2.m31,
	    m1.m32 - m2.m32,
	    m1.m33 - m2.m33
	    );
    }

    /**
     * Sets the value of this matrix to the matrix difference of itself
     * and matrix m1 (this = this - m1). 
     * @param m1 the other matrix 
     */
    public final void sub(Matrix4f m1) {
	m00 -= m1.m00; m01 -= m1.m01; m02 -= m1.m02; m03 -= m1.m03;
	m10 -= m1.m10; m11 -= m1.m11; m12 -= m1.m12; m13 -= m1.m13;
	m20 -= m1.m20; m21 -= m1.m21; m22 -= m1.m22; m23 -= m1.m23;
	m30 -= m1.m30; m31 -= m1.m31; m32 -= m1.m32; m33 -= m1.m33;
    }
    
    public final void divide(float scalar) {
	m00 /= scalar; m01 /= scalar; m02 /= scalar; m03 /= scalar;
	m10 /= scalar; m11 /= scalar; m12 /= scalar; m13 /= scalar;
	m20 /= scalar; m21 /= scalar; m22 /= scalar; m23 /= scalar;
	m30 /= scalar; m31 /= scalar; m32 /= scalar; m33 /= scalar;
    }

    /**
      * Sets the value of this matrix to its transpose. 
      */
    public final void transpose() {
	float tmp = m01;
	m01 = m10;
	m10 = tmp;

	tmp = m02;
	m02 = m20;
	m20 = tmp;

	tmp = m03;
	m03 = m30;
	m30 = tmp;

	tmp = m12;
	m12 = m21;
	m21 = tmp;

	tmp = m13;
	m13 = m31;
	m31 = tmp;

	tmp = m23;
	m23 = m32;
	m32 = tmp;
    }

    /**
     * Sets the value of this matrix to the transpose of the argument matrix
     * @param m1 the matrix to be transposed 
     */
    public final void transpose(Matrix4f m1) {
	// alias-safe
	set(m1);
	transpose();
    }

    /**
     * Sets the value of this matrix to the matrix conversion of the
     * single precision quaternion argument. 
     * @param q1 the quaternion to be converted 
     */
    public final void set(Quat4f q1)  {
	setFromQuat(q1.x, q1.y, q1.z, q1.w);
    }
    /**
      * Sets the value of this matrix to the matrix conversion of the
      * single precision axis and angle argument. 
      * @param a1 the axis and angle to be converted 
      */
    public final void set(AxisAngle4f a1) {
	setFromAxisAngle(a1.x, a1.y, a1.z, a1.angle);
    }

    /**
      * Sets the value of this matrix to the matrix conversion of the
      * (double precision) quaternion argument. 
      * @param q1 the quaternion to be converted 
      */
    public final void set(Quat4d q1) {
	setFromQuat(q1.x, q1.y, q1.z, q1.w);
    }

    /**
      * Sets the value of this matrix to the matrix conversion of the
      * single precision axis and angle argument. 
      * @param a1 the axis and angle to be converted 
      */
    public final void set(AxisAngle4d a1) {
	setFromAxisAngle(a1.x, a1.y, a1.z, a1.angle);
    }

  /**
    * Sets the value of this matrix from the rotation expressed by the
    * quaternion q1, the translation t1, and the scale s.
    * @param q1  the rotation expressed as a quaternion
    * @param t1  the translation
    * @param s  the scale value
    */
    public final void set(Quat4d q1, Vector3d t1, double s) {
	set(q1);
	mulRotationScale((float)s);
	m30 = (float)t1.x;
	m31 = (float)t1.y;
	m32 = (float)t1.z;
    }

  /**
    * Sets the value of this matrix from the rotation expressed by the
    * quaternion q1, the translation t1, and the scale s.
    * @param q1  the rotation expressed as a quaternion
    * @param t1  the translation
    * @param s  the scale value
    */
    public final void set(Quat4f q1, Vector3f t1, float s) {
	set(q1);
	mulRotationScale(s);
	m30 = t1.x;
	m31 = t1.y;
	m32 = t1.z;
    }

    /**
      * Sets the value of this matrix to a copy of the
      * passed matrix m1.
      * @param m1 the matrix to be copied
      */
    public final void set(Matrix4d m1) {
	m00 = (float)m1.m00; m01 = (float)m1.m01; m02 = (float)m1.m02; m03 = (float)m1.m03;
	m10 = (float)m1.m10; m11 = (float)m1.m11; m12 = (float)m1.m12; m13 = (float)m1.m13;
	m20 = (float)m1.m20; m21 = (float)m1.m21; m22 = (float)m1.m22; m23 = (float)m1.m23;
	m30 = (float)m1.m30; m31 = (float)m1.m31; m32 = (float)m1.m32; m33 = (float)m1.m33;
    }

    /**
      * Sets the value of this matrix to a copy of the
      * passed matrix m1.
      * @param m1 the matrix to be copied
      */
    public final void set(Matrix4f m1) {
	m00 = m1.m00; m01 = m1.m01; m02 = m1.m02; m03 = m1.m03;
	m10 = m1.m10; m11 = m1.m11; m12 = m1.m12; m13 = m1.m13;
	m20 = m1.m20; m21 = m1.m21; m22 = m1.m22; m23 = m1.m23;
	m30 = m1.m30; m31 = m1.m31; m32 = m1.m32; m33 = m1.m33;
    }



    /**
     * Sets the value of this matrix to the matrix inverse
     * of the passed matrix m1. 
     * @param m1 the matrix to be inverted 
     */
    public final void invert(Matrix4f m1)  {
	set(m1);
	invert();
    }

    /**
     * Sets the value of this matrix to its inverse.
     */
    public final void invert() {
	float s = determinant();
	if (s == 0.0)
	    return;
	s = 1/s;
	// alias-safe way.
	// less *,+,- calculation than expanded expression.
	set(
	    m11*(m22*m33 - m32*m23) + m21*(m32*m13 - m12*m33) + m31*(m12*m23 - m22*m13),
	    m12*(m20*m33 - m30*m23) + m22*(m30*m13 - m10*m33) + m32*(m10*m23 - m20*m13),
	    m13*(m20*m31 - m30*m21) + m23*(m30*m11 - m10*m31) + m33*(m10*m21 - m20*m11),
	    m10*(m31*m22 - m21*m32) + m20*(m11*m32 - m31*m12) + m30*(m21*m12 - m11*m22),

	    m21*(m02*m33 - m32*m03) + m31*(m22*m03 - m02*m23) + m01*(m32*m23 - m22*m33),
	    m22*(m00*m33 - m30*m03) + m32*(m20*m03 - m00*m23) + m02*(m30*m23 - m20*m33),
	    m23*(m00*m31 - m30*m01) + m33*(m20*m01 - m00*m21) + m03*(m30*m21 - m20*m31),
	    m20*(m31*m02 - m01*m32) + m30*(m01*m22 - m21*m02) + m00*(m21*m32 - m31*m22),

	    m31*(m02*m13 - m12*m03) + m01*(m12*m33 - m32*m13) + m11*(m32*m03 - m02*m33),
	    m32*(m00*m13 - m10*m03) + m02*(m10*m33 - m30*m13) + m12*(m30*m03 - m00*m33),
	    m33*(m00*m11 - m10*m01) + m03*(m10*m31 - m30*m11) + m13*(m30*m01 - m00*m31),
	    m30*(m11*m02 - m01*m12) + m00*(m31*m12 - m11*m32) + m10*(m01*m32 - m31*m02),

	    m01*(m22*m13 - m12*m23) + m11*(m02*m23 - m22*m03) + m21*(m12*m03 - m02*m13),
	    m02*(m20*m13 - m10*m23) + m12*(m00*m23 - m20*m03) + m22*(m10*m03 - m00*m13),
	    m03*(m20*m11 - m10*m21) + m13*(m00*m21 - m20*m01) + m23*(m10*m01 - m00*m11),
	    m00*(m11*m22 - m21*m12) + m10*(m21*m02 - m01*m22) + m20*(m01*m12 - m11*m02)
	    );

	mul(s);
    }

    /**
     * Computes the determinant of this matrix. 
     * @return the determinant of the matrix 
     */
    public final float determinant()  {
	// less *,+,- calculation than expanded expression.
	return
	    (m00*m11 - m10*m01)*(m22*m33 - m32*m23)
	   -(m00*m21 - m20*m01)*(m12*m33 - m32*m13)
	   +(m00*m31 - m30*m01)*(m12*m23 - m22*m13)
	   +(m10*m21 - m20*m11)*(m02*m33 - m32*m03)
	   -(m10*m31 - m30*m11)*(m02*m23 - m22*m03)
	   +(m20*m31 - m30*m21)*(m02*m13 - m12*m03);

    }


    /**
      * Sets the rotational component (upper 3x3) of this matrix to the matrix
      * values in the single precision Matrix3f argument; the other elements of
      * this matrix are initialized as if this were an identity matrix
      * (ie, affine matrix with no translational component).
      * @param m1 the 3x3 matrix
      */
    public final void set(Matrix3f m1)  {
	m00 = m1.m00; m01 = m1.m01; m02 = m1.m02; m03 = 0.0f;
	m10 = m1.m10; m11 = m1.m11; m12 = m1.m12; m13 = 0.0f;
	m20 = m1.m20; m21 = m1.m21; m22 = m1.m22; m23 = 0.0f;
	m30 =   0.0f; m31 =   0.0f; m32 =   0.0f; m33 = 1.0f;
    }

    /**
      * Sets the rotational component (upper 3x3) of this matrix to the matrix
      * values in the double precision Matrix3d argument; the other elements of
      * this matrix are initialized as if this were an identity matrix
      * (ie, affine matrix with no translational component).
      * @param m1 the 3x3 matrix
      */
    public final void set(Matrix3d m1)  {
	m00 = (float)m1.m00; m01 = (float)m1.m01; m02 = (float)m1.m02; m03 = 0.0f;
	m10 = (float)m1.m10; m11 = (float)m1.m11; m12 = (float)m1.m12; m13 = 0.0f;
	m20 = (float)m1.m20; m21 = (float)m1.m21; m22 = (float)m1.m22; m23 = 0.0f;
	m30 =    0.0f;       m31 =    0.0f;       m32 =    0.0f;       m33 = 1.0f;
    }

    /**
     * Sets the value of this matrix to a scale matrix with the
     * passed scale amount. 
     * @param scale the scale factor for the matrix 
     */
    public final void set(float scale)  {
	m00 = scale; m01 = 0.0f;  m02 = 0.0f;  m03 = 0.0f;
	m10 = 0.0f;  m11 = scale; m12 = 0.0f;  m13 = 0.0f;
	m20 = 0.0f;  m21 = 0.0f;  m22 = scale; m23 = 0.0f;
	m30 = 0.0f;  m31 = 0.0f;  m32 = 0.0f;  m33 = 1.0f;
    }


    /**
      * Sets the values in this Matrix4f equal to the row-major array parameter
      * (ie, the first four elements of the array will be copied into the first
      * row of this matrix, etc.).
      */
    public final void set(float m[]) {
	m00 = m[ 0]; m10 = m[ 1]; m20 = m[ 2]; m30 = m[ 3];
	m01 = m[ 4]; m11 = m[ 5]; m21 = m[ 6]; m31 = m[ 7];
	m02 = m[ 8]; m12 = m[ 9]; m22 = m[10]; m32 = m[11];
	m03 = m[12]; m13 = m[13]; m23 = m[14]; m33 = m[15];
    }

    /**
     * Sets the value of this matrix to a translate matrix by the
     * passed translation value.
     * @param v1 the translation amount
     */
    public final void set(Vector3f v1) {
		setIdentity();
		setTranslation(v1);
    }

    public final void set(Vector3d v1) {
    	setIdentity();
		setTranslation(v1);
    }
    
    /**
     * Sets the value of this matrix to a scale and translation matrix;
     * scale is not applied to the translation and all of the matrix
     * values are modified.
     * @param scale the scale factor for the matrix
     * @param v1 the translation amount
     */
    public final void set(float scale, Vector3f v1) {
	set(scale);
	setTranslation(v1);
    }

    /**
     * Sets the value of this matrix to a scale and translation matrix;
     * the translation is scaled by the scale factor and all of the
     * matrix values are modified.
     * @param v1 the translation amount
     * @param scale the scale factor for the matrix
     */
    public final void set(Vector3f v1, float scale) {
	m00 = scale; m10 = 0.0f;  m20 = 0.0f;  m30 = scale*v1.x;
	m01 = 0.0f;  m11 = scale; m21 = 0.0f;  m31 = scale*v1.y;
	m02 = 0.0f;  m12 = 0.0f;  m22 = scale; m32 = scale*v1.z;
	m03 = 0.0f;  m13 = 0.0f;  m23 = 0.0f;  m33 = 1.0f;
    }


    /**
      * Sets the value of this matrix from the rotation expressed by the
      * rotation matrix m1, the translation t1, and the scale s. The translation
      * is not modified by the scale.
      * @param m1 The rotation component
      * @param t1 The translation component
      * @param scale The scale component
      */
    public final void set(Matrix3f m1, Vector3f t1, float scale) {
	setRotationScale(m1);
	mulRotationScale(scale);
	setTranslation(t1);
	m33 = 1.0f;
    }

    /**
      * Sets the value of this matrix from the rotation expressed by the
      * rotation matrix m1, the translation t1, and the scale s. The translation
      * is not modified by the scale.
      * @param m1 The rotation component
      * @param t1 The translation component
      * @param scale The scale component
      */
    public final void set(Matrix3d m1, Vector3d t1, double scale) {
	setRotationScale(m1);
	mulRotationScale((float)scale);
	setTranslation(t1);
	m33 = 1.0f;
    }

    /**
      * Modifies the translational components of this matrix to the values of
      * the Vector3f argument; the other values of this matrix are not modified.
      * @param trans the translational component
      */
    public void setTranslation(Vector3f trans) {
	m30 = trans.x;
	m31 = trans.y;
	m32 = trans.z;
    }

	/**
	 * Rotates the matrix around the given axis the specified angle
	 * @param angle the angle, in radians.
	 * @param axis The vector representing the rotation axis. Must be normalized.
	 * @return this
	 */
	public Matrix4f rotate(float angle, Vector3f axis) {
		return rotate(angle, axis, this);
	}

	/**
	 * Rotates the matrix around the given axis the specified angle
	 * @param angle the angle, in radians.
	 * @param axis The vector representing the rotation axis. Must be normalized.
	 * @param dest The matrix to put the result, or null if a new matrix is to be created
	 * @return The rotated matrix
	 */
	public Matrix4f rotate(float angle, Vector3f axis, Matrix4f dest) {
		return rotate(angle, axis, this, dest);
	}

	/**
	 * Rotates the source matrix around the given axis the specified angle and
	 * put the result in the destination matrix.
	 * @param angle the angle, in radians.
	 * @param axis The vector representing the rotation axis. Must be normalized.
	 * @param src The matrix to rotate
	 * @param dest The matrix to put the result, or null if a new matrix is to be created
	 * @return The rotated matrix
	 */
	public static Matrix4f rotate(float angle, Vector3f axis, Matrix4f src, Matrix4f dest) {
		if (dest == null)
			dest = new Matrix4f();
		float c = (float) Math.cos(angle);
		float s = (float) Math.sin(angle);
		float oneminusc = 1.0f - c;
		float xy = axis.x*axis.y;
		float yz = axis.y*axis.z;
		float xz = axis.x*axis.z;
		float xs = axis.x*s;
		float ys = axis.y*s;
		float zs = axis.z*s;

		float f00 = axis.x*axis.x*oneminusc+c;
		float f01 = xy*oneminusc+zs;
		float f02 = xz*oneminusc-ys;
		// n[3] not used
		float f10 = xy*oneminusc-zs;
		float f11 = axis.y*axis.y*oneminusc+c;
		float f12 = yz*oneminusc+xs;
		// n[7] not used
		float f20 = xz*oneminusc+ys;
		float f21 = yz*oneminusc-xs;
		float f22 = axis.z*axis.z*oneminusc+c;

		float t00 = src.m00 * f00 + src.m10 * f01 + src.m20 * f02;
		float t01 = src.m01 * f00 + src.m11 * f01 + src.m21 * f02;
		float t02 = src.m02 * f00 + src.m12 * f01 + src.m22 * f02;
		float t03 = src.m03 * f00 + src.m13 * f01 + src.m23 * f02;
		float t10 = src.m00 * f10 + src.m10 * f11 + src.m20 * f12;
		float t11 = src.m01 * f10 + src.m11 * f11 + src.m21 * f12;
		float t12 = src.m02 * f10 + src.m12 * f11 + src.m22 * f12;
		float t13 = src.m03 * f10 + src.m13 * f11 + src.m23 * f12;
		dest.m20 = src.m00 * f20 + src.m10 * f21 + src.m20 * f22;
		dest.m21 = src.m01 * f20 + src.m11 * f21 + src.m21 * f22;
		dest.m22 = src.m02 * f20 + src.m12 * f21 + src.m22 * f22;
		dest.m23 = src.m03 * f20 + src.m13 * f21 + src.m23 * f22;
		dest.m00 = t00;
		dest.m01 = t01;
		dest.m02 = t02;
		dest.m03 = t03;
		dest.m10 = t10;
		dest.m11 = t11;
		dest.m12 = t12;
		dest.m13 = t13;
		return dest;
	}
    
    /**
     * Rotates this matrix about the x axis
     * by the passed angle.
     * @param angle the angle to rotate about the X axis in radians 
     */
    public final void rotX(float angle)  {
    	rotate(angle, Vector3f.X_UNIT_VECTOR, this, this);
    }

    /**
     * Rotates this matrix about the y axis
     * by the passed angle.
     * @param angle the angle to rotate about the Y axis in radians 
     */
    public final void rotY(float angle)  {
    	rotate(angle, Vector3f.Y_UNIT_VECTOR, this, this);
    }

    /**
     * Rotates this matrix about the z axis
     * by the passed angle.
     * @param angle the angle to rotate about the Z axis in radians
     */
    public final void rotZ(float angle)  {
    	rotate(angle, Vector3f.Z_UNIT_VECTOR, this, this);
    }

    /**
      * Multiplies each element of this matrix by a scalar.
      * @param scalar The scalar multiplier.
      */
     public final void mul(float scalar) {
	m00 *= scalar; m01 *= scalar;  m02 *= scalar; m03 *= scalar;
	m10 *= scalar; m11 *= scalar;  m12 *= scalar; m13 *= scalar;
	m20 *= scalar; m21 *= scalar;  m22 *= scalar; m23 *= scalar;
	m30 *= scalar; m31 *= scalar;  m32 *= scalar; m33 *= scalar;
     }

 	public Matrix4f translate(Vector3f vec) {
		return translate(vec, this, this);
	}
     
 	public Matrix4f translate(Vector3f vec, Matrix4f dest) {
		return translate(vec, this, dest);
	}

	/**
	 * Translate the source matrix and stash the result in the destination matrix
	 * @param vec The vector to translate by
	 * @param src The source matrix
	 * @param dest The destination matrix or null if a new matrix is to be created
	 * @return The translated matrix
	 */
	public static Matrix4f translate(Vector3f vec, Matrix4f src, Matrix4f dest) {
		if (dest == null)
			dest = new Matrix4f();

		dest.m30 += src.m00 * vec.x + src.m10 * vec.y + src.m20 * vec.z;
		dest.m31 += src.m01 * vec.x + src.m11 * vec.y + src.m21 * vec.z;
		dest.m32 += src.m02 * vec.x + src.m12 * vec.y + src.m22 * vec.z;
		dest.m33 += src.m03 * vec.x + src.m13 * vec.y + src.m23 * vec.z;

		return dest;
	}

	/**
	 * Translate this matrix and stash the result in another matrix
	 * @param vec The vector to translate by
	 * @param dest The destination matrix or null if a new matrix is to be created
	 * @return the translated matrix
	 */
	public Matrix4f translate(Vector2f vec, Matrix4f dest) {
		return translate(vec, this, dest);
	}

	/**
	 * Translate the source matrix and stash the result in the destination matrix
	 * @param vec The vector to translate by
	 * @param src The source matrix
	 * @param dest The destination matrix or null if a new matrix is to be created
	 * @return The translated matrix
	 */
	public static Matrix4f translate(Vector2f vec, Matrix4f src, Matrix4f dest) {
		if (dest == null)
			dest = new Matrix4f();

		dest.m30 += src.m00 * vec.x + src.m10 * vec.y;
		dest.m31 += src.m01 * vec.x + src.m11 * vec.y;
		dest.m32 += src.m02 * vec.x + src.m12 * vec.y;
		dest.m33 += src.m03 * vec.x + src.m13 * vec.y;

		return dest;
	}
     
    /**
      * Multiplies each element of matrix m1 by a scalar and places the result
      * into this. Matrix m1 is not modified.
      * @param scalar The scalar multiplier.
      * @param m1 The original matrix.
      */
     public final void mul(float scalar, Matrix4f m1) {
	 set(m1);
	 mul(scalar);
     }

    /**
     * Sets the value of this matrix to the result of multiplying itself
     * with matrix m1. 
     * @param m1 the other matrix 
     */
    public final void mul(Matrix4f m1) {
	mul(this, m1);
    }

    /**
     * Sets the value of this matrix to the result of multiplying
     * the two argument matrices together. 
     * @param m1 the first matrix 
     * @param m2 the second matrix 
     */
    public final void mul(Matrix4f m1, Matrix4f m2) {
	// alias-safe way.
	set(
	    m1.m00*m2.m00 + m1.m10*m2.m01 + m1.m20*m2.m02 + m1.m30*m2.m03,
	    m1.m00*m2.m10 + m1.m10*m2.m11 + m1.m20*m2.m12 + m1.m30*m2.m13,
	    m1.m00*m2.m20 + m1.m10*m2.m21 + m1.m20*m2.m22 + m1.m30*m2.m23,
	    m1.m00*m2.m30 + m1.m10*m2.m31 + m1.m20*m2.m32 + m1.m30*m2.m33,

	    m1.m01*m2.m00 + m1.m11*m2.m01 + m1.m21*m2.m02 + m1.m31*m2.m03,
	    m1.m01*m2.m10 + m1.m11*m2.m11 + m1.m21*m2.m12 + m1.m31*m2.m13,
	    m1.m01*m2.m20 + m1.m11*m2.m21 + m1.m21*m2.m22 + m1.m31*m2.m23,
	    m1.m01*m2.m30 + m1.m11*m2.m31 + m1.m21*m2.m32 + m1.m31*m2.m33,

	    m1.m02*m2.m00 + m1.m12*m2.m01 + m1.m22*m2.m02 + m1.m32*m2.m03,
	    m1.m02*m2.m10 + m1.m12*m2.m11 + m1.m22*m2.m12 + m1.m32*m2.m13,
	    m1.m02*m2.m20 + m1.m12*m2.m21 + m1.m22*m2.m22 + m1.m32*m2.m23,
	    m1.m02*m2.m30 + m1.m12*m2.m31 + m1.m22*m2.m32 + m1.m32*m2.m33,

	    m1.m03*m2.m00 + m1.m13*m2.m01 + m1.m23*m2.m02 + m1.m33*m2.m03,
	    m1.m03*m2.m10 + m1.m13*m2.m11 + m1.m23*m2.m12 + m1.m33*m2.m13,
	    m1.m03*m2.m20 + m1.m13*m2.m21 + m1.m23*m2.m22 + m1.m33*m2.m23,
	    m1.m03*m2.m30 + m1.m13*m2.m31 + m1.m23*m2.m32 + m1.m33*m2.m33
	    );
  }

    /**
      * Multiplies the transpose of matrix m1 times the transpose of matrix m2,
      * and places the result into this.
      * @param m1 The matrix on the left hand side of the multiplication
      * @param m2 The matrix on the right hand side of the multiplication
      */
    public final void mulTransposeBoth(Matrix4f m1, Matrix4f m2) {
	mul(m2, m1);
	transpose();
    }
/*
    /**
      * Multiplies matrix m1 times the transpose of matrix m2, and places the
      * result into this.
      * @param m1 The matrix on the left hand side of the multiplication
      * @param m2 The matrix on the right hand side of the multiplication
      *
    public final void mulTransposeRight(Matrix4f m1, Matrix4f m2) {
	// alias-safe way.
	set(
	    m1.m00*m2.m00 + m1.m01*m2.m01 + m1.m02*m2.m02 + m1.m03*m2.m03,
	    m1.m00*m2.m10 + m1.m01*m2.m11 + m1.m02*m2.m12 + m1.m03*m2.m13,
	    m1.m00*m2.m20 + m1.m01*m2.m21 + m1.m02*m2.m22 + m1.m03*m2.m23,
	    m1.m00*m2.m30 + m1.m01*m2.m31 + m1.m02*m2.m32 + m1.m03*m2.m33,

	    m1.m10*m2.m00 + m1.m11*m2.m01 + m1.m12*m2.m02 + m1.m13*m2.m03,
	    m1.m10*m2.m10 + m1.m11*m2.m11 + m1.m12*m2.m12 + m1.m13*m2.m13,
	    m1.m10*m2.m20 + m1.m11*m2.m21 + m1.m12*m2.m22 + m1.m13*m2.m23,
	    m1.m10*m2.m30 + m1.m11*m2.m31 + m1.m12*m2.m32 + m1.m13*m2.m33,

	    m1.m20*m2.m00 + m1.m21*m2.m01 + m1.m22*m2.m02 + m1.m23*m2.m03,
	    m1.m20*m2.m10 + m1.m21*m2.m11 + m1.m22*m2.m12 + m1.m23*m2.m13,
	    m1.m20*m2.m20 + m1.m21*m2.m21 + m1.m22*m2.m22 + m1.m23*m2.m23,
	    m1.m20*m2.m30 + m1.m21*m2.m31 + m1.m22*m2.m32 + m1.m23*m2.m33,
	    
	    m1.m30*m2.m00 + m1.m31*m2.m01 + m1.m32*m2.m02 + m1.m33*m2.m03,
	    m1.m30*m2.m10 + m1.m31*m2.m11 + m1.m32*m2.m12 + m1.m33*m2.m13,
	    m1.m30*m2.m20 + m1.m31*m2.m21 + m1.m32*m2.m22 + m1.m33*m2.m23,
	    m1.m30*m2.m30 + m1.m31*m2.m31 + m1.m32*m2.m32 + m1.m33*m2.m33
	    );
    }

    
    /**
      * Multiplies the transpose of matrix m1 times matrix m2, and places the
      * result into this.
      * @param m1 The matrix on the left hand side of the multiplication
      * @param m2 The matrix on the right hand side of the multiplication
      *
    public final void mulTransposeLeft(Matrix4f m1, Matrix4f m2) {
	// alias-safe way.
	set(
	    m1.m00*m2.m00 + m1.m10*m2.m10 + m1.m20*m2.m20 + m1.m30*m2.m30,
	    m1.m00*m2.m01 + m1.m10*m2.m11 + m1.m20*m2.m21 + m1.m30*m2.m31,
	    m1.m00*m2.m02 + m1.m10*m2.m12 + m1.m20*m2.m22 + m1.m30*m2.m32,
	    m1.m00*m2.m03 + m1.m10*m2.m13 + m1.m20*m2.m23 + m1.m30*m2.m33,

	    m1.m01*m2.m00 + m1.m11*m2.m10 + m1.m21*m2.m20 + m1.m31*m2.m30,
	    m1.m01*m2.m01 + m1.m11*m2.m11 + m1.m21*m2.m21 + m1.m31*m2.m31,
	    m1.m01*m2.m02 + m1.m11*m2.m12 + m1.m21*m2.m22 + m1.m31*m2.m32,
	    m1.m01*m2.m03 + m1.m11*m2.m13 + m1.m21*m2.m23 + m1.m31*m2.m33,

	    m1.m02*m2.m00 + m1.m12*m2.m10 + m1.m22*m2.m20 + m1.m32*m2.m30,
	    m1.m02*m2.m01 + m1.m12*m2.m11 + m1.m22*m2.m21 + m1.m32*m2.m31,
	    m1.m02*m2.m02 + m1.m12*m2.m12 + m1.m22*m2.m22 + m1.m32*m2.m32,
	    m1.m02*m2.m03 + m1.m12*m2.m13 + m1.m22*m2.m23 + m1.m32*m2.m33,

	    m1.m03*m2.m00 + m1.m13*m2.m10 + m1.m23*m2.m20 + m1.m33*m2.m30,
	    m1.m03*m2.m01 + m1.m13*m2.m11 + m1.m23*m2.m21 + m1.m33*m2.m31,
	    m1.m03*m2.m02 + m1.m13*m2.m12 + m1.m23*m2.m22 + m1.m33*m2.m32,
	    m1.m03*m2.m03 + m1.m13*m2.m13 + m1.m23*m2.m23 + m1.m33*m2.m33
	    );
    }
*/

    /**
     * Returns true if all of the data members of Matrix4f m1 are
     * equal to the corresponding data members in this Matrix4f. 
     * @param m1 The matrix with which the comparison is made. 
     * @return true or false 
     */
    public boolean equals(Matrix4f m1)  {
	return  m1 != null
	        && m00 == m1.m00
		&& m01 == m1.m01
		&& m02 == m1.m02 
		&& m03 == m1.m03
		&& m10 == m1.m10
		&& m11 == m1.m11
		&& m12 == m1.m12
		&& m13 == m1.m13
		&& m20 == m1.m20
		&& m21 == m1.m21
		&& m22 == m1.m22
		&& m23 == m1.m23
		&& m30 == m1.m30
		&& m31 == m1.m31
		&& m32 == m1.m32
		&& m33 == m1.m33;
    }

    /**
      * Returns true if the Object o1 is of type Matrix4f and all of the data
      * members of t1 are equal to the corresponding data members in this
      * Matrix4f.
      * @param o1 the object with which the comparison is made.
      */
    public boolean equals(Object o1) {
	return o1 != null && (o1 instanceof Matrix4f) && equals((Matrix4f)o1);
    }

    /**
      * Returns true if the L-infinite distance between this matrix and matrix
      * m1 is less than or equal to the epsilon parameter, otherwise returns
      * false. The L-infinite distance is equal to MAX[i=0,1,2,3 ; j=0,1,2,3 ;
      * abs(this.m(i,j) - m1.m(i,j)]
      * @param m1 The matrix to be compared to this matrix
      * @param epsilon the threshold value
      */
      public boolean epsilonEquals(Matrix4f m1, float epsilon) {
	  // why epsilon is float ??
	  return  Math.abs(m00 - m1.m00) <= epsilon
		&& Math.abs(m01 - m1.m01) <= epsilon
		&& Math.abs(m02 - m1.m02 ) <= epsilon
		&& Math.abs(m03 - m1.m03) <= epsilon

		&& Math.abs(m10 - m1.m10) <= epsilon
		&& Math.abs(m11 - m1.m11) <= epsilon
		&& Math.abs(m12 - m1.m12) <= epsilon
		&& Math.abs(m13 - m1.m13) <= epsilon

		&& Math.abs(m20 - m1.m20) <= epsilon
		&& Math.abs(m21 - m1.m21) <= epsilon
		&& Math.abs(m22 - m1.m22) <= epsilon
		&& Math.abs(m23 - m1.m23) <= epsilon

		&& Math.abs(m30 - m1.m30) <= epsilon
		&& Math.abs(m31 - m1.m31) <= epsilon
		&& Math.abs(m32 - m1.m32) <= epsilon
		&& Math.abs(m33 - m1.m33) <= epsilon;
      }

    /**
     * Returns a hash number based on the data values in this
     * object.  Two different Matrix4f objects with identical data values
     * (ie, returns true for equals(Matrix4f) ) will return the same hash
     * number.  Two objects with different data members may return the
     * same hash value, although this is not likely.
     * @return the integer hash value 
     */
    public int hashCode() {
	return Float.floatToIntBits(m00) ^
	       Float.floatToIntBits(m01) ^
	       Float.floatToIntBits(m02) ^
	       Float.floatToIntBits(m03) ^
           Float.floatToIntBits(m10) ^
	       Float.floatToIntBits(m11) ^
	       Float.floatToIntBits(m12) ^
	       Float.floatToIntBits(m13) ^
	       Float.floatToIntBits(m20) ^
	       Float.floatToIntBits(m21) ^
	       Float.floatToIntBits(m22) ^
	       Float.floatToIntBits(m23) ^
	       Float.floatToIntBits(m30) ^
	       Float.floatToIntBits(m31) ^
	       Float.floatToIntBits(m32) ^
	       Float.floatToIntBits(m33);
    }

    /**
     * Transform the vector vec using this Matrix4f and place the
     * result into vecOut.
     * @param vec the single precision vector to be transformed
     * @param vecOut the vector into which the transformed values are placed
     */
    public final void transform(Tuple4f vec, Tuple4f vecOut) {
	// alias-safe
	vecOut.set(
	    m00*vec.x + m10*vec.y + m20*vec.z + m30*vec.w,
	    m01*vec.x + m11*vec.y + m21*vec.z + m31*vec.w,
	    m02*vec.x + m12*vec.y + m22*vec.z + m32*vec.w,
	    m03*vec.x + m13*vec.y + m23*vec.z + m33*vec.w
	    );
    }

    /**
     * Transform the vector vec using this Matrix4f and place the
     * result back into vec.
     * @param vec the single precision vector to be transformed
     */
    public final void transform(Tuple4f vec)  {
	transform(vec, vec);
    }

    /**
      * Transforms the point parameter with this Matrix4f and places the result
      * into pointOut. The fourth element of the point input paramter is assumed
      * to be one.
      * @param point the input point to be transformed.
      * @param pointOut the transformed point
      */
    public final void transform(Point3f point, Point3f pointOut) {
	pointOut.set(
	    m00*point.x + m10*point.y + m20*point.z + m03,
	    m01*point.x + m11*point.y + m21*point.z + m13,
	    m02*point.x + m12*point.y + m22*point.z + m23
	    );
    }


    /**
     * Transforms the point parameter with this Matrix4f and
     * places the result back into point.  The fourth element of the
     * point input paramter is assumed to be one.
     * @param point the input point to be transformed.
     */
    public final void transform(Point3f point) {
	transform(point, point);
    }

    /**
     * Transforms the normal parameter by this Matrix4f and places the value
     * into normalOut.  The fourth element of the normal is assumed to be zero.
     * @param normal the input normal to be transformed.
     * @param normalOut the transformed normal
     */
    public final void transform(Vector3f normal, Vector3f normalOut) {
	normalOut.set(
	    m00 * normal.x + m10 * normal.y + m20 * normal.z,
	    m01 * normal.x + m11 * normal.y + m21 * normal.z,
	    m02 * normal.x + m12 * normal.y + m22 * normal.z
	    );
    }

    /**
     * Transforms the normal parameter by this transform and places the value
     * back into normal.  The fourth element of the normal is assumed to be zero.
     * @param normal the input normal to be transformed.
     */
    public final void transform(Vector3f normal) {
	transform(normal, normal);
    }

    /**
      * Sets the rotational component (upper 3x3) of this matrix to the matrix
      * values in the single precision Matrix3f argument; the other elements of
      * this matrix are unchanged; a singular value decomposition is performed
      * on this object's upper 3x3 matrix to factor out the scale, then this
      * object's upper 3x3 matrix components are replaced by the passed rotation
      * components, and then the scale is reapplied to the rotational
      * components.
      * @param m1 single precision 3x3 matrix
      */
    public final void setRotation(Matrix3d m1) {
	float scale = SVD(null);
	setRotationScale(m1);
	mulRotationScale(scale);
    }

    /**
      * Sets the rotational component (upper 3x3) of this matrix to the matrix
      * values in the single precision Matrix3f argument; the other elements of
      * this matrix are unchanged; a singular value decomposition is performed
      * on this object's upper 3x3 matrix to factor out the scale, then this
      * object's upper 3x3 matrix components are replaced by the passed rotation
      * components, and then the scale is reapplied to the rotational
      * components.
      * @param m1 single precision 3x3 matrix
      */
    public final void setRotation(Matrix3f m1) {
	float scale = SVD(null);
	setRotationScale(m1);
	mulRotationScale(scale);
    }

    /**
      * Sets the rotational component (upper 3x3) of this matrix to the matrix
      * equivalent values of the quaternion argument; the other elements of this
      * matrix are unchanged; a singular value decomposition is performed on
      * this object's upper 3x3 matrix to factor out the scale, then this
      * object's upper 3x3 matrix components are replaced by the matrix
      * equivalent of the quaternion, and then the scale is reapplied to the
      * rotational components.
      * @param q1 the quaternion that specifies the rotation
      */
    public final void setRotation(Quat4f q1) {
	float scale = SVD(null, null);

	// save other values
	float tx = m03; 
	float ty = m13; 
	float tz = m23; 
	float w0 = m30;                  
	float w1 = m31;
	float w2 = m32;
	float w3 = m33;

	set(q1);
	mulRotationScale(scale);

	// set back
	m03 = tx;
	m13 = ty;
	m23 = tz;
	m30 = w0;
	m31 = w1;
	m32 = w2;
	m33 = w3;
    }

    /**
      * Sets the rotational component (upper 3x3) of this matrix to the matrix
      * equivalent values of the quaternion argument; the other elements of this
      * matrix are unchanged; a singular value decomposition is performed on
      * this object's upper 3x3 matrix to factor out the scale, then this
      * object's upper 3x3 matrix components are replaced by the matrix
      * equivalent of the quaternion, and then the scale is reapplied to the
      * rotational components.
      * @param q1 the quaternion that specifies the rotation
      */
    public final void setRotation(Quat4d q1) {
	float scale = SVD(null, null);
	// save other values
	float tx = m03; 
	float ty = m13; 
	float tz = m23; 
	float w0 = m30;                  
	float w1 = m31;
	float w2 = m32;
	float w3 = m33;

	set(q1);
	mulRotationScale(scale);

	// set back
	m03 = tx;
	m13 = ty;
	m23 = tz;
	m30 = w0;
	m31 = w1;
	m32 = w2;
	m33 = w3;
    }

    /**
      * Sets the rotational component (upper 3x3) of this matrix to the matrix
      * equivalent values of the axis-angle argument; the other elements of this
      * matrix are unchanged; a singular value decomposition is performed on
      * this object's upper 3x3 matrix to factor out the scale, then this
      * object's upper 3x3 matrix components are replaced by the matrix
      * equivalent of the axis-angle, and then the scale is reapplied to the
      * rotational components.
      * @param a1 the axis-angle to be converted (x, y, z, angle)
      */
    public final void setRotation(AxisAngle4f a1) {
	float scale = SVD(null, null);
	// save other values
	float tx = m03; 
	float ty = m13; 
	float tz = m23; 
	float w0 = m30;                  
	float w1 = m31;
	float w2 = m32;
	float w3 = m33;

	set(a1);
	mulRotationScale(scale);

	// set back
	m03 = tx;
	m13 = ty;
	m23 = tz;
	m30 = w0;
	m31 = w1;
	m32 = w2;
	m33 = w3;
    }

    /**
      * Sets this matrix to all zeros.
      */
    public final void setZero() {
        m00 = 0.0f; m01 = 0.0f; m02 = 0.0f; m03 = 0.0f;
        m10 = 0.0f; m11 = 0.0f; m12 = 0.0f; m13 = 0.0f;
        m20 = 0.0f; m21 = 0.0f; m22 = 0.0f; m23 = 0.0f;
        m30 = 0.0f; m31 = 0.0f; m32 = 0.0f; m33 = 0.0f;
    }

    /**
      * Negates the value of this matrix: this = -this.
      */
    public final void negate() {
        m00 = -m00; m01 = -m01; m02 = -m02; m03 = -m03;
        m10 = -m10; m11 = -m11; m12 = -m12; m13 = -m13;
        m20 = -m20; m21 = -m21; m22 = -m22; m23 = -m23;
        m30 = -m30; m31 = -m31; m32 = -m32; m33 = -m33;
    }

    /**
      * Sets the value of this matrix equal to the negation of of the Matrix4f
      * parameter.
      * @param m1 The source matrix
      */
    public final void negate(Matrix4f m1) {
	set(m1);
	negate();
    }

    /**
      * Sets 16 values	
      */
    private void set(float m00, float m10, float m20, float m30, 
		  float m01, float m11, float m21, float m31,
		  float m02, float m12, float m22, float m32,
		  float m03, float m13, float m23, float m33) {
	this.m00 = m00; this.m01 = m01; this.m02 = m02; this.m03 = m03;
	this.m10 = m10; this.m11 = m11; this.m12 = m12; this.m13 = m13;
	this.m20 = m20; this.m21 = m21; this.m22 = m22; this.m23 = m23;
	this.m30 = m30; this.m31 = m31; this.m32 = m32; this.m33 = m33;
    }

    /**
      * Performs SVD on this matrix and gets scale and rotation.
      * Rotation is placed into rot.
      * @param rot3 the rotation factor(Matrix3d).
      * @param rot4 the rotation factor(Matrix4f) only upper 3x3 elements are changed.
      * @return scale factor
      */
    private float SVD(Matrix3f rot3, Matrix4f rot4) {
	// this is a simple svd.
	// Not complete but fast and reasonable.
	// See comment in Matrix3d.

	float s = (float)Math.sqrt(
	    (
	     m00*m00 + m10*m10 + m20*m20 + 
	     m01*m01 + m11*m11 + m21*m21 +
	     m02*m02 + m12*m12 + m22*m22
	    )/3.0
	    );

	// zero-div may occur.
	float t = (s == 0.0f ? 0.0f : 1.0f/s);

	if (rot3 != null) {
	    this.getRotationScale(rot3);
	    rot3.mul(t);
	}

	if (rot4 != null) {
	    if (rot4 != this)
		rot4.setRotationScale(this);  // private method
	    rot4.mulRotationScale(t);         // private method
	}

	return s;
    }

    /**
      * Performs SVD on this matrix and gets the scale and the pure rotation.
      * The pure rotation is placed into rot.
      * @param rot the rotation factor.
      * @return scale factor
      */
    private float SVD(Matrix3d rot) {
	// this is a simple svd.
	// Not complete but fast and reasonable.
	// See comment in Matrix3d.

	float s = (float)Math.sqrt(
	    (
	     m00*m00 + m10*m10 + m20*m20 + 
	     m01*m01 + m11*m11 + m21*m21 +
	     m02*m02 + m12*m12 + m22*m22
	    )/3.0
	    );
	
	if (rot != null) {
		// zero-div may occur.
		float t = (s == 0.0f ? 0.0f : 1.0f/s);
	    this.getRotationScale(rot);
	    rot.mul(t);
	}

	return s;
    }

    /**
      * Multiplies 3x3 upper elements of this matrix by a scalar.
      * The other elements are unchanged.
      */
    private void mulRotationScale(float scale) {
	m00 *= scale; m01 *= scale; m02 *= scale;
	m10 *= scale; m11 *= scale; m12 *= scale;
	m20 *= scale; m21 *= scale; m22 *= scale;
    }

    /**
      * Sets only 3x3 upper elements of this matrix to that of m1.
      * The other elements are unchanged.
      */
    private void setRotationScale(Matrix4f m1) {
	m00 = m1.m00; m01 = m1.m01; m02 = m1.m02;
	m10 = m1.m10; m11 = m1.m11; m12 = m1.m12;
	m20 = m1.m20; m21 = m1.m21; m22 = m1.m22;
    }

    /**
      * Replaces the upper 3x3 matrix values of this matrix with the values in the matrix m1.
      * @param m1 The matrix that will be the new upper 3x3
      */
    private void setRotationScale(Matrix3d m1) {
	m00 = (float)m1.m00; m01 = (float)m1.m01; m02 = (float)m1.m02;
	m10 = (float)m1.m10; m11 = (float)m1.m11; m12 = (float)m1.m12;
	m20 = (float)m1.m20; m21 = (float)m1.m21; m22 = (float)m1.m22;
    }

    /**
      * Modifies the translational components of this matrix to the values of
      * the Vector3d argument; the other values of this matrix are not modified.
      * @param trans the translational component
      */
    private void setTranslation(Vector3d trans) {
		m30 = (float)trans.x;
	    m31 = (float)trans.y;  
		m32 = (float)trans.z;
    }


    /**
      * Gets the upper 3x3 values of this matrix and places them into the matrix m1.
      * @param m1 The matrix that will hold the values
      */
    private final void getRotationScale(Matrix3d m1) {
	m1.m00 = m00; m1.m01 = m01; m1.m02 = m02;
	m1.m10 = m10; m1.m11 = m11; m1.m12 = m12;
	m1.m20 = m20; m1.m21 = m21; m1.m22 = m22;
    }

    private void setFromQuat(double x, double y, double z, double w) {
		double n = x*x + y*y + z*z + w*w;
		double s = (n > 0.0) ? (2.0/n) : 0.0;
	
		double xs = x*s,  ys = y*s,  zs = z*s;
		double wx = w*xs, wy = w*ys, wz = w*zs;
		double xx = x*xs, xy = x*ys, xz = x*zs;
		double yy = y*ys, yz = y*zs, zz = z*zs;
	
		setIdentity();
		m00 = (float)(1.0 - (yy + zz));	m10 = (float)(xy - wz);         m20 = (float)(xz + wy);
		m01 = (float)(xy + wz);         m11 = (float)(1.0 - (xx + zz)); m21 = (float)(yz - wx);
		m02 = (float)(xz - wy);         m12 = (float)(yz + wx);         m22 = (float)(1.0 - (xx + yy));
    }
	
    private void setFromAxisAngle(double x, double y, double z, double angle) {
		// Taken from Rick's which is taken from Wertz. pg. 412
		// Bug Fixed and changed into right-handed by hiranabe
		double n = Math.sqrt(x*x + y*y + z*z);
		// zero-div may occur
		n = 1/n;
		x *= n;
		y *= n;
		z *= n;
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		double omc = 1.0 - c;
	
		m00 = (float)(c + x*x*omc);
		m11 = (float)(c + y*y*omc);
		m22 = (float)(c + z*z*omc);
	
		double tmp1 = x*y*omc;
		double tmp2 = z*s;
		m10 = (float)(tmp1 - tmp2);
		m01 = (float)(tmp1 + tmp2);
	
		tmp1 = x*z*omc;
		tmp2 = y*s;
		m20 = (float)(tmp1 + tmp2);
		m02 = (float)(tmp1 - tmp2);
	
		tmp1 = y*z*omc;
		tmp2 = x*s;
		m21 = (float)(tmp1 - tmp2);
		m12 = (float)(tmp1 + tmp2);
    }
/*
	/**
	 * Load from another matrix4f
	 * @param src The source matrix
	 * @return this
	 /
	public Matrix4f load(Matrix4f src) {
		return load(src, this);
	}

	/**
	 * Copy the source matrix to the destination matrix
	 * @param src The source matrix
	 * @param dest The destination matrix, or null if a new one is to be created
	 * @return The copied matrix
	 /
	public static Matrix4f load(Matrix4f src, Matrix4f dest) {
		if (dest == null)
			dest = new Matrix4f();
		dest.m00 = src.m00;
		dest.m01 = src.m01;
		dest.m02 = src.m02;
		dest.m03 = src.m03;
		dest.m10 = src.m10;
		dest.m11 = src.m11;
		dest.m12 = src.m12;
		dest.m13 = src.m13;
		dest.m20 = src.m20;
		dest.m21 = src.m21;
		dest.m22 = src.m22;
		dest.m23 = src.m23;
		dest.m30 = src.m30;
		dest.m31 = src.m31;
		dest.m32 = src.m32;
		dest.m33 = src.m33;

		return dest;
	}
*/
	/**
	 * Load from a float buffer. The buffer stores the matrix in column major
	 * (OpenGL) order.
	 *
	 * @param buf A float buffer to read from
	 * @return this
	 */
	public Matrix4f load(FloatBuffer buf) {

		m00 = buf.get();
		m01 = buf.get();
		m02 = buf.get();
		m03 = buf.get();
		m10 = buf.get();
		m11 = buf.get();
		m12 = buf.get();
		m13 = buf.get();
		m20 = buf.get();
		m21 = buf.get();
		m22 = buf.get();
		m23 = buf.get();
		m30 = buf.get();
		m31 = buf.get();
		m32 = buf.get();
		m33 = buf.get();

		return this;
	}

	/**
	 * Load from a float buffer. The buffer stores the matrix in row major
	 * (maths) order.
	 *
	 * @param buf A float buffer to read from
	 * @return this
	 */
	public Matrix4f loadTranspose(FloatBuffer buf) {

		m00 = buf.get();
		m10 = buf.get();
		m20 = buf.get();
		m30 = buf.get();
		m01 = buf.get();
		m11 = buf.get();
		m21 = buf.get();
		m31 = buf.get();
		m02 = buf.get();
		m12 = buf.get();
		m22 = buf.get();
		m32 = buf.get();
		m03 = buf.get();
		m13 = buf.get();
		m23 = buf.get();
		m33 = buf.get();

		return this;
	}

	/**
	 * Store this matrix in a float buffer. The matrix is stored in column
	 * major (openGL) order.
	 * @param buf The buffer to store this matrix in
	 */
	public void store(FloatBuffer buf) {
		buf.put(m00);
		buf.put(m01);
		buf.put(m02);
		buf.put(m03);
		buf.put(m10);
		buf.put(m11);
		buf.put(m12);
		buf.put(m13);
		buf.put(m20);
		buf.put(m21);
		buf.put(m22);
		buf.put(m23);
		buf.put(m30);
		buf.put(m31);
		buf.put(m32);
		buf.put(m33);
	}

	/**
	 * Store this matrix in a float buffer. The matrix is stored in row
	 * major (maths) order.
	 * @param buf The buffer to store this matrix in
	 */
	public void storeTranspose(FloatBuffer buf) {
		buf.put(m00);
		buf.put(m10);
		buf.put(m20);
		buf.put(m30);
		buf.put(m01);
		buf.put(m11);
		buf.put(m21);
		buf.put(m31);
		buf.put(m02);
		buf.put(m12);
		buf.put(m22);
		buf.put(m32);
		buf.put(m03);
		buf.put(m13);
		buf.put(m23);
		buf.put(m33);
	}

	/**
	 * Store the rotation portion of this matrix in a float buffer. The matrix is stored in column
	 * major (openGL) order.
	 * @param buf The buffer to store this matrix in
	 */
	public void store3f(FloatBuffer buf) {
		buf.put(m00);
		buf.put(m01);
		buf.put(m02);
		buf.put(m10);
		buf.put(m11);
		buf.put(m12);
		buf.put(m20);
		buf.put(m21);
		buf.put(m22);
	}
	
	public Vector3f translationVector() {
		Vector3f translation = new Vector3f();
		get(translation);
		return translation;
	}
}
