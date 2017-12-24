public class SimpleChatClientTest {
    public static void main(String args[]) {
        SimpleChatServerTest server1 = new SimpleChatServerTest();
        SimpleChatClient client1 = new SimpleChatClient();
        server1.go();
        client1.start();

    }
}