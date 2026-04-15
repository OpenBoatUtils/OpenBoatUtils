---
outline: deep
---
# Settings Packets
Plugin Channel Identifier: `openboatutils:settings`

Contains packets that modify the current context (by default this is the default context)
with the exception of the version packet, which is here due to backwards compatibility

## Serverbound
### Version
Sends the server the version of OpenBoatUtils we are currently running. This is sent once on join.

As of `0.5.0` an unstable flag is also sent after the first short.
It is intended to block use of potentially broken development builds without
having to impact the version Ids. ***probably reject builds of these for most people***

Take care to only read the unstable flag when the version id is `>= 19` (0.5.0).

| Packet ID | Version | Unstable (as of 0.5.0) |
|-----------|---------|------------------------|
| 0 (`short`) | [Version ID](https://github.com/o7Moon/OpenBoatUtils/wiki/Version-IDs) (`int`) | `boolean` |

### Transaction Result
Sent after a transaction packet has been processed

As of `0.5.0` an unstable flag is also sent after the first short.
It is intended to block use of potentially broken development builds without
having to impact the version Ids. ***probably reject builds of these for most people***

Take care to only read the unstable flag when the version id is `>= 19` (0.5.0).

| Packet ID | Version | Unstable (as of 0.5.0) |
|-----------|---------|------------------------|
| 0 (`short`) | [Version ID](https://github.com/o7Moon/OpenBoatUtils/wiki/Version-IDs) (`int`) | `boolean` |

## Clientbound

### Reset
Resets all settings in the current context back to vanilla values. If operating in the default context, this also fully disables the mod (equivalent to no context being active).

| Packet ID |
|-----------|
| 0 (`short`) |

***

### Set Step Height
Sets the player's boat step height, in blocks. The vanilla value is `0f` and the old boatutils uses a value of `1.25f`.

| Packet ID | Step Height |
|-----------|-------------|
| 1 (`short`) | `float` |

***

### Set Default Slipperiness
Sets the slipperiness value that is used for any blocks that don't have a specified slipperiness value. The vanilla value is `0.6f`.

| Packet ID | Default Slipperiness |
|-----------|----------------------|
| 2 (`short`) | `float` |

***

### Set Block(s) Slipperiness
Sets the slipperiness value that is used the specified block(s).

| Packet ID | Slipperiness | Blocks |
|-----------|--------------|--------|
| 3 (`short`) | `float` | `String`, comma seperated list of block ID's |

***

### Set Boat Fall Damage
Sets if the boat takes fall damage and breaks. The vanilla value is `true`.

| Packet ID | Fall Damage |
|-----------|-------------|
| 4 (`short`) | `boolean` |

***

### Set Boat Water Elevation
If true, the boat will move upward in water streams until it reaches the top. This is implemented in the same way as the old boatutils for parity reasons.

| Packet ID | Water Elevation |
|-----------|-----------------|
| 5 (`short`) | `boolean` |

***

### Set Boat Air Control
If true, the boat will control in the air like it does on the ground. it's slipperiness value will be taken from `minecraft:air` or the default slipperiness if no value is provided.

| Packet ID | Air Control |
|-----------|-------------|
| 6 (`short`) | `boolean` |

***

### Set Boat Jump Force
If this is greater than `0f` (the default), the player will be able to jump while in the boat, and their y velocity will be set to this value when jumping. `0.36f` is a good value for 1 block tall jumps with a bit of leniency.

| Packet ID | Jump Force |
|-----------|------------|
| 7 (`short`) | `float` |

***

### Set Mode
Modes are like setting presets. upon receiving this packet the client will set it's settings to those of the mode.

There is a list of modes and more information [here](https://github.com/o7Moon/OpenBoatUtils/wiki/Modes).

| Packet ID | Mode ID |
|-----------|---------|
| 8 (`short`) | `short` |

***

### Set Gravity
Sets the magnitude of the gravity force. the vanilla value is `-0.03999999910593033` (blame mojang).

| Packet ID | Gravity |
|-----------|---------|
| 9 (`short`) | `double` |

***

### Set Yaw Acceleration
Sets the boat's angular acceleration. The vanilla value is `1.0f`.

| Packet ID | Acceleration |
|-----------|--------------|
| 10 (`short`) | `float` |

***

### Set Forward Acceleration
Sets the boat's acceleration when pressing the forward key. The vanilla value is `0.04f`.

| Packet ID | Acceleration |
|-----------|--------------|
| 11 (`short`) | `float` |

***

### Set Backward Acceleration
Sets the boat's acceleration when pressing the backward key. The vanilla value is `0.005f`.

| Packet ID | Acceleration |
|-----------|--------------|
| 12 (`short`) | `float` |

***

### Set Turning Forward Acceleration
The boat gets forwards acceleration when you turn and aren't pressing forwards or backwards. The vanilla value is `0.005f`.

| Packet ID | Acceleration |
|-----------|--------------|
| 13 (`short`) | `float` |

***

### Allow Acceleration Stacking
Allows Turning Forward Acceleration to apply at the same time as forward/backward acceleration.

| Packet ID | Enabled |
|-----------|---------|
| 14 (`short`) | `boolean` |

***

### Resend Version
The client will send the version packet again when receiving this.

| Packet ID |
|-----------|
| 15 (`short`) |

***

### Set Underwater Control
If true, makes the boat's underwater physics act like grounded physics (similar to air control), using the slipperiness of `minecraft:water`

| Packet ID | Enabled |
|-----------|---------|
| 16 (`short`) | `boolean` |

***

### Set Surface Water Control
If true, makes the boat's surface water physics act like grounded physics (similar to the above packet), using the slipperiness of `minecraft:water`

| Packet ID | Enabled |
|-----------|---------|
| 17 (`short`) | `boolean` |

***

### Set Exclusive Mode
Sets a mode after first resetting all settings to vanilla values.

There is a list of modes and more information [here](https://github.com/o7Moon/OpenBoatUtils/wiki/Modes).

| Packet ID | Mode ID |
|-----------|---------|
| 18 (`short`) | `short` |

***

### Set Coyote Time
Sets the number of ticks after leaving the ground where the boat can still jump.

| Packet ID | Time |
|-----------|------|
| 19 (`short`) | `int` |

***

### Set Water Jumping
If true, and a jump force has been set, allows the boat to jump when on the surface of water.

| Packet ID | Enabled |
|-----------|---------|
| 20 (`short`) | `boolean` |

***

### Set Swim Force
If nonzero, pressing space while underwater will apply this vertical force to the boat.

| Packet ID | force |
|-----------|-------|
| 21 (`short`) | `float` |

***

### Clear Blocks Slipperiness
Removes the specific slipperiness for the specified blocks, giving them to whatever the default slipperiness is.

| Packet ID | blocks |
|-----------|--------|
| 22 (`short`) | `string`, comma separated block IDs |

***

### Clear All Slipperiness
removes the specific slipperiness for all blocks, making everything have the default slipperiness (makes ice and similar blocks with specific slipperiness have the same slipperiness as everything else).

| Packet ID |
|-----------|
| 23 (`short`) |

***

### Apply Mode Series
applies a list of modes in order with one packet.

| Packet ID | amount | modes |
|-----------|--------|-------|
| 24 (`short`) | `short` | `short[]` |

***

### Apply Exclusive Mode Series
applies a list of modes in order with one packet, resetting first.

| Packet ID | amount | modes |
|-----------|--------|-------|
| 25 (`short`) | `short` | `short[]` |

***

### Set Per-Block Setting
Sets a setting that is applied based on the blocks the bloat is standing above, similar to how slipperiness works. When the boat is not on the ground, the value for `minecraft:air` is used, and the default values for every block are the ones set by previous packets for these settings (if you set a jump force, and then set a per-block jump force for a set of blocks, the difference will only be noticeable when standing on those blocks).

| Packet ID | setting | value | blocks |
|-----------|---------|-------|--------|
| 26 (`short`) | `short` (values below) | `float` | `string`, comma separated block ids to apply this value to |

**Values for `setting`:**

| jumpForce | forwardsAccel | backwardsAccel | yawAccel | turnForwardsAccel |
|-----------|---------------|----------------|----------|-------------------|
| 0 | 1 | 2 | 3 | 4 |

***

### Set Collision Mode
Sets how the boat collides with other entities.

| Packet ID | ID |
|-----------|----|
| 27 (`short`) | `short` (values below) |

**Values for `ID`:**

| vanilla | no collision with boats and players | no collision with any entities | filtered collision | no collision with boats and players plus filtered collision |
|--------|--------------------------------------|--------------------------------|--------------------|-------------------------------------------------------------|
| 0 | 1 | 2 | 3 | 4 |

***

### Set Air Stepping
Sets if the boat is allowed to step when it hits the side of a block in the air.

| Packet ID | enabled |
|-----------|---------|
| 28 (`short`) | `boolean` (default false) |

***

### Set Ten Step Interpolation  <Badge type="warning" text="Non-Context" />
Sets if the boat interpolation is changed from 3 steps to 10.

This affects *all* boats and is handled completely separately to contexts, a reset packet won't clear this setting but relogging will.

| Packet ID | enabled |
|-----------|---------|
| 29 (`short`) | `boolean` (default false) |

::: details Why do this?
Before 1.21.3 boats interpolated over 10 ticks, after this point it was changed to 3 ticks by mojang.
Most servers will set this to true upon join to replicate the pre 1.21.3 behaviour which is usually essential for racing.
:::

***

### Set Collision Resolution
Sets the amount of times the boat's movement step is divided, to try to avoid incorrect corner collisions at higher speeds.

This is capped to 50 but a value of like 5 is usually more than enough.

| Packet ID | resolution |
|-----------|------------|
| 30 (`short`) | `byte` (1-50) |

***

### Add To Collision Filter
Adds a certain type of entity to the collision filter to be filtered out when using filtered [collision modes](#set-collision-mode).

Entity names are namespaced like `minecraft:player`.

| Packet ID | entities |
|-----------|----------|
| 31 (`short`) | `string` (comma separated list to add to the filter) |

***

### Clear Collision Filter
Removes all entity types currently filtered out when using filtered [collision modes](#set-collision-mode).

| Packet ID |
|-----------|
| 32 (`short`) |

***

### Transaction <Badge type="tip" text="^0.5.0" />
Sends multiple setting packets bundled together as a single atomic update. Each inner packet is a full settings packet including its own packet ID short.

| Packet ID | Count | Packets |
|-----------|-------|---------|
| 33 (`short`) | `int` | `packet[]` (each a full clientbound settings packet) |

***

### Set Walltap Multiplier <Badge type="tip" text="^0.5.0" />
Sets the multiplier applied to the boat's velocity when it collides with a wall.

| Packet ID | Multiplier |
|-----------|------------|
| 34 (`short`) | `float` |

***

### Set Jumps <Badge type="tip" text="^0.5.0" />
Sets the number of jumps the boat is allowed before landing again (i.e. for mid-air/double jump support). A value of `1` is the standard single jump.

| Packet ID | Jumps |
|-----------|-------|
| 35 (`short`) | `int` |

***

### Set Scale <Badge type="tip" text="^0.5.0" />
Sets the scale of the boat.

| Packet ID | Scale |
|-----------|-------|
| 36 (`short`) | `float` |

***

### Set Step Up Slipperiness <Badge type="tip" text="^0.5.0" />
The boat's current velocity will be multiplied by this value whenever the boat steps up a block.

| Packet ID    | Scale |
|--------------|-------|
| 37 (`short`) | `float` |

***