package dream.it.filterTitle;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import dream.it.pojo.AsinEntity;
import lombok.extern.slf4j.Slf4j;

import java.io.File;


/**
 * 步骤1 使用T1 的词组匹配跟标题不能匹配中的 词组并输出出来
 * 步骤2 使用输出出来的词组交给gpt 更改描述
 */
@Slf4j
public class TitleFilter {// 配置参数

    // 目标标题（用于过滤匹配词组）
    private static final String TITLE = "Seenero Gold Watch For Women's Dainty Analog Dress Round Roman Diamond Dial Unique Designer Trendy Bangle Gold Bracelets For Ladies Watches With Waterproof Luxury Gift";
    private static final String TITLE_LOWER = TITLE.toLowerCase();

    // 结果文件输出目录（默认与Excel文件夹同级的result目录）
    private static String resultDir;

    public static void main(String[] args) {
        // 1. 配置路径
        String folderPath = "D:\\dream\\keyWordFilter"; // Excel文件夹路径
        resultDir = folderPath;   // 结果文件保存目录

        // 2. 初始化结果目录
        initResultDir();

        // 3. 处理文件夹中所有Excel文件
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("文件夹不存在或不是有效目录：" + folderPath);
            return;
        }
        processExcelFiles(folder);

        System.out.println("\n===== 所有文件处理完成，结果已保存至：" + resultDir + " =====");
    }

    /**
     * 初始化结果目录（不存在则创建）
     */
    private static void initResultDir() {
        File dir = new File(resultDir);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new RuntimeException("无法创建结果目录：" + resultDir);
            }
        }
    }

    /**
     * 处理文件夹中所有Excel文件
     */
    private static void processExcelFiles(File folder) {
        File[] excelFiles = folder.listFiles(file -> {
            String name = file.getName().toLowerCase();
            return file.isFile() && (name.endsWith(".xlsx") || name.endsWith(".xls"));
        });

        if (excelFiles == null || excelFiles.length == 0) {
            System.out.println("文件夹中未找到Excel文件");
            return;
        }

        for (File excelFile : excelFiles) {
            System.out.println("\n===== 处理文件：" + excelFile.getName() + " =====");
            readAndExportExcel(excelFile);
        }
    }

    /**
     * 读取Excel并导出不匹配词组到TXT
     */
    private static void readAndExportExcel(File excelFile) {
        // 构建结果文件路径（与原Excel同名，后缀改为txt）
        String fileName = excelFile.getName().replaceAll("\\.(xlsx|xls)$", ".txt");
        String resultFilePath = resultDir + "/" + fileName;

        // 用于收集当前文件的所有结果
        List<String> fileResult = new ArrayList<>();
        fileResult.add("===== " + excelFile.getName() + " 处理结果 =====");
        fileResult.add("比对标题：" + TITLE);
        fileResult.add(""); // 空行分隔

        // 读取Excel并处理数据
        EasyExcel.read(excelFile)
                .head(AsinEntity.class)
                .sheet(0)
                .headRowNumber(0)
                .registerReadListener(new PageReadListener<AsinEntity>(dataList -> {
                    for (AsinEntity entity : dataList) {
                        processEntity(entity, fileResult);
                    }
                }))
                .doRead();

        // 将结果写入TXT文件
        writeToTxt(resultFilePath, fileResult);
        System.out.println("结果已导出至：" + resultFilePath);
    }

    /**
     * 处理单个ASIN实体并收集结果
     */
    private static void processEntity(AsinEntity entity, List<String> fileResult) {
        if (entity == null || entity.getKeyWords() == null || entity.getKeyWords().trim().isEmpty()) {
            return;
        }

        // 获取并过滤词组
        List<String> allPhrases = Collections.singletonList(entity.getKeyWords());
        List<String> unmatchedPhrases = allPhrases.stream()
                .filter(phrase -> phrase != null && !phrase.trim().isEmpty())
                .filter(phrase -> !TITLE_LOWER.contains(phrase.trim().toLowerCase()))
                .collect(Collectors.toList());

        // 收集结果
        log.info("ASIN: " + entity.getKeyWords().trim());
        log.info("不匹配的词组（共" + unmatchedPhrases.size() + "个）：");
        for (int i = 0; i < unmatchedPhrases.size(); i++) {
            fileResult.add(unmatchedPhrases.get(i).trim());
        }
    }

    /**
     * 将结果写入TXT文件
     */
    private static void writeToTxt(String filePath, List<String> content) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(filePath),
                StandardCharsets.UTF_8)) {
            for (String line : content) {
                writer.write(line);
                writer.newLine(); // 换行
            }
        } catch (IOException e) {
            System.err.println("写入文件失败：" + filePath);
            e.printStackTrace();
        }
    }
}
