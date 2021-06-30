package io.patamon.geocoding.controll;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.patamon.geocoding.Geocoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author yunpeng.gu
 * @date 2021/6/4 15:04
 * @Email:yunpeng.gu@percent.cn
 */
@RestController
public class GeocodingControll {

    private static final Logger log = LoggerFactory.getLogger(GeocodingControll.class);

    @GetMapping("/geocoding/normalizing")
    public JSON normalizingWithResult(@RequestParam("addr")String addr){
        log.info("解析：{}",addr);
        return JSONUtil.parse(Geocoding.normalizing(addr));
    }

    @GetMapping("/geocoding/similarity")
    public JSON similarityWithResult(@RequestParam("addr1")String addr1, @RequestParam("addr2")String addr2){
        JSONObject similarityScore = JSONUtil.createObj().putOnce("score", Geocoding.similarity(addr1, addr2));
        log.info("{}和{}相似度为：{}",addr1,addr2,similarityScore);
        return similarityScore;
    }

}
