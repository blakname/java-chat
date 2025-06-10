package ahj;

import java.io.*;
import java.net.*;

public class SaveBinaryData {
    public static void main(String[] args) {
        URL u;
        URLConnection uc;
        for (int i = 0; i < args.length; i++) {
            try {
                u = new URL(args[i]);
                uc = u.openConnection();

                // Display all header fields and keys
                int n = 0;
                String headerKey;
                String headerValue;
                while ((headerKey = uc.getHeaderFieldKey(n)) != null) {
                    headerValue = uc.getHeaderField(n);
                    System.out.println(headerKey + ": " + headerValue);
                    n++;
                }

                // Get the content type
                String ct = uc.getContentType();
                int cl = uc.getContentLength();

                if (ct.startsWith("text/")) {
                    // Read and print text content
                    InputStream is = uc.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                    reader.close();
                } else if (cl != -1) { // Proceed if content length is known (assumes it's binary)
                    InputStream is = uc.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[cl];
                    int bytesRead = 0;
                    int offset = 0;

                    while (offset < cl) {
                        bytesRead = bis.read(buffer, offset, buffer.length - offset);
                        if (bytesRead == -1) break;
                        offset += bytesRead;
                    }
                    bis.close();

                    if (offset != cl) {
                        System.out.println("데이터를 정상적으로 읽지 않았습니다.");
                    } else {
                        // Ensure directory exists
                        File directory = new File("download12");
                        if (!directory.exists()) {
                            directory.mkdir();
                        }

                        // Get and modify the filename
                        String filename = u.getFile();
                        filename = filename.substring(filename.lastIndexOf('/') + 1);
                        File file = new File(directory, filename);

                        // Save data to file
                        FileOutputStream fout = new FileOutputStream(file);
                        fout.write(buffer);
                        fout.flush();
                        fout.close();
                    }
                }
            } catch (MalformedURLException e) {
                System.out.println("입력된 URL은 잘못된 URL 입니다.");
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}