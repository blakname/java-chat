package ahj;

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class MulticastReceiver implements Runnable, WindowListener {
    protected InetAddress group;
    protected int port;
    protected Frame frame;
    protected TextArea output;
    protected Thread listener;
    protected MulticastSocket socket;
    protected DatagramPacket incoming;

    public MulticastReceiver(InetAddress group, int port) throws IOException {
        this.group = group;
        this.port = port;
        initAWT();
        initNet();
        start();
    }

    protected void initAWT() {
        frame = new Frame("멀티캐스트 수신자 [호스트: " + group.getHostAddress() + " 포트: " + port + "]");
        frame.addWindowListener(this);
        output = new TextArea();
        output.setEditable(false);
        frame.setLayout(new BorderLayout());
        frame.add(output, "Center");
        frame.pack();
        frame.setVisible(true);
    }

    protected void initNet() throws IOException {
        socket = new MulticastSocket(port);
        socket.joinGroup(group);
        incoming = new DatagramPacket(new byte[65508], 65508);
    }

    public synchronized void start() {
        if (listener == null) {
            listener = new Thread(this);
            listener.start();
        }
    }

    public synchronized void stop() throws IOException {
        frame.setVisible(false);
        if (listener != null) {
            listener.interrupt();
            listener = null;
        }
        socket.leaveGroup(group);
        socket.close();
        System.exit(0);
    }

    public void windowOpened(WindowEvent we) {}
    public void windowClosing(WindowEvent we) {
        try {
            stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void windowClosed(WindowEvent we) {}
    public void windowIconified(WindowEvent we) {}
    public void windowDeiconified(WindowEvent we) {}
    public void windowActivated(WindowEvent we) {}
    public void windowDeactivated(WindowEvent we) {}

    public void run() {
        try {
            while (!Thread.interrupted()) {
                incoming.setLength(incoming.getData().length);
                socket.receive(incoming);
                String message = new String(incoming.getData(), 0, incoming.getLength(), "UTF8");
                output.append(message + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1 || args[0].indexOf(":") < 0) {
            throw new IllegalArgumentException("Usage: java MulticastReceiver <multicast_address:port>");
        }
        int idx = args[0].indexOf(":");
        InetAddress group = InetAddress.getByName(args[0].substring(0, idx));
        int port = Integer.parseInt(args[0].substring(idx + 1));
        new MulticastReceiver(group, port);
    }
}
