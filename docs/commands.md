# Commands

These commands are only available in singleplayer, and on servers with the openboatutils mod installed.

## `/reset`
Resets all settings to their vanilla defaults.

Example: `/reset`

## `/defaultslipperiness <slipperiness>`
Sets the slipperiness value used for blocks that do not already have a defined slipperiness value. This is `0.6` in vanilla.

- `<slipperiness>` - float

Example: `/defaultslipperiness 0.6`

## `/blockslipperiness <slipperiness> <block,..>`
Sets the slipperiness value for one or more specific blocks.

- `<slipperiness>` - float
- `<block,..>` - comma-separated list of namespaced keys (no spaces)

Example: `/blockslipperiness 0.98 minecraft:grass_block,minecraft:coarse_dirt`

## `/removeblockslipperiness <block,...>`
Removes custom slipperiness values from the specified blocks.

- `<block,...>` - comma-separated list of namespaced keys (no spaces)

Example: `/removeblockslipperiness minecraft:grass_block,minecraft:stone`

## `/clearslipperiness`
Clears all custom slipperiness values and resets them to defaults.

Example: `/clearslipperiness`

## `/aircontrol <enabled>`
Enables or disables "air control." When enabled, your boat moves in the air as if it were on the ground. Slipperiness is based on `minecraft:air` or the default value if not set.

- `<enabled>` - boolean

Example: `/aircontrol true`

## `/waterelevation <enabled>`
Enables or disables "water elevation." When enabled, your boat will rise in water streams until it reaches the top, similar to old BoatUtils behavior.

- `<enabled>` - boolean

Example: `/waterelevation true`

## `/falldamage <enabled>`
Enables or disables fall damage for boats. When enabled (default), boats will break after falling more than a few blocks.

- `<enabled>` - boolean

Example: `/falldamage false`

## `/jumpforce <force>`
Sets the force applied when the boat jumps. Default is `0`.

- `<force>` - float

Example: `/jumpforce 0.36`

## `/boatmode <mode>`
Sets the boat movement mode. Modes stack on top of the current settings. the [documentation](/modes) for available modes.

- `<mode>` - mode name (string)

Example: `/boatmode BA`

## `/exclusiveboatmode <mode>`
Resets all settings, then applies the specified mode.

- `<mode>` - mode name (string)

Example: `/exclusiveboatmode BA`

## `/modeseries <mode,...>`
Applies a sequence of modes in order. Modes stack as they are applied.

- `<mode,...>` - comma-separated list of mode names (no spaces)

Example: `/modeseries BA,JUMP_BLOCKS`

## `/exclusivemodeseries <mode,...>`
Resets all settings, then applies a sequence of modes in order.

- `<mode,...>` - comma-separated list of mode names (no spaces)

Example: `/exclusivemodeseries BA,JUMP_BLOCKS`

## `/boatgravity <gravity>`
Sets the strength of gravity applied to the boat. The vanilla value is `-0.03999999910593033` (yes).

- `<gravity>` - double

Example: `/boatgravity -0.02`

## `/setyawaccel <accel>`
Sets the boat's angular (turning) acceleration. The vanilla value is `1.0`.

- `<accel>` - float

Example: `/setyawaccel 1.2`

## `/setforwardaccel <accel>`
Sets the boat's acceleration when moving forward. The vanilla value is `0.04`.

- `<accel>` - float

Example: `/setforwardaccel 0.05`

## `/setbackwardaccel <accel>`
Sets the boat's acceleration when moving backward. The vanilla value is `0.005`.

- `<accel>` - float

Example: `/setbackwardaccel 0.006`

## `/setturnforwardaccel <accel>`
Sets the forward acceleration applied when turning without pressing forward or backward. The vanilla value is `0.005`.

- `<accel>` - float

Example: `/setturnforwardaccel 0.007`

## `/allowaccelstacking <enabled>`
Enables or disables acceleration stacking. When enabled, turn-based forward acceleration can stack with forward/backward acceleration.

- `<enabled>` - boolean

Example: `/allowaccelstacking true`

## `/stepsize <size>`
Sets the player's step height. Higher values allow stepping up taller blocks without jumping.

- `<size>` - float

Example: `/stepsize 1.25`

