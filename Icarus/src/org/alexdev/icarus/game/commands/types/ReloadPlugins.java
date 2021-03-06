package org.alexdev.icarus.game.commands.types;

import org.alexdev.icarus.game.commands.Command;
import org.alexdev.icarus.game.player.Player;
import org.alexdev.icarus.game.plugins.PluginManager;

public class ReloadPlugins extends Command {

    @Override
    public void handleCommand(Player player, String message) {
        PluginManager.disposePlugins();
        PluginManager.load();
    }

    @Override
    public void addPermissions() {
        this.permissions.add("administrator");
    }
}
