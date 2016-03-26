package com.jmgarzo.tools;

import android.content.Context;

import com.jmgarzo.bbdd.BBDDRepostajes;
import com.jmgarzo.objects.Repostaje;

import java.math.BigDecimal;

/**
 * Created by jmgarzo on 29/12/14.
 */
public class ToolsRespostaje {

    public static void calcularConsumoMedio(Context context, Repostaje repostaje) {

        BBDDRepostajes bbddRepostajes = new BBDDRepostajes(context);
        BigDecimal sumaKmRecorridos = new BigDecimal(0);
        BigDecimal sumaLitros = new BigDecimal(0);


        if (null != repostaje) {
            BigDecimal cien = BigDecimal.valueOf(100);
            sumaLitros = sumaLitros.add(repostaje.getLitros());
            Repostaje repostajeAnterior = bbddRepostajes.getRepostajeAnteriorPorCoche(repostaje);


            boolean hayUnCompletoAnterior = bbddRepostajes.hayUnCompletoAnterior(repostaje.getIdCoche(), repostaje.getKmRepostaje());

            if (repostaje.getEsCompleto() && hayUnCompletoAnterior) {


                if (null != repostajeAnterior) {
                    repostaje.setKmRecorridos(BigDecimal.valueOf((repostaje.getKmRepostaje().doubleValue() - repostajeAnterior.getKmRepostaje().doubleValue())));
                    sumaKmRecorridos = repostaje.getKmRecorridos();
//                    do {
//                        sumaKmRecorridos = sumaKmRecorridos.add(repostajeAnterior.getKmRecorridos());
//                        sumaLitros = sumaLitros.add(repostajeAnterior.getLitros());
//                        repostajeAnterior = bbddRepostajes.getRepostajeAnteriorPorCoche(repostajeAnterior);
//                    } while (null != repostajeAnterior && !repostajeAnterior.getEsCompleto());

                    while (null != repostajeAnterior && !repostajeAnterior.getEsCompleto()) {
                        sumaKmRecorridos = sumaKmRecorridos.add(repostajeAnterior.getKmRecorridos());
                        sumaLitros = sumaLitros.add(repostajeAnterior.getLitros());
                        repostajeAnterior = bbddRepostajes.getRepostajeAnteriorPorCoche(repostajeAnterior);
                    }
                    if (sumaKmRecorridos.compareTo(BigDecimal.ZERO) > 0)
                        repostaje.setMediaConsumo((sumaLitros.multiply(cien)).divide(sumaKmRecorridos, 2,
                                BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));

//                    if (repostajeAnterior.getEsCompleto()) {
//                        repostaje.setKmRecorridos(BigDecimal.valueOf((repostaje.getKmRepostaje().doubleValue() - repostajeAnterior
//                                .getKmRepostaje().doubleValue())));
//
//                        BigDecimal cien = BigDecimal.valueOf(100);
//                        if (repostaje.getKmRecorridos().compareTo(BigDecimal.ZERO) != 0) {
//                            repostaje.setMediaConsumo((repostaje.getLitros().multiply(cien)).divide(repostaje.getKmRecorridos(), 2,
//                                    BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
                } else {
                    repostaje.setMediaConsumo(BigDecimal.ZERO);
                    repostaje.setKmRecorridos(BigDecimal.ZERO);
                }

            } else {
                repostaje.setMediaConsumo(BigDecimal.ZERO);
                if (null != repostajeAnterior) {
                    repostaje.setKmRecorridos(BigDecimal.valueOf((repostaje.getKmRepostaje().doubleValue() - repostajeAnterior.getKmRepostaje().doubleValue())));
                } else {

                    repostaje.setKmRecorridos(BigDecimal.ZERO);
                }

            }

        }

    }


}
