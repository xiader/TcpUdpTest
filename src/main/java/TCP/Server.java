package TCP; /**
 * Created by sasha on 17.10.2017.
 */
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
//start server first
    public static void main(String[] args) {

        ServerSocket socket = null;
        Socket clientAccepted = null;
        ObjectInputStream sois = null;
        ObjectOutputStream soos = null;

        try {
            System.out.println("TCP.Server is started...");
            socket = new ServerSocket(2525);
            clientAccepted = socket.accept();
            System.out.println("Connection established...");

            soos = new ObjectOutputStream(clientAccepted.getOutputStream());
            soos.flush();

            sois = new ObjectInputStream(clientAccepted.getInputStream());

            Object ms = sois.readObject();

            while (!"quite".equals(ms)) {

                double[][] msg = (double[][]) ms;

                Server.invert(msg);

                soos.writeObject(msg);
                soos.flush();

                ms = sois.readObject();
            }
        } catch (Exception ex) {
        } finally {
            try {

                if (sois != null) sois.close();
                if (soos != null) soos.close();
                if (socket != null) socket.close();
                if (clientAccepted != null) clientAccepted.close();

            } catch (Exception ex) {

            }
        }

    }

    public static final void invert(double matrix[][]) {
        int n = matrix.length;
        int row[] = new int[n];
        int col[] = new int[n];
        double temp[] = new double[n];
        int hold, I_pivot, J_pivot;
        double pivot, abs_pivot;

        if (matrix[0].length != n) {
            System.out.println("Error in Matrix.invert, inconsistent array sizes.");
        }
        // установиим row и column как вектор изменений.
        for (int k = 0; k < n; k++) {
            row[k] = k;
            col[k] = k;
        }
        // главный цикл
        for (int k = 0; k < n; k++) {
            // найдем наибольший элемент для основы
            pivot = matrix[row[k]][col[k]];
            I_pivot = k;
            J_pivot = k;
            for (int i = k; i < n; i++) {
                for (int j = k; j < n; j++) {
                    abs_pivot = Math.abs(pivot);
                    if (Math.abs(matrix[row[i]][col[j]]) > abs_pivot) {
                        I_pivot = i;
                        J_pivot = j;
                        pivot = matrix[row[i]][col[j]];
                    }
                }
            }
            if (Math.abs(pivot) < 1.0E-10) {
                System.out.println("Matrix is singular !");
                return;
            }
            //перестановка к-ой строки и к-ого столбца с стобцом и строкой, содержащий основной элемент(pivot основу)
            hold = row[k];
            row[k] = row[I_pivot];
            row[I_pivot] = hold;
            hold = col[k];
            col[k] = col[J_pivot];
            col[J_pivot] = hold;
            // k-ую строку с учетом перестановок делим на основной элемент
            matrix[row[k]][col[k]] = 1.0 / pivot;
            for (int j = 0; j < n; j++) {
                if (j != k) {
                    matrix[row[k]][col[j]] = matrix[row[k]][col[j]] * matrix[row[k]][col[k]];
                }
            }
            // внутренний цикл
            for (int i = 0; i < n; i++) {
                if (k != i) {
                    for (int j = 0; j < n; j++) {
                        if (k != j) {
                            matrix[row[i]][col[j]] = matrix[row[i]][col[j]] - matrix[row[i]][col[k]] *
                                    matrix[row[k]][col[j]];
                        }
                    }
                    matrix[row[i]][col[k]] = -matrix[row[i]][col[k]] * matrix[row[k]][col[k]];
                }
            }
        }
        // конец главного цикла

        // переставляем назад rows
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                temp[col[i]] = matrix[row[i]][j];
            }
            for (int i = 0; i < n; i++) {
                matrix[i][j] = temp[i];
            }
        }
        // переставляем назад columns
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                temp[row[j]] = matrix[i][col[j]];
            }
            for (int j = 0; j < n; j++) {
                matrix[i][j] = temp[j];
            }
        }
    }
}
