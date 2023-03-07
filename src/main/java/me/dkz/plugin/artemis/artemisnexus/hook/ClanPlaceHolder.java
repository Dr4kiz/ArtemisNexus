package me.dkz.plugin.artemis.artemisnexus.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.dkz.plugin.artemis.artemisnexus.Main;
import me.dkz.plugin.artemis.artemisnexus.clan.Clan;
import me.dkz.plugin.artemis.artemisnexus.clan.Member;
import me.dkz.plugin.artemis.artemisnexus.utils.StringUtils;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ClanPlaceHolder extends PlaceholderExpansion {

    private Main plugin = Main.getInstance();

    @Override
    public String getIdentifier() {
        return "ArtemisNexus";
    }

    @Override
    public String getAuthor() {
        return "Drakiz";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        Member member = plugin.getClanManager().getPlayer(player.getUniqueId());
        Clan clan = member.getClan();

        if (identifier.equalsIgnoreCase("clan_name")) {
            return member.hasClan() ? clan.getName() : "§7Nenhum";
        }

        if (identifier.equalsIgnoreCase("clan_tag")) {
            return member.hasClan() ? "["+clan.getTag()+"] " : "";
        }

        if (identifier.equalsIgnoreCase("clan_role")) {

            return member.hasClan() ? member.getRole().name() : "-";
        }

        if (identifier.equalsIgnoreCase("clan_chunk")) {
            return plugin.getClanManager().getClanByChunk(player.getLocation().getChunk()) != null ? plugin.getClanManager().getClanByChunk(player.getLocation().getChunk()).getName() : "§aÁrea Livre";
        }

        if (identifier.equalsIgnoreCase("clan_energy")) {
            return StringUtils.format5(member.getEnergy());
        }


        return "Inválido";
    }
}
