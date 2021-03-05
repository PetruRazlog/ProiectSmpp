package test2.client;

import lombok.Data;


public @Data class ClientConfig {

    private String systemId;
    private String password;
    private String host;
    private int port;

    public ClientConfig(String systemId, String password, String host, int port) {
        this.systemId = systemId;
        this.password = password;
        this.host = host;
        this.port = port;
    }

}
