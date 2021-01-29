/*
This file is part of the "IndividualKeepInventory" project.
You can find it here: https://github.com/Logics4/IndividualKeepInventory

Copyright (C) 2020  Logics4

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package io.github.logics4.individualkeepinventory.sponge;

import com.google.inject.Inject;

import io.github.logics4.individualkeepinventory.common.Constants;

import java.util.concurrent.Callable;

import org.bstats.sponge.Metrics2;

import org.spongepowered.api.Game;
import org.spongepowered.api.Platform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = "individualkeepinventory",
    name = PluginInfo.NAME,
    authors = {"Logics4"},
    version = PluginInfo.VERSION,
    description = PluginInfo.DESCRIPTION,
    url = PluginInfo.URL)
public class IKI {
    @Inject
    private Game game;

    private final Metrics2 metrics;

    @Inject
    public IKI(Metrics2.Factory metricsFactory) {
        int bStatsId = 10158; // plugin ID for bStats for Sponge
        metrics = metricsFactory.make(bStatsId);
    }

    // bStats's custom charts
    @Listener
    public void onServerStarted(GameStartedServerEvent event) {
        String implementationName = game.getPlatform().getContainer(Platform.Component.IMPLEMENTATION).getName();

        // Custom chart for bStats to collect SpongeAPI implementation name (e.g. "SpongeVanilla", "SpongeForge")
        metrics.addCustomChart(new Metrics2.SimplePie("spongeapi_implementation", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return implementationName;
            }
        }));
    }

    // the per-player keepInventory code
    @Listener
    public void onPlayerDeath(DestructEntityEvent.Death event, @Getter("getTargetEntity")Player player) {
        if (player.hasPermission(Constants.IKI_KEEPINVENTORY_PERMISSION)) {
            event.setKeepInventory(true);
        }
    }
}
