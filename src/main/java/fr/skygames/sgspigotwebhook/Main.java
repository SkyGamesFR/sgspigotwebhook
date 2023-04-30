package fr.skygames.sgspigotwebhook;

import fr.skygames.sgspigotwebhook.manager.Manager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private final Manager manager = new Manager();

    @Override
    public void onEnable() {
        manager.init(this);
        manager.loadConfig();
        manager.initJDA();
        manager.initWebhook();
    }

    @Override
    public void onDisable() {
        manager.shutdownWebhook();
    }
}
