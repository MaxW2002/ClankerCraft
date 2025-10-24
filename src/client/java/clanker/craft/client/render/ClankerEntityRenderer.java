package clanker.craft.client.render;

import clanker.craft.ClankerCraft;
import clanker.craft.entity.ClankerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.CopperGolemEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.state.CopperGolemEntityRenderState;
import net.minecraft.util.Identifier;

/**
 * Custom renderer for the Clanker entity.
 * Uses the Copper Golem model but with a custom texture.
 */
public class ClankerEntityRenderer extends MobEntityRenderer<ClankerEntity, CopperGolemEntityRenderState, CopperGolemEntityModel> {
    
    private static final Identifier TEXTURE = Identifier.of(ClankerCraft.MOD_ID, "textures/entity/clanker.png");

    public ClankerEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new CopperGolemEntityModel(context.getPart(EntityModelLayers.COPPER_GOLEM)), 0.5f);
    }

    @Override
    public Identifier getTexture(CopperGolemEntityRenderState state) {
        return TEXTURE;
    }
    
    @Override
    public CopperGolemEntityRenderState createRenderState() {
        return new CopperGolemEntityRenderState();
    }
}
