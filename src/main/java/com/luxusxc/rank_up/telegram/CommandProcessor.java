package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.model.BotAction;
import com.luxusxc.rank_up.service.CommandParser;
import com.luxusxc.rank_up.telegram.commands.Command;
import com.luxusxc.rank_up.telegram.commands.CommandFactory;
import com.luxusxc.rank_up.telegram.commands.CommandType;
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
