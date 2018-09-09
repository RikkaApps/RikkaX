package moe.shizuku.billing;


import android.content.Context;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.StringDef;
import android.support.annotation.WorkerThread;
import android.util.JsonReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Retention;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class PaymentClient {

    /** Possible response codes. */
    @IntDef({
            PurchaseResponse.INVALID_RETURN_DATA,
            PurchaseResponse.CONNECTION_ISSUE,
            PurchaseResponse.OK,
            PurchaseResponse.INVALID_REDEEM_CODE
    })
    @Retention(SOURCE)
    public @interface PurchaseResponse {
        /** Return data is invalid */
        int INVALID_RETURN_DATA = -2;
        /** Any issue about connection*/
        int CONNECTION_ISSUE = -1;
        /** Success */
        int OK = 0;
        /** Redeem code invalid (wrong product, refunded) */
        int INVALID_REDEEM_CODE = 1;
    }

    /** Possible response messages. */
    @StringDef({
            PurchaseMessage.PRODUCT_NOT_EXIST,
            PurchaseMessage.CODE_NOT_FOUND,
            PurchaseMessage.CODE_IS_REFUNDED,
            PurchaseMessage.CODE_USED_IN_OTHER_PRODUCT,
            PurchaseMessage.INVALID_DEVICE,
            PurchaseMessage.OK,
            PurchaseMessage.NEW_DEVICE,
            PurchaseMessage.CHANGE_DEVICE,
    })
    @Retention(SOURCE)
    public @interface PurchaseMessage {
        String PRODUCT_NOT_EXIST = "pne";
        String CODE_NOT_FOUND = "nfd";
        String CODE_IS_REFUNDED = "rd";
        String CODE_USED_IN_OTHER_PRODUCT = "ud";
        String INVALID_DEVICE = "bod";
        String OK = "ok";
        String NEW_DEVICE = "bd";
        String CHANGE_DEVICE = "bnd";
    }

    private static String toString(InputStream is) {
        try {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString("UTF-8");
        } catch (IOException ignored) {
            return null;
        }
    }

    public static class Result {

        private final @PurchaseResponse int result;
        private final @PurchaseMessage String message;

        public Result(@PurchaseResponse int result) {
            this(result, null);
        }

        public Result(@PurchaseResponse int result, @PurchaseMessage String message) {
            this.result = result;
            this.message = message;
        }

        public String getMessage(Context context) {
            switch (result) {
                case PurchaseResponse.CONNECTION_ISSUE:
                case PurchaseResponse.INVALID_RETURN_DATA:
                    return context.getString(R.string.billing_failed_connection_issue);
                case PurchaseResponse.OK: {
                    if (message.startsWith(PurchaseMessage.NEW_DEVICE)) {
                        String device = message.replaceFirst(PurchaseMessage.NEW_DEVICE, "").trim();
                        return context.getString(R.string.billing_success_new_device, device);
                    }
                    if (message.startsWith(PurchaseMessage.CHANGE_DEVICE)) {
                        String device = message.replaceFirst(PurchaseMessage.CHANGE_DEVICE, "").trim();
                        return context.getString(R.string.billing_success_change_device, device);
                    }
                    break;
                }
                case PurchaseResponse.INVALID_REDEEM_CODE:
                    return context.getString(R.string.billing_failed_invalid);
            }
            return message;
        }

        public boolean verified() {
            return result == PurchaseResponse.OK;
        }

        public boolean unverified() {
            return result == PurchaseResponse.INVALID_REDEEM_CODE;
        }
    }

    private static Result post(String url, Map<String, String> parameters) throws IOException {
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, String> param : parameters.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }

        byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(10000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        conn.connect();

        int code = conn.getResponseCode();
        if (code == 200) {
            try {
                int result = Integer.MIN_VALUE;
                String message = null;

                JsonReader reader = new JsonReader(new InputStreamReader(conn.getInputStream()));
                reader.beginObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();
                    if ("result".equals(name)) {
                        result = reader.nextInt();
                    } else if ("message".equals(name)) {
                        message = reader.nextString();
                    }
                }
                if (result == Integer.MIN_VALUE) {
                    return new Result(PurchaseResponse.INVALID_RETURN_DATA);
                }
                return new Result(result, message);
            } catch (Exception e) {
                return new Result(PurchaseResponse.INVALID_RETURN_DATA);
            }
        } else if (code < 400) {
            return new Result(PurchaseResponse.INVALID_RETURN_DATA);
        } else {
            return new Result(PurchaseResponse.INVALID_RETURN_DATA);
        }
    }

    public static class Redeem {

        private static String registerUrl;
        private static String verifyUrl;
        private static String device;
        private static String product;

        public static void setRegisterUrl(String url) {
            Redeem.registerUrl = url;
        }

        public static void setVerifyUrl(String url) {
            Redeem.verifyUrl = url;
        }

        public static void setDevice(String device) {
            Redeem.device = device;
        }

        public static void setProduct(String product) {
            Redeem.product = product;
        }

        @WorkerThread
        public static Result register(String code) {
            Map<String, String> parameterList = new HashMap<>();
            parameterList.put("product", product);
            parameterList.put("tradeNo", code);
            parameterList.put("device", device);
            parameterList.put("deviceName", Build.MODEL);

            try {
                return post(registerUrl, parameterList);
            } catch (IOException e) {
                e.printStackTrace();

                return new Result(PurchaseResponse.CONNECTION_ISSUE);
            }
        }

        @WorkerThread
        public static Result verify(String code) {
            Map<String, String> parameterList = new HashMap<>();
            parameterList.put("product", product);
            parameterList.put("tradeNo", code);
            parameterList.put("device", device);
            parameterList.put("deviceName", Build.MODEL);

            try {
                return post(verifyUrl, parameterList);
            } catch (IOException e) {
                e.printStackTrace();

                return new Result(PurchaseResponse.CONNECTION_ISSUE);
            }
        }
    }
}