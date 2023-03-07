package me.dkz.plugin.artemis.artemisnexus.command;

import javafx.scene.control.Tab;
import me.dkz.plugin.artemis.artemisnexus.ClanManager;
import me.dkz.plugin.artemis.artemisnexus.InviteManager;
import me.dkz.plugin.artemis.artemisnexus.Main;
import me.dkz.plugin.artemis.artemisnexus.clan.Member;
import me.dkz.plugin.artemis.artemisnexus.clan.Clan;
import me.dkz.plugin.artemis.artemisnexus.clan.gui.ClanGui;
import me.dkz.plugin.artemis.artemisnexus.clan.gui.InvitesGui;
import me.dkz.plugin.artemis.artemisnexus.clan.gui.MembersGui;
import me.dkz.plugin.artemis.artemisnexus.clan.gui.TopClansGui;
import me.dkz.plugin.artemis.artemisnexus.clan.role.ClanRole;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ClanCommand implements CommandExecutor, TabCompleter {

    private Main plugin = Main.getInstance();
    private ClanManager clanManager = plugin.getClanManager();
    private InviteManager inviteManager = plugin.getClanInvite();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        Member member = clanManager.getPlayer(player.getUniqueId());

        if(clanManager.getNamequeue().contains(player) || clanManager.getTagqueue().containsKey(player)){
            player.sendMessage("§cVocê já está em uma criação de clan.");
            return true;
        }

        if (args.length < 1) {
            ClanGui clanGui = new ClanGui();
            clanGui.openInventory(player);
            return true;
        }

        if (args[0].equals("criar")) {

            if (member.hasClan()) {
                ClanGui clanGui = new ClanGui();
                clanGui.openInventory(player);
                return true;
            }

            player.sendMessage("§eDigite o §lNOME§e do seu clan ou digite §c§lCANCELAR §epara cancelar a criação.");
            clanManager.getNamequeue().add(player);


        } else {

            if(args[0].equalsIgnoreCase("top")){
                TopClansGui topClansGui = new TopClansGui(new ClanGui());
                topClansGui.openInventory(player);
                return true;
            }

            if(args[0].equalsIgnoreCase("ignorar")){
                String name = args[1];
                Clan clan = clanManager.getById(UUID.fromString(name));
                if(clan == null){
                    player.sendMessage("§cClan não encontrado.");
                    return true;
                }


                if(!plugin.getClanInvite().hasInvite(player, clan)){
                    player.sendMessage("§cVocê não tem convites deste clan.");
                    return true;
                }

                plugin.getClanInvite().removeInvite(player, clan);
                player.sendMessage("§aVocê ignorou os convites do clan " + clan.getName() + ".");

                return true;

            }

            if(args[0].equalsIgnoreCase("convites")){
                InvitesGui invitesGui = new InvitesGui(new ClanGui());
                invitesGui.openInventory(player);
                return true;
            }




            if (!member.hasClan()) {
                ClanGui clanGui = new ClanGui();
                clanGui.openInventory(player);
                return true;

            }

            Clan clan = member.getClan();

            if(args[0].equalsIgnoreCase("membros")){
                MembersGui membersGui = new MembersGui(new ClanGui());
                membersGui.openInventory(player);
                return true;
            }

            if (args[0].equalsIgnoreCase("sair")) {
                if (member.getRole() == ClanRole.LIDER) {
                    player.sendMessage("§eVocê não pode sair do clan, pois é o líder, utilize /clan desfazer");
                    return true;
                }
                member.setClan(null);
                clan.broadcast("§c[-] " + player.getName() + " saiu do clan");
                player.sendMessage("§cVocê saiu do seu clan.");
                return true;
            }

            if(args[0].equalsIgnoreCase("convidar")){

                if(member.getRole().equals(ClanRole.MEMBRO)){
                    player.sendMessage("§cVocê não tem permissão para convidar jogadores.");
                    return true;
                }

                if(args.length < 2){
                    player.sendMessage("§cUtilize /clan convidar <jogador>");
                    return true;
                }

                OfflinePlayer target = plugin.getServer().getOfflinePlayer(args[1]);
                if(target == null){
                    player.sendMessage("§cJogador não encontrado.");
                    return true;
                }

                if(target.getUniqueId().equals(player.getUniqueId())){
                    player.sendMessage("§cVocê não pode convidar a si mesmo.");
                    return true;
                }

                if(clanManager.getPlayer(target.getUniqueId()).hasClan()){
                    player.sendMessage("§cEste jogador já está em um clan.");
                    return true;
                }
                if(inviteManager.hasInvite(target, clan)){
                    player.sendMessage("§cEste jogador já possui um convite.");
                    return true;
                }

                if(clan.getPlayers().size() >= clan.getMaxMembers()){
                    player.sendMessage("§cSeu clan já está cheio.");
                    return true;
                }

                inviteManager.sendInvite(player, target, clan);
                player.sendMessage("§aVocê convidou §e" + target.getName() + " §apara o seu clan.");
            }

            if (args[0].equalsIgnoreCase("desfazer")) {
                if (member.getRole() != ClanRole.LIDER) {
                    player.sendMessage("§cVocê não pode desfazer o clan, pois não é o líder.");
                    return true;
                }
                clan.broadcast("§4§lALERTA! §cO clan foi desfeito pelo líder.");
                clanManager.removeClan(member.getClan());

                return true;
            }

            if (args[0].equalsIgnoreCase("info")) {
                player.sendMessage(member.getClan().toString());
                return true;
            }

            if (args[0].equalsIgnoreCase("claimar")) {
                return ClaimCommand.claim(player, member);
            }

            if(args[0].equalsIgnoreCase("chunks")){
                if(!sender.hasPermission("clan.verchunks")){
                    sender.sendMessage("§cVocê não tem permissão para executar este comando.");
                    return true;
                }
                return ClaimCommand.sendMap(member);
            }

            if(args[0].equalsIgnoreCase("levelup")){

                if(!sender.hasPermission("clan.levelup")) {
                    sender.sendMessage("§cVocê não tem permissão para executar este comando.");
                    return true;
                }

                if(clan.getLevel() >= 75){
                    sender.sendMessage("§cSeu clan já está no nível máximo.");
                    return true;
                }

                clan.levelUp();
                return true;
            }

            if(args[0].equalsIgnoreCase("nexus")){
                return NexusCommand.spawnNexus(clan, member);
            }

        }

        return false;
    }


    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {

        List<String> subcommands = Arrays.asList("criar", "sair", "info", "desfazer", "claimar", "convidar", "nexus", "ajuda", "membros", "convites", "top");;

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("convidar")) {
                List<String> players = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    players.add(player.getName());
                }
                return players;
            }

            return subcommands.stream().filter(subcommand -> subcommand.startsWith(args[0])).collect(Collectors.toList());
        }

        return null;
    }
}
