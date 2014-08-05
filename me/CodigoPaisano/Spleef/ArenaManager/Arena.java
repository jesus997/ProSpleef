package me.CodigoPaisano.Spleef.ArenaManager;

import java.util.ArrayList;
import me.CodigoPaisano.Spleef.Contantes.Estado;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 *
 * @author jesus
 */
public class Arena {
    public static ArrayList<Arena> arenaObjects = new ArrayList<>();
    private ArrayList<Location> spawns = new ArrayList<>();
    private Location joinLocation, endLocation;
    private String Arenaname;
    private ArrayList<String> players = new ArrayList<>();
    private ArrayList<String> spectators = new ArrayList<>();
    private int minPlayer, maxPlayers;
    public Estado estado = Estado.SinIniciar;
    
    public Arena(String name, Location joinLocation, Location endLocation, ArrayList<Location> spawns, int minPlayers, int maxPlayers){
        this.Arenaname = name;
        this.joinLocation = joinLocation;
        this.endLocation = endLocation;
        this.spawns = spawns;
        this.minPlayer = minPlayers;
        this.maxPlayers = maxPlayers;
        
        arenaObjects.add(this);
    }
    
    public Location getJoinLocation(){
        return this.joinLocation;
    }
    
    public void setJoinLocation(Location joinLocation){
        this.joinLocation = joinLocation;
    }
    
    public ArrayList<Location> getSpawnsPoint(){
        return this.spawns;
    }
    
    public void addSpawnsPoint(Location spawn){
        this.spawns.add(spawn);
    }
    
    public Location getEndLocation(){
        return this.endLocation;
    }
    
    public void setEndLocation(Location endLocation){
        this.endLocation = endLocation;
    }
    
    public String getName(){
        return this.Arenaname;
    }
    
    public void setName(String name){
        this.Arenaname = name;
    }
    
    public int getMaxPlayers(){
        return this.maxPlayers;
    }
    
    public void setMaxPlayers(int maxPlayers){
        this.maxPlayers = maxPlayers;
    }
    
    public int getMinPlayers(){
        return this.minPlayer;
    }
    
    public void setMixPlayers(int minPlayers){
        this.minPlayer = minPlayers;
    }
    
    public ArrayList<String> getPlayers(){
        return this.players;
    }
    
    public ArrayList<String> getSpectator(){
        return this.players;
    }
    
    public boolean isFull(){
        return players.size() >= maxPlayers;
    }
    
    public boolean isInGame(){
        if(this.estado == Estado.Iniciando || this.estado == Estado.EnCurso){
            return true;
        }
        return false;
    }
    
    /*public boolean validateArena(Arena a){
        
    }*/
    
    public void sendMessage(String message){
        for(String s : players){
            Bukkit.getPlayer(s).sendMessage(message);    
        }
    }
}
