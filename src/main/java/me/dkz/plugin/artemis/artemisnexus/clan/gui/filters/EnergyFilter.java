package me.dkz.plugin.artemis.artemisnexus.clan.gui.filters;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.CustomInventoryImpl;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.paged.PagedInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.item.supplier.InventoryItemSupplier;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.henryfabio.minecraft.inventoryapi.viewer.impl.paged.PagedViewer;
import me.dkz.plugin.artemis.artemisnexus.ClanManager;
import me.dkz.plugin.artemis.artemisnexus.Main;
import me.dkz.plugin.artemis.artemisnexus.clan.Clan;
import me.dkz.plugin.artemis.artemisnexus.clan.gui.ClanGui;
import me.dkz.plugin.artemis.artemisnexus.clan.gui.TopClansGui;
import me.dkz.plugin.artemis.artemisnexus.utils.CustomHeads;
import me.dkz.plugin.artemis.artemisnexus.utils.ItemStackUtils;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static me.dkz.plugin.artemis.artemisnexus.clan.gui.TopClansGui.getInventoryItemSuppliers;

public class EnergyFilter extends PagedInventory {


    private Main plugin = Main.getInstance();
    private ClanManager clanManager = plugin.getClanManager();

    private List<Clan> clans = new ArrayList<>();

    private CustomInventoryImpl last;

    public EnergyFilter(CustomInventoryImpl last) {
        super("me.dkz.energyfilter", "Lista de Clans: Por Energia", 5 * 9);
        this.last = last;

        configuration(configuration -> {
            configuration.secondUpdate(1);
        });




    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {
        clans = clanManager.getClans().stream().sorted(Comparator.comparingLong(Clan::getTotalEnergy).reversed()).collect(Collectors.toList());
        TopClansGui.getDefaultButtons(viewer, editor, last);

        ItemStack energyFilter = ItemStackUtils.customItem(CustomHeads.create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODcxM2Y4YmQxOTM4NGVkNWI5ZDcyZWE5MWNjY2ZhMGU4Zjk0YjM4MjFjYzVlOTdiNTI3N2U5OWU5MTZiOGY0NiJ9fX0="),
                "&7Filtrando por Energia", 1, "&8Clique para filtrar por energia");
        editor.setItem(40, InventoryItem.of(energyFilter));
        ItemStack levelFilterItem = ItemStackUtils.customItem(CustomHeads.create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWY4NDczNWZjOWM3NjBlOTVlYWYxMGNlYzRmMTBlZGI1ZjM4MjJhNWZmOTU1MWVlYjUwOTUxMzVkMWZmYTMwMiJ9fX0="),
                "&7Filtrar por Nivel", 1, "&8Clique para filtrar por nivel");
        editor.setItem(41, InventoryItem.of(levelFilterItem).defaultCallback(c ->{
            LevelFilter levelFilter = new LevelFilter(new ClanGui());
            levelFilter.openInventory(viewer.getPlayer());
        }));
    }

    @Override
    protected List<InventoryItemSupplier> createPageItems(PagedViewer viewer) {
        return getInventoryItemSuppliers(clans, clanManager);
    }



}
