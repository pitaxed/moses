package com.example.moses;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Moses.MODID);
    public static RegistryObject<Block> SLOW_WATER_BLOCK = BLOCKS.register("slow_water_block", () -> new SlowLiquidBlock(new SlowWaterFluid.Source(), BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));

    @ObjectHolder("moses:slow_water_block")
    public static final SlowLiquidBlock slow_water_block = null;
}