## `/stepwhilefalling <enabled>`
Enables or disables stepping up blocks while falling.

- `<enabled>` - boolean

Example: `/stepwhilefalling true`

## `/setblocksetting <setting> <value> <block,...>`
Sets a per-block setting for one or more specific blocks.

- `<setting>` - setting name (string)
- `<value>` - float
- `<block,...>` - comma-separated list of namespaced keys (no spaces)

Example: `/setblocksetting JUMPS 4 minecraft:ice,minecraft:packed_ice`

| Setting               | Global Command          |
|-----------------------|-------------------------|
| `JUMP_FORCE`          | `/jumpforce`            |
| `FORWARDS_ACCEL`      | `/setforwardsaccel`     |
| `BACKWARDS_ACCEL`     | `/setbackwardsaccel`    |
| `YAW_ACCEL`           | `/setyawaccel`          |
| `TURN_FORWARDS_ACCEL` | `/setturnforwardsaccel` |
| `WALLTAP_MULTIPLIER`  | `/setwalltapmultiplier` |
| `JUMPS`               | `/setjumps`             |
| `COYOTE_TIME`         | `/coyotetime`           |

## `/collisionmode <id>`
Sets the collision mode by its numeric ID.

- `<id>` - integer

Example: `/collisionmode 1`

|    ID     | Setting                           |
|:---------:|-----------------------------------|
|    `0`    | `VANILLA`                         |
|    `1`    | `NO_BOATS_OR_PLAYERS`             |
|    `2`    | `NO_ENTITIES`                     |
|    `3`    | `EMTITYTYPE_FILTER`               |
|    `4`    | `NO_BOATS_OR_PLAYERS_PLUS_FILTER` |

## `/clearcollisionfilter`
Clears all entity type collision filters.

Example: `/clearcollisionfilter`

## `/addcollisionfilter <entitytypes>`
Adds entity types to the collision filter.

- `<entitytypes>` - comma-separated list of namespaced keys (no spaces)

Example: `/addcollisionfilter minecraft:zombie,minecraft:item`

## `/underwatercontrol <enabled>`
Enables or disables boat control while fully underwater.

- `<enabled>` - boolean

Example: `/underwatercontrol true`

## `/surfacewatercontrol <enabled>`
Enables or disables boat control while on the surface of water.

- `<enabled>` - boolean

Example: `/surfacewatercontrol true`

## `/coyotetime <ticks>`
Sets the number of ticks where actions are still allowed after leaving the ground.

- `<ticks>` - integer

Example: `/coyotetime 5`

## `/waterjumping <enabled>`
Enables or disables jumping while in water.

- `<enabled>` - boolean

Example: `/waterjumping true`

## `/swimforce <force>`
Sets the force applied when moving in water.

- `<force>` - float

Example: `/swimforce 0.1`

## `/setwalltapmultiplier <multiplier>`
Sets the velocity multiplier applied when the boat collides with a wall.
Setting this value to `0` is equivalent to vanilla behaviour and setting
it to `1` will result in 100% of your velocity being reflected off the collision.

- `<multiplier>` - float

Example: `/setwalltapmultiplier 0.5`

## `/setjumps <jumps>`
Sets the number of jumps available to the boat. This is 1 by default and doesn't do anything while jump force is `0`.
Setting this to `2` will result in a double jump and so on.

- `<jumps>` - integer

Example: `/setjumps 2`

## `/setscale <scale>`
Sets the scale of the boat.

- `<scale>` - float

Example: `/setscale 1.5`

## `/switchcontext <context>`
Switches to the specified settings context, creating it if it does not exist. Each context holds its own independent set of settings.

- `<context>` - context name (namespaced id)

Example: `/switchcontext myplugin:mycontext`

## `/dropcontext <context>`
Drops the specified settings context, removing it and all settings stored within it.

- `<context>` - context name (namespaced id)

Example: `/dropcontext myplugin:mycontext`

## `/setinterpolationten <enabled>`
Enables or disables interpolation compatibility mode.

- `<enabled>` - boolean

Example: `/setinterpolationten true`

## `/setcollisionresolution <resolution>`
Sets the collision resolution level. Must be between `1` and `50`.

- `<resolution>` - integer

Example: `/setcollisionresolution 10`
