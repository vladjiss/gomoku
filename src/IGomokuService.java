import java.rmi.*;

public interface IGomokuService extends Remote {
    void Connect(IGomokuService service, String name) throws RemoteException;
    void SendMessageToClient(String message) throws RemoteException;
}