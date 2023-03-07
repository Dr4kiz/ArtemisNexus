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
import me.dkz.plugin.artemis.artemisnexus.clan.Member;
import me.dkz.plugin.artemis.artemisnexus.clan.Clan;
import me.dkz.plugin.artemis.artemisnexus.clan.role.ClanRole;
import me.dkz.plugin.artemis.artemisnexus.utils.CustomHeads;
import me.dkz.plugin.artemis.artemisnexus.utils.ItemStackUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;

public class MembersGui extends PagedInventory {


    private Main plugin = Main.getInstance();
    private ClanManager clanManager = plugin.getClanManager();

    private CustomInventoryImpl lastInventory;

    public MembersGui(CustomInventoryImpl lastInventory) {
        super("dkz.clan.gui.members", "§7Membros do Clan", 9 * 4);
        this.lastInventory = lastInventory;
        configuration(configuration -> {
            configuration.secondUpdate(1);
        });
    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {
        editor.setItem(27, InventoryItem.of(ItemStackUtils.customItem(Material.SIGN, "&7Voltar", 1, "&8Clique para voltar")).callback(ClickType.LEFT, (i) ->{
            lastInventory.openInventory(viewer.getPlayer());
        }));
    }
    @Override
    protected List<InventoryItemSupplier> createPageItems(PagedViewer viewer) {
        List<InventoryItemSupplier> items = new LinkedList<>();

        Member cPlayer = clanManager.getPlayer(viewer.getPlayer().getUniqueId());
        Player player = viewer.getPlayer();

        Clan clan = cPlayer.getClan();
        clan.getPlayers().forEach(member -> {
            ItemStack head = ItemStackUtils.getHead(member.getBukkitPlayer());
            ItemStack playerInfo = ItemStackUtils.customItem(head,
                    "§eInformações de " + member.getBukkitPlayer().getName(), 1,
                    " §7Abates do Jogador: §e" + member.getKills(),
                    " §7Mortes do Jogador: §c" + member.getDeaths(),
                    " §7K/D do Jogador: §a" + member.getKd(),
                    "",
                    " &eEnergia: &b" + member.getEnergy(),
                    " &eCargo: &7" + member.getRole().toString(),
                    " &eEntrada: &7" + member.getFormatJoinDate(),
                    " &eStatus: " + (Bukkit.getPlayer(member.getUuid()) != null ? "&aOnline" : "&cOffline")
            );



            if (!cPlayer.getRole().equals(ClanRole.LIDER)) {
                memberView(items, playerInfo);
            }else{
                playerInfo = ItemStackUtils.customItem(head,
                        "§eInformações de " + member.getBukkitPlayer().getName(), 1,
                        " §7Abates do Jogador: §e" + member.getKills(),
                        " §7Mortes do Jogador: §c" + member.getDeaths(),
                        " §7K/D do Jogador: §a" + member.getKd(),
                        "",
                        " &eEnergia: &b" + member.getEnergy(),
                        " &eCargo: &7" + member.getRole().toString(),
                        " &eEntrada: &7" + member.getFormatJoinDate(),
                        " &eStatus: " + (Bukkit.getPlayer(member.getUuid()) != null ? "&aOnline" : "&cOffline"),
                        "",
                        "§7CLIQUE ESQUERDO: §aPromover Jogador",
                        "§7CLIQUE DIREITO: §cRebaixar Jogador",
                        "§7SHIFT + CLIQUE ESQUERDO: §4Expulsar Jogador"
                );
                ownerView(items, cPlayer, player, clan, member, playerInfo);
            }

        });

        ItemStack freeHead = CustomHeads.create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmFkYzA0OGE3Y2U3OGY3ZGFkNzJhMDdkYTI3ZDg1YzA5MTY4ODFlNTUyMmVlZWQxZTNkYWYyMTdhMzhjMWEifX19");
        ItemStack freeSlot = ItemStackUtils.customItem(freeHead, "§cVaga Livre", 1, " §7Convide jogadores para o seu clan!");
        for (int i = clan.getPlayers().size(); i < clan.getMaxMembers(); i++) {
            items.add(() -> {
                return InventoryItem.of(freeSlot);
            });
        }

        return items;
    }

    private static void memberView(List<InventoryItemSupplier> items, ItemStack playerInfo) {
        items.add(() -> InventoryItem.of(playerInfo));
    }

    private static void ownerView(List<InventoryItemSupplier> items, Member cPlayer, Player player, Clan clan, Member member, ItemStack playerInfoOwner) {
        items.add(() -> InventoryItem.of(playerInfoOwner).callback(ClickType.LEFT, c -> {

            if (member.equals(cPlayer)) {
                player.sendMessage("§cVocê não pode promover a si mesmo!");
                return;
            }

            switch (member.getRole()) {
                case LIDER:
                    player.sendMessage("§cVocê não pode promover o Líder!");
                    break;
                case MEMBRO:
                    member.setRole(ClanRole.RECRUTADOR);
                    clan.broadcast("§a" + member.getBukkitPlayer().getName() + "§7 foi promovido a §aRecrutador§7!");
                    break;
                case RECRUTADOR:
                    member.setRole(ClanRole.OFICIAL);
                    clan.broadcast("§a" + member.getBukkitPlayer().getName() + "§7 foi promovido a §aOFICIAL§7!");
                    break;
                case OFICIAL:
                    player.sendMessage("§cO jogador já atingiu o cargo máximo!");
                    break;
            }
        }).callback(ClickType.RIGHT, c -> {

            if (member.equals(cPlayer)) {
                player.sendMessage("§cVocê não pode rebaixar a si mesmo!");
                return;
            }

            switch (member.getRole()) {
                case LIDER:
                    player.sendMessage("§cVocê não pode rebaixar o Líder!");
                    break;
                case MEMBRO:
                    player.sendMessage("§cO jogador já atingiu o cargo mínimo!");
                    break;
                case RECRUTADOR:
                    member.setRole(ClanRole.MEMBRO);
                    clan.broadcast("§a" + member.getBukkitPlayer().getName() + "§7 foi rebaixado a §aMEMBRO§7!");
                    break;
                case OFICIAL:
                    member.setRole(ClanRole.RECRUTADOR);
                    clan.broadcast("§a" + member.getBukkitPlayer().getName() + "§7 foi rebaixado a §aRecrutador§7!");
                    break;
            }
        }).callback(ClickType.SHIFT_LEFT, c -> {

            if (member.equals(cPlayer)) {
                player.sendMessage("§cVocê não pode expulsar a si mesmo!");
                return;
            }

            if (member.getRole().equals(ClanRole.LIDER)) {
                player.sendMessage("§cVocê não pode expulsar o Líder!");
                return;
            }
            clan.broadcast("§a" + member.getBukkitPlayer().getName() + "§7 foi expulso do clan!");
            clan.removePlayer(member);
        }));
    }
}
