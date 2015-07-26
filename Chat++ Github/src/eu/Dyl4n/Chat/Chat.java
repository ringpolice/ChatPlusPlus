// Testing

package eu.Dyl4n.Chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Chat extends JavaPlugin implements Listener {
	
	// Messages
	String usage;
	String chatClearMessage;
	String reloadMessage;
	String incorrectPermissions;
	String chatMuted;
	String toggleMuteMessage;
	String toggleUnmuteMessage;
	boolean isToggled;
	int blankSpaces;
	
	// Permissions
	String chatClearPermission;
	String chatTogglePermission;
	String chatBypassPermission;

	@Override
	public void onEnable() {
		usage = getConfig().getString("Usage");
		chatClearMessage = getConfig().getString("ChatClear_Message");
		reloadMessage = getConfig().getString("ReloadMessage");
		incorrectPermissions = getConfig().getString("NoPermissionsMessage");
		chatMuted = getConfig().getString("ChatMutedMessage");
		toggleMuteMessage = getConfig().getString("ChatToggleMutedMessage");
		toggleUnmuteMessage = getConfig().getString("ChatToggleUnmutedMessage");
		isToggled = getConfig().getBoolean("ChatToggled");
		blankSpaces = getConfig().getInt("ChatClear_BlankSpaces");
		chatClearPermission = getConfig().getString("ChatClearPermission");
		chatTogglePermission = getConfig().getString("ChatTogglePermission");
		chatBypassPermission = getConfig().getString("ChatBypassPermission");
		
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("Enabling Chat++ by JiNJaProductionz");
		getConfig().options().copyDefaults(true);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,String label, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("chat")){
				if(args.length < 1){
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', usage));
					return true;
				}else{
					
					if(args[0].equalsIgnoreCase("clear")){
						if(p.hasPermission(chatClearPermission)){
							for(int i = 0; i < blankSpaces;){
								Bukkit.broadcastMessage("");
								i++;
							}
							Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', chatClearMessage).replace("{PLAYER}", p.getName()));
							return true;
						}else{
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', incorrectPermissions));
							return true;
						}
					}
					
					if(args[0].equalsIgnoreCase("toggle")){
						if(p.hasPermission(chatTogglePermission)){
							if(isToggled){
								Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', toggleUnmuteMessage).replace("{PLAYER}", p.getName()));
								isToggled = false;
								return true;
							}else{
								Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', toggleMuteMessage).replace("{PLAYER}", p.getName()));
								isToggled = true;
								return true;
							}
						}else{
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', incorrectPermissions));
							return true;
						}
					}
				}
			}
		}else{
			if(cmd.getName().equalsIgnoreCase("chat")){
				if(args.length < 1){
					getLogger().info(ChatColor.translateAlternateColorCodes('&', usage));
					return true;
				}else{
					if(args[0].equalsIgnoreCase("clear")){
						for(int i = 0; i < blankSpaces;){
							Bukkit.broadcastMessage("");
							i++;
						}
						Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', chatClearMessage).replace("{PLAYER}", "Console"));
						return true;
					}
					
					if(args[0].equalsIgnoreCase("toggle")){
						if(isToggled){
							Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', toggleUnmuteMessage).replace("{PLAYER}", "Console"));
							isToggled = false;
							return true;
						}else{
							Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', toggleMuteMessage).replace("{PLAYER}", "Console"));
							isToggled = true;
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent e){
		Player p = e.getPlayer();
		if(isToggled){
			if(p.hasPermission(chatBypassPermission)){
				e.setCancelled(false);
			}else{
				e.setCancelled(true);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', chatMuted));
			}
		}
	}
	
}
