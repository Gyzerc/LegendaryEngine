package com.gyzer.legendaryrealms.Command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.util.*;
public abstract class LegendaryCommandHandler implements CommandExecutor, TabCompleter {
    private HashMap<String,LegendaryCommand> commands;
    private String PLUGIN;
    private List<String> HELP_PLAYER;
    private String UNKNOWN_COMMAND;
    private String NO_PERMISSION;

    abstract void registerCommands();
    public LegendaryCommandHandler(String PLUGIN, List<String> HELP_PLAYER, String UNKNOWN_COMMAND, String NO_PERMISSION) {
        this.commands = new HashMap<>();
        this.PLUGIN = PLUGIN;
        this.HELP_PLAYER = HELP_PLAYER;
        this.UNKNOWN_COMMAND = UNKNOWN_COMMAND;
        this.NO_PERMISSION = NO_PERMISSION;
        registerCommands();
    }

    public LegendaryCommandHandler() {
        this.commands = new HashMap<>();
        this.PLUGIN = "PLUGIN >> ";
        this.HELP_PLAYER = Collections.singletonList("HELP_PLAYER");
        this.UNKNOWN_COMMAND = "UNKNOWN_COMMAND";
        this.NO_PERMISSION = "NO_PERMISSION";
        registerCommands();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings)
    {
        int length = strings.length;
        if (length == 0){
            //发送指令提示
            HELP_PLAYER.forEach(msg -> {
                sender.sendMessage(msg);
            });
        }
        else {
            String subCommandName = strings[0];
            HashMap<String,LegendaryCommand> map=commands;
            LegendaryCommand cmd = map.get(subCommandName);
            if (cmd == null){
                sender.sendMessage(PLUGIN+UNKNOWN_COMMAND);
                return false;
            }
            if (cmd.getPermission().isEmpty() || sender.hasPermission(cmd.getPermission())) {
                if (length == cmd.getLength()) {
                    cmd.handle(sender, strings);
                    return true;
                }
            }
            else {
                sender.sendMessage(PLUGIN+NO_PERMISSION);
                return true;
            }
            sender.sendMessage(PLUGIN + UNKNOWN_COMMAND);
            return false;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        int length = strings.length;
        List<String> tab=new ArrayList<>();
        if (length == 1 ){
            for (Map.Entry<String,LegendaryCommand> entry:commands.entrySet()){
                LegendaryCommand legendaryCommand=entry.getValue();
                if (legendaryCommand.getPermission().isEmpty() || (legendaryCommand.isAdmin() && commandSender.isOp()) || (commandSender.hasPermission(legendaryCommand.getPermission()))){
                    tab.add(entry.getKey());
                }
            }
            return tab;
        }
        else {
            String subCommand = strings[0];
            HashMap<String,LegendaryCommand> map=commands;
            LegendaryCommand legendaryCommand = map.get(subCommand);
            if (legendaryCommand != null){
                if ((legendaryCommand.isAdmin() && commandSender.isOp()) || (commandSender.hasPermission(legendaryCommand.getPermission()))){
                    return legendaryCommand.complete(commandSender,strings);
                }
            }
        }
        return null;
    }

    public void setCommands(HashMap<String, LegendaryCommand> commands) {
        this.commands = commands;
    }

    public void setPLUGIN(String PLUGIN) {
        this.PLUGIN = PLUGIN;
    }

    public void setHELP_PLAYER(List<String> HELP_PLAYER) {
        this.HELP_PLAYER = HELP_PLAYER;
    }

    public void setUNKNOWN_COMMAND(String UNKNOWN_COMMAND) {
        this.UNKNOWN_COMMAND = UNKNOWN_COMMAND;
    }

    public void setNO_PERMISSION(String NO_PERMISSION) {
        this.NO_PERMISSION = NO_PERMISSION;
    }

    public HashMap<String, LegendaryCommand> getCommands() {
        return commands;
    }

}
