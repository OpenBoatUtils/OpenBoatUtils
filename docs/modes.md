---
outline: deep
---

# Modes
Modes are like presets that apply multiple settings at once.
- Modes stack when applied sequentially.
- The last applied mode takes priority given conflicts.
  To apply only one mode, reset first or use `/exclusivemode`.

You might want to consider using [stored contexts](/developers/context) over modes. 

## `RALLY` `8`
| Setting | Value |
|---------|-------|
| Default Slipperiness | 0.98 |
| Fall Damage | false |
| Air Control | true |
| Step Size | 1.25 |
## `RALLY_BLUE` `9`
| Setting | Value |
|---------|-------|
| Default Slipperiness | 0.989 |
| Fall Damage | false |
| Air Control | true |
| Step Size | 1.25 |
## `BA_NOFD` `10`
| Setting | Value |
|---------|-------|
| Fall Damage | false |
| Air Control | true |
| Air Slipperiness | 0.98 |
| Step Size | 1.25 |
| Water Elevation | true |
## `PARKOUR` `11`
| Setting | Value |
|---------|-------|
| Fall Damage | false |
| Air Control | true |
| Jump Force | 0.36 |
| Step Size | 0.5 |
| Default Slipperiness | 0.98 |
## `BA_BLUE_NOFD` `12`
| Setting | Value |
|---------|-------|
| Fall Damage | false |
| Air Control | true |
| Air Slipperiness | 0.989 |
| Step Size | 1.25 |
| Water Elevation | true |
## `PARKOUR_BLUE` `13`
| Setting | Value |
|---------|-------|
| Fall Damage | false |
| Air Control | true |
| Jump Force | 0.36 |
| Step Size | 0.5 |
| Default Slipperiness | 0.989 |
## `BA` `14`
| Setting | Value |
|---------|-------|
| Air Control | true |
| Air Slipperiness | 0.98 |
| Step Size | 1.25 |
| Water Elevation | true |
## `BA_BLUE` `15`
| Setting | Value |
|---------|-------|
| Air Control | true |
| Air Slipperiness | 0.989 |
| Step Size | 1.25 |
| Water Elevation | true |
## `JUMP_BLOCKS` `16`
Sets per-block jump forces for specific concrete colours.
| Block | Jump Force |
|-------|-----------|
| Orange Concrete | 0.36 |
| Black Concrete | 0.0 |
| Green Concrete | 0.5 |
| Yellow Concrete | 0.18 |
## `BOOSTER_BLOCKS` `17`
Sets per-block acceleration for specific glazed terracotta blocks.
| Block | Setting | Value |
|-------|---------|-------|
| Magenta Glazed Terracotta | Forwards Accel | 0.08 |
| Light Gray Glazed Terracotta | Yaw Accel | 0.08 |
## `DEFAULT_ICE` `18`
| Setting | Value |
|---------|-------|
| Default Slipperiness | 0.98 |
## `DEFAULT_NINE_EIGHT_FIVE` `19`
| Setting | Value |
|---------|-------|
| Default Slipperiness | 0.985 |
## `NOCOL_BOATS_AND_PLAYERS` `20`
| Setting | Value |
|---------|-------|
| Collision Mode | No Boats or Players |
## `NOCOL_ALL_ENTITIES` `21`
| Setting | Value |
|---------|-------|
| Collision Mode | No Entities |
## `BA_JANKLESS` `22`
| Setting | Value |
|---------|-------|
| Step While Falling | true |
| Air Control | true |
| Air Slipperiness | 0.98 |
| Step Size | 1.25 |
| Water Elevation | true |
## `BA_BLUE_JANKLESS` `23`
| Setting | Value |
|---------|-------|
| Step While Falling | true |
| Air Control | true |
| Air Slipperiness | 0.989 |
| Step Size | 1.25 |
| Water Elevation | true |
## `DEFAULT_BLUE_ICE` `24`
| Setting | Value |
|---------|-------|
| Default Slipperiness | 0.989 |

---

## Broken Slime
::: warning
Due to a typo, slime blocks had default slipperiness (rather than 0.8) until 0.3.1
The modes that existed before `0.3.1` have been given this "broken slime" and deprecated so that existing uses of these mode ID's aren't changed. the modes above should be used in place of these, they only exist for compatibility.
### `BROKEN_SLIME_RALLY` `0`
| Setting | Value |
|---------|-------|
| Default Slipperiness | 0.98 |
| Fall Damage | false |
| Air Control | true |
| Step Size | 1.25 |
### `BROKEN_SLIME_RALLY_BLUE` `1`
| Setting | Value |
|---------|-------|
| Default Slipperiness | 0.989 |
| Fall Damage | false |
| Air Control | true |
| Step Size | 1.25 |
### `BROKEN_SLIME_BA_NOFD` `2`
| Setting | Value |
|---------|-------|
| Fall Damage | false |
| Air Control | true |
| Air Slipperiness | 0.98 |
| Step Size | 1.25 |
| Water Elevation | true |
### `BROKEN_SLIME_PARKOUR` `3`
| Setting | Value |
|---------|-------|
| Fall Damage | false |
| Air Control | true |
| Jump Force | 0.36 |
| Step Size | 0.5 |
| Default Slipperiness | 0.98 |
### `BROKEN_SLIME_BA_BLUE_NOFD` `4`
| Setting | Value |
|---------|-------|
| Fall Damage | false |
| Air Control | true |
| Air Slipperiness | 0.989 |
| Step Size | 1.25 |
| Water Elevation | true |
### `BROKEN_SLIME_PARKOUR_BLUE` `5`
| Setting | Value |
|---------|-------|
| Fall Damage | false |
| Air Control | true |
| Jump Force | 0.36 |
| Step Size | 0.5 |
| Default Slipperiness | 0.989 |
### `BROKEN_SLIME_BA` `6`
| Setting | Value |
|---------|-------|
| Air Control | true |
| Air Slipperiness | 0.98 |
| Step Size | 1.25 |
| Water Elevation | true |
### `BROKEN_SLIME_BA_BLUE` `7`
| Setting | Value |
|---------|-------|
| Air Control | true |
| Air Slipperiness | 0.989 |
| Step Size | 1.25 |
| Water Elevation | true |
:::