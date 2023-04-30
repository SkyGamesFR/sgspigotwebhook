package fr.skygames.sgspigotwebhook.listeners;

import fr.skygames.sgspigotwebhook.manager.Manager;
import fr.skygames.sgspigotwebhook.utils.Constants;
import fr.skygames.sgspigotwebhook.utils.DiscordWebhook;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;
import java.util.logging.Logger;

public class EventsListeners implements Listener {

    private final Logger logger;

    public EventsListeners(Logger logger) {
        this.logger = logger;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        int playerCount = Bukkit.getOnlinePlayers().size();

        event.setJoinMessage("§7[§6§lSkyGames§7] §6§l" + Constants.SERVER_NAME + " §7» §r" + event.getPlayer().getDisplayName() + " à rejoint le serveur !");

        DiscordWebhook webhook = new DiscordWebhook(Constants.WEBHOOK_URL);
        webhook.setUsername(player.getDisplayName());
        webhook.setAvatarUrl(Constants.getPlayerHead(player.getUniqueId().toString()));
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setDescription("**" + player.getDisplayName() + "** a rejoint le serveur ! (" + playerCount + "/" + Bukkit.getMaxPlayers() + ")")
                .setFooter(Constants.SERVER_FOOTER, Constants.SERVER_ICON)
                .setColor(Color.decode(Constants.SERVER_COLOR)));
        try {
            webhook.execute();
        } catch (Exception e) {
            logger.severe("Impossible d'envoyer le message de connexion d'un joueur sur le webhook. " + e.getMessage());
        }

        Manager.getInstance().getJda().getPresence().setActivity(Activity.listening(Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + " joueurs"));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        int playerCount = Bukkit.getOnlinePlayers().size();

        event.setQuitMessage("§7[§6§lSkyGames§7] §6§l" + Constants.SERVER_NAME + " §7» §r" + event.getPlayer().getDisplayName() + " à quitté le serveur !");

        DiscordWebhook webhook = new DiscordWebhook(Constants.WEBHOOK_URL);
        webhook.setUsername(player.getDisplayName());
        webhook.setAvatarUrl(Constants.getPlayerHead(player.getUniqueId().toString()));
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setDescription("**" + player.getDisplayName() + "** a quitté le serveur ! (" + playerCount + "/" + Bukkit.getMaxPlayers() + ")")
                .setFooter(Constants.SERVER_FOOTER, Constants.SERVER_ICON)
                .setColor(Color.decode(Constants.SERVER_COLOR)));
        try {
            webhook.execute();
        } catch (Exception e) {
            logger.severe("Impossible d'envoyer le message de déconnexion d'un joueur sur le webhook. " + e.getMessage());
        }

        Manager.getInstance().getJda().getPresence().setActivity(Activity.listening(Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + " joueurs"));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        DiscordWebhook webhook = new DiscordWebhook(Constants.WEBHOOK_URL);
        webhook.setAvatarUrl(Constants.getPlayerHead(player.getUniqueId().toString()));
        webhook.setUsername(player.getDisplayName());
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setDescription(event.getDeathMessage().replace("§r", ""))
                .setFooter(Constants.SERVER_FOOTER, Constants.SERVER_ICON)
                .setColor(Color.decode(Constants.SERVER_COLOR)));
        try {
            webhook.execute();
        } catch (Exception e) {
            logger.severe("Impossible d'envoyer le message de mort d'un joueur sur le webhook. " + e.getMessage());
        }
    }

    @EventHandler(priority =  EventPriority.LOW)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        message = message.replace("@everyone", "`@\u0435veryone`").replaceAll("@here", "`@h\u0435re`");

        DiscordWebhook webhook = new DiscordWebhook(Constants.WEBHOOK_URL);
        webhook.setUsername(player.getDisplayName());
        webhook.setAvatarUrl(Constants.getPlayerHead(player.getUniqueId().toString()));
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setDescription("**" + player.getDisplayName() + "** : " + message)
                .setColor(Color.decode(Constants.SERVER_COLOR)));
        try {
            webhook.execute();
        } catch (Exception e) {
            logger.severe("Impossible d'envoyer le message de chat d'un joueur sur le webhook. " + e.getMessage());
        }
    }

}
