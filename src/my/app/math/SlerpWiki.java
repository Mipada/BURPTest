/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.app.math;

import com.jme3.math.Vector4f;
import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 *
 * @author Peter
 */
//https://en.wikipedia.org/wiki/Slerp#Source_code
public class SlerpWiki {
Quaternion slerp(Quaternion v0, Quaternion v1, double t) {
    // Only unit quaternions are valid rotations.
    // Normalize to avoid undefined behavior.
    v0.normalizeLocal();
    v1.normalizeLocal();

    // Compute the cosine of the angle between the two vectors.
    double dot = v0.dot(v1);

    // If the dot product is negative, slerp won't take
    // the shorter path. Note that v1 and -v1 are equivalent when
    // the negation is applied to all four components. Fix by 
    // reversing one quaternion.
    if (dot < 0.0f) {
        v1.negate();
        dot = -dot;
    }  

    final double DOT_THRESHOLD = 0.9995;
    if (dot > DOT_THRESHOLD) {
        // If the inputs are too close for comfort, linearly interpolate
        // and normalize the result.

        Vector4f d = new Vector4f();
        d.x = v0.getX() - v1.getX();
        d.y = v0.getY() - v1.getY();
        d.z = v0.getZ() - v1.getZ();
        d.w = v0.getW() - v1.getW();

        Quaternion result = v0 + t*(v1 - v0);
        result.normalize();
        return result;
    }

    // Since dot is in range [0, DOT_THRESHOLD], acos is safe
    double theta_0 = acos(dot);        // theta_0 = angle between input vectors
    double theta = theta_0*t;          // theta = angle between v0 and result
    double sin_theta = sin(theta);     // compute this value only once
    double sin_theta_0 = sin(theta_0); // compute this value only once

    double s0 = cos(theta) - dot * sin_theta / sin_theta_0;  // == sin(theta_0 - theta) / sin(theta_0)
    double s1 = sin_theta / sin_theta_0;

    return (s0 * v0) + (s1 * v1);
}
    
}
