---
outline: deep
---

# Sync Packets <Badge type="tip" text="^0.5.2" />
Plugin Channel Identifier: `openboatutils:sync`

A ridden boat is only simulated on its driver's client. Everyone else sees it where the server last heard it was,
which is usually 0.3 to 0.6 seconds behind, so boats collide with a ghost of where the other boat used to be.

With [Boat Sync](/developers/settings#set-boat-sync) enabled the driver sends its boat's state every tick (or every
few ticks, as the setting's interval says) and the server relays it to the other players. Each client estimates how old
the state is and predicts the boat forward to the present, stopping it at blocks. Other entities are not part of the prediction.

Only Minecraft `26.1+` clients send or use these packets. Nothing is sent unless the active context has boat sync enabled (an interval above `0`).

## Relaying
The server does not need to understand the packets. For every serverbound sync packet it receives:
- check the length (`75` bytes for State, `14` for Probe) and drop anything else
- take the entity id of the sender's vehicle, only if the sender is its controlling passenger, never from the payload
- send the players who can see the boat (the ones tracking that entity, other than the sender, that have registered `openboatutils:sync`) the entity id as an `int` followed by the packet's bytes unchanged

Relay as soon as you can. Holding packets until the next server tick still works (probes measure the hold away)
but adds up to one tick of delay.

Rate limit each sender (OpenBoatUtils drops anything past 40 sync packets a second, a driver sends about 28 at an interval of 1),
and always take the entity id from the sender's own vehicle, never from the payload.
A server may also drop states whose position is far from the boat's server position.

OpenBoatUtils itself relays this way when it is installed on the server (singleplayer and LAN included).

## Timing
There is no shared clock. Every packet carries the sender's `System.nanoTime()`, and the receiver keeps the minimum of
`receive time - stamp` over the last 10 seconds. A state's age is then `(now - stamp) - minimum + sender half RTT + receiver half RTT`.

Each client measures its round trip time to the server with the vanilla ping request packet once a second, half of the
smallest of the last 10 results is sent along with every packet.

Probes are sent about 8 times a second between ticks, their offset inside the tick stepping by the golden ratio so every
phase of the tick gets covered. Client and server ticks run in lock step, so states alone could all carry the same hold from
the server and the minimum would never see past it.

Receivers take the receive time when the packet comes off the network, not when the client thread handles it a frame later.

## Serverbound
### State
Sent at the end of every tick of the boat the player is driving, or every `N`th tick for a boat sync interval of `N`.

`Friction` is the friction applied to velocity and yaw rate this tick. `Yaw Accel` is the yaw rate change the player's
turn input made this tick and `Acceleration` the velocity change their input made this tick. Both are assumed to stay held
while predicting, the acceleration rotating with the boat.

Receivers drop a state with any non-finite field, a velocity component over `100`, an acceleration component over `10`,
a friction outside `0` to `2`, or a yaw rate or yaw accel over `1000`.

| Packet ID   | Stamp                               | Half RTT     | X        | Y        | Z        | Velocity X | Velocity Y | Velocity Z | Yaw     | Yaw Rate                   | Yaw Accel                  | Friction | Acceleration X | Acceleration Z | Flags                  |
|-------------|-------------------------------------|--------------|----------|----------|----------|------------|------------|------------|---------|----------------------------|----------------------------|----------|----------------|----------------|------------------------|
| 0 (`short`) | `long` (sender `System.nanoTime()`) | `float` (ms) | `double` | `double` | `double` | `float`    | `float`    | `float`    | `float` | `float` (degrees per tick) | `float` (degrees per tick) | `float`  | `float`        | `float`        | `byte` (reserved, `0`) |

***

### Probe
Sent about 8 times a second between ticks while driving, only used for timing.

| Packet ID   | Stamp                               | Half RTT     |
|-------------|-------------------------------------|--------------|
| 1 (`short`) | `long` (sender `System.nanoTime()`) | `float` (ms) |

***

## Clientbound
### Relayed
A serverbound sync packet from another player, prefixed with the entity id of that player's boat.

| Vehicle Entity ID | Packet                                       |
|-------------------|----------------------------------------------|
| `int`             | the serverbound State or Probe, byte for byte |
