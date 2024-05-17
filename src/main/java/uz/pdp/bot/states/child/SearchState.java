package uz.pdp.bot.states.child;

public enum SearchState implements State {
    SEARCH_JANR(null),
    SEARCH_RESULT(SEARCH_JANR);
    public SearchState prevState;

    SearchState(SearchState prevState) {
        this.prevState = prevState;
    }
}
