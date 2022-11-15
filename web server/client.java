import javax.naming.ldap.SortKey;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;


public class client {
    private static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    public static void main(String[] args) {
        final String host = "douban.com";
        final int port = 80;
        final String path = "/";

        String request = String.format("GET %s HTTP/1.1\r\nHost:%s\r\n\r\n", path, host);
        log("request:\n%s", request);

        try (Socket socket = new Socket(host, port)) { //程序往对方发出去，要有ip和端口号，自己的端口和ip程序会自动分配。
            //发送http请求给服务器
            socketSendAll(socket,request);

            //接收服务器的响应数据
            String response = socketReadAll(socket);

            log("response:\n%s", response);
        } catch (Exception e) {
            log("error:" + e.getMessage());
        }
    }

    public static void socketSendAll(Socket socket,String request) throws IOException {
        OutputStream outputStream = socket.getOutputStream();//套路代码，通过outputSteam来发数据
        outputStream.write(request.getBytes());//socket底层传输的是二进制，所以要把请求转换成数字
    }

    public static String socketReadAll(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);//使用 inputSteam 读数据不是很方便，所以用reader包装了一层

        //指定读取的数据长度为1024
        int bufferSize = 1024;
        //初始化指定长度的数组
        char[] data = new char[bufferSize];
        //StringBuilder字符串的拼接效率比String高,String Builder的底层是一个数组
        StringBuilder response = new StringBuilder();
        //read函数会把读到的数据复制到data数组中去
        int size = reader.read(data, 0, data.length);
        //append方法会把data数组中的数据转换成字符串
        response.append(data, 0, size);

        return response.toString();
    }
}
