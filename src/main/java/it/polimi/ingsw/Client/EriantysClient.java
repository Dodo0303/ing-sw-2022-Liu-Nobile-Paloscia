package it.polimi.ingsw.Client;

public class EriantysClient implements Runnable{
    //TODO
    private ServerHandler serverHandler;


    public void main() {
        EriantysClient client = new EriantysClient();
        client.run();
    }

    public ServerHandler getServerHandler()
    {
        return this.serverHandler;
    }

    @Override
    public void run() {

    }
}
