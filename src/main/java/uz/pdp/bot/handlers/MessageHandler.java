package uz.pdp.bot.handlers;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.bot.states.base.BaseState;
import uz.pdp.bot.states.child.AdminState;
import uz.pdp.bot.states.child.MainState;

import java.util.Objects;

public class MessageHandler extends BaseHandler{
    @Override
    public void handle(Update update) {
        Message message = update.message();
        User from = message.from();
        super.update = update;
        super.curUser = getUserOrCreate(from);
        if (Objects.equals(curUser.getId(),ADMIN_ID)){
            messageHandlerAdmin.handle(update);
        } else {
            String text = message.text();
            if (Objects.equals(text,"/start")){
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
