package dream.it.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 卖家精灵版本
 */
@Getter
@Setter
@EqualsAndHashCode
public class AsinEntity {

    @ExcelProperty("关键词")
    private String keyWords;

    @ExcelProperty("关键词翻译")
    private String keyWordCn;

    @ExcelProperty("流量占比")
    private String trafficPencent;

    @ExcelProperty("预估周曝光量")
    private String baoguang;

    @ExcelProperty("关键词类型")
    private String keyType;

    @ExcelProperty("转化效果")
    private String conversionEffect;

    @ExcelProperty("流量词类型")
    private String trafficKeywordType;

    @ExcelProperty("自然流量占比")
    private String orcPercent;

    @ExcelProperty("广告流量占比")
    private String guangGaoPercent;

    @ExcelProperty("自然排名")
    private String organicRanking;

    @ExcelProperty("自然排名页码")
    private String a1;
    @ExcelProperty("更新时间")
    private String a2;
    @ExcelProperty("广告排名")
    private String a3;
    @ExcelProperty("广告排名页码")
    private String a4;
    @ExcelProperty("广告更新时间")
    private String a5;
    @ExcelProperty(value = "ABA周排名")
    private String abaRank;
    @ExcelProperty(value = "月搜索量")
    private String mouthSearch;
    @ExcelProperty(value = "spr")
    private String a6;
    @ExcelProperty(value = "标题密度")
    private String a7;
    @ExcelProperty(value = "购买量")
    private String a8;
    @ExcelProperty(value = "购买率")
    private String a9;
    @ExcelProperty(value = "展示量")
    private String b1;
    @ExcelProperty(value = "点击量")
    private String b2;
    @ExcelProperty(value = "商品数")
    private String b3;
    @ExcelProperty(value = "需供比")
    private String b4;
    @ExcelProperty(value = "近7天广告竞品数")
    private String b5;
    @ExcelProperty(value = "点击总占比失效数据")
    private String b6;
    @ExcelProperty(value = "转化总占比失效数据")
    private String b7;
    @ExcelProperty(value = "PPC价格")
    private String b8;
    @ExcelProperty(value = "建议竞价格范围")
    private String b9;
    @ExcelProperty(value = "前十ASIN")
    private String b10;
}
