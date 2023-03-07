package me.dkz.plugin.artemis.artemisnexus.listener;

import me.dkz.plugin.artemis.artemisnexus.ClanManager;
import me.dkz.plugin.artemis.artemisnexus.Main;
import me.dkz.plugin.artemis.artemisnexus.clan.Member;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class MemberListener implements Listener {

    private Main plugin = Main.getInstance();
    private ClanManager clanManager = plugin.getClanManager();
    @EventHandler
    void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        Member member = clanManager.getPlayer(player.getUniqueId());
        member.setJoinDate(System.currentTimeMillis());
        clanManager.add(member);
    }

    @EventHandler
    void onPlayerDeath(PlayerDeathEvent e){
        Player player = e.getEntity();
        Member member = clanManager.getPlayer(player.getUniqueId());
        member.setDeaths(member.getDeaths() + 1);
    }

    @EventHandler
    void onPlayerDamage(EntityDamageByEntityEvent e){


        if(!(e.getEntity()instanceof Player)) return;


        if(e.getDamager() instanceof Arrow){
            Arrow arrow = (Arrow) e.getDamager();
            if(arrow.getShooter() instanceof Player){
                Player damager = (Player) arrow.getShooter();
                Member member = clanManager.getPlayer(damager.getUniqueId());
                if(e.getEntity() instanceof Player){
                    Player player = (Player) e.getEntity();
                    Member member2 = clanManager.getPlayer(player.getUniqueId());
                    if(member2.getClan().equals(member.getClan())){
                        e.setCancelled(true);
                    }
                }
            }
        }

        if (e.getDamager() instanceof Player){
            Player damager = (Player) e.getDamager();
            Member member = clanManager.getPlayer(damager.getUniqueId());
            if(e.getEntity() instanceof Player){
                Player player = (Player) e.getEntity();
                Member member2 = clanManager.getPlayer(player.getUniqueId());
                if(member2.getClan().equals(member.getClan())){
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    void onEntityKilledByEntity(EntityDeathEvent e){
        if (e.getEntity().getKiller() instanceof Player){
            Player player = e.getEntity().getKiller();
            Member member = clanManager.getPlayer(player.getUniqueId());
            member.setKills(member.getKills() + 1);
        }
    }
}
