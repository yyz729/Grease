package com.yyz.item;

import com.yyz.Grease;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class GreaseItem extends Item {
    private final String type;
    private Hand useHand;
    public GreaseItem(String typeString, Settings settings) {
        super(settings);
        this.type = typeString;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if(!world.isClient) {
            Hand target = useHand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
            ItemStack targetStack = user.getStackInHand(target);
            if (targetStack.getItem() instanceof ToolItem) {
                targetStack.set(Grease.GREASE_COMPONENT_TYPE, getType());
                targetStack.set(Grease.GREASE_AGE_COMPONENT_TYPE, world.getTime() + Grease.getConfig().grease_tick);
                ((PlayerEntity) user).getItemCooldownManager().set(this, 20);
                stack.decrement(1);
            } else {
                user.sendMessage(Text.translatable("grease.fail.message"));
            }
        }
        return stack;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        useHand = hand;
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BRUSH;
    }


    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 32;
    }

    public String getType() {
        return type;
    }
}
