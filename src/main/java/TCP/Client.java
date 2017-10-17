package TCP; /**
 * Created by sasha on 17.10.2017.
 */
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    //start server first
    public static void main(String[] args) {

        try {
            System.out.println("Connecting...");
            Socket socket = new Socket("localhost", 2525);
            System.out.println("Connection established");
            ObjectOutputStream coos = new ObjectOutputStream(socket.getOutputStream());
            coos.flush();
            ObjectInputStream cois = new ObjectInputStream(socket.getInputStream());
            Scanner in = new Scanner(System.in);

            do {
                double arr[][] = new double[3][3];
                int z1, z2, z3, x1, x2, x3, c1, c2, c3, p1, p2, p3;
                System.out.print("Enter  z1:");
                z1 = in.nextInt();
                System.out.print("Enter  z2:");
                z2 = in.nextInt();
                System.out.print("Enter  z3:");
                z3 = in.nextInt();
                System.out.print("Enter  x1:");
                x1 = in.nextInt();
                System.out.print("Enter  x2:");
                x2 = in.nextInt();
                System.out.print("Enter  x3:");
                x3 = in.nextInt();
                System.out.print("Enter  c1:");
                c1 = in.nextInt();
                System.out.print("Enter  c2:");
                c2 = in.nextInt();
                System.out.print("Enter  c3:");
                c3 = in.nextInt();
                arr[0][0] = z1;
                arr[0][1] = z2;
                arr[0][2] = z3;
                arr[1][0] = x1;
                arr[1][1] = x2;
                arr[1][2] = x3;
                arr[2][0] = c1;
                arr[2][1] = c2;
                arr[2][2] = c3;

                coos.writeObject(arr);
                coos.flush();
                System.out.println("response: ");

                Client.print((double[][]) cois.readObject());

                System.out.println("repeat? (y or n)");
                in.nextLine();
            } while ("y".equals(in.nextLine()));

            coos.writeObject("quite");
            coos.flush();

        } catch (Exception ex) {
        }

    }

    public static final void print(double matrix[][]) {
        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            System.out.print("|  ");
            for (int j = 0; j < n; j++) {
                System.out.print(matrix[i][j] + "  ");
            }
            System.out.println("|");
        }
    }

}