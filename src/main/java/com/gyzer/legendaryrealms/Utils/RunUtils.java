package com.gyzer.legendaryrealms.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class RunUtils {
    private List<String> runs;
    private Player p;

    public RunUtils(List<String> runs, Player p) {
        this.runs = runs;
        this.p = p;
    }

    public void run(){
        runs.forEach(s -> deal(s));
    }

    private void deal(String str){
        String symbol = str.split(";")[0];
        String value = str.split(";")[1].replace("%player%",p.getName());
        switch (symbol){
            case "msg" :
                p.sendMessage(dealMsg(value));
                break;
            case "title" :
                String[] values = value.split(",");
                String main = values[0];
                String sub = values.length > 1 ? values[1] : "";
                p.sendTitle(dealMsg(main),dealMsg(sub));
                break;
            case "player" :
                p.performCommand(value);
                break;
            case "op" :
                if (p.isOp()){
                    p.performCommand(value);
                } else {
                    p.setOp(true);
                    p.performCommand(value);
                    p.setOp(false);
                }
                break;
            case "console" :
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),value);
                break;
            case "sound" :
                String id = value.toUpperCase();
                p.playSound(p.getLocation(), Sound.valueOf(id),1,1);
                break;
            case "broad" :
                Bukkit.broadcastMessage(value);
                break;
        }
    }

    private String dealMsg(String str){
        return MsgUtils.msg(str);
    }
}
