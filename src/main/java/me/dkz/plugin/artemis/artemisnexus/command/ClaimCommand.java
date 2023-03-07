package me.dkz.plugin.artemis.artemisnexus.command;

import me.dkz.plugin.artemis.artemisnexus.ClanManager;
import me.dkz.plugin.artemis.artemisnexus.Main;
import me.dkz.plugin.artemis.artemisnexus.clan.Member;
import me.dkz.plugin.artemis.artemisnexus.clan.Clan;
import me.dkz.plugin.artemis.artemisnexus.clan.role.ClanRole;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ClaimCommand {
    private static Main plugin = Main.getInstance();
    private static ClanManager clanManager = plugin.getClanManager();
    public static boolean claim(Player player, Member member) {
        Clan clan = member.getClan();
        System.out.println(member.getRole());
        if(member.getRole() != ClanRole.LIDER && member.getRole() != ClanRole.OFICIAL){
            player.sendMessage("§cVocê não tem permissão para proteger terrenos.");
            return false;
        }


        if(clan.getChunks().size() >= clan.getMaxChunks()){
            player.sendMessage("§cVocê não pode proteger mais terrenos.");
            return false;
        }

        Chunk chunk = player.getLocation().getChunk();

        if(clanManager.getClanByChunk(chunk) != null){
            player.sendMessage("§cEste terreno já está protegido.");
            return false;
        }


        if(clan.getChunks().size() == 0){
            player.sendMessage("§cSeu clan não possuí um nexus, posicione o nexus para proteger seu terreno.");
            return true;
        }


        int found = 0;
        for (int x = chunk.getX() - 1; x <= chunk.getX() + 1; x++) {
            for (int z = chunk.getZ() - 1; z <= chunk.getZ() + 1; z++) {
                Chunk c = chunk.getWorld().getChunkAt(x, z);
                if(clanManager.getClanByChunk(c) != null){
                    if(clan.hasChunk(c)){
                        clan.addChunk(chunk);
                        found = 1;
                        break;
                    }
                }
            }
        }

        if(found == 0){
            player.sendMessage("§cVocê não pode proteger este terreno.");
            return false;
        }
        clan.broadcast("§7" + member.getBukkitPlayer().getName() + " protegeu um novo terreno.");
        return true;
    }


    public static boolean sendMap(Member player){
        TextComponent[][] components = getChunks(player);
        Player p = Bukkit.getPlayer(player.getUuid());
        for (int i = 0; i < components.length; i++) {
            p.spigot().sendMessage(components[i]);
        }
        return true;
    }

    private static List<Chunk> getChunks(Chunk centerChunk, int radius) {
        List<Chunk> chunks = new ArrayList<>();
        for (int x = centerChunk.getX() - radius; x < centerChunk.getX() + radius; x++) {
            for (int z = centerChunk.getZ() - radius; z < centerChunk.getZ() + radius; z++) {
                Chunk chunk = centerChunk.getWorld().getChunkAt(x, z);
                chunks.add(chunk);
            }
        }
        return chunks;
    }

    private static TextComponent[][] getChunks(Member cplayer) {
        int range = 24;
        Player player = plugin.getServer().getPlayer(cplayer.getUuid());
        Location center = player.getLocation();
        List<Chunk> chunks = getChunks(center.getChunk(), range / 2);
        chunks.add(player.getLocation().getChunk());

        TextComponent text = new TextComponent("▇");

        ChatColor[][] colors = new ChatColor[range][range];
        TextComponent[][] components = new TextComponent[range][range];

        for (int x = 0; x < range; x++) {
            for (int z = 0; z < range; z++) {
                colors[x][z] = ChatColor.GRAY;
            }
        }
        Clan clan = null;

        for (Chunk chunk : chunks) {
            if (plugin.getClanManager().getClanByChunk(chunk) != null) {
                clan = plugin.getClanManager().getClanByChunk(chunk);
                if (cplayer.getClan().equals(clan.getID())) {
                    colors[chunk.getX() - center.getChunk().getX() + range / 2][chunk.getZ() - center.getChunk().getZ() + range / 2] = ChatColor.GREEN;
                } else {
                    colors[chunk.getX() - center.getChunk().getX() + range / 2][chunk.getZ() - center.getChunk().getZ() + range / 2] = ChatColor.DARK_RED;
                }
//                if (clan.isStaffClan()) {
//                    colors[chunk.getX() - center.getChunk().getX() + 8][chunk.getZ() - center.getChunk().getZ() + 8] = ChatColor.YELLOW;
//                }
            }

            if (chunk.equals(player.getLocation().getChunk())) {
                colors[chunk.getX() - center.getChunk().getX() + range / 2][chunk.getZ() - center.getChunk().getZ() + range / 2] = ChatColor.BLUE;
            }
        }
        for (int x = 0; x < range; x++) {
            for (int z = 0; z < range; z++) {
                components[x][z] = new TextComponent(text);
                if (colors[x][z] != null) {
                    if (plugin.getClanManager().getClanByChunk(chunks.get(x * range + z)) != null) {
                        clan = plugin.getClanManager().getClanByChunk(chunks.get(x * range + z));

                        components[x][z].setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§e"+clan.getClanName()).create()));

                    }
                }
                components[x][z].setColor(colors[x][z]);
                if (player.getLocation().getChunk().equals(chunks.get(x * range + z))) {
                    components[x][z].setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eVocê").create()));
                }
            }
        }
        return components;
    }
}
