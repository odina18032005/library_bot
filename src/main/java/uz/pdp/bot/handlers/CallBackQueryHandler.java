package uz.pdp.bot.handlers;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.backend.model.Book;
import uz.pdp.bot.states.base.BaseState;
import uz.pdp.bot.states.child.Janr;
import uz.pdp.bot.states.child.LibraryState;
import uz.pdp.bot.states.child.MainState;

import java.util.List;
import java.util.Objects;

public class CallBackQueryHandler extends BaseHandler {


    @Override
    public void handle(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        User from = callbackQuery.from();
        super.curUser = getUserOrCreate(from);
        super.update = update;
        String baseStateString = curUser.getBaseState();
        BaseState baseState = BaseState.valueOf(baseStateString);
        if (Objects.equals(curUser.getId(),ADMIN_ID)){
            curUser.setState(LibraryState.SAVE.name());
            userService.save(curUser);
            forAdminQuerys();
        } else {
            if (Objects.equals(baseState, BaseState.MAIN_STATE)) {
                mainSate();
            }
        }
    }

    private void forAdminQuerys() {
        String state = curUser.getState();
        LibraryState libraryState = LibraryState.valueOf(state);
        CallbackQuery callbackQuery = update.callbackQuery();
        Message message = callbackQuery.message();
        String data = callbackQuery.data();
        switch (libraryState){
            case SAVE ->  {
                SendMessage sendMessage = messageMakerAdmin.showJanrBook(curUser);
                bot.execute(sendMessage);
                curUser.setState(LibraryState.ENTER_RULE.name());
                userService.save(curUser);
                deleteMessage(message.messageId());
            }
            case ENTER_RULE -> {
                SendMessage sendMessage = new SendMessage(curUser.getId(), "cdcdcd");
                bot.execute(sendMessage);
            }
        }
    }


    private void bookCreate() {
        String stateStr = curUser.getState();
        LibraryState state = LibraryState.valueOf(stateStr);
        CallbackQuery callbackQuery = update.callbackQuery();
        Message message = callbackQuery.message();
        String data = callbackQuery.data();
        switch (state) {
            case ENTER_NAME -> {
                Book build = Book.builder()
                        .currency(data)
                        .build();
                archiveService.save(build);
                SendMessage sendMessage = messageMakerAdmin.enterNameForArchive(curUser);
                bot.execute(sendMessage);
                curUser.setState(LibraryState.ENTER_DISC.name());
                userService.save(curUser);
                deleteMessage(message.messageId());
            }case ENTER_DISC -> {}
        }
    }

    private void change() {

    }

    private void save() {

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
                List<Book> book = archiveService.getBooks(Janr.ROMANCE);
                SendMessage sendMessage = messageMaker.showResultForArchive(curUser, book);
                deleteMessage(message.messageId());
                bot.execute(sendMessage);
            }
            case "DRAMA" -> {
                List<Book> book = archiveService.getBooks(Janr.DRAMA);
                SendMessage sendMessage = messageMaker.showResultForArchive(curUser, book);
                deleteMessage(message.messageId());
                bot.execute(sendMessage);
            }
            case "FANTACY" ->{
                List<Book> book = archiveService.getBooks(Janr.FANTACY);
                SendMessage sendMessage = messageMaker.showResultForArchive(curUser, book);
                deleteMessage(message.messageId());
                bot.execute(sendMessage);
            }
            case "COMEDY" -> {
                List<Book> book = archiveService.getBooks(Janr.COMEDY);
                SendMessage sendMessage = messageMaker.showResultForArchive(curUser, book);
                deleteMessage(message.messageId());
                bot.execute(sendMessage);
            }
        }
    }
}