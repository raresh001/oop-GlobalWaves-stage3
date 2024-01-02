package fileio.input;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public final class CommandInput {
    private String command;
    private String username;
    private Integer timestamp;
    private String type;
    private FiltersInput filters;
    private Integer itemNumber;
    private Integer seed;
    private Integer playlistId;
    private String playlistName;
    private String name;
    private String description;
    private String date;
    private Integer price;
    private Integer age;
    private String city;
    private ArrayList<EpisodeInput> episodes;
    private Integer releaseYear;
    private ArrayList<SongInput> songs;
    private String nextPage;
    private String recommendationType;
}
