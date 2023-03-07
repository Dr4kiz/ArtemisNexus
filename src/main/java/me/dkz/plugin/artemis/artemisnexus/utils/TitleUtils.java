package me.dkz.plugin.artemis.artemisnexus.utils;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;


public class TitleUtils {
    public static void sendActionText(Player player, String message){
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte)2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
    public static void send(Player player, int fadeInTime, int showTime, int fadeOutTime, String title2, String subtitle) {
        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title2 + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");
        IChatBaseComponent chatSub = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle sub = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSub);
        PacketPlayOutTitle length = new PacketPlayOutTitle(fadeInTime, showTime, fadeInTime);


        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(sub);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
    }




}
