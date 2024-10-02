package com.luxusxc.rank_up.telegram;

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
    private final CommandParser commandParser;

    public BotAction processCommand(Message message) {
        String messageText = message.getText();
        return bot -> {
            CommandFactory commandFactory = new CommandFactory(bot);
            String commandBody = commandParser.getCommandBody(messageText);
            CommandType commandType = CommandType.getInstance(commandBody);
            Command command = commandFactory.getCommand(commandType);
            command.execute(message);
        };
    }
}
