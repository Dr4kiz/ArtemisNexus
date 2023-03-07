package me.dkz.plugin.artemis.artemisnexus.clan;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.dkz.plugin.artemis.artemisnexus.Main;
import me.dkz.plugin.artemis.artemisnexus.clan.nexus.Nexus;
import me.dkz.plugin.artemis.artemisnexus.clan.role.ClanRole;
import me.dkz.plugin.artemis.artemisnexus.utils.TitleUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.bukkit.Chunk;

import java.util.*;

@Getter
@Setter
@RequiredArgsConstructor
public class Clan {

    private final UUID ID = UUID.randomUUID();
    private String tag = "";
    public String name = "";
    public UUID owner = null;
    public String description = "Clique para editar a descrição do clan.";

    public Nexus nexus;

    private int level = 1;

    public long creationDate = System.currentTimeMillis();

    private Set<String> chunks = new HashSet<>();
    private Set<String> effects = new HashSet<>();

    private boolean shield = false;


    public boolean hasShield(){
        return shield;
    }



    public Clan getInstance(){
        return this;
    }


    @Override
    public String toString() {
        return "Clan{" +
                "ID=" + ID +
                ", tag='" + tag + '\'' +
                ", name='" + name + '\'' +
                ", chunks='" + chunks + '\'' +
                '}';
    }

    public List<Member> getPlayers() {
        return Main.getInstance().getClanManager().getPlayers(ID);
    }

    public boolean hasNexus(){
        return nexus.getLocation() != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Clan)) return false;
        Clan clan = (Clan) o;
        return this.name.equals(clan.name) && this.tag.equals(clan.tag) && this.ID.equals(clan.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, tag, name);
    }

    public void addChunk(Chunk chunk){
        chunks.add(chunk.getX() + ":" + chunk.getZ());
    }

    public boolean hasChunk(Chunk compare){
        return chunks.contains(compare.getX() + ":" + compare.getZ());
    }


    public String getClanName(){
        return "["+tag+"] "+name;
    }

    public UUID getOwner() {
        return owner;
    }

    public void broadcast(String s) {
        Main.getInstance().getClanManager().getPlayers(this.ID).forEach(cplayer ->{
            if(cplayer.getBukkitPlayer() != null){
                cplayer.getBukkitPlayer().getPlayer().sendMessage("§b[C]: "+s.replaceAll("&", "§"));
            }
        });
    }

    public void levelUp(){
        level++;
        broadcast("§eO clan subiu para o level §a§l"+level+"§e!");
    }

    public int getMaxMembers() {
        if(level == 1) return 7;
        if(level == 2) return 9;
        if(level == 3) return 12;
        return 14;
    }

    public int getMaxChunks() {
        if(level == 1) return 1;
        if(level >= 9) return 9;
        return level;
    }

    public void removePlayer(Member member) {
        member.setClan(null);
        member.setRole(ClanRole.SEM_CLAN);
    }

    public  long getTotalEnergy() {
        return getPlayers().stream().mapToLong(Member::getEnergy).sum();
    }

    public String getCreationDate() {
        return DateFormatUtils.format(creationDate, "dd/MM/yyyy - HH:mm:ss");
    }

    public int getLevel() {
        return level;
    }

    public void broadcastAlert(String s) {
        getPlayers().forEach(player -> {
            if(player.getBukkitPlayer() != null) {
                TitleUtils.send(player.getBukkitPlayer().getPlayer(), 10, 50, 10, "§bAlerta do Clan", s.replaceAll("&", "§"));
            }
        });
    }

    public void sendMessage(Member member, String[] args) {
        String message = String.join(" ", args);
        broadcast("§6["+member.getRole().name()+"] §7"+member.getBukkitPlayer().getName()+": §a"+message.replaceAll("&", "§"));
    }
}
