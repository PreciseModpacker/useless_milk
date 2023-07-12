package fr.shoqapik.uselessmilk;

import com.mojang.logging.LogUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(UselessMilk.MODID)
public class UselessMilk
{
    public static final String MODID = "uselessmilk";


    public UselessMilk()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void eatMilk(LivingEntityUseItemEvent.Tick event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            System.out.println(event.getDuration());
            if(event.getDuration() <= 1){
                if(event.getItem().toString().contains("milk_bottle")){
                    player.setItemInHand(InteractionHand.MAIN_HAND, handleMilkBottle(player, event.getItem()));
                    event.setDuration(40);

                }else if(event.getItem().toString().contains("milkshake")){
                    event.setDuration(40);
                    handleMilshake(player, event.getItem());
                } else if(event.getItem().toString().contains("milk_bucket")){
                    player.setItemInHand(InteractionHand.MAIN_HAND, handleMilkBucket(player, event.getItem()));
                    event.setDuration(32);
                }

            }
        }
    }

    public ItemStack handleMilkBucket(LivingEntity entity, ItemStack stack){
        if (entity instanceof ServerPlayer serverplayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, stack);
            serverplayer.awardStat(Stats.ITEM_USED.get(stack.getItem()));
        }

        if (entity instanceof Player && !((Player)entity).getAbilities().instabuild) {
            stack.shrink(1);
        }

        return stack.isEmpty() ? new ItemStack(Items.BUCKET) : stack;
    }
    private void handleMilshake(Player player, ItemStack item) {
        item.shrink(1);

    }

    public ItemStack handleMilkBottle(LivingEntity entity, ItemStack stack){
        if (entity instanceof ServerPlayer serverplayerentity) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayerentity, stack);
            serverplayerentity.awardStat(Stats.ITEM_USED.get(stack.getItem()));
        }

        if (entity instanceof Player && !((Player)entity).getAbilities().instabuild) {
            stack.shrink(1);
        }

        if (stack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        } else {
            if (entity instanceof Player) {
                Player playerentity = (Player)entity;
                if (!((Player)entity).getAbilities().instabuild) {
                    ItemStack itemstack = new ItemStack(Items.GLASS_BOTTLE);
                    if (!playerentity.getInventory().add(itemstack)) {
                        playerentity.drop(itemstack, false);
                    }
                }
            }

            return stack;
        }
    }

}
