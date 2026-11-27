package dev.o7moon.openboatutils.sync;

// Dead reckoning for a remote boat, mirroring the per tick order of AbstractBoat:
// floatBoat (velocity and yaw rate times friction), controlBoat (yaw rate += turn input, yaw += yaw rate,
// velocity += acceleration along the new yaw) and move (collide, a blocked axis loses its velocity).
// The inputs held during the last sender tick are assumed to stay held, so the turn input and the
// acceleration are carried forward, the acceleration rotated with the boat. Vertical velocity is kept as is.
public final class SyncPredictor {
    public static final double TICK_MS = 50.0;
    public static final double MAX_AGE_MS = 500.0;
    public static final double MAX_SPEED = 100.0;
    // world limits for a state's position
    public static final double MAX_HORIZONTAL = 3.0E7;
    public static final double MAX_VERTICAL = 1.0E5;

    private static final double BLOCKED_EPSILON = 1.0E-5;

    private SyncPredictor() {}

    public interface Collider {
        // Returns the part of (dx, dy, dz) a boat placed at (x, y, z) can actually move.
        double[] collide(double x, double y, double z, double dx, double dy, double dz);
    }

    public static final class State {
        public double x, y, z;
        public double vx, vy, vz;
        public float yaw;
        public float yawRate;
        public float yawAccel;
        public float friction;
        public float ax, az;

        public State() {}

        public State(State other) {
            this.x = other.x; this.y = other.y; this.z = other.z;
            this.vx = other.vx; this.vy = other.vy; this.vz = other.vz;
            this.yaw = other.yaw;
            this.yawRate = other.yawRate;
            this.yawAccel = other.yawAccel;
            this.friction = other.friction;
            this.ax = other.ax; this.az = other.az;
        }

        // Rejects anything a well behaved client could not have sent.
        public boolean isValid() {
            return within(x, MAX_HORIZONTAL) && within(y, MAX_VERTICAL) && within(z, MAX_HORIZONTAL)
                    && Float.isFinite(yaw)
                    && within(vx, 100) && within(vy, 100) && within(vz, 100)
                    && within(ax, 10) && within(az, 10)
                    && Float.isFinite(friction) && friction >= 0 && friction <= 2
                    && within(yawRate, 1000) && within(yawAccel, 1000);
        }

        // Whether the position is within maxDistance blocks of the given one.
        public boolean isNear(double bx, double by, double bz, double maxDistance) {
            double dx = x - bx, dy = y - by, dz = z - bz;
            return dx * dx + dy * dy + dz * dz <= maxDistance * maxDistance;
        }

        public boolean isFinite() {
            return Double.isFinite(x) && Double.isFinite(y) && Double.isFinite(z)
                    && Double.isFinite(vx) && Double.isFinite(vy) && Double.isFinite(vz)
                    && Float.isFinite(yaw);
        }

        private static boolean within(double value, double limit) {
            return Double.isFinite(value) && Math.abs(value) <= limit;
        }
    }

    public static State predict(State from, double ageMs, Collider collider) {
        State state = new State(from);

        double steps = Math.max(0, Math.min(ageMs, MAX_AGE_MS)) / TICK_MS;
        int whole = (int) Math.floor(steps);
        double fraction = steps - whole;

        for (int i = 0; i < whole; i++) step(state, 1.0, collider);
        if (fraction > 1.0E-9) step(state, fraction, collider);

        return state;
    }

    private static void step(State s, double fraction, Collider collider) {
        float friction = s.friction;

        s.yawRate = s.yawRate * friction + s.yawAccel;
        float deltaYaw = s.yawRate;

        double radians = Math.toRadians(deltaYaw);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        float ax = (float) (s.ax * cos - s.az * sin);
        float az = (float) (s.ax * sin + s.az * cos);
        s.ax = ax;
        s.az = az;

        s.vx = s.vx * friction + ax;
        s.vz = s.vz * friction + az;

        double speed = Math.sqrt(s.vx * s.vx + s.vy * s.vy + s.vz * s.vz);
        if (speed > MAX_SPEED) {
            double scale = MAX_SPEED / speed;
            s.vx *= scale;
            s.vy *= scale;
            s.vz *= scale;
        }

        double dx = s.vx * fraction;
        double dy = s.vy * fraction;
        double dz = s.vz * fraction;

        double[] moved = collider == null ? new double[]{dx, dy, dz} : collider.collide(s.x, s.y, s.z, dx, dy, dz);

        if (Math.abs(moved[0] - dx) > BLOCKED_EPSILON) s.vx = 0;
        if (Math.abs(moved[1] - dy) > BLOCKED_EPSILON) s.vy = 0;
        if (Math.abs(moved[2] - dz) > BLOCKED_EPSILON) s.vz = 0;

        s.x += moved[0];
        s.y += moved[1];
        s.z += moved[2];
        s.yaw += (float) (deltaYaw * fraction);
    }
}
