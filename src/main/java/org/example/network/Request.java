package org.example.network;

import java.util.Map;
import java.util.Objects;

public class Request {
    private String username;
    private String command;
    private Map<String, Object> data;

    public Request() {
    }

    public Request(String username, String command, Map<String, Object> data) {
        this.username = username;
        this.command = command;
        this.data = data;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;
        return Objects.equals(username, request.username) &&
                Objects.equals(command, request.command) &&
                Objects.equals(data, request.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, command, data);
    }
}