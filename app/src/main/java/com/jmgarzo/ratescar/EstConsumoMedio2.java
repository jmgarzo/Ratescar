package com.jmgarzo.ratescar;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.androidplot.Plot;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;


import java.text.DecimalFormat;
import java.util.Random;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidplot.Plot;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.jmgarzo.bbdd.BBDDCoches;
import com.jmgarzo.bbdd.BBDDRepostajes;
import com.jmgarzo.objects.Coche;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Repostaje;


public class EstConsumoMedio2 extends Activity implements View.OnTouchListener {

    private AdView adView;
    // private static final int SERIES_SIZE = 200;
    private XYPlot mySimpleXYPlot;
    private Button resetButton;
    private SimpleXYSeries[] series = null;
    private PointF minXY;
    private PointF maxXY;

    private EditText txtFechaInicioConsumo, txtFechaFinConsumo;
    private Spinner cmbCocheConsumo1, cmbCocheConsumo2;
    private TextView lbCocheConsumo2;
    private Button btnActualizarGrafico;

    Long fechaInicio;
    Long fechaFin;


    private BBDDRepostajes bbddRepostajes;
    private BBDDCoches bbddCoches;

    private ArrayAdapter<Coche> adaptadorCoche1;
    private ArrayAdapter<Coche> adaptadorCoche2;

    // private ArrayList<Coche> listaCochesTotal;
    private ArrayList<Coche> listaCoches1;
    private ArrayList<Coche> listaCoches2;

    private ArrayList<Number> listaConsumos1;
    private ArrayList<Number> listaFechas1;
    private Integer idCoche1;
    private Integer idCoche2;
    private ArrayList<Number> listaConsumos2;
    private ArrayList<Number> listaFechas2;


    private boolean hayMasDeUnCoche;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_est_consumo_medio2);

        /**PUBLICIDAD**/

        adView = new AdView(this);
        adView.setAdUnitId(Constantes.ADUNITID);
        adView.setAdSize(AdSize.BANNER);

        // Buscar LinearLayout suponiendo que se le ha asignado
        // el atributo android:id="@+id/mainLayout".
        LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.banner);

        // Añadirle adView.
        linearLayout.addView(adView);

        // Iniciar una solicitud genérica.
        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)       // Emulador
//                .addTestDevice("16A4155AB1690691277DB1EB823AB891") // Mi teléfono
                .build();

        // Cargar adView con la solicitud de anuncio.
        adView.loadAd(adRequest);

        /*FIN PUBLIDAD*/

        getActionBar().setDisplayHomeAsUpEnabled(true);


        hayMasDeUnCoche = hayMasDeUnCocheConRepostaje();
        bbddRepostajes = new BBDDRepostajes(this);
        bbddCoches = new BBDDCoches(this);
        //listaCochesTotal = bbddCoches.getTodosLosCochesConRepostajeOrdenadosPorId();
        listaCoches1 = new ArrayList<Coche>();
        listaCoches2 = new ArrayList<Coche>();
        ArrayList<Integer> listaIdCocheConMasDeUnRepostaje = getIdCocheConMasDeUnRepostaje();
        //listaCoches2.addAll(listaCochesTotal);

        for (Integer idCoche : listaIdCocheConMasDeUnRepostaje) {
            Coche coche = new Coche();
            coche = bbddCoches.getCoche(idCoche.toString());
            listaCoches1.add(coche);
        }
        listaCoches2.addAll(listaCoches1);


        lbCocheConsumo2 = (TextView) findViewById(R.id.lbCocheConsumo2);
        txtFechaInicioConsumo = (EditText) findViewById(R.id.txtFechaInicioConsumo);
        txtFechaFinConsumo = (EditText) findViewById(R.id.txtFechaFinConsumo);
        cmbCocheConsumo1 = (Spinner) findViewById(R.id.cmbCocheConsumo1);
        cmbCocheConsumo2 = (Spinner) findViewById(R.id.cmbCocheConsumo2);
        btnActualizarGrafico = (Button) findViewById(R.id.btnActualizarGrafico);

        /************* FECHA INICIO ********************/

        txtFechaInicioConsumo.setFocusable(false);
        txtFechaInicioConsumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(txtFechaInicioConsumo);
            }
        });


        fechaInicio = bbddRepostajes.getFechaPrimerRepostaje();

        /************* FECHA PRIMER REPOSTAJE ********************/
        Calendar cal = new GregorianCalendar();
        java.util.Date dateInicio = new Date(
                fechaInicio);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(dateInicio);

        txtFechaInicioConsumo.setText(formatteDate);


        /************************************************************************/
        /************* FECHA FIN ********************/

        txtFechaFinConsumo.setFocusable(false);
        txtFechaFinConsumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(txtFechaFinConsumo);
            }
        });


        fechaFin = bbddRepostajes.getFechaUltimoRespotaje();

        /************* FECHA ULTIMO REPOSTAJE ********************/
        Calendar cal2 = new GregorianCalendar();
        java.util.Date dateFin = new Date(
                fechaFin);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate2 = df2.format(dateFin);

        txtFechaFinConsumo.setText(formatteDate2);


        cmbCocheConsumo1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //                finish();
                //                startActivity(getIntent());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        cmbCocheConsumo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//         resetButton = (Button) findViewById(R.id.resetButton);
