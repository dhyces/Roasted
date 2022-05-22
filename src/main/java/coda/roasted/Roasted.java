package coda.roasted;

import coda.roasted.crafting.RoastednessIngredient;
import coda.roasted.registry.RoastedItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.ArrayList;
import java.util.List;

@Mod(Roasted.MOD_ID)
public class Roasted {
    public static final String MOD_ID = "roasted";
    public static final List<Runnable> CALLBACKS = new ArrayList<>();

    public Roasted() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        RoastedItems.ITEMS.register(bus);

        bus.addListener(this::init);
        bus.addListener(this::registerClient);
    }

    private void init(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> CraftingHelper.register(
                new ResourceLocation(MOD_ID, "roasted"),
                RoastednessIngredient.Serializer.INSTANCE
        ));
    }

    private void registerClient(FMLClientSetupEvent event) {
        CALLBACKS.forEach(Runnable::run);
        CALLBACKS.clear();
    }
}
