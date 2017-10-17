package UDP;//import java.net.*;
//import java.io.*;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {
    //start server first
    public static final String FILE_NAME = "file.txt";

    public final static int DEFAULT_PORT = 8001;
    public final String VERSION_CMD = "VERS";//определение версии команды
    public final String EVALUATE_CMD = "EVAL";
    public final String QUIT_CMD = "QUIT";//определение команды «выход»
    public final byte[] VERSION = {'V', '2', '.', '0'};//создание массива для определения версии сервера
    public final byte[] UNKNOWN_CMD = {'U', 'n', 'k', 'n', 'o', 'w', 'n', ' ',
            'c', 'o', 'm', 'm', 'a', 'n', 'd'};//неизвестная команда

    public void runServer() throws IOException {//????? ??????? runServer
        DatagramSocket s = null;
        try {
            boolean stopFlag = false;
            byte[] buf = new byte[512];//неизвестная команда
            s = new DatagramSocket(DEFAULT_PORT);//привязка сокета к реальному объекту с портом DEFAULT_PORT
            System.out.println("UDP.UDPServer: Started on " + s.getLocalAddress() + ":"
                    + s.getLocalPort());
            while (!stopFlag) {//???? ?? ??? ???, ???? ???? ?? ?????? ???????? true
                DatagramPacket recvPacket = new DatagramPacket(buf, buf.length);//создание объекта дейтаграммы для получения данных
                s.receive(recvPacket);//помещение полученного содержимого в объект дейтаграммы

                byte[] command = new byte[4];
                System.arraycopy(recvPacket.getData(), 0, command, 0, 4);
                if (EVALUATE_CMD.equals(new String(command))) {
                    byte[] xbyte = new byte[10];
                    byte[] ybyte = new byte[10];
                    byte[] zbyte = new byte[10];

                    System.arraycopy(recvPacket.getData(), 4, xbyte, 0, 10);
                    System.arraycopy(recvPacket.getData(), 14, ybyte, 0, 10);
                    System.arraycopy(recvPacket.getData(), 24, zbyte, 0, 10);

                    int x = new Integer(new String(xbyte).trim());
                    int y = new Integer(new String(ybyte).trim());
                    int z = new Integer(new String(zbyte).trim());

                    double result = eval(x, y, z);

                    System.out.println("UDP.UDPServer: Command: " + new String(command));
                    System.out.println("UDP.UDPServer: Evaluating: " + "sigma(" + x + ", " + y + ", " + z + ") = " + result);

                    write("sigma(" + x + ", " + y + ", " + z + ") = " + result);

                    DatagramPacket sendResult = new DatagramPacket(Double.toString(result).getBytes(), Double.toString(result).getBytes().length, recvPacket.getAddress(), recvPacket.getPort());

                    s.send(sendResult);


                } else {

                    String cmd = new String(recvPacket.getData()).substring(0, recvPacket.getLength()).trim();//?????????? ??????? ?? ??????
                    System.out.println("UDP.UDPServer: Command: " + cmd);
                    DatagramPacket sendPacket = new DatagramPacket(buf, 0, recvPacket.getAddress(), recvPacket.getPort());//???????????? ???????                                                                  // ??????????? ??? ??????? ??????
                    int n = 0;//количество байт в ответе
                    if (cmd.equals(VERSION_CMD)) {//проверка версии команды
                        n = VERSION.length;
                        System.arraycopy(VERSION, 0, buf, 0, n);
                    } else if (cmd.equals(QUIT_CMD)) {
                        stopFlag = true;//остановка сервера
                        continue;
                    } else {
                        n = UNKNOWN_CMD.length;
                        System.arraycopy(UNKNOWN_CMD, 0, buf, 0, n);
                    }
                    sendPacket.setData(buf);//установить массив посылаемых данных
                    sendPacket.setLength(n);//установить длину посылаемых данных
                    s.send(sendPacket);//послать сами данные
                }
            } // while(server is not stopped)
            System.out.println("UDP.UDPServer: Stopped");
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }

    private double eval(int x, int y, int z) {
        double res;

       
res = ((2 * Math.cos(x - Math.PI / 6)) / (Math.exp(1 / 2) + Math.pow(Math.sin(y), 2))) * (1 + z * z / (3 - Math.pow(z, 5) / 5));
        return res;
    }

    private void write(String txt) {
        FileWriter sw = null;
        try {
            sw = new FileWriter(FILE_NAME, true);

            sw.write(txt + "\n");

            sw.close();

        } catch (IOException e) {
            System.out.println("Error: can't to write to file " + FILE_NAME);
        }
    }

    public static void main(String[] args) {
        try {
            UDPServer udpSvr = new UDPServer();
            udpSvr.runServer();//вызов
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
