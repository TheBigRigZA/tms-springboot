package com.vnptt.tms.dto;

public class CommandDTO extends AbstractDTO<CommandDTO>{
    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}