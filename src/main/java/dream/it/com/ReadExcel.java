package dream.it.com;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import dream.it.pojo.AsinEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReadExcel {
    // 存储过滤后的行数据
    private static List<List<String>> filteredData = new ArrayList<>();
    // 用于暂存读取到的数据
    private static final List<AsinEntity> dataList = new ArrayList<>();

    public static void main(String[] args) {
        // 输入文件路径
        String inputFilePath = "D:\\dream\\ReverseASIN-US-B0F482BVM7-Last-30-days.xlsx";
        // 输出文件路径
        String outputFilePath = "D:\\dream\\output.xlsx";
        // 要过滤的关键字
        String keyword = "bvlgari";

        // 源Excel文件路径（读取的文件）
        String sourceFilePath = "path/to/source.xlsx";
        // 目标Excel文件路径（写入的新文件）
        String targetFilePath = "path/to/target.xlsx";

        // 1. 从第三行开始读取源Excel数据
        readExcelFromThirdRow(inputFilePath);

        // 2. 将读取到的数据写入新Excel
        writeDataToNewExcel(outputFilePath);

        System.out.println("操作完成！共处理 " + dataList.size() + " 条数据");

// 读取并过滤 Excel
//        readAndFilterExcel(inputFilePath, keyword);
//        readAndConsole(inputFilePath, keyword);
// 打印过滤后的内容
//        printFilteredData();
// 写入过滤后的内容到新 Excel
//        writeFilteredExcel(outputFilePath);
//        System.out.println("处理完成！过滤后的文件已保存至:" + outputFilePath);
    }

    /**
     * 从第三行开始读取Excel数据
     */
    private static void readExcelFromThirdRow(String sourceFilePath) {
        EasyExcel.read(sourceFilePath, AsinEntity.class, new AnalysisEventListener<AsinEntity>() {
                    @Override
                    public void invoke(AsinEntity asinEntity, AnalysisContext context) {
                        // 将读取到的数据添加到集合
                        dataList.add(asinEntity);
                        System.out.println("读取到数据: " + asinEntity);
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext context) {
                        System.out.println("读取完成，共获取 " + dataList.size() + " 条数据");
                    }
                })
                .headRowNumber(2) // 从第三行开始读取（0索引）
                .sheet()
                .doRead();
    }

    /**
     * 将数据写入新的Excel文件
     */
    private static void writeDataToNewExcel(String targetFilePath) {
        EasyExcel.write(targetFilePath, AsinEntity.class)
                .sheet("处理后的数据") // 新工作表名称
                .doWrite(dataList); // 写入数据集合
    }

    /**
     * 读取 Excel 并过滤含有关键字的行，从第三行开始处理
     */
    private static void readAndConsole(String filePath, String keyword) {

        // 读取Excel，从第三行开始（索引为2）
        EasyExcel.read(filePath, AsinEntity.class, new AnalysisEventListener<AsinEntity>() {

                    // 每读取一行数据（从第三行开始）调用此方法
                    @Override
                    public void invoke(AsinEntity asinEntity, AnalysisContext context) {
                        System.out.println("读取到数据: " + asinEntity);
                    }

                    // 所有数据读取完成后调用
                    @Override
                    public void doAfterAllAnalysed(AnalysisContext context) {
                        System.out.println("Excel文件读取完成，已打印所有从第三行开始的数据");
                    }
                })
                .headRowNumber(2) // 核心配置：设置表头行号为2（0索引），即从第三行开始读数据
                .sheet() // 读取第一个工作表
                .doRead();
    }

    /**
     * 读取 Excel 并过滤含有关键字的行，从第三行开始处理
     */
    private static void readAndFilterExcel(String filePath, String keyword) {
        EasyExcel.read(new File(filePath))
                .headRowNumber(2) // 从第 0 行开始读（包含表头）
                .registerReadListener(new AnalysisEventListener<List<String>>() {
                    // 表头数据
                    private List<String> header;
                    // 记录当前行号
                    private int rowNumber = 0;

                    @Override
                    public void invokeHeadMap(java.util.Map<Integer, String> headMap, AnalysisContext context) {
// 保存表头
                        header = new ArrayList<>(headMap.values());
                        filteredData.add(header);
                    }

                    @Override
                    public void invoke(List<String> rowData, AnalysisContext context) {
                        rowNumber++;
// 从第三行开始检查第一列是否包含关键字，不包含则保留
                        if (rowNumber >= 3) {
                            if (rowData != null && !rowData.isEmpty()) {
                                String firstColumnValue = rowData.get(0);
                                if (firstColumnValue == null || !firstColumnValue.contains(keyword)) {
                                    filteredData.add(rowData);
                                }
                            }
                        } else {
// 第一行和第二行（表头和第二行数据）直接保留
                            filteredData.add(rowData);
                        }
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext context) {
                        System.out.println("Excel 读取完成，共处理" + (filteredData.size() - 1) + "行数据（不含表头）");
                    }
                })
                .sheet() // 读取第一个 sheet
                .doRead();
    }

    /**
     * 打印过滤后的内容
     */
    private static void printFilteredData() {
        System.out.println("\n 过滤后的 Excel 内容：");
        for (List<String> row : filteredData) {
            for (String cell : row) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }
    }

    /**
     * 将过滤后的内容写入新 Excel
     */
    private static void writeFilteredExcel(String filePath) {
        EasyExcel.write(new File(filePath))
                .head(Collections.singletonList(filteredData.get(0))) // 使用原表头
                .sheet("过滤后的数据")
                .doWrite(filteredData.subList(1, filteredData.size())); // 跳过表头
    }


}

