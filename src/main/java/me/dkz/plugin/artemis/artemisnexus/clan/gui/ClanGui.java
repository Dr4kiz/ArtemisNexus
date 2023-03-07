package me.dkz.plugin.artemis.artemisnexus.clan.gui;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import me.dkz.plugin.artemis.artemisnexus.ClanManager;
import me.dkz.plugin.artemis.artemisnexus.InviteManager;
import me.dkz.plugin.artemis.artemisnexus.Main;
import me.dkz.plugin.artemis.artemisnexus.clan.Member;
import me.dkz.plugin.artemis.artemisnexus.clan.Clan;
import me.dkz.plugin.artemis.artemisnexus.clan.role.ClanRole;
import me.dkz.plugin.artemis.artemisnexus.utils.CustomHeads;
import me.dkz.plugin.artemis.artemisnexus.utils.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class ClanGui extends SimpleInventory {

    private Main plugin = Main.getInstance();
    private ClanManager clanManager = plugin.getClanManager();
    private InviteManager inviteManager = plugin.getClanInvite();
    private Inventory inventory;
    private Set<Player> queue = new HashSet<>();

    public ClanGui() {
        super("dkz.clan.gui", "§7Menu do Clan", 9 * 3);
        configuration(configuration -> {
            configuration.secondUpdate(1);
        });
    }


    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {
        Member member = clanManager.getPlayer(viewer.getPlayer().getUniqueId());
        Player player = viewer.getPlayer();
        if (!member.hasClan()) {
            ItemStack create = ItemStackUtils.customItem(Material.BANNER, "§aCriar Clan", 1, "§7Clique para o seu clan", "§7e convidar amigos!");
            editor.setItem(12, InventoryItem.of(create).defaultCallback(e -> {
                player.performCommand("clan criar");
                player.closeInventory();
            }));

            ItemStack join = ItemStackUtils.customItem(Material.PAPER, "§eConvites de Clans",
                    inviteManager.getInvites(player).size(), "§7Você possuí §e" + inviteManager.getInvites(player).size() + "§7 convites de clans", "§7Clique para gerenciar seus convites.");
            editor.setItem(14, InventoryItem.of(join).defaultCallback(e -> {
                InvitesGui invitesGui = new InvitesGui(this);
                invitesGui.openInventory(player);
            }));
        } else {
            Clan clan = member.getClan();
            if(clan == null){
                player.sendMessage("§cSeu clan não foi encontrado, por favor, relogue.");
                return;
            }

            ItemStack info = ItemStackUtils.customItem(
                    Material.BOOK, "§aInformações do Clan",
                    1,
                    "§e[" + clan.getTag() + "] §e" + clan.getName(),
                    " §7Membros: §e(" + clan.getPlayers().size() + "/" + clan.getMaxMembers() + ")",
                    " §7Terrenos: &e(" + clan.getChunks().size() + "/" + clan.getMaxChunks() + ")",
                    "",
                    "§7Descrição:",
                    "§8 " + clan.getDescription()
            );
            editor.setItem(11, InventoryItem.of(info).defaultCallback(c ->{
                if(member.getRole().equals(ClanRole.LIDER) || member.getRole().equals(ClanRole.OFICIAL)) {
                    clanManager.getDescqueue().put(player, clan);
                    player.sendMessage("§eDigite a nova §LDESCRIÇAO §edo seu clan ou §C§lCANCELAR §epara sair");
                    player.closeInventory();
                }
            }));

            ItemStack playerHead = ItemStackUtils.getHead(player);
            ItemStack playerInfo = ItemStackUtils.customItem(playerHead,
                    "§eSuas informações", 1,
                    " §7Seus Abates: §e" + member.getKills(),
                    " §7Suas Mortes: §c" + member.getDeaths(),
                    " §7Seu K/D: §a" + member.getKd(),
                    "",
                    " &eEnergia: &b"+ member.getFormatEnergy(),
                    " &eCargo: &7"+ member.getRole().toString(),
                    " &eEntrada: &7"+ member.getFormatJoinDate());
            editor.setItem(12, InventoryItem.of(playerInfo));

            ItemStack membersHead = CustomHeads.create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmIyZjAwMDBjOTYzYmVkYTkzYTdiODdlNjhkZTkzYTllZDhjYjhhZjViMGU2MzkyMjVkMWQwYjQyY2VkZTc3MSJ9fX0=");
            ItemStack members = ItemStackUtils.customItem(membersHead, "§eMembros do Clan", 1, "§7Clique para ver os membros do seu clan.");
            editor.setItem(14, InventoryItem.of(members).defaultCallback(e -> {
                MembersGui membersGui = new MembersGui(this);
                membersGui.openInventory(player);
            }));

            ItemStack topClansHead = CustomHeads.create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmE1MzYxYjUyZGFmNGYxYzVjNTQ4MGEzOWZhYWExMDg5NzU5NWZhNTc2M2Y3NTdiZGRhMzk1NjU4OGZlYzY3OCJ9fX0=");
            ItemStack topClans = ItemStackUtils.customItem(topClansHead, "§eTop Clans", 1, "§7Clique para ver os melhores clans do servidor.");

            editor.setItem(15, InventoryItem.of(topClans).defaultCallback(e -> {
                TopClansGui topClansGui = new TopClansGui(this);
                topClansGui.openInventory(player);
            }));


            ItemStack nexusGui = ItemStackUtils.customItem(CustomHeads.create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjc0ODEzOWVlNmNmZjc0NGM3ZTg5NTkzZjZiOTBkNDYwNDRkMDdlZTVlNjM4MjhmYmU5NTMxZmI2NmRmOWI4ZiJ9fX0="),
                    "§bConfigurações do Nexus", 1,
                    clan.hasNexus() ? "§7Clique para gerenciar o seu nexus." : "§7Clique para criar o seu nexus.");
            editor.setItem(16, InventoryItem.of(nexusGui).defaultCallback(e -> {
                if(clan.hasNexus()) {
                    NexusGUI nexusGui1 = new NexusGUI(this);
                    nexusGui1.openInventory(player);
                } else {
                    if(!member.getRole().equals(ClanRole.LIDER)){
                        player.sendMessage("§cVocê não tem permissão para criar um nexus.");
                        return;
                    }
                    player.performCommand("clan nexus");
                    player.closeInventory();
                }
            }));
            }

        }

}
