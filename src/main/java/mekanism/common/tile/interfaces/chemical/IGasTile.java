package mekanism.common.tile.interfaces.chemical;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mcp.MethodsReturnNonnullByDefault;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.chemical.gas.IMekanismGasHandler;
import mekanism.common.capabilities.chemical.dynamic.DynamicGasHandler;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.resolver.manager.chemical.GasHandlerManager;
import net.minecraft.util.Direction;

@MethodsReturnNonnullByDefault
public interface IGasTile {

    GasHandlerManager getGasManager();

    /**
     * @apiNote This should not be overridden, or directly called except for initial creation
     */
    default GasHandlerManager getInitialGasManager(@Nonnull Runnable onContentsChanged) {
        DynamicGasHandler handler = new DynamicGasHandler(this::getGasManager, onContentsChanged);
        return new GasHandlerManager(getInitialGasTanks(handler), handler);
    }

    /**
     * @apiNote Do not call directly, only override implementation
     */
    @Nullable
    default IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(@Nonnull IMekanismGasHandler handler) {
        return null;
    }

    /**
     * @apiNote This should not be overridden
     */
    default boolean canHandleGas() {
        return getGasManager().canHandle();
    }

    /**
     * @apiNote This should not be overridden
     */
    default List<IGasTank> getGasTanks(@Nullable Direction side) {
        return getGasManager().getContainers(side);
    }
}