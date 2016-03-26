package com.jmgarzo.ratescar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.jmgarzo.bbdd.BBDDAjustesAplicacion;
import com.jmgarzo.bbdd.BBDDMarcas;
import com.jmgarzo.objects.Constantes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import ar.com.daidalos.afiledialog.FileChooserDialog;


/**
 * Created by jmgarzo on 30/01/15.
 */
public class DialogoImportarBBDD extends DialogFragment {
    private Context contextDialog;
    private String tipoImportacion;
    private DbxAccountManager mAccountManager;
    // private static final int REQUEST_LINK_TO_DBX = 1;  // This value is up to you


    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContextDialog());

        builder.setMessage(R.string.dialogo_importar_bbdd)
                .setPositiveButton(R.string.importar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (tipoImportacion.equals("sd")) {

                            FileChooserDialog fileChooserDialog = new FileChooserDialog(getContextDialog());
                            fileChooserDialog.show();

                            fileChooserDialog.addListener(new FileChooserDialog.OnFileSelectedListener() {
                                public void onFileSelected(Dialog source, File file) {

                                    source.hide();
                                    grabarBBDD(file.getPath().toString());


                                    BBDDMarcas bbddMarcas = new BBDDMarcas(contextDialog);

                                    bbddMarcas.recargarMarcas();

                                    Toast toast = Toast.makeText(getContextDialog(), getContextDialog().getString(R.string.toast_fichero_seleccionado) + file.getName(), Toast.LENGTH_LONG);
                                    toast.show();
                                }

                                public void onFileSelected(Dialog source, File folder, String name) {
                                    source.hide();

                                    Toast toast = Toast.makeText(getContextDialog(), getContextDialog().getString(R.string.toast_fichero_creado) + folder.getName() + "/" + name, Toast.LENGTH_LONG);
                                    toast.show();
                                }

                            });
                        } else if (tipoImportacion.equals("dropbox")) {

                            if (isNetworkAvailable()) {
                                mAccountManager = DbxAccountManager.getInstance(getContextDialog().getApplicationContext(), Constantes.APP_KEY, Constantes.APP_SECRET);


                                if (mAccountManager.hasLinkedAccount()) {
                                    DbxFileSystem dbxFs = null;
                                    try {
                                        dbxFs = DbxFileSystem.forAccount(mAccountManager.getLinkedAccount());
                                        DbxPath dbxPath = new DbxPath(Constantes.BBDD_NOMBRE);
                                        dbxFs.hasSynced();
                                        dbxFs.exists(dbxPath);

                                        List<DbxFileInfo> infos = dbxFs.listFolder(DbxPath.ROOT);
                                        //List<DbxFileInfo> infos2 = dbxFs.listFolder(dbxPath);

                                        if (dbxFs.exists(dbxPath)) {
                                            DbxFile filedb = null;
                                            filedb = dbxFs.open(new DbxPath(Constantes.BBDD_NOMBRE));


                                            byte[] readData = new byte[1024];
                                            FileInputStream fis = filedb.getReadStream();

                                            File ficheroDestino = new File(Environment.getExternalStorageDirectory(), Constantes.DIRECTORIOAPP + "/bbdd.temp");
                                            ficheroDestino.setReadable(true, false);
                                            FileOutputStream fos = new FileOutputStream(ficheroDestino);
                                            int i = fis.read(readData);

                                            while (i != -1) {
                                                fos.write(readData, 0, i);
                                                i = fis.read(readData);
                                            }
                                            filedb.close();
                                            fis.close();
                                            fos.close();

                                            grabarBBDD(Environment.getExternalStorageDirectory() + "/" + Constantes.DIRECTORIOAPP + "/bbdd.temp");

                                            borrarArchivoDeSD(Environment.getExternalStorageDirectory() + "/" + Constantes.DIRECTORIOAPP + "/bbdd.temp");
                                            Toast toast = Toast.makeText(getContextDialog(), "Importación Finalizada", Toast.LENGTH_LONG);
                                            toast.show();

                                            BBDDMarcas bbddMarcas = new BBDDMarcas(contextDialog);

                                            bbddMarcas.recargarMarcas();


                                        } else {

                                            //TODO mostrar aviso de que no existe copia de seguridad en la cuenta de dropBox facilitada
                                            Toast.makeText(contextDialog, getString(R.string.mensaje_no_existe_fichero_en_dropbox), Toast.LENGTH_LONG).show();

                                        }


                                    } catch (DbxException.Unauthorized unauthorized) {
                                        unauthorized.printStackTrace();
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (DbxException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Log.e(getContextDialog().getPackageName(), "No está abierta la conexión con dropbox");
                                    Toast.makeText(contextDialog, getString(R.string.mensaje_no_hay_conexion_dropbox), Toast.LENGTH_LONG).show();

                                }
                            } else {
                                Toast.makeText(contextDialog, getString(R.string.mensaje_no_hay_conexion), Toast.LENGTH_LONG).show();
                            }

                            dialog.cancel();

                        }


//                        Intent intent = new Intent(getActivity(), FrmImportarExportar.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
                        //dialog.cancel();

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getContextDialog(), FrmImportarExportar.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        dialog.cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public Context getContextDialog() {
        return contextDialog;
    }

    public void setContextDialog(Context context) {
        this.contextDialog = context;
    }

    public String getTipoImportacion() {
        return tipoImportacion;
    }

    public void setTipoImportacion(String tipoImportacion) {
        this.tipoImportacion = tipoImportacion;
    }

//    public DbxAccountManager getmAccountManager() {
//        return mAccountManager;
//    }
//
//    public void setmAccountManager(DbxAccountManager mAccountManager) {
//        this.mAccountManager = mAccountManager;
//    }

    private void grabarBBDD(String ruta) {
        BBDDSQLiteHelper bbddhelper = new BBDDSQLiteHelper(getContextDialog(), Constantes.BBDD_NOMBRE,
                null, Constantes.BBDD_VERSION);
        File sd = Environment.getExternalStorageDirectory();
        String backupSDPath = ruta;

        File backupSD = new File(backupSDPath, "");
        backupSD.setReadable(true);
        if (backupSD.canRead()) {
            try {
                bbddhelper.importDatabase(backupSDPath);
            } catch (IOException e) {
                Log.e(getContextDialog().getPackageName(), e.toString());
            }


        }

        //Reacondicionamos la BBDD si es anterior a la versión 16
        BBDDAjustesAplicacion bbddAjustesAplicacion = new BBDDAjustesAplicacion(contextDialog);
        bbddAjustesAplicacion.reacondicionarBBDDRepostajesCarreteraYPagos();
    }

    private void borrarArchivoDeSD(String ruta) {

        File fichero = new File(ruta, "");
        if (fichero.exists()) {
            fichero.delete();
        } else {
            Log.e(getContextDialog().getPackageName(), "El fichero a borrar no existe");
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) contextDialog.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
