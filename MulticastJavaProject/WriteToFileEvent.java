package ahj;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

class WriteToFileEvent extends Frame implements ActionListener {
    Label lfile, ldata;
    TextField tfile, tdata;
    Button save,close;
    String filename, data;
    byte buffer[] = new byte[80];

    public WriteToFileEvent(String str) {
        super(str);
        setLayout(new FlowLayout());
        lfile = new Label("파일이름을 입력하세요");
        add(lfile);
        tfile = new TextField(20);
        add(tfile);
        ldata = new Label("저장할 데이터를 입력하세요");
        add(ldata);
        tdata = new TextField(20);
        add(tdata);
        Button save = new Button("저장하기");
        save.addActionListener(this);
        add(save);
        Button close = new Button("닫기");
        close.addActionListener(this);
        add(close);
        addWindowListener(new WinListener());
    }
    

    public static void main(String args[]) {
        WriteToFileEvent text = new WriteToFileEvent("파일저장");
        text.setSize(270, 270);
        text.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if(ae.getActionCommand().equals("저장하기")) {
            filename = tfile.getText();
            data = tdata.getText();
            buffer = data.getBytes();
            try {
                FileOutputStream fout = new FileOutputStream(filename);
                fout.write(buffer);
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        } else if(ae.getActionCommand().equals("닫기")) {
            System.exit(0);
        }
    }
}

class WinListener extends WindowAdapter {
    public void windowClosing(WindowEvent we) {
        System.exit(0);
    }
}