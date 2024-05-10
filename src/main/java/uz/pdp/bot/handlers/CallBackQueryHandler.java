package uz.pdp.bot.handlers;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.backend.model.Book;
import uz.pdp.bot.states.base.BaseState;
import uz.pdp.bot.states.child.Janr;
import uz.pdp.bot.states.child.MainState;

import java.util.Objects;

public class CallBackQueryHandler extends BaseHandler {


    @Override
    public void handle(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        User from = callbackQuery.from();
        super.curUser = getUserOrCreate(from);
        super.update = update;
        if (Objects.equals(curUser.getId(),ADMIN_ID)){
            callBackQueryHandlerAdmin.handle(update);
        } else {
            String baseStateString = curUser.getBaseState();
            BaseState baseState = BaseState.valueOf(baseStateString);
            if (Objects.equals(baseState, BaseState.MAIN_STATE)) {
                mainSate();
            }
        }
    }

    private void mainSate() {
        String stateStr = curUser.getState();
        MainState state = MainState.valueOf(stateStr);
        CallbackQuery callbackQuery = update.callbackQuery();
        switch (state) {
            case MAIN_MENU -> {
                String data = callbackQuery.data();
                mainMenu(data);
            }
        }
    }

    private void mainMenu(String data) {
        CallbackQuery callbackQuery = update.callbackQuery();
        Message message = callbackQuery.message();
        switch (data) {
            case "ROMANCE" -> {
                Book book = archiveService.get(Janr.ROMANCE);
                SendMessage sendMessage = messageMaker.showResultForArchive(curUser, book);
                deleteMessage(message.messageId());
                bot.execute(sendMessage);
            }
            case "DRAMA" -> {
                Book book = archiveService.get(Janr.DRAMA);
                SendMessage sendMessage = messageMaker.showResultForArchive(curUser, book);
                deleteMessage(message.messageId());
                bot.execute(sendMessage);
            }
            case "FANTACY" ->{
                Book book = archiveService.get(Janr.FANTACY);
                SendMessage sendMessage = messageMaker.showResultForArchive(curUser, book);
                deleteMessage(message.messageId());
                bot.execute(sendMessage);
            }
            case "COMEDY" -> {
                Book book = archiveService.get(Janr.COMEDY);
                SendMessage sendMessage = messageMaker.showResultForArchive(curUser, book);
                deleteMessage(message.messageId());
                bot.execute(sendMessage);
            }
        }
    }
}