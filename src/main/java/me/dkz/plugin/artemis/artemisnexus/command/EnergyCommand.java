package me.dkz.plugin.artemis.artemisnexus.command;

import me.dkz.plugin.artemis.artemisnexus.ClanManager;
import me.dkz.plugin.artemis.artemisnexus.Main;
import me.dkz.plugin.artemis.artemisnexus.clan.Member;
import me.dkz.plugin.artemis.artemisnexus.utils.StringUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class EnergyCommand implements CommandExecutor, TabCompleter {
    private Main plugin = Main.getInstance();
    private ClanManager clanManager = plugin.getClanManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {



        if (args.length == 0) {
            sender.sendMessage("§cUse /nexus ajuda");
            return false;
        }

        if (args.length < 3) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cUse /energia <adicionar|remover|ver> <quantidade> <player>");
                return false;
            }
            Player target = (Player) sender;
            Member cplayer = clanManager.getPlayer(target.getUniqueId());

            if (args[0].equalsIgnoreCase("ver")) {
                target.sendMessage("§eVocê possui §b" + StringUtils.format5(cplayer.getEnergy()) + " §eenergias.");
                return false;
            }


            if (!sender.hasPermission("artemisnexus.staff")) {
                sender.sendMessage("§cVocê não tem permissão para executar esse comando.");
                return false;
            }

            if(args.length < 2){
                sender.sendMessage("§cUse /energia <adicionar|remover> <quantidade>");
                return false;
            }

            long amount = 0;
            try {
                amount = Long.parseLong(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage("§cInsira um valor válido para a quantidade.");
                return false;
            }
            String amountString = StringUtils.format5(amount);

            if (args[0].equalsIgnoreCase("adicionar")) {
                if (args.length < 2) {
                    sender.sendMessage("§cUse /energia adicionar <quantidade> <player>");
                    return false;
                }
                cplayer.setEnergy(cplayer.getEnergy() + amount);
                target.sendMessage("§eVocê recebeu §b" + amountString + " §eenergias.");
                return false;
            }

            if (args[0].equalsIgnoreCase("remover")) {
                if (args.length < 2) {
                    sender.sendMessage("§cUse /energia remover <quantidade> <player>");
                    return false;
                }
                cplayer.setEnergy(cplayer.getEnergy() - amount);
                target.sendMessage("§cVocê perdeu §b" + amountString + " §cenergias.");
                return false;
            }


            return false;
        } else {
            OfflinePlayer target = plugin.getServer().getOfflinePlayer(args[2]);

            if (target == null) {
                sender.sendMessage("§cO jogador '" + args[2] + "' não foi encontrado.");
                return false;
            }

            Member cplayer = clanManager.getPlayer(target.getUniqueId());
            long amount = 0;
            try {
                amount = Long.parseLong(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage("§cInsira um valor válido para a quantidade.");
                return false;
            }
            String amountString = StringUtils.format5(amount);

            if (args[0].equalsIgnoreCase("adicionar")) {
                if (args.length < 2) {
                    sender.sendMessage("§cUse /energia adicionar <quantidade> <player>");
                    return false;
                }
                cplayer.setEnergy(cplayer.getEnergy() + amount);
                if (target.getPlayer() != null) {
                    target.getPlayer().sendMessage("§eVocê recebeu §b" + amountString + " §eenergias.");
                }
                sender.sendMessage("§aVocê adicionou §b" + amountString + "§a energias para §b" + target.getName());
                return false;
            }

            if (args[0].equalsIgnoreCase("remover")) {
                if (args.length < 2) {
                    sender.sendMessage("§cUse /energia remover <quantidade> <player>");
                    return false;
                }
                cplayer.setEnergy(cplayer.getEnergy() - amount);
                if (target.getPlayer() != null) {

                    target.getPlayer().sendMessage("§cVocê perdeu §b" + amountString + " §cenergias.");
                }
                sender.sendMessage("§aVocê removeu §b" + amountString + "§a energias de §b" + target.getName());
                return false;
            }

            if (args[0].equalsIgnoreCase("ver")) {

                sender.sendMessage("§aO jogador §b" + target.getName() + "§a possui §b" + StringUtils.format5(cplayer.getEnergy()) + "§a de energia.");
                return false;
            }
        }


        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {

        if(commandSender.hasPermission("artemisnexus.staff")){
            return Arrays.asList("adicionar", "remover", "ver");
        }else{
            return Arrays.asList("ver");
        }
    }
}
