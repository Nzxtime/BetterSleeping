package laserlord.bettersleeping.Listener;

import laserlord.bettersleeping.BetterSleeping;
import org.bukkit.GameMode;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerEvent;

import java.util.List;


public class SleepingListener implements Listener {
    BetterSleeping plugin;
    private double multiplier = 0.5;

    public SleepingListener(BetterSleeping plugin) {
        if (plugin.getConfig().getString("multiplier") != null) {
            multiplier = plugin.getCustomConfig().getDouble("multiplier");
        }
    }

    @EventHandler
    public void onPlayerBedEnterEvent(PlayerBedEnterEvent event) {
        checkForNightSkip(event);
    }

    @EventHandler
    public void onPlayerBedLeaveEvent(PlayerBedLeaveEvent event) {
        checkForNightSkip(event);
    }


    private void checkForNightSkip(PlayerEvent event) {
        System.out.println("Triggered");
        World world = event.getPlayer().getWorld();
        long playerCount =
                (int) world.getPlayers().stream().filter(x -> x.getGameMode().equals(GameMode.SURVIVAL)).count();

        long neededPlayers = (long) Math.floor(playerCount / multiplier);
        List<Player> playerList = world.getPlayers();

        long sleepingPlayers = playerList.stream().filter(x -> x.getGameMode().equals(GameMode.SURVIVAL)).filter(LivingEntity::isSleeping).count();

        if (sleepingPlayers >= neededPlayers) {
            world.getPlayers().forEach(player ->
                    player.setStatistic(Statistic.TIME_SINCE_REST, 0));

            world.setStorm(false);
            world.setThundering(false);
            world.setTime(0);
        }
    }
}
