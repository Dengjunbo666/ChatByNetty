package com.dengjunbo.netty.protocol.response;

import com.dengjunbo.netty.protocol.Packet;
import com.dengjunbo.netty.protocol.command.Command;
import lombok.Data;

@Data
public class MessageResponsePacket extends Packet {
    private String message;
    @Override
    public Byte getCommand() {
        return Command.MESSAGE_RESPONSE;
    }
}
