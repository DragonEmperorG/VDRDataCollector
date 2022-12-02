/******************************************************************************
 *  Compilation:  javac Quaternion.java
 *  Execution:    java Quaternion
 *
 *  Data type for quaternions.
 *
 *  http://mathworld.wolfram.com/Quaternion.html
 *
 *  The data type is "immutable" so once you create and initialize
 *  a Quaternion, you cannot change it.
 *
 *  % java Quaternion
 *
 *  https://introcs.cs.princeton.edu/java/32class/Quaternion.java
 *
 ******************************************************************************/

package cn.edu.whu.lmars.unl.util;

public class Quaternion {

    private final double q0;
    private final double q1;
    private final double q2;
    private final double q3;

    // create a new object with the given components
    public Quaternion(double q0, double x1, double q2, double q3) {
        this.q0 = q0;
        this.q1 = x1;
        this.q2 = q2;
        this.q3 = q3;
    }

    public float[] toArray() {
        float q0 = (float) this.q0;
        float q1 = (float) this.q1;
        float q2 = (float) this.q2;
        float q3 = (float) this.q3;
        return new float[] {q0, q1, q2, q3};
    }

    // return a string representation of the invoking object
    public String toString() {
        return q0 + " + " + q1 + "i + " + q2 + "j + " + q3 + "k";
    }

    // return the quaternion norm
    public double norm() {
        return Math.sqrt(q0 * q0 + q1 * q1 + q2 * q2 + q3 * q3);
    }

    // return the quaternion conjugate
    public Quaternion conjugate() {
        return new Quaternion(q0, -q1, -q2, -q3);
    }

    // return a new Quaternion whose value is (this + b)
    public Quaternion plus(Quaternion b) {
        Quaternion a = this;
        return new Quaternion(a.q0 +b.q0, a.q1 +b.q1, a.q2 +b.q2, a.q3 +b.q3);
    }


    // return a new Quaternion whose value is (this * b)
    public Quaternion times(Quaternion b) {
        Quaternion a = this;
        double y0 = a.q0 *b.q0 - a.q1 *b.q1 - a.q2 *b.q2 - a.q3 *b.q3;
        double y1 = a.q0 *b.q1 + a.q1 *b.q0 + a.q2 *b.q3 - a.q3 *b.q2;
        double y2 = a.q0 *b.q2 - a.q1 *b.q3 + a.q2 *b.q0 + a.q3 *b.q1;
        double y3 = a.q0 *b.q3 + a.q1 *b.q2 - a.q2 *b.q1 + a.q3 *b.q0;
        return new Quaternion(y0, y1, y2, y3);
    }

    // return a new Quaternion whose value is the inverse of this
    public Quaternion inverse() {
        double d = q0 * q0 + q1 * q1 + q2 * q2 + q3 * q3;
        return new Quaternion(q0 /d, -q1 /d, -q2 /d, -q3 /d);
    }


    // return a / b
    // we use the definition a * b^-1 (as opposed to b^-1 a)
    public Quaternion divides(Quaternion b) {
        Quaternion a = this;
        return a.times(b.inverse());
    }

}
