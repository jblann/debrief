/*
 *    Debrief - the Open Source Maritime Analysis Application
 *    http://debrief.info
 *
 *    (C) 2000-2014, PlanetMayo Ltd
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the Eclipse Public License v1.0
 *    (http://www.eclipse.org/legal/epl-v10.html)
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 */
// 
// <copyright>
// 
//  BBN Technologies
//  10 Moulton Street
//  Cambridge, MA 02138
//  (617) 873-8000
// 
//  Copyright (C) BBNT Solutions LLC. All rights reserved.
// 
// </copyright>
// **********************************************************************
// 
// $Source: i:/mwc/coag/asset/cvsroot/util/MWC/GUI/S57/support/MoreMath.java,v $
// $RCSfile: MoreMath.java,v $
// $Revision: 1.1 $
// $Date: 2007/04/27 09:20:01 $
// $Author: ian.mayo $
// 
// **********************************************************************

package MWC.GUI.S57.support;

/**
 * MoreMath provides functions that are not part of the standard Math
 * class.
 * <p>
 * 
 * <pre>
 * 
 *  Functions:
 *       asinh(float x) - hyperbolic arcsine
 *       sinh(float x) - hyperbolic sine
 * 
 *  Need to Implement:
 *  Function                Definition                              
 *  Hyperbolic cosine       (e&circ;x+e&circ;-x)/2                            
 *  Hyperbolic tangent      (e&circ;x-e&circ;-x)/(e&circ;x+e&circ;-x)                   
 *  Hyperbolic arc cosine   2 log  (sqrt((x+1)/2) + sqrt((x-1)/2))  
 *  Hyperbolic arc tangent  (log  (1+x) - log (1-x))/2
 *  
 * </pre>
 */
//CS-IGNORE:ON FINAL_CLASS
//CS-IGNORE:ON FINAL_PARAMETERS
//CS-IGNORE:ON MAGIC_NUMBER
//CS-IGNORE:ON UNNECESSARY_PARENTHESES
public class MoreMath {

    /**
     * 2*Math.PI
     */
    final public static transient float TWO_PI = (float) Math.PI * 2.0f;

    /**
     * 2*Math.PI
     */
    final public static transient double TWO_PI_D = Math.PI * 2.0d;

    /**
     * Math.PI/2
     */
    final public static transient float HALF_PI = (float) Math.PI / 2.0f;

    /**
     * Math.PI/2
     */
    final public static transient double HALF_PI_D = Math.PI / 2.0d;

    // cannot construct
    private MoreMath() {}

    /**
     * Checks if a ~= b. Use this to test equality of floating point
     * numbers.
     * <p>
     * 
     * @param a double
     * @param b double
     * @param epsilon the allowable error
     * @return boolean
     */
    final public static boolean approximately_equal(final double a, final double b,
                                                    final double epsilon) {
        return (Math.abs(a - b) <= epsilon);
    }

    /**
     * Checks if a ~= b. Use this to test equality of floating point
     * numbers.
     * <p>
     * 
     * @param a float
     * @param b float
     * @param epsilon the allowable error
     * @return boolean
     */
    final public static boolean approximately_equal(final float a, final float b,
                                                    final float epsilon) {
        return (Math.abs(a - b) <= epsilon);
    }

    /**
     * Hyperbolic arcsin.
     * <p>
     * Hyperbolic arc sine: log (x+sqrt(1+x^2))
     * 
     * @param x float
     * @return float asinh(x)
     */
    public static final float asinh(final float x) {
        return (float) Math.log(x + Math.sqrt(x * x + 1));
    }

    /**
     * Hyperbolic arcsin.
     * <p>
     * Hyperbolic arc sine: log (x+sqrt(1+x^2))
     * 
     * @param x double
     * @return double asinh(x)
     */
    public static final double asinh(final double x) {
        return (double) Math.log(x + Math.sqrt(x * x + 1));
    }