//         resetButton.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View view) {
//                 minXY.x = series[0].getX(0).floatValue();
//                 maxXY.x = series[3].getX(series[3].size() - 1).floatValue();
//                 mySimpleXYPlot.setDomainBoundaries(minXY.x, maxXY.x, BoundaryMode.FIXED);
//
//                 // pre 0.5.1 users should use postRedraw() instead.
//                 mySimpleXYPlot.redraw();
//             }
//         });


        btnActualizarGrafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarDatos();
                mySimpleXYPlot.clear();


                dibujarGrafico(listaConsumos1, listaFechas1, idCoche1, listaConsumos2, listaFechas2, idCoche2);
                minXY.x = series[0].getX(0).floatValue();
                maxXY.x = series[3].getX(series[3].size() - 1).floatValue();
                mySimpleXYPlot.setDomainBoundaries(minXY.x, maxXY.x, BoundaryMode.FIXED);

                // pre 0.5.1 users should use postRedraw() instead.
                mySimpleXYPlot.redraw();
            }
        });


        /****************************************************************************************/
        //********* CARGA del SPINNER COCHES 1 *************//
        // ArrayList<Coche> listaCochesTotal = bbddCoches.getTodosLosCochesConRepostajeOrdenadosPorId();
//        ArrayList<Coche> listaCoches1 = new ArrayList<Coche>();
//        ArrayList<Coche> listaCoches2 = new ArrayList<Coche>();
//        ArrayList<Integer>listaIdCocheConMasDeUnRepostaje = getIdCocheConMasDeUnRepostaje();
//        for(Integer idCoche : listaIdCocheConMasDeUnRepostaje){
//            Coche coche= new Coche();
//            coche = bbddCoches.getCoche(idCoche.toString());
//        }
//        listaCoches1.addAll(listaCochesTotal);
//        listaCoches2.addAll(listaCochesTotal);

