---
outline: deep
---
# Configuration Packets
Plugin Channel Identifier: `openboatutils:configuration`

## Serverbound
### Version <Badge type="tip" text="^0.5.0" />
A direct mirror of [the `openboatutils:settings` version packet](/developers/settings#version), except in the configuration phase. It is intended to be used to determine if a client has openboatutils installed before entering the play phase.

This packet was introduced in 0.5.0 and will always contain the unstable flag, it is not sent before this version.

| Packet ID   | Version                                    | Unstable  |
|-------------|--------------------------------------------|-----------|
| 0 (`short`) | [Version ID](/developers/versions) (`int`) | `boolean` |

