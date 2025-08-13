package dream.it.task;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import dream.it.constant.KeywordConstants;
import dream.it.pojo.AsinEntity;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class EnhancedExcelProcessor {
    // 存储去重后的所有数据
    private static final List<AsinEntity> allDeduplicatedData = new ArrayList<>();
    // 存储包含关键词的数据
    private static final List<AsinEntity> keywordData = new ArrayList<>();
    // 存储不包含关键词的数据
    private static final List<AsinEntity> nonKeywordData = new ArrayList<>();
    // 存储符合二次筛选条件的数据（第三个sheet）
    private static final List<AsinEntity> naturalSearchData = new ArrayList<>();

    // 引用女表关键词（直接使用枚举类）
    private static final Set<String> KEYWORD_SET = KeywordConstants.WOMEN_WATCH.getKeywordSet();


    public static void main(String[] args) {
        // 目标文件夹路径
        String folderPath = "D:\\dream\\seeneromen";
        // 输出文件路径
        String outputFilePath = "D:\\dream\\seeneromen\\output1.xlsx";

        // 验证关键词配置
        if (KEYWORD_SET.isEmpty()) {
            System.err.println("错误：请配置有效的关键词");
            return;
        }

        // 1. 处理文件夹中所有Excel文件
        processExcelFilesInFolder(folderPath);

        // 2. 按关键词筛选数据
        filterDataByKeywords();

        // 3. 二次筛选：从非关键词数据中提取符合条件的行到第三个sheet
        filterNaturalSearchData();

        // 4. 输出结果到一个Excel文件的三个sheet
        writeResultToExcel(outputFilePath);

        System.out.println("处理完成！结果已保存至：" + outputFilePath);
        System.out.println("总去重后数据行数：" + allDeduplicatedData.size());
        System.out.println("包含关键词的数据行数：" + keywordData.size());
        System.out.println("不包含关键词的数据行数：" + nonKeywordData.size());
        System.out.println("自然搜索词数据行数：" + naturalSearchData.size());
    }

    /**
     * 处理文件夹中的所有Excel文件
     */
    private static void processExcelFilesInFolder(String folderPath) {
        File folder = new File(folderPath);
        File[] excelFiles = folder.listFiles(file -> {
            String fileName = file.getName().toLowerCase();
            return fileName.endsWith(".xlsx") || fileName.endsWith(".xls");
        });

        if (excelFiles == null || excelFiles.length == 0) {
            System.out.println("未找到任何Excel文件");
            return;
        }

        System.out.println("找到 " + excelFiles.length + " 个Excel文件，开始处理...");

        // 用于去重的Set，存储第一列的值
        Set<String> firstColumnValues = new HashSet<>();

        // 遍历处理每个Excel文件
        for (File file : excelFiles) {
            System.out.println("处理文件：" + file.getName());

            // 读取Excel文件，从第三行开始（索引为2）
            EasyExcel.read(file.getAbsolutePath(), AsinEntity.class, new AnalysisEventListener<AsinEntity>() {
                @Override
                public void invoke(AsinEntity data, AnalysisContext context) {
                    // 只处理第三行及以后的数据
                    if (context.readRowHolder().getRowIndex() >= 2) {
                        String firstColumn = data.getKeyWords();
                        if (firstColumn != null && !firstColumn.trim().isEmpty()) {
                            // 去重处理：只保留第一列首次出现的数据
                            if (firstColumnValues.add(firstColumn.trim())) {
                                allDeduplicatedData.add(data);
                            }
                        }
                    }
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                    System.out.println("文件处理完成：" + file.getName());
                }
            }).sheet().doRead();
        }
    }

    /**
     * 按关键词筛选数据
     */
    private static void filterDataByKeywords() {
        for (AsinEntity data : allDeduplicatedData) {
            String firstColumn = data.getKeyWords();
            if (firstColumn == null) {
                nonKeywordData.add(data);
                continue;
            }

            // 检查第一列是否包含任何关键词
            boolean containsKeyword = KEYWORD_SET.stream()
                    .anyMatch(keyword -> firstColumn.contains(keyword));

            if (containsKeyword) {
                keywordData.add(data);
            } else {
                nonKeywordData.add(data);
            }
        }
    }

    /**
     * 二次筛选：从非关键词数据中提取符合条件的行
     * 条件：第一列包含关键词组 且 G列包含"自然搜索词"
     */
    private static void filterNaturalSearchData() {
        for (AsinEntity data : nonKeywordData) {
            String firstColumn = data.getKeyWords();
            String gColumn = data.getTrafficKeywordType(); // 获取G列数据

            // 检查条件：第一列包含关键词组 且 G列包含"自然搜索词"
            boolean firstColumnHasKeyword = firstColumn != null && KEYWORD_SET.stream()
                    .anyMatch(keyword -> firstColumn.contains(keyword));

            boolean gColumnHasNaturalSearch = gColumn != null && gColumn.contains("自然搜索词");

            if (firstColumnHasKeyword && gColumnHasNaturalSearch) {
                naturalSearchData.add(data);
                // 从非关键词数据中移除这些行
                nonKeywordData.remove(data);
            }
        }
    }

    /**
     * 将结果写入Excel文件的三个sheet
     */
    private static void writeResultToExcel(String outputFilePath) {
        // 创建一个Excel写入器
        try (var writer = EasyExcel.write(outputFilePath, AsinEntity.class).build()) {
            // 第一个sheet：不包含关键词的数据（已移除要放到第三个sheet的行）
            var nonKeywordSheet = EasyExcel.writerSheet(0, "非关键词数据").build();
            writer.write(nonKeywordData, nonKeywordSheet);

            // 第二个sheet：包含关键词的数据
            var keywordSheet = EasyExcel.writerSheet(1, "关键词数据").build();
            writer.write(keywordData, keywordSheet);

            // 第三个sheet：符合二次筛选条件的数据
            var naturalSearchSheet = EasyExcel.writerSheet(2, "自然搜索词数据").build();
            writer.write(naturalSearchData, naturalSearchSheet);
        }
    }
}
    