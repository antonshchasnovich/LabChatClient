import coders.MessageDecoder;
import coders.MessageEncoder;
import message.Message;


import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint(decoders = {MessageDecoder.class}, encoders = {MessageEncoder.class})
public class ChatClientEndpoint {
    private Session userSession;
    private Message message;

    ChatClientEndpoint(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(Session userSession) throws IOException, EncodeException {
        this.userSession = userSession;
        sendMessage(message);
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        this.userSession = null;
    }


    @OnMessage
    public void onMessage(Session session, Message message) {
        if(!message.getName().equals(this.message.getName())){
            System.out.println(message.getName() + ": " + message.getText());
        }
    }


    void sendMessage(Message message) throws IOException, EncodeException {
        this.message = message;
        this.userSession.getBasicRemote().sendObject(message);
    }

}
