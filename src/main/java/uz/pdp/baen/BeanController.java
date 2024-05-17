package uz.pdp.baen;

import uz.pdp.backend.service.BookService;
import uz.pdp.backend.service.UserService;
import uz.pdp.bot.maker.MessageMaker;

public interface BeanController {
    ThreadLocal<UserService> userServiceByThreadLocal = ThreadLocal.withInitial(UserService::new);
    ThreadLocal<BookService> bookServiceByThreadLocal = ThreadLocal.withInitial(BookService::new);
    ThreadLocal<MessageMaker> messageMakerByThreadLocal = ThreadLocal.withInitial(MessageMaker::new);
}
