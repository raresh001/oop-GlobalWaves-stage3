package user.normalUser.page;

import user.normalUser.NormalUser;

public interface Page {
    enum AddMerchResult {
        INVALID_PAGE,
        NONEXISTENT_MERCH,
        SUCCESSFUL_PURCHASE
    }

    /**
     * Print the info of the page
     * @return - a string containing the information from the page
     */
    String printPage();

    /**
     * @param username - the name of a user
     * @return - a boolean indicating if username is the creator of this page
     */
    default boolean isCreatedByUser(final String username) {
        return false;
    }

    /**
     * Analyzes if a merch can be bought from this page
     * @param name - the name of the merch
     * @return -
     */
    default AddMerchResult acceptBuyMerch(final String name) {
        return AddMerchResult.INVALID_PAGE;
    }

    /**
     * accept the subscription for the owner of this page (visit)
     * @param normalUser - the user that subscribed
     * @return - a string containing the response of this action
     */
    default String acceptSubscribe(final NormalUser normalUser) {
        return "To subscribe you need to be on the page of an artist or host.";
    }
}
