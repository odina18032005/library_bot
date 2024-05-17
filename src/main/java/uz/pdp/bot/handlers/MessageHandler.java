package uz.pdp.bot.handlers;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.backend.model.Book;
import uz.pdp.bot.states.base.BaseState;
import uz.pdp.bot.states.child.AddState;
import uz.pdp.bot.states.child.MainState;

import java.util.Arrays;
import java.util.Objects;

public class MessageHandler extends BaseHandler{
    @Override
    public void handle(Update update) {
        Message message = update.message();
        User from = message.from();
        super.update = update;
        super.curUser = getUserOrCreate(from);
        String text = message.text();
        if (Objects.equals(text,"/start")){
            super.curUser.setBaseState(BaseState.MAIN_STATE.name());
            if (Objects.isNull(curUser.getPhoneNumber()) || curUser.getPhoneNumber().isEmpty()) {
                enterPhoneNumber();
            } else {
                mainManu();
            }
        }else {
            String baseStateString = curUser.getBaseState();
            BaseState baseState = BaseState.valueOf(baseStateString);
            if (Objects.equals(baseState,BaseState.MAIN_STATE)){
                mainState();
            } else if (Objects.equals(baseState, BaseState.ADD_STATE)) {
                addState();
            } else if (Objects.equals(baseState, BaseState.SEARCH_STATE)) {
                searchState();
            }
        }
    }

    private void searchState() {

    }

    private void addState() {
        Message message = update.message();
        Book book = createBook();
        if (book!=null){
            String state = curUser.getState();
            AddState addState = AddState.valueOf(state);
            switch (addState){
                case ENTER_NAME ->{
                    if (message.text()==null){
                        SendMessage sendMessage = new SendMessage(curUser.getId(), "Please send text!!!");
                        bot.execute(sendMessage);
                        return;
                    }
                    SendMessage sendMessage = new SendMessage(curUser.getId(), "Enter author book");
                    bot.execute(sendMessage);
                    book.setName(message.text());
                    bookService.save(book);
                    curUser.setState(AddState.ENTER_AUTHOR.name());
                    userService.save(curUser);
                }
                case ENTER_AUTHOR -> {
                    if (message.text()==null){
                        SendMessage sendMessage = new SendMessage(curUser.getId(), "Please send text!!!");
                        bot.execute(sendMessage);
                        return;
                    }
                    SendMessage sendMessage = new SendMessage(curUser.getId(), "Enter discription book");
                    bot.execute(sendMessage);
                    book.setAuthor(message.text());
                    bookService.save(book);
                    curUser.setState(AddState.ENTER_DISC.name());
                    userService.save(curUser);
                }
                case ENTER_DISC -> {
                    if (message.text()==null){
                        SendMessage sendMessage = new SendMessage(curUser.getId(), "Please send text!!!");
                        bot.execute(sendMessage);
                        return;
                    }
                    SendMessage sendMessage = new SendMessage(curUser.getId(), "Send a photo of the book");
                    bot.execute(sendMessage);
                    book.setDiscription(message.text());
                    bookService.save(book);
                    curUser.setState(AddState.ENTER_PHOTO.name());
                    userService.save(curUser);
                }
                case ENTER_PHOTO -> {
                    if (message.photo()==null){
                        SendMessage sendMessage = new SendMessage(curUser.getId(), "Please send photo!!!");
                        bot.execute(sendMessage);
                        return;
                    }
                    SendMessage sendMessage = messageMaker.chooseJanr(curUser);
                    bot.execute(sendMessage);
                    String photoId = null;
                    for (PhotoSize photoSize : message.photo()) {
                        photoId = photoSize.fileId();
                        break;
                    }
                    book.setPhotoId(photoId);
                    bookService.save(book);
                    curUser.setState(AddState.ENTER_JANR.name());
                    userService.save(curUser);
                }
                case ENTER_PDF -> {
                    if (message.document()==null){
                        SendMessage sendMessage = new SendMessage(curUser.getId(), "Please send file!!!");
                        bot.execute(sendMessage);
                        return;
                    }
                    SendMessage sendMessage = new SendMessage(curUser.getId(), "Book has been added");
                    bot.execute(sendMessage);
                    Document document = message.document();
                    String fileId = document.fileId();
                    book.setBookId(fileId);
                    book.setAdded(true);
                    bookService.save(book);
                    curUser.setBaseState(BaseState.MAIN_STATE.name());
                    curUser.setState(MainState.MAIN_MENU.name());
                    userService.save(curUser);
                    SendMessage sMessage = messageMaker.mainMenu(curUser);
                    bot.execute(sMessage);
                }

            }
        }
    }

    private void mainState() {
        String stateStr = curUser.getState();
        MainState state = MainState.valueOf(stateStr);
        switch (state){
            case REGISTER -> {
                Message message = update.message();
                Contact contact = message.contact();
                if (contact!=null){
                    String phoneNumber = contact.phoneNumber();
                    curUser.setPhoneNumber(phoneNumber);
                    userService.save(curUser);
                    mainManu();
                }else {
                    incorrectData("Phone Number");
                }
            }

        }
    }

    private void incorrectData(String data  ) {
        bot.execute(new SendMessage(curUser.getId(),"You entered incorrect " + data));

    }

    private void mainManu() {
        SendMessage sendMessage = messageMaker.mainMenu(curUser);
        bot.execute(sendMessage);
        curUser.setState(MainState.MAIN_MENU.name());
        userService.save(curUser);
    }

    public void enterPhoneNumber(){
        SendMessage sendMessage = messageMaker.enterPhoneNumber(curUser);
        bot.execute(sendMessage);
    }
}
