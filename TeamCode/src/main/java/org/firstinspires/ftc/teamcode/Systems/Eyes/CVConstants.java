package org.firstinspires.ftc.teamcode.Systems.Eyes;

import com.pedropathing.math.Matrix;
public class CVConstants {
    public static final double CAMERA_HEIGHT = 19.3;
    public static final double CAMERA_PITCH = Math.toRadians(25.0);
    public static final double FORWARD_OFFSET = 6.5;

    public static final Matrix xyzConverter = new Matrix(
            new double[][] {
                    {1, 0, 0},
                    {0, 0, 1},
                    {0, 1, 0},
            }
    );

    public static Matrix createRotationX(double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        return new Matrix(
                new double[][] {
                        {1, 0,  0},
                        {0, c, -s},
                        {0, s,  c},
                }
        );
    }
}