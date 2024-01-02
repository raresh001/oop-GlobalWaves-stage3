package commands.statisticsCommands;

import commands.Command;
import fileio.input.CommandInput;

public abstract class StatisticsCommand extends Command {
    protected static final int STATISTICS_RESULTS_MAX_LENGTH = 5;
    protected StatisticsCommand(final CommandInput commandInput) {
        super(commandInput);
    }
}
