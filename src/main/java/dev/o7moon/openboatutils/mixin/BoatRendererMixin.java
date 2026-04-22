package dev.o7moon.openboatutils.mixin;

//? >= 1.21.9 {
/*import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
*///? }
import dev.o7moon.openboatutils.ISettingContext;
import dev.o7moon.openboatutils.OpenBoatUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
//? >= 1.21.3 {
/*import net.minecraft.client.renderer.entity.AbstractBoatRenderer;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import dev.o7moon.openboatutils.ScaledBoatRenderState;
*///? } else {
import net.minecraft.client.renderer.entity.BoatRenderer;
//? }
import net.minecraft.world.entity.vehicle.Boat;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? >= 1.21.3 {
/*@Mixin(AbstractBoatRenderer.class)
*///? } else {
@Mixin(BoatRenderer.class)
//? }
public abstract class BoatRendererMixin {

    //? >= 1.21.3 {
    /*@Unique private float openBoatUtils$getScale(Boat boat) {
    *///? } else {
    @Unique private float openBoatUtils$getScale(Boat boat) {
    //? }
        @Nullable ISettingContext boatContext = OpenBoatUtils.instance.getEntityContext(boat.getUUID());

        if (boatContext != null) {
            return boatContext.getScale();
        }

        @Nullable ISettingContext context = OpenBoatUtils.instance.getActiveContext();

        if (context != null) {
            return context.getScale();
        }

        return 1f;
    }

    //? >= 1.21.9 {
    /*@Inject(method = "submit*", at = @At("HEAD"))
    private void preRender(BoatRenderState state, PoseStack matrices, SubmitNodeCollector renderCommandQueue, CameraRenderState cameraRenderState, CallbackInfo ci) {
        float scale = ((ScaledBoatRenderState) state).openBoatUtils$getScale();
        matrices.pushPose();

        if (scale < 0) {
            matrices.translate(0, 0.5625 * -scale, 0);
        }

        matrices.scale(scale, scale, scale);

        if (scale < 0) {
            matrices.translate(0, 0.5625 * scale, 0);
        }
    }

    @Inject(method = "submit*", at = @At("RETURN"))
    private void postRender(BoatRenderState state, PoseStack matrices, SubmitNodeCollector renderCommandQueue, CameraRenderState cameraRenderState, CallbackInfo ci) {
        matrices.popPose();
    }
    *///? } else if >= 1.21.3 {
    /*@Inject(method = "render(Lnet/minecraft/client/renderer/entity/state/BoatRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))
    private void preRender(BoatRenderState state, PoseStack matrices, MultiBufferSource vertexConsumerProvider, int i, CallbackInfo ci) {
        float scale = ((ScaledBoatRenderState) state).openBoatUtils$getScale();
        matrices.pushPose();

        if (scale < 0) {
            matrices.translate(0, 0.5625 * -scale, 0);
        }

        matrices.scale(scale, scale, scale);
    }

    @Inject(method = "render(Lnet/minecraft/client/renderer/entity/state/BoatRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("RETURN"))
    private void postRender(BoatRenderState state, PoseStack matrices, MultiBufferSource vertexConsumerProvider, int i, CallbackInfo ci) {
        matrices.popPose();
    }
    *///? } else {
    @Inject(method = "render(Lnet/minecraft/world/entity/vehicle/Boat;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))
    private void preRender(Boat boatEntity, float f, float g, PoseStack matrices, MultiBufferSource vertexConsumerProvider, int i, CallbackInfo ci) {
        float scale = openBoatUtils$getScale(boatEntity);
        matrices.pushPose();

        if (scale < 0) {
            matrices.translate(0, 0.5625 * -scale, 0);
        }

        matrices.scale(scale, scale, scale);
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/vehicle/Boat;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("RETURN"))
    private void postRender(Boat boatEntity, float f, float g, PoseStack matrices, MultiBufferSource vertexConsumerProvider, int i, CallbackInfo ci) {
        matrices.popPose();
    }
    //? }

    //? >= 1.21.3 {
    /*@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/vehicle/Boat;Lnet/minecraft/client/renderer/entity/state/BoatRenderState;F)V", at = @At("RETURN"))
    private void populateScale(Boat entity, BoatRenderState state,
                               float tickDelta, CallbackInfo ci) {

        ((ScaledBoatRenderState) state).openBoatUtils$setScale(
                openBoatUtils$getScale(entity)
        );
    }
    *///? }
}