---
outline: deep
---

# Transactions
<Badge type="tip" text="^0.5.1" />
Transactions provide a way for servers to be sure that a client has received and processed a given packet.

You can send a transaction by prepending a transaction header to any packet sent to the client.

Tip: `32767` is `Short.MAX_VALUE`

## Serverbound

### Transaction Response <Badge type="tip" text="^0.5.1" />
Sent by the client after processing an incoming transaction.
| Packet ID       | Transaction ID |
|-----------------|----------------|
| 32767 (`short`) | `int`          |

## Clientbound

### Transaction Wrapper <Badge type="tip" text="^0.5.1" />
Wraps a given packet and returns a server bound response on the same channel with the same packet id `32767`.
| Packet ID       | Transaction ID | Payload            |
|-----------------|----------------| ------------------ |
| 32767 (`short`) | `int`          | Any Packet Payload |
