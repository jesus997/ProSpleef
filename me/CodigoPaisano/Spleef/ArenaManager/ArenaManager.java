package me.CodigoPaisano.Spleef.ArenaManager;

import me.CodigoPaisano.Spleef.Contantes.Estado;
import me.CodigoPaisano.Spleef.Spleef;
import me.CodigoPaisano.Spleef.Util.UPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
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
    private static ArenaManager am = new ArenaManager();
    public static ArenaManager getManager(){
        return am;
    }
    
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
               //removePlayer(p);
           }
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
                
                if(a.getSpectator().contains(p.getName())){
                    //Remover jugador de espectador
                }
                
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
            
            // Estas como espectador.
        }else{
            // Ya eres un espectador!
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
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
