# Commands

## `/reset`
Resets all settings to their vanilla defaults.

Example: `/reset`

## `/defaultslipperiness <slipperiness>`
Sets the slipperiness value used for blocks that do not already have a defined slipperiness value. This is `0.6` in vanilla.

Example: `/defaultslipperiness 0.6`

## `/blockslipperiness <slipperiness> <block,..>`
Sets the slipperiness value for one or more specific blocks. `<block,..>` is a comma-separated list of block IDs (no spaces).

Example: `/blockslipperiness 0.98 minecraft:grass_block,minecraft:coarse_dirt`

## `/aircontrol <enabled>`
Enables or disables "air control." When enabled, your boat moves in the air as if it were on the ground. Slipperiness is based on `minecraft:air` or the default value if not set.

Example: `/aircontrol true`

## `/waterelevation <enabled>`
Enables or disables "water elevation." When enabled, your boat will rise in water streams until it reaches the top, similar to old BoatUtils behavior.

Example: `/waterelevation true`

## `/falldamage <enabled>`
Enables or disables fall damage for boats. When enabled (default), boats will break after falling more than a few blocks.

Example: `/falldamage false`

## `/jumpforce <number>`
Sets the force applied when the boat jumps. Default is `0`.

Example: `/jumpforce 0.36`

## `/boatmode <mode>`
Sets the boat movement mode. See (the documentation)[/modes] for available modes.

Example: `/boatmode BA`

## `/boatgravity <number>`
Sets the strength of gravity applied to the boat. The vanilla value is `-0.03999999910593033`.

Example: `/boatgravity -0.02`

## `/setyawaccel <number>`
Sets the boat’s angular (turning) acceleration. The vanilla value is `1.0`.

Example: `/setyawaccel 1.2`

## `/setforwardaccel <number>`
Sets the boat’s acceleration when moving forward. The vanilla value is `0.04`.

Example: `/setforwardaccel 0.05`

## `/setbackwardaccel <number>`
Sets the boat’s acceleration when moving backward. The vanilla value is `0.005`.

Example: `/setbackwardaccel 0.006`

## `/setturnforwardaccel <number>`
Sets the forward acceleration applied when turning without pressing forward or backward. The vanilla value is `0.005`.

Example: `/setturnforwardaccel 0.007`

## `/allowaccelstacking <enabled>`
Enables or disables acceleration stacking. When enabled, turn-based forward acceleration can stack with forward/backward acceleration.

Example: `/allowaccelstacking true`

## `/stepsize <size>`
Sets the player’s step height. Higher values allow stepping up taller blocks without jumping.

Example: `/stepsize 1.25`

## `/sendversionpacket`
Resends the version packet to the client for re-synchronisation.

Example: `/sendversionpacket`

## `/underwatercontrol <enabled>`
Enables or disables boat control while fully underwater.

Example: `/underwatercontrol true`

## `/surfacewatercontrol <enabled>`
Enables or disables boat control while on the surface of water.

Example: `/surfacewatercontrol true`

## `/exclusiveboatmode <mode>`
Sets an exclusive boat mode, overriding other modes.

Example: `/exclusiveboatmode BA`

## `/coyotetime <ticks>`
Sets the number of ticks where actions are still allowed after leaving the ground.

Example: `/coyotetime 5`

## `/waterjumping <enabled>`
Enables or disables jumping while in water.

Example: `/waterjumping true`

## `/swimforce <force>`
Sets the force applied when moving in water.

Example: `/swimforce 0.1`

## `/removeblockslipperiness <block,...>`
Removes custom slipperiness values from the specified blocks. `<block,...>` is a comma-separated list of block IDs (no spaces).

Example: `/removeblockslipperiness minecraft:grass_block,minecraft:stone`

## `/clearslipperiness`
Clears all custom slipperiness values and resets them to defaults.

Example: `/clearslipperiness`

## `/modeseries <mode,...>`
Sets a sequence of modes to be applied in order. `<mode,...>` is a comma-separated list of mode names (no spaces).

Example: `/modeseries speed,control,vanilla`

## `/exclusivemodeseries <mode,...>`
Sets a sequence of exclusive modes to be applied in order. `<mode,...>` is a comma-separated list of mode names (no spaces).

Example: `/exclusivemodeseries speed,control`

## `/setblocksetting <setting> <value> <block,...>`
Sets a per-block setting with a specified value. `<block,...>` is a comma-separated list of block IDs (no spaces).

Example: `/setblocksetting FRICTION 0.8 minecraft:ice,minecraft:packed_ice`

## `/collisionmode <id>`
Sets the collision mode by its numeric ID.

Example: `/collisionmode 1`

## `/stepwhilefalling <enabled>`
Enables or disables stepping up blocks while falling.

Example: `/stepwhilefalling true`

## `/setinterpolationten <enabled>`
Enables or disables interpolation compatibility mode.

Example: `/setinterpolationten true`

## `/setcollisionresolution <resolution>`
Sets the collision resolution level.

Example: `/setcollisionresolution 10`

## `/clearcollisionfilter`
Clears all entity type collision filters.

Example: `/clearcollisionfilter`

## `/addcollisionfilter <entitytypes>`
Adds entity types to the collision filter. `<entitytypes>` is a comma-separated list (no spaces).

Example: `/addcollisionfilter minecraft:zombie,minecraft:item`