package com.example.gblocalchat;

import java.util.Arrays;

public enum Command {
    AUTHOK("/authok"),
    AUTH("/auth"),
    PRIVATE_MESSAGE("/w"),
    END("/end"),
    CLIENTS("/clients"),
    CHANGE_NICK("/chn");


    private String command;

    Command(String command) {
        this.command = command;
    }

    public static Command getCommandByText(String text) {
        return Arrays.stream(values())
                .filter(cmd -> text.startsWith(cmd.getCommand()))
                .findAny().orElseThrow(() -> new RuntimeException("Несуществующая команда"));
    }

    public static boolean isCommand(String message) {
        return message.startsWith(Command.getCommandByPrefix());
    }

    public String getCommand() {
        return command;
    }

    public static String getCommandByPrefix() {
        return "/";
    }
}
