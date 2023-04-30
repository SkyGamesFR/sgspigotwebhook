package fr.skygames.sgspigotwebhook.listeners;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DiscordListeners extends ListenerAdapter {

    Plugin plugin;

    public DiscordListeners(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        TextChannel channel = event.getGuild().getTextChannelById(plugin.getConfig().get("discord.channel").toString());

        if (event.getChannel() != channel) return;
        if(event.getAuthor().isBot()) return;

        Message message = event.getMessage();
        String content = message.getContentRaw();

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("§l§8[§bDISCORD§8§l] §f§r" + event.getMessage().getAuthor().getName() + " §8§l>§r "+ content);

            if (content.contains(player.getName())) {
                content = content.replace(player.getName(), "§b@" + player.getName() + "§f");
            }
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        plugin.getLogger().info("Le bot est connecté à Discord.");
        event.getJDA().getPresence().setActivity(Activity.listening(Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + " joueurs"));
    }
}
