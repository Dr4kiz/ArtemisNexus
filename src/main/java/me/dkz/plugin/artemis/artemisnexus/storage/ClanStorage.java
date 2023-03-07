package me.dkz.plugin.artemis.artemisnexus.storage;

import com.google.gson.Gson;
import me.dkz.plugin.artemis.artemisnexus.Main;
import me.dkz.plugin.artemis.artemisnexus.clan.Member;
import me.dkz.plugin.artemis.artemisnexus.clan.Clan;
import me.dkz.plugin.artemis.artemisnexus.clan.role.ClanRole;
import me.dkz.plugin.artemis.artemisnexus.storage.config.StorageConfig;
import org.bukkit.Chunk;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ClanStorage {

    private HashMap<UUID, Clan> clans = new HashMap<UUID, Clan>();
    private Set<Member> cplayers = new HashSet<>();

    private Main plugin = Main.getInstance();

    public void forceSave() {
        Gson gson = new Gson();

        clans.forEach((id, c) -> {
            StorageConfig config = new StorageConfig(id.toString(), "mcnexus_clans");
            String data = gson.toJson(c);
            try (FileWriter writer = new FileWriter(config.getFile())) {
                writer.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        cplayers.forEach(p -> {
            StorageConfig config = new StorageConfig(p.getUuid().toString(), "mcnexus_players");
            String data = gson.toJson(p);
            try (FileWriter writer = new FileWriter(config.getFile())) {
                writer.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public void forceLoad() {
        File clansFolder = new File(plugin.getServer().getWorldContainer(), "mcnexus_clans");
        File playersFolder = new File(plugin.getServer().getWorldContainer(), "mcnexus_players");
        Gson gson = new Gson();
        if (clansFolder.exists()) {
            for (File file : clansFolder.listFiles()) {
                try (FileReader reader = new FileReader(file)) {
                    Clan clan = gson.fromJson(reader, Clan.class);
                    clans.put(clan.getID(), clan);
                } catch (Exception e) {
                    plugin.getLogger().severe("Não foi possível carregar o clã " + file.getName().replace(".json", ""));
                }
            }
        }
        if (playersFolder.exists()) {
            for (File file : playersFolder.listFiles()) {
                try (FileReader reader = new FileReader(file)) {
                    Member member = gson.fromJson(reader, Member.class);
                    cplayers.add(member);
                } catch (Exception e) {
                    plugin.getLogger().severe("Não foi possível carregar o jogador " + file.getName().replace(".json", ""));
                }
            }
        }
    }


    public void addPlayer(Member cplayer) {
        if (!cplayers.contains(cplayer)) cplayers.add(cplayer);
    }


    public void create(Clan clan) {
        clans.put(clan.getID(), clan);
    }

    public Optional<Clan> getClan(UUID id) {
        return Optional.ofNullable(clans.get(id));
    }


    public Member getPlayer(UUID uniqueId) {
        return cplayers.stream().filter(c -> c.getUuid().equals(uniqueId)).findFirst().orElseGet(() -> {
            Member member = new Member(uniqueId);
            cplayers.add(member);
            return member;
        });
    }

    public void removeClan(UUID clan) {
        removeAllFromClan(clan);
        if(getClan(clan).get().hasNexus()){
            getClan(clan).get().getNexus().remove();
        }
        clans.remove(clan);
        File file = new File(plugin.getServer().getWorldContainer(), "mcnexus_clans/" + clan.toString() + ".json");
        if (file.exists()) file.delete();


    }

    public Clan getClanByChunk(Chunk chunk) {
        return clans.values().stream().filter(c -> c.hasChunk(chunk)).findFirst().orElse(null);
    }

    public Clan getClanByName(String nome) {
        return clans.values().stream().filter(c -> c.getName().equalsIgnoreCase(nome)).findFirst().orElse(null);
    }

    public void removeAllFromClan(UUID clan) {
        cplayers.stream().filter(c -> {
            if (c.getClan() == null) return false;
            return c.getClan().getID().equals(clan);
        }).forEach(c -> {
            c.setClan(null);
            c.setRole(ClanRole.SEM_CLAN);
        });
    }

    public List<Member> getPlayers(UUID clan) {
        List<Member> members = new ArrayList<>();
        cplayers.stream().filter(c -> {
            if (c.getClan() == null) return false;
            return c.getClan().getID().equals(clan);
        }).forEach(members::add);
        return members;
    }

    public List<Clan> getClans() {
        return new ArrayList<>(clans.values());
    }
}
