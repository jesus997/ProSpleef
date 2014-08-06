package me.CodigoPaisano.Spleef;

import java.io.File;
import me.CodigoPaisano.Spleef.ArenaManager.ArenaManager;
import me.CodigoPaisano.Spleef.Util.GhostPlayer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Spleef extends JavaPlugin{
    private ArenaManager a;
    private Economy econ = null;
    public static GhostPlayer spec;
    File arena;
    FileConfiguration arenas;
    
    @Override
    public void onEnable(){
        arena = new File(getDataFolder(), "arenas.yml");
        arenas = YamlConfiguration.loadConfiguration(arena);
        saveConfigArena();
        saveConfig();
        spec = new GhostPlayer(this);
        a = new ArenaManager(this);
        if(arenas.contains("Arenas.")){
            a.loadArenas(this);
        }
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
    
    public void saveConfigArena(){
        try{ 
            arenas.save(arena);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ConfigCreateArena(String name, int minPlayers, int maxPlayers){
        String path = "Arenas." + name + ".";
        arenas.set(path, "minPlayers." + minPlayers);
        arenas.set(path, "maxPlayers." + maxPlayers); 
        saveConfigArena();
    }
    
    public void ConfigAddJoinLocation(String arena, Location l){
        String path = "Arenas." + arena + ".";
        arenas.set(path, "JoinLocation." + a.serializeLoc(l));
        saveConfigArena();
    }
    
    public void ConfigAddEndLocation(String arena, Location l){
        String path = "Arenas." + arena + ".";
        arenas.set(path, "EndLocation." + a.serializeLoc(l));
        saveConfigArena();
    }
        
    public void ConfigAddSpawnsPont(String arena, Location l, int i){
        String path = "Arenas." + arena + ".";
        arenas.set(path, "spawns." + i + "." + a.serializeLoc(l));
        saveConfigArena();
    }
    
    public FileConfiguration getArenaConfig(){
        return this.arenas;
    }
}
