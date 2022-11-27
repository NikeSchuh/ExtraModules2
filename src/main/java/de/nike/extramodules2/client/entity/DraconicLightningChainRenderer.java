package de.nike.extramodules2.client.entity;

import codechicken.lib.vec.Vector3;
import com.brandon3055.brandonscore.api.TimeKeeper;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import de.nike.extramodules2.entity.projectiles.DraconicLightningChainEntity;
import de.nike.extramodules2.utils.NikesMath;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.*;
import org.lwjgl.system.CallbackI;

import java.awt.*;
import java.util.Random;

public class DraconicLightningChainRenderer extends EntityRenderer<DraconicLightningChainEntity> {


    public DraconicLightningChainRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }
    public void render(DraconicLightningChainEntity lightningChain, float entityYaw, float partialTicks, MatrixStack mStack, IRenderTypeBuffer getter, int packedLightIn) {
        super.render(lightningChain, entityYaw, partialTicks, mStack, getter, packedLightIn);
        if(lightningChain.getTarget() == null) return;
        if(lightningChain.getOrigin() == null) return;

        Vector3d startPosRaw = new Vector3d(0, lightningChain.getLightningSize(), 0).add(0, lightningChain.getTarget().getEyeHeight(), 0);
        Vector3d endPosRaw = new Vector3d(0, lightningChain.getLightningSize() * lightningChain.getProgression(), 0).add(0, lightningChain.getTarget().getEyeHeight(), 0);;
        Vector3d targetChainOffset = lightningChain.getTarget().position().subtract(lightningChain.position());
        Vector3d startPos = targetChainOffset.add(startPosRaw);
        Vector3d endPos = targetChainOffset.add(endPosRaw);

        int segCount = lightningChain.getLightningSegments();
        long randSeed = (TimeKeeper.getClientTick() / 2);
        float scaleMod = 2f;
        float deflectMod = 1f;
        boolean autoScale = true;
        float segTaper = 0.125F;
        int colour = lightningChain.getLightningColor();
        mStack.pushPose();
        rendeArcP2P(mStack, getter, startPos ,endPos ,segCount ,randSeed ,scaleMod ,deflectMod ,autoScale ,segTaper ,colour);
        mStack.popPose();
    }


    public void drawVertex(Matrix4f matrix, Matrix3f normals, IVertexBuilder vertexBuilder, int offsetX, int offsetY, int offsetZ, float textureX, float textureY, int p_229039_9_, int p_229039_10_, int p_229039_11_, int packedLightIn) {
        vertexBuilder.vertex(matrix, (float) offsetX, (float) offsetY, (float) offsetZ).color(255, 255, 255, 255).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(normals, (float) p_229039_9_, (float) p_229039_11_, (float) p_229039_10_).endVertex();
    }

    public ResourceLocation getTextureLocation(DraconicLightningChainEntity entity) {
        return new ResourceLocation("");
    }

    public static void rendeArcP2P(MatrixStack mStack, IRenderTypeBuffer getter, Vector3d startPos, Vector3d endPos, int segCount, long randSeed, float scaleMod, float deflectMod, boolean autoScale, float segTaper, int colour) {

        double height = endPos.y - startPos.y;
        float relScale = autoScale ? (float) height / 128F : 1F; //A scale value calculated by comparing the bolt height to that of vanilla lightning
        float segHeight = (float) height / segCount;
        float[] segXOffset = new float[segCount + 1];
        float[] segZOffset = new float[segCount + 1];
        float xOffSum = 0;
        float zOffSum = 0;

        Random random = new Random(randSeed);
        for (int segment = 0; segment < segCount + 1; segment++) {
            segXOffset[segment] = xOffSum + (float) startPos.x;
            segZOffset[segment] = zOffSum + (float) startPos.z;
            //Figure out what the total offset will be so we can subtract it at the start in order to end up in the correct spot at the end.
            if (segment < segCount) {
                xOffSum += (5 - (random.nextFloat() * 10)) * relScale * deflectMod;
                zOffSum += (5 - (random.nextFloat() * 10)) * relScale * deflectMod;
            }
        }

        xOffSum -= (float) (endPos.x - startPos.x);
        zOffSum -= (float) (endPos.z - startPos.z);

        IVertexBuilder builder = getter.getBuffer(RenderType.lightning());
        Matrix4f matrix4f = mStack.last().pose();

        for (int layer = 0; layer < 4; ++layer) {
            float red = ((colour >> 16) & 0xFF) / 255F;
            float green = ((colour >> 8) & 0xFF) / 255F;
            float blue = (colour & 0xFF) / 255F;
            float alpha = 0.3F;
            if (layer == 0) {
                red = green = blue = alpha = 1;
            }

            for (int seg = 0; seg < segCount; seg++) {
                float pos = seg / (float)(segCount);
                float x = segXOffset[seg] - (xOffSum * pos);
                float z = segZOffset[seg] - (zOffSum * pos);

                float nextPos = (seg + 1) / (float)(segCount);
                float nextX = segXOffset[seg+1] - (xOffSum * nextPos);
                float nextZ = segZOffset[seg+1] - (zOffSum * nextPos);

                //The size of each shell
                float layerOffsetA = (0.1F + (layer * 0.2F * (1F + segTaper))) * relScale * scaleMod;
                float layerOffsetB = (0.1F + (layer * 0.2F * (1F - segTaper))) * relScale * scaleMod;

                addSegmentQuad(matrix4f, builder, x, (float) startPos.y, z, seg, nextX, nextZ, red, green, blue, alpha, layerOffsetA, layerOffsetB, false, false, true, false, segHeight);    //North Side
                addSegmentQuad(matrix4f, builder, x, (float) startPos.y, z, seg, nextX, nextZ, red, green, blue, alpha, layerOffsetA, layerOffsetB, true, false, true, true, segHeight);      //East Side
                addSegmentQuad(matrix4f, builder, x, (float) startPos.y, z, seg, nextX, nextZ, red, green, blue, alpha, layerOffsetA, layerOffsetB, true, true, false, true, segHeight);      //South Side
                addSegmentQuad(matrix4f, builder, x, (float) startPos.y, z, seg, nextX, nextZ, red, green, blue, alpha, layerOffsetA, layerOffsetB, false, true, false, false, segHeight);    //West Side
            }
        }
    }

    private static void addSegmentQuad(Matrix4f matrix4f, IVertexBuilder builder, float x1, float yOffset, float z1, int segIndex, float x2, float z2, float red, float green, float blue, float alpha, float offsetA, float offsetB, boolean invA, boolean invB, boolean invC, boolean invD, float segHeight) {
        builder.vertex(matrix4f, x1 + (invA ? offsetB : -offsetB), yOffset + segIndex * segHeight, z1 + (invB ? offsetB : -offsetB)).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix4f, x2 + (invA ? offsetA : -offsetA), yOffset + (segIndex + 1F) * segHeight, z2 + (invB ? offsetA : -offsetA)).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix4f, x2 + (invC ? offsetA : -offsetA), yOffset + (segIndex + 1F) * segHeight, z2 + (invD ? offsetA : -offsetA)).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix4f, x1 + (invC ? offsetB : -offsetB), yOffset + segIndex * segHeight, z1 + (invD ? offsetB : -offsetB)).color(red, green, blue, alpha).endVertex();
    }

}