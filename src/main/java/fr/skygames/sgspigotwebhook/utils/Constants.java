package fr.skygames.sgspigotwebhook.utils;

import fr.skygames.sgspigotwebhook.manager.Manager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {

    public static final String WEBHOOK_URL = Manager.getInstance().getConfig().getString("discord.webhook");
    public static final String SERVER_NAME = Manager.getInstance().getConfig().getString("server.name");
    public static final String SERVER_COLOR = Manager.getInstance().getConfig().getString("server.color");
    public static final String SERVER_ICON = Manager.getInstance().getConfig().getString("server.icon");
    public static final String SERVER_FOOTER = "SkyGames" + " | " + new SimpleDateFormat("'le' dd/MM/yyyy '\u00E0' kk:mm:ss").format(new Date());

    public static String getPlayerHead(String playerName) {
        return "https://crafatar.com/avatars/" + playerName + "?size=128&overlay";
    }

}
