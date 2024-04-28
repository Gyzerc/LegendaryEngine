package com.gyzer.legendaryrealms.Menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class MenuDraw implements InventoryHolder {

    public Inventory inv;
    private MenuLoader loader;
    public Player p;
    public <T> MenuDraw(Player p,String menuId,MenuLoader loader){
        this.p = p;
        this.loader = loader;
        this.stringStore = new HashMap<>();
    }
    public MenuItem getMenuItem(int slot){
        return loader.getMenuItem(slot);
    }

    public String getPlaceHolder(String id){
        return loader.getPlaceHolder(id);
    }
    private HashMap<Integer,String> stringStore;
    public void DrawEssentail(Inventory inv){
        loader.legendaryPlugin.sync(new Runnable() {
            @Override
            public void run() {
                loader.getItem().forEach((integer, menuItem) -> {
                    inv.setItem(integer,menuItem.getItem());
                });
            }
        });

    }
    public List<String> getLayout(){
        return loader.getLayout();
    }

    public void DrawEssentailSpecial(Inventory inv,Consumer<MenuItem> consumer){

        loader.legendaryPlugin.sync(new Runnable() {
            @Override
            public void run() {
                for (Map.Entry<Integer,MenuItem> entry : loader.getItem().entrySet()){
                    MenuItem menuItem = entry.getValue();
                    MenuItem newMenu = new MenuItem(menuItem.getId(),menuItem.getItem().clone(),menuItem.getFuction());
                    consumer.accept(newMenu);
                    if (newMenu.isPut()) {
                        inv.setItem(entry.getKey(),newMenu.getItem());
                    }
                    if (newMenu.getValue() != null){
                        stringStore.put(entry.getKey(), newMenu.getValue());
                    }
                }
            }
        });
    }

    public String getString(int slot){
        return stringStore.get(slot);
    }
    public boolean dealEssentailsButton(int slot){
        MenuItem fuction = loader.getMenuItem(slot);
        if (fuction != null){
            switch (fuction.getFuction()){
                case "close": {
                    p.closeInventory();
                    return true;
                }
                case "none":
                    return true;
            }
        }
        return false;
    }

    public MenuLoader getLoader() {
        return loader;
    }

    public abstract void onClick(InventoryClickEvent e);

    public void open(){
        if (loader.getSound().isPresent()){
            p.playSound(p.getLocation(),loader.getSound().get(),1,1);
        }
        p.openInventory(inv);
    }

    public <T> boolean hasPage(int page, List<T> list){
        int start = 0 + (page-1) * getLayout().size();
        int end = getLayout().size() + (page-1)*getLayout().size();
        return list.size() > start ;
    }


    public <T> List<T> getPage(int page, List<T> list){
        int start = 0 + (page-1) * getLayout().size();
        int end = getLayout().size() + (page-1)*getLayout().size();
        List<T> rL = new ArrayList<>();
        for (int get = start;get < end ; get ++){
            if (list.size() > get) {
                rL.add(list.get(get));
            }
        }
        return rL;
    }
    @Override
    public Inventory getInventory() {
        return inv;
    }
}
