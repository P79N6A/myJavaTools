package spring.commons;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ConfigFileParser implements ResourceLoaderAware {
    private static Logger log = LoggerFactory.getLogger(ConfigFileParser.class);

    private static ResourceLoader resourceLoader;
    private static List<Long> usersForEn;

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public static List<Long> getUsersForEn(){
        String filename = "WEB-INF/classes/usersForEn.txt";
        if(usersForEn == null){
            Resource resource = resourceLoader.getResource(filename);
            try {
                List<Long> cache = new ArrayList<Long>();
                File file = resource.getFile();
                List<String> list = FileUtils.readLines(file, "utf8");
                for(String l:list){
                    l = l.trim();
                    if(StringUtils.isBlank(l)) continue;
                    if(l.startsWith("#")) continue;
                    long userid = Long.parseLong(l);
                    cache.add(userid);
                }
                log.info("init parse file:" + filename + ",size:" + cache.size());
                usersForEn = cache;
            } catch (Throwable e) {
                log.error("" + filename, e);
            }
        }
        return usersForEn;
    }

}