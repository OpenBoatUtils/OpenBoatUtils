# Serverbound
Packets sent to the server.

## Version
Sends the server the version of OpenBoatUtils we are currently running. This is sent once, on join.

As of `5.0.0` an unstable flag is also sent after the first short.
It is intended to block use of potentially broken development builds without
having to impact the version Ids. ***probably reject builds of these for most people***

Take care to only read the unstable flag when the version id is `>= 19`.

**Payload:**
Packet ID|Version|Unstable (as of 5.0.0)|
-|-|-|
0 (`short`)|[Version ID](https://github.com/o7Moon/OpenBoatUtils/wiki/Version-IDs) (`int`)|`boolean`|
***