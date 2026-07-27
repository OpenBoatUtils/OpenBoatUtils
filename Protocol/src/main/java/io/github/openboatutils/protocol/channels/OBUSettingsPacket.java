package io.github.openboatutils.protocol.channels;

import io.github.openboatutils.protocol.OBUChannel;
import io.github.openboatutils.protocol.OBUPacket;
import io.github.openboatutils.protocol.PacketWriter;
import io.github.openboatutils.protocol.Payload;

import java.io.IOException;
import java.util.List;

public sealed interface OBUSettingsPacket extends OBUPacket
        permits OBUSettingsPacket.CollisionFilterAddEntityType, OBUSettingsPacket.AirControl, OBUSettingsPacket.AccelStacking, OBUSettingsPacket.Impulse, OBUSettingsPacket.ImpulseRelative, OBUSettingsPacket.BackwardAccel, OBUSettingsPacket.BlockSlipperiness, OBUSettingsPacket.FallDamage, OBUSettingsPacket.JumpForce, OBUSettingsPacket.WaterElevation, OBUSettingsPacket.CollisionFilterClear, OBUSettingsPacket.ClearSlipperines, OBUSettingsPacket.CollisionMode, OBUSettingsPacket.CollisionResolution, OBUSettingsPacket.Compound, OBUSettingsPacket.CoyoteTime, OBUSettingsPacket.DefaultSlipperiness, OBUSettingsPacket.ExclusiveMode, OBUSettingsPacket.ExclusiveModeSeries, OBUSettingsPacket.ForwardAccel, OBUSettingsPacket.Gravity, OBUSettingsPacket.InterpolationCompatibility, OBUSettingsPacket.Mode, OBUSettingsPacket.ModeSeries, OBUSettingsPacket.PerBlock, OBUSettingsPacket.RemoveBlockSlipperiness, OBUSettingsPacket.ResendVersion, OBUSettingsPacket.Reset, OBUSettingsPacket.BrakeSlipperiness, OBUSettingsPacket.FixDoubleWaterElevation, OBUSettingsPacket.HoneyCompatibility, OBUSettingsPacket.Jumps, OBUSettingsPacket.LateralSlipperiness, OBUSettingsPacket.MaxSpeed, OBUSettingsPacket.MaxSpeedResistance, OBUSettingsPacket.MultiStepping, OBUSettingsPacket.ResetOnWorldLoad, OBUSettingsPacket.Scale, OBUSettingsPacket.StepUpSlipperiness, OBUSettingsPacket.WalltapMultiplier, OBUSettingsPacket.StepSize, OBUSettingsPacket.AirStepping, OBUSettingsPacket.SurfaceWaterControl, OBUSettingsPacket.SwimForce, OBUSettingsPacket.TurnAccel, OBUSettingsPacket.UnderwaterControl, OBUSettingsPacket.WaterJumping, OBUSettingsPacket.YawAccel {

    @Override default short getPacketId() {
        throw new RuntimeException("Not Implemented");
    }
    @Override default short getProtocolVersion() { throw new RuntimeException("Not Implemented"); }

    @Override
    default OBUChannel getChannel() {
        return OBUChannel.SETTINGS;
    };

    final class Reset implements OBUSettingsPacket {
        public short getVersion() { return 16; }
        public short getPacketId() { return 0; }
    }

    final class StepSize implements OBUSettingsPacket {
        public float height;

        public StepSize() {}
        public StepSize(float height) {
            this.height = height;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 1; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeFloat(height);
        }
    }

    final class DefaultSlipperiness implements OBUSettingsPacket {
        public float slipperiness;

        public DefaultSlipperiness() {}
        public DefaultSlipperiness(float slipperiness) {
            this.slipperiness = slipperiness;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 2; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeFloat(slipperiness);
        }
    }

    final class BlockSlipperiness implements OBUSettingsPacket {
        public float slipperiness;
        public List<String> block_ids;

        public BlockSlipperiness() {}
        public BlockSlipperiness(float slipperiness, List<String> block_ids) {
            this.slipperiness = slipperiness;
            this.block_ids = block_ids;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 3; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeFloat(slipperiness);
            writer.writeString(String.join(",", block_ids));
        }
    }

    final class FallDamage implements OBUSettingsPacket {
        public boolean enabled;

        public FallDamage() {}
        public FallDamage(boolean enabled) {
            this.enabled = enabled;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 4; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeBoolean(enabled);
        }
    }

    final class WaterElevation implements OBUSettingsPacket {
        public boolean enabled;

        public WaterElevation() {}
        public WaterElevation(boolean enabled) {
            this.enabled = enabled;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 5; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeBoolean(enabled);
        }
    }

    final class AirControl implements OBUSettingsPacket {
        public boolean enabled;

        public AirControl() {}
        public AirControl(boolean enabled) {
            this.enabled = enabled;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 6; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeBoolean(enabled);
        }
    }

    final class JumpForce implements OBUSettingsPacket {
        public float force;

        public JumpForce() {}
        public JumpForce(float force) {
            this.force = force;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 7; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeFloat(force);
        }
    }

    final class Mode implements OBUSettingsPacket {
        public short mode_id;

        public Mode() {}
        public Mode(short mode_id) {
            this.mode_id = mode_id;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 8; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeShort(mode_id);
        }
    }

    final class Gravity implements OBUSettingsPacket {
        public double gravity;

        public Gravity() {}
        public Gravity(double gravity) {
            this.gravity = gravity;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 9; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeDouble(gravity);
        }
    }

    final class YawAccel implements OBUSettingsPacket {
        public float accel;

        public YawAccel() {}
        public YawAccel(float accel) {
            this.accel = accel;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 10; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeFloat(accel);
        }
    }

    final class ForwardAccel implements OBUSettingsPacket {
        public float accel;

        public ForwardAccel() {}
        public ForwardAccel(float accel) {
            this.accel = accel;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 11; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeFloat(accel);
        }
    }

    final class BackwardAccel implements OBUSettingsPacket {
        public float accel;

        public BackwardAccel() {}
        public BackwardAccel(float accel) {
            this.accel = accel;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 12; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeFloat(accel);
        }
    }

    final class TurnAccel implements OBUSettingsPacket {
        public float accel;

        public TurnAccel() {}
        public TurnAccel(float accel) {
            this.accel = accel;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 13; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeFloat(accel);
        }
    }

    final class AccelStacking implements OBUSettingsPacket {
        public boolean enabled;

        public AccelStacking() {}
        public AccelStacking(boolean enabled) {
            this.enabled = enabled;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 14; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeBoolean(enabled);
        }
    }

    final class ResendVersion implements OBUSettingsPacket {
        public short getVersion() { return 16; }
        public short getPacketId() { return 15; }
    }

    final class UnderwaterControl implements OBUSettingsPacket {
        public boolean enabled;

        public UnderwaterControl() {}
        public UnderwaterControl(boolean enabled) {
            this.enabled = enabled;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 16; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeBoolean(enabled);
        }
    }

    final class SurfaceWaterControl implements OBUSettingsPacket {
        public boolean enabled;

        public SurfaceWaterControl() {}
        public SurfaceWaterControl(boolean enabled) {
            this.enabled = enabled;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 17; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeBoolean(enabled);
        }
    }

    final class ExclusiveMode implements OBUSettingsPacket {
        public short mode_id;

        public ExclusiveMode() {}
        public ExclusiveMode(short mode_id) {
            this.mode_id = mode_id;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 18; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeShort(mode_id);
        }
    }

    final class CoyoteTime implements OBUSettingsPacket {
        public int ticks;

        public CoyoteTime() {}
        public CoyoteTime(int ticks) {
            this.ticks = ticks;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 19; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeInt(ticks);
        }
    }

    final class WaterJumping implements OBUSettingsPacket {
        public boolean enabled;

        public WaterJumping() {}
        public WaterJumping(boolean enabled) {
            this.enabled = enabled;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 20; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeBoolean(enabled);
        }
    }

    final class SwimForce implements OBUSettingsPacket {
        public float force;

        public SwimForce() {}
        public SwimForce(float force) {
            this.force = force;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 21; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeFloat(force);
        }
    }

    final class RemoveBlockSlipperiness implements OBUSettingsPacket {
        public List<String> block_ids;

        public RemoveBlockSlipperiness() {}
        public RemoveBlockSlipperiness(List<String> block_ids) {
            this.block_ids = block_ids;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 22; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeString(String.join(",", block_ids));
        }
    }

    final class ClearSlipperines implements OBUSettingsPacket {
        public short getVersion() { return 16; }
        public short getPacketId() { return 23; }
    }

    final class ModeSeries implements OBUSettingsPacket {
        public List<Short> mode_ids;

        public ModeSeries() {}
        public ModeSeries(List<Short> mode_ids) {
            this.mode_ids = mode_ids;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 24; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeShort((short) mode_ids.size());
            for (short id : mode_ids) writer.writeShort(id);
        }
    }

    final class ExclusiveModeSeries implements OBUSettingsPacket {
        public List<Short> mode_ids;

        public ExclusiveModeSeries() {}
        public ExclusiveModeSeries(List<Short> mode_ids) {
            this.mode_ids = mode_ids;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 25; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeShort((short) mode_ids.size());
            for (short id : mode_ids) writer.writeShort(id);
        }
    }

    final class PerBlock implements OBUSettingsPacket {
        public PerBlockSettings setting;
        public float value;
        public List<String> block_ids;

        public PerBlock() {}
        public PerBlock(PerBlockSettings setting, float value, List<String> block_ids) {
            this.setting = setting;
            this.value = value;
            this.block_ids = block_ids;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 26; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeShort((short) setting.ordinal());
            writer.writeFloat(value);
            writer.writeString(String.join(",", block_ids));
        }
    }

    final class CollisionMode implements OBUSettingsPacket {
        public CollisionModes mode;

        public CollisionMode() {}
        public CollisionMode(CollisionModes mode) {
            this.mode = mode;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 27; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeShort((short) mode.ordinal());
        }
    }

    final class AirStepping implements OBUSettingsPacket {
        public boolean enabled;

        public AirStepping() {}
        public AirStepping(boolean enabled) {
            this.enabled = enabled;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 28; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeBoolean(enabled);
        }
    }

    final class InterpolationCompatibility implements OBUSettingsPacket {
        public boolean enabled;

        public InterpolationCompatibility() {}
        public InterpolationCompatibility(boolean enabled) {
            this.enabled = enabled;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 29; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeBoolean(enabled);
        }
    }

    final class CollisionResolution implements OBUSettingsPacket {
        public byte resolution;

        public CollisionResolution() {}
        public CollisionResolution(byte resolution) {
            this.resolution = resolution;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 30; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeByte(resolution);
        }
    }

    final class CollisionFilterAddEntityType implements OBUSettingsPacket {
        public List<String> entity_ids;

        public CollisionFilterAddEntityType() {}
        public CollisionFilterAddEntityType(List<String> entity_ids) {
            this.entity_ids = entity_ids;
        }

        public short getVersion() { return 16; }
        public short getPacketId() { return 31; }
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeString(String.join(",", entity_ids));
        }
    }

    final class CollisionFilterClear implements OBUSettingsPacket {
        public short getVersion() { return 16; }
        public short getPacketId() { return 32; }
    }

    final class Compound implements OBUSettingsPacket {
        public CompoundPayload compoundPayload;

        public Compound() {}
        public Compound(CompoundPayload compoundPayload) { this.compoundPayload = compoundPayload; }

        public short getVersion() { return 19; }
        public short getPacketId() { return 33; }

        @Override
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            compoundPayload.write(writer);
        }
    }

    final class WalltapMultiplier implements OBUSettingsPacket {
        public float multiplier;

        public WalltapMultiplier() {}
        public WalltapMultiplier(float multiplier) { this.multiplier = multiplier; }

        public short getVersion() { return 19; }
        public short getPacketId() { return 34; }

        @Override
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeFloat(multiplier);
        }
    }

    final class Jumps implements OBUSettingsPacket {
        public int jumps;

        public Jumps() {}
        public Jumps(int jumps) { this.jumps = jumps; }

        public short getVersion() { return 19; }
        public short getPacketId() { return 35; }

        @Override
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeInt(jumps);
        }
    }

    final class Scale implements OBUSettingsPacket {
        public float scale;

        public Scale() {}
        public Scale(float scale) { this.scale = scale; }

        public short getVersion() { return 19; }
        public short getPacketId() { return 36; }

        @Override
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeFloat(scale);
        }
    }

    final class StepUpSlipperiness implements OBUSettingsPacket {
        public float slipperiness;

        public StepUpSlipperiness() {}
        public StepUpSlipperiness(float slipperiness) { this.slipperiness = slipperiness; }

        public short getVersion() { return 19; }
        public short getPacketId() { return 37; }

        @Override
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeFloat(slipperiness);
        }
    }

    final class ResetOnWorldLoad implements OBUSettingsPacket {
        public boolean enabled;

        public ResetOnWorldLoad() {}
        public ResetOnWorldLoad(boolean enabled) { this.enabled = enabled; }

        public short getVersion() { return 19; }
        public short getPacketId() { return 38; }

        @Override
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeBoolean(enabled);
        }
    }

    final class FixDoubleWaterElevation implements OBUSettingsPacket {
        public boolean enabled;

        public FixDoubleWaterElevation() {}
        public FixDoubleWaterElevation(boolean enabled) { this.enabled = enabled; }

        public short getVersion() { return 22; }
        public short getPacketId() { return 39; }

        @Override
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeBoolean(enabled);
        }
    }

    final class LateralSlipperiness implements OBUSettingsPacket {
        public float slipperiness;

        public LateralSlipperiness() {}
        public LateralSlipperiness(float slipperiness) { this.slipperiness = slipperiness; }

        public short getVersion() { return 22; }
        public short getPacketId() { return 40; }

        @Override
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeFloat(slipperiness);
        }
    }

    final class BrakeSlipperiness implements OBUSettingsPacket {
        public float slipperiness;

        public BrakeSlipperiness() {}
        public BrakeSlipperiness(float slipperiness) { this.slipperiness = slipperiness; }

        public short getVersion() { return 22; }
        public short getPacketId() { return 41; }

        @Override
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeFloat(slipperiness);
        }
    }

    final class Impulse implements OBUSettingsPacket {
        public double x;
        public double y;
        public double z;

        public Impulse() {}
        public Impulse(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public short getVersion() { return 22; }
        public short getPacketId() { return 42; }

        @Override
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeDouble(x);
            writer.writeDouble(y);
            writer.writeDouble(z);
        }
    }

    final class ImpulseRelative implements OBUSettingsPacket {
        public double x;
        public double y;
        public double z;

        public ImpulseRelative() {}
        public ImpulseRelative(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public short getVersion() { return 22; }
        public short getPacketId() { return 43; }

        @Override
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeDouble(x);
            writer.writeDouble(y);
            writer.writeDouble(z);
        }
    }

    final class MultiStepping implements OBUSettingsPacket {
        public boolean enabled;

        public MultiStepping() {}
        public MultiStepping(boolean enabled) { this.enabled = enabled; }

        public short getVersion() { return 22; }
        public short getPacketId() { return 44; }

        @Override
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeBoolean(enabled);
        }
    }

    final class MaxSpeed implements OBUSettingsPacket {
        public float speed;

        public MaxSpeed() {}
        public MaxSpeed(float speed) { this.speed = speed; }

        public short getVersion() { return 22; }
        public short getPacketId() { return 45; }

        @Override
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeFloat(speed);
        }
    }

    final class MaxSpeedResistance implements OBUSettingsPacket {
        public float resistance;

        public MaxSpeedResistance() {}
        public MaxSpeedResistance(float resistance) { this.resistance = resistance; }

        public short getVersion() { return 22; }
        public short getPacketId() { return 46; }

        @Override
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeFloat(resistance);
        }
    }

    final class HoneyCompatibility implements OBUSettingsPacket {
        public boolean enabled;

        public HoneyCompatibility() {}
        public HoneyCompatibility(boolean enabled) { this.enabled = enabled; }

        public short getVersion() { return 22; }
        public short getPacketId() { return 47; }

        @Override
        public void write(PacketWriter writer) throws IOException {
            OBUSettingsPacket.super.write(writer);
            writer.writeBoolean(enabled);
        }
    }

    class CompoundPayload implements Payload {
        public List<OBUSettingsPacket> packets;

        public CompoundPayload(List<OBUSettingsPacket> packets) { this.packets = packets; }

        @Override
        public void write(PacketWriter writer) throws IOException {
            writer.writeInt(packets.size());

            for (OBUSettingsPacket packet : packets) {
                packet.write(writer);
            }
        }
    }

    enum PerBlockSettings {
        JUMP_FORCE,
        FORWARD_ACCEL,
        BACKWARD_ACCEL,
        YAW_ACCEL,
        TURN_FORWARD_ACCEL
    }

    enum CollisionModes {
        VANILLA,
        NO_BOATS_AND_PLAYERS,
        NO_ENTITIES,
        FILTERED,
        NO_BOATS_AND_PLAYERS_AND_FILTERED
    }
}