    /**
     * Hyperbolic sin.
     * <p>
     * Hyperbolic sine: (e^x-e^-x)/2
     * 
     * @param x float
     * @return float sinh(x)
     */
    public static final float sinh(final float x) {
        return (float) (Math.pow(Math.E, x) - Math.pow(Math.E, -x)) / 2.0f;
    }

    /**
     * Hyperbolic sin.
     * <p>
     * Hyperbolic sine: (e^x-e^-x)/2
     * 
     * @param x double
     * @return double sinh(x)
     */
    public static final double sinh(final double x) {
        return (double) (Math.pow(Math.E, x) - Math.pow(Math.E, -x)) / 2.0d;
    }

    // HACK - are there functions that already exist?
    /**
     * Return sign of number.
     * 
     * @param x short
     * @return int sign -1, 1
     */
    public static final int sign(final short x) {
        return (x < 0) ? -1 : 1;
    }

    /**
     * Return sign of number.
     * 
     * @param x int
     * @return int sign -1, 1
     */
    public static final int sign(final int x) {
        return (x < 0) ? -1 : 1;
    }

    /**
     * Return sign of number.
     * 
     * @param x long
     * @return int sign -1, 1
     */
    public static final int sign(final long x) {
        return (x < 0) ? -1 : 1;
    }

    /**
     * Return sign of number.
     * 
     * @param x float
     * @return int sign -1, 1
     */
    public static final int sign(final float x) {
        return (x < 0f) ? -1 : 1;
    }

    /**
     * Return sign of number.
     * 
     * @param x double
     * @return int sign -1, 1
     */
    public static final int sign(final double x) {
        return (x < 0d) ? -1 : 1;
    }

    /**
     * Check if number is odd.
     * 
     * @param x short
     * @return boolean
     */
    public static final boolean odd(final short x) {
        return !even(x);
    }

    /**
     * Check if number is odd.
     * 
     * @param x int
     * @return boolean
     */
    public static final boolean odd(final int x) {
        return !even(x);
    }

    /**
     * Check if number is odd.
     * 
     * @param x long
     * @return boolean
     */
    public static final boolean odd(final long x) {
        return !even(x);
    }

    /**
     * Check if number is even.
     * 
     * @param x short
     * @return boolean
     */
    public static final boolean even(final short x) {
        return ((x & 0x1) == 0);
    }

    /**
     * Check if number is even.
     * 
     * @param x int
     * @return boolean
     */
    public static final boolean even(final int x) {
        return ((x & 0x1) == 0);
    }

    /**
     * Check if number is even.
     * 
     * @param x long
     * @return boolean
     */
    public static final boolean even(final long x) {
        return ((x & 0x1) == 0);
    }

    /**
     * Converts a byte in the range of -128 to 127 to an int in the
     * range 0 - 255.
     * 
     * @param b (-128 &lt;= b &lt;= 127)
     * @return int (0 &lt;= b &lt;= 255)
     */
    public static final int signedToInt(final byte b) {
        return ((int) b & 0xff);
    }

    /**
     * Converts a short in the range of -32768 to 32767 to an int in
     * the range 0 - 65535.
     * 
     * @param w (-32768 &lt;= b &lt;= 32767)
     * @return int (0 &lt;= b &lt;= 65535)
     */
    public static final int signedToInt(final short w) {
        return ((int) w & 0xffff);
    }

    /**
     * Convert an int in the range of -2147483648 to 2147483647 to a
     * long in the range 0 to 4294967295.
     * 
     * @param x (-2147483648 &lt;= x &lt;= 2147483647)
     * @return long (0 &lt;= x &lt;= 4294967295)
     */
    public static final long signedToLong(final int x) {
        return ((long) x & 0xFFFFFFFFL);
    }

    /**
     * Converts an int in the range of 0 - 65535 to an int in the
     * range of 0 - 255.
     * 
     * @param w int (0 &lt;= w &lt;= 65535)
     * @return int (0 &lt;= w &lt;= 255)
     */
    public static final int wordToByte(final int w) {
        return w >> 8;
    }

