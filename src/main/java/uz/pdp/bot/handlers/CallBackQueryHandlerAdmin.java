package uz.pdp.bot.handlers;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.bot.states.base.BaseState;
import uz.pdp.bot.states.child.AdminState;

import java.util.Objects;

public class CallBackQueryHandlerAdmin extends BaseHandler {
    @Override
    public void handle(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        User from = callbackQuery.from();
        super.curUser = getUserOrCreate(from);
        super.update = update;
        String baseStateString = curUser.getBaseState();
        BaseState baseState = BaseState.valueOf(baseStateString);
        if (Objects.equals(baseState, BaseState.MAIN_STATE)) {
            adminState();
        }
    }
    private void adminState() {
        String stateStr = curUser.getState();
        AdminState state = AdminState.valueOf(stateStr);
        CallbackQuery callbackQuery = update.callbackQuery();
        switch (state) {
            case ADMIN_STATE -> {
                String data = callbackQuery.data();
                adminMenu(data);
                curUser.setState(data);
                userService.save(curUser);
            }
        }
    }

    private void adminMenu(String data) {
        CallbackQuery callbackQuery = update.callbackQuery();
        Message message = callbackQuery.message();
        switch (data) {
            case "SAVE" -> {
                SendMessage sendMessage = messageMakerAdmin.showJanrBook(curUser);
                deleteMessage(message.messageId());
                bot.execute(sendMessage);
                save();
            }
            case "CHANGE" -> {
                SendMessage sendMessage = messageMakerAdmin.showJanrBook(curUser);
                deleteMessage(message.messageId());
                bot.execute(sendMessage);
                change();
            }
        }
    }

    private void change() {

    }

    private void save() {

    }
}
