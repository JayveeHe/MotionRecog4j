package Utils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import net.dongliu.requests.Requests;
import net.dongliu.requests.Response;
import org.apache.http.client.HttpClient;

import java.net.URLEncoder;

/**
 * Created by jayvee on 15/10/9.
 */
public class LeancloudUtils {
    public static void getRemoteData(String ClassName, String conditions, int limit) {
        String url = "https://api.leancloud.cn/1.1/classes/" + ClassName + "?where=";
        url += URLEncoder.encode(conditions);
        url += "&limit=" + limit;
        Response<String> resp = Requests.get(url)
                .addHeader("X-LC-Id", "9ra69chz8rbbl77mlplnl4l2pxyaclm612khhytztl8b1f9o")
                .addHeader("X-LC-Key", "1zohz2ihxp9dhqamhfpeaer8nh1ewqd9uephe9ztvkka544b")
                .addHeader("Content-Type", "application/json").text();
        System.out.println(resp.getBody());
    }

    public static void main(String args[]) {
        getRemoteData("Log", "{\"type\":\"accSensor\"}", 2);
    }
}
