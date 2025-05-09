package com.yyz.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static com.yyz.Grease.MOD_ID;

public class ModItems {
    public static final GreaseItem LIGHTNING_GREASE = new GreaseItem("lightning", new Item.Settings().maxCount(16));
    public static final GreaseItem SACRED_GREASE = new GreaseItem("sacred", new Item.Settings().maxCount(16));
    public static final GreaseItem FIRE_GREASE = new GreaseItem("fire", new Item.Settings().maxCount(16));
    public static final GreaseItem MAGIC_GREASE = new GreaseItem("magic", new Item.Settings().maxCount(16));
    public static final GreaseItem POISON_GREASE = new GreaseItem("poison", new Item.Settings().maxCount(16));
    public static final GreaseItem FREEZING_GREASE = new GreaseItem("freezing", new Item.Settings().maxCount(16));

    public static final ItemGroup GREASE = FabricItemGroup.builder()
            .icon(() -> new ItemStack(LIGHTNING_GREASE))
            .displayName(Text.translatable("itemGroup.grease.title"))
            .entries((context, entries) -> {

                entries.add(LIGHTNING_GREASE);
                entries.add(SACRED_GREASE);
                entries.add(FIRE_GREASE);
                entries.add(MAGIC_GREASE);
                entries.add(POISON_GREASE);
                entries.add(FREEZING_GREASE);

            })
            .build();

    public static void register(){
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "lightning_grease"), LIGHTNING_GREASE);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "sacred_grease"), SACRED_GREASE);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "fire_grease"), FIRE_GREASE);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "magic_grease"), MAGIC_GREASE);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "poison_grease"), POISON_GREASE);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "freezing_grease"), FREEZING_GREASE);

        Registry.register(Registries.ITEM_GROUP, new Identifier(MOD_ID, "group"), GREASE);
    }
}