    /**
     * Build short out of bytes (in big endian order).
     * 
     * @param bytevec bytes
     * @param offset byte offset
     * @return short
     */
    public static final short BuildShortBE(final byte bytevec[], final int offset) {
        return (short) (((int) (bytevec[0 + offset]) << 8) | (signedToInt(bytevec[1 + offset])));
    }

    /**
     * Build short out of bytes (in little endian order).
     * 
     * @param bytevec bytes
     * @param offset byte offset
     * @return short
     */
    public static final short BuildShortLE(final byte bytevec[], final int offset) {
        return (short) (((int) (bytevec[1 + offset]) << 8) | (signedToInt(bytevec[0 + offset])));
    }

    /**
     * Build short out of bytes.
     * 
     * @param bytevec bytes
     * @param offset byte offset
     * @param MSBFirst BE or LE?
     * @return short
     */
    public static final short BuildShort(final byte bytevec[], final int offset,
                                         final boolean MSBFirst) {
        if (MSBFirst) {
            return (BuildShortBE(bytevec, offset));
        } else {
            return (BuildShortLE(bytevec, offset));
        }
    }

    /**
     * Build short out of bytes (in big endian order).
     * 
     * @param bytevec bytes
     * @param MSBFirst BE or LE?
     * @return short
     */

    public static final short BuildShortBE(final byte bytevec[], final boolean MSBFirst) {
        return BuildShortBE(bytevec, 0);
    }

    /**
     * Build short out of bytes (in little endian order).
     * 
     * @param bytevec bytes
     * @param MSBFirst BE or LE?
     * @return short
     */
    public static final short BuildShortLE(final byte bytevec[], final boolean MSBFirst) {
        return BuildShortLE(bytevec, 0);
    }

    /**
     * Build short out of bytes.
     * 
     * @param bytevec bytes
     * @param MSBFirst BE or LE?
     * @return short
     */
    public static final short BuildShort(final byte bytevec[], final boolean MSBFirst) {
        return BuildShort(bytevec, 0, MSBFirst);
    }

    /**
     * Build int out of bytes (in big endian order).
     * 
     * @param bytevec bytes
     * @param offset byte offset
     * @return int
     */
    public static final int BuildIntegerBE(final byte bytevec[], final int offset) {
        return (((int) (bytevec[0 + offset]) << 24)
                | (signedToInt(bytevec[1 + offset]) << 16)
                | (signedToInt(bytevec[2 + offset]) << 8) | (signedToInt(bytevec[3 + offset])));
    }

    /**
     * Build int out of bytes (in little endian order).
     * 
     * @param bytevec bytes
     * @param offset byte offset
     * @return int
     */
    public static final int BuildIntegerLE(final byte bytevec[], final int offset) {
        return (((int) (bytevec[3 + offset]) << 24)
                | (signedToInt(bytevec[2 + offset]) << 16)
                | (signedToInt(bytevec[1 + offset]) << 8) | (signedToInt(bytevec[0 + offset])));
    }

    /**
     * Build int out of bytes.
     * 
     * @param bytevec bytes
     * @param offset byte offset
     * @param MSBFirst BE or LE?
     * @return int
     */
    public static final int BuildInteger(final byte bytevec[], final int offset,
                                         final boolean MSBFirst) {
        if (MSBFirst)
            return BuildIntegerBE(bytevec, offset);
        else
            return BuildIntegerLE(bytevec, offset);
    }

    /**
     * Build int out of bytes (in big endian order).
     * 
     * @param bytevec bytes
     * @return int
     */
    public static final int BuildIntegerBE(final byte bytevec[]) {
        return BuildIntegerBE(bytevec, 0);
    }

    /**
     * Build int out of bytes (in little endian order).
     * 
     * @param bytevec bytes
     * @return int
     */
    public static final int BuildIntegerLE(final byte bytevec[]) {
        return BuildIntegerLE(bytevec, 0);
    }

    /**
     * Build int out of bytes.
     * 
     * @param bytevec bytes
     * @param MSBFirst BE or LE?
     * @return int
     */
    public static final int BuildInteger(final byte bytevec[], final boolean MSBFirst) {
        if (MSBFirst)
            return BuildIntegerBE(bytevec, 0);
        else
            return BuildIntegerLE(bytevec, 0);
    }

