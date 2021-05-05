package de.evoila.cf.notification.model;

import java.io.Serializable;

/**
 * Configurations of an SMTP server needed to send e-mails.
 */
public class SMTPConfig implements Serializable {

    public String id;

    // host and port name of the SMTP server
    public String host;
    public int port;

    // username and password to authenticate with the email server
    public String username;
    public String password;

    public SMTPConfig(String id, String host, int port, String username, String password) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
