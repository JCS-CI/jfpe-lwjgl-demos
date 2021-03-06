package demos.firework;

import org.jcs.jfpe.Firework;
import org.joml.Vector3f;

import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

public class FireworkRule {

    public static final Vector3f GRAVITY = new Vector3f(0f, -9.81f, 0f);
    public static final Random random = new Random();

    int type;
    float minAge;
    float maxAge;
    Vector3f minVelocity;
    Vector3f maxVelocity;
    float damping;

    class Payload {

        int type;
        int count;

        void set(int type, int count) {
            this.type = type;
            this.count = count;
        }
    }


    int payloadCount;
    Payload[] payloads;

    void init(int payloadCount) {
        this.payloadCount = payloadCount;
        this.payloads = new Payload[payloadCount];

        for (int i = 0; i < payloads.length; i++) {
            payloads[i] = new Payload();
        }

    }

    void setParameters(int type, float minAge, float maxAge, Vector3f minVelocity, Vector3f maxVelocity,
                       float damping) {
        this.type = type;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.minVelocity = minVelocity;
        this.maxVelocity = maxVelocity;
        this.damping = damping;
    }

    public Firework create(Firework parent) {
        Firework firework = new Firework();
        firework.setType(type);
        firework.setAge((maxAge - minAge) * random.nextFloat() + minAge);

        Vector3f vel;
        if (parent != null) {
            firework.getPosition().set(parent.getPosition());
            vel = new Vector3f(parent.getVelocity());
            float xx = (maxVelocity.x - minVelocity.x) * random.nextFloat() + minVelocity.x;
            float yy = (maxVelocity.y - minVelocity.y) * random.nextFloat() + minVelocity.y;
            float zz = (maxVelocity.z - minVelocity.z) * random.nextFloat() + minVelocity.z;
            vel.add(xx, yy, zz);
        } else {
            Vector3f start = new Vector3f();
            int x = random.nextInt(5) - 2;
            start.x = 5.0f * x;
            firework.getPosition().set(start);

            float xx = (maxVelocity.x - minVelocity.x) * random.nextFloat() + minVelocity.x;
            float yy = (maxVelocity.y - minVelocity.y) * random.nextFloat() + minVelocity.y;
            float zz = (maxVelocity.z - minVelocity.z) * random.nextFloat() + minVelocity.z;
            vel = new Vector3f(xx, yy, zz);
        }

        firework.getVelocity().set(vel);
        firework.setMass(1);
        firework.setDamping(damping);
        firework.getAcceleration().set(GRAVITY);
        firework.clearAccumulator();

        return firework;
    }

    public static void renderFireWork(Firework firework) {
        glBegin(GL_QUADS);
        if (firework.getType() > 0) {
            switch (firework.getType()) {
                case 1:
                    glColor3f(1, 0, 0);
                    break;
                case 2:
                    glColor3f(1, 0.5f, 0);
                    break;
                case 3:
                    glColor3f(1, 1, 0);
                    break;
                case 4:
                    glColor3f(0, 1, 0);
                    break;
                case 5:
                    glColor3f(0, 1, 1);
                    break;
                case 6:
                    glColor3f(0.4f, 0.4f, 1);
                    break;
                case 7:
                    glColor3f(1, 0, 1);
                    break;
                case 8:
                    glColor3f(1, 1, 1);
                    break;
                case 9:
                    glColor3f(1, 0.5f, 0.5f);
                    break;
            }
            float size = 0.1f;
            Vector3f pos = firework.getPosition();
            glVertex3f(pos.x - size, pos.y - size, pos.z);
            glVertex3f(pos.x + size, pos.y - size, pos.z);
            glVertex3f(pos.x + size, pos.y + size, pos.z);
            glVertex3f(pos.x - size, pos.y + size, pos.z);

            // Render the demos.ballistic.firework's reflection
            glVertex3f(pos.x - size, -pos.y - size, pos.z);
            glVertex3f(pos.x + size, -pos.y - size, pos.z);
            glVertex3f(pos.x + size, -pos.y + size, pos.z);
            glVertex3f(pos.x - size, -pos.y + size, pos.z);
        }
        glEnd();
    }
}
