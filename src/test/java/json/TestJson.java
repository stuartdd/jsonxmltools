package json;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author stuartdd
 */
public class TestJson {

    private static final String TEST_JSON = "{\"a\":[1,2,{\"b\":true},3],\"c\":3}";
    private static final String TEST_JSON_FMT = "{\n"
            + "  \"a\" : [ 1, 2, {\n"
            + "    \"b\" : true\n"
            + "  }, 3 ],\n"
            + "  \"c\" : 3\n"
            + "}";

    @Test
    public void testToJsonNull() {
        MyBeanString myBean = new MyBeanString();
        String myBeanJson = JsonUtils.toJson(myBean);
        Assert.assertEquals("{\"a\":null,\"c\":null}", myBeanJson);

        String myBeanJsonMin = JsonUtils.toJsonMin(myBean);
        Assert.assertEquals("{}", myBeanJsonMin);

        myBean.setC("CCC");
        myBeanJsonMin = JsonUtils.toJsonMin(myBean);
        Assert.assertEquals("{\"c\":\"CCC\"}", myBeanJsonMin);
    }

    @Test
    public void testToJsonStream() throws IOException {
        MyBean myBean = (MyBean) JsonUtils.beanFromJson(MyBean.class, MyBean.class.getResource("/TestConfig.json").openStream());
        String myBeanJson = JsonUtils.toJson(myBean);
        Assert.assertEquals(TEST_JSON, myBeanJson);
    }

    @Test
    public void testToJsonFile() {
        MyBean myBean = (MyBean) JsonUtils.beanFromJson(MyBean.class, new File("testData/TestConfig.json"));
        String myBeanJson = JsonUtils.toJson(myBean);
        Assert.assertEquals(TEST_JSON, myBeanJson);
    }

    @Test
    public void testToJson() {
        MyBean myBean = (MyBean) JsonUtils.beanFromJson(MyBean.class, TEST_JSON);
        String myBeanJson = JsonUtils.toJson(myBean);
        Assert.assertEquals(TEST_JSON, myBeanJson);

        String myBeanJsonFormatted = JsonUtils.toJsonFormatted(myBean);
        Assert.assertEquals(str(TEST_JSON_FMT), str(myBeanJsonFormatted));
    }

    @Test(expected = JsonToObjectException.class)
    public void testInvalidPathJsonFileBean() {
        JsonUtils.beanFromJson(MyBean.class, new File("testData"));
    }

    @Test(expected = JsonToObjectException.class)
    public void testFileNotFoundJsonFileBean() {
        JsonUtils.beanFromJson(MyBean.class, new File("testData/notFound"));
    }

    @Test(expected = JsonToObjectException.class)
    public void testFileEmptyJsonFileBean() {
        JsonUtils.beanFromJson(MyBean.class, new File("testData/TestConfigEmpty.json"));
    }

    @Test(expected = JsonToObjectException.class)
    public void testFileInvalidJsonFileBean() {
        JsonUtils.beanFromJson(MyBean.class, new File("testData/TestConfigInvalid.json"));
    }

    @Test(expected = JsonToObjectException.class)
    public void testFileNull() {
        JsonUtils.beanFromJson(MyBean.class, (File) null);
    }

    @Test(expected = JsonToObjectException.class)
    public void testStreamNull() {
        JsonUtils.beanFromJson(MyBean.class, (InputStream) null);
    }

    @Test(expected = JsonToObjectException.class)
    public void testStringNull() {
        JsonUtils.beanFromJson(MyBean.class, (String) null);
    }

//    @Test(expected = JsonToObjectException.class)
//    public void testMapStringNull() {
//        JsonUtils.mapFromJson((String) null);
//    }
//
//    @Test(expected = JsonToObjectException.class)
//    public void testMapFileNull() {
//        JsonUtils.mapFromJson((File) null);
//    }
//    
//    @Test(expected = JsonToObjectException.class)
//    public void testMapStreamNull() {
//        JsonUtils.mapFromJson((InputStream) null);
//    }
    @Test
    public void testJsonMap() {
        Map<String, Object> map = JsonUtils.mapFromJson(TEST_JSON);

        ArrayList a = (ArrayList) map.get("a");
        for (Object o : a) {
            System.out.println(o.getClass().getName() + " " + o);
        }
        assertInt(a.get(0), 1);
        assertInt(a.get(1), 2);
        assertMap(a.get(2), "{b=true}");
        assertInt(a.get(3), 3);

    }

    private void assertInt(Object o, Integer i) {
        Assert.assertTrue(o instanceof Integer);
        Assert.assertEquals(i, o);
    }

    private void assertMap(Object o, String like) {
        Assert.assertTrue(o instanceof Map);
        Assert.assertEquals(like, o.toString());
    }

    private static class MyBean {

        private List a;
        private int c;

        public List getA() {
            return a;
        }

        public void setA(List a) {
            this.a = a;
        }

        public int getC() {
            return c;
        }

        public void setC(int c) {
            this.c = c;
        }

    }

    private static class MyBeanString {

        private List a;
        private String c;

        public List getA() {
            return a;
        }

        public void setA(List a) {
            this.a = a;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

    }

    private String str(String string) {
        StringBuilder sb = new StringBuilder();
        for (char c:string.toCharArray()) {
            if (c >= ' ') {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
