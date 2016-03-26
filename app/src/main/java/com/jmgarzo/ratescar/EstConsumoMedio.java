//package com.jmgarzo.ratescar;
//
//import android.app.Activity;
//import android.graphics.Color;
//import android.graphics.PointF;
//import android.os.Bundle;
//import android.util.FloatMath;
//import android.view.DragEvent;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import com.androidplot.Plot;
//import com.androidplot.xy.BoundaryMode;
//import com.androidplot.xy.LineAndPointFormatter;
//import com.androidplot.xy.PointLabelFormatter;
//import com.androidplot.xy.SimpleXYSeries;
//import com.androidplot.xy.XYPlot;
//import com.androidplot.xy.XYSeries;
//import com.androidplot.xy.XYStepMode;
//import com.jmgarzo.bbdd.BBDDCoches;
//import com.jmgarzo.bbdd.BBDDRepostajes;
//import com.jmgarzo.objects.Coche;
//import com.jmgarzo.objects.Constantes;
//import com.jmgarzo.objects.Repostaje;
//
//import java.math.BigDecimal;
//import java.sql.Date;
//import java.text.DateFormat;
//import java.text.DecimalFormat;
//import java.text.FieldPosition;
//import java.text.Format;
//import java.text.ParseException;
//import java.text.ParsePosition;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.GregorianCalendar;
//
//
//public class EstConsumoMedio extends Activity implements View.OnTouchListener {
//
//    private EditText txtFechaInicioConsumo, txtFechaFinConsumo;
//    private Spinner cmbCocheConsumo1, cmbCocheConsumo2;
//    private TextView lbCocheConsumo2;
//    private Button btnActualizarGrafico;
//
//    private XYPlot XYPlotConsumoMedio;
//    private XYPlot XYPlotConsumoMedio2;
//
//    private BBDDRepostajes bbddRepostajes;
//    private BBDDCoches bbddCoches;
//
//    private ArrayList<Coche> listaCochesTotal;
//    private ArrayList<Coche> listaCoches1;
//    private ArrayList<Coche> listaCoches2;
//    private ArrayAdapter<Coche> adaptadorCoche1;
//    private ArrayAdapter<Coche> adaptadorCoche2;
//
//    Long fechaInicio;
//    Long fechaFin;
//
//    private boolean hayMasDeUnCoche;
//
//    private SimpleXYSeries[] series = null;
//
//    // Definition of the touch states
//    private static final int NONE = 0;
//    private static final int ONE_FINGER_DRAG = 1;
//    private static final int TWO_FINGERS_DRAG = 2;
//    private int mode = NONE;
//
//    PointF firstFinger;
//    float distBetweenFingers;
//    boolean stopThread = false;
//
//    private PointF minXY;
//    private PointF maxXY;
//
//    private SimpleXYSeries series1, series2;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_est_consumo_medio);
//
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//
//        hayMasDeUnCoche = false;
//
//        bbddRepostajes = new BBDDRepostajes(this);
//
//        bbddCoches = new BBDDCoches(this);
//        listaCochesTotal = bbddCoches.getTodosLosCochesConRepostajeOrdenadosPorId();
//        listaCoches1 = new ArrayList<Coche>();
//        listaCoches2 = new ArrayList<Coche>();
//        listaCoches1.addAll(listaCochesTotal);
//        listaCoches2.addAll(listaCochesTotal);
//
//        lbCocheConsumo2 = (TextView) findViewById(R.id.lbCocheConsumo2);
//        txtFechaInicioConsumo = (EditText) findViewById(R.id.txtFechaInicioConsumo);
//        txtFechaFinConsumo = (EditText) findViewById(R.id.txtFechaFinConsumo);
//        cmbCocheConsumo1 = (Spinner) findViewById(R.id.cmbCocheConsumo1);
//        cmbCocheConsumo2 = (Spinner) findViewById(R.id.cmbCocheConsumo2);
//        btnActualizarGrafico = (Button) findViewById(R.id.btnActualizarGrafico);
//
//        XYPlotConsumoMedio2 = (XYPlot) findViewById(R.id.XYPlotConsumoMedio2);
//        //XYPlotConsumoMedio2.clear();
//        XYPlotConsumoMedio2.setOnTouchListener(this);
//        XYPlotConsumoMedio2.getGraphWidget().setTicksPerRangeLabel(2);
//        XYPlotConsumoMedio2.getGraphWidget().setTicksPerDomainLabel(2);
//        XYPlotConsumoMedio2.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
//        XYPlotConsumoMedio2.getGraphWidget().setRangeLabelWidth(10);
//
//        series = new SimpleXYSeries[2];
//
//        /************* FECHA INICIO ********************/
//
//        txtFechaInicioConsumo.setFocusable(false);
//        txtFechaInicioConsumo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDatePickerDialog(txtFechaInicioConsumo);
//            }
//        });
//
//
//        fechaInicio = bbddRepostajes.getFechaPrimerRepostaje();
//
//        /************* FECHA PRIMER REPOSTAJE ********************/
//        Calendar cal = new GregorianCalendar();
//        java.util.Date dateInicio = new Date(
//                fechaInicio);
//        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//        String formatteDate = df.format(dateInicio);
//
//        txtFechaInicioConsumo.setText(formatteDate);
//
//
///************************************************************************/
//        /************* FECHA FIN ********************/
//
//        txtFechaFinConsumo.setFocusable(false);
//        txtFechaFinConsumo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDatePickerDialog(txtFechaFinConsumo);
//            }
//        });
//
//
//        fechaFin = bbddRepostajes.getFechaUltimoRespotaje();
//
//        /************* FECHA ULTIMO REPOSTAJE ********************/
//        Calendar cal2 = new GregorianCalendar();
//        java.util.Date dateFin = new Date(
//                fechaFin);
//        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
//        String formatteDate2 = df2.format(dateFin);
//
//        txtFechaFinConsumo.setText(formatteDate2);
//
//
//
//        cmbCocheConsumo1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                finish();
////                startActivity(getIntent());
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//
//        });
//
//
//        cmbCocheConsumo2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
///****************************************************************************************/
//        //********* CARGA del SPINNER COCHES 1 *************//
//
//
//        if (bbddCoches.getNumeroCochesConRepostajes() >= 2) {
//            hayMasDeUnCoche = true;
//            adaptadorCoche1 = new ArrayAdapter<Coche>(this, android.R.layout.simple_spinner_item,
//                    listaCoches1);
//            cmbCocheConsumo1.setAdapter(adaptadorCoche1);
//
////            listaCoches2.remove(0);
//
//            adaptadorCoche2 = new ArrayAdapter<Coche>(this, android.R.layout.simple_spinner_item,
//                    listaCoches2);
//            cmbCocheConsumo2.setAdapter(adaptadorCoche2);
//            cmbCocheConsumo1.setSelection(1);
//
//        } else {
//            hayMasDeUnCoche = false;
//
//            adaptadorCoche1 = new ArrayAdapter<Coche>(this, android.R.layout.simple_spinner_item,
//                    listaCochesTotal);
//            cmbCocheConsumo1.setAdapter(adaptadorCoche1);
//
//            //Ocultamos el spinner del segundo coche
//            lbCocheConsumo2.setVisibility(View.INVISIBLE);
//            cmbCocheConsumo2.setVisibility(View.INVISIBLE);
//
//        }
//
//
//
//
//        btnActualizarGrafico.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                Integer idCoche1 = listaCoches1.get(cmbCocheConsumo1.getSelectedItemPosition()).getIdCoche();
//
//                String sfecha = txtFechaInicioConsumo.getText().toString();
//                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//                GregorianCalendar cal = new GregorianCalendar();
//                try {
//                    cal.setTime(format.parse(sfecha));
//                } catch (ParseException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                // calendarFecha.getTimeInMillis();
//                fechaInicio = cal.getTimeInMillis();
//
//
//                String sfecha2 = txtFechaFinConsumo.getText().toString();
//                SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
//                GregorianCalendar cal2 = new GregorianCalendar();
//                try {
//                    cal2.setTime(format2.parse(sfecha2));
//                } catch (ParseException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                // calendarFecha.getTimeInMillis();
//                fechaFin = cal2.getTimeInMillis();
//
//                ArrayList<Repostaje> listaRepostajes1 = bbddRepostajes.getRepostajesPorCocheYEntreFechasOrdenadosFecha(idCoche1,fechaInicio,fechaFin);
//
//                ArrayList<Number> listaConsumos1 = new ArrayList<Number>();
//                ArrayList<Number> listaFechas1 = new ArrayList<Number>();
//                for(Repostaje rep : listaRepostajes1 ){
//                    listaConsumos1.add(rep.getMediaConsumo());
//                    listaFechas1.add(rep.getFechaRespostaje());
//                }
//
//
//
//                ArrayList<Number> listaConsumos2 = new ArrayList<Number>();
//                ArrayList<Number> listaFechas2 = new ArrayList<Number>();
//                Integer idCoche2 = 0;
//                if (hayMasDeUnCoche) {
////                    idCoche2 = listaCoches2.get(cmbCocheConsumo2.getSelectedItemPosition()).getIdCoche();
////                    listaConsumos2 = bbddRepostajes.getConsumoMedioPorCocheYFecha(idCoche2, fechaInicio, fechaFin);
//
//                    idCoche2 = listaCoches2.get(cmbCocheConsumo2.getSelectedItemPosition()).getIdCoche();
//
//
//                    ArrayList<Repostaje> listaRepostajes2 = bbddRepostajes.getRepostajesPorCocheYEntreFechasOrdenadosFecha(idCoche2,fechaInicio,fechaFin);
//
//                    listaConsumos2 = new ArrayList<Number>();
//                    listaFechas2 = new ArrayList<Number>();
//                    for(Repostaje rep : listaRepostajes2 ){
//                        listaConsumos2.add(rep.getMediaConsumo());
//                        listaFechas2.add(rep.getFechaRespostaje());
//                    }
//                }
//
//
//
//                dibujarGrafico2(listaConsumos1,listaFechas1, idCoche1, listaConsumos2, listaFechas2 , idCoche2);
//
//            }
//        });
//
//
//        Integer idCoche1 = listaCoches1.get(cmbCocheConsumo1.getSelectedItemPosition()).getIdCoche();
//
////        ArrayList<Number> listaConsumo1 = bbddRepostajes.getConsumoMedioPorCocheYFecha(idCoche1, fechaInicio, fechaFin);
////
////        ArrayList<Number> listaConsumo2 = null;
////        Integer idCoche2 = 0;
////        if (hayMasDeUnCoche) {
////            idCoche2 = listaCoches2.get(cmbCocheConsumo2.getSelectedItemPosition()).getIdCoche();
////            listaConsumo2 = bbddRepostajes.getConsumoMedioPorCocheYFecha(idCoche2, fechaInicio, fechaFin);
////        }
////
////        //dibujarGrafico(listaConsumo1, idCoche1, listaConsumo2, idCoche2);
//
//        ArrayList<Repostaje> listaRepostajes1 = bbddRepostajes.getRepostajesPorCocheYEntreFechasOrdenadosFecha(idCoche1,fechaInicio,fechaFin);
//
//        ArrayList<Number> listaConsumos1 = new ArrayList<Number>();
//        ArrayList<Number> listaFechas1 = new ArrayList<Number>();
//        for(Repostaje rep : listaRepostajes1 ){
//            listaConsumos1.add(rep.getMediaConsumo().setScale(2, BigDecimal.ROUND_HALF_UP));
//            listaFechas1.add(rep.getFechaRespostaje());
//        }
//
//
//
//
//        ArrayList<Number> listaConsumos2 = new ArrayList<Number>();
//        ArrayList<Number> listaFechas2 = new ArrayList<Number>();
//        Integer idCoche2 = 0;
//                if (hayMasDeUnCoche) {
//                    idCoche2 = listaCoches2.get(cmbCocheConsumo2.getSelectedItemPosition()).getIdCoche();
//                    ArrayList<Repostaje> listaRepostajes2 = bbddRepostajes.getRepostajesPorCocheYEntreFechasOrdenadosFecha(idCoche2,fechaInicio,fechaFin);
//                    for(Repostaje rep : listaRepostajes2 ){
//                        listaConsumos2.add(rep.getMediaConsumo().setScale(2, BigDecimal.ROUND_HALF_UP));
//                        listaFechas2.add(rep.getFechaRespostaje());
//                    }
//
//                }
//
////        minXY = new PointF(XYPlotConsumoMedio2.getCalculatedMinX().floatValue(),
////                XYPlotConsumoMedio2.getCalculatedMinY().floatValue());
////        maxXY = new PointF(XYPlotConsumoMedio2.getCalculatedMaxX().floatValue(),
////                XYPlotConsumoMedio2.getCalculatedMaxY().floatValue());
//
//
//
//        dibujarGrafico2(listaConsumos1,listaFechas1, idCoche1, listaConsumos2,listaFechas2, idCoche2);
//
//
//
//
//    }
//
////TODO descomentar si se quiere usar el primer grafico
////    private void dibujarGrafico(ArrayList<Number> listaConsumo1, Integer idCoche1, ArrayList<Number> listaConsumo2, Integer idCoche2) {
////        XYPlotConsumoMedio = (XYPlot) findViewById(R.id.XYPlotConsumoMedio);
////        XYPlotConsumoMedio.clear();
////
////        Coche coche1 = bbddCoches.getCoche(idCoche1.toString());
////        String titulo1 = "";
////        if (!Constantes.esVacio(coche1.getNombre())) {
////            titulo1 = coche1.getNombre();
////        } else if (!Constantes.esVacio(coche1.getMatricula())) {
////            titulo1 = coche1.getMatricula();
////        }
////
////        String titulo2 = "";
////        if (hayMasDeUnCoche) {
////            Coche coche2 = bbddCoches.getCoche(idCoche2.toString());
////            if (!Constantes.esVacio(coche2.getNombre())) {
////                titulo2 = coche2.getNombre();
////            } else if (!Constantes.esVacio(coche2.getMatricula())) {
////                titulo2 = coche2.getMatricula();
////            }
////        }
////
////        if (titulo1.length() > 12) {
////            titulo1 = titulo1.substring(0, 12);
////
////        }
////        // Añadimos Línea Número UNO:
////        XYSeries series1 = new SimpleXYSeries(listaConsumo1, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, titulo1);
////
////        // Repetimos para la segunda serie
////        XYSeries series2 = null;
////        if (hayMasDeUnCoche) {
////
////            if (titulo2.length() > 12) {
////                titulo2 = titulo2.substring(0, 12);
////
////            }
////            series2 = new SimpleXYSeries(listaConsumo2, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, titulo2);
////        }
////
////        LineAndPointFormatter series1Format = new LineAndPointFormatter();
////        series1Format.setPointLabelFormatter(new PointLabelFormatter());
////        series1Format.configure(getApplicationContext(),
////                R.xml.line_point_formatter_with_plf1);
////
////
////        // Una vez definida la serie (datos y estilo), la añadimos al panel
////        XYPlotConsumoMedio.addSeries(series1, series1Format);
////
////        if (hayMasDeUnCoche) {
////            // Repetimos para la segunda serie
////            LineAndPointFormatter series2Format = new LineAndPointFormatter();
////            series2Format.setPointLabelFormatter(new PointLabelFormatter());
////            series2Format.configure(getApplicationContext(),
////                    R.xml.line_point_formatter_with_plf2);
////            XYPlotConsumoMedio.addSeries(series2, series2Format);
////
////        }
////
////
////        XYPlotConsumoMedio.redraw();
////    }
//
//
//    private void dibujarGrafico2(ArrayList<Number> listaConsumos1, ArrayList<Number> listaFechas1, Integer idCoche1, ArrayList<Number> listaConsumos2,ArrayList<Number> listaFechas2, Integer idCoche2) {
//
//       // XYPlotConsumoMedio2 = (XYPlot) findViewById(R.id.XYPlotConsumoMedio2);
//        XYPlotConsumoMedio2.clear();
//
//
//
//
//        Coche coche1 = bbddCoches.getCoche(idCoche1.toString());
//        String titulo1 = "";
//        if (!Constantes.esVacio(coche1.getNombre())) {
//            titulo1 = coche1.getNombre();
//        } else if (!Constantes.esVacio(coche1.getMatricula())) {
//            titulo1 = coche1.getMatricula();
//        }
//
//
//        String titulo2 = "";
//        if (hayMasDeUnCoche) {
//            Coche coche2 = bbddCoches.getCoche(idCoche2.toString());
//            if (!Constantes.esVacio(coche2.getNombre())) {
//                titulo2 = coche2.getNombre();
//            } else if (!Constantes.esVacio(coche2.getMatricula())) {
//                titulo2 = coche2.getMatricula();
//            }
//        }
//
//
//
//
//        XYPlotConsumoMedio2.setDomainValueFormat(DateFormat.getDateInstance());
//        if (titulo1.length() > 12) {
//            titulo1 = titulo1.substring(0, 12);
//        }
//
//
//        // Añadimos Línea Número UNO:
//        series1 = new SimpleXYSeries(listaFechas1,listaConsumos1, titulo1);
//
//        series[0]=series1;
//
//        // Repetimos para la segunda serie
//        series2 = null;
//        if (hayMasDeUnCoche) {
//
//            if (titulo2.length() > 12) {
//                titulo2 = titulo2.substring(0, 12);
//
//            }
//            series2 = new SimpleXYSeries(listaFechas2,listaConsumos2, titulo2);
//            series[1]=series2;
//        }
//
//        XYPlotConsumoMedio2.calculateMinMaxVals();
//
//        LineAndPointFormatter series1Format = new LineAndPointFormatter();
//        series1Format.setPointLabelFormatter(new PointLabelFormatter());
//        series1Format.configure(getApplicationContext(),
//                R.xml.line_point_formatter_with_plf1);
//
//
//        // Una vez definida la serie (datos y estilo), la añadimos al panel
//        XYPlotConsumoMedio2.addSeries(series1, series1Format);
//
//
//        if (hayMasDeUnCoche) {
//            // Repetimos para la segunda serie
//            LineAndPointFormatter series2Format = new LineAndPointFormatter();
//            series2Format.setPointLabelFormatter(new PointLabelFormatter());
//            series2Format.configure(getApplicationContext(),
//                    R.xml.line_point_formatter_with_plf2);
//            XYPlotConsumoMedio2.addSeries(series2, series2Format);
//
//        }
//
//        XYPlotConsumoMedio2.setDomainValueFormat(new Format() {
//
//            // create a simple date format that draws on the year portion of our timestamp.
//            // see http://download.oracle.com/javase/1.4.2/docs/api/java/text/SimpleDateFormat.html
//            // for a full description of SimpleDateFormat.
//            private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//
//            @Override
//            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
//
//                // because our timestamps are in seconds and SimpleDateFormat expects milliseconds
//                // we multiply our timestamp by 1000:
//                long timestamp = ((Number) obj).longValue();
//                java.util.Date date = new java.util.Date(timestamp);
//                return dateFormat.format(date, toAppendTo, pos);
//            }
//
//            @Override
//            public Object parseObject(String source, ParsePosition pos) {
//                return null;
//
//            }
//        });
//
//        // by default, AndroidPlot displays developer guides to aid in laying out your plot.
//        // To get rid of them call disableAllMarkup():
//        //plot1.disableAllMarkup();
//
//        XYPlotConsumoMedio2.setRangeValueFormat(new DecimalFormat("0.00"));
//
//        //XYPlotConsumoMedio2.setDomainStep(XYStepMode.SUBDIVIDE,3);
//        Number maxConsumos1 = calcularMax(listaConsumos1);
//        Number minConsumos1 = calcularMin(listaConsumos1);
//
//
//
//
//        Number maxFechas1 = calcularMax(listaFechas1);
//        Number minFechas1 = calcularMin(listaFechas1);
//
//
////        minXY = new PointF(minConsumos1.floatValue(),minFechas1.floatValue());
////
////        maxXY = new PointF(maxConsumos1.floatValue(),maxFechas1.floatValue());
//
//        minXY = new PointF(XYPlotConsumoMedio2.getCalculatedMinX().floatValue(),
//                XYPlotConsumoMedio2.getCalculatedMinY().floatValue());
//        maxXY = new PointF(XYPlotConsumoMedio2.getCalculatedMaxX().floatValue(),
//                XYPlotConsumoMedio2.getCalculatedMaxY().floatValue());;
//
//
//        XYPlotConsumoMedio2.redraw();
//    }
//
//    public void showDatePickerDialog(EditText v) {
//        DatePickerFragment newFragment = new DatePickerFragment(this, v);
//        newFragment.show(getFragmentManager(), "datePicker");
//
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_est_consumo_medio, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//
//    public boolean onTouch(View arg0, MotionEvent event) {
//        switch (event.getAction() & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_DOWN: // Start gesture
//                firstFinger = new PointF(event.getX(), event.getY());
//                mode = ONE_FINGER_DRAG;
//                stopThread = true;
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_POINTER_UP:
//                mode = NONE;
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN: // second finger
//                distBetweenFingers = spacing(event);
//                // the distance check is done to avoid false alarms
//                if (distBetweenFingers > 5f) {
//                    mode = TWO_FINGERS_DRAG;
//                }
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (mode == ONE_FINGER_DRAG) {
//                    PointF oldFirstFinger = firstFinger;
//                    firstFinger = new PointF(event.getX(), event.getY());
//                    scroll(oldFirstFinger.x - firstFinger.x);
//                    XYPlotConsumoMedio2.setDomainBoundaries(minXY.x, maxXY.x,
//                            BoundaryMode.FIXED);
//                    XYPlotConsumoMedio2.redraw();
//
//                } else if (mode == TWO_FINGERS_DRAG) {
//                    float oldDist = distBetweenFingers;
//                    distBetweenFingers = spacing(event);
//                    zoom(oldDist / distBetweenFingers);
//                    XYPlotConsumoMedio2.setDomainBoundaries(minXY.x, maxXY.x,
//                            BoundaryMode.FIXED);
//                    XYPlotConsumoMedio2.redraw();
//                }
//                break;
//        }
//        return true;
//    }
//    private void zoom(float scale) {
//        float domainSpan = maxXY.x - minXY.x;
//        float domainMidPoint = maxXY.x - domainSpan / 2.0f;
//        float offset = domainSpan * scale / 2.0f;
//
//        minXY.x = domainMidPoint - offset;
//        maxXY.x = domainMidPoint + offset;
//
//        minXY.x = Math.min(minXY.x, series[1].getX(series[1].size() - 1)
//                .floatValue());
//        maxXY.x = Math.max(maxXY.x, series[0].getX(1).floatValue());
//        clampToDomainBounds(domainSpan);
//    }
//
//    private void scroll(float pan) {
//        float domainSpan = maxXY.x - minXY.x;
//        float step = domainSpan / XYPlotConsumoMedio2.getWidth();
//        float offset = pan * step;
//        minXY.x = minXY.x + offset;
//        maxXY.x = maxXY.x + offset;
//        clampToDomainBounds(domainSpan);
//    }
//
//    private void clampToDomainBounds(float domainSpan) {
//        float leftBoundary = series[0].getX(0).floatValue();
//        float rightBoundary = series[1].getX(series[1].size() -1 ).floatValue();
//        // enforce left scroll boundary:
//        if (minXY.x < leftBoundary) {
//            minXY.x = leftBoundary;
//            maxXY.x = leftBoundary + domainSpan;
//        } else if (maxXY.x > series[1].getX(series[1].size() -1 ).floatValue() ) {
//            maxXY.x = rightBoundary;
//            minXY.x = rightBoundary - domainSpan;
//        }
//    }
//
//    private float spacing(MotionEvent event) {
//        float x = event.getX(0) - event.getX(1);
//        float y = event.getY(0) - event.getY(1);
//        return FloatMath.sqrt(x * x + y * y);
//    }
//
//
//
//    private double calcularMax(ArrayList<Number> listaNumber){
//
//        Double maximo = Double.MIN_VALUE;
//        for(Number number : listaNumber){
//            if(number.doubleValue() > maximo.doubleValue()){
//                maximo = number.doubleValue();
//            }
//        }
//        return maximo;
//    }
//
//    private double calcularMin(ArrayList<Number> listaNumber){
//
//        Double minimo = Double.MAX_VALUE;
//        for(Number number : listaNumber){
//            if(number.doubleValue() < minimo.doubleValue()){
//                minimo = number.doubleValue();
//            }
//        }
//        return minimo;
//    }
//
//
//
//}
