package crankshaft;

import math.FunctionContainer;

/**
 * 单段曲轴，可以计算法向力、切向力和弯矩
 */
public class CrankShaft {

    // 常量定义
    private double E = 2.12e11; // 杨氏模量
    private double G = 8.2813e10; // 切变模量
    private double wy = 106852553.051/108/1e9; // 抗弯截面模数
    private double wp = 213705106.101/108/1e9; // 抗扭截面模数
    private double a = 0.2745; // 主轴颈中点到曲柄销中点轴向距离
    private double b = 0.251;  // 两曲柄销中点轴向距离
    private double r = 0.085;  // 曲柄销-主轴颈轴线距离
    private double crl = 0.46;// 连杆长度

    // 受力
    public double fr;   // 法向力
    public double ft;   // 切向力
    public FunctionContainer moment;    // 弯矩
    public FunctionContainer torque;    // 扭矩
    public FunctionContainer sigma;     // 弯曲正应力
    public FunctionContainer tau;       // 扭转切应力
    public FunctionContainer stress;    // 应力强度

    public CrankShaft() {}

    public void calForce(double phi) {
        double[] frt = calForceRT(phi);
        fr = frt[0];
        ft = frt[1];
        moment = calMoment(fr);
    }

    // 法向力、切向力
    private double[] calForceRT(double phi) {
        double rodLoad = (277 * Math.sin(phi) - 95) * 1e3;
        System.out.println("杆载荷: " + rodLoad);
        double beta = Math.asin( Math.sin(phi) * r / crl);
        double crForce = rodLoad / Math.cos(beta);
        System.out.println("连杆力: " + crForce);
        double fr = crForce * Math.cos(phi + beta);
        double ft = crForce * Math.sin(phi + beta);
        System.out.println("法向力: " + fr + ", 切向力: " + ft);
        double fy = crForce * Math.cos(beta);
        double fz = crForce * Math.sin(beta);
        System.out.println("Fy: " + fy + ", Fz: " + fz);
        return new double[]{fr, ft};
    }

    // 弯矩
    private FunctionContainer calMoment(double fr) {
        double l = 2 * a + b;
        // 内力
        double[] shearForce = new double[] {
                b/l*fr,
                -2*a/l*fr,
                b/l*fr
        };
        return x -> {
            double momentF; // 外力引起的弯矩
            if (0 <= x && x <= a) momentF = shearForce[0] * x;
            else if (a < x && x <= a+b) momentF = shearForce[0] * a + shearForce[1] * (x-a);
            else if (a+b < x && x <= 2*a+b) momentF = shearForce[0] * a + shearForce[1] * b + shearForce[2] * (x-a-b);
            else momentF = -1;
            return Math.abs(momentF) < 1e-8 ? 0 : momentF;
        };
    }

    // 计算最大扭矩
    public double getMaxTorque() {
        return 2 * ft * r;
    }

    // 计算扭矩
    public void calTorque(double add) {
        this.torque = x -> {
            double torque;
            if (0 <= x && x <= a) torque = 2 * ft * r + add;
            else if (a < x && x <= a+b) torque = ft * r + add;
            else if (a+b < x && x <= 2*a+b) torque = add;
            else torque = -1;
            return Math.abs(torque) < 1e-8 ? 0 : torque;
        };
    }

    // 弯曲正应力
    private void calSigma() {
        this.sigma = x -> moment.function(x) / wy;
    }

    // 扭转切应力
    private void calTau() {
        this.tau = x -> torque.function(x) / wp;
    }

    // 应力强度
    public void calStress() {
        calSigma();
        calTau();
        this.stress = x -> {
            double sigma = this.sigma.function(x);
            double tau = this.tau.function(x);
            return Math.sqrt(sigma*sigma + 4*tau*tau);
        };
    }
}
