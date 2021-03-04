package test2.client;

public class ClientConfig {

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

    public String getSystemId() {
        return systemId;
    }

    @SuppressWarnings("unused")
    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getPassword() {
        return password;
    }

    @SuppressWarnings("unused")
    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    @SuppressWarnings("unused")
    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    @SuppressWarnings("unused")
    public void setPort(int port) {
        this.port = port;
    }
}
