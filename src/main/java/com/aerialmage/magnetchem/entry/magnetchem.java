package com.aerialmage.magnetchem.entry;

import com.aerialmage.magnetchem.blocks.*;
import com.aerialmage.magnetchem.core.CreativeTabsAMP;
import com.aerialmage.magnetchem.core.Reference;
import com.aerialmage.magnetchem.core.worldgen.WorldgenMonitor;
import com.aerialmage.magnetchem.fluids.LiquidWorldgen;
import com.aerialmage.magnetchem.items.ItemBloodyStone;
import com.aerialmage.magnetchem.items.ItemGaussMeter;
import com.aerialmage.magnetchem.items.ItemRedCells;
import com.aerialmage.magnetchem.proxy.CommonProxy;
import com.aerialmage.magnetchem.recipes.InfusionRecipe;
import com.aerialmage.magnetchem.tileentities.TileEntityMagnetic;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.io.File;

/**
 * Created by sirius on 7/25/14.
 */
@Mod(modid = "magnetchem", name = "Magnetic Chemistry", version = "1.7.4-0.1")
public class magnetchem {
    @Mod.Instance("magnetchem")
    public static magnetchem instance;


    @SidedProxy(clientSide = com.aerialmage.magnetchem.core.Reference.CLIENT_PROXY_CLASS, serverSide = com.aerialmage.magnetchem.core.Reference.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    public static Item itemBandedStuff;
    public static Item itemBloodyStone;
    public static Item GaussMeter;
    public static Block blockRedCellV2;
    public static Block blockMultiColorWool;
    public static Block blockBloodyStone;
    public static Block blockMagneticConductor;
    public static Block blockMagneticInductionFurnace;
    public static Block blockWorldgenLiquifier;
    public static Block blockWorldgenRegenerator;
    public static Fluid fluidLiquidWorldgen;
    public static File configAlt;

    /***
     * This is code that is executed prior to your mod being initialized into of Minecraft
     * Examples of code that could be run here;
     *
     * Initializing your items/blocks (you must do this here)
     * Setting up your own custom logger for writing log data to
     * Loading your language translations for your mod (if your mod has translations for other languages)
     * Registering your mod's key bindings and sounds
     *
     * @param event The Forge ModLoader pre-initialization event
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
        configAlt = new File(event.getModConfigurationDirectory().getAbsolutePath()+"/AMP_BlockValues.cfg");
        try
        {
            cfg.load();
            Reference.load(cfg);
        }
        catch(Exception e)
        {
            System.err.println("Aerial mage prototype has a problem loading its configuration file:");
            e.printStackTrace(System.err);
        }
        finally
        {
            if(cfg.hasChanged())
                cfg.save();
        }

        //proxy.registerTickHandlers();
        instance = this;
        blockMultiColorWool = new BlockMulticolorWool(Material.cloth).setHardness(1.5F).setStepSound(Block.soundTypeCloth).setBlockTextureName("multi_color_wool").setBlockName("AMP:multi_color_wool_side").setCreativeTab(CreativeTabsAMP.instance);
        blockRedCellV2 = new BlockRedCell().setHardness(1.5F).setStepSound(Block.soundTypeCloth).setBlockTextureName("red_cell_active").setBlockName("AMP:red_cell").setCreativeTab(CreativeTabsAMP.instance);
        blockBloodyStone = new BlockBloodyStone(Material.rock).setHardness(1.5F).setStepSound(Block.soundTypeCloth).setBlockTextureName("block_bloody_stone_0").setBlockName("AMP:bloody_stone").setCreativeTab(CreativeTabsAMP.instance);
        blockMagneticConductor = new BlockMagneticConductor(Material.rock).setHardness(1.5F).setStepSound(Block.soundTypeCloth).setBlockTextureName("magnet_ends").setBlockName("AMP:magnet_block").setCreativeTab(CreativeTabsAMP.instance);
        blockMagneticInductionFurnace = new BlockMagnetInductionFurnace(Material.rock).setHardness(1.5F).setStepSound(Block.soundTypeCloth).setBlockTextureName("magnet_ends").setBlockName("AMP:magnet_furnace_block").setCreativeTab(CreativeTabsAMP.instance);
        blockWorldgenLiquifier = new BlockWorldgenLiquifier(Material.rock).setHardness(1.5F).setStepSound(Block.soundTypeCloth).setBlockTextureName("magnet_ends").setBlockName("AMP:worldgen_liquifier_block").setCreativeTab(CreativeTabsAMP.instance);
        blockWorldgenRegenerator = new BlockWorldgenRegenerator(Material.rock).setHardness(1.5F).setStepSound(Block.soundTypeCloth).setBlockTextureName("magnet_ends").setBlockName("AMP:worldgen_regenerator_block").setCreativeTab(CreativeTabsAMP.instance);
        GameRegistry.registerBlock(blockMultiColorWool, "multiColorWool");
        GameRegistry.registerBlock(blockRedCellV2, "redCell");
        GameRegistry.registerBlock(blockBloodyStone, "bloodyStone");
        GameRegistry.registerBlock(blockMagneticConductor, "magneticConductor");
        GameRegistry.registerBlock(blockMagneticInductionFurnace, "magneticInductionFurnace");
        GameRegistry.registerBlock(blockWorldgenLiquifier, "worldgenLiquifier");
        GameRegistry.registerBlock(blockWorldgenRegenerator, "worldgenRegenerator");


        itemBandedStuff = new ItemRedCells();
        itemBandedStuff.setUnlocalizedName(Reference.MOD_ID+":bandedstuff");

        itemBloodyStone = new ItemBloodyStone();
        itemBloodyStone.setUnlocalizedName(Reference.MOD_ID+":bloodystone");

        GaussMeter = new ItemGaussMeter();
        GaussMeter.setUnlocalizedName(Reference.MOD_ID+":gaussmeter");

        fluidLiquidWorldgen = new LiquidWorldgen("liquid worldgen");
        FluidRegistry.registerFluid(fluidLiquidWorldgen);

    }

    /***
     * This is code that is executed when your mod is being initialized in Minecraft
     * Examples of code that could be run here;
     *
     * Registering your GUI Handler
     * Registering your different event listeners
     * Registering your different tile entities
     * Adding in any recipes you have
     *
     * @param event The Forge ModLoader initialization event
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        //FMLNetworkHandler.instance().registerNetworkMod();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
        net.minecraft.item.crafting.CraftingManager.getInstance().getRecipeList().add(new InfusionRecipe());
        GameRegistry.addRecipe(new ItemStack(blockRedCellV2), "BBB", "BRB", "BBB", 'B', new ItemStack(itemBandedStuff, 1, 0), 'R', new ItemStack(itemBandedStuff, 1, 1));
        GameRegistry.addShapelessRecipe(new ItemStack(blockMultiColorWool), new Object[]{new ItemStack(Blocks.wool, 1), new ItemStack(Items.dye, 1, 1), new ItemStack(Items.dye, 1, 2), new ItemStack(Items.dye, 1, 4)});
        GameRegistry.addRecipe(new ItemStack(itemBandedStuff, 1, 0), "SSS", "SGS", "SSS", 'S', Blocks.stone, 'G', Blocks.glass_pane);
        GameRegistry.addRecipe(new ItemStack(itemBandedStuff, 1, 1), "I I", " R ", "I I", 'I', Items.iron_ingot, 'R', Blocks.redstone_block);
        GameRegistry.addRecipe(new ItemStack(itemBloodyStone, 1, 64), "RRR", "RSR", "RRR", 'R', Items.rotten_flesh, 'S', Blocks.stone);
        GameRegistry.addRecipe(new ItemStack(blockBloodyStone, 1, 0), "ILI", "LSL", "ILI", 'I', Items.iron_ingot, 'L', Items.leather, 'S', new ItemStack(itemBloodyStone, 1, 0));
        GameRegistry.registerTileEntity(TileEntityMagnetic.class, "magnetBlock");

        MinecraftForge.EVENT_BUS.register(new WorldgenMonitor());
        MinecraftForge.EVENT_BUS.register(fluidLiquidWorldgen);
        MinecraftForge.EVENT_BUS.register(blockRedCellV2);

    }

    /***
     * This is code that is executed after all mods are initialized in Minecraft
     * This is a good place to execute code that interacts with other mods (ie, loads an addon module
     * of your mod if you find a particular mod).
     *
     * @param event The Forge ModLoader post-initialization event
     */
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

}
