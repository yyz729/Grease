package com.yyz.client;

import com.yyz.event.ModEvents;
import net.fabricmc.api.ClientModInitializer;

public class GreaseClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModEvents.registerClient();
    }
}
