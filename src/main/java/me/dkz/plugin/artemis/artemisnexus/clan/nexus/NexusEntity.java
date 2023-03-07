package me.dkz.plugin.artemis.artemisnexus.clan.nexus;

import me.dkz.plugin.artemis.artemisnexus.ClanManager;
import me.dkz.plugin.artemis.artemisnexus.Main;
import me.dkz.plugin.artemis.artemisnexus.clan.Clan;
import me.dkz.plugin.artemis.artemisnexus.clan.Member;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntityEnderCrystal;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NexusEntity extends EntityEnderCrystal {

    private Nexus nexus;
    private Main plugin = Main.getInstance();
    private ClanManager clanManager = plugin.getClanManager();

    public NexusEntity(World world, Nexus nexus) {
        super(world);
        this.nexus = nexus;
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {


        if(damagesource.getEntity() instanceof EntityPlayer) {
            Player player = ((EntityPlayer) damagesource.getEntity()).getBukkitEntity();
            Member member = clanManager.getPlayer(player.getUniqueId());
            return attackNexus(player, member, f);
        }

        if(damagesource.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) damagesource.getEntity();
            if(arrow.getShooter() instanceof Player) {
                Player player = (Player) arrow.getShooter();
                Member member = clanManager.getPlayer(player.getUniqueId());
                return attackNexus(player, member, f);
            }
        }

        if(nexus.getHealth() <= 0){
            nexus.nexusDeath();
            if(damagesource.getEntity() instanceof EntityPlayer) {
                Player player = ((EntityPlayer) damagesource.getEntity()).getBukkitEntity();
                Member member = clanManager.getPlayer(player.getUniqueId());
                Clan clan = member.getClan();
                clan.levelUp();

            }
            return super.damageEntity(damagesource, f);
        }

        return false;
    }

    private boolean attackNexus(Player player, Member member, float f) {
        Clan clan = member.getClan();
        nexus.sendMessage(player);
        if(!member.hasClan()) return false;
        if(member.getClan().getID().equals(nexus.getClan().getID())) return false;
        nexus.damageNexus(f);
        clan.getEffects().forEach(effect ->{
            try {
                PotionEffectType type = PotionEffectType.getByName(effect);
                if(player.hasPotionEffect(type)) return;
                player.addPotionEffect(new PotionEffect(type, 20 * 30, 1));

            }catch (Exception e){
                plugin.getLogger().warning("O efeito " + effect + " não existe!");
            }
        });
        return false;
    }

}
