package admin;

import audio.audioCollections.Album;
import audio.audioCollections.Playlist;
import audio.audioCollections.Podcast;
import audio.audioFiles.Song;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.UserInput;
import user.User;
import user.artist.Artist;
import user.host.Host;
import user.normalUser.NormalUser;

import java.util.ArrayList;
import java.util.LinkedList;

public final class Admin {
    private final Song ad;
    private final ArrayList<Song> library = new ArrayList<>();
    private final LinkedList<NormalUser> normalUsers = new LinkedList<>();
    private final LinkedList<Artist> artists = new LinkedList<>();
    private final LinkedList<Host> hosts = new LinkedList<>();
    private final ArrayList<Podcast> podcasts = new ArrayList<>();
    private final ArrayList<Album> albums = new ArrayList<>();
    private final LinkedList<Playlist> playlists = new LinkedList<>();
    private static Admin instance;
    private Admin(final LibraryInput libraryInput) {
        ad = new Song(libraryInput.getSongs().get(0));

        for (PodcastInput podcastInput : libraryInput.getPodcasts()) {
            podcasts.add(new Podcast(podcastInput));
        }

        // library input contains only normal users
        for (UserInput userInput : libraryInput.getUsers()) {
            normalUsers.add(new NormalUser(userInput));
        }
    }

    /**
     * initialise the only instance of Library
     * @param libraryInput - the object containing input information
     */
    public static void initialiseInstance(final LibraryInput libraryInput) {
        instance = new Admin(libraryInput);
    }

    /**
     * destroy the current instance (it should be reinitialised
     * at the beginning of each test)
     */
    public static void destroyInstance() {
        instance = null;
    }

    /**
     * throws error if instance was not initialised
     * @return the singleton instance, assured to be not null
     */
    private static Admin getInstance() {
        if (instance == null) {
            throw new UninitialisedInstanceError();
        }

        return instance;
    }

    public static Song getAdd() {
        return getInstance().ad;
    }

    /**
     * @return songs from the system (program fails if the instance was
     * not initialised)
     */
    public static ArrayList<Song> getSongs() {
        return getInstance().library;
    }

    /**
     * @return podcasts from the system (program fails if the instance was not initialised)
     */
    public static ArrayList<Podcast> getPodcasts() {
        return getInstance().podcasts;
    }

    /**
     * @return albums from the system (program fails if the instance was not initialised)
     */
    public static ArrayList<Album> getAlbums() {
        return getInstance().albums;
    }

    /**
     * @return playlists from the system (program fails if the instance was not initialised)
     */
    public static LinkedList<Playlist> getPlaylists() {
        return getInstance().playlists;
    }

    /**
     * @return normal users from the system (program fails if the instance was not initialised)
     */
    public static LinkedList<NormalUser> getNormalUsers() {
        return getInstance().normalUsers;
    }

    /**
     * @return artists from the system (program fails if the instance was not initialised)
     */
    public static LinkedList<Artist> getArtists() {
        return getInstance().artists;
    }

    /**
     * @return hosts from the system (program fails if the instance was not initialised)
     */
    public static LinkedList<Host> getHosts() {
        return getInstance().hosts;
    }

    /**
     * @param name - the name of the searched normal user
     * @return - the normal user from the system with the given name
     * (or null, if there is no such user)
     */
    public static NormalUser getNormalUserByName(final String name) {
        for (NormalUser normalUser : getNormalUsers()) {
            if (normalUser.getName().equals(name)) {
                return normalUser;
            }
        }

        return null;
    }

    /**
     * @param name - the name of the searched artist
     * @return - the artist from the system with the given name
     * (or null, if there is no such artist)
     */
    public static Artist getArtistByName(final String name) {
        for (Artist artist : getInstance().artists) {
            if (artist.getName().equals(name)) {
                return artist;
            }
        }

        return null;
    }

    /**
     * @param name - the name of the searched host
     * @return - the host from the system with the given name
     * (or null, if there is no such host)
     */
    public static Host getHostByName(final String name) {
        for (Host host : getHosts()) {
            if (host.getName().equals(name)) {
                return host;
            }
        }

        return null;
    }

    /**
     * deletes a normal user from the system (being guaranteed that this
     * action is valid and that there are no dependencies left)
     * @param normalUser - the user that needs to be removed
     */
    public static void removeUser(final NormalUser normalUser) {
        if (instance == null) {
            throw new UninitialisedInstanceError();
        }

        instance.normalUsers.remove(normalUser);
        for (Playlist playlist : normalUser.getPlaylists()) {
            // remove user's playlists from all followed lists
            for (NormalUser followingUser : playlist.getFollowingUsers()) {
                followingUser.getFollowedPlaylists().remove(playlist);
            }
            instance.playlists.remove(playlist);
        }

        for (Playlist playlist : normalUser.getFollowedPlaylists()) {
            playlist.getFollowingUsers().remove(normalUser);
        }
    }

    /**
     * deletes an artist from the system (being guaranteed that this
     * action is valid and that there are no dependencies left)
     * @param artist - the artist that needs to be removed
     */
    public static void removeUser(final Artist artist) {
        getArtists().remove(artist);
        for (Album album : artist.getAlbums()) {
            for (Song song : album.getSongs()) {
                // remove all songs from this album from liked lists and all playlists
                for (NormalUser normalUser : song.getLikes()) {
                    normalUser.getLikedSongs().remove(song);
                }

                for (Playlist playlist : song.getPlaylistList()) {
                    playlist.getSongs().remove(song);
                }
            }
            getAlbums().remove(album);
        }
    }

    /**
     * deletes a host from the system (being guaranteed that this
     * action is valid and that there are no dependencies left)
     * @param host - the host that needs to be removed
     */
    public static void removeUser(final Host host) {
        getHosts().remove(host);
        for (Podcast podcast : host.getPodcasts()) {
            getPodcasts().remove(podcast);
        }
    }

    /**
     * searches for a user with the given name in the system
     * @param name - the name of the searched user
     * @return - the user (it can be null)
     */
    public static User getUserByName(final String name) {
        User user = Admin.getNormalUserByName(name);
        if (user != null) {
            return user;
        }

        user = Admin.getArtistByName(name);
        if (user != null) {
            return user;
        }

        return Admin.getHostByName(name);
    }

    /**
     * update all players from the system
     * @param timestamp - the current moment of time
     */
    public static void updatePlayers(final int timestamp) {
        for (NormalUser normalUser : getNormalUsers()) {
            if (normalUser.isOnline()) {
                normalUser.getPlayer().updatePosition(timestamp);
            }
        }
    }
}
