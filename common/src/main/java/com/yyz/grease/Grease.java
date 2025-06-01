package com.yyz.grease;

import com.yyz.grease.config.GreaseConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.io.File;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Grease{
    public static final String MOD_ID = "grease";
    private static GreaseConfig config;

    public static void init() {
        config = GreaseConfig.loadConfig(new File(GreasePlatform.getConfigDirectory() + "/grease.json"));
    }

    public static GreaseConfig getConfig() {
        return config;
    }

    public static boolean enableGrease(Item item) {
        Identifier id = Registries.ITEM.getId(item);
        return convertStringSetToIdentifierSet(getConfig().grease_item).contains(id);
    }

    private static Set<Identifier> convertStringSetToIdentifierSet(Set<String> stringSet) {
        return stringSet.stream()
                .map(s -> {
                    try {
                        return new Identifier(s);
                    } catch (InvalidIdentifierException e) {
                        System.err.println("Invalid Identifier: " + s);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public static int getColor(ItemStack stack){
        String type = stack.getOrCreateNbt().getString("grease");
        return switch (type) {
            case "lightning" -> 0x481475da;
            case "fire" -> 0x28f10023;
            case "sacred" -> 0x48f0cd50;
            case "poison" -> 0x48dcf285;
            case "freezing" -> 0x7f7695af;
            case "magic" ->  0x4856edfc;
            default -> 0x7f505050;
        };
    }

    public static Identifier getIdentifier(String type){
        switch (type){
            case "lightning" : return (new Identifier("grease","textures/fx/lightning_fx.png"));
            case "fire" : return (new Identifier("grease","textures/fx/fire_fx.png"));
            case "freezing" : return (new Identifier("grease","textures/fx/freezing_fx.png"));
            case "magic" : return (new Identifier("grease","textures/fx/magic_fx.png"));
            case "poison" : return (new Identifier("grease","textures/fx/poison_fx.png"));
            case "sacred" : return (new Identifier("grease","textures/fx/sacred_fx.png"));
            default : return (new Identifier("grease","textures/fx/general_fx.png"));
        }
    }
}
