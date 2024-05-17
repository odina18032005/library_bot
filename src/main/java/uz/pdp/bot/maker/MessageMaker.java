package uz.pdp.bot.maker;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import uz.pdp.backend.model.Book;
import uz.pdp.backend.model.MyUser;
import uz.pdp.bot.states.child.Janr;

import java.util.List;

public class MessageMaker {

    public SendMessage
    mainMenu(MyUser curUser){
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Main Menu: ");
        InlineKeyboardButton[][] buttons = {
                {
                        new InlineKeyboardButton("Add").callbackData("ADD"),
                        new InlineKeyboardButton("Search").callbackData("SEARCH")

                }
        };
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(markup);
        return sendMessage;
    }
    public SendMessage chooseJanr(MyUser curUser){
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Choose janr: ");
        InlineKeyboardButton[][] buttons = {
                {
                        new InlineKeyboardButton("Romance").callbackData(Janr.ROMANCE.name()),
                        new InlineKeyboardButton("Drama").callbackData(Janr.DRAMA.name())

                },
                {
                        new InlineKeyboardButton("Fantacy").callbackData(Janr.FANTACY.name()),
                        new InlineKeyboardButton("Comedy").callbackData(Janr.COMEDY.name())
                }
        };
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(markup);
        return sendMessage;
    }

     public SendPhoto showResultForArchive(MyUser curUser, Book book){
        String text;
        SendPhoto sendPhoto = new SendPhoto(curUser.getId(), book.getPhotoId());
        text = """
            Name: %s
            
            Author: %s
            
            Discription: %s""".formatted(book.getName(),book.getAuthor(),book.getDiscription());
        sendPhoto.caption(text);

        InlineKeyboardButton[][] buttons = {
                {
                        new InlineKeyboardButton("Read").callbackData(book.getId()),
                        new InlineKeyboardButton("Back").callbackData("BACK")
                }
        };
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        sendPhoto.replyMarkup(markup);

        return sendPhoto;

     }



    public SendMessage enterPhoneNumber(MyUser curUser){
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Enter Phone Number");
        KeyboardButton[][] buttons ={
                { new KeyboardButton("Phone Number").requestContact(true) }
        };
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(buttons).oneTimeKeyboard(true).resizeKeyboard(true);
        sendMessage.replyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }
}
