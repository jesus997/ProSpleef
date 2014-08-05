package me.CodigoPaisano.Spleef;

import me.CodigoPaisano.Spleef.Util.GhostPlayer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Spleef extends JavaPlugin{
    private Economy econ = null;
    public static GhostPlayer spec;
    
    @Override
    public void onEnable(){
        spec = new GhostPlayer(this);
    }
     
    @Override
    public void onDisable(){
        
    }
    
    public static void getColor(String p, String str){
        if("CONSOLE".equals(p)){
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', str));
        }else{
            Bukkit.getPlayer(p).sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[&f&lSpleef&a]" + str));
        }
    }
    
    public boolean setupEconomy(){
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
