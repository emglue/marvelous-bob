package com.marvelousbob.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.marvelousbob.common.network.constants.NetworkConstants;
import com.marvelousbob.common.network.register.Msg;
import com.marvelousbob.common.network.register.Ping;
import com.marvelousbob.common.network.register.Register;
import lombok.SneakyThrows;

public class MainServer {

    private final Server server;

    @SneakyThrows
    public MainServer() {
        this.server = new Server();
        Register.registerClasses(server);

        server.addListener(new Listener.ThreadedListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                System.out.println(
                        "[SERVER SIDE] : Connected with " + connection.getRemoteAddressTCP()
                                .toString());
                server.sendToTCP(connection.getID(), new Msg("connection success!"));
            }

            @Override
            public void received(Connection connection, Object o) {
                if (o instanceof Msg) {
                    Msg m = (Msg) o;
                    server.sendToTCP(connection.getID(), m);
                }
                if (o instanceof Ping) {
                    Ping p = (Ping) o;
                    server.sendToTCP(connection.getID(), p);
                }
            }
        }));
        server.start();
        server.bind(NetworkConstants.PORT);
    }
}
