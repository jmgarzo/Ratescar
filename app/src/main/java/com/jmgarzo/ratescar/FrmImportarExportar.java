package com.jmgarzo.ratescar;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileStatus;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.dropbox.sync.android.DbxSyncStatus;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.jmgarzo.bbdd.BBDDMarcas;
import com.jmgarzo.objects.Constantes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

import ar.com.daidalos.afiledialog.FileChooserDialog;


public class FrmImportarExportar extends Activity {

    private AdView adView;


    private Button btnExportar;
    private Button btnImportar;
    private Button btnExportarDropBox;
    private Button btnImportarDropBox;
    private CheckBox chkDropbox;
    //    private final String APP_KEY = "aep1aa93gs1iauo";
//    private final String APP_SECRET = "0y29g0sykinvy5f";
    private TextView lbBytesTrasferidos;


    private Button mLinkButton;
    private DbxAccountManager mAccountManager;
    // private DbxAccountManager mDbxAcctMgr;
    private static final int REQUEST_LINK_TO_DBX = 0;  // This value is up to you


    private ProgressDialog pDialog;
    private SubirDropboxAsincronaDialog subir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_importar_exportar);

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

        mAccountManager = DbxAccountManager.getInstance(getApplicationContext(), Constantes.APP_KEY, Constantes.APP_SECRET);


//        mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(), APP_KEY, APP_SECRET);
        lbBytesTrasferidos = (TextView) findViewById(R.id.lbBytesTrasferidos);

        btnExportar = (Button) findViewById(R.id.btnExportar);

        btnExportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportar();
            }
        });

        btnImportar = (Button) findViewById(R.id.btnImportar);
        btnImportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importar();
            }
        });

        btnExportarDropBox = (Button) findViewById(R.id.btnExportarDropBox);
        btnExportarDropBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportarDropBox();
            }
        });

        btnImportarDropBox = (Button) findViewById(R.id.btnImportarDropBox);
        btnImportarDropBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importarDropBox();
            }
        });


        chkDropbox = (CheckBox) findViewById(R.id.chkDropbox);
        if (mAccountManager.hasLinkedAccount()) {
            chkDropbox.setChecked(true);
            btnExportarDropBox.setEnabled(true);
            btnImportarDropBox.setEnabled(true);
        } else {
            chkDropbox.setChecked(false);
            btnExportarDropBox.setEnabled(false);
            btnImportarDropBox.setEnabled(false);
        }

        chkDropbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mAccountManager.hasLinkedAccount()) {
                        btnExportarDropBox.setEnabled(true);
                        btnImportarDropBox.setEnabled(true);

                    } else {
                        mAccountManager.startLink(FrmImportarExportar.this, REQUEST_LINK_TO_DBX);

                    }

                } else {
                    if (mAccountManager.hasLinkedAccount()) {
                        mAccountManager.unlink();
                    }
                    btnExportarDropBox.setEnabled(false);
                    btnImportarDropBox.setEnabled(false);
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mAccountManager.hasLinkedAccount()) {
            chkDropbox.setChecked(true);
            btnExportarDropBox.setEnabled(true);
            btnImportarDropBox.setEnabled(true);
        } else {
            chkDropbox.setChecked(false);
            btnExportarDropBox.setEnabled(false);
            btnImportarDropBox.setEnabled(false);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_frm_importar_exportar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Exporta la BBDD actual a la tarjeta SD
     */
    private void exportar() {


        //Exportar BBDD
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "/data/com.jmgarzo.ratescar/databases/" + Constantes.BBDD_NOMBRE;
                String backupDBDirectory = Constantes.DIRECTORIOAPP;
                String backupDBFile = Constantes.BBDD_NOMBRE;
                String backupDBPath = backupDBDirectory.concat("/").concat(backupDBFile);

                File fCurrentDB = new File(data, currentDBPath);
                File fBackupDB = new File(sd, backupDBPath);
                fBackupDB.setReadable(true, false);
                fBackupDB.setWritable(true);
                fBackupDB.setExecutable(true);


                if (fCurrentDB.exists()) {

                    final File newFile = new File(sd.toString().concat("/").concat(backupDBDirectory));
                    if (newFile.mkdir()) {
                        newFile.setReadable(true, false);
//                        MediaScannerConnection.scanFile(this, new String[]{sd.toString().concat("/").concat(backupDBDirectory)}, null, null);
                        //MediaScannerConnection.scanFile(this, new String[]{sd.toString().concat("/").concat(backupDBDirectory)}, new String[]{"application/vnd.google-apps.folder"},null);
//                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
//                                + Environment.getExternalStorageDirectory())));
//                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));

                        Log.i(getPackageName(), "Se ha creado el directorio");
                        copiarFichero(fCurrentDB, fBackupDB);
                    } else {
                        if (newFile.exists()) {
                            if (fBackupDB.exists()) {
                                File fBackupOld = new File(sd, backupDBPath.concat("~"));
                                copiarFichero(fBackupDB, fBackupOld);
                                fBackupDB.delete();
                            }
                            copiarFichero(fCurrentDB, fBackupDB);
                        } else {
                            Log.e(getPackageName(), "Ha fallado al crear el directorio");
                        }
                    }
                }
            }

        } catch (Exception e) {
            Log.e(getLocalClassName(), e.toString());
        }

        //Exportar IMAGENES
        //TODO Estoy aquí

        boolean sdDisponible = false;
        boolean sdAccesoEscritura = false;

