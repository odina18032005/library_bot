package uz.pdp.bot.maker;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.backend.model.Book;
import uz.pdp.backend.model.MyUser;

public class MessageMaker {

    public SendMessage mainMenu(MyUser curUser){
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Main Menu");
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
    public SendMessage enterDateForArchive(MyUser curUser){
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Enter Date (dd/mm/yyyy):");
        InlineKeyboardButton[][] buttons = {
                {
                        new InlineKeyboardButton("Back").callbackData("BACK")
                }
        };
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(markup);
        return sendMessage;
    }
     public SendMessage showResultForArchive(MyUser curUser, Book book){

        String text = """
                Result:
                    %s
                    Name: %s
                    Discription: %s
                """.formatted(book.getPhotoId(),book.getName(),book.getDiscription());

        SendMessage sendMessage = new SendMessage(curUser.getId(), text);
        InlineKeyboardButton[][] buttons = {
                {
                        new InlineKeyboardButton("Read").callbackData("READ"),
                        new InlineKeyboardButton("Back").callbackData("BACK")
                }
        };
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(markup);
        return sendMessage;
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
