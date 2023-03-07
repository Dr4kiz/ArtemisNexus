package me.dkz.plugin.artemis.artemisnexus.listener;

import me.dkz.plugin.artemis.artemisnexus.ClanManager;
import me.dkz.plugin.artemis.artemisnexus.Main;
import me.dkz.plugin.artemis.artemisnexus.clan.Member;
import me.dkz.plugin.artemis.artemisnexus.clan.Clan;
import me.dkz.plugin.artemis.artemisnexus.clan.gui.ClanGui;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class NexusListener implements Listener {
    private Main plugin = Main.getInstance();
    private ClanManager clanManager = plugin.getClanManager();
    @EventHandler
    void onInteractEvent(PlayerInteractAtEntityEvent e){
        if(e.getRightClicked() instanceof EnderCrystal){
            e.setCancelled(true);
            Member member = clanManager.getPlayer(e.getPlayer().getUniqueId());
            if(!member.hasClan()) return;
            Clan clan = member.getClan();
            if(!clan.hasNexus()) return;
            if(clan.getNexus().getLocation().equals(e.getRightClicked().getLocation())){
                ClanGui clanGui = new ClanGui();
                clanGui.openInventory(e.getPlayer());
            }
        }
    }

}
