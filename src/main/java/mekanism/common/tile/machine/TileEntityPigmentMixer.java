package mekanism.common.tile.machine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mekanism.api.RelativeSide;
import mekanism.api.Upgrade;
import mekanism.api.annotations.NonNull;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.pigment.IPigmentTank;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.PigmentMixingRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.PigmentMixingCachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.inputs.chemical.PigmentStackIngredient;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper.ComputerChemicalTankWrapper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.sync.SyncableFloatingLong;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.chemical.PigmentInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.ChemicalSlotInfo.PigmentSlotInfo;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.tile.interfaces.IBoundingBlock;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.RecipeLookupUtil;
import mekanism.common.util.WorldUtils;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class TileEntityPigmentMixer extends TileEntityRecipeMachine<PigmentMixingRecipe> implements IBoundingBlock {

    @WrappingComputerMethod(wrapper = ComputerChemicalTankWrapper.class, methodNames = {"getLeftInput", "getLeftInputCapacity", "getLeftInputNeeded"})
    public IPigmentTank leftInputTank;
    @WrappingComputerMethod(wrapper = ComputerChemicalTankWrapper.class, methodNames = {"getRightInput", "getRightInputCapacity", "getRightInputNeeded"})
    public IPigmentTank rightInputTank;
    @WrappingComputerMethod(wrapper = ComputerChemicalTankWrapper.class, methodNames = {"getOutput", "getOutputCapacity", "getOutputNeeded"})
    public IPigmentTank outputTank;

    private FloatingLong clientEnergyUsed = FloatingLong.ZERO;

    private final IOutputHandler<@NonNull PigmentStack> outputHandler;
    private final IInputHandler<@NonNull PigmentStack> leftInputHandler;
    private final IInputHandler<@NonNull PigmentStack> rightInputHandler;

    private MachineEnergyContainer<TileEntityPigmentMixer> energyContainer;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getLeftInputItem")
    private PigmentInventorySlot leftInputSlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getOutputItem")
    private PigmentInventorySlot outputSlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getRightInputItem")
    private PigmentInventorySlot rightInputSlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getEnergyItem")
    private EnergyInventorySlot energySlot;

    public TileEntityPigmentMixer() {
        super(MekanismBlocks.PIGMENT_MIXER);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.PIGMENT, TransmissionType.ENERGY);

        ConfigInfo itemConfig = configComponent.getConfig(TransmissionType.ITEM);
        if (itemConfig != null) {
            itemConfig.addSlotInfo(DataType.INPUT_1, new InventorySlotInfo(true, true, leftInputSlot));
            itemConfig.addSlotInfo(DataType.INPUT_2, new InventorySlotInfo(true, true, rightInputSlot));
            itemConfig.addSlotInfo(DataType.OUTPUT, new InventorySlotInfo(true, true, outputSlot));
            itemConfig.addSlotInfo(DataType.INPUT_OUTPUT, new InventorySlotInfo(true, true, leftInputSlot, rightInputSlot, outputSlot));
            itemConfig.addSlotInfo(DataType.ENERGY, new InventorySlotInfo(true, true, energySlot));
            //Set default config directions
            itemConfig.setDataType(DataType.INPUT_1, RelativeSide.LEFT);
            itemConfig.setDataType(DataType.INPUT_2, RelativeSide.RIGHT);
            itemConfig.setDataType(DataType.OUTPUT, RelativeSide.FRONT);
            itemConfig.setDataType(DataType.ENERGY, RelativeSide.BACK);
        }

        ConfigInfo pigmentConfig = configComponent.getConfig(TransmissionType.PIGMENT);
        if (pigmentConfig != null) {
            pigmentConfig.addSlotInfo(DataType.INPUT_1, new PigmentSlotInfo(true, false, leftInputTank));
            pigmentConfig.addSlotInfo(DataType.INPUT_2, new PigmentSlotInfo(true, false, rightInputTank));
            pigmentConfig.addSlotInfo(DataType.OUTPUT, new PigmentSlotInfo(false, true, outputTank));
            pigmentConfig.addSlotInfo(DataType.INPUT_OUTPUT, new PigmentSlotInfo(true, true, leftInputTank, rightInputTank, outputTank));
            pigmentConfig.setDataType(DataType.INPUT_1, RelativeSide.LEFT);
            pigmentConfig.setDataType(DataType.INPUT_2, RelativeSide.RIGHT);
            pigmentConfig.setDataType(DataType.OUTPUT, RelativeSide.FRONT);
            pigmentConfig.setEjecting(true);
        }

        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM, TransmissionType.PIGMENT);

        leftInputHandler = InputHelper.getInputHandler(leftInputTank);
        rightInputHandler = InputHelper.getInputHandler(rightInputTank);
        outputHandler = OutputHelper.getOutputHandler(outputTank);
    }

    @Nonnull
    @Override
    public IChemicalTankHolder<Pigment, PigmentStack, IPigmentTank> getInitialPigmentTanks() {
        ChemicalTankHelper<Pigment, PigmentStack, IPigmentTank> builder = ChemicalTankHelper.forSidePigmentWithConfig(this::getDirection, this::getConfig);
        builder.addTank(leftInputTank = ChemicalTankBuilder.PIGMENT.input(1_000, pigment -> isValidPigment(pigment, rightInputTank), this::isValidPigment, this));
        builder.addTank(rightInputTank = ChemicalTankBuilder.PIGMENT.input(1_000, pigment -> isValidPigment(pigment, leftInputTank), this::isValidPigment, this));
        builder.addTank(outputTank = ChemicalTankBuilder.PIGMENT.output(2_000, this));
        return builder.build();
    }

    private boolean isValidPigment(@Nonnull Pigment pigment) {
        return containsRecipe(recipe -> recipe.getLeftInput().testType(pigment) || recipe.getRightInput().testType(pigment));
    }

    private boolean isValidPigment(@Nonnull Pigment pigment, IPigmentTank otherTank) {
        if (otherTank.isEmpty()) {
            //If the other tank is empty, don't limit what can enter our tank based on it
            return true;
        }
        PigmentStack stack = otherTank.getStack();
        return containsRecipe(recipe -> {
            PigmentStackIngredient leftInput = recipe.getLeftInput();
            PigmentStackIngredient rightInput = recipe.getRightInput();
            return rightInput.testType(pigment) && leftInput.testType(stack) || leftInput.testType(pigment) && rightInput.testType(stack);
        });
    }

    @Nonnull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers() {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this));
        return builder.build();
    }

    @Nonnull
    @Override
    protected IInventorySlotHolder getInitialInventory() {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        //TODO: Should our pigment checking, also check the other tank's contents so we don't let putting the same pigment in on both sides
        builder.addSlot(leftInputSlot = PigmentInventorySlot.fill(leftInputTank, this, 6, 56));
        builder.addSlot(rightInputSlot = PigmentInventorySlot.fill(rightInputTank, this, 154, 56));
        builder.addSlot(outputSlot = PigmentInventorySlot.drain(outputTank, this, 80, 65));
        builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, this, 154, 14));
        leftInputSlot.setSlotType(ContainerSlotType.INPUT);
        leftInputSlot.setSlotOverlay(SlotOverlay.MINUS);
        rightInputSlot.setSlotType(ContainerSlotType.INPUT);
        rightInputSlot.setSlotOverlay(SlotOverlay.MINUS);
        outputSlot.setSlotType(ContainerSlotType.OUTPUT);
        outputSlot.setSlotOverlay(SlotOverlay.PLUS);
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        leftInputSlot.fillTank();
        rightInputSlot.fillTank();
        outputSlot.drainTank();
        FloatingLong prev = energyContainer.getEnergy().copyAsConst();
        cachedRecipe = getUpdatedCache(0);
        if (cachedRecipe != null) {
            cachedRecipe.process();
        }
        //Update amount of energy that actually got used, as if we are "near" full we may not have performed our max number of operations
        clientEnergyUsed = prev.subtract(energyContainer.getEnergy());
    }

    @Nonnull
    @ComputerMethod(nameOverride = "getEnergyUsage")
    public FloatingLong getEnergyUsed() {
        return clientEnergyUsed;
    }

    @Nonnull
    @Override
    public MekanismRecipeType<PigmentMixingRecipe> getRecipeType() {
        return MekanismRecipeType.PIGMENT_MIXING;
    }

    @Nullable
    @Override
    public PigmentMixingRecipe getRecipe(int cacheIndex) {
        return RecipeLookupUtil.findChemicalChemicalRecipe(this, leftInputHandler, rightInputHandler);
    }

    @Nullable
    @Override
    public CachedRecipe<PigmentMixingRecipe> createNewCachedRecipe(@Nonnull PigmentMixingRecipe recipe, int cacheIndex) {
        return new PigmentMixingCachedRecipe(recipe, leftInputHandler, rightInputHandler, outputHandler)
              .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
              .setActive(this::setActive)
              .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
              .setOnFinish(() -> markDirty(false))
              .setPostProcessOperations(currentMax -> {
                  if (currentMax <= 0) {
                      //Short circuit that if we already can't perform any outputs, just return
                      return currentMax;
                  }
                  return Math.min((int) Math.pow(2, upgradeComponent.getUpgrades(Upgrade.SPEED)), currentMax);
              });
    }

    @Override
    public void onPlace() {
        WorldUtils.makeBoundingBlock(getLevel(), getBlockPos().above(), getBlockPos());
    }

    @Override
    public void onBreak(BlockState oldState) {
        World world = getLevel();
        if (world != null) {
            world.removeBlock(getBlockPos().above(), false);
            world.removeBlock(getBlockPos(), false);
        }
    }

    @Nonnull
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        //TODO - 10.1: Re-evaluate this (and other render bounding box implementations) as I think this may only actually be needed
        // if our tile has a TER
        return new AxisAlignedBB(worldPosition, worldPosition.offset(1, 2, 1));
    }

    public MachineEnergyContainer<TileEntityPigmentMixer> getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableFloatingLong.create(this::getEnergyUsed, value -> clientEnergyUsed = value));
    }
}