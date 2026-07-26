package io.github.openboatutils.protocol;

public enum OBUChannel {
    SETTINGS(Phase.PLAY, "openboatutils:settings"),
    CONTEXT(Phase.PLAY, "openboatutils:context"),
    CONFIGURATION(Phase.CONFIGURATION, "openboatutils:configuration");

    private final Phase phase;
    private final String channel;

    OBUChannel(Phase phase, String channel) {
        this.phase = phase;
        this.channel = channel;
    }

    public Phase getPhase() {
        return phase;
    }

    public String getChannel() {
        return channel;
    }

    public static OBUChannel getChannel(String channel) {
        for (OBUChannel value : values()) {
            if (value.getChannel().equals(channel)) {
                return value;
            }
        }

        return null;
    }

    public enum Phase {
        PLAY,
        CONFIGURATION
    }
}
