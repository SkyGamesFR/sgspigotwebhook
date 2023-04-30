package fr.skygames.sgspigotwebhook.manager;

import fr.skygames.sgspigotwebhook.listeners.DiscordListeners;
import fr.skygames.sgspigotwebhook.listeners.EventsListeners;
import fr.skygames.sgspigotwebhook.utils.Constants;
import fr.skygames.sgspigotwebhook.utils.DiscordWebhook;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.awt.*;
import java.io.File;
import java.util.Collection;

public class Manager {

    public static Manager instance;
    private Plugin plugin;
    private FileConfiguration config;
    private File configFile;
    private JDA jda;

    public Manager() {
        instance = this;
    }

    public void init(Plugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new EventsListeners(plugin.getLogger()), plugin);
    }

    public void loadConfig() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getLogger().info("Création du fichier de configuration...");
            plugin.saveDefaultConfig();
        }

        config = plugin.getConfig();

        if (config.getString("discord.token").equalsIgnoreCase("token")) {
            plugin.getLogger().severe("Veuillez renseigner le token du bot dans le fichier de configuration.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }

        if (config.getString("discord.channel").equalsIgnoreCase("channel")) {
            plugin.getLogger().severe("Veuillez renseigner l'ID du channel Discord dans le fichier de configuration.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }

        if(config.getString("discord.webhook").equalsIgnoreCase("webhook")) {
            plugin.getLogger().severe("Veuillez renseigner l'URL du webhook Discord dans le fichier de configuration.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    public void initJDA() {
        Collection<GatewayIntent> intents = GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS);

        try {
            jda = JDABuilder.createDefault(config.getString("discord.token"))
                    .setAutoReconnect(true)
                    .addEventListeners(new DiscordListeners(plugin))
                    .setEnabledIntents(intents)
                    .build();
        } catch (Exception e) {
            plugin.getLogger().severe("Impossible de se connecter au bot Discord. " + e.getMessage());
        }
    }

    public void initWebhook() {
        DiscordWebhook webhook = new DiscordWebhook(Constants.WEBHOOK_URL);
        webhook.setAvatarUrl(Constants.SERVER_ICON);
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setDescription(Constants.SERVER_NAME + " Démarrage du serveur...")
                .setFooter(Constants.SERVER_FOOTER, Constants.SERVER_ICON)
                .setColor(Color.decode("#00ff00")));
        try {
            webhook.execute();
        } catch (Exception e) {
            plugin.getLogger().severe("Impossible d'envoyer le message de démarrage du serveur sur le webhook. " + e.getMessage());
        }
    }

    public void shutdownWebhook() {
        DiscordWebhook webhook = new DiscordWebhook(Constants.WEBHOOK_URL);
        webhook.setAvatarUrl(Constants.SERVER_ICON);
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setDescription(Constants.SERVER_NAME + " Arrêt/Redémarrage du serveur...")
                .setFooter(Constants.SERVER_FOOTER, Constants.SERVER_ICON)
                .setColor(Color.decode("#ff0000")));
        try {
            webhook.execute();
        } catch (Exception e) {
            plugin.getLogger().severe("Impossible d'envoyer le message d'arrêt du serveur sur le webhook. " + e.getMessage());
        }
    }

    public static Manager getInstance() {
        return instance;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public JDA getJda() {
        return jda;
    }
}
