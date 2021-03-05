package test2.client;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ClientConfig {

    private String systemId;
    private String password;
    private String host;
    private int port;

}
