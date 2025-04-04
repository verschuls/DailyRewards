package eu.athelion.dailyrewards.commandmanager.subcommand;

import eu.athelion.dailyrewards.DailyRewardsPlugin;
import eu.athelion.dailyrewards.commandmanager.SubCommand;
import eu.athelion.dailyrewards.configuration.file.Lang;
import eu.athelion.dailyrewards.manager.reward.Reward;
import eu.athelion.dailyrewards.manager.reward.action.ResetAction;
import eu.athelion.dailyrewards.util.PermissionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResetCommand implements SubCommand {
    @Override
    public String getName() {
        return "reset";
    }

    @Override
    public Lang getDescription() {
        return Lang.RESET_COMMAND_DESCRIPTION;
    }

    @Override
    public String getSyntax() {
        return "/reward reset <player> <daily|weekly|monthly|all>";
    }

    @Override
    public String getPermission() {
        return PermissionUtil.Permission.RESET_FOR_OTHERS.get();
    }

    @Override
    public List<String> getTabCompletion(CommandSender sender, int index, String[] args) {
        switch (index){
            case 0: return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
            case 1: return Stream.concat(DailyRewardsPlugin.getRewardManager().getRewards().stream().map(Reward::getName), Stream.of("all"))
                    .collect(Collectors.toList());

        }
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length != 2){
            sender.sendMessage(Lang.COMMAND_USAGE.asColoredString().replace("%usage%", getSyntax()));
            return;
        }

        final String playerName = args[0];

        DailyRewardsPlugin.get().runAsync(() -> new ResetAction(sender).preCheck(DailyRewardsPlugin.get().getServer().getOfflinePlayer(playerName), args[1]));
    }
}
