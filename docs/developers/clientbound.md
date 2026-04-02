# Clientbound
Packets sent to a client.

## Reset
Resets the current context to the vanilla state.

**Payload:**
Packet ID |
-|
0 (`short`)|
***

## Set Step Height
Sets the player's boat step height, in blocks. The vanilla value is `0f` and the old boatutils uses a value of `1.25f`.

**Payload:**
Packet ID|Step Height|
-|-|
1 (`short`)|`float`|

## Set Default Slipperiness
Sets the slipperiness value that is used for any blocks that don't have a specified slipperiness value. The vanilla value is `0.6f`.

**Payload:**
Packet ID|Default Slipperiness|
-|-|
2 (`short`)|`float`|

## Set Block(s) Slipperiness
Sets the slipperiness value that is used the specified block(s).

**Payload:**
Packet ID|Slipperiness|Blocks|
-|-|-|
3 (`short`)|`float`|`String`, comma seperated list of block ID's|

## Set Boat Fall Damage
Sets if the boat takes fall damage and breaks. The vanilla value is `true`.

**Payload:**
Packet ID|Fall Damage|
-|-|
4 (`short`)|`boolean`|

## Set Boat Water Elevation
If true, the boat will move upward in water streams until it reaches the top. This is implemented in the same way as the old boatutils for parity reasons.

**Payload:**
Packet ID|Water Elevation|
-|-|
5 (`short`)|`boolean`|

## Set Boat Air Control
If true, the boat will control in the air like it does on the ground. it's slipperiness value will be taken from `minecraft:air` or the default slipperiness if no value is provided.

**Payload:**
Packet ID|Air Control|
-|-|
6 (`short`)|`boolean`|

## Set Boat Jump Force
If this is greater than `0f` (the default), the player will be able to jump while in the boat, and their y velocity will be set to this value when jumping. `0.36f` is a good value for 1 block tall jumps with a bit of leniency.

**Payload:**
Packet ID|Jump Force|
-|-|
7 (`short`)|`float`|

## Set Mode
Modes are like setting presets. upon receiving this packet the client will set it's settings to those of the mode.

