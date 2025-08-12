package dream.it.com;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import dream.it.pojo.AsinEntity;

import java.util.List;

public class ExcelReadExample {
    public static void main(String[] args) {
        // Excel文件路径，根据实际情况修改
        String filePath = "your_local_excel_file.xlsx";

        // 读取Excel文件
        EasyExcel.read(filePath, AsinEntity.class, new AnalysisEventListener<List<AsinEntity>>() {
            // 每读取一批数据（默认一次读取100条，可配置）就会调用此方法

            @Override
            public void invoke(List<AsinEntity> dataList, AnalysisContext analysisContext) {
                for (AsinEntity asinEntity : dataList) {
                    System.out.println(asinEntity);
                }
            }

            // 所有数据读取完成后调用
            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                System.out.println("Excel数据读取完成");
            }
        }).sheet().doRead();
    }
}