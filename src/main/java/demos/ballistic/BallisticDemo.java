package demos.ballistic;


import engine.GameEngine;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import util.Font;
import util.Shape;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class BallisticDemo extends GameEngine {

    private final Vector3f initPos = new Vector3f(0.0f, 1.5f, 0.0f);
    private AmmoRound.ShotType currentShotType = AmmoRound.ShotType.UNUSED;
    private String type = "None";
    private List<AmmoRound> ammoRounds = new ArrayList<>();
    private final int maxAmmoRounds = 20;

    @Override
    public void config() {
        tittle = "Demo:: BallisticDemo";
        width = 640;
        height = 320;
    }

    @Override
    public void init() {
        projection = new Matrix4f().setPerspective((float) Math.toRadians(60), width / height, 0.01f, 1000.0f);
        view = new Matrix4f().setLookAt(-25.0f, 8.0f, 5.0f, 0.0f, 5.0f, 22.0f, 0.0f, 1.0f, 0.0f);
        fb = BufferUtils.createFloatBuffer(16);
    }

    @Override
    public void initCallbacks() {

        glfwSetWindowCloseCallback(window, win -> running = false);

        glfwSetMouseButtonCallback(window, (win, button, action, mods) -> {
            if (action == GLFW_RELEASE)
                return;

            if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
                fire();
            }

            if (button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS) {
                if (!ammoRounds.isEmpty())
                    ammoRounds.remove(0);
            }
        });

        glfwSetKeyCallback(window, (win, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_UNKNOWN)
                return;
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                running = false;

            switch (key) {
                case GLFW_KEY_1:
                    currentShotType = AmmoRound.ShotType.PISTOL;
                    type = "Pistol";
                    break;
                case GLFW_KEY_2:
                    currentShotType = AmmoRound.ShotType.ARTILLERY;
                    type = "Artillery";
                    break;
                case GLFW_KEY_3:
                    currentShotType = AmmoRound.ShotType.FIREBALL;
                    type = "Fireball";
                    break;
                case GLFW_KEY_4:
                    currentShotType = AmmoRound.ShotType.LASER;
                    type = "Laser";
                    break;
            }
        });

        glfwSetFramebufferSizeCallback(window, (win, w, h) -> {
            width = w;
            height = h;
            glViewport(0, 0, width, height);
        });

    }

    @Override
    public void update(float delta) {

        if (delta <= 0.0f)
            return;

        List<AmmoRound> cache = new ArrayList<>();
        for (AmmoRound shot : ammoRounds) {
            shot.integrate(delta);
            shot.isRemove();
            cache.add(shot);
        }

        cache.stream().filter(shotCache -> shotCache.type == AmmoRound.ShotType.UNUSED)
                .forEach(shotCache -> ammoRounds.remove(shotCache));

        cache.clear();
    }

    @Override
    public void render() {
        glClearColor(0.9f, 0.95f, 1.0f, 1.0f);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glLoadMatrixf(projection.get(fb));

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glLoadMatrixf(view.get(fb));

        renderLines();

        glColor3f(0.25f, 0.25f, 0.25f);
        glPushMatrix();
        glTranslatef(initPos.x, initPos.y, initPos.z);
        glScaled(0.25f, 0.25f, 0.25f);
        Shape.renderSphere();
        glPopMatrix();

        //glPushMatrix();
        ammoRounds.forEach(AmmoRound::render);
        //glPopMatrix();

        Font.render(tittle, window, 10, 10, new Vector3f());
        Font.render(upsCount, window, 10, 20, new Vector3f());
        Font.render("LWJGL: " + Version.getVersion(), window, 10, 30, new Vector3f());
        Font.render(" Click: Fire\n 1-4: Select Ammo\n Current Ammo: " + type, window,
                width - 150, height - 50, new Vector3f());
    }

    private int l = -1;

    private void renderLines() {
        if (l == -1) {
            l = glGenLists(1);
            glNewList(l, GL_COMPILE);
            glBegin(GL_LINES);
            glColor3f(0.55f, 0.55f, 0.55f);

            for (int i = 0; i < 200; i += 10) {
                glVertex3f(-5.0f, 0.0f, i);
                glVertex3f(5.0f, 0.0f, i);
            }
            glEnd();
            glEndList();
        }

        glCallList(l);
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

    private void fire() {
        if (currentShotType == AmmoRound.ShotType.UNUSED)
            return;

        if (ammoRounds.size() == maxAmmoRounds)
            return;

        AmmoRound shot = null;

        switch (currentShotType) {
            case PISTOL:
                shot = AmmoRound.PISTOL();
                break;

            case ARTILLERY:
                shot = AmmoRound.ARTILLERY();
                break;

            case FIREBALL:
                shot = AmmoRound.FIREBALL();
                break;

            case LASER:
                shot = AmmoRound.LASER();
                break;
            case UNUSED:

                break;
        }

        shot.setPosition(initPos);
        shot.type = currentShotType;

        ammoRounds.add(shot);
    }

    public static void main(String[] args) {
        new BallisticDemo().run();
    }
}
