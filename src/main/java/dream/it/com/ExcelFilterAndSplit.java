package dream.it.com;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import dream.it.pojo.AsinEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ExcelFilterAndSplit {

    // 存储不含关键词的数据（原数据sheet）
    private static final List<AsinEntity> originalData = new ArrayList<>();
    // 存储含关键词的数据（第二个sheet）
    private static final List<AsinEntity> keywordData = new ArrayList<>();
    // 多关键词（逗号分隔）
    private static final String KEYWORDS = "titan,cartier,sekonda,reloj,cheetah,casio";
    // 关键词集合（去重且trim处理）
    private static final Set<String> KEYWORD_SET = Arrays.stream(KEYWORDS.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toSet());

    public static void main(String[] args) {
        // 源Excel文件路径
        String sourceFilePath = "D:\\dream\\ReverseASIN-US-B0F482BVM7-Last-30-days.xlsx";
        // 目标Excel文件路径
        String targetFilePath = "D:\\dream\\output1.xlsx";

        // 检查关键词配置
        if (KEYWORD_SET.isEmpty()) {
            System.out.println("警告：未配置有效的关键词");
            return;
        }

        // 1. 从第三行开始读取并筛选数据
        readAndFilterData(sourceFilePath);

        // 2. 将筛选后的数据写入新Excel的两个sheet
        writeProcessedData(targetFilePath);

        System.out.println("处理完成！");
        System.out.println("原数据（不含关键词）：" + originalData.size() + " 条");
        System.out.println("关键词数据：" + keywordData.size() + " 条");
    }

    /**
     * 从第三行开始读取数据并进行筛选
     */
    private static void readAndFilterData(String sourceFile) {
        EasyExcel.read(sourceFile, AsinEntity.class, new AnalysisEventListener<AsinEntity>() {
                    @Override
                    public void invoke(AsinEntity entity, AnalysisContext context) {
                        // 获取第一列的值（根据实际实体类调整）
                        String firstColumnValue = getFirstColumnValue(entity);

                        // 检查是否包含任何关键词
                        boolean hasKeyword = checkContainsAnyKeyword(firstColumnValue);

                        // 根据检查结果分类存储
                        if (hasKeyword) {
                            keywordData.add(entity);
                        } else {
                            originalData.add(entity);
                        }
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext context) {
                        System.out.println("数据读取与筛选完成");
                    }
                })
                .headRowNumber(2)  // 关键配置：从第三行开始读取数据（0索引）
                .sheet()
                .doRead();
    }

    /**
     * 将处理后的数据写入新Excel的两个sheet
     */
    private static void writeProcessedData(String outputFile) {
        // 写入原数据（不含关键词）到第一个sheet
        EasyExcel.write(outputFile, AsinEntity.class)
                .sheet("原数据")
                .doWrite(originalData);

        // 写入含关键词数据到第二个sheet
        EasyExcel.write(outputFile, AsinEntity.class)
                .sheet("含关键词数据")
                .doWrite(keywordData);
    }

    /**
     * 检查字符串是否包含任何关键词
     */
    private static boolean checkContainsAnyKeyword(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        for (String keyword : KEYWORD_SET) {
            if (value.contains(keyword)) {
                return true;  // 匹配到任何一个关键词就返回true
            }
        }
        return false;
    }

    /**
     * 获取第一列的值（需根据AsinEntity实际结构调整）
     */
    private static String getFirstColumnValue(AsinEntity entity) {
        // 示例：假设实体类中对应第一列的字段是asin
        // 请根据你的实际字段名修改
        return entity.getKeyWords();
    }
}