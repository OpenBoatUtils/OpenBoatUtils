package dev.o7moon.openboatutils.sync;

// Everything known about one remote boat: its clock estimator, the newest state and the correction
// residual. Only used from the client thread.
public class RemoteBoatTrack {
    public static final long MAX_STATE_AGE_NANOS = 1_000_000_000L;
    // a little negative age is estimator noise, a lot means the estimate is wrong
    public static final long MIN_STATE_AGE_NANOS = -250_000_000L;
    // a jump between two states farther than this beyond what the speeds explain is a teleport
    public static final double TELEPORT_MARGIN = 8.0;
    // a correction larger than this is not blended, the boat goes straight to the new position
    public static final double MAX_BLEND = 32.0;

    private final SyncAgeEstimator estimator = new SyncAgeEstimator();

    private SyncPredictor.State latest;
    private long latestStamp;
    private long lastReceiveNanos;

    private SyncPredictor.State used;
    private long usedStamp;
    // the previous update's predicted state before the residual was added, and the age it was predicted to
    private SyncPredictor.State previous;
    private double previousAgeMs;
    private double residualX, residualY, residualZ;
    private float residualYaw;

    public void onState(long stampNanos, long receiveNanos, float senderHalfRttMs, SyncPredictor.State state) {
        if (!state.isValid()) return;

        addSample(stampNanos, receiveNanos, senderHalfRttMs);

        // packets can arrive out of order, never go back to an older state
        if (latest == null || stampNanos - latestStamp > 0) {
            latest = state;
            latestStamp = stampNanos;
        }
    }

    public void onProbe(long stampNanos, long receiveNanos, float senderHalfRttMs) {
        addSample(stampNanos, receiveNanos, senderHalfRttMs);
    }

    private void addSample(long stampNanos, long receiveNanos, float senderHalfRttMs) {
        lastReceiveNanos = receiveNanos;

        if (estimator.addSample(stampNanos, receiveNanos, senderHalfRttMs)) {
            // the sender's clock jumped (restart or a new driver), nothing from the old clock is comparable
            latest = null;
            reset();
        }
    }

    public long getLastReceiveNanos() {
        return lastReceiveNanos;
    }

    public long ageNanos(long stampNanos, long nowNanos, float myHalfRttMs) {
        return estimator.ageNanos(stampNanos, nowNanos, myHalfRttMs);
    }

    public boolean hasFreshState(long nowNanos, float myHalfRttMs) {
        return latest != null && isUsableAge(estimator.ageNanos(latestStamp, nowNanos, myHalfRttMs));
    }

    private static boolean isUsableAge(long age) {
        return age >= MIN_STATE_AGE_NANOS && age <= MAX_STATE_AGE_NANOS;
    }

    // Returns where the boat should be shown at nowNanos, or null when there is no usable state
    // (the caller should then leave the boat to vanilla). The displayed position and yaw are where the
    // boat is shown right now, the first usable state starts from there instead of jumping.
    public SyncPredictor.State update(long nowNanos, float myHalfRttMs, SyncPredictor.Collider collider,
                                      double displayedX, double displayedY, double displayedZ, float displayedYaw) {
        SyncPredictor.State state = latest;
        long age = state == null ? Long.MAX_VALUE : estimator.ageNanos(latestStamp, nowNanos, myHalfRttMs);

        if (state == null || !isUsableAge(age)) {
            reset();
            return null;
        }

        SyncPredictor.State predicted = SyncPredictor.predict(state, age / 1_000_000.0, collider);
        if (!predicted.isFinite()) {
            reset();
            return null;
        }

        residualX *= 0.5;
        residualY *= 0.5;
        residualZ *= 0.5;
        residualYaw *= 0.5f;

        if (used == null) {
            // first usable state, start half way from where the boat is shown now
            residualX = (displayedX - predicted.x) * 0.5;
            residualY = (displayedY - predicted.y) * 0.5;
            residualZ = (displayedZ - predicted.z) * 0.5;
            residualYaw = wrapDegrees(displayedYaw - predicted.yaw) * 0.5f;
        } else if (used != state) {
            if (isTeleport(used, state, latestStamp - usedStamp)) {
                residualX = residualY = residualZ = 0;
                residualYaw = 0;
            } else {
                // a new state arrived, keep showing the old trajectory and fold the jump into the residual
                // how far the old trajectory went since the last update (plain arithmetic), moved through
                // collision once from where it was shown so a wall still stops it
                double oldAgeMs = estimator.ageNanos(usedStamp, nowNanos, myHalfRttMs) / 1_000_000.0;
                SyncPredictor.State then = SyncPredictor.predict(used, previousAgeMs, null);
                SyncPredictor.State now = SyncPredictor.predict(used, oldAgeMs, null);

                double[] moved = {now.x - then.x, now.y - then.y, now.z - then.z};
                if (collider != null) moved = collider.collide(previous.x, previous.y, previous.z, moved[0], moved[1], moved[2]);

                residualX += previous.x + moved[0] - predicted.x;
                residualY += previous.y + moved[1] - predicted.y;
                residualZ += previous.z + moved[2] - predicted.z;
                residualYaw = wrapDegrees(residualYaw + previous.yaw + (now.yaw - then.yaw) - predicted.yaw);
            }
        }

        double residualSq = residualX * residualX + residualY * residualY + residualZ * residualZ;
        if (!(residualSq <= MAX_BLEND * MAX_BLEND) || !Float.isFinite(residualYaw)) {
            residualX = residualY = residualZ = 0;
            residualYaw = 0;
        }

        used = state;
        usedStamp = latestStamp;
        previous = new SyncPredictor.State(predicted);
        previousAgeMs = age / 1_000_000.0;

        predicted.x += residualX;
        predicted.y += residualY;
        predicted.z += residualZ;
        predicted.yaw += residualYaw;
        return predicted;
    }

    // The sender moved further than its speed explains (a teleport or respawn), nothing to blend from.
    private static boolean isTeleport(SyncPredictor.State from, SyncPredictor.State to, long stampGapNanos) {
        double speed = Math.max(speed(from), speed(to));
        double gapTicks = Math.max(1.0, stampGapNanos / (SyncPredictor.TICK_MS * 1_000_000.0));
        double limit = 2 * speed * gapTicks + TELEPORT_MARGIN;

        return !to.isNear(from.x, from.y, from.z, limit);
    }

    private static double speed(SyncPredictor.State state) {
        return Math.sqrt(state.vx * state.vx + state.vy * state.vy + state.vz * state.vz);
    }

    private void reset() {
        used = null;
        previous = null;
        residualX = residualY = residualZ = 0;
        residualYaw = 0;
    }

    static float wrapDegrees(float degrees) {
        float wrapped = degrees % 360f;
        if (wrapped >= 180f) wrapped -= 360f;
        if (wrapped < -180f) wrapped += 360f;
        return wrapped;
    }
}
