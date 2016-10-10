package util;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.nglfwGetFramebufferSize;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBEasyFont.stb_easy_font_print;
import static org.lwjgl.system.MemoryUtil.memAddress;

/**
 * Created by Jcs on 8/10/2016.
 */
public class Font {

    public static void render(String text, long window, int x, int y, Vector3f color) {
        ByteBuffer charBuffer = BufferUtils.createByteBuffer(text.length() * 270);
        int quads = stb_easy_font_print(0, 0, text, null, charBuffer);

        IntBuffer framebufferSize = BufferUtils.createIntBuffer(2);
        nglfwGetFramebufferSize(window, memAddress(framebufferSize), memAddress(framebufferSize) + 4);
        int width = framebufferSize.get(0);
        int height = framebufferSize.get(1);

        glDisable(GL_CULL_FACE);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0.0, width, height, 0.0, -1.0, 1.0);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glEnableClientState(GL_VERTEX_ARRAY);
        glVertexPointer(2, GL_FLOAT, 16, charBuffer);

        glColor3f(color.x, color.y, color.z);
        glPushMatrix();
        glTranslated(x, y, 0.0f);
        glDrawArrays(GL_QUADS, 0, quads * 4);
        glPopMatrix();

        charBuffer.clear();
        glDisableClientState(GL_VERTEX_ARRAY);
        glEnable(GL_CULL_FACE);
    }

    public static void render(String text, long window, int x, int y) {
        render(text, window, x, y, new Vector3f(1f));
    }

}
