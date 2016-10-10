package demos.firework;

import engine.GameEngine;
import org.jcs.jfpe.Firework;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import util.Font;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class FireworkDemo extends GameEngine {

    private final static int ruleCount = 9;
    private final static int maxFireworks = 1024 * 2;
    private Firework[] fireworks = new Firework[maxFireworks];
    private FireworkRule[] rules = new FireworkRule[ruleCount];

    @Override
    public void config() {
        tittle = "Demo:: FireworkDemo";
        width = 640;
        height = 320;
    }

    @Override
    public void init() {
        projection = new Matrix4f().setPerspective((float) Math.toRadians(60), width / height, 0.01f, 1000.0f);
        view = new Matrix4f().setLookAt(0.0f, 4.0f, 10.0f, 0.0f, 4.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        fb = BufferUtils.createFloatBuffer(16);

        for (int i = 0; i < fireworks.length; i++) {
            fireworks[i] = new Firework();
            fireworks[i].setType(0);
        }

        initFireworkRules();
    }

    @Override
    public void initCallbacks() {

        glfwSetWindowCloseCallback(window, win -> running = false);

        glfwSetFramebufferSizeCallback(window, (win, w, h) -> {
            width = w;
            height = h;
            glViewport(0, 0, width, height);
        });

        glfwSetKeyCallback(window, (win, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_UNKNOWN)
                return;
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                running = false;

            if (action == GLFW_PRESS || action == GLFW_REPEAT) {
                switch (key) {
                    case GLFW_KEY_1:
                        create(1, 1, null);
                        break;
                    case GLFW_KEY_2:
                        create(2, 1, null);
                        break;
                    case GLFW_KEY_3:
                        create(3, 1, null);
                        break;
                    case GLFW_KEY_4:
                        create(4, 1, null);
                        break;
                    case GLFW_KEY_5:
                        create(5, 1, null);
                        break;
                    case GLFW_KEY_6:
                        create(6, 1, null);
                        break;
                    case GLFW_KEY_7:
                        create(7, 1, null);
                        break;
                    case GLFW_KEY_8:
                        create(8, 1, null);
                        break;
                    case GLFW_KEY_9:
                        create(9, 1, null);
                        break;
                }
            }

        });
    }

    @Override
    public void update(float delta) {

        if (delta <= 0.0f)
            return;

        for (Firework firework : fireworks)
            if (firework.getType() != 0)
                if (!firework.update(delta)) {

                    FireworkRule rule = rules[firework.getType() - 1];
                    firework.setType(0);

                    for (int j = 0; j < rule.payloadCount; j++) {
                        FireworkRule.Payload payload = rule.payloads[j];
                        create(payload.type, payload.count, firework);
                    }
                }
    }

    @Override
    public void render() {
        glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glLoadMatrixf(projection.get(fb));

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glLoadMatrixf(view.get(fb));

        int noFireWorks = 0;
        for (Firework firework : fireworks)
            if (firework.getType() != 0) {
                FireworkRule.renderFireWork(firework);
                noFireWorks++;
            }


        Font.render(tittle, window, 10, 5);
        Font.render(upsCount, window, 10, 15);
        Font.render("num FireWorks: " + noFireWorks, window, 10, 25);
        Font.render("LWJGL: " + Version.getVersion(), window, 10, 35);

        Font.render("1-9: Select Rule", window,
                width - 100, height - 30);
    }

    private String upsCount = "";

    @Override
    public void oneSecond(int ups, int fps) {
        upsCount = "ups: " + ups + ", fps: " + fps;
    }

    @Override
    public void finish() {
        glfwFreeCallbacks(window);
    }

    private int nextFirework = 0;

    private void create(int type, Firework parent) {
        // Get the rule needed to create this firework
        FireworkRule rule = rules[type - 1];

        // Create the firework
        fireworks[nextFirework] = rule.create(parent);

        // Increment the index for the next firework
        nextFirework = (++nextFirework) % maxFireworks;
    }

    private void create(int type, int number, Firework parent) {
        for (int i = 0; i < number; i++) {
            create(type, parent);
        }
    }

    private void initFireworkRules() {
        rules[0] = new FireworkRule();
        rules[0].init(2);
        rules[0].setParameters(1, // type
                0.5f, 1.4f, // age range
                new Vector3f(-7f, 15f, -7f), // min velocity
                new Vector3f(7f, 20f, 7f), // max velocity
                0.1f // damping
        );
        rules[0].payloads[0].set(3, 5);
        rules[0].payloads[1].set(5, 5);

        rules[1] = new FireworkRule();
        rules[1].init(1);
        rules[1].setParameters(2, // type
                0.5f, 1.0f, // age range
                new Vector3f(-5f, 10f, -5f), // min velocity
                new Vector3f(5f, 15f, 5f), // max velocity
                0.8f // damping
        );
        rules[1].payloads[0].set(4, 2);

        rules[2] = new FireworkRule();
        rules[2].init(1);
        rules[2].setParameters(3, // type
                0.5f, 1.5f, // age range
                new Vector3f(-5f, 0f, -5f), // min velocity
                new Vector3f(5f, 10f, 5f), // max velocity
                0.1f // damping
        );
        rules[2].payloads[0].set(8, 5);

        rules[3] = new FireworkRule();
        rules[3].init(0);
        rules[3].setParameters(4, // type
                0.25f, 0.5f, // age range
                new Vector3f(-20f, 5f, -20f), // min velocity
                new Vector3f(20f, 5f, 20f), // max velocity
                0.2f // damping
        );

        rules[4] = new FireworkRule();
        rules[4].init(1);
        rules[4].setParameters(5, // type
                0.5f, 1.0f, // age range
                new Vector3f(-20f, 2f, -20f), // min velocity
                new Vector3f(20f, 18f, 20f), // max velocity
                0.01f // damping
        );
        rules[4].payloads[0].set(3, 5);

        rules[5] = new FireworkRule();
        rules[5].init(0);
        rules[5].setParameters(6, // type
                3, 5, // age range
                new Vector3f(-5f, 5f, -5f), // min velocity
                new Vector3f(5f, 10f, 5f), // max velocity
                0.95f // damping
        );

        rules[6] = new FireworkRule();
        rules[6].init(1);
        rules[6].setParameters(7, // type
                0.5f, 2.0f, // age range
                new Vector3f(-5f, 30f, -5f), // min velocity
                new Vector3f(5f, 40f, 5f), // max velocity
                0.01f // damping
        );
        rules[6].payloads[0].set(8, 10);

        rules[7] = new FireworkRule();
        rules[7].init(0);
        rules[7].setParameters(8, // type
                0.25f, 0.9f, // age range
                new Vector3f(-2f, 5f, -2f), // min velocity
                new Vector3f(2f, 10f, 2f), // max velocity
                0.05f // damping
        );

        rules[8] = new FireworkRule();
        rules[8].init(0);
        rules[8].setParameters(9, // type
                3f, 5f, // age range
                new Vector3f(-15f, -15f, -5f), // min velocity
                new Vector3f(15f, 15f, 5f), // max velocity
                0.95f // damping
        );
        // ... and so on for other firework types ...
    }

    public static void main(String[] args) {
        new FireworkDemo().run();
    }
}
