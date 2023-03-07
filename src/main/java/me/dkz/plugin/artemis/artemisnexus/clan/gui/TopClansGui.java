package me.dkz.plugin.artemis.artemisnexus.clan.gui;

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
import me.dkz.plugin.artemis.artemisnexus.clan.gui.filters.ClanLetterHeads;
import me.dkz.plugin.artemis.artemisnexus.clan.gui.filters.EnergyFilter;
import me.dkz.plugin.artemis.artemisnexus.clan.gui.filters.LevelFilter;
import me.dkz.plugin.artemis.artemisnexus.utils.CustomHeads;
import me.dkz.plugin.artemis.artemisnexus.utils.ItemStackUtils;
import me.dkz.plugin.artemis.artemisnexus.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class TopClansGui extends PagedInventory {

    private Main plugin = Main.getInstance();
    private ClanManager clanManager = plugin.getClanManager();
    private List<Clan> clans = new ArrayList<>();

    private CustomInventoryImpl last;

    public TopClansGui(CustomInventoryImpl last) {
        super("me.dkz.topclans", "Lista de Clans: Por Nome", 5 * 9);
        this.last = last;
        clans = clanManager.getClans().stream().sorted(Comparator.comparing(Clan::getName)).collect(Collectors.toList());
        configuration(configuration -> {
            configuration.secondUpdate(1);
        });
    }




    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {
        editor.setItem(36, InventoryItem.of(ItemStackUtils.customItem(Material.SIGN, "&7Voltar", 1, "&8Clique para voltar")).callback(ClickType.LEFT, (i) ->{
            last.openInventory(viewer.getPlayer());
        }));
        ItemStack nameFilter = ItemStackUtils.customItem(CustomHeads.create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmFkOGEzYTNiMzZhZGQ1ZDk1NDFhOGVjOTcwOTk2ZmJkY2RlYTk0MTRjZDc1NGM1MGU0OGU1ZDM0ZjFiZjYwYSJ9fX0="),
                "&7Filtrando por Nome", 1, "&8Clique para filtrar por nome");
        editor.setItem(39, InventoryItem.of(nameFilter));

        ItemStack energyFilter = ItemStackUtils.customItem(CustomHeads.create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODcxM2Y4YmQxOTM4NGVkNWI5ZDcyZWE5MWNjY2ZhMGU4Zjk0YjM4MjFjYzVlOTdiNTI3N2U5OWU5MTZiOGY0NiJ9fX0="),
                "&7Filtrar por Energia", 1, "&8Clique para filtrar por energia");
        editor.setItem(40, InventoryItem.of(energyFilter).defaultCallback(c ->{
            EnergyFilter energyFilter1 = new EnergyFilter(new ClanGui());
            energyFilter1.openInventory(c.getPlayer());
        }));
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


    public static List<InventoryItemSupplier> getInventoryItemSuppliers(List<Clan> clans, ClanManager clanManager) {
        List<InventoryItemSupplier> items = new LinkedList<>();

        clans.forEach(clan ->{
            int index = clans.indexOf(clan);
            ItemStack itemStack = ItemStackUtils.customItem(CustomHeads.create(ClanLetterHeads.getTexture(clan.name.toUpperCase().charAt(0))),
                    "§7["+clan.getTag()+"] "+clan.getName() +" §f#"+(index+1),
                    1,
                    "",
                    " §7Fundado: §a"+clan.getCreationDate(),
                    " §7Membros do Clan: §e" + clan.getPlayers().size(),
                    " §7Energia Total: §b"+ StringUtils.format5(clan.getTotalEnergy()),
                    " §7Nível do Clan: §e"+clan.getLevel(),
                    "",
                    "§7 "+clan.getDescription(),
                    "");
            items.add(() -> InventoryItem.of(itemStack));
        });


        return items;
    }

    public static void getDefaultButtons(Viewer viewer, InventoryEditor editor, CustomInventoryImpl last) {
        editor.setItem(36, InventoryItem.of(ItemStackUtils.customItem(Material.SIGN, "&7Voltar", 1, "&8Clique para voltar")).callback(ClickType.LEFT, (i) ->{
            last.openInventory(viewer.getPlayer());
        }));
        ItemStack nameFilter = ItemStackUtils.customItem(CustomHeads.create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmFkOGEzYTNiMzZhZGQ1ZDk1NDFhOGVjOTcwOTk2ZmJkY2RlYTk0MTRjZDc1NGM1MGU0OGU1ZDM0ZjFiZjYwYSJ9fX0="),
                "&7Filtrar por Nome", 1, "&8Clique para filtrar por nome");
        editor.setItem(39, InventoryItem.of(nameFilter).defaultCallback(c ->{
            TopClansGui topClansGui = new TopClansGui(new ClanGui());
            topClansGui.openInventory(viewer.getPlayer());
        }));
    }
}