//Comprobamos el estado de la memoria externa (tarjeta SD)
        String estado = Environment.getExternalStorageState();

        if (estado.equals(Environment.MEDIA_MOUNTED)) {
            sdDisponible = true;
            sdAccesoEscritura = true;
        } else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            sdDisponible = true;
            sdAccesoEscritura = false;
        } else {
            sdDisponible = false;
            sdAccesoEscritura = false;
        }

        if (sdAccesoEscritura) {
            File ruta_sd = Environment.getExternalStorageDirectory();
            File file2 = new File(getExternalFilesDir("Documentos"), "prueba.txt");
        }


    }


    /**
     * Importa la BBDD seleccionada al la aplicación
     */
    private void importar() {

        DialogoImportarBBDD dialogoImportarBBDD = new DialogoImportarBBDD();
        dialogoImportarBBDD.setContextDialog(this);
        dialogoImportarBBDD.setTipoImportacion("sd");
        dialogoImportarBBDD.show(getFragmentManager(), "Importar");

//        FileChooserDialog dialog = new FileChooserDialog(this);
//        dialog.show();

//        dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {
//            public void onFileSelected(Dialog source, File file) {
//
//                source.hide();
//                grabarBBDD(file.getPath().toString());
//                Toast toast = Toast.makeText(source.getContext(), "File selected: " + file.getName(), Toast.LENGTH_LONG);
//                toast.show();
//
//
//            }
//
//            public void onFileSelected(Dialog source, File folder, String name) {
//                source.hide();
//                Toast toast = Toast.makeText(source.getContext(), "File created: " + folder.getName() + "/" + name, Toast.LENGTH_LONG);
//                toast.show();
//
//            }
//        });


    }

    private void exportarDropBox() {


        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();

        if (sd.canRead()) {
            exportar();
            String currentDBPath = "/data/com.jmgarzo.ratescar/databases/" + Constantes.BBDD_NOMBRE;
            File currentDB = new File(data, currentDBPath);
            if (currentDB.exists()) {
                if (mAccountManager.hasLinkedAccount()) {
                    try {
                        DbxFileSystem dbxFs = DbxFileSystem.forAccount(mAccountManager.getLinkedAccount());
                        List<DbxFileInfo> infos = dbxFs.listFolder(DbxPath.ROOT);
                        DbxPath dbxPath = new DbxPath(Constantes.BBDD_NOMBRE);
                        if (dbxFs.exists(dbxPath)) {
                            copiarFicheroDropBox(dbxFs, dbxPath);
                            dbxFs.delete(dbxPath);
                        }
                        DbxFile dxFile = dbxFs.create(new DbxPath(Constantes.BBDD_NOMBRE));

                        //subirFicheroDropBox(currentDB, dxFile);
                        //dxFile.writeFromExistingFile(currentDB, false);
                        DbxFileStatus dbxFileStatus = dxFile.getSyncStatus();

                        if (dbxFileStatus.pending == DbxFileStatus.PendingOperation.UPLOAD) {
//                                Long bytesTransferred = dbxFileStatus.bytesTransferred;


//                                lbBytesTrasferidos.setText(bytesTransferred.toString());

                            pDialog = new ProgressDialog(FrmImportarExportar.this);
                            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            pDialog.setMessage(getString(R.string.mensaje_exportando));
                            pDialog.setCancelable(true);
                            pDialog.setMax(100);


                            subir = new SubirDropboxAsincronaDialog(currentDB, dxFile);
                            subir.execute();

                        }


                    } catch (DbxException.Unauthorized unauthorized) {
                        unauthorized.printStackTrace();
                    } catch (DbxException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        Log.e(getLocalClassName().toString(), e.toString());
                    }
                } else {
                    mAccountManager.startLink(this, REQUEST_LINK_TO_DBX);
                }

            }

        }

    }


    private void importarDropBox() {

//        mAccountManager = DbxAccountManager.getInstance(getApplicationContext(), APP_KEY, APP_SECRET);

        DialogoImportarBBDD dialogoImportarBBDD = new DialogoImportarBBDD();
        //dialogoImportarBBDD.setmAccountManager(mAccountManager);
        dialogoImportarBBDD.setContextDialog(this);
        dialogoImportarBBDD.setTipoImportacion("dropbox");
        dialogoImportarBBDD.show(getFragmentManager(), "Importar");


//        if (mAccountManager.hasLinkedAccount()) {
//            DbxFileSystem dbxFs = null;
//            try {
//                dbxFs = DbxFileSystem.forAccount(mAccountManager.getLinkedAccount());
//
//                if (dbxFs.exists(new DbxPath(Constantes.BBDD_NOMBRE))) {
//
//                    DbxFile filedb = dbxFs.open(new DbxPath(Constantes.BBDD_NOMBRE));
//
//                    byte[] readData = new byte[1024];
//                    FileInputStream fis = filedb.getReadStream();
//
//                    File ficheroDestino = new File(Environment.getExternalStorageDirectory(), Constantes.DIRECTORIOAPP + "bbdd.temp");
//                    ficheroDestino.setReadable(true, false);
//                    FileOutputStream fos = new FileOutputStream(ficheroDestino);
//                    int i = fis.read(readData);
//
//                    while (i != -1) {
//                        fos.write(readData, 0, i);
//                        i = fis.read(readData);
//                    }
//                    filedb.close();
//                    fis.close();
//                    fos.close();
//
//                    grabarBBDD(Environment.getExternalStorageDirectory() + "/" + Constantes.DIRECTORIOAPP + "/bbdd.temp");
//                    borrarArchivoDeSD(Environment.getExternalStorageDirectory() + "/" + Constantes.DIRECTORIOAPP + "/bbdd.temp");
//
//                } else {
//
//                    //TODO mostrar aviso de que no existe copia de seguridad en la cuenta de dropBox facilitada
//
//                }
//
//
//            } catch (DbxException.Unauthorized unauthorized) {
//                unauthorized.printStackTrace();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (DbxException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            mAccountManager.startLink(this, REQUEST_LINK_TO_DBX);
//        }

    }


    private void borrarArchivoDeSD(String ruta) {

        File fichero = new File(ruta, "");
        if (fichero.exists()) {
            fichero.delete();
        } else {
            Log.e(getPackageName(), "El fichero a borrar no existe");
        }
    }


    private void copiarFicheroDropBox(DbxFileSystem dbxFs, DbxPath dbxPath) {

        DbxFile filedb = null;
        File ficheroTemportal = new File(Environment.getExternalStorageDirectory(), Constantes.DIRECTORIOAPP + "bbdd.temp");

        try {
            filedb = dbxFs.open(dbxPath);
            //leemos el fichero original del dropBox
            byte[] readData = new byte[1024];
            FileInputStream fis = filedb.getReadStream();

            ficheroTemportal.setReadable(true, false);
            FileOutputStream fos = new FileOutputStream(ficheroTemportal);
            int i = fis.read(readData);

            while (i != -1) {
                fos.write(readData, 0, i);
                i = fis.read(readData);
            }
            filedb.close();
            fis.close();
            fos.close();
        } catch (DbxException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Creamos el nuevo fichero en el DropBox y
        if (ficheroTemportal.exists()) {
            try {
                DbxPath dbxPathBackup = new DbxPath(Constantes.BBDD_NOMBRE.concat("~"));

                if (dbxFs.exists(dbxPathBackup)) {
                    dbxFs.delete(dbxPathBackup);


                }

                DbxFile dxFile = dbxFs.create(dbxPathBackup);
                dxFile.writeFromExistingFile(ficheroTemportal, false);
                dxFile.close();
                ficheroTemportal.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    private void grabarBBDD(String ruta) {
        BBDDSQLiteHelper bbddhelper = new BBDDSQLiteHelper(this, Constantes.BBDD_NOMBRE,
                null, Constantes.BBDD_VERSION);
        File sd = Environment.getExternalStorageDirectory();
        String backupSDPath = ruta;

        File backupSD = new File(backupSDPath, "");

        if (backupSD.canRead()) {
            try {
                bbddhelper.importDatabase(backupSDPath);
            } catch (IOException e) {
                Log.e(getPackageName(), e.toString());
            }

        }
    }

    private void copiarFichero(File ficheroOrigen, File ficheroDestino) {
        FileChannel src = null;
        FileChannel dst = null;
        try {
            src = new FileInputStream(ficheroOrigen).getChannel();
            dst = new FileOutputStream(ficheroDestino).getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


//    public void onClickLinkToDropbox(View view) {
//        mAccountManager.startLink(this, REQUEST_LINK_TO_DBX);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LINK_TO_DBX) {
            if (resultCode == Activity.RESULT_OK) {
                // ... Start using Dropbox files.
            } else {
                // ... Link failed or was cancelled by the user.
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private class SubirDropboxAsincronaDialog extends AsyncTask<Void, Integer, Boolean> {

        private File file;
        private DbxFile dbxFile;
        boolean result = true;

        public SubirDropboxAsincronaDialog(File file, DbxFile dbxFile) {
            this.file = file;
            this.dbxFile = dbxFile;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

//            for(int i=1; i<=10; i++) {
//                tareaLarga();
//
//                publishProgress(i*10);
//
//                if(isCancelled())
//                    break;
//            }

            try {
                dbxFile.writeFromExistingFile(file, false);
                DbxFileStatus dbxFileStatus = dbxFile.getSyncStatus();
                Long totalBytes = dbxFileStatus.bytesTotal;
                int count = 0;
                while (dbxFileStatus.pending == DbxFileStatus.PendingOperation.UPLOAD) {
                    Long progres = dbxFileStatus.bytesTransferred * 100 / totalBytes;
                    publishProgress(progres.intValue());
                    dbxFileStatus = dbxFile.getSyncStatus();
                    Thread.currentThread().sleep(1);
                    count++;
                    if (count > 2) {
//                        this.cancel(true);
//                        result= false;
                        break;

                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                dbxFile.close();
            }


            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();

            pDialog.setProgress(progreso);
        }

        @Override
        protected void onPreExecute() {

            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    SubirDropboxAsincronaDialog.this.cancel(true);
                }
            });

            pDialog.setProgress(0);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                pDialog.dismiss();
                Toast.makeText(FrmImportarExportar.this, getString(R.string.mensaje_exportado_dropbox_completado), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(FrmImportarExportar.this, getString(R.string.mensaje_exportando_dropbox_cancelado), Toast.LENGTH_LONG).show();
        }
    }


}
