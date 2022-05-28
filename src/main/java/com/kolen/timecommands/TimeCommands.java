package com.kolen.timecommands;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class TimeCommands extends JavaPlugin {

    public HashMap<String, BukkitTask> tasks = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reload();
        getCommand("tc").setExecutor(new TimeCommand(this));
        getCommand("timecommands").setExecutor(new TimeCommand(this));
    }

    public void reload(){
        for(Map.Entry<String, BukkitTask> task : tasks.entrySet()){
            task.getValue().cancel();
        }
        tasks.clear();
        for(String commandName : getConfig().getConfigurationSection("commands").getKeys(false)){
            ConfigurationSection command = getConfig().getConfigurationSection("commands." + commandName);
            BukkitTask task = Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
                @Override
                public void run() {
                    getServer().dispatchCommand(getServer().getConsoleSender(), command.getString("command"));

                }
            }, 0L, ((long) (command.getInt("time") * 20L)));

            tasks.put(commandName, task);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
