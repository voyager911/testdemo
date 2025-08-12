package dream.it.task;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import dream.it.pojo.Employee;
import dream.it.util.TestFileUtil;

public class readDemo {
    private String fileName = TestFileUtil.getPath() + "simpleWrite1750300831958.xlsx";

    public void getFileName() {
        EasyExcel.read(fileName, Employee.class, new PageReadListener<Employee>(dataList -> {
            //读取数据后对数据执行的操作，这里直接输出
            for (Employee demoData : dataList) {
                System.out.println(demoData);
            }
        })).sheet().doRead();
    }
}
