package com.gyzer.legendaryrealms.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

public class ItemUtils {

    public static ItemStack getItemFromFileSection(ConfigurationSection section,String id){
        ItemStack i = new ItemStack(getMaterial(section.getString(id+".material")), section.getInt(id+".amount",1),(short)section.getInt(id+".data",0));
        ItemMeta meta = i .getItemMeta();
        meta.setDisplayName(MsgUtils.msg(section.getString(id+".display"," ")));
        List<String> l = section.getStringList(id+".lore") != null ? MsgUtils.msg(section.getStringList(id+".lore")) : new ArrayList();
        meta.setLore(l);
        meta.setCustomModelData(section.getInt(id+".model",0));
        i.setItemMeta(meta);
        return i;
    }

    public static void giveItem(Player p, ItemStack i, int amount, boolean Multiply) {
        int targetAmount = amount;
        if (Multiply)
            targetAmount = amount * i.getAmount();
        if (targetAmount > 64) {
            ItemStack clone = i.clone();
            clone.setAmount(64);
            HashMap<Integer, ItemStack> drops = p.getInventory().addItem(new ItemStack[] { clone });
            if (!drops.isEmpty()) {
                for (Map.Entry<Integer, ItemStack> drop : drops.entrySet()) {
                    giveEndChest(p,drop.getValue());
                }
                giveItem(p, i, targetAmount - 64, false);
            }
        } else {
            ItemStack clone = i.clone();
            clone.setAmount(targetAmount);
            HashMap<Integer, ItemStack> drops = p.getInventory().addItem(new ItemStack[]{clone});
            if (!drops.isEmpty()) {
                for (Map.Entry<Integer, ItemStack> drop : drops.entrySet()) {
                    giveEndChest(p, drop.getValue());
                }
            }
        }
    }

    private static void giveEndChest(Player p,ItemStack i) {
        HashMap<Integer, ItemStack> drops = p.getEnderChest().addItem(i);
        if (!drops.isEmpty()) {
            for (Map.Entry<Integer, ItemStack> drop : drops.entrySet()) {
                p.getWorld().dropItem(p.getLocation(), drop.getValue());
            }
        }
    }

    public static ItemStack build(Material material, String display, List<String> lore, int model, int amount) {
        ItemStack i = new ItemStack(material,amount);
        ItemMeta id = i.getItemMeta();
        id.setDisplayName(MsgUtils.msg(display));
        id.setLore(MsgUtils.msg(lore));
        id.setCustomModelData(model);
        i.setItemMeta(id);
        return i;
    }

    public static Material getMaterial(String str){
        return Material.getMaterial(str) != null ? Material.getMaterial(str) : Material.APPLE;
    }

    public static String itemStackArrayToBase64(ItemStack[] items) {
        if (items == null || items.length == 0) {
            return "";
        }
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(items.length);

            // Save every element in the list
            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ItemStack[] itemStackArrayFromBase64(String data) {
        if (data == null || data.isEmpty()) {
            return new ItemStack[]{};
        }
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            // Read the serialized inventory
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ItemStack toItem(String arg)
    {
        byte[] bytes= Base64.getDecoder().decode(arg);
        try (ByteArrayInputStream byteArrayOutputStream=new ByteArrayInputStream(bytes);
             BukkitObjectInputStream bukkitObjectOutputStream=new BukkitObjectInputStream(byteArrayOutputStream)){
            return (ItemStack) bukkitObjectOutputStream.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public static String toItemString(ItemStack i) {
        try (ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
             BukkitObjectOutputStream bukkitObjectOutputStream=new BukkitObjectOutputStream(byteArrayOutputStream)){
            bukkitObjectOutputStream.writeObject(i);
            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
