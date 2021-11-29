import crankshaft.CrankShaft;
import math.FunctionContainer;
import math.MathUtil;


public class Main {
    public static void main(String[] args) {
        solve(5*Math.PI/3);
    }

    /**
     * @param phi 驱动端转角（顺时针转）
     */
    public static void solve(double phi) {
        phi = -phi;
        double[] cooZ = {0.095, 0.203, 0.2745, 0.346, 0.454, 0.5255, 0.597, 0.705}; // 待校验截面的坐标

        // 计算受力和弯矩
        System.out.println("驱动端角度: " + phi/Math.PI*180);
        CrankShaft[] cArr = new CrankShaft[3];
        for (int i = 0; i < 3; i++) {
            cArr[i] = new CrankShaft();
        }
        cArr[0].calForce(phi);
        cArr[1].calForce(phi + 2.0/3.0*Math.PI);
        cArr[2].calForce(phi + 4.0/3.0*Math.PI);

        // 更新扭矩
        double t1 = cArr[1].getMaxTorque();
        double t2 = cArr[2].getMaxTorque();
        cArr[0].calTorque(t1+t2);
        cArr[1].calTorque(t2);
        cArr[2].calTorque(0);

        // 应力计算
        FunctionContainer[] stressF = new FunctionContainer[3];
        for (int i = 0; i < 3; i++) {
            cArr[i].calStress();
            stressF[i] = cArr[i].stress;
        }
        MathUtil.showFunction(cArr[0].stress, cArr[1].stress, cArr[2].stress, "stress");
        MathUtil.showFunction(cArr[0].sigma, cArr[1].sigma, cArr[2].sigma, "sigma");
        MathUtil.showFunction(cArr[0].tau, cArr[1].tau, cArr[2].tau, "tau");

        System.out.println("应力结果*****************************************");
        for (int i = 0; i < 3; i++) {
            double[] stress = new double[cooZ.length];
            for (int j = 0; j < cooZ.length; j++) {
                stress[j] = stressF[i].function(cooZ[j]) / 1e6;
            }
            showArr(stress);
        }
    }

    public static void showArr(double[] arr) {
        StringBuffer sb = new StringBuffer();
        for (double num : arr) {
            sb.append(num);
            sb.append(',');
        }
        sb.deleteCharAt(sb.length()-1);
        System.out.println(sb);
    }
}
