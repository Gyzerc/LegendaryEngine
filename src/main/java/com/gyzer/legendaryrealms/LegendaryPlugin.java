package com.gyzer.legendaryrealms;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public interface LegendaryPlugin {
    void initModules();
    void reload();
    void info(String msg);
    void info(String msg, Level level);
    JavaPlugin getPlugin();
    void sync(Runnable runnable);
}
