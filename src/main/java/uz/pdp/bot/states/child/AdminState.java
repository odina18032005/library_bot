package uz.pdp.bot.states.child;

public enum AdminState {
    ADMIN_STATE(null),
    SAVE(ADMIN_STATE),
    CHANGE(ADMIN_STATE);
    AdminState adminState;

    AdminState(AdminState adminState) {
        this.adminState = adminState;
    }
}
