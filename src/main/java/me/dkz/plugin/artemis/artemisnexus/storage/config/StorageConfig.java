package me.dkz.plugin.artemis.artemisnexus.storage.config;

import me.dkz.plugin.artemis.artemisnexus.Main;

import java.io.File;
import java.io.IOException;

public class StorageConfig {

    private Main plugin = Main.getInstance();
    private File file;

    public StorageConfig(String name, String f) {
        try {
            File folder = new File(plugin.getServer().getWorldContainer()+"/"+f);
            if(!folder.exists()) folder.mkdirs();
            file = new File(plugin.getServer().getWorldContainer()+"/"+f+"/", name + ".json");
            if (!file.exists()) file.createNewFile();


        } catch (IOException e) {
            plugin.getLogger().severe("Não foi possível criar os dados de " + name);
            e.printStackTrace();
        }
    }


    public File getFile() {
        return file;
    }





}
