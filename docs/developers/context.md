---
outline: deep
---

# Context Packets
Plugin Channel Identifier: `openboatutils:context`

Contexts come in three flavours:
- Default Context, activated by any `openboatutils:settings` packet and resets to vannila whenever it is switched to.
- Stored Context, a named persistent context that can be dynamically switched to that stores any settings sent to it.
- Entity Context, applies to a specific entity uuid, overriding any other contexts that could be active.

By default openboatutils has no context active (***`null` context***), which effectively disables every internal hook used, sending a reset packet to the default context will switch to the `null` context as a safeguard.

## Clientbound

### Reset Context
Resets the currently active context back to `null`.

This effectively disables openboatutils and returns behavior to vanilla.

| Packet ID |
|-----------|
| 0 (`short`) |

***

### Switch Context
Switches the active context to a stored context identified by its Identifier.

If the identifier belongs to the `openboatutils` namespace, only the `default` context is allowed, the rest are reserved for future use.

| Packet ID | Identifier |
|-----------|------------|
| 1 (`short`) | `string` |

***

### Drop Context
Removes a stored context from the client.

If the removed context is currently active, the active context is also reset to `null`.

| Packet ID | Identifier |
|-----------|------------|
| 2 (`short`) | `string` |

***

### Store Context
Creates or overwrites a stored context with the given identifier, and applies a batch of settings to it.

The settings are read as a [Transaction](developers/settings.html#transaction) minus the transaction packet id `short`.

| Packet ID | Identifier | Settings |
|-----------|------------|----------|
| 3 (`short`) | `string` | `transaction` |

***

### Entity Context
Creates or updates a context tied to a specific entity (by UUID).

Like `Store Context`, the settings are read as a [Transaction](developers/settings.html#transaction) minus the transaction packet id `short`.

| Packet ID | Entity UUID | Settings |
|-----------|-------------|----------|
| 4 (`short`) | `string (UUID)` | `transaction` |
