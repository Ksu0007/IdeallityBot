package org.example;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

public interface BotCommand {
    String getCommandIdentifier();
    String getDescription();
    void execute(AbsSender absSender, Update update);
}

