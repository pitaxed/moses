package com.example.moses;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegistryObject;

import java.util.concurrent.Flow;

public class FluidInit {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Moses.MODID);
    public static final RegistryObject<FlowingFluid> SLOW_FLOWING_WATER = FLUIDS.register("slow_flowing_water", () -> new SlowWaterFluid.Flowing());
    public static RegistryObject<FlowingFluid> SLOW_WATER = FLUIDS.register("slow_water", () -> new SlowWaterFluid.Source());
    @ObjectHolder("moses:slow_water")
    public static final FlowingFluid slow_water = null;
    @ObjectHolder("moses:slow_flowing_water")
    public static final FlowingFluid slow_flowing_water = null;

}
