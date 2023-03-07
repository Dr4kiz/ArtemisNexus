package me.dkz.plugin.artemis.artemisnexus;

import me.dkz.plugin.artemis.artemisnexus.clan.Clan;
import me.dkz.plugin.artemis.artemisnexus.clan.Member;
import me.dkz.plugin.artemis.artemisnexus.clan.nexus.Nexus;
import me.dkz.plugin.artemis.artemisnexus.storage.ClanStorage;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.*;

public class ClanManager {

    private Main plugin = Main.getInstance();
    private ClanStorage clanStorage = plugin.getClanStorage();
    public Set<Player> namequeue = new HashSet<>();
    public HashMap<Player, Clan> tagqueue = new HashMap<>();
    public HashMap<Player, Clan> descqueue = new HashMap<>();

    public Clan getById(UUID id) {
        return clanStorage.getClan(id).orElse(new Clan());
    }

    public HashMap<Player, Clan> getDescqueue() {
        return descqueue;
    }

    public Set<Player> getNamequeue() {
        return namequeue;
    }

    public HashMap<Player, Clan> getTagqueue() {
        return tagqueue;
    }

    private List<Clan> alertQueue = new ArrayList<>();


    public List<Clan> getAlertQueue() {
        return alertQueue;
    }

    public void createClan(Clan clan) {
        Nexus nexus = new Nexus(clan.getID());
        clan.setNexus(nexus);
        clanStorage.create(clan);
    }


    public void add(Member member) {
        clanStorage.addPlayer(member);
    }

    public Member getPlayer(UUID uniqueId) {
        return clanStorage.getPlayer(uniqueId);
    }

    public void removeClan(Clan clan) {
        clanStorage.removeClan(clan.getID());
    }

    public Clan getClanByChunk(Chunk chunk) {
        return clanStorage.getClanByChunk(chunk);
    }

    public Clan getByName(String nome) {
        return clanStorage.getClanByName(nome);
    }


    public List<Member> getPlayers(UUID clan) {
        return clanStorage.getPlayers(clan);
    }

    public List<Clan> getClans() {
        return clanStorage.getClans();
    }

    public void removeFromClan(UUID clan) {
        clanStorage.removeAllFromClan(clan);
    }
}
