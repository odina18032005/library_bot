package uz.pdp.bot.states.child;

import lombok.Getter;

@Getter
public enum LibraryState {
    SAVE(null),
    ENTER_RULE(SAVE),
    ENTER_NAME(ENTER_RULE),
    ENTER_DISC(ENTER_NAME),
    ENTER_FILE(ENTER_DISC);

    public LibraryState pervState;

    LibraryState(LibraryState pervState) {
        this.pervState = pervState;
    }
}
