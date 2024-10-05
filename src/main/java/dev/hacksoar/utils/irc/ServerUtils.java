package dev.hacksoar.utils.irc;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dev.hacksoar.modules.impl.utilty.IRC;
import dev.hacksoar.utils.Logger;
import dev.hacksoar.utils.PlayerUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bson.Document;

public class ServerUtils {
    public static ConnectionString connectionString = null;
    public static MongoClientSettings settings = null;
    public static MongoClient mongoClient = null;
    public static MongoCollection<Document> messagesCollection = null;

    public static String messageRoom = "Room-1";

    public static void ConnectServer() {
        try {
            connectionString = new ConnectionString("mongodb+srv://Homo:Homo114514Fuckyou@homoccc.nvr1n7n.mongodb.net/?retryWrites=true&w=majority");
            settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .serverApi(ServerApi.builder()
                            .version(ServerApiVersion.V1)
                            .build())
                    .build();
            mongoClient = MongoClients.create(settings);

            MongoDatabase database = mongoClient.getDatabase("message");
            database.runCommand(new Document("ping", 1));
            messagesCollection = database.getCollection("1");
            messageRoom = "1";
            Logger.log("Pinged your deployment. You successfully connected to MongoDB!");
            PlayerUtils.tellPlayerIrc("We successfully connected to server.");
            IRC.setServerStatus(true);
        } catch (Exception e){
            IRC.setServerStatus(false);
        }
    }

    /**
     * Add message to IRC sendMessages
     * then send to server
     */
    public static void sendMessage(String string) {
        IRC.sendMessages.add(string);
    }

    public static long getTime() {
        HttpClient client = HttpClients.createDefault();
        String url = "https://cube.meituan.com/ipromotion/cube/toc/component/base/getServerCurrentTime";
        HttpGet httpGet=new HttpGet(url);
        JSONObject jsonObject = null;
        try {
            HttpResponse res = client.execute(httpGet);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 返回json格式：
                jsonObject = JSONObject.parseObject(EntityUtils.toString(res.getEntity()));
                // System.out.println(jsonObject);
            }
        } catch (Exception e) {
            Logger.error("Oops, cant get meituan time api");
        }
        if (jsonObject != null) {
            return jsonObject.getLongValue("data");
        }
        return -1;
    }
}
