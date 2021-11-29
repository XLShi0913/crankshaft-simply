package math;

import java.util.ArrayList;
import java.util.List;

public class MathUtil {
    private static final int GRID = 10000; // 积分网格数

    /**
     * 数值积分工具：被积函数必须封装在给定的接口中
     * 如有兴趣，可将此积分工具按照龙格库塔法进行改进
     * @param fc 被积函数的封装容器
     * @param start 积分上界
     * @param end 积分下界
     * @return 定积分结果
     */
    public static double integral(FunctionContainer fc, double start, double end) {
        if (fc == null) return 0;
        double ans = 0;
        double step = (end-start)/GRID;
        double x = start;
        while (x < end) {
            double y = x+step > end ? fc.function(x) :
                    (fc.function(x)+fc.function(x+step))/2;
            ans += y * step;
            x += step;
        }
        return ans;
    }

    /**
     * 求指定函数的曲线
     * @param fun 函数
     * @param lo 下界
     * @param hi 上界
     * @param step 步长
     * @param title 图像名称
     */
    public static void showFunction(FunctionContainer fun, double lo, double hi, double step, String title) {
        List<Double> data = new ArrayList<>();
        for (double i = lo; i <= hi; i += step) {
            data.add(fun.function(i));
        }
        double[][] dataArr = new double[2][data.size()];
        for (int i = 0; i < data.size(); i++) {
            dataArr[0][i] = 0;
            dataArr[1][i] = data.get(i);
        }
        PlotUtil.plotXY(dataArr, title);
    }

    public static void showFunction(FunctionContainer fun, String title) {
        showFunction(fun, 0, 0.8, 0.01, title);
    }

    // 拼接
    public static void showFunction(FunctionContainer f1, FunctionContainer f2, FunctionContainer f3, String title) {
        FunctionContainer total = x -> {
            if (0.0 <= x && x < 0.8) return f1.function(x);
            else if (0.8 <= x && x < 1.6) return f2.function(x-0.8);
            else if (1.6 <= x && x < 2.4) return f3.function(x-1.6);
            else return 0.0;
        };
        showFunction(total, 0, 2.4, 0.01, title);
    }
}
