package com.willfp.reforges.display;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.display.Display;
import com.willfp.eco.core.display.DisplayModule;
import com.willfp.eco.core.display.DisplayPriority;
import com.willfp.eco.core.fast.FastItemStack;
import com.willfp.reforges.reforges.Reforge;
import com.willfp.reforges.reforges.meta.ReforgeTarget;
import com.willfp.reforges.reforges.util.ReforgeUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ReforgesDisplay extends DisplayModule {
    /**
     * Create weapons display.
     *
     * @param plugin Instance of Reforges.
     */
    public ReforgesDisplay(@NotNull final EcoPlugin plugin) {
        super(plugin, DisplayPriority.HIGHEST);
    }

    @Override
    protected void display(@NotNull final ItemStack itemStack,
                           @NotNull final Object... args) {
        ReforgeTarget target = ReforgeTarget.getForMaterial(itemStack.getType());

        if (target == null) {
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;

        Reforge reforge = ReforgeUtils.getReforge(meta);

        FastItemStack fastItemStack = FastItemStack.wrap(itemStack);
        List<String> lore = fastItemStack.getLore();
        assert lore != null;

        if (reforge != null) {
            if (this.getPlugin().getConfigYml().getBool("reforge.display-in-lore")) {
                List<String> addLore = this.getPlugin().getConfigYml().getStrings("reforge.lore-suffix");

                addLore.replaceAll(s -> Display.PREFIX + s);
                addLore.replaceAll(s -> s.replace("%reforge%", reforge.getName()));
                addLore.replaceAll(s -> s.replace("%description%", reforge.getDescription()));
                lore.addAll(addLore);
            }
        }

        if (reforge == null && this.getPlugin().getConfigYml().getBool("reforge.show-reforgable")) {
            List<String> addLore = this.getPlugin().getConfigYml().getStrings("reforge.reforgable-suffix");

            addLore.replaceAll(s -> Display.PREFIX + s);
            lore.addAll(addLore);
        }

        fastItemStack.setLore(lore);
    }
}
