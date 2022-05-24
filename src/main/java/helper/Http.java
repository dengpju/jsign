package helper;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;


public class Http {

    public String appid;
    public String secret;

    public Map<String, String> headers;

    public Http() {
        headers = new HashMap<String, String>();
    }

    /**
     * @return
     */
    public static String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    /**
     * 签名
     *
     * @param data
     * @return
     */
    public String sign(final Map<String, Object> data) {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[0]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyArray.length; i++) {
            sb.append(keyArray[i]).append("=").append(data.get(keyArray[i]));
            if (i < keyArray.length - 1) {
                sb.append("&");
            }
        }
        return md5(secret + sb.toString() + secret);
    }

    /**
     * md5
     *
     * @param str
     * @return
     */
    public static String md5(String str) {
        return DigestUtils.md5Hex(str);
    }

    /**
     * @param map
     * @return
     */
    public static String mapToJson(Map map) {
        JSONObject json = new JSONObject();
        Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            json.put(entry.getKey(), entry.getValue());
        }
        return json.toString();
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url        发送请求的URL
     * @param parameters 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public String get(String url, Map<String, Object> parameters) {
        String query = "";
        if (parameters != null) {
            Set<String> keySet = parameters.keySet();
            String[] keyArray = keySet.toArray(new String[0]);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < keyArray.length; i++) {
                sb.append(keyArray[i]).append("=").append(parameters.get(keyArray[i]));
                if (i < keyArray.length - 1) {
                    sb.append("&");
                }
            }
            if (!sb.toString().equals("")) {
                query = sb.toString();
                url += "?" + query;
            }
        }
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            Set<String> mapkeys = headers.keySet();
            for (String key : mapkeys) {
                connection.setRequestProperty(key, headers.get(key));
            }
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        } finally { // 使用finally块来关闭输入流
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送JSON方法的请求
     *
     * @param url        发送请求的 URL
     * @param parameters
     * @return
     */
    public String json(String url, Map<String, Object> parameters) {
        String json = "";
        System.out.println();
        if (parameters != null) {
            json = mapToJson(parameters);
        }
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            Set<String> mapkeys = headers.keySet();
            for (String key : mapkeys) {
                conn.setRequestProperty(key, headers.get(key));
            }
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(json);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        } finally { //使用finally块来关闭输出流、输入流
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url        发送请求的 URL
     * @param parameters
     * @return
     */
    public String post(String url, Map<String, Object> parameters) {
        String form = "";
        if (parameters != null) {
            Set<String> keySet = parameters.keySet();
            String[] keyArray = keySet.toArray(new String[0]);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < keyArray.length; i++) {
                sb.append(keyArray[i]).append("=").append(parameters.get(keyArray[i]));
                if (i < keyArray.length - 1) {
                    sb.append("&");
                }
            }
            if (!sb.toString().equals("")) {
                form = sb.toString();
            }
        }
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            Set<String> mapkeys = headers.keySet();
            for (String key : mapkeys) {
                conn.setRequestProperty(key, headers.get(key));
            }
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(form);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        } finally { //使用finally块来关闭输出流、输入流
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
