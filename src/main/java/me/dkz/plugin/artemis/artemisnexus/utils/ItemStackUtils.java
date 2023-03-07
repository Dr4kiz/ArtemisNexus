package me.dkz.plugin.artemis.artemisnexus.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.dkz.plugin.artemis.artemisnexus.Main;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class ItemStackUtils {

    public static ItemStack getHead(OfflinePlayer player) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        try {
            SkullMeta skull = (SkullMeta) item.getItemMeta();
            skull.setDisplayName(player.getName());
            skull.setOwner(player.getName());
            item.setItemMeta(skull);
        }catch (Exception e){
            Main.getInstance().getLogger().log(Level.INFO, "Erro ao coletar cabeças.");
        }
        return item;
    }

    public static ItemStack customItem(Material material, String name, int amount, String... desc){
        ItemStack stack = new ItemStack(material);
        return getItemStack(stack, name, amount, desc);
    }
    public static ItemStack customItem(Material material, short id, String name, int amount, String... desc){
        ItemStack stack = new ItemStack(material, amount, id);
        return getItemStack(stack, name, amount, desc);
    }
    public static ItemStack customItem(Material material, short id, boolean enchant, String name, int amount, String... desc){
        ItemStack stack = new ItemStack(material, amount, id);
        if(enchant){
            stack.addUnsafeEnchantment(Enchantment.LURE, 1);
            ItemMeta itemMeta = stack.getItemMeta();
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            stack.setItemMeta(itemMeta);

        }
        return getItemStack(stack, name, amount, desc);
    }
    public static ItemStack customItem(Material material, boolean enchant, String name, int amount, String... desc){
        ItemStack stack = new ItemStack(material, amount);
        if(enchant){
            stack.addUnsafeEnchantment(Enchantment.LURE, 1);
            ItemMeta itemMeta = stack.getItemMeta();
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            stack.setItemMeta(itemMeta);

        }
        return getItemStack(stack, name, amount, desc);
    }

    public static ItemStack customItem(ItemStack stack, String name, int amount, String... desc){
        return getItemStack(stack, name, amount, desc);
    }

    public static ItemStack customBook(String... strings){
        ItemStack itemStack = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bm = (BookMeta) itemStack.getItemMeta();
        bm.setTitle("Livro de Ajuda");
        bm.setAuthor("Drakiz");
        bm.setLore(Arrays.asList(strings));
        itemStack.setItemMeta(bm);
        return itemStack;
    }

    public static ItemStack getPotionStack(short id, boolean enchant){
        ItemStack stack = new ItemStack(Material.POTION,1, id);
        ItemMeta pm = stack.getItemMeta();
        if(enchant){
            stack.addUnsafeEnchantment(Enchantment.LURE, 1);
            pm.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        }
        pm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        stack.setItemMeta(pm);
        return stack;
    }

    public static ItemStack getPotionStack(short id){
        ItemStack stack = new ItemStack(Material.POTION,1, id);
        ItemMeta pm = stack.getItemMeta();
        pm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        stack.setItemMeta(pm);
        return stack;
    }

    public static ItemStack getSkull(String name) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        try {
            SkullMeta skull = (SkullMeta) item.getItemMeta();
            skull.setDisplayName(name);
            skull.setOwner(name);
            item.setItemMeta(skull);
        }catch (Exception e){
            Main.getInstance().getLogger().log(Level.INFO, "Erro ao coletar cabeças.");
        }
        return item;
    }


    private static ItemStack getItemStack(ItemStack stack, String name, int amount, String[] desc) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name.replaceAll("&", "§"));
        meta.setLore(Arrays.asList(desc).stream().map(str -> str.replaceAll("&", "§")).collect(Collectors.toList()));
        stack.setAmount(amount);
        stack.setItemMeta(meta);
        return stack;
    }
}
