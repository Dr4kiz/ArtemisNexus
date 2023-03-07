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
import me.dkz.plugin.artemis.artemisnexus.InviteManager;
import me.dkz.plugin.artemis.artemisnexus.clan.Invite;
import me.dkz.plugin.artemis.artemisnexus.utils.ItemStackUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class InvitesGui extends PagedInventory {
    private Main plugin = Main.getInstance();
    private ClanManager clanManager = plugin.getClanManager();
    private InviteManager inviteManager = plugin.getClanInvite();

    private CustomInventoryImpl lastInventory;

    public InvitesGui(CustomInventoryImpl lastInventory) {
        super("me.dkz.plugin.artemis.artemisnexus.clan.gui.invites", "§7Convites de Clans", 9 * 4);
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
        List<InventoryItemSupplier> itemSuppliers = new LinkedList<>();
        Member member = clanManager.getPlayer(viewer.getPlayer().getUniqueId());
        Player player = viewer.getPlayer();
        for (Invite invite : inviteManager.getInvites(player)) {
            Clan c = clanManager.getById(invite.getClan());
            ItemStack item = ItemStackUtils.customItem(Material.PAPER, "§eConvite de Clan", 1,
                    "§7Você foi convidado para o clan §e" + c.getName(),
                    "",
                    "§7Enviado por: §e" + Bukkit.getOfflinePlayer(invite.getSender()).getName(),
                    "&7Enviado a: &a" +formatDate(invite.getDate()),
                    "",
                    "&7CLIQUE ESQUERDO: §aAceitar",
                    "&7CLIQUE DIREITO: §cRecusar");

            itemSuppliers.add(() -> {
                return InventoryItem.of(item).callback(ClickType.LEFT, e -> {
                    member.setClan(c.getID());
                    Clan clan = clanManager.getById(c.getID());
                    clan.broadcast("§a[+] " + player.getName() + " entrou no clan");
                    inviteManager.removeInvite(player, c);
                    player.closeInventory();
                }).callback(ClickType.RIGHT, e -> {
                    inviteManager.removeInvite(player, c);
                    player.sendMessage("§cVocê recusou o convite do clan " + c.getName());
                    player.closeInventory();
                });
            });

        }

        return itemSuppliers;
    }


    private String formatDate(Date date){

        int dateNow = (int) (System.currentTimeMillis() / 1000);
        int dateSend = (int) (date.getTime() / 1000);

        int seconds = dateNow - dateSend;
        int minutes = seconds / 60;
        int hours = minutes / 60;
        return hours + "h " + minutes + "m " + seconds + "s";
    }
}
