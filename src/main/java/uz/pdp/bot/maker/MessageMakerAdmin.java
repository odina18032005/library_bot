package uz.pdp.bot.maker;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.backend.model.MyUser;

public class MessageMakerAdmin {
    public SendMessage adminMenu(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Add Book: ");
        InlineKeyboardButton[][] buttons = {
                {
                        new InlineKeyboardButton("Save").callbackData("SAVE"),
                        new InlineKeyboardButton("Change").callbackData("COMEDY")
                }
        };
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(markup);
        return sendMessage;
    }

    public SendMessage enterNameForArchive(MyUser curUser){
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Enter name: ");
        InlineKeyboardButton[][] buttons = {
                {
                        new InlineKeyboardButton("Back").callbackData("BACK")
                }
        };
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(markup);
        return sendMessage;
    }



    /*public SendMessage enterDiscriptionForArchive(MyUser curUser){}
    public SendMessage enterPhotoForArchive(MyUser curUser){}
    public SendMessage enterFileForArchive(MyUser curUser){}*/

    public SendMessage showJanrBook(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Add Book: ");
        InlineKeyboardButton[][] buttons = {
                {
                        new InlineKeyboardButton("Romance").callbackData("ROMANCE"),
                        new InlineKeyboardButton("Drama").callbackData("DRAMA")

                },
                {
                        new InlineKeyboardButton("Fantacy").callbackData("FANTACY"),
                        new InlineKeyboardButton("Comedy").callbackData("COMEDY")
                }
        };
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(markup);
        return sendMessage;
    }
}
