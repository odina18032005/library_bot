package uz.pdp.bot.handlers;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.bot.states.base.BaseState;
import uz.pdp.bot.states.child.AdminState;

import java.util.Objects;

public class MessageHandlerAdmin extends BaseHandler {
    @Override
    public void handle(Update update) {
        Message message = update.message();
        User from = message.from();
        super.update = update;
        super.curUser = getUserOrCreate(from);
        String baseStateString = curUser.getBaseState();
        BaseState baseState = BaseState.valueOf(baseStateString);
        if (Objects.equals(baseState,BaseState.MAIN_STATE)){
            adminMenu();
        }
    }
    private void adminMenu() {
        curUser.setState(AdminState.ADMIN_STATE.name());
        userService.save(curUser);
        SendMessage sendMessage = messageMakerAdmin.adminMenu(curUser);
        bot.execute(sendMessage);
    }
}
