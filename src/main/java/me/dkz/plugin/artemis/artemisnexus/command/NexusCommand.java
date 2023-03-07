package me.dkz.plugin.artemis.artemisnexus.command;

import me.dkz.plugin.artemis.artemisnexus.ClanManager;
import me.dkz.plugin.artemis.artemisnexus.Main;
import me.dkz.plugin.artemis.artemisnexus.clan.Member;
import me.dkz.plugin.artemis.artemisnexus.clan.Clan;
import me.dkz.plugin.artemis.artemisnexus.clan.role.ClanRole;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class NexusCommand {
    private static Main plugin = Main.getInstance();
    private static ClanManager clanManager = plugin.getClanManager();

    public static boolean spawnNexus(Clan clan, Member cplayer){
        Player player = cplayer.getBukkitPlayer().getPlayer();
        if(!cplayer.getRole().equals(ClanRole.LIDER)){
            player.sendMessage("§cApenas o líder do clan pode criar o nexus.");
            return false;
        }

        if(clan.getNexus().hasNexusInLocation()){
            player.getPlayer().sendMessage("§cO nexus do clan já foi criado.");
            return false;
        }

        Chunk chunk = player.getLocation().getChunk();
        if(clanManager.getClanByChunk(chunk) != null){
            player.sendMessage("§cEsse terreno já pertence a um clan.");
            return false;
        }

        if(player.getLocation().add(0, -1,0).getBlock().equals(null) || player.getLocation().add(0, -1, 0).getBlock().getType().equals(Material.AIR)){
            player.sendMessage("§cVocê não pode criar o nexus aqui.");
            return false;
        }

        clan.getNexus().spawnNexus(player.getLocation());
        player.sendMessage("§a§lSUCESSO! §eO nexus do clan foi criado");

        return false;
    }

}
