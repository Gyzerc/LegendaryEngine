package com.gyzer.legendaryrealms.FileConguration;

import com.gyzer.legendaryrealms.LegendaryPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;

public abstract class FileProvider {
    public File file;
    public YamlConfiguration yml;
    public LegendaryPlugin legendaryPlugin;
    public FileProvider(LegendaryPlugin legendaryPlugin, String path, String fileName){
        this.legendaryPlugin = legendaryPlugin;
        this.file = new File("./plugins/"+legendaryPlugin.getPlugin().getName() + path,fileName);
        if (!file.exists()){
            legendaryPlugin.getPlugin().saveResource(path+fileName,false);
            legendaryPlugin.info("创建 "+fileName+" . ", Level.INFO);
        }

        this.yml = YamlConfiguration.loadConfiguration(file);

        readDefault();
    }
    protected abstract void readDefault();
    public<T> T getValue(String path,T defaultValue){
        if (yml.get(path) != null){
            try {
                return (T) yml.get(path);
            } catch (ClassCastException ex){
                legendaryPlugin.info("强制转化出错！-> "+path, Level.SEVERE);
                return defaultValue;
            }
        }
        yml.set(path,defaultValue);
        return defaultValue;
    }

    public Optional<ConfigurationSection> getSection(String path){
        ConfigurationSection section = yml.getConfigurationSection(path);
        return section !=null ? Optional.of(section) : Optional.empty();
    }

    public void saveYml(){
        try {
            yml.save(file);
        } catch (IOException e) {
            legendaryPlugin.info("保存 "+file.getName() +" 失败.",Level.SEVERE);
        }
    }

}
