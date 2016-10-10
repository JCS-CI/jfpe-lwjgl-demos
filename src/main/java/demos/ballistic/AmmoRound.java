package demos.ballistic;

import org.jcs.jfpe.Particle;
import org.joml.Vector3f;
import util.Shape;

import static org.lwjgl.opengl.GL11.*;

public class AmmoRound extends Particle {
    private Vector3f pos = getPosition();

    public enum ShotType {
        UNUSED, PISTOL, ARTILLERY, FIREBALL, LASER
    }

    public ShotType type;

    public void render() {
        glPushMatrix();
        glTranslatef(pos.x, pos.y, pos.z);
        renderType();
        Shape.renderSphere();
        glPopMatrix();
    }

    public void isRemove() {
        if (getPosition().y >= 35 ||
                getPosition().y <= 0 ||
                getPosition().z > 200.0f)
            type = ShotType.UNUSED;
    }

    private void renderType() {

        switch (type) {
            case PISTOL:
                glColor3f(0.28f, 0.70f, 0.07f);
                glScaled(0.25f, 0.25f, 0.25f);
                break;

            case ARTILLERY:
                glColor3f(0.27f, 0.06f, 0.06f);
                glScaled(0.5f, 0.5f, 0.5f);
                break;

            case FIREBALL:
                glColor3f(1.0f, 0.50f, 0.25f);
                glScaled(0.35f, 0.35f, 0.35f);
                break;

            case LASER:
                glColor3f(0.96f, 0.53f, 0.13f);
                glScaled(0.2f, 0.2f, 0.2f);
                break;

            case UNUSED:

                break;
        }
    }



    public static AmmoRound PISTOL() {
        AmmoRound shot = new AmmoRound();

        shot.setMass(2.0f); // 2.0kg
        shot.setVelocity(0.0f, 0.0f, 35.0f); // 35m/s
        shot.setAcceleration(0.0f, -1.0f, 0.0f);
        shot.setDamping(0.99f);

        return shot;
    }


    public static AmmoRound ARTILLERY() {
        AmmoRound shot = new AmmoRound();

        shot.setMass(200.0f); // 200.0kg
        shot.setVelocity(0.0f, 30.0f, 40.0f); // 50m/s
        shot.setAcceleration(0.0f, -20.0f, 0.0f);
        shot.setDamping(0.99f);

        return shot;
    }

    public static AmmoRound FIREBALL() {
        AmmoRound shot = new AmmoRound();

        shot.setMass(1.0f); // 1.0kg - mostly blast damage
        shot.setVelocity(0.0f, 0.0f, 10.0f); // 5m/s
        shot.setAcceleration(0.0f, 0.6f, 0.0f); // Floats up
        shot.setDamping(0.9f);

        return shot;
    }

    public static AmmoRound LASER() {
        AmmoRound shot = new AmmoRound();

        // Note that this is the kind of laser bolt seen in films,
        // not a realistic laser beam!
        shot.setMass(0.0001f); // 0.1kg - almost no weight
        shot.setVelocity(0.0f, 0.0f, 100.0f); // 100m/s
        shot.setAcceleration(0.0f, 0.0f, 0.0f); // No gravity
        shot.setDamping(0.99f);

        return shot;
    }
}
