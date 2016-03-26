package com.jmgarzo.tools;

import java.math.BigDecimal;

/**
 * Created by JoseMaria on 17/11/2015.
 */
public class ToolsConversiones {
    private final static BigDecimal kmPorMilla = new BigDecimal("1.609344");
    private final static BigDecimal litrosPorGalonUK = new BigDecimal("4.5460902819948");
    private final static BigDecimal litrosPorGalonUS = new BigDecimal("3.785411784");

    public static BigDecimal kmToMillas(BigDecimal km) {
        return km.divide(kmPorMilla, 15, BigDecimal.ROUND_HALF_UP);
    }


    public static BigDecimal millasToKm(BigDecimal millas) {
        return (millas.multiply(kmPorMilla)).setScale(15, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal litrosToGalonesUS(BigDecimal litros) {
        return litros.divide(litrosPorGalonUS, 15, BigDecimal.ROUND_HALF_UP).setScale(15, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal galonesUSToLitros(BigDecimal galonesUS) {
        return (galonesUS.multiply(litrosPorGalonUS)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal litrosToGalonesUK(BigDecimal litros) {
        return litros.divide(litrosPorGalonUK, 15, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal galonesUKToLitros(BigDecimal galonesUK) {
        return (galonesUK.multiply(litrosPorGalonUK)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal precioPorLitroToPrecioPorGalonUS(BigDecimal precioPorLitro) {
        return (precioPorLitro.multiply(litrosPorGalonUS)).setScale(3, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal precioPorGalonUSToPrecioPorLitro(BigDecimal precioPorGalonUS) {
        return (precioPorGalonUS.divide(litrosPorGalonUS, 15, BigDecimal.ROUND_HALF_UP)).setScale(3, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal precioPorLitroToPrecioPorGalonUK(BigDecimal precioPorLitro) {
        return (precioPorLitro.multiply(litrosPorGalonUK)).setScale(3, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal precioPorGalonUKToPrecioPorLitro(BigDecimal precioPorGalonUK) {
        return (precioPorGalonUK.divide(litrosPorGalonUK, 15, BigDecimal.ROUND_HALF_UP).setScale(3, BigDecimal.ROUND_HALF_UP));
    }

    public static BigDecimal litros100ToMpgUS(BigDecimal litrosALos100) {
        BigDecimal resultado = BigDecimal.ZERO;
        if (null != litrosALos100 && (litrosALos100.compareTo(BigDecimal.ZERO) != 0)) {
            resultado = new BigDecimal(235.215).divide(litrosALos100, 15, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return resultado;

    }

    public static BigDecimal mpgUSToLitros100(BigDecimal mpgUS) {
        BigDecimal resultado = BigDecimal.ZERO;
        if (null != mpgUS && (mpgUS.compareTo(BigDecimal.ZERO) != 0)) {
            resultado = new BigDecimal(235.215).divide(mpgUS, 15, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return resultado;
    }

    public static BigDecimal litros100ToMpgImp(BigDecimal litrosALos100) {
        BigDecimal resultado = BigDecimal.ZERO;
        if (null != litrosALos100 && (litrosALos100.compareTo(BigDecimal.ZERO) != 0)) {
            resultado = new BigDecimal(282.481).divide(litrosALos100, 15, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return resultado;
    }

    public static BigDecimal mpgImpToLitros100(BigDecimal mpgImp) {
        BigDecimal resultado = BigDecimal.ZERO;
        if (null != mpgImp && (mpgImp.compareTo(BigDecimal.ZERO) != 0)) {
            resultado = new BigDecimal(282.481).divide(mpgImp, 15, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return resultado;
    }


    public static BigDecimal RedondearSinDecimales(BigDecimal valor) {
        return valor.setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal RedondearDecimales(BigDecimal valor, int decimales) {
        return valor.setScale(decimales, BigDecimal.ROUND_HALF_UP);
    }


}
