import helper.Http;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println(11);
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("ttt", "ggg");
        parameters.put("ttt1", "ggg1");
        try {
            Http http = new Http();
            http.appid = "3453121877";
            http.secret = "priRip7DvDv4zy2Y1ODzhdlHUzg6y4qH";
            http.headers.put("appid", http.appid);
            String timestamp = Http.getTimeStamp();
            http.headers.put("timestamp", timestamp);
            Map<String, Object> data = (Map<String, Object>) ((HashMap<String, Object>) parameters).clone();
            data.put("appid", http.appid);
            data.put("timestamp", timestamp);
            String sign = http.sign(data);
            System.out.println(sign);
            http.headers.put("sign", sign);

            System.out.println(http.headers.get("timestamp"));
            String s = http.post("http://tianli-cloud-v2.yumi.com/cloud/api/school/list", parameters);
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
