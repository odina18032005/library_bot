package uz.pdp.bot.handlers;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import uz.pdp.backend.model.Book;
import uz.pdp.bot.states.base.BaseState;
import uz.pdp.bot.states.child.AddState;
import uz.pdp.bot.states.child.Janr;
import uz.pdp.bot.states.child.MainState;
import uz.pdp.bot.states.child.SearchState;

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
        if (Objects.equals(baseState, BaseState.MAIN_STATE)) {
            mainSate();
        } else if (Objects.equals(baseState, BaseState.ADD_STATE)) {
            addState();
        } else if (Objects.equals(baseState, BaseState.SEARCH_STATE)) {
            searchState();
        }
    }

    private void searchState() {
        CallbackQuery callbackQuery = update.callbackQuery();
        Message message = callbackQuery.message();
        String state = curUser.getState();
        SearchState searchState = SearchState.valueOf(state);
        switch (searchState) {
            case SEARCH_JANR -> {
                List<Book> books = bookService.getBooks(callbackQuery.data());
                if (books.isEmpty()){
                    bot.execute(new SendMessage(curUser.getId(),"Afsus bu janrdagi kotiblar yoq..."));
                    curUser.setBaseState(BaseState.MAIN_STATE.name());
                    curUser.setState(MainState.MAIN_MENU.name());
                    userService.save(curUser);
                    SendMessage sMessage = messageMaker.mainMenu(curUser);
                    bot.execute(sMessage);
                    return;
                }
                books.forEach(book ->{
                    SendPhoto sendPhoto = messageMaker.showResultForArchive(curUser, book);
                    SendResponse execute = bot.execute(sendPhoto);
                    if (!execute.isOk()) {
                        System.out.println(execute.description());
                    }
                } );
                curUser.setState(SearchState.SEARCH_RESULT.name());
                userService.save(curUser);
            } case SEARCH_RESULT -> {
                String data = callbackQuery.data();
                if (Objects.equals("BACK",data)){
                    curUser.setBaseState(BaseState.MAIN_STATE.name());
                    curUser.setState(MainState.MAIN_MENU.name());
                    userService.save(curUser);
                    SendMessage sMessage = messageMaker.mainMenu(curUser);
                    bot.execute(sMessage);
                } else {
                    for (Book book : bookService.getAll()) {
                        if (book.isAdded() && Objects.equals(book.getId(),data)){
                            SendDocument sendDocument = new SendDocument(curUser.getId(), book.getBookId());
                            bot.execute(sendDocument);
                        }
                    }
                }
            }
        }

    }

    private void addState() {
        CallbackQuery callbackQuery = update.callbackQuery();
        String data = callbackQuery.data();
        String state = curUser.getState();
        AddState addState = AddState.valueOf(state);
        Book book = createBook();
        switch (addState) {
            case ENTER_JANR -> {
                SendMessage sendMessage = new SendMessage(curUser.getId(), "Send PDF book");
                bot.execute(sendMessage);
                book.setJanr(data);
                curUser.setState(AddState.ENTER_PDF.name());
                bookService.save(book);
                userService.save(curUser);
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
            case "ADD" ->{
                curUser.setBaseState(BaseState.ADD_STATE.name());
                curUser.setState(AddState.ENTER_NAME.name());
                userService.save(curUser);
                SendMessage sendMessage = new SendMessage(curUser.getId(), "Enter book name");
                bot.execute(sendMessage);
                deleteMessage(message.messageId());
            }
            case "SEARCH" ->{
                curUser.setBaseState(BaseState.SEARCH_STATE.name());
                curUser.setState(SearchState.SEARCH_JANR.name());
                userService.save(curUser);
                chooseJanr();
                deleteMessage(message.messageId());
            }
        }
    }


    private void chooseJanr() {
        CallbackQuery callbackQuery = update.callbackQuery();
        Message message = callbackQuery.message();
        SendMessage sendMessage = messageMaker.chooseJanr(curUser);
        deleteMessage(message.messageId());
        bot.execute(sendMessage);
        curUser.setState(SearchState.SEARCH_JANR.name());
        curUser.setBaseState(BaseState.SEARCH_STATE.name());
        userService.save(curUser);
    }
}