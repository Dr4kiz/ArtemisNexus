package me.dkz.plugin.artemis.artemisnexus.command;

import me.dkz.plugin.artemis.artemisnexus.Main;
import me.dkz.plugin.artemis.artemisnexus.clan.Clan;
import me.dkz.plugin.artemis.artemisnexus.clan.Member;
import me.dkz.plugin.artemis.artemisnexus.clan.gui.ClanGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClanChat implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem executar este comando.");
            return false;
        }
        if(args.length < 1)
            return false;
        Player player = (Player) sender;
        Member member = Main.getInstance().getClanManager().getPlayer(player.getUniqueId());
        if(member.hasClan()){
            member.getClan().sendMessage(member, args);
        }else{
            ClanGui clanGui = new ClanGui();
            clanGui.openInventory(player);
        }
        return false;
    }
}
