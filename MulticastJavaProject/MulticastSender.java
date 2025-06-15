package ahj;

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class MulticastSender implements ActionListener, WindowListener {
    protected InetAddress group;
    protected int port;
    protected Frame frame;
    protected TextField input;
    protected MulticastSocket socket;
    protected DatagramPacket outgoing;

    public MulticastSender(InetAddress group, int port) throws IOException {
        this.group = group;
        this.port = port;
        initAWT();
        initNet();
    }

    protected void initAWT() {
        frame = new Frame("멀티캐스트 송신자 [호스트: " + group.getHostAddress() + " 포트: " + port + "]");
        frame.addWindowListener(this);
        input = new TextField();
        input.addActionListener(this);
        frame.setLayout(new BorderLayout());
        frame.add(input, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }

    protected void initNet() throws IOException {
        socket = new MulticastSocket();
        socket.setTimeToLive(1);
        outgoing = new DatagramPacket(new byte[1], 1, group, port);
    }

    public synchronized void stop() throws IOException {
        frame.setVisible(false);
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

    public void actionPerformed(ActionEvent ae) {
        try {
            byte[] utf = ae.getActionCommand().getBytes("UTF8");
            outgoing.setData(utf);
            outgoing.setLength(utf.length);
            socket.send(outgoing);
            input.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1 || args[0].indexOf(":") < 0) {
            throw new IllegalArgumentException("Usage: java MulticastSender <multicast_address:port>");
        }
        int idx = args[0].indexOf(":");
        InetAddress group = InetAddress.getByName(args[0].substring(0, idx));
        int port = Integer.parseInt(args[0].substring(idx + 1));
        new MulticastSender(group, port);
    }
}
