package clanker.craft.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import clanker.craft.ClankerCraft;

public record ReloadResourcesS2CPayload() implements CustomPayload {
    public static final Id<ReloadResourcesS2CPayload> ID = new Id<>(Identifier.of(ClankerCraft.MOD_ID, "reload_resources"));
    public static final PacketCodec<RegistryByteBuf, ReloadResourcesS2CPayload> CODEC = PacketCodec.of(
            (value, buf) -> {}, // no data to encode
            buf -> new ReloadResourcesS2CPayload() // no data to decode
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
