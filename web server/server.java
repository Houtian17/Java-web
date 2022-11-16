import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
    private static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    public static void main(String[] args) {
        run();
    }

    private static void run(){
        //监听请求
        //获取请求数据
        //发送响应数据
        int port = 9001;
        log(" http://localhost:%s",port);
        try(ServerSocket serverSocket = new ServerSocket(port)){
            try(Socket socket= serverSocket.accept()){
                log("client connection successful");
                //读取客户端请求数据
                String request= socketReadAll(socket);
                log("request：\n%s",request);

                String body = "<html><body>1234</body></html>";
                String length = String.format("Content-Length: %s", body.length());
                String response = "HTTP/1.1 200 very OK\r\n" +
                        "Content-Type: text/html;\r\n" +
                        length + "\r\n" +
                        "\r\n" +
                        body;
                socketSendAll(socket,response);
            }
        } catch (IOException e) {
            System.out.println("exception:"+e.getMessage());
        }
    }
    public static void socketSendAll(Socket socket,String request) throws IOException {
        OutputStream outputStream = socket.getOutputStream();//套路代码，通过outputSteam来发数据
        outputStream.write(request.getBytes());//socket底层传输的是二进制，所以要把请求转换成数字
    }

    public static String socketReadAll(Socket socket) throws IOException {
        InputStream inputStream= socket.getInputStream();
        InputStreamReader reader= new InputStreamReader(inputStream);

        int bufferSize = 1024;
        char[] data = new char[bufferSize];
        StringBuilder request = new StringBuilder();

        while(true){
            int size =reader.read(data,0,data.length);//size是真正读到的数据

            if (size>0){
                request.append(data,0,size);
            }
            log("size and data:" + size+"||"+data.length);

//            if (size <bufferSize){
//                break;
//            }

            if (!reader.ready()){
                break;
            }

        }

        return request.toString();
    }
}

