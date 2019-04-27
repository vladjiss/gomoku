import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class GomokuServer extends UnicastRemoteObject implements IGomokuService {
    private ArrayList<IGomokuService> _clients = new ArrayList<>();
    private String _currentPlayer;
    private static int[][] data = new int[19][19];

    public GomokuServer() throws RemoteException{}

    @Override
    public void Connect(IGomokuService client, String name) throws RemoteException{
        _clients.add(client);

        System.out.println("\n"+name+" connected.");
        client.SendMessageToClient("You was connected.");

        if(_clients.size() == 1){
            client.SendMessageToClient("First turn yours.");
            _currentPlayer = name;
        }
    }

    @Override
    public void SendMessageToClient(String message) throws RemoteException {
        System.out.println(message);
        ParseMessage(message);
    }

    private void ParseMessage(String message){
        String[] msg = message.split(" ");
        if(msg.length > 0 && msg[0].equals("step")){
            boolean player1 = Boolean.parseBoolean(msg[1]);
            int row = Integer.parseInt(msg[2]);
            int col = Integer.parseInt(msg[3]);
            System.out.println("step "+row+" "+col);
            if(player1){
                data[row][col] = 1;
                try {
                    _clients.get(1).SendMessageToClient(message);
                }
                catch (Exception e){

                }
            }
            else {

                data[row][col] = 2;
                try {
                    _clients.get(0).SendMessageToClient(message);
                }
                catch (Exception e){

                }
            }
            Check();
        }
    }

    private void Check(){

        for(int i = 0; i < 19; i++){
            int counter1 = 0;
            int counter2 = 0;
            for(int j = 0; j < 19; j++){
                if(data[i][j] == 1){
                    counter1++;
                    counter2 = 0;
                }
                if(data[i][j] == 2){
                    counter2++;
                    counter1 = 0;
                }
                if(counter1 >=5){
                    try {
                        _clients.get(1).SendMessageToClient("first win");
                        _clients.get(2).SendMessageToClient("first win");
                    }
                    catch (Exception e){

                    }
                    break;
                }
                if(counter2 >=5){
                    try {
                        _clients.get(1).SendMessageToClient("second win");
                        _clients.get(2).SendMessageToClient("second win");
                    }
                    catch (Exception e){

                    }
                    break;
                }
            }
        }
        int i,j;
        for(j = 0; j < 19; j++){
            int counter1 = 0;
            int counter2 = 0;
            for(i = 0; i < 19; i++){
                if(data[i][j] == 1){
                    counter1++;
                    counter2 = 0;
                }
                if(data[i][j] == 2){
                    counter2++;
                    counter1 = 0;
                }
                if(data[i][j] == 0){
                    counter2 = 0;
                    counter1 = 0;
                }
                if(counter1 >=5){
                    try {
                        _clients.get(1).SendMessageToClient("first win");
                        _clients.get(2).SendMessageToClient("first win");
                    }
                    catch (Exception e){

                    }
                    break;
                }
                if(counter2 >=5){
                    try {
                        _clients.get(1).SendMessageToClient("second win");
                        _clients.get(2).SendMessageToClient("second win");
                    }
                    catch (Exception e){

                    }
                    break;
                }
            }
        }
    }

    public static void main(String args[]) {
        final String RMI_ADDRESS = "java.rmi.server.hostname";
        final String ADDRESS = "127.0.0.1";
        final int PORT = 4504;

        for(int i = 0; i < 19; i++){
            for(int j = 0; j < 19; j++){
                data[i][j] = 0;
            }
        }

        try {
            System.setProperty(RMI_ADDRESS, ADDRESS);
            IGomokuService server = new GomokuServer();
            Registry registry = LocateRegistry.createRegistry(PORT);
            registry.bind("GomokuServer", server);

            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

}