package util;

import org.lwjgl.util.par.ParShapesMesh;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.par.ParShapes.par_shapes_create_subdivided_sphere;

public class Shape {

    private static FloatBuffer fbSphere;
    private static ShortBuffer sbSphere;

    static {
        ParShapesMesh shapeMesh = par_shapes_create_subdivided_sphere(1);
        int vc = shapeMesh.npoints();
        fbSphere = shapeMesh.points(vc * 3);
        int tc = shapeMesh.ntriangles();
        sbSphere = shapeMesh.triangles(tc * 3);
    }

    public static void renderSphere() {
        glEnableClientState(GL_VERTEX_ARRAY);
        glVertexPointer(3, GL_FLOAT, 0, fbSphere);
        glDrawElements(GL_TRIANGLES, sbSphere);
        glDisableClientState(GL_VERTEX_ARRAY);
    }

}
