package dev.o7moon.openboatutils.mixin;

//? >= 26.1 {
/*import dev.o7moon.openboatutils.sync.BoatSyncClient;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerSyncMixin {
    // handlePongResponse runs on the network thread, the server answers pings there too
    @Inject(method = "handlePongResponse", at = @At("HEAD"), cancellable = true)
    private void openboatutils$syncPong(ClientboundPongResponsePacket packet, CallbackInfo ci) {
        if (BoatSyncClient.onPong(packet.time(), System.nanoTime())) ci.cancel();
    }
}
*///? }
