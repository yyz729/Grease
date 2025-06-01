package com.yyz.grease.forge;

import com.yyz.grease.client.GreaseRenderLayer;
import com.yyz.grease.item.GreaseItem;
import com.yyz.grease.Grease;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(Grease.MOD_ID)
public class GreaseForge {
    public static final DeferredRegister<ItemGroup> TABS = DeferredRegister.create(RegistryKeys.ITEM_GROUP,Grease.MOD_ID);
    public static final RegistryObject<ItemGroup> GREASE_TAB = TABS.register("grease_tab", () ->
            ItemGroup.builder().icon(() -> new ItemStack(GreaseForge.LIGHTNING_GREASE.get()))
                    .displayName(Text.translatable("itemGroup.grease.title"))
                    .entries((context, entries) -> {
                        entries.add(GreaseForge.LIGHTNING_GREASE.get());
                        entries.add(GreaseForge.SACRED_GREASE.get());
                        entries.add(GreaseForge.FIRE_GREASE.get());
                        entries.add(GreaseForge.MAGIC_GREASE.get());
                        entries.add(GreaseForge.POISON_GREASE.get());
                        entries.add(GreaseForge.FREEZING_GREASE.get());
                    })
                    .build());

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,Grease.MOD_ID);

    public static final RegistryObject<Item> LIGHTNING_GREASE = ITEMS.register("lightning_grease", () -> new GreaseItem("lightning",new Item.Settings().maxCount(16)));
    public static final RegistryObject<Item> SACRED_GREASE = ITEMS.register("sacred_grease", () -> new GreaseItem("sacred",new Item.Settings().maxCount(16)));
    public static final RegistryObject<Item> FIRE_GREASE = ITEMS.register("fire_grease", () -> new GreaseItem("fire",new Item.Settings().maxCount(16)));
    public static final RegistryObject<Item> MAGIC_GREASE = ITEMS.register("magic_grease", () -> new GreaseItem("magic",new Item.Settings().maxCount(16)));
    public static final RegistryObject<Item> POISON_GREASE = ITEMS.register("poison_grease", () -> new GreaseItem("poison",new Item.Settings().maxCount(16)));
    public static final RegistryObject<Item> FREEZING_GREASE = ITEMS.register("freezing_grease", () -> new GreaseItem("freezing",new Item.Settings().maxCount(16)));


    public GreaseForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        TABS.register(modEventBus);
        ITEMS.register(modEventBus);
        Grease.init();
    }


    public static RenderLayer getGlint(String textureType) {
        // 直接从映射中获取对应的渲染层
        RenderLayer layer = GreaseRenderLayer.GLINT_TYPES.get(textureType);

        // 如果找不到指定类型，使用默认的发光效果
        if (layer == null) {
            // 尝试使用小写查找
            layer = GreaseRenderLayer.GLINT_TYPES.get(textureType.toLowerCase());

            // 如果还是找不到，使用默认的发光效果
            if (layer == null) {
                return RenderLayer.getGlint();
            }
        }

        return layer;
    }
}
