package com.example.event;

import com.example.ExampleMod;
import com.google.common.eventbus.Subscribe;
import moddedmite.rustedironcore.api.event.Handlers;
import moddedmite.rustedironcore.api.event.listener.IInitializationListener;
import net.minecraft.Minecraft;
import net.xiaoyu233.fml.reload.event.MITEEvents;
import net.xiaoyu233.fml.reload.event.SoundsRegisterEvent;

// register fish & RIC events
public class ExampleEvent extends Handlers {
    // Reference net.xiaoyu233.fml.reload.event for fish events
    // Notice: Please try to avoid using Fish Event, as Fish Event will be removed in FishModLoader v4.
    @Subscribe
    public void onSoundsRegister(SoundsRegisterEvent event) {
    }

    // Reference moddedmite.rustedironcore.api.event.Handlers for RIC events
    public static void register() {
        MITEEvents.MITE_EVENT_BUS.register(new ExampleEvent());

        Handlers.Initialization.register(new IInitializationListener() {
            @Override
            public void onClientStarted(Minecraft client) {
                ExampleMod.LOGGER.info("Hello events!");
            }
        });
    }
}
