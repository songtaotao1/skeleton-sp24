package hashmap;

import org.apache.commons.lang3.StringUtils;

public class TestCommons {
    public static void main(String[] args) {
        String input = "   hello world   ";
        String result = StringUtils.strip(input); // 去除首尾空格
        System.out.println("结果: " + result);
    }
}
