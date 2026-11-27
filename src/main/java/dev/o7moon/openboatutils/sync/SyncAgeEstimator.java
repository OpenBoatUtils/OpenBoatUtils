package dev.o7moon.openboatutils.sync;

import java.util.ArrayDeque;

// Estimates how old a remote boat state is without a shared clock.
//
// Every STATE or PROBE gives d = receiveNanos - stampNanos, which is the clock offset between the two
// clients plus this packet's one way delay. The minimum d over the window is the offset plus the
// fastest path seen. The age of a stamp is then (now - stamp) - base, plus the fastest path itself,
// which is estimated from both clients' half round trip times to the server.
//
// Probes are sent between ticks, spread over the tick phase. Client and server ticks are phase locked,
// so if the relay holds packets until a tick boundary, every STATE carries the same hold and the
// minimum of STATE packets alone would hide it. Probes see every phase, so their minimum does not.
//
// Receive times should be taken when the packet comes off the network, not when the client thread
// gets to it: a frame's wait in the receive time is not removed by the minimum (see the tests).
public class SyncAgeEstimator {
    public static final long DEFAULT_WINDOW_NANOS = 10_000_000_000L;

    // A sample this far from the current base, either way, means the sender's clock jumped
    // (restart, or another player now drives the boat), start over.
    public static final long DISCONTINUITY_NANOS = 2_000_000_000L;

    private final long windowNanos;

    // sliding window minimum, entries are {receiveNanos, d} with strictly increasing d
    private final ArrayDeque<long[]> window = new ArrayDeque<>();

    private float senderHalfRttMs;

    public static final long PROBE_INTERVAL_NANOS = 125_000_000L;
    private static final long TICK_NANOS = 50_000_000L;
    private static final double GOLDEN = 0.6180339887498949;

    // When the k-th probe goes out, relative to the first. About 8 a second, with the offset inside
    // the tick stepping by the golden ratio so the tick phase is covered evenly instead of by chance.
    public static long probeTimeNanos(long k) {
        double phase = (k * GOLDEN) % 1.0;
        return k * PROBE_INTERVAL_NANOS + (long) (phase * TICK_NANOS);
    }

    public SyncAgeEstimator() {
        this(DEFAULT_WINDOW_NANOS);
    }

    public SyncAgeEstimator(long windowNanos) {
        this.windowNanos = windowNanos;
    }

    // Returns true if this sample was a clock discontinuity and the window was reset.
    public boolean addSample(long stampNanos, long receiveNanos, float senderHalfRttMs) {
        long d = receiveNanos - stampNanos;

        boolean discontinuity = !window.isEmpty() && Math.abs(d - window.peekFirst()[1]) > DISCONTINUITY_NANOS;
        if (discontinuity) window.clear();

        while (!window.isEmpty() && window.peekLast()[1] >= d) window.pollLast();
        window.addLast(new long[]{receiveNanos, d});

        while (receiveNanos - window.peekFirst()[0] > windowNanos) window.pollFirst();

        this.senderHalfRttMs = sanitizeHalfRtt(senderHalfRttMs);
        return discontinuity;
    }

    public boolean hasBase() {
        return !window.isEmpty();
    }

    public long getBaseNanos() {
        return window.isEmpty() ? 0 : window.peekFirst()[1];
    }

    public float getSenderHalfRttMs() {
        return senderHalfRttMs;
    }

    // Estimated time between the sender taking the stamp and nowNanos (both read on their own clocks).
    public long ageNanos(long stampNanos, long nowNanos, float myHalfRttMs) {
        long fastestPath = (long) ((senderHalfRttMs + sanitizeHalfRtt(myHalfRttMs)) * 1_000_000.0);
        return (nowNanos - stampNanos) - getBaseNanos() + fastestPath;
    }

    private static float sanitizeHalfRtt(float halfRttMs) {
        if (!(halfRttMs > 0)) return 0; // also catches NaN
        return Math.min(halfRttMs, 1000f);
    }
}
