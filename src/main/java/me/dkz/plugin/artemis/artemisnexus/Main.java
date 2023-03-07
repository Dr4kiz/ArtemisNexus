package me.dkz.plugin.artemis.artemisnexus;

import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager;
import me.dkz.plugin.artemis.artemisnexus.command.ClanChat;
import me.dkz.plugin.artemis.artemisnexus.command.EnergyCommand;
import me.dkz.plugin.artemis.artemisnexus.command.ClanCommand;
import me.dkz.plugin.artemis.artemisnexus.command.ClanCreation;
import me.dkz.plugin.artemis.artemisnexus.hook.ClanPlaceHolder;
import me.dkz.plugin.artemis.artemisnexus.listener.MemberListener;
import me.dkz.plugin.artemis.artemisnexus.listener.ChunksListener;
import me.dkz.plugin.artemis.artemisnexus.listener.NexusListener;
import me.dkz.plugin.artemis.artemisnexus.storage.ClanStorage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Main extends JavaPlugin {

    private ClanManager clanManager;
    private ClanStorage clanStorage;
    private InviteManager inviteManager;

    public static Main getInstance(){
        return getPlugin(Main.class);
    }

    public ClanManager getClanManager() {
        return clanManager;
    }


    protected ClanStorage getClanStorage() {
        return clanStorage;
    }

    private long start = 0;



    @Override
    public void onEnable() {
        start = System.currentTimeMillis();
        getLogger().info("Registrando controladores...");
        inviteManager = new InviteManager();
        clanStorage = new ClanStorage();
        clanManager = new ClanManager();
        clanStorage.forceLoad();
        getLogger().severe("Registrando a API de inventários...");
        InventoryManager.enable(this);
        getLogger().info("Registrando eventos e comandos...");
        getServer().getPluginManager().registerEvents(new MemberListener(), this);
        getServer().getPluginManager().registerEvents(new ChunksListener(), this);
        getServer().getPluginManager().registerEvents(new NexusListener(), this);
        getServer().getPluginManager().registerEvents(new ClanCreation(), this);
        getCommand("clan").setExecutor(new ClanCommand());
        getCommand("clan").setTabCompleter(new ClanCommand());
        getCommand("c").setExecutor(new ClanChat());
        getCommand("energia").setExecutor(new EnergyCommand());
        getCommand("energia").setTabCompleter(new EnergyCommand());
        getCommand("artemisnexus").setExecutor(this::onCommand);
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            getLogger().log(Level.INFO, "PlaceholderAPI encontrado, ativando os hooks...");
            new ClanPlaceHolder().register();
        }
        getLogger().info("Plugin iniciado com sucesso! (" + (System.currentTimeMillis() - start) + "ms)");
    }

    @Override
    public void onDisable() {
        clanStorage.forceSave();
    }

    public InviteManager getClanInvite() {
        return inviteManager;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args.length < 1){
            String message = "\n§b[ArtemisNexus] §eInformações:\n" +
                    "§e| Versão: §b" + getDescription().getVersion() +"\n" +
                    "§e| Desenvolvedor: §5Drakiz \n" +
                    "§e| Site: §cEm breve\n" +
                    "§e| Discord: §9https://discord.gg/r7N6YfFwQa";
            sender.sendMessage(message);
            return true;
        }


        if(sender.hasPermission("artemis.staff")){
            if(args[0].equalsIgnoreCase("reload")){
                long start = System.currentTimeMillis();
                clanStorage.forceSave();
                clanStorage.forceLoad();
                sender.sendMessage("§b[ArtemisNexus] §aPlugin recarregado com sucesso em " + (System.currentTimeMillis() - start) + "ms");
                return true;
            }
            if(args[0].equalsIgnoreCase("load")){
                long start = System.currentTimeMillis();
                clanStorage.forceLoad();
                sender.sendMessage("§b[ArtemisNexus] §aLoad forçado com sucesso em " + (System.currentTimeMillis() - start) + "ms");
                return true;
            }

            if(args[0].equalsIgnoreCase("save")){
                long start = System.currentTimeMillis();
                clanStorage.forceSave();
                sender.sendMessage("§b[ArtemisNexus] §aSave forçado com sucesso em " + (System.currentTimeMillis() - start) + "ms");
                return true;
            }
        }

        return false;
    }
}