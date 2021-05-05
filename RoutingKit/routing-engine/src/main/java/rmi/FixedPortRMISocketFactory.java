package rmi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMISocketFactory;

/**
 * Created by Thomas Farrenkopf on 08.03.2018.
 */
public class FixedPortRMISocketFactory extends RMISocketFactory {

    public FixedPortRMISocketFactory() {
        super();
    }

    /**
     * Creates a client socket connected to the specified host and port and writes out debugging info
     *
     * @param host the host name
     * @param port the port number
     * @return a socket connected to the specified host and port.
     * @throws IOException if an I/O error occurs during socket creation
     */
    public Socket createSocket(String host, int port)
            throws IOException {
        port = (port == 0 ? RMIConfiguration.FIXED_SOCKET_PORT : port);
        System.out.println("Creating socket to host : " + host + " on port " + port);
        return new Socket(host, port);
    }

    /**
     * Create a server socket on the specified port (port 0 indicates
     * an anonymous port) and writes out some debugging info
     *
     * @param port the port number
     * @return the server socket on the specified port
     * @throws IOException if an I/O error occurs during server socket
     *                     creation
     */
    public ServerSocket createServerSocket(int port)
            throws IOException {
        port = (port == 0 ? RMIConfiguration.FIXED_SOCKET_PORT : port);
        System.out.println("Creating ServerSocket on port " + port);
        return new ServerSocket(port);

    }

}