package me.dkz.plugin.artemis.artemisnexus.clan;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.dkz.plugin.artemis.artemisnexus.Main;
import me.dkz.plugin.artemis.artemisnexus.clan.role.ClanRole;
import me.dkz.plugin.artemis.artemisnexus.utils.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.bukkit.OfflinePlayer;

import java.math.BigInteger;
import java.util.Date;
import java.util.UUID;


@RequiredArgsConstructor
@Getter @Setter
public class Member {

    private UUID uuid;
    private UUID clan;

    private long energy = 0;
    private int deaths = 0;
    private int kills = 0;

    public long getEnergy() {
        return energy;
    }


    private long joinDate = System.currentTimeMillis();

    private ClanRole role = ClanRole.SEM_CLAN;

    public Member(UUID player){
        this.uuid = player;
    }


    public void setEnergy(long energy) {
        this.energy = energy;
        if(this.energy < 0) this.energy = 0;
    }

    @Override
    public String toString() {
        return "Member{" +
                "uuid=" + uuid +
                ", clan=" + clan +
                ", role=" + role +
                '}';
    }

    public String getFormatEnergy(){
        return StringUtils.format5(energy);
    }

    public OfflinePlayer getBukkitPlayer(){
        return Main.getInstance().getServer().getOfflinePlayer(uuid);
    }

    public boolean hasClan(){
        return clan != null;
    }

    public double getKd() {
        return (kills == 0 ? 0 : (deaths == 0 ? kills : (kills / deaths)));
    }

    public Clan getClan(){
        if(!hasClan()) return null;
        return Main.getInstance().getClanManager().getById(clan);
    }

    public String getFormatJoinDate() {
        Date joinDate = new Date(this.joinDate);
        return DateFormatUtils.format(joinDate, "dd/MM/yyyy - HH:mm:ss");
    }
}
