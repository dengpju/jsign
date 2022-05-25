import helper.Http;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println(11);
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("schoolId", 1438);
        try {
            Http http = new Http();
            http.appid = "3453121877";
            http.secret = "priRip7DvDv4zy2Y1ODzhdlHUzg6y4qH";
            http.headers.put("appid", http.appid);
            String timestamp = Http.getTimeStamp();
            http.headers.put("timestamp", timestamp);
            http.headers.put("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2FjY291bnQiOiJRODg4ODg4ODgiLCJyYW5kb20iOiJLZUV0a2xwTyJ9.wJCdC4eAoKkd-dVEymXX0EXLkBXY_HwRBR0Kih9PC6Y");
            Map<String, Object> data = (Map<String, Object>) ((HashMap<String, Object>) parameters).clone();
            data.put("appid", http.appid);
            data.put("timestamp", timestamp);
            String sign = http.sign(data);
            System.out.println(sign);
            http.headers.put("sign", sign);

            System.out.println(http.headers.get("timestamp"));
            String s = http.get("http://tianli-cloud-v2.yumi.com/cloud/api/external/grade", parameters);
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
