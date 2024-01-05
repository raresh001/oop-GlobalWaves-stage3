package commands;

import commands.adminCommands.*;
import commands.statisticsCommands.GetTop5AlbumsCommand;
import commands.statisticsCommands.GetTop5ArtistsCommand;
import commands.statisticsCommands.GetTop5PlaylistsCommand;
import commands.statisticsCommands.GetTop5SongsCommand;
import commands.userCommands.GetNotificationsCommand;
import commands.userCommands.WrappedCommand;
import commands.userCommands.artistCommands.*;
import commands.userCommands.hostCommands.AddAnnouncementCommand;
import commands.userCommands.hostCommands.AddPodcastCommand;
import commands.userCommands.hostCommands.RemoveAnnouncementCommand;
import commands.userCommands.hostCommands.RemovePodcastCommand;
import commands.userCommands.normalUserCommands.pageCommands.*;
import commands.userCommands.normalUserCommands.ShowPreferredSongsCommand;
import commands.userCommands.normalUserCommands.SwitchConnectionStatus;
import commands.userCommands.normalUserCommands.playerCommands.*;
import commands.userCommands.normalUserCommands.playlistCommands.*;
import commands.userCommands.normalUserCommands.premiumCommands.BuyPremiumCommand;
import commands.userCommands.normalUserCommands.premiumCommands.CancelPremiumCommand;
import commands.userCommands.normalUserCommands.searchCommands.SearchCommand;
import commands.userCommands.normalUserCommands.searchCommands.SelectCommand;
import fileio.input.CommandInput;

public final class CommandsFactory {
    private CommandsFactory() {
    }

    /**
     * @param commandInput - the command's info from input
     * @return - an instance of a class that implements Command
     */
    public static Command createCommand(final CommandInput commandInput) {
        return switch (commandInput.getCommand()) {
            case "search" -> new SearchCommand(commandInput);
            case "select" -> new SelectCommand(commandInput);
            case "load" -> new LoadCommand(commandInput);
            case "playPause" -> new PlayPauseCommand(commandInput);
            case "repeat" -> new RepeatCommand(commandInput);
            case "shuffle" -> new ShuffleCommand(commandInput);
            case "forward" -> new ForwardCommand(commandInput);
            case "backward" -> new BackwardCommand(commandInput);
            case "next" -> new NextCommand(commandInput);
            case "prev" -> new PrevCommand(commandInput);
            case "status" -> new StatusCommand(commandInput);
            case "like" -> new LikeCommand(commandInput);
            case "addRemoveInPlaylist" -> new AddRemoveInPlaylistCommand(commandInput);
            case "createPlaylist" -> new CreatePlaylistCommand(commandInput);
            case "switchVisibility" -> new SwitchVisibilityCommand(commandInput);
            case "follow" -> new FollowPlaylistCommand(commandInput);
            case "showPlaylists" -> new ShowPlaylistsCommand(commandInput);
            case "showPreferredSongs" -> new ShowPreferredSongsCommand(commandInput);
            case "getTop5Songs" -> new GetTop5SongsCommand(commandInput);
            case "getTop5Playlists" -> new GetTop5PlaylistsCommand(commandInput);
            case "switchConnectionStatus" -> new SwitchConnectionStatus(commandInput);
            case "getOnlineUsers" -> new GetOnlineUsersCommand(commandInput);
            case "addUser" -> new AddUserCommand(commandInput);
            case "addAlbum" -> new AddAlbumCommand(commandInput);
            case "showAlbums" -> new ShowAlbumsCommand(commandInput);
            case "printCurrentPage" -> new PrintPageCommand(commandInput);
            case "addEvent" -> new AddEventCommand(commandInput);
            case "addMerch" -> new AddMerchCommand(commandInput);
            case "getAllUsers" -> new GetAllUsersCommand(commandInput);
            case "deleteUser" -> new DeleteUserCommand(commandInput);
            case "addPodcast" -> new AddPodcastCommand(commandInput);
            case "addAnnouncement" -> new AddAnnouncementCommand(commandInput);
            case "removeAnnouncement" -> new RemoveAnnouncementCommand(commandInput);
            case "showPodcasts" -> new ShowPodcastsCommand(commandInput);
            case "removeAlbum" -> new RemoveAlbumCommand(commandInput);
            case "changePage" -> new ChangePageCommand(commandInput);
            case "removePodcast" -> new RemovePodcastCommand(commandInput);
            case "removeEvent" -> new RemoveEventCommand(commandInput);
            case "getTop5Albums" -> new GetTop5AlbumsCommand(commandInput);
            case "getTop5Artists" -> new GetTop5ArtistsCommand(commandInput);
            case "buyMerch" -> new BuyMerchCommand(commandInput);
            case "seeMerch" -> new SeeMerchCommand(commandInput);
            case "buyPremium" -> new BuyPremiumCommand(commandInput);
            case "cancelPremium" -> new CancelPremiumCommand(commandInput);
            case "subscribe" -> new SubscribeCommand(commandInput);
            case "getNotifications" -> new GetNotificationsCommand(commandInput);
            case "wrapped" -> new WrappedCommand(commandInput);
            case "adBreak" -> new AddBreakCommand(commandInput);
            case "nextPage" -> new NextPageCommand(commandInput);
            case "previousPage" -> new PreviousPageCommand(commandInput);
            case "updateRecommendations" -> new UpdateRecommendationsCommand(commandInput);
            case "loadRecommendations" -> new LoadRecommendationsCommand(commandInput);
            default -> new InvalidCommand(commandInput);
        };
    }
}
