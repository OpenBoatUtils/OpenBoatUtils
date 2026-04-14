//~ !boat_entity
package dev.o7moon.openboatutils.mixin;

import dev.o7moon.openboatutils.ISettingContext;
import dev.o7moon.openboatutils.OpenBoatUtils;
//? >= 1.21.9 {
/*import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
*///? }
import net.minecraft.client.render.VertexConsumerProvider;
//? >= 1.21.3 {
/*import net.minecraft.client.render.entity.AbstractBoatEntityRenderer;
*///? } else {
import net.minecraft.client.render.entity.BoatEntityRenderer;
//? }
//? >= 1.21.3 {
/*import net.minecraft.client.render.entity.state.BoatEntityRenderState;
import dev.o7moon.openboatutils.ScaledBoatRenderState;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
*///? } else {
import net.minecraft.entity.vehicle.BoatEntity;
//? }
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? >= 1.21.3 {
/*@Mixin(AbstractBoatEntityRenderer.class)
*///? } else {
@Mixin(BoatEntityRenderer.class)
//? }
public abstract class BoatRendererMixin {

    //? >= 1.21.3 {
    /*@Unique private float openBoatUtils$getScale(AbstractBoatEntity boat) {
    *///? } else {
    @Unique private float openBoatUtils$getScale(BoatEntity boat) {
    //? }
        @Nullable ISettingContext boatContext = OpenBoatUtils.instance.getEntityContext(boat.getUuid());

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
    /*@Inject(method = "render*", at = @At("HEAD"))
    private void preRender(BoatEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue renderCommandQueue, CameraRenderState cameraRenderState, CallbackInfo ci) {
        float scale = ((ScaledBoatRenderState) state).openBoatUtils$getScale();
        matrices.push();
        matrices.scale(scale, scale, scale);
    }

    @Inject(method = "render*", at = @At("RETURN"))
    private void postRender(BoatEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue renderCommandQueue, CameraRenderState cameraRenderState, CallbackInfo ci) {
        matrices.pop();
    }
    *///? } else if >= 1.21.3 {
    /*@Inject(method = "render(Lnet/minecraft/client/render/entity/state/BoatEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"))
    private void preRender(BoatEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        float scale = ((ScaledBoatRenderState) state).openBoatUtils$getScale();
        matrices.push();
        matrices.scale(scale, scale, scale);
    }

    @Inject(method = "render(Lnet/minecraft/client/render/entity/state/BoatEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("RETURN"))
    private void postRender(BoatEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        matrices.pop();
    }
    *///? } else {
    @Inject(method = "render(Lnet/minecraft/entity/vehicle/BoatEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"))
    private void preRender(BoatEntity boatEntity, float f, float g, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        float scale = openBoatUtils$getScale(boatEntity);
        matrices.push();
        matrices.scale(scale, scale, scale);
    }

    @Inject(method = "render(Lnet/minecraft/entity/vehicle/BoatEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("RETURN"))
    private void postRender(BoatEntity boatEntity, float f, float g, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        matrices.pop();
    }
    //? }

    //? >= 1.21.3 {
    /*@Inject(method = "updateRenderState*", at = @At("RETURN"))
    private void populateScale(AbstractBoatEntity entity, BoatEntityRenderState state,
                               float tickDelta, CallbackInfo ci) {

        ((ScaledBoatRenderState) state).openBoatUtils$setScale(
                openBoatUtils$getScale(entity)
        );
    }
    *///? }
}