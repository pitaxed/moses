package com.example.moses;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Queue;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("moses")
public class Moses extends SpongeBlock {
    // Directly reference a log4j logger.
    static String MODID = "moses";
    private static final Logger LOGGER = LogManager.getLogger();



    public Moses() {
        super(BlockBehaviour.Properties.of(Material.SPONGE).strength(0.6F).sound(SoundType.GRASS));
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        FluidInit.FLUIDS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BlockInit.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void movementEvent(MovementInputUpdateEvent event) {
        Player player = Minecraft.getInstance().player;

        if (event.getEntityLiving() instanceof Player && player != null) {
            ServerLevel level = Minecraft.getInstance()
                    .getSingleplayerServer().overworld();

            //BlockInit.slow_water_block.defaultBlockState();
            BlockPos blockPos = new BlockPos(player.getPosition(0));
            //BlockState blockState = Blocks.WATER.defaultBlockState();//new SlowLiquidBlock(new SlowWaterFluid.Source(),
                   // BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()).defaultBlockState();
            if (Minecraft.getInstance().level.isEmptyBlock(blockPos)) {
                //Minecraft.getInstance().level.setBlock(blockPos, blockState, 0);
            }
            if(level.getFluidState(blockPos).is(FluidTags.WATER)){
                LOGGER.info("SLOW_WATER == WATER???");

            }

            //removeWaterBreadthFirstSearch(level, blockPos);
        }
    }

    private boolean removeWaterBreadthFirstSearch(Level p_56808_, BlockPos p_56809_) {
        Queue<Tuple<BlockPos, Integer>> queue = Lists.newLinkedList();
        queue.add(new Tuple<>(p_56809_, 0));
        int i = 0;

        while (!queue.isEmpty()) {
            Tuple<BlockPos, Integer> tuple = queue.poll();
            BlockPos blockpos = tuple.getA();
            int j = tuple.getB();

            for (Direction direction : Direction.values()) {
                BlockPos blockpos1 = blockpos.relative(direction);
                BlockState blockstate = p_56808_.getBlockState(blockpos1);
                FluidState fluidstate = p_56808_.getFluidState(blockpos1);
                Material material = blockstate.getMaterial();
                if (fluidstate.is(FluidTags.WATER)) {
                    if (blockstate.getBlock() instanceof BucketPickup && !((BucketPickup) blockstate.getBlock()).pickupBlock(p_56808_, blockpos1, blockstate).isEmpty()) {
                        ++i;
                        if (j < 6) {
                            queue.add(new Tuple<>(blockpos1, j + 1));
                        }
                    } else if (blockstate.getBlock() instanceof SlowLiquidBlock) {
                        p_56808_.setBlock(blockpos1, Blocks.AIR.defaultBlockState(), 3);
                        ++i;
                        if (j < 6) {
                            queue.add(new Tuple<>(blockpos1, j + 1));
                        }
                    } else if (material == Material.WATER_PLANT || material == Material.REPLACEABLE_WATER_PLANT) {
                        BlockEntity blockentity = blockstate.hasBlockEntity() ? p_56808_.getBlockEntity(blockpos1) : null;
                        dropResources(blockstate, p_56808_, blockpos1, blockentity);
                        p_56808_.setBlock(blockpos1, Blocks.AIR.defaultBlockState(), 3);
                        ++i;
                        if (j < 6) {
                            queue.add(new Tuple<>(blockpos1, j + 1));
                        }
                    }

                }
            }

            if (i > 64) {
                break;
            }
        }

        return i > 0;
    }


}