//        ArrayList<Coche> cochesConRepostaje = bbddCoches.getTodosLosCochesConRepostajeOrdenadosPorId();
//        for(Coche coche : cochesConRepostaje){
//            coche.getIdCoche()
//        }

        if (bbddCoches.getNumeroCochesConRepostajes() >= 2 && hayMasDeUnCocheConRepostaje()) {
            hayMasDeUnCoche = true;

            adaptadorCoche1 = new ArrayAdapter<Coche>(this, android.R.layout.simple_spinner_item, listaCoches1);

            cmbCocheConsumo1.setAdapter(adaptadorCoche1);


            adaptadorCoche2 = new ArrayAdapter<Coche>(this, android.R.layout.simple_spinner_item,
                    listaCoches2);
            cmbCocheConsumo2.setAdapter(adaptadorCoche2);
            cmbCocheConsumo1.setSelection(1);

        } else {
            hayMasDeUnCoche = false;

            adaptadorCoche1 = new ArrayAdapter<Coche>(this, android.R.layout.simple_spinner_item,
                    listaCoches1);
            cmbCocheConsumo1.setAdapter(adaptadorCoche1);

            //Ocultamos el spinner del segundo coche
            lbCocheConsumo2.setVisibility(View.INVISIBLE);
            cmbCocheConsumo2.setVisibility(View.INVISIBLE);

        }


        actualizarDatos();

        dibujarGrafico(listaConsumos1, listaFechas1, idCoche1, listaConsumos2, listaFechas2, idCoche2);
    }


    // Definition of the touch states
    static final int NONE = 0;
    static final int ONE_FINGER_DRAG = 1;
    static final int TWO_FINGERS_DRAG = 2;
    int mode = NONE;

    PointF firstFinger;
    float distBetweenFingers;
    boolean stopThread = false;

    @Override
    public boolean onTouch(View arg0, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: // Start gesture
                firstFinger = new PointF(event.getX(), event.getY());
                mode = ONE_FINGER_DRAG;
                stopThread = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_POINTER_DOWN: // second finger
                distBetweenFingers = spacing(event);
                // the distance check is done to avoid false alarms
                if (distBetweenFingers > 5f) {
                    mode = TWO_FINGERS_DRAG;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == ONE_FINGER_DRAG) {
                    PointF oldFirstFinger = firstFinger;
                    firstFinger = new PointF(event.getX(), event.getY());
                    scroll(oldFirstFinger.x - firstFinger.x);
                    mySimpleXYPlot.setDomainBoundaries(minXY.x, maxXY.x,
                            BoundaryMode.FIXED);
                    mySimpleXYPlot.redraw();

                } else if (mode == TWO_FINGERS_DRAG) {
                    float oldDist = distBetweenFingers;
                    distBetweenFingers = spacing(event);
                    zoom(oldDist / distBetweenFingers);
                    mySimpleXYPlot.setDomainBoundaries(minXY.x, maxXY.x,
                            BoundaryMode.FIXED);
                    mySimpleXYPlot.redraw();
                }
                break;
        }
        return true;
    }

    private void zoom(float scale) {
        float domainSpan = maxXY.x - minXY.x;
        float domainMidPoint = maxXY.x - domainSpan / 2.0f;
        float offset = domainSpan * scale / 2.0f;

        minXY.x = domainMidPoint - offset;
        maxXY.x = domainMidPoint + offset;

        minXY.x = Math.min(minXY.x, series[3].getX(series[3].size() - 2)
                .floatValue());
        maxXY.x = Math.max(maxXY.x, series[0].getX(1).floatValue());
        clampToDomainBounds(domainSpan);
    }

    private void scroll(float pan) {
        float domainSpan = maxXY.x - minXY.x;
        float step = domainSpan / mySimpleXYPlot.getWidth();
        float offset = pan * step;
        minXY.x = minXY.x + offset;
        maxXY.x = maxXY.x + offset;
        clampToDomainBounds(domainSpan);
    }

    private void clampToDomainBounds(float domainSpan) {
        float leftBoundary = series[0].getX(0).floatValue();
        float rightBoundary = series[3].getX(series[3].size() - 1).floatValue();
        // enforce left scroll boundary:
        if (minXY.x < leftBoundary) {
            minXY.x = leftBoundary;
            maxXY.x = leftBoundary + domainSpan;
        } else if (maxXY.x > series[3].getX(series[3].size() - 1).floatValue()) {
            maxXY.x = rightBoundary;
            minXY.x = rightBoundary - domainSpan;
        }
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    public void showDatePickerDialog(EditText v) {
        DatePickerFragment newFragment = new DatePickerFragment(this, v);
        newFragment.show(getFragmentManager(), "datePicker");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_est_consumo_medio2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void dibujarGrafico(ArrayList<Number> listaConsumos1, ArrayList<Number> listaFechas1, Integer idCoche1, ArrayList<Number> listaConsumos2, ArrayList<Number> listaFechas2, Integer idCoche2) {

        aumentarFechaSiDosIguales(listaFechas1);
        aumentarFechaSiDosIguales(listaFechas2);

        mySimpleXYPlot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
        mySimpleXYPlot.setOnTouchListener(this);
        mySimpleXYPlot.getGraphWidget().setTicksPerRangeLabel(2);
        mySimpleXYPlot.getGraphWidget().setTicksPerDomainLabel(2);
        mySimpleXYPlot.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
        mySimpleXYPlot.getGraphWidget().setRangeValueFormat(
                new DecimalFormat("##.##"));
        mySimpleXYPlot.setDomainValueFormat(new Format() {

            // create a simple date format that draws on the year portion of our timestamp.
            // see http://download.oracle.com/javase/1.4.2/docs/api/java/text/SimpleDateFormat.html
            // for a full description of SimpleDateFormat.
            private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {

                // because our timestamps are in seconds and SimpleDateFormat expects milliseconds
                // we multiply our timestamp by 1000:
                long timestamp = ((Number) obj).longValue();
                java.util.Date date = new java.util.Date(timestamp);
                return dateFormat.format(date, toAppendTo, pos);
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;

            }
        });
        mySimpleXYPlot.getGraphWidget().setRangeLabelWidth(10);
        mySimpleXYPlot.setTitle(getString(R.string.titulo_grafico_consumo_medio));
        mySimpleXYPlot.setRangeLabel(getString(R.string.titulo_grafico_consumo_medio_range));
        mySimpleXYPlot.setDomainLabel(getString(R.string.titulo_grafico_consumo_medio_domain));

        //        mySimpleXYPlot.setBorderStyle(Plot.BorderStyle.NONE, null, null);
        //mySimpleXYPlot.disableAllMarkup();
        series = new SimpleXYSeries[4];
        //        int scale = 1;
        //        for (int i = 2; i < 4; i++, scale *= 5) {
        //            series[i] = new SimpleXYSeries("S" + i);
        //            populateSeries(series[i], scale);
        //        }


        Coche coche1 = bbddCoches.getCoche(idCoche1.toString());
        String titulo1 = "";
        if (!Constantes.esVacio(coche1.getNombre())) {
            titulo1 = coche1.getNombre();
        } else if (!Constantes.esVacio(coche1.getMatricula())) {
            titulo1 = coche1.getMatricula();
        }


        String titulo2 = "";
        if (hayMasDeUnCoche) {
            Coche coche2 = bbddCoches.getCoche(idCoche2.toString());
            if (!Constantes.esVacio(coche2.getNombre())) {
                titulo2 = coche2.getNombre();
            } else if (!Constantes.esVacio(coche2.getMatricula())) {
                titulo2 = coche2.getMatricula();
            }
        }

        series[0] = new SimpleXYSeries(listaFechas1, listaConsumos1, titulo1);
        series[1] = new SimpleXYSeries(listaFechas2, listaConsumos2, titulo2);
        series[2] = new SimpleXYSeries(listaFechas1, listaConsumos1, "");
        series[3] = new SimpleXYSeries(listaFechas2, listaConsumos2, "");


        LineAndPointFormatter series1Format = new LineAndPointFormatter();
        series1Format.setPointLabelFormatter(new PointLabelFormatter());
        series1Format.configure(getApplicationContext(),
                R.xml.line_point_formatter_with_plf1);
        mySimpleXYPlot.addSeries(series[0], series1Format);

        LineAndPointFormatter series2Format = new LineAndPointFormatter();
        series2Format.setPointLabelFormatter(new PointLabelFormatter());
        series2Format.configure(getApplicationContext(),
                R.xml.line_point_formatter_with_plf2);
        if (hayMasDeUnCoche) {
            mySimpleXYPlot.addSeries(series[1], series2Format);
        }


        //mySimpleXYPlot.addSeries(series[2], series1Format);


        //mySimpleXYPlot.addSeries(series[3], series2Format);


        //mySimpleXYPlot.redraw();
        mySimpleXYPlot.calculateMinMaxVals();
        minXY = new PointF(mySimpleXYPlot.getCalculatedMinX().floatValue(),
                mySimpleXYPlot.getCalculatedMinY().floatValue());
        maxXY = new PointF(mySimpleXYPlot.getCalculatedMaxX().floatValue(),
                mySimpleXYPlot.getCalculatedMaxY().floatValue());

        mySimpleXYPlot.redraw();

    }

    private void actualizarDatos() {

        idCoche1 = listaCoches1.get(cmbCocheConsumo1.getSelectedItemPosition()).getIdCoche();

        String sfecha = txtFechaInicioConsumo.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        GregorianCalendar cal = new GregorianCalendar();
        try {
            cal.setTime(format.parse(sfecha));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // calendarFecha.getTimeInMillis();
        fechaInicio = cal.getTimeInMillis();


        String sfecha2 = txtFechaFinConsumo.getText().toString();
        SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
        GregorianCalendar cal2 = new GregorianCalendar();
        try {
            cal2.setTime(format2.parse(sfecha2));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // calendarFecha.getTimeInMillis();
        fechaFin = cal2.getTimeInMillis();

        ArrayList<Repostaje> listaRepostajes1 = bbddRepostajes.getRepostajesPorCocheYEntreFechasOrdenadosFecha(idCoche1, fechaInicio, fechaFin);

        listaConsumos1 = new ArrayList<Number>();
        listaFechas1 = new ArrayList<Number>();
        for (Repostaje rep : listaRepostajes1) {
            listaConsumos1.add(rep.getMediaConsumo().setScale(2, BigDecimal.ROUND_HALF_UP));
            listaFechas1.add(rep.getFechaRespostaje());
        }


        listaConsumos2 = new ArrayList<Number>();
        listaFechas2 = new ArrayList<Number>();
        idCoche2 = 0;
        if (hayMasDeUnCoche) {
            idCoche2 = listaCoches2.get(cmbCocheConsumo2.getSelectedItemPosition()).getIdCoche();
            ArrayList<Repostaje> listaRepostajes2 = bbddRepostajes.getRepostajesPorCocheYEntreFechasOrdenadosFecha(idCoche2, fechaInicio, fechaFin);

            listaConsumos2 = new ArrayList<Number>();
            listaFechas2 = new ArrayList<Number>();
            for (Repostaje rep : listaRepostajes2) {
                listaConsumos2.add(rep.getMediaConsumo().setScale(2, BigDecimal.ROUND_HALF_UP));
                listaFechas2.add(rep.getFechaRespostaje());
            }
        } else {
            listaConsumos2 = listaConsumos1;
            listaFechas2 = listaFechas1;
        }
    }

    private boolean hayMasDeUnCocheConRepostaje() {

        ArrayList<Integer> cochesConRepostajes = getIdCocheConMasDeUnRepostaje();
        boolean hayMasDeUnCocheConRepostaje;
        if (cochesConRepostajes.size() > 1)
            hayMasDeUnCocheConRepostaje = true;
        else {
            hayMasDeUnCocheConRepostaje = false;
        }
        return hayMasDeUnCocheConRepostaje;
    }

    private ArrayList<Integer> getIdCocheConMasDeUnRepostaje() {

        BBDDRepostajes bbddRepostajes = new BBDDRepostajes(this);
        ArrayList<Repostaje> listaRepostajes = new ArrayList<Repostaje>();
        listaRepostajes = bbddRepostajes.getTodosLosRepostajesOrdenadoPorCoche();

        Integer max = 0;
        int contadorRepostajes = 0;
        Integer idCoche;
        ArrayList<Integer> cochesConRepostajes = new ArrayList<Integer>();
        if (null != listaRepostajes && listaRepostajes.size() > 1) {
            idCoche = listaRepostajes.get(0).getIdCoche();
            for (Repostaje re : listaRepostajes) {
                if (idCoche == re.getIdCoche()) {
                    if (re.getMediaConsumo().toString() != BigDecimal.ZERO.toString()) {
                        contadorRepostajes++;
                    }
                } else {


                    idCoche = re.getIdCoche();
                    contadorRepostajes = 0;
                }

                if (contadorRepostajes > max) {
                    max = contadorRepostajes;
                }

                if (contadorRepostajes > 1) {
                    if (!cochesConRepostajes.contains(idCoche)) {
                        cochesConRepostajes.add(idCoche);
                    }
                }
            }
        }
        return cochesConRepostajes;
    }

    /**
     * Cuando se graba un mismo repostaje el mismo día no se muestra correctamente en el gráfico
     * Para ello vamos a sumar un valor a la fecha si hay dos fechas iguales
     */
    private void aumentarFechaSiDosIguales(ArrayList<Number> listaFechas) {
        Number result;
        ArrayList<Number> listaTransformada = new ArrayList<Number>();
        for (int i = 0; i < listaFechas.size(); i++) {
            if (i > 0) {
                if (listaFechas.get(i).longValue() == listaFechas.get(i - 1).longValue()) {
                    result = listaTransformada.get(i - 1).longValue() + 1l;
                    listaTransformada.add(result);
                } else {
                    listaTransformada.add(listaFechas.get(i));
                }

            } else {
                listaTransformada.add(listaFechas.get(i));
            }
        }
        listaFechas.clear();
        listaFechas.addAll(listaTransformada);

    }


}



