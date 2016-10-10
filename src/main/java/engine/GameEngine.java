package engine;

import org.joml.Matrix4f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by Jcs on 8/10/2016.
 */
public abstract class GameEngine {
    protected boolean running = false;
    protected long window;
    protected long monitor;
    protected int width = -1;
    protected int height = -1;
    protected String tittle = "";
    protected GLCapabilities capabilities;

    protected Matrix4f projection;
    protected Matrix4f view;
    protected FloatBuffer fb;

    public abstract void config();

    public abstract void init();

    public abstract void initCallbacks();

    public abstract void update(float delta);

    public abstract void render();

    public abstract void oneSecond(int ups, int fps);

    public abstract void finish();

    private void loop() {
        int ups = 0, fps = 0;
        glfwSetTime(0);
        double lastTime, lastTimer;

        lastTime = lastTimer = glfwGetTime();

        while (running) {
            double currentTime = glfwGetTime();
            float deltaTime = (float) (currentTime - lastTime);
            lastTime = currentTime;

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();

            ups++;
            update(deltaTime);

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            fps++;
            render();
            glfwSwapBuffers(window); // swap the color buffers

            if (glfwGetTime() - lastTimer > 1) {
                oneSecond(ups, fps);
                ups = fps = 0;
                lastTimer = glfwGetTime();
            }
        }

        glfwSetWindowShouldClose(window, true); // We will detect this in our rendering loop
        finish();
    }

    private void initGLFW() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        if (width == -1 || height == -1) {
            width = 480;
            height = 360;
        }

        // Create the window
        window = glfwCreateWindow(width, height, tittle, NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Get the resolution of the primary monitor
        monitor = glfwGetPrimaryMonitor();
        GLFWVidMode vidmode = glfwGetVideoMode(monitor);

        // Center our window
        glfwSetWindowPos(
                window,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);

        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        capabilities = GL.createCapabilities();
    }

    protected void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + " !");

        try {
            running = true;
            config();
            initGLFW();
            init();
            initCallbacks();

            loop();

            glfwDestroyWindow(window);
        } finally {
            // Terminate GLFW and free the error callback
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    }
}
