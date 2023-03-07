package me.dkz.plugin.artemis.artemisnexus;

import me.dkz.plugin.artemis.artemisnexus.clan.Clan;
import me.dkz.plugin.artemis.artemisnexus.clan.Invite;
import me.dkz.plugin.artemis.artemisnexus.utils.StringUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;

public class InviteManager {

    private HashMap<OfflinePlayer, HashSet<Invite>> invites = new HashMap<>();


    public void addInvite(OfflinePlayer sender, OfflinePlayer target, Clan clan) {
        Invite invite = new Invite(clan.getID(), sender.getUniqueId(), target.getUniqueId());
        HashSet<Invite> invites = this.invites.getOrDefault(target, new HashSet<>());
        invites.add(invite);
        this.invites.put(target, invites);
    }


    public void removeInvite(OfflinePlayer player, Clan clan) {
        HashSet<Invite> invites = this.invites.getOrDefault(player, new HashSet<>());
        invites.removeIf(invite -> invite.getClan().equals(clan.getID()));
        this.invites.put(player, invites);
    }

    public void removeInvites(OfflinePlayer player) {
        invites.remove(player);
    }

    public boolean hasInvite(OfflinePlayer player, Clan clan) {
        return invites.getOrDefault(player, new HashSet<>()).stream().anyMatch(invite -> invite.getClan().equals(clan.getID()));
    }

    public HashSet<Invite> getInvites(OfflinePlayer player) {
        return invites.getOrDefault(player, new HashSet<>());
    }

    public void send(OfflinePlayer player, Clan clan) {
        if (Bukkit.getPlayer(player.getUniqueId()) != null) {
            Player p = Bukkit.getPlayer(player.getUniqueId());
            p.sendMessage("");
            p.sendMessage("§eVocê recebeu um convite de clan!");
            p.sendMessage("");
            p.sendMessage("§7| Clan: §c" + clan.getClanName());
            p.sendMessage("§7| Membros: §e(" + clan.getPlayers().size() + "/" + clan.getMaxMembers()+")");
            p.sendMessage("§7| Nível: §a" + clan.getLevel());
            p.sendMessage("§7| Energia: §b" + StringUtils.format5(clan.getTotalEnergy()));
            p.sendMessage("");
            TextComponent open = new TextComponent("  §a§l[ABRIR]");
            open.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClique para ver seus convites").create()));
            open.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan convites"));
            TextComponent ignore = new TextComponent("§c§l[IGNORAR]");
            ignore.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cClique para ignorar").create()));
            ignore.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan ignorar " + clan.getID()));
            open.addExtra(" ");
            open.addExtra(ignore);
            p.spigot().sendMessage(open);
            p.sendMessage("");

        }
    }

    public void sendInvite(OfflinePlayer invite, OfflinePlayer target, Clan clan) {
        addInvite(invite, target, clan);
        send(target, clan);
    }
}
