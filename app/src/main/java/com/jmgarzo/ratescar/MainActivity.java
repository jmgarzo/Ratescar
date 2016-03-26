package com.jmgarzo.ratescar;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.jmgarzo.bbdd.BBDDAjustesAplicacion;
import com.jmgarzo.bbdd.BBDDCategorias;
import com.jmgarzo.bbdd.BBDDCoches;
import com.jmgarzo.objects.Opcion;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {

    //Google Analytics
    public static GoogleAnalytics analytics;
    public static Tracker tracker;

//    private AdView adView;

    private ArrayList<Opcion> listaOpciones;
    private DrawerLayout miDrawerLayout;
    private ListView miListViewDrawer;

    private ActionBarDrawerToggle miDrawerToggle;

    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private boolean hayBBDD;
    private BBDDAjustesAplicacion bbddAjustesAplicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//      GOOGLE ANALYTICS
        //Se puede realizar una configuración mas completa. Comprobar en la documentación de google Analytics
        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);

        tracker = analytics.newTracker("UA-63539362-2"); // Replace with actual tracker/property Id
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);

        // Get tracker.




        setContentView(R.layout.activity_main);


//        /**PUBLICIDAD**/
//
//
//        // Crear adView.
////        adView = new AdView(this);
////        adView.setAdUnitId(MY_AD_UNIT_ID);
////        adView.setAdSize(AdSize.BANNER);
//
//        adView = new AdView(this);
//        adView.setAdUnitId("ca-app-pub-9433862228892990/5816419666");
//        adView.setAdSize(AdSize.BANNER);
//
//
//
//
//
//        // Buscar LinearLayout suponiendo que se le ha asignado
//        // el atributo android:id="@+id/mainLayout".
//        LinearLayout linearLayout= (LinearLayout) findViewById(R.id.banner);
//
//        // Añadirle adView.
//        linearLayout.addView(adView);
//
//        // Iniciar una solicitud genérica.
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)       // Emulador
//                .addTestDevice("16A4155AB1690691277DB1EB823AB891") // Mi teléfono
//                .build();
//
//        // Cargar adView con la solicitud de anuncio.
//        adView.loadAd(adRequest);
//
//
//
//
//
//        /*FIN PUBLIDAD*/


        bbddAjustesAplicacion = new BBDDAjustesAplicacion(this);
        String idioma = Locale.getDefault().getDisplayLanguage();
        if (!bbddAjustesAplicacion.getValorIdioma().equalsIgnoreCase(idioma)) {

            BBDDCategorias bbddCategorias = new BBDDCategorias(this);
            bbddCategorias.eliminarCategorias();
            bbddCategorias.rellenarCategorias();
        }


        BBDDCoches bbddCoches = new BBDDCoches(this);
        if (bbddCoches.getNumeroCoches() != 0) {
            hayBBDD = true;
        } else {
            hayBBDD = false;
        }


        mTitle = mDrawerTitle = getTitle();

        listaOpciones = new ArrayList<Opcion>();

        listaOpciones.add(new Opcion(getString(R.string.opcion_inicio), R.drawable.ic_home));
        listaOpciones.add(new Opcion(getString(R.string.opcion_coches), R.drawable.ic_coche_recomendado));
        listaOpciones.add(new Opcion(getString(R.string.opcion_repostajes), R.drawable.ic_senal_gasolinera));
        listaOpciones.add(new Opcion(getString(R.string.opcion_mantenimientos), R.drawable.ic_senal_taller));
