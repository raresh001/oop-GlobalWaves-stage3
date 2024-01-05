package commands.userCommands.artistCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import user.artist.Event;
import user.normalUser.Notification;

public final class AddEventCommand extends ArtistCommand {
    private static final int DATE_LENGTH = 10;
    private static final int[] YEAR_LIMITS = {1900, 2023};
    private static final int MAX_MONTH_VALUE = 12;
    private static final int MAX_DAY_VALUE = 31;
    private static final int MAX_DAY_VALUE_FEBRUARY = 28;
    private static final int FEBRUARY = 2;
    private static final int FIRST_DELIMITER_INDEX_IN_DATE_CHAR = 2;
    private static final int SECOND_DELIMITER_INDEX_IN_DATE_CHAR = 5;
    private static final int DAY_SUBSTRING_BEGIN = 0;
    private static final int DAY_SUBSTRING_END = FIRST_DELIMITER_INDEX_IN_DATE_CHAR;
    private static final int MONTH_SUBSTRING_BEGIN = FIRST_DELIMITER_INDEX_IN_DATE_CHAR + 1;
    private static final int MONTH_SUBSTRING_END = SECOND_DELIMITER_INDEX_IN_DATE_CHAR;
    private static final int YEAR_SUBSTRING_BEGIN = SECOND_DELIMITER_INDEX_IN_DATE_CHAR + 1;
    private static final int YEAR_SUBSTRING_END = 10;
    private final String name;
    private final String description;
    private final String date;
    public AddEventCommand(final CommandInput commandInput) {
        super(commandInput);
        name = commandInput.getName();
        description = commandInput.getDescription();
        date = commandInput.getDate();
    }

    private static boolean checkDate(final String date) {
        if (date.length() != DATE_LENGTH) {
            return false;
        }

        char[] dateArray = date.toCharArray();

        if (dateArray[FIRST_DELIMITER_INDEX_IN_DATE_CHAR] != '-'
                || dateArray[SECOND_DELIMITER_INDEX_IN_DATE_CHAR] != '-') {
            return false;
        }

        int day = Integer.parseInt(date.substring(DAY_SUBSTRING_BEGIN, DAY_SUBSTRING_END));
        int month = Integer.parseInt(date.substring(MONTH_SUBSTRING_BEGIN, MONTH_SUBSTRING_END));
        int year = Integer.parseInt(date.substring(YEAR_SUBSTRING_BEGIN, YEAR_SUBSTRING_END));

        return year >= YEAR_LIMITS[0] && year <= YEAR_LIMITS[1]
                && month >= 1 && month <= MAX_MONTH_VALUE
                && day >= 1 && day <= MAX_DAY_VALUE
                && (month != FEBRUARY || day <= MAX_DAY_VALUE_FEBRUARY);
    }

    /**
     * Executes add event command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateArtistResult("addEvent", output);
        if (objectNode == null) {
            return;
        }

        for (Event event : artist.getEvents()) {
            if (event.getName().equals(name)) {
                objectNode.put("message", username
                                                    + " has another event with the same name.");
                output.add(objectNode);
                return;
            }
        }

        if (!checkDate(date)) {
            objectNode.put("message", "Event for "
                                                + username
                                                + " does not have a valid date.");
            output.add(objectNode);
            return;
        }

        artist.getEvents().add(new Event(name, description, date));
        artist.notifySubscribers(new Notification("New Event",
                                "New Event from " + artist.getName() + "."));
        objectNode.put("message", username + " has added new event successfully.");
        output.add(objectNode);
    }
}
