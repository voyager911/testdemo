package dream.it.com;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.util.ListUtils;
import dream.it.pojo.AsinEntity;
import dream.it.pojo.Employee;
import dream.it.util.TestFileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class UnitlExcel {

    private static UnitlExcel excel = new UnitlExcel();

    // 存储过滤后的行数据
    private static List<List<String>> filteredData = new ArrayList<>();

    private static String fileName = TestFileUtil.getPath() + "simpleWrite1754625327603.xlsx";
    private static String fileName1 = "D:\\dream\\ReverseASIN-US-B0F482BVM7-Last-30-days.xlsx";

    public static void main(String[] args) {
        System.out.println("heelii");
        excel.getFileNameLocal(fileName1);
    }

    // 读方法
    public void getFileName(String path) {
        System.out.println("path" + path);
        EasyExcel.read(path, Employee.class, new PageReadListener<Employee>(dataList -> {
            //读取数据后对数据执行的操作，这里直接输出
            for (Employee demoData : dataList) {
                System.out.println(demoData);
            }
        })).sheet().doRead();
    }

    // 读方法
    public void getFileNameLocal(String path) {
        System.out.println("path" + path);
        // 输入文件路径
//        String inputFilePath = "D:\\dream\\ReverseASIN-US-B0F482BVM7-Last-30-days.xlsx";
        // 输出文件路径
        String outputFilePath = "D:\\dream\\output1.xlsx";
        // 要过滤的关键字
        String keyword = "bvlgari";
        // 读取并过滤 Excel
//        readAndFilterExcel(path, keyword);
        // 打印过滤后的内容
        printFilteredData();
        // 写入过滤后的内容到新 Excel
        writeFilteredExcel(outputFilePath);
        System.out.println("处理完成！过滤后的文件已保存至:" + outputFilePath);
    }

    public void redAndFilterExcel(){

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
        EasyExcel.write(new File(filePath)).head(Collections.singletonList(filteredData.get(0))) // 使用原表头
                .sheet("过滤后的数据").doWrite(filteredData.subList(1, filteredData.size())); // 跳过表头
    }


    // 写方法
    public void write() {
        //文件的路径和名字
        String fileName = TestFileUtil.getPath() + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(fileName, Employee.class).sheet("模板666").doWrite(data(10)); //sheet: 表格名字
    }

    private List<Employee> data(int count) {
        List<Employee> list = ListUtils.newArrayList();
        for (int i = 1; i <= count; i++) {
            list.add(new Employee(i, "测试数据" + i, new Date(), 6.6 * i));
        }
        return list;
    }
}
