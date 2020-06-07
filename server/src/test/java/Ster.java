import org.revo.chat.server.message.DelimiterBasedMessageEncoder;
import org.revo.chat.server.message.Message;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Ster {
    public static void main(String[] args) throws IOException {


        Socket socket = new Socket("localhost", 9888);
        PrintWriter wtr = new PrintWriter(socket.getOutputStream());

        Message message = new Message();
        message.setPath("LOGIN");
        message.setPayload("fsdfas");
        message.setStatus("200");
        new DelimiterBasedMessageEncoder().apply(message).forEach(wtr::print);
        wtr.flush();


        message.setPath("ME");
        new DelimiterBasedMessageEncoder().apply(message).forEach(wtr::print);
        wtr.flush();


        Scanner scanner = new Scanner(socket.getInputStream());
        while (scanner.hasNext()) {
            System.out.println(scanner.nextLine());
        }







//        wtr.print("LOGIN\r\n");
//        wtr.print("\r\n");
//        wtr.print("OPTIONS\r\n");
//        wtr.print("SEND\r\n");
//        wtr.print("ME\r\n");
//        wtr.print("\r\n");
//        wtr.flush();


//        dataOutputStream.write("hellow wdfa".getBytes());
//        dataOutputStream.flush();
    }
}