//        listaOpciones.add(new Opcion(getString(R.string.opcion_recambios), R.drawable.ic_machine2));
        listaOpciones.add(new Opcion(getString(R.string.opcion_itv), R.drawable.ic_itv));
        listaOpciones.add(new Opcion(getString(R.string.opcion_peajes), R.drawable.ic_peaje));
        listaOpciones.add(new Opcion(getString(R.string.opcion_seguros), R.drawable.ic_seguro));
        listaOpciones.add(new Opcion(getString(R.string.opcion_impuesto_circulacion), R.drawable.ic_impuestos));
        listaOpciones.add(new Opcion(getString(R.string.opcion_estadisticas), R.drawable.ic_graficotorta));
        listaOpciones.add(new Opcion(getString(R.string.opcion_ajustes), R.drawable.ic_machine2));


        miDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        miListViewDrawer = (ListView) findViewById(R.id.left_drawer);

        //miListViewDrawer.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, opciones));
        AdaptadorNavigation adptadorNavigation = new AdaptadorNavigation(this);
        miListViewDrawer.setAdapter(adptadorNavigation);
        miListViewDrawer.setOnItemClickListener(new MiDrawerItemCLickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        miDrawerToggle = new ActionBarDrawerToggle(this, miDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActionBar().setTitle(mTitle);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
            }

        };

        miDrawerLayout.setDrawerListener(miDrawerToggle);

        // Importante no olvidar, si es la primera vez que se inicia, se carga
        // el item 0


        if (savedInstanceState == null) {
            selectItem(0);
        }


        Fragment fragment = new FragmentRepostajes();
        FragmentManager fragmentManager = getFragmentManager();
        if (null != getIntent().getStringExtra("Repostaje") && getIntent().getStringExtra("Repostaje").equals("repostaje")) {
            fragment = new FragmentRepostajes();
            fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("Repostaje").commit();
        } else if (null != getIntent().getStringExtra("Coche") && getIntent().getStringExtra("Coche").equals("coche")) {
            fragment = new FragmentCoches();
            fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("Coche").commit();

        } else if (null != getIntent().getStringExtra("Mantenimiento") && getIntent().getStringExtra("Mantenimiento").equals("mantenimiento")) {
            fragment = new FragmentMantenimientos();
            fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("Mantenimiento").commit();
        } else if (null != getIntent().getStringExtra("Recambio") && getIntent().getStringExtra("Recambio").equals("recambio")) {
            fragment = new FragmentRecambios();
            fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("Recambio").commit();
        } else if (null != getIntent().getStringExtra("Itv") && getIntent().getStringExtra("Itv").equals("itv")) {
            fragment = new FragmentItv();
            fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("Itv").commit();
        } else if (null != getIntent().getStringExtra("Peaje") && getIntent().getStringExtra("Peaje").equals("peaje")) {
            fragment = new FragmentPeaje();
            fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("Peaje").commit();
        } else if (null != getIntent().getStringExtra("Seguro") && getIntent().getStringExtra("Seguro").equals("seguro")) {
            fragment = new FragmentSeguros();
            fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("Seguro").commit();
        } else if (null != getIntent().getStringExtra("ImpuestoCirculacion") && getIntent().getStringExtra("ImpuestoCirculacion").equals("impuestoCirculacion")) {
            fragment = new FragmentImpuestoCirculacion();
            fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("ImpuestoCirculacion").commit();
        } else if (null != getIntent().getStringExtra("Ajustes") && getIntent().getStringExtra("Ajustes").equals("ajustes")) {
            fragment = new FragmentAjustes();
            fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("Ajustes").commit();
        } else if (null != getIntent().getStringExtra("Estadisticas") && getIntent().getStringExtra("Estadisticas").equals("estadisticas")) {
            fragment = new FragmentEstadisticas();
            fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("Estadisticas").commit();
        }
        if (null != getIntent().getStringExtra("Inicio") && getIntent().getStringExtra("Inicio").equals("inicio")) {
            fragment = new FragmentInicio();
            fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("Inicio").commit();
        }


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        miDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        miDrawerToggle.onConfigurationChanged(newConfig);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (miDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    private class MiDrawerItemCLickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ArrayList<String> errores = new ArrayList<String>();
            Fragment fragment = null;
            FragmentManager fragmentManager = getFragmentManager();


            switch (position) {
                case 0:
                    fragment = new FragmentInicio();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("Inicio").commit();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    break;
                case 1:
                    fragment = new FragmentCoches();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("Coche").commit();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

                    break;
                case 2:
                    if (hayBBDD) {
                        fragment = new FragmentRepostajes();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("Repostaje").commit();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    } else {
                        mostrarAvisoBBDD(getString(R.string.error_no_hay_bbdd_repostaje), fragmentManager, fragment);
                    }

                    break;
                case 3:
                    if (hayBBDD) {
                        fragment = new FragmentMantenimientos();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("Mantenimiento").commit();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    } else {
                        mostrarAvisoBBDD(getString(R.string.error_no_hay_bbdd_mantenimiento), fragmentManager, fragment);

                    }
                    break;
//                case 4:
//                    if (hayBBDD) {
//                        fragment = new FragmentRecambios();
//                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("Recambio").commit();
//                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
//                    } else {
//                        mostrarAvisoBBDD(getString(R.string.error_no_hay_bbdd_recambio), fragmentManager, fragment);
//                    }
//                    break;
                case 4:
                    if (hayBBDD) {
                        fragment = new FragmentItv();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("Itv").commit();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    } else {
                        mostrarAvisoBBDD(getString(R.string.error_no_hay_bbdd_itv), fragmentManager, fragment);
                    }
                    break;
                case 5:
                    if (hayBBDD) {
                        fragment = new FragmentPeaje();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("Peaje").commit();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    } else {
                        mostrarAvisoBBDD(getString(R.string.error_no_hay_bbdd_peaje), fragmentManager, fragment);
                    }
                    break;
                case 6:
                    if (hayBBDD) {
                        fragment = new FragmentSeguros();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("Seguro").commit();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    } else {
                        mostrarAvisoBBDD(getString(R.string.error_no_hay_bbdd_seguro), fragmentManager, fragment);
                    }
                    break;

                case 7:

                    if (hayBBDD) {
                        fragment = new FragmentImpuestoCirculacion();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("ImpuestoCirculacion").commit();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    } else {
                        mostrarAvisoBBDD(getString(R.string.error_no_hay_bbdd_impuesto_circulacion), fragmentManager, fragment);

                    }
                    break;

                case 8:
                    if (hayBBDD) {
                        fragment = new FragmentEstadisticas();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("Estadisticas").commit();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    } else {
                        mostrarAvisoBBDD(getString(R.string.error_no_hay_bbdd_estadisticas), fragmentManager, fragment);

                    }
                    break;

                case 9:
                    fragment = new FragmentAjustes();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("Ajustes").commit();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    break;


                default:
                    fragment = new FragmentInicio();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    break;
            }


            miListViewDrawer.setItemChecked(position, true);
            setTitle(listaOpciones.get(position).getNombre());
            miDrawerLayout.closeDrawer(miListViewDrawer);

        }


    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = new FragmentInicio();

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        miListViewDrawer.setItemChecked(position, true);
        setTitle(listaOpciones.get(position).getNombre());
        miDrawerLayout.closeDrawer(miListViewDrawer);
    }


    class AdaptadorNavigation extends ArrayAdapter<Opcion> {

        Activity context;

        AdaptadorNavigation(Activity context) {
            super(context, R.layout.list_opciones, listaOpciones);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView;
            ViewHolder holder;

            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(R.layout.list_opciones, null);


                holder = new ViewHolder();
                holder.icono = (ImageView) item.findViewById(R.id.ImgIcono);
                holder.nombre = (TextView) item.findViewById(R.id.lbOpcion);

                item.setTag(holder);

            } else {
                holder = (ViewHolder) item.getTag();
            }

            Opcion opcion = listaOpciones.get(position);


            if (null != opcion) {
                holder.icono.setImageResource(opcion.getIcono());
                holder.nombre.setText(opcion.getNombre());


            }

            return (item);
        }
    }

    static class ViewHolder {
        ImageView icono;
        TextView nombre;


    }

    public void mostrarAvisoBBDD(String error, FragmentManager fragmentManager, Fragment fragment) {
        ArrayList<String> errores = new ArrayList<String>();
        errores.add(error);
        FragmentManager fm = getFragmentManager();
        Bundle b = new Bundle();
        b.putStringArrayList("errores", errores);
        DialogoIniciarBBDD dialogo = new DialogoIniciarBBDD();
        dialogo.setArguments(b);
        dialogo.show(fm, "tagAlerta");

        fragment = new FragmentInicio();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BBDDCoches bbddCoches = new BBDDCoches(this);
        if (bbddCoches.getNumeroCoches() != 0) {
            hayBBDD = true;
        } else {
            hayBBDD = false;
        }
    }

    @Override
    public void onBackPressed() {

        if (miDrawerLayout.isDrawerOpen(miListViewDrawer)) {
            miDrawerLayout.closeDrawer(miListViewDrawer);
        } else {
            super.onBackPressed();
        }
    }
}
