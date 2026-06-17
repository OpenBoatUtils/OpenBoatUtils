# Versions

The client will send the server a packet letting it know which version of OpenBoatUtils it's running. This is mostly complete table of the version IDs of each version.

Versions `< 0.1.2` do not send a version packet.

Make sure to reject builds marked as such, bugged builds may have limited functionality and you may consider blocking them.

| ID  | Version | State  | Notes                                                                                                    |
|-----|:-------:|--------|----------------------------------------------------------------------------------------------------------|
| 22  | 5.1.0c  |        | fixed [Bugged scale with entity contexts](https://github.com/OpenBoatUtils/OpenBoatUtils/pull/23).       |
| 21  | 5.1.0b  | Bugged | includes a critical bug fix                                                                              |
| 20  |  5.1.0  | Reject | "Last" 1.21.1 version, [changelist](https://github.com/OpenBoatUtils/OpenBoatUtils/milestone/2?closed=1) |
| 19  |  5.0.0  |        | Context Rewrite                                                                                          |
| ... |   ...   |        |                                                                                                          |
| 16  |  0.4.8  |        | 1.21.3 patch which fixes an interpolation bug in the previous                                            |
| 15  |  0.4.7  | Bugged |                                                                                                          |
| 14  |  0.4.6  |        |                                                                                                          |
| 13  |  0.4.5  |        | has the fix for the previous, only exists on 1.21.3                                                      |
| 12  |  0.4.4  | Reject | broken 1.21.3 build, reject this version if possible                                                     |
| 11  |  0.4.4  |        |                                                                                                          |
| 10  |  0.4.3  |        |                                                                                                          |
| 9   |  0.4.2  |        |                                                                                                          |
| 8   |  0.4.2  | Reject | build with broken aircontrol, reject this version if possible                                            |
| 6   |   0.4   |        |                                                                                                          |
| 5   |  0.3.1  |        |                                                                                                          |
| 4   |   0.3   |        |                                                                                                          |
| 3   |  0.2.2  |        |                                                                                                          |
| 2   |  0.2.1  |        |                                                                                                          |
| 1   |   0.2   |        |                                                                                                          |
| 0   |  0.1.2  |        |                                                                                                          |