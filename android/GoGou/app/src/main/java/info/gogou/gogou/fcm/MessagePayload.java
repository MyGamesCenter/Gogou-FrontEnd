package info.gogou.gogou.fcm;

/**
 * Created by Letian_Xu on 6/27/2016.
 */
public class MessagePayload {

    public static final String KEY_CATEGORY = "category";
    public static final String KEY_MESSAGE = "message";

    public enum Category {

        ORDER("order");

        private String value;

        private Category(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