    /**
     * Build long out of bytes (in big endian order).
     * 
     * @param bytevec bytes
     * @param offset byte offset
     * @return long
     */
    public static final long BuildLongBE(final byte bytevec[], final int offset) {
        return (((long) signedToInt(bytevec[0 + offset]) << 56)
                | ((long) signedToInt(bytevec[1 + offset]) << 48)
                | ((long) signedToInt(bytevec[2 + offset]) << 40)
                | ((long) signedToInt(bytevec[3 + offset]) << 32)
                | ((long) signedToInt(bytevec[4 + offset]) << 24)
                | ((long) signedToInt(bytevec[5 + offset]) << 16)
                | ((long) signedToInt(bytevec[6 + offset]) << 8) | ((long) signedToInt(bytevec[7 + offset])));
    }

    /**
     * Build long out of bytes (in little endian order).
     * 
     * @param bytevec bytes
     * @param offset byte offset
     * @return long
     */
    public static final long BuildLongLE(final byte bytevec[], final int offset) {
        return (((long) signedToInt(bytevec[7 + offset]) << 56)
                | ((long) signedToInt(bytevec[6 + offset]) << 48)
                | ((long) signedToInt(bytevec[5 + offset]) << 40)
                | ((long) signedToInt(bytevec[4 + offset]) << 32)
                | ((long) signedToInt(bytevec[3 + offset]) << 24)
                | ((long) signedToInt(bytevec[2 + offset]) << 16)
                | ((long) signedToInt(bytevec[1 + offset]) << 8) | ((long) signedToInt(bytevec[0 + offset])));
    }

    /**
     * Build long out of bytes.
     * 
     * @param bytevec bytes
     * @param offset byte offset
     * @param MSBFirst BE or LE?
     * @return long
     */
    public static final long BuildLong(final byte bytevec[], final int offset,
                                       final boolean MSBFirst) {
        if (MSBFirst)
            return BuildLongBE(bytevec, offset);
        else
            return BuildLongLE(bytevec, offset);
    }

    /**
     * Build long out of bytes (in big endian order).
     * 
     * @param bytevec bytes
     * @return long
     */
    public static final long BuildLongBE(final byte bytevec[]) {
        return BuildLongBE(bytevec, 0);
    }

    /**
     * Build long out of bytes (in little endian order).
     * 
     * @param bytevec bytes
     * @return long
     */
    public static final long BuildLongLE(final byte bytevec[]) {
        return BuildLongLE(bytevec, 0);
    }

    /**
     * Build long out of bytes.
     * 
     * @param bytevec bytes
     * @param MSBFirst BE or LE?
     * @return long
     */
    public static final long BuildLong(final byte bytevec[], final boolean MSBFirst) {
        if (MSBFirst)
            return BuildLongBE(bytevec, 0);
        else
            return BuildLongLE(bytevec, 0);
    }

    /*
     * public static final void main(String[] args) { byte[] b = new
     * byte[4]; b[0] = (byte)0xff; b[1] = (byte)0x7f;
     * com.bbn.openmap.util.Debug.output("32767="+BuildShortLE(b, 0));
     * b[0] = (byte)0x7f; b[1] = (byte)0xff;
     * com.bbn.openmap.util.Debug.output("32767="+BuildShortBE(b, 0));
     * b[1] = (byte)0xff; b[2] = (byte)0xff; b[3] = (byte)0xff;
     * com.bbn.openmap.util.Debug.output("2147483647="+BuildIntegerBE(b,
     * 0));
     * com.bbn.openmap.util.Debug.output("maxuint="+signedToLong(0xffffffff)); }
     */
}
//CS-IGNORE:OFF FINAL_CLASS
//CS-IGNORE:OFF FINAL_PARAMETERS
//CS-IGNORE:OFF MAGIC_NUMBER
//CS-IGNORE:OFF UNNECESSARY_PARENTHESES
