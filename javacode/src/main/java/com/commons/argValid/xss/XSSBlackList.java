package com.commons.argValid.xss;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author anzengli
 * @date 2019/4/12 09:47
 * @description xss 黑名单
 */
public class XSSBlackList {
    public Set<String> dangerWords;
    public Set<String> dangerPunctuations;
    public Map<String, String> replagePunctuations;

    public XSSBlackList() {
        dangerWords = new HashSet<String>();
        dangerPunctuations = new HashSet<String>();
        replagePunctuations = new HashMap<>();
    }

    public static XSSBlackList dangerWords() {
        return new XSSBlackList().addDangerWords("alert", "appendChild", "atob ", "base64_decode", "base64_encode",
                "btoa", "charAt", "charCodeAt", "cleardata", "confirm", "concat", "convert_uudecode",
                "convert_uuencode", "cookie", "createElement", "document", "docuemt.URL", "encodeURI",
                "encodeURIComponent", "escape", "eval", "exec", "explode", "form", "fromCharCode", "getdata", "href",
                "htmlspecialchars", "htmlspecialchars_decode", "iframe", "img", "implode", "innerHTML", "ipaddress",
                "join", "location", "prompt", "prototype", "removeRepeat", "replace", "request", "RTCPeerConnection",
                "script", "session", "setAttribute", "setdata", "slice", "split", "src", "strrev", "strstr",
                "substr", "toString", "trim", "urldecode", "urlencode", "vps", "WebSocket", "window")
                .addDangerWords("onabort", "onactivate", "onafterprint", "onafterupdate", "onbeforeactivate",
                        "onbeforecopy", "onbeforecut", "onbeforedeactivate", "onbeforeeditfocus", "onbeforepaste",
                        "onbeforeprint", "onbeforeunload", "onbeforeupdate", "onblur", "onbounce", "oncellchange",
                        "onclick", "oncontextmenu", "oncontrolselect", "oncopy", "oncut", "ondataavailable",
                        "ondatasetchanged", "ondatasetcomplete", "ondblclick", "ondeactivate", "ondrag", "ondragend",
                        "ondragenter", "ondragleave", "ondragover", "ondragstart", "ondrop", "onerror",
                        "onerrorupdate", "onfilterchange", "onfinish", "onfocus", "onfocusin", "onfocusout",
                        "onhelp", "onkeydown", "onkeypress", "onkeyup", "onlayoutcomplete", "onload",
                        "onlosecapture", "onmousedown", "onmouseenter", "onmouseleave", "onmousemove", "onmouseout",
                        "onmouseover", "onmouseup", "onmousewheel", "onmove", "onmoveend", "onmovestart", "onpaste",
                        "onpropertychange", "onreadystatechange", "onreset", "onresize", "onresizeend",
                        "onresizestart", "onrowenter", "onrowexit", "onrowsdelete", "onrowsinserted", "onscroll",
                        "onselect", "onselectionchange", "onselectstart", "onstart", "onstop", "onsubmit", "onunload");
    }

    public static XSSBlackList dangerPunctuations() {
        return new XSSBlackList().addDangerPunctuations("`");
    }

    public static XSSBlackList replagePunctuations() {
        Map<String, String> replace = new HashMap<>();
        replace.put("\\[", "【");
        replace.put("&#91;", "【");
        replace.put("&#x5b;", "【");
        replace.put("\\]", "】");
        replace.put("&#93;", "】");
        replace.put("&#x5d;", "】");
        replace.put("\\(", "（");
        replace.put("&#40;", "（");
        replace.put("&#x28;", "（");
        replace.put("\\)", "）");
        replace.put("&#41;", "）");
        replace.put("&#x29;", "）");
        return new XSSBlackList().addReplagePunctuations(replace);
    }

    public XSSBlackList addDangerWords(String... words) {
        if (dangerWords == null) {
            dangerWords = new HashSet<>();
        }
        for (String word : words) {
            dangerWords.add(word);
        }
        return this;
    }

    public XSSBlackList addDangerPunctuations(String... punctuations) {
        if (dangerPunctuations == null) {
            dangerPunctuations = new HashSet<>();
        }
        for (String punctuation : punctuations) {
            dangerPunctuations.add(punctuation);
        }
        return this;
    }

    public XSSBlackList addReplagePunctuations(Map<String, String> punctuations) {
        if (replagePunctuations == null) {
            replagePunctuations = new HashMap<>();
        }
        replagePunctuations.putAll(punctuations);
        return this;
    }

    public XSSBlackList removeDangerWords(String... words) {
        if (dangerWords != null) {
            for (String word : words) {
                dangerWords.remove(word);
            }
        }
        return this;
    }

    public XSSBlackList removeDangerPunctuations(String... punctuations) {
        if (replagePunctuations != null) {
            for (String key : punctuations) {
                replagePunctuations.remove(key);
            }
        }
        return this;
    }

    public XSSBlackList removeReplagePunctuations(String... punctuations) {
        if (dangerPunctuations == null) {
            dangerPunctuations = new HashSet<>();
        }
        for (String punctuation : punctuations) {
            dangerPunctuations.add(punctuation);
        }
        return this;
    }
}
