package com.kolen.timecommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class TimeCommand implements CommandExecutor {
    public final TimeCommands plugin;

    public TimeCommand(TimeCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1){
            if(args[0].equals("reload")){
                plugin.reloadConfig();
                plugin.reload();
                sender.sendMessage(ChatColor.GREEN + "The plugin has been successfully reloaded");
            }else if(args[0].equals("list")){
                if(plugin.getConfig().getConfigurationSection("commands").getKeys(false).size() >= 1) {
                    for (String commandName : plugin.getConfig().getConfigurationSection("commands").getKeys(false)) {
                        ConfigurationSection cmd = plugin.getConfig().getConfigurationSection("commands." + commandName);
                        sender.sendMessage(ChatColor.DARK_GREEN + commandName);
                        sender.sendMessage(ChatColor.WHITE + "Command: " + cmd.getString("command"));
                        sender.sendMessage(ChatColor.WHITE + "Every seconds: " + cmd.getString("time"));
                    }
                }else{
                    sender.sendMessage(ChatColor.AQUA + "You don't have any commands yet");
                }
            }
        }else if(args.length == 2){
            if(args[0].equals("remove")){
                System.out.println("commands." + args[1]);
                plugin.getConfig().set("commands." + args[1], null);
                plugin.saveConfig();
                plugin.reloadConfig();
                plugin.tasks.get(args[1]).cancel();
                plugin.tasks.remove(args[1]);
                sender.sendMessage(ChatColor.GREEN + "You have successfully deleted the command " + args[1]);
            }
        }else if(args.length >= 3) {
            if (args[0].equals("add")) {
                String[] commandInArray = Arrays.copyOfRange(args, 2, args.length);
                String addCommand = String.join(" ", commandInArray);
                HashMap<String, Object> data = new HashMap<>();
                data.put("command", addCommand);
                data.put("time", Integer.parseInt(args[1]));
                String id = UUID.randomUUID().toString();
                plugin.getConfig().set("commands." + id, data);
                plugin.saveConfig();
                plugin.reloadConfig();
                plugin.reload();
                sender.sendMessage(ChatColor.GREEN + "You have successfully added the command" + id);
            }
        }
        return true;
    }
}
