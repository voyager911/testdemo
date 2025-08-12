package dream.it.task;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import dream.it.pojo.AsinEntity;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 女表去除品牌分析,后需要去重
 */
public class MenWatchTask {
    // 存储所有不含关键词的数据（原sheet）
    private static final List<AsinEntity> allOriginalData = new ArrayList<>();
    // 存储所有含有关键词的数据（第二个sheet）
    private static final List<AsinEntity> allKeywordData = new ArrayList<>();
    // 女表品牌词（逗号分隔）
    private static final String KEYWORDS = "fitbit,apple,bark,fit bit,enewton,phoibos,brandy,foxgirl,nibosi,hermès,aobei,joyas,civo,hermes,seiko,skagen,speidel,tag heuer,pulsar,larsson,van cleef,dw,larsson,fiyta,bulgari,charm,salvador,bering,rosegold,raymond,vivienne,boderry,lola rose,longines,david,pavoi,xoxo,mondaine,ferragamo,disney,breda,aesthetic,moissanite,geneva,brighton,polo,trump,steve madden,oferta,mvmt,guess,burberry,serpent,montre,ecodrive,dainty,philippe,panthere,nixon,elgin,tissot,reloj acero,liebeskind,sketchers,armani,anna,kate,armitron,skmei,pandora,jbw,mk,jessica,olevs,pagani,berny,timex,micheal,cavalli,calvin,stretch,coach,lacoste,swiss,skechers,ck,reloj fosil,kate,kenneth,ann klein,michael,michel,boho,swatch,victoria,scott,tommy,michael kors,ofertas,dkny,bulova,peugeot,citizen,omega,tory burch,reloges,olivia burton,rado,relic,michael kors,nine west,stuhrling,movado,ninewest,fossil,goth,betsey johnson,chanel,technomarine,michele,versace,bvlgari,daniel,swarovski,anne,invicta,michele,titan,cartier,sekonda,cheetah,casio,gucci,rolex,shark,tank,fendi,snake";

    // 关键词集合
    private static final Set<String> KEYWORD_SET = Arrays.stream(KEYWORDS.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toSet());
    // 统计参数
    private static int processedFileCount = 0;
    private static int totalRowCount = 0;
    // 新增：记录跳过的临时文件数量
    private static int skippedTempFileCount = 0;

    public static void main(String[] args) {
        // 目标文件夹路径
        String folderPath = "D:\\dream\\seeneromen";
        // 输出文件路径
        String outputFilePath = "D:\\dream\\seeneromen\\output2.xlsx";

        // 验证关键词配置
        if (KEYWORD_SET.isEmpty()) {
            System.err.println("错误：请配置有效的关键词");
            return;
        }

        // 1. 遍历文件夹处理所有Excel文件
        processAllExcelInFolder(folderPath);

        // 2. 打印统计信息
        printStatistics();

        // 3. 写入汇总结果
        writeSummaryResult(outputFilePath);

        System.out.println("\n处理完成！汇总结果已保存至：" + outputFilePath);
    }

    /**
     * 遍历文件夹中的所有Excel文件并处理
     * 优化点：排除Excel临时文件（以~$开头的文件）
     */
    private static void processAllExcelInFolder(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> {
            // 过滤条件：排除以~$开头的临时文件，同时只保留.xls和.xlsx格式
            return !name.startsWith("~$") &&
                    (name.toLowerCase().endsWith(".xlsx") || name.toLowerCase().endsWith(".xls"));
        });

        if (files == null || files.length == 0) {
            System.out.println("未找到任何有效Excel文件");
            return;
        }

        // 统计并显示临时文件数量
        countSkippedTempFiles(folder);

        for (File file : files) {
            processedFileCount++;
            System.out.println("[" + processedFileCount + "/" + files.length + "] 正在处理文件：" + file.getName());
            processSingleExcel(file.getAbsolutePath());
        }
    }

    /**
     * 统计并显示被跳过的临时文件
     */
    private static void countSkippedTempFiles(File folder) {
        File[] tempFiles = folder.listFiles((dir, name) ->
                name.startsWith("~$") &&
                        (name.toLowerCase().endsWith(".xlsx") || name.toLowerCase().endsWith(".xls"))
        );

        if (tempFiles != null && tempFiles.length > 0) {
            skippedTempFileCount = tempFiles.length;
            System.out.println("检测到" + skippedTempFileCount + "个Excel临时文件，将自动跳过处理");
            for (File tempFile : tempFiles) {
                System.out.println("跳过临时文件：" + tempFile.getName());
            }
        }
    }

    /**
     * 处理单个Excel文件
     */
    private static void processSingleExcel(String filePath) {
        try {
            EasyExcel.read(filePath, AsinEntity.class, new AnalysisEventListener<AsinEntity>() {
                        @Override
                        public void invoke(AsinEntity entity, AnalysisContext context) {
                            // 只处理第三行及以后的数据（行索引从0开始，第三行为索引2）
                            if (context.readRowHolder().getRowIndex() >= 2) {
                                totalRowCount++;
                                String firstColumnValue = entity.getKeyWords();

                                boolean hasKeyword = KEYWORD_SET.stream()
                                        .anyMatch(keyword -> firstColumnValue != null
                                                && firstColumnValue.contains(keyword));

                                if (hasKeyword) {
                                    allKeywordData.add(entity);
                                } else {
                                    allOriginalData.add(entity);
                                }
                            }
                        }

                        @Override
                        public void doAfterAllAnalysed(AnalysisContext context) {
                            System.out.println("文件处理完成：" + filePath);
                        }
                    })
                    .headRowNumber(0) // 表头行号（不影响数据读取逻辑，这里仅为解析用）
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            System.err.println("处理文件失败：" + filePath + "，错误信息：" + e.getMessage());
            // 可选：如果需要继续处理其他文件，可以不抛出异常
            // e.printStackTrace();
        }
    }

    /**
     * 打印统计信息
     * 优化点：增加临时文件跳过统计
     */
    private static void printStatistics() {
        System.out.println("\n===== 处理统计 =====");
        System.out.println("1. 处理的Excel文件数量：" + processedFileCount);
        System.out.println("2. 跳过的临时文件数量：" + skippedTempFileCount);
        System.out.println("3. 总处理数据行数（第三行及以后）：" + totalRowCount);
        System.out.println("4. 不含过滤词的数据行数：" + allOriginalData.size());
        System.out.println("5. 含有过滤词的数据行数：" + allKeywordData.size());
        System.out.println("6. 筛选比例：" + String.format("%.2f%%",
                totalRowCount > 0 ? (double) allKeywordData.size() / totalRowCount * 100 : 0));
    }

    /**
     * 写入汇总结果到Excel
     */
    private static void writeSummaryResult(String outputFilePath) {
        try (var writer = EasyExcel.write(outputFilePath, AsinEntity.class).build()) {
            // 第一个sheet：所有不含关键词的数据
            var originalSheet = EasyExcel.writerSheet(0, "过滤后的数据").build();
            writer.write(allOriginalData, originalSheet);

            // 第二个sheet：所有含有关键词的数据
            var keywordSheet = EasyExcel.writerSheet(1, "含过滤词的数据").build();
            writer.write(allKeywordData, keywordSheet);
        } catch (Exception e) {
            System.err.println("写入输出文件失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}