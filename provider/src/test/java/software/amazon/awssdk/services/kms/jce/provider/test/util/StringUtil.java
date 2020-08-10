package software.amazon.awssdk.services.kms.jce.provider.test.util;

public abstract class StringUtil {

    public static String hex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(String.format("%02x", aByte));
        }
        return result.toString();
    }

}
