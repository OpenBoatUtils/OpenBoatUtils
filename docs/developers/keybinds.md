---
outline: deep
---

# Keybind Packets
Plugin Channel Identifier: `openboatutils:keybinds`

Lets a server define its own keybinds that the client surfaces in the vanilla controls menu. When a
defined key is pressed or released the client sends an event back to the server, and can optionally
switch an OpenBoatUtils context instantly (with no server round trip) so things like gear shifting
or a drift toggle feel responsive while the server stays authoritative.

The whole api is optional. A server that never sends a `Define Keybinds` packet behaves exactly like
before.

## Persistence
The server only ever defines keybinds; the client owns and persists the player's chosen key
**mappings**, per server, in `config/openboatutils-keybinds.json`:

```json
{
  "play.whatever.net:25565": {
    "obu_drift": "key.keyboard.left.shift",
    "gear_up": "key.keyboard.r"
  }
}
```

- Mappings are keyed by the server address (`singleplayer` for an integrated server).
- A keybind the player never rebound is not written; only their customisations are stored.
- When a `Define Keybinds` packet arrives, mappings for ids the server no longer defines are
  pruned, while customisations for ids that still exist are kept (the server's default key never
  overwrites a player's choice).

## Clientbound
### Define Keybinds
Replaces the client's current set of OpenBoatUtils keybinds with the ones listed.

Each keybind has a unique `id` (echoed back in `Keybind Event`), a `label` shown in the controls
menu, a `default key` as an [`InputUtil` translation key](https://minecraft.wiki/w/Controls)
(e.g. `key.keyboard.g`, `key.mouse.right`), and a client side action for press and for release.

| Packet ID   | Count       | Keybinds                |
|-------------|-------------|-------------------------|
| 0 (`short`) | `int`       | `count` × `keybind`     |

Each `keybind` is:

| id         | label      | default key | on press | on release |
|------------|------------|-------------|----------|------------|
| `string`   | `string`   | `string`    | `action` | `action`   |

Each `action` tells the client what to do locally the instant the key changes state (the server
fully defines this; the client only runs what it is told):

| action type   | value      | extra        |
|---------------|------------|--------------|
| 0 (`byte`)    | none       | —            |
| 1 (`byte`)    | switch ctx | `string` context identifier (`namespaced:key`) |
| 2 (`byte`)    | reset ctx  | —            |

`switch ctx` switches to a [stored context](/developers/context#store-context) (which the server
should have already sent), `reset ctx` returns to the `null` context (vanilla behaviour).

***

### Clear Keybinds
Removes every OpenBoatUtils keybind from the client.

| Packet ID   |
|-------------|
| 1 (`short`) |

## Serverbound
### Keybind Event
Sent whenever a defined key is pressed or released.

`tick` is the client's world time at the moment of the event, intended for sync / anti-cheat.

| Packet ID   | id       | pressed   | tick     |
|-------------|----------|-----------|----------|
| 0 (`short`) | `string` | `boolean` | `long`   |
