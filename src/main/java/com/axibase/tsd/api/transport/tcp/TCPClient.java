package com.axibase.tsd.api.transport.tcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Dmitry Korchagin.
 */
public class TCPClient {
    private String url;
    private Integer port;
    private StringBuilder command;

    TCPClient(String url, Integer port) {
        this.url = url;
        this.port = port;
        command = new StringBuilder();
    }

    public void setCommand(String command) {
        this.command = new StringBuilder(command);
    }

    public void appendCommand(String commandPart) {
        command.append(commandPart);
    }

    public boolean send() throws IOException {
        Socket socket = new Socket(url, port);
        DataOutputStream requesStream = new DataOutputStream(socket.getOutputStream());
        BufferedReader responseStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        requesStream.writeBytes(command.append('\n').toString());
        String response = responseStream.readLine();
        return response.equals("ok");
    }

}
