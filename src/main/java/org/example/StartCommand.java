package org.example;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class StartCommand implements BotCommand {

    private static final String COMMAND_IDENTIFIER = "start";
    private static final String DESCRIPTION = "Start the bot and receive a welcome message";

    @Override
    public String getCommandIdentifier() {
        return COMMAND_IDENTIFIER;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public void execute(AbsSender absSender, Update update) {
        Message message = update.getMessage();
        long chatId = message.getChatId();

        String welcomeMessage = "Welcome to the Ideallity! 🎉 We're thrilled to have you here. Our goal is to help you achieve your ideal body weight by providing personalized calculations and guidance. 🏋️‍♂️🥗\n\n" +
                "To get started, please answer the following question:\n\n" +
                "Please write down how much you would like to weigh?)" +
                "\n\n" +
                "We'll compare this number with the results of our calculations to give you a better understanding of your ideal weight range. Let's work together towards a healthier and happier you! 💪";

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(welcomeMessage);

        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

