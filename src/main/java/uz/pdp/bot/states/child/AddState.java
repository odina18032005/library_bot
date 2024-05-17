package uz.pdp.bot.states.child;

public enum AddState implements State {
    ENTER_NAME(null),
    ENTER_AUTHOR(ENTER_NAME),
    ENTER_DISC(ENTER_AUTHOR),
    ENTER_JANR(ENTER_DISC),
    ENTER_PHOTO(ENTER_JANR),
    ENTER_PDF(ENTER_PHOTO);
    public AddState prevstate;

    AddState(AddState prevstate) {
        this.prevstate = prevstate;
    }
}
