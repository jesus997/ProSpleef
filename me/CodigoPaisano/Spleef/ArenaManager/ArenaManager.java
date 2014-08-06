package me.CodigoPaisano.Spleef.ArenaManager;

import me.CodigoPaisano.Spleef.Contantes.Estado;
import me.CodigoPaisano.Spleef.Spleef;
import me.CodigoPaisano.Spleef.Util.UPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

/**
 *
 * @author jesus
 */
public class ArenaManager {
    private Spleef main;
    public ArenaManager(Spleef main){
        this.main = main;
    }
    
    /*private static ArenaManager am = new ArenaManager();
    public static ArenaManager getManager(){
        return am;
    }*/
    
    public Arena getArena(String arena){
        for(Arena a : Arena.arenaObjects){
            if(a.getName().equalsIgnoreCase(arena)){
                return a;
            }
        }
        
        return null;
    }
    
    public void startArena(String arena){
        if(getArena(arena) != null){
            Arena a = getArena(arena);
            a.estado = Estado.Iniciando;
        }
    }
    
    public void endArena(String arena){
        if(getArena(arena) != null){
           Arena a = getArena(arena);
           a.sendMessage(getColor("&2La arena ha finalizado! :)"));
           a.estado = Estado.Finalizada;
           
           for(String s : a.getPlayers()){
               Player p = Bukkit.getPlayer(s);
               removePlayers(p);
           }
        }
    }
    
    public void removeArena(Player p, String arena){
        if(getArena(arena) != null){
            Arena a = getArena(arena);
            Arena.arenaObjects.remove(a);
            // Eliminarla de la config
            p.sendMessage(getColor("&2Arena eliminada!"));
        }
    }
    
    public void addPlayer(Player p, String arena){
        if(getArena(arena) != null){
            Arena a = getArena(arena);
            if(!a.isFull()){
                if(!a.isInGame()){
                    UPlayer.getUPlayer(p);
                    removeItems(p);
                    
                    p.teleport(a.getJoinLocation());
                    a.getPlayers().add(p.getName());
                    
                    int numPlayer = a.getMinPlayers() - a.getPlayers().size();
                    a.sendMessage(getColor("&c%l" + p.getName() + " &2ha entrado en la arena! Faltan &a&l" + numPlayer + " &2jugadores para iniciar."));
                    
                    if(numPlayer == 0){
                        startArena(a.getName());
                    }
                }else{
                    p.sendMessage(getColor("&4La arena a la que intentas ingresar esta en juego!"));
                }
            }else{
                p.sendMessage(getColor("&4La arena a la intentas ingresar esta llena!"));
            }
        }else{
            p.sendMessage(getColor("&4No existe la arena con nombre &c&l" + arena + "&4!"));
        }
    }
    
    public void removePlayers(Player p){
        for(Arena a : Arena.arenaObjects){
            if(a.getPlayers().contains(p.getName())){
                removeItems(p);
                removeSpectator(p, a);
                
                p.teleport(a.getEndLocation());
                UPlayer.getUPlayer(p).exitPlayer();
                
                a.sendMessage(getColor("&c&l" + p.getName() + " &2ha salido de la arena! &f(&b&l" + a.getPlayers().size() + "&f)"));
            }else{
                p.sendMessage(getColor("&4No estas dentro de nunguna arena!"));
            }
        }
    }
    
    public void setSpectator(Player p, Arena a){
        if(!a.getSpectator().contains(p.getName())){
            a.getSpectator().add(p.getName());
            Spleef.spec.setGhost(p, true);
            p.teleport(a.getEndLocation());
            p.setGameMode(GameMode.ADVENTURE);
            p.setFlying(true);
            
            p.sendMessage(getColor("&2Has sido movido al grupo &b&lSpectador"));
        }
    }
    
    public void removeSpectator(Player p, Arena a){
        if(a.getSpectator().contains(p.getName())){
           p.setFlying(false);
           p.setGameMode(GameMode.SURVIVAL);
           Spleef.spec.removePlayer(p);
           a.getSpectator().remove(p.getName());
           
           p.sendMessage(getColor("&2Has sido removido del grupo &b&lSpectador"));
        }
    }
    
    public void giveItems(Player p){
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta cm = compass.getItemMeta();
        cm.setDisplayName(getColor("&c&lSelector de Mapas"));
        cm.addEnchant(Enchantment.KNOCKBACK, 1, true);
        compass.setItemMeta(cm);
        
        ItemStack door = new ItemStack(Material.IRON_DOOR);
        ItemMeta dm = door.getItemMeta();
        dm.setDisplayName(getColor("&4&lSalir"));
        cm.addEnchant(Enchantment.KNOCKBACK, 1, true);
        door.setItemMeta(dm);
        
        p.getInventory().setItem(0, compass);
        p.getInventory().setItem(8, door);
    }
    
    public void removeItems(Player p){
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.setHealth(2);
        p.setFoodLevel(0);
        p.setExp(0);
        p.setLevel(0);
        p.setFireTicks(0); 
        
        for(PotionEffect ef : p.getActivePotionEffects()){
            p.removePotionEffect(ef.getType());
        }
    }
    
    public String getColor(String str){
        return ChatColor.translateAlternateColorCodes('&', "&a[&f&lSpleef&a]" + str);
    }
    
    public void loadArenas(Spleef m){
        FileConfiguration fc = m.getArenaConfig();
        for(String name : fc.getConfigurationSection("Arenas").getKeys(false)){
            Location joinLocation = deserializeLoc(fc.getString("Arenas." + name + "joinLocation"));
            Location endLocation = deserializeLoc(fc.getString("Arenas." + name + "endLocation"));
            int minPlayers = fc.getInt("Arenas." + name + "minPlayers");
            int maxPlayers = fc.getInt("Arenas." + name + "maxPlayers");
            Arena a = new Arena(name, joinLocation, endLocation, minPlayers, maxPlayers);
            for(String s : fc.getConfigurationSection("spawns").getKeys(false)){
                a.addSpawnsPoint(deserializeLoc(s));
            }
        }
    }
    
    public void createArena(String name, int minPlayers, int maxPlayers){
        Arena a = new Arena(name, null, null, minPlayers, maxPlayers);
        main.ConfigCreateArena(name, minPlayers, maxPlayers);
    }
    
    public void createJoinLocation(Location l, String arena){
        if(getArena(arena) != null){
            Arena a = getArena(arena);
            a.setJoinLocation(l);
            main.ConfigAddJoinLocation(arena, l);
        }
    }

    public void createEndLocation(Location l, String arena){
        if(getArena(arena) != null){
            Arena a = getArena(arena);
            a.setEndLocation(l);
            main.ConfigAddEndLocation(arena, l);
        }
    }
    
    public void createSpawns(Location l, String arena, Player p){
        if(getArena(arena) != null){
            Arena a = getArena(arena);
            a.addSpawnsPoint(l); 
            main.ConfigAddSpawnsPont(a.getName(), l, a.getSpawnsPoint().size());
            p.sendMessage("&2Respawn &b&l" + a.getSpawnsPoint().size() + " &2creado para la arena &a&l" + a.getName());
        }
    }
    
    public String serializeLoc(Location l){
        return l.getWorld().getName()+","+l.getBlockX()+","+l.getBlockY()+","+l.getBlockZ(); 
    }
    
    public Location deserializeLoc(String s){
        String[] st = s.split(",");
        return new Location(Bukkit.getWorld(st[0]), Double.parseDouble(st[1]), Double.parseDouble(st[2]), Double.parseDouble(st[3]));
    }
}
