package com.luxusxc.rank_up.telegram.service;

import com.luxusxc.rank_up.telegram.model.BotAction;
import com.luxusxc.rank_up.telegram.command.Command;
import com.luxusxc.rank_up.telegram.command.CommandFactory;
import com.luxusxc.rank_up.telegram.command.CommandType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@AllArgsConstructor
public class CommandProcessor {
    private final CommandParser parser;

    public BotAction processUserCommand(Message message) {
        String messageText = message.getText();
        return bot -> {
            String commandBody = parser.getCommandBody(messageText);
            CommandFactory commandFactory = new CommandFactory(bot);
            CommandType commandType = CommandType.getUserInstance(commandBody);
            Command command = commandFactory.getCommand(commandType);
            command.execute(message);
        };
    }

    public BotAction processGroupCommand(Message message) {
        String messageText = message.getText();
        return bot -> {
            String commandBody = parser.getCommandBody(messageText);
            CommandFactory commandFactory = new CommandFactory(bot);
            CommandType commandType = CommandType.getGroupInstance(commandBody);
            Command command = commandFactory.getCommand(commandType);
            command.execute(message);
        };
    }
}