There is a list of modes and more information [here](https://github.com/o7Moon/OpenBoatUtils/wiki/Modes).

**Payload:**
Packet ID|Mode ID|
-|-|
8 (`short`)|`short`|

## Set Gravity
Sets the magnitude of the gravity force. the vanilla value is `-0.03999999910593033` (blame mojang).

**Payload:**
Packet ID|Gravity|
-|-|
9 (`short`)|`double`|

## Set Yaw Acceleration
Sets the boat's angular acceleration. The vanilla value is `1.0f`.

**Payload:**
Packet ID|Acceleration|
-|-|
10 (`short`)|`float`|

## Set Forward Acceleration
Sets the boat's acceleration when pressing the forward key. The vanilla value is `0.04f`.

**Payload:**
Packet ID|Acceleration|
-|-|
11 (`short`)|`float`|

## Set Backward Acceleration
Sets the boat's acceleration when pressing the backward key. The vanilla value is `0.005f`.

**Payload:**
Packet ID|Acceleration|
-|-|
12 (`short`)|`float`|

## Set Turning Forward Acceleration
The boat gets forwards acceleration when you turn and aren't pressing forwards or backwards. The vanilla value is `0.005f`.

**Payload:**
Packet ID|Acceleration|
-|-|
13 (`short`)|`float`|

## Allow Acceleration Stacking
Allows Turning Forward Acceleration to apply at the same time as forward/backward acceleration.

**Payload:**
Packet ID|Enabled|
-|-|
14 (`short`)|`boolean`|

## Resend Version
The client will send the version packet again when receiving this.

**Payload:**
Packet ID|
-|
15 (`short`)|

## Set Underwater Control
If true, makes the boat's underwater physics act like grounded physics (similar to air control), using the slipperiness of `minecraft:water`

**Payload:**
Packet ID|Enabled|
-|-|
16 (`short`)|`boolean`|

## Set Surface Water Control
If true, makes the boat's surface water physics act like grounded physics (similar to the above packet), using the slipperiness of `minecraft:water`

**Payload:**
Packet ID|Enabled|
-|-|
17 (`short`)|`boolean`|

## Set Exclusive Mode
Sets a mode after first resetting all settings to vanilla values.

There is a list of modes and more information [here](https://github.com/o7Moon/OpenBoatUtils/wiki/Modes).

**Payload:**
Packet ID|Mode ID|
-|-|
18 (`short`)|`short`|

## Set Coyote Time
Sets the number of ticks after leaving the ground where the boat can still jump.

**Payload:**
Packet ID|Time|
-|-|
19 (`short`)|`int`|

## Set Water Jumping
If true, and a jump force has been set, allows the boat to jump when on the surface of water.

**Payload:**
Packet ID|Enabled|
-|-|
20 (`short`)|`boolean`|

## Set Swim Force
If nonzero, pressing space while underwater will apply this vertical force to the boat.

**Payload:**
Packet ID|force|
-|-|
21 (`short`)|`float`|

## Clear Blocks Slipperiness
removes the specific slipperiness for the specified blocks, giving them to whatever the default slipperiness is.

**Payload:**
Packet ID|blocks|
-|-|
22 (`short`)|`string`, comma separated block IDs|

## Clear All Slipperiness
removes the specific slipperiness for all blocks, making everything have the default slipperiness (makes ice and similar blocks with specific slipperiness have the same slipperiness as everything else).

**Payload:**
Packet ID|
-|
23 (`short`)|

## Apply Mode Series
applies a list of modes in order with one packet.

**Payload:**
Packet ID|amount|modes|
-|-|-|
24 (`short`)|`short`|`short[amount]`|

## Apply Exclusive Mode Series
applies a list of modes in order with one packet, resetting first.

**Payload:**
Packet ID|amount|modes|
-|-|-|
25 (`short`)|`short`|`short[amount]`|

## Set Per-Block Setting
Sets a setting that is applied based on the blocks the bloat is standing above, similar to how slipperiness works. When the boat is not on the ground, the value for `minecraft:air` is used, and the default values for every block are the ones set by previous packets for these settings (if you set a jump force, and then set a per-block jump force for a set of blocks, the difference will only be noticeable when standing on those blocks).

**Payload:**
Packet ID|setting|value|blocks|
-|-|-|-|
26 (`short`)|`short` (values below)|`float`|`string`, comma separated block ids to apply this value to|

**Values for `setting`:**
`jumpForce`|`forwardsAccel`|`backwardsAccel`|`yawAccel`|`turnForwardsAccel`|
-|-|-|-|-|
0|1|2|3|4|

## Set Collision Mode
Sets how the boat collides with other entities.

**Payload:**
Packet ID|ID|
-|-|
27 (`short`)|`short` (values below)|

**Values for `ID`:**
vanilla|no collision with boats and players|no collision with any entities|[filtered collision](#add-to-collision-filter)|no collision with boats and players plus filtered collision|
-|-|-|-|-|
0|1|2|3|4|

## Set Air Stepping
Sets if the boat is allowed to step when it hits the side of a block in the air.

**Payload:**
Packet ID|enabled|
-|-|
28 (`short`)|`boolean` (default false)|

## Set Ten Step Interpolation
Sets if the boat interpolation is changed from 3 steps to 10. this is a non-context setting, the api for these is still being defined so it is not very ergonomic to use currently (they cant be reset, you have to manually set the value if you want the default one).

This affects *all* boats.

**Payload:**
Packet ID|enabled|
-|-|
29 (`short`)|`boolean` (default false)|

## Set Air Stepping
Sets if the boat is allowed to step when it hits the side of a block in the air.

**Payload:**
Packet ID|enabled|
-|-|
28 (`short`)|`boolean` (default false)|

## Set Collision Resolution
Sets the amount of times the boat's movement step is divided, to try to avoid incorrect corner collisions at higher speeds.

This is capped to 50 but a value of like 5 is usually more than enough.

**Payload:**
Packet ID|resolution|
-|-|
30 (`short`)|`byte` (1-50)|

## Add To Collision Filter
Adds a certain type of entity to the collision filter to be filtered out when using filtered [collision modes](#set-collision-mode).

Entity names are namespaced like `minecraft:player`.

**Payload:**
Packet ID|entities|
-|-|
31 (`short`)|`string` (comma separated list to add to the filter)|

## Clear Collision Filter
Removes all entity types currently filtered out when using filtered [collision modes](#set-collision-mode).

**Payload:**
Packet ID|
-|
32 (`short`)|

