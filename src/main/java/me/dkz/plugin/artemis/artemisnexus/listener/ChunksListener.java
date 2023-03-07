package me.dkz.plugin.artemis.artemisnexus.listener;

import me.dkz.plugin.artemis.artemisnexus.ClanManager;
import me.dkz.plugin.artemis.artemisnexus.Main;
import me.dkz.plugin.artemis.artemisnexus.clan.Member;
import me.dkz.plugin.artemis.artemisnexus.clan.Clan;
import me.dkz.plugin.artemis.artemisnexus.utils.TitleUtils;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

public class ChunksListener implements Listener {

    private Main plugin = Main.getInstance();
    private ClanManager clanManager = plugin.getClanManager();


    @EventHandler
    void onPlayerInteract(PlayerInteractEvent e) {
        Chunk chunk = e.getPlayer().getLocation().getChunk();
        Player player = e.getPlayer();
        if(e.getAction().equals(Action.RIGHT_CLICK_AIR)) return;
        if(e.getAction().equals(Action.LEFT_CLICK_AIR)) return;
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;
        e.setCancelled(!canInteract(player, chunk));
    }

    @EventHandler
    void onPlayerBreak(BlockBreakEvent e) {
        Chunk chunk = e.getPlayer().getLocation().getChunk();
        Player player = e.getPlayer();
        e.setCancelled(!canInteract(player, chunk));
    }

    @EventHandler
    void onPlayerPlace(BlockPlaceEvent e) {
        Chunk chunk = e.getPlayer().getLocation().getChunk();
        Player player = e.getPlayer();
        e.setCancelled(!canInteract(player, chunk));
    }

    private HashMap<Player, Chunk> lastChunk = new HashMap<>();

    @EventHandler
    void alertChunkChange(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Chunk chunk = player.getLocation().getChunk();
        if(!lastChunk.containsKey(player)) lastChunk.put(player, chunk);
        if(lastChunk.get(player) != chunk) {
            if(isClanChunk(chunk)) {
                Clan clan = clanManager.getClanByChunk(chunk);
                Clan lastClan = clanManager.getClanByChunk(lastChunk.get(player));
                if(lastClan == null) {
                    TitleUtils.send(player, 10, 35, 10, "§e[" + clan.getTag() + "] - " + clan.getName(), "§7" + String.join(",", clan.getDescription()).replaceAll(",", " "));
                    TitleUtils.sendActionText(player, "§eTerritório do clan §7" + clan.getClanName());
                } else {
                    if(lastClan.getID() != clan.getID()) {
                        TitleUtils.send(player, 10, 35, 10, "§e[" + clan.getTag() + "] - " + clan.getName(), "§7" + String.join(",", clan.getDescription()).replaceAll(",", " "));
                        TitleUtils.sendActionText(player, "§eTerritório do clan §7" + clan.getClanName());
                    }
                }
            } else {
                if(lastChunk.get(player) != null){
                    if(isClanChunk(lastChunk.get(player))) {
                        TitleUtils.send(player, 10, 35, 10, "§eÁrea Livre", "§aTerritório neutro");
                        TitleUtils.sendActionText(player,  "§7Território neutro");
                    }
                }
            }
            lastChunk.put(player, chunk);
        }
    }




    public boolean canInteract(Player player, Chunk chunk) {
        if(chunk == null) return true;
        Member member = clanManager.getPlayer(player.getUniqueId());
        if (!isClanChunk(chunk)) return true;
        if (!member.hasClan()) return false;
        Clan clan = member.getClan();
        if (clan.hasChunk(chunk)) return true;
        TitleUtils.sendActionText(player, "§cVocê não pode interagir com esse terreno");
        return false;
    }

    public boolean isClanChunk(Chunk chunk) {
        return clanManager.getClanByChunk(chunk) != null;
    }

}
