package coda.roasted;

import coda.roasted.registry.RoastedItems;
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

        bus.addListener(this::registerClient);
    }

    private void registerClient(FMLClientSetupEvent event) {
        CALLBACKS.forEach(Runnable::run);
        CALLBACKS.clear();
    }
}
