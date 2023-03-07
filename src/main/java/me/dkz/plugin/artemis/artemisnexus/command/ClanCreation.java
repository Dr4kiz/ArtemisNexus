package me.dkz.plugin.artemis.artemisnexus.command;

import me.dkz.plugin.artemis.artemisnexus.ClanManager;
import me.dkz.plugin.artemis.artemisnexus.Main;
import me.dkz.plugin.artemis.artemisnexus.clan.Member;
import me.dkz.plugin.artemis.artemisnexus.clan.Clan;
import me.dkz.plugin.artemis.artemisnexus.clan.role.ClanRole;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ClanCreation implements Listener {



    private Main plugin =Main.getInstance();
    private ClanManager clanManager = plugin.getClanManager();




    @EventHandler
    void onMessage(AsyncPlayerChatEvent e){
        Player player = e.getPlayer();
        Member member = clanManager.getPlayer(player.getUniqueId());
        if(clanManager.getNamequeue().contains(player)) {
            String message = e.getMessage();


            if(message.equalsIgnoreCase("cancelar")){
                clanManager.getNamequeue().remove(player);
                player.sendMessage("§cCriação de clan cancelada.");
                e.setCancelled(true);
                return;
            }


            if (message.length() < 3) {
                player.sendMessage("§cO nome do clan deve ter no mínimo 3 caracteres.");
                e.setCancelled(true);
                return;
            }

            if (message.length() > 12) {
                player.sendMessage("§cO nome do clan deve ter no máximo 12 caracteres.");
                e.setCancelled(true);
                return;
            }

            if(!Character.isAlphabetic(message.charAt(0))){
                player.sendMessage("§cO nome do clan deve começar com uma letra.");
                e.setCancelled(true);
                return;
            }

            if(!StringUtils.isAlphanumeric(message)){
                player.sendMessage("§cO nome do clan deve conter apenas letras e números.");
                e.setCancelled(true);
                return;
            }


            if (clanManager.getByName(message) != null) {
                player.sendMessage("§cUm clan com o nome informado já existe.");
                e.setCancelled(true);
                return;
            }
            Clan clan = new Clan();
            clan.setName(e.getMessage());
            clanManager.getTagqueue().put(player, clan);
            clanManager.getNamequeue().remove(player);
            player.sendMessage("§eDigite a §lTAG§e do seu clan ou digite §c§lCANCELAR §epara cancelar a criação.");
            e.setCancelled(true);

        }else if(clanManager.getTagqueue().containsKey(player)) {
            Clan clan = clanManager.getTagqueue().get(player);
            String message = e.getMessage();


            if(message.equalsIgnoreCase("cancelar")){
                clanManager.getTagqueue().remove(player);
                player.sendMessage("§cCriação de clan cancelada.");
                e.setCancelled(true);
                return;
            }

            if (message.length() < 3) {
                player.sendMessage("§cA tag do clan deve ter no mínimo 3 caracteres.");
                e.setCancelled(true);
                return;
            }

            if (message.length() > 3) {
                player.sendMessage("§cA tag do clan deve ter no máximo 3 caracteres.");
                e.setCancelled(true);
                return;
            }


            if(!Character.isAlphabetic(message.charAt(0))){
                player.sendMessage("§cA tag do clan deve começar com uma letra.");
                e.setCancelled(true);
                return;
            }

            if(!StringUtils.isAlphanumeric(message)){
                player.sendMessage("§cA tag do clan deve conter apenas letras e números.");
                e.setCancelled(true);
                return;
            }



            clan.setOwner(player.getUniqueId());
            member.setRole(ClanRole.LIDER);
            member.setClan(clan.getID());
            clan.setTag(message);
            clanManager.getTagqueue().remove(player);
            clanManager.createClan(clan);
            player.sendMessage("§a§lSUCESSO! §aSeu clan foi criado.");
            e.setCancelled(true);
        }else if(clanManager.getDescqueue().containsKey(player)) {
            Clan clan = clanManager.getDescqueue().get(player);
            String message = e.getMessage().replaceAll("&", "§");
            if(message.equalsIgnoreCase("cancelar")){
                clanManager.getDescqueue().remove(player);
                player.sendMessage("§cEdição de descrição cancelada");
                e.setCancelled(true);
                return;
            }

            if(message.length() > 32){
                player.sendMessage("§cA descrição do clan deve ter no máximo 32 caracteres.");
                e.setCancelled(true);
                return;
            }

            clan.setDescription(message);
            clanManager.getDescqueue().remove(player);
            clan.broadcast("§aNova descrição do clan definida.");
            player.sendMessage("§a§lSUCESSO! §aDescrição do clan editada.");
            e.setCancelled(true);

        }

    }

}
