package uz.pdp.bot.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.DeleteMessage;
import uz.pdp.backend.model.Book;
import uz.pdp.backend.model.MyUser;
import uz.pdp.backend.service.BookService;
import uz.pdp.backend.service.UserService;
import uz.pdp.baen.BeanController;
import uz.pdp.bot.Bot;
import uz.pdp.bot.maker.MessageMaker;
import uz.pdp.bot.states.base.BaseState;
import uz.pdp.bot.states.child.MainState;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public abstract class BaseHandler {
    protected TelegramBot bot;
    protected MyUser curUser;
    protected Update update;
    protected UserService userService;
    protected BookService bookService;
    protected MessageMaker messageMaker;
    //    protected static final long ADMIN_ID = 1279888934;

    public BaseHandler() {
        this.bot = new TelegramBot(Bot.BOT_TOKEN);
        this.userService = BeanController.userServiceByThreadLocal.get();
        this.bookService = BeanController.bookServiceByThreadLocal.get();
        this.messageMaker = BeanController.messageMakerByThreadLocal.get();
    }

    public abstract void handle(Update update);


    protected MyUser getUserOrCreate(User from){
        MyUser myUser = userService.get(from.id());
        if (myUser==null){
            myUser = MyUser.builder()
                    .id(from.id())
                    .username(from.username())
                    .baseState(BaseState.MAIN_STATE.name())
                    .state(MainState.REGISTER.name())
                    .firstname(from.firstName())
                    .lastname(from.lastName())
                    .build();
            userService.save(myUser);
        }
        return myUser;
    }
    protected Book createBook(){
        List<Book> books = bookService.getAll();
        if (books.isEmpty()){
            String id = UUID.randomUUID().toString();
            Book book = new Book(id, null, null, null, null, null, null,false);
            book.setBookId(UUID.randomUUID().toString());
            bookService.save(book);
            return book;
        }
        int res=0;
        for (Book book : books) {
            if (book.isAdded()){
                res++;
            }
        }
        if (res==books.size()){
            String id = UUID.randomUUID().toString();
            Book book = new Book(id, null, null, null, null, null, null,false);
            book.setBookId(UUID.randomUUID().toString());
            bookService.save(book);
            return book;
        } else {
            for (Book book : books) {
                if (!book.isAdded()){
                    return book;
                }
            }
        }
        return null;
    }

    protected void deleteMessage(int messageId){
        DeleteMessage deleteMessage = new DeleteMessage(curUser.getId(), messageId);
        bot.execute(deleteMessage);
    }
}
