package me.dkz.plugin.artemis.artemisnexus.clan.nexus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.dkz.plugin.artemis.artemisnexus.Main;
import me.dkz.plugin.artemis.artemisnexus.clan.Clan;
import me.dkz.plugin.artemis.artemisnexus.clan.nexus.NexusEntity;
import me.dkz.plugin.artemis.artemisnexus.utils.TitleUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Nexus {

    private String location = "";

    private float health = 1500;

    private final UUID clan;


    public Location getLocation() {
        if (location.isEmpty()) return null;
        String[] split = location.split(",");
        World world = split[0].isEmpty() ? null : org.bukkit.Bukkit.getWorld(split[0]);
        if (world == null) return null;
        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[2]);
        double z = Double.parseDouble(split[3]);
        return new Location(world, x, y, z);
    }

    public boolean hasNexusInLocation() {
        if (getLocation() == null) return false;
        getLocation().getWorld().getNearbyEntities(getLocation(), 2, 2, 2).forEach(System.out::println);
        return getLocation().getWorld().getNearbyEntities(getLocation(), 2, 2, 2).stream().anyMatch(entity -> entity instanceof EnderCrystal);
    }

    public void damageNexus(float damage) {
        this.health -= damage;
        if(!Main.getInstance().getClanManager().getAlertQueue().contains(getClan())){
            Main.getInstance().getClanManager().getAlertQueue().add(getClan());
            getClan().broadcastAlert("§cO nexus do clan está sob ataque!");
            Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), () -> {
                Main.getInstance().getClanManager().getAlertQueue().remove(getClan());
            }, 20 * 300);
        }
    }

    public void sendMessage(Player damager) {
        TitleUtils.sendActionText(damager, "§a❤ " + (int) getHealth() + " §e" + getClan().getClanName() + " - Lv." + getClan().getLevel());
    }


    public float getMaxHealth() {
        if (getClan().getLevel() > 1) {
            return (getClan().getLevel() * 1500);
        }
        return 1500;
    }

    public Clan getClan() {
        return Main.getInstance().getClanManager().getById(clan);
    }

    public void nexusDeath() {
        Bukkit.broadcastMessage("§cO Clan " + getClan().getName() + " foi destruido!");
        Main.getInstance().getClanManager().removeClan(getClan());
    }

    public void spawnNexus(Location lo) {

        lo.setX(lo.getBlockX() + 0.5);
        lo.setZ(lo.getBlockZ() + 0.5);
        lo.add(0, 1, 0);

        CraftWorld craftWorld = (CraftWorld) lo.getWorld();
        NexusEntity nexus = new NexusEntity(craftWorld.getHandle(), this);
        nexus.setLocation(lo.getX(), lo.getY(), lo.getZ(), 0, 0);
        craftWorld.getHandle().addEntity(nexus);
        this.location = lo.getWorld().getName() + "," + lo.getX() + "," + lo.getY() + "," + lo.getZ();
        getClan().addChunk(lo.getChunk());
    }

    public void remove() {
        if (getLocation() == null) return;
        getLocation().getWorld().strikeLightningEffect(getLocation());
        getLocation().getWorld().getNearbyEntities(getLocation(), 2, 2, 2).forEach(entity -> {
            if (entity instanceof EnderCrystal) {
                entity.remove();
            }
        });
    }

    public void setHealth(float v) {
        this.health = v;
    }
}
