package dream.it.com;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class ExcelFilter {

    // 存储过滤后的行数据
    private static List<List<String>> filteredData = new ArrayList<>();

    public static void main(String[] args) {
        // 输入文件路径
        String inputFilePath = "D:\\dream\\ReverseASIN-US-B0F482BVM7-Last-30-days.xlsx";
        // 输出文件路径
        String outputFilePath = "output.xlsx";
        // 要过滤的关键字
        String keyword = "bvlgari";

        // 读取并过滤Excel
        readAndFilterExcel(inputFilePath, keyword);

        // 打印过滤后的内容
        printFilteredData();

        // 写入过滤后的内容到新Excel
        writeFilteredExcel(outputFilePath);

        System.out.println("处理完成！过滤后的文件已保存至: " + outputFilePath);
    }

    /**
     * 读取Excel并过滤含有关键字的行
     */
    private static void readAndFilterExcel(String filePath, String keyword) {
        EasyExcel.read(filePath)
                .headRowNumber(2) // 从第0行开始读（包含表头）
                .registerReadListener(new AnalysisEventListener<List<String>>() {
                    // 表头数据
                    private List<String> header;

                    @Override
                    public void invokeHeadMap(java.util.Map<Integer, String> headMap, AnalysisContext context) {
                        // 保存表头
                        header = new ArrayList<>(headMap.values());
                        filteredData.add(header);
                    }

                    @Override
                    public void invoke(List<String> rowData, AnalysisContext context) {
                        // 检查第一列是否包含关键字，不包含则保留
                        if (rowData != null && !rowData.isEmpty()) {
                            String firstColumnValue = rowData.get(0);
                            if (firstColumnValue == null || !firstColumnValue.contains(keyword)) {
                                filteredData.add(rowData);
                            }
                        }
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext context) {
                        System.out.println("Excel读取完成，共处理 " + (filteredData.size() - 1) + " 行数据（不含表头）");
                    }
                })
                .sheet() // 读取第一个sheet
                .doRead();
    }

    /**
     * 打印过滤后的内容
     */
    private static void printFilteredData() {
        System.out.println("\n过滤后的Excel内容：");
        for (List<String> row : filteredData) {
            for (String cell : row) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }
    }

    /**
     * 将过滤后的内容写入新Excel
     */
    private static void writeFilteredExcel(String filePath) {
        EasyExcel.write(filePath)
                .head(Collections.singletonList(filteredData.get(0))) // 使用原表头
                .sheet("过滤后的数据")
                .doWrite(filteredData.subList(1, filteredData.size())); // 跳过表头
    }
}
