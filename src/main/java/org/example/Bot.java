package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class Bot extends TelegramLongPollingBot {

    
    private InlineKeyboardMarkup keyboardM1;
    private InlineKeyboardMarkup keyboardM2;

    @Override
    public String getBotUsername() {
        return "moiseev_vpn_bot";
    }

    @Override
    public String getBotToken() {
        return "7884641664:AAHe0CNzozfsYAhR9IIuPQX0XL9rGNKMGXk";
    }




    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        var user = msg.getFrom();
        var id = user.getId();
        var txt = msg.getText();
        var next = InlineKeyboardButton.builder()
                .text("Next").callbackData("next")
                .build();

        var back = InlineKeyboardButton.builder()
                .text("Back").callbackData("back")
                .build();

        var url = InlineKeyboardButton.builder()
                .text("Tutorial")
                .url("https://core.telegram.org/bots/api")
                .build();
        keyboardM1 = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(next)).build();

//Buttons are wrapped in lists since each keyboard is a set of button rows
        keyboardM2 = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(back))
                .keyboardRow(List.of(url))
                .build();

         if(msg.isCommand()) {
                if (txt.equals("/menu")){
                    sendMenu(id, "<b>Menu 1</b>", keyboardM1);
                }

             if (txt.equals("/menu2")){
                 sendMenu(id, "<b>Menu 2</b>", keyboardM2);
             }
        }


       System.out.println(                user.getUserName() + "(" + user.getId() + "): " + msg.getText()        );
    }
        public void sendMenu(Long who, String txt, InlineKeyboardMarkup kb){
            SendMessage sm = SendMessage.builder().chatId(who.toString())
            .parseMode("HTML").text(txt)
            .replyMarkup(kb).build();

            try {
                  execute(sm);
             } catch (TelegramApiException e) {
                throw new RuntimeException(e);
                }
        }


private void buttonTap(Long id, String queryId, String data, int msgId) throws TelegramApiException {

    EditMessageText newTxt = EditMessageText.builder()
            .chatId(id.toString())
            .messageId(msgId).text("").build();

    EditMessageReplyMarkup newKb = EditMessageReplyMarkup.builder()
            .chatId(id.toString()).messageId(msgId).build();                           

    if(data.equals("next")) {
        newTxt.setText("MENU 2");
        newKb.setReplyMarkup(keyboardM2);
    } else if(data.equals("back")) {
        newTxt.setText("MENU 1");
        newKb.setReplyMarkup(keyboardM1);
    }

    AnswerCallbackQuery close = AnswerCallbackQuery.builder()
            .callbackQueryId(queryId).build();

    execute(close);
    execute(newTxt);
    execute(newKb);
}


    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }
}
