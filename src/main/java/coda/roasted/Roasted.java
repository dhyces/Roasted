package coda.roasted;

import coda.roasted.crafting.SmoreRecipe;
import coda.roasted.registry.RoastedItems;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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

        bus.addGenericListener(RecipeSerializer.class, (RegistryEvent.Register<RecipeSerializer<?>> e) -> e.getRegistry().register(new SmoreRecipe.Serializer()));

        bus.addListener(this::registerClient);
    }

    private void registerClient(FMLClientSetupEvent event) {
        CALLBACKS.forEach(Runnable::run);
        CALLBACKS.clear();
    }
}
