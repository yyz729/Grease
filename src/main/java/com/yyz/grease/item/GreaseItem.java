package com.yyz.grease.item;

import com.yyz.grease.Grease;
import net.minecraft.client.option.Perspective;
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
    Perspective curperspective;
    public GreaseItem(String typeString, Settings settings) {
        super(settings);
        this.type = typeString;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if(!world.isClient) {
            Hand target = useHand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
            ItemStack targetStack = user.getStackInHand(target);
            if (targetStack.getItem() instanceof ToolItem || Grease.enableGrease(targetStack.getItem())) {
                NbtCompound nbt = targetStack.getOrCreateNbt();
                nbt.putString("grease", getType());
                nbt.putLong("grease_age", world.getTime() + Grease.getConfig().grease_tick);
                ((PlayerEntity) user).getItemCooldownManager().set(this, 20);
                stack.decrement(1);
            } else {
                user.sendMessage(Text.translatable("grease.fail.message"));
            }
        }
        return stack;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        useHand = hand;
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.CROSSBOW;
    }

    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    public String getType() {
        return type;
    }
}
