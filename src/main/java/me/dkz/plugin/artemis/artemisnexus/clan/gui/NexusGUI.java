package me.dkz.plugin.artemis.artemisnexus.clan.gui;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.CustomInventoryImpl;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.global.GlobalInventory;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.paged.PagedInventory;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.item.supplier.InventoryItemSupplier;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.henryfabio.minecraft.inventoryapi.viewer.impl.paged.PagedViewer;
import me.dkz.plugin.artemis.artemisnexus.ClanManager;
import me.dkz.plugin.artemis.artemisnexus.Main;
import me.dkz.plugin.artemis.artemisnexus.clan.Member;
import me.dkz.plugin.artemis.artemisnexus.clan.Clan;
import me.dkz.plugin.artemis.artemisnexus.clan.gui.filters.ClanLetterHeads;
import me.dkz.plugin.artemis.artemisnexus.utils.CustomHeads;
import me.dkz.plugin.artemis.artemisnexus.utils.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class NexusGUI extends SimpleInventory {

    private CustomInventoryImpl last;
    private Main plugin = Main.getInstance();
    private ClanManager clanManager = plugin.getClanManager();

    public NexusGUI(CustomInventoryImpl last) {
        super("me.dkz.nexusgui",  "§7Menu do Nexus", 9 * 5);
        this.last = last;
        configuration(configuration -> {
            configuration.secondUpdate(1);
        });
    }


    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {
        editor.setItem(36, InventoryItem.of(ItemStackUtils.customItem(Material.SIGN, "&7Voltar", 1, "&8Clique para voltar")).callback(ClickType.LEFT, (i) ->{
            last.openInventory(viewer.getPlayer());
        }));

        Member member = clanManager.getPlayer(viewer.getPlayer().getUniqueId());
        Clan clan = member.getClan();


        float recovery = clan.getNexus().getMaxHealth() - clan.getNexus().getHealth();
        float price = recovery/2;
        ItemStack heart = ItemStackUtils.customItem(CustomHeads.create(ClanLetterHeads.getTexture("HEART")),
                "§cRecuperar Vida",
                1,
                "",
                "§7Nome: &cRecuperação de vida",
                " §7Recupere &a❤"+recovery+" de vida &7do Nexus",
                " §7Preço: &b"+price+" Energias &7ou &a"+price*50+" coins",
                "",
                " §7CLIQUE ESQUERDO: §eRecuperar TUDO | §bEnergia",
                " §7CLIQUE+SHIFT: Recuperar §a❤ 500 | §bEnergia",
                " §7CLIQUE DIREITO: §eRecuperar TUDO | §aCoins");

        editor.setItem(29, InventoryItem.of(heart).callback(ClickType.LEFT, (i) ->{
            if (clan.getNexus().getHealth() == clan.getNexus().getMaxHealth()) {
                viewer.getPlayer().sendMessage("§cO Nexus já está com a vida cheia!");
                return;
            }
//            if (clan.getCoins() >= price*50) {
//                clan.setCoins(clan.getCoins() - price*50);
//                clan.getNexus().setHealth(clan.getNexus().getHealth() + recovery);
//                viewer.getPlayer().sendMessage("§aVocê recuperou §b"+recovery+" de vida §ado Nexus!");
//                return;
//            }
            if (member.getEnergy() >= price) {
                member.setEnergy(member.getEnergy() - (int) price);
                clan.getNexus().setHealth(clan.getNexus().getHealth() + recovery);
                viewer.getPlayer().sendMessage("§eVocê recuperou §a❤"+((int)recovery)+"§e de vida do Nexus!");
                return;
            }
            viewer.getPlayer().sendMessage("§cVocê não tem energia suficiente para recuperar a vida do Nexus!");
        }).callback(ClickType.SHIFT_LEFT, i ->{
            if(clan.getNexus().getHealth() == clan.getNexus().getMaxHealth()) {
                viewer.getPlayer().sendMessage("§cO Nexus já está com a vida cheia!");
                return;
            }

            if(clan.getNexus().getHealth() + 500 > clan.getNexus().getMaxHealth()) {
                viewer.getPlayer().sendMessage("§cLimite de vida do Nexus atingido");
                return;
            }

            int half = (int) (clan.getNexus().getMaxHealth() - 500)/2;
            if (member.getEnergy() >= half) {
                member.setEnergy(member.getEnergy() - half);
                clan.getNexus().setHealth(clan.getNexus().getHealth() + 500);
                viewer.getPlayer().sendMessage("§eVocê recuperou §a❤500§e de vida do Nexus!");
                return;
            }
            viewer.getPlayer().sendMessage("§cVocê não tem energia suficiente para recuperar a vida do Nexus!");
        }));

        InventoryItem weakness = InventoryItem.of(ItemStackUtils.customItem(CustomHeads.create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjAxYzI5M2RmNGM5ZGIzYjZmNjNlMzQzNDIwMWU2ZjQ1NGRkMmRkZjAxMDkyZDFhMjQ3NjczZDAzMDBiMjJiOCJ9fX0="),
                "§8Weakness "+(clan.getEffects().contains("WEAKNESS") ? "§a(Ativo)" : ""), 1,
                "",
                " §7Nome: &8Efeito de Fraqueza",
                " §8Aplique fraqueza por &a30 segundos &8a",
                " &8quem atacar o nexus.",
                " §7Preço: &b1.000 Energias",
                "")).callback(ClickType.LEFT, (i) -> {
            applyEffect(member, "weakness", 1000);
        });
        editor.setItem(30, weakness);

        InventoryItem blind = InventoryItem.of(ItemStackUtils.customItem(CustomHeads.create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjAxYzI5M2RmNGM5ZGIzYjZmNjNlMzQzNDIwMWU2ZjQ1NGRkMmRkZjAxMDkyZDFhMjQ3NjczZDAzMDBiMjJiOCJ9fX0="),
                "§8Blindness "+(clan.getEffects().contains("BLINDNESS") ? "§a(Ativo)" : ""), 1,
                "",
                " §7Nome: &8Efeito de Cegueira",
                " §8Aplique Cegueira por &a30 segundos &8a",
                " &8quem atacar o nexus.",
                " §7Preço: &b2.000 Energias",
                "")).callback(ClickType.LEFT, (i) -> {
            applyEffect(member, "blindness", 2000);
        });
        editor.setItem(31, levelCheck(clan, 2, blind, "Efeito Cegueira"));

        InventoryItem wither  = InventoryItem.of(ItemStackUtils.customItem(CustomHeads.create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzM3YTRmYTVlOWM0ZmU0MDg5MWU1YWMwNmEwMzM3YWRmMDIwMDVjMDAzOWVmODg1ZWE2M2MzNTI4YTZhNzRiMSJ9fX0="),
                "&8Wither "+(clan.getEffects().contains("WITHER") ? "§a(Ativo)" : ""), 1,
                "",
                " §7Nome: &8Efeito do Wither ",
                " §8Aplique efeito Wither por &a30 segundos &8a",
                " &8quem atacar o nexus.",
                " §7Preço: &b3.500 Energias")).callback(ClickType.LEFT, (i) -> {
            applyEffect(member, "wither", 3500);
        });
        editor.setItem(32, levelCheck(clan, 4, wither, "Efeito Wither"));

        InventoryItem shield  = InventoryItem.of(ItemStackUtils.customItem(CustomHeads.create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjUyNTU5ZjJiY2VhZDk4M2Y0YjY1NjFjMmI1ZjJiNTg4ZjBkNjExNmQ0NDY2NmNlZmYxMjAyMDc5ZDI3Y2E3NCJ9fX0="),
                "&bEscudo "+(clan.hasShield() ? "§a(Ativo)" : ""), 1,
                "",
                " §7Nome: &bEscudo do Nexus" ,
                " §eAplique um escudo que &bresiste &ea todos os",
                " &eataques, quebrando apenas para uma &cExplosão",
                " §7Preço: &b12.000 Energias",
                "")).defaultCallback(i ->{

            if(!clan.hasShield()){
                if (member.getEnergy() >= 12000) {
                    clan.setShield(true);
                    clan.broadcast("&7" + i.getPlayer().getDisplayName() + " comprou o §6escudo do nexus §7por §b12.000 Energias");
                    i.getPlayer().closeInventory();
                    member.setEnergy(member.getEnergy() - 12000);
                }else{
                    i.getPlayer().sendMessage("§cVocê não possuí energia suficiente.");
                }
            }else{
                i.getPlayer().sendMessage("§cO nexus já está protegido");
            }

        });
        editor.setItem(33, levelCheck(clan, 6, shield, "Escudo do Nexus"));

    }


    public InventoryItem levelCheck(Clan clan, int necessaryLevel, InventoryItem item, String name) {
        if (clan.getLevel() >= necessaryLevel) {
            return item;
        } else {
            InventoryItem locked = InventoryItem.of(ItemStackUtils.customItem(CustomHeads.create(ClanLetterHeads.getTexture("DISABLED")),
                    "§cEfeito Bloqueado",
                    1,
                    " §7Nome: &c"+name,
                    " §7Nível necessário: &c"+necessaryLevel));
            return locked;
        }
    }


    private void applyEffect(Member cplayer, String effect, int price) {
        Player player = cplayer.getBukkitPlayer().getPlayer();
        if (cplayer.getEnergy() >= price) {
            Clan clan = cplayer.getClan();
            if (!clan.getEffects().contains(effect.toUpperCase())) {

                clan.getEffects().add(effect.toUpperCase());
                clan.broadcast("&7" + player.getDisplayName() + " comprou o efeito &8" + effect.toUpperCase() + " &7por &b" + price + " Energias");
                player.closeInventory();
                cplayer.setEnergy(cplayer.getEnergy() - price);
            } else {
                player.sendMessage("§cSeu clan já possuí esse efeito.");
            }
        } else {
            player.sendMessage("§cVocê não possuí energia suficiente.");
        }
    }


}
