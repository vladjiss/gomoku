import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GomokuClient extends UnicastRemoteObject implements IGomokuService, Runnable{
    private IGomokuService _server;
    private String _clientName;
    private GameForm game;
    private boolean first;
    private GomokuClient(IGomokuService server, String clientName) throws RemoteException {
        _server = server;
        _clientName = clientName;
        server.Connect(this, _clientName);
    }

    @Override
    public void run() {


        Scanner scanner = new Scanner(System. in);
        game = new GameForm(_server);
        if(first){
            game.lock = false;
            game.first = true;
            game.SetText("Игрок 1");
        }
        else
            game.SetText("Игрок 2");
        while(true){
            String inputString = scanner.nextLine();
            if(inputString == "exit") break;
        }
    }

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(4504);
            IGomokuService server = (IGomokuService) registry.lookup("GomokuServer");
            String clientName = "new client";
            System.out.println("\nConnecting to server...");

            new Thread(new GomokuClient(server, clientName)).start();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void Connect(IGomokuService service, String name) {}

    @Override
    public void SendMessageToClient(String message) throws RemoteException {
        System.out.println(message);
        ParseMessage(message);
    }

    private void ParseMessage(String message){
        String[] msg = message.split(" ");
        if(message.equals("First turn yours.")){
            first = true;
        }
        if(msg.length > 0 && msg[0].equals("step")){
            int row = Integer.parseInt(msg[2]);
            int col = Integer.parseInt(msg[3]);
            System.out.println("step "+row+" "+col);
            Color color;
            if(first)
                color = Color.BLACK;
            else
                color = Color.WHITE;
            game.addCircle(color, row, col);
            game.lock = false;
        }
        if(msg.length > 1 && msg[1].equals("win")){
            String player = msg[0];
            if(player.equals("first")){
                game.Alert("Первый игрок выиграл!");
            }
            else {
                game.Alert("Первый игрок выиграл!");
            }
        }
    }

}
