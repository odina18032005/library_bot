package uz.pdp.bot.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.DeleteMessage;
import uz.pdp.backend.model.MyUser;
import uz.pdp.backend.service.ArchiveService;
import uz.pdp.backend.service.UserService;
import uz.pdp.baen.BeanController;
import uz.pdp.bot.Bot;
import uz.pdp.bot.maker.MessageMaker;
import uz.pdp.bot.maker.MessageMakerAdmin;
import uz.pdp.bot.states.base.BaseState;
import uz.pdp.bot.states.child.MainState;

public abstract class BaseHandler {
    protected TelegramBot bot;
    protected MyUser curUser;
    protected Update update;
    protected UserService userService;
    protected ArchiveService archiveService;
    protected MessageMaker messageMaker;
    protected MessageMakerAdmin messageMakerAdmin;
    protected static final long ADMIN_ID = 1175039090;

    public BaseHandler() {
        this.bot = new TelegramBot(Bot.BOT_TOKEN);
        this.userService = BeanController.userServiceByThreadLocal.get();
        this.archiveService = BeanController.archiveServiceByThreadLocal.get();
        this.messageMaker = BeanController.messageMakerByThreadLocal.get();
        this.messageMakerAdmin = BeanController.messageMakerAdminByThreadLocal.get();
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

    protected void deleteMessage(int messageId){
        DeleteMessage deleteMessage = new DeleteMessage(curUser.getId(), messageId);
        bot.execute(deleteMessage);
    }
}
