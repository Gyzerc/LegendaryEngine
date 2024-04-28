package com.gyzer.legendaryrealms.Command;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public abstract class LegendaryCommand {

    private String permission;
    private String command;
    private int length;
    private boolean admin;

    public LegendaryCommand(String permission, String command, int length, boolean admin) {
        this.permission = permission;
        this.command = command;
        this.length = length;
        this.admin = admin;
    }
    public abstract void handle(CommandSender sender, String[] args);
    public abstract List<String> complete(CommandSender sender, String[] args);

    public String getPermission() {
        return permission;
    }

    public String getCommand() {
        return command;
    }

    public int getLength() {
        return length;
    }

    public boolean isAdmin() {
        return admin;
    }

    public OfflinePlayer getPlayer(String name) {
        OfflinePlayer p = Bukkit.getPlayerExact(name);
        if (p == null) {
            // Not the best option, but Spigot doesn't offer a good replacement (as usual)
            p = Bukkit.getOfflinePlayer(name);
            return p.hasPlayedBefore() ? p : null;
        }
        return p;
    }
    public List<String> getOnlines() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }


}
