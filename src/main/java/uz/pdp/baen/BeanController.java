package uz.pdp.baen;

import uz.pdp.backend.service.ArchiveService;
import uz.pdp.backend.service.CurrencyService;
import uz.pdp.backend.service.UserService;
import uz.pdp.bot.maker.MessageMaker;
import uz.pdp.bot.maker.MessageMakerAdmin;

public interface BeanController {
    ThreadLocal<UserService> userServiceByThreadLocal = ThreadLocal.withInitial(UserService::new);
    ThreadLocal<CurrencyService> currencyServiceByThreadLocal = ThreadLocal.withInitial(CurrencyService::new);
    ThreadLocal<ArchiveService> archiveServiceByThreadLocal = ThreadLocal.withInitial(ArchiveService::new);
    ThreadLocal<MessageMaker> messageMakerByThreadLocal = ThreadLocal.withInitial(MessageMaker::new);
    ThreadLocal<MessageMakerAdmin> messageMakerAdminByThreadLocal = ThreadLocal.withInitial(MessageMakerAdmin::new);
}
