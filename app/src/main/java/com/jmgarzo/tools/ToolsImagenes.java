//package com.jmgarzo.tools;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.util.Log;
//
//import com.jmgarzo.bbdd.BBDDCoches;
//import com.jmgarzo.bbdd.BBDDImagenes;
//import com.jmgarzo.objects.Constantes;
//import com.jmgarzo.objects.Imagen;
//
//import org.apache.http.util.ByteArrayBuffer;
//
//import java.io.BufferedInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.URL;
//import java.net.URLConnection;
//import java.util.ArrayList;
//
//import static android.support.v4.app.ActivityCompat.startActivityForResult;
//
///**
// * Created by jmgarzo on 13/03/15.
// */
//public class ToolsImagenes {
//
//    // convert from bitmap to byte array
//    public static byte[] getBytes(Bitmap bitmap) {
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
//        return stream.toByteArray();
//    }
//
//    // convert from byte array to bitmap
//    public static Bitmap getImage(byte[] image) {
//        return BitmapFactory.decodeByteArray(image, 0, image.length);
//    }
//
//
////
//
//    /**
//     * @param activity
//     * @param idCoche
//     * @param request
//     * @return String URL donde se encuentra la foto
//     * <p/>
//     * <p/>
//     * Se utilizará un método para mantener solo una foto por coche.
//     */
//
//
//    public static String sacarFoto(Activity activity, Integer idCoche, int request) {
//
//
//        boolean mExternalStorageAvailable = false;
//        boolean mExternalStorageWriteable = false;
//        String state = Environment.getExternalStorageState();
//
//        // COMPROBACION DEL ALMACENAMIENTO EXTERNO
//        if (Environment.MEDIA_MOUNTED.equals(state)) {
//            // Podremos leer y escribir en ella
//            mExternalStorageAvailable = mExternalStorageWriteable = true;
//        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
//            // En este caso solo podremos leer los datos
//            mExternalStorageAvailable = true;
//            mExternalStorageWriteable = false;
//        } else {
//            // No podremos leer ni escribir en ella
//            mExternalStorageAvailable = mExternalStorageWriteable = false;
//        }
//
//        long captureTime = System.currentTimeMillis();
//        String photoPath = "";
//
//        if (null == idCoche) {
//            BBDDCoches bbddCoches = new BBDDCoches(activity);
//            idCoche = bbddCoches.siguienteIdCoche();
//        }
//
//        if (mExternalStorageWriteable) {
//            photoPath = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + "Coches") + "/" + Constantes.TIPO_COCHE + idCoche.toString() + "_" + captureTime + ".jpg";
//            try {
//                Intent intent =
//                        new Intent("android.media.action.IMAGE_CAPTURE");
//                File photo = new File(photoPath);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
//                activity.startActivityForResult
//                        (Intent.createChooser(intent, "Capture Image"), request);
//            } catch (Exception e) {
//            }
//        }
//
//        return photoPath;
//    }
//
//
//    public static String seleccionarFotoGaleria(Activity activity, Integer idCoche, int request) {
//
//
//        boolean mExternalStorageAvailable = false;
//        boolean mExternalStorageWriteable = false;
//        String state = Environment.getExternalStorageState();
//
//        // COMPROBACION DEL ALMACENAMIENTO EXTERNO
//        if (Environment.MEDIA_MOUNTED.equals(state)) {
//            // Podremos leer y escribir en ella
//            mExternalStorageAvailable = mExternalStorageWriteable = true;
//        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
//            // En este caso solo podremos leer los datos
//            mExternalStorageAvailable = true;
//            mExternalStorageWriteable = false;
//        } else {
//            // No podremos leer ni escribir en ella
//            mExternalStorageAvailable = mExternalStorageWriteable = false;
//        }
//
//        long captureTime = System.currentTimeMillis();
//        String photoPath = "";
//
//        if (null == idCoche) {
//            BBDDCoches bbddCoches = new BBDDCoches(activity);
//            idCoche = bbddCoches.siguienteIdCoche();
//        }
//
//        if (mExternalStorageWriteable) {
//            //photoPath=activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/" + idCoche.toString()+ "_" + captureTime + ".jpg";
//            try {
////                Intent intent =
////                        new Intent("android.media.action.IMAGE_CAPTURE");
////                File photo = new File(photoPath);
////                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
////                activity.startActivityForResult
////                        (Intent.createChooser(intent, "Capture Image"), 1);
//
//
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                        MediaStore.Images.Media.CONTENT_TYPE);
//                activity.startActivityForResult
//                        (Intent.createChooser(intent, "Select Image"), request);
//            } catch (Exception e) {
//            }
//        }
//
//        return photoPath;
//    }
//
//
//    /**
//     * @param listaImagenes
//     * @param context
//     * @return numero de fotografías borradas
//     */
//    public static int limpiarFotosCocheSinUso(ArrayList<Imagen> listaImagenes, Context context) {
//        BBDDCoches bbddCoches = new BBDDCoches(context);
//        ArrayList<Integer> listaIdCoches = bbddCoches.getIdsCoche();
//
//        boolean mExternalStorageAvailable = false;
//        boolean mExternalStorageWriteable = false;
//        String state = Environment.getExternalStorageState();
//
//        if (!listaImagenes.isEmpty()) {
//
//
//            // COMPROBACION DEL ALMACENAMIENTO EXTERNO
//            if (Environment.MEDIA_MOUNTED.equals(state)) {
//                // Podremos leer y escribir en ella
//                mExternalStorageAvailable = mExternalStorageWriteable = true;
//            } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
//                // En este caso solo podremos leer los datos
//                mExternalStorageAvailable = true;
//                mExternalStorageWriteable = false;
//            } else {
//                // No podremos leer ni escribir en ella
//                mExternalStorageAvailable = mExternalStorageWriteable = false;
//            }
//
//            if (mExternalStorageAvailable) {
//
//                ArrayList<String> listaFotosEnSD = new ArrayList<String>();
//                String urlDirectorio = listaImagenes.get(0).getRuta();
//                char cUrl[] = urlDirectorio.toCharArray();
//                int ultimaBarra = 0;
//                for (int i = 0; i < urlDirectorio.length(); i++) {
//                    if (cUrl[i] == "/".toCharArray()[0]) {
//                        ultimaBarra = i;
//                    }
//
//
//                }
//
//                urlDirectorio = urlDirectorio.substring(0, ultimaBarra);
//
//
//                File dir = new File(urlDirectorio);
//                if (dir.isDirectory()) {
//                    String[] children = dir.list();
//
//                    for (int i = 0; i < children.length; i++) {
//                        if (!siExisteEnBBDD((urlDirectorio + "/" + children[i]), listaImagenes)) {
//                            ToolsFile.borrarFichero(urlDirectorio + "/" + children[i]);
//
//                        }
//                    }
//                }
//
//            }
//        }
//
//        return 0;
//    }
//
//    private static boolean siExisteEnBBDD(String url, ArrayList<Imagen> listaImagenes) {
//        boolean result = false;
//        for (Imagen img : listaImagenes) {
//            if (img.getRuta().equalsIgnoreCase(url)) {
//                result = true;
//            }
//        }
//        return result;
//    }
//
//
////    public static void grabarImagenEnBBDD(Imagen imagen,Context context){
////        BBDDImagenes bbddImagenes = new BBDDImagenes(context);
////        bbddImagenes.nuevaImagen(imagen);
////    }
//
//
////    public static  byte[] getImagenFromUrl(String url) {
////        try {
////            URL imageUrl = new URL(" file:///" + url);
////            URLConnection ucon = imageUrl.openConnection();
////
////            InputStream is = ucon.getInputStream();
////            BufferedInputStream bis = new BufferedInputStream(is);
////
////            ByteArrayBuffer baf = new ByteArrayBuffer(500);
////            int current = 0;
////            while ((current = bis.read()) != -1) {
////                baf.append((byte) current);
////            }
////
////            return baf.toByteArray();
////        } catch (Exception e) {
////            Log.d("ImageManager", "Error: " + e.toString());
////        }
////        return null;
////    }
//
//
////    public static void copiarImagenADirectorioAPP(Context context,String photoPath){
////
////        boolean mExternalStorageAvailable = false;
////        boolean mExternalStorageWriteable = false;
////        String state = Environment.getExternalStorageState();
////
////        // COMPROBACION DEL ALMACENAMIENTO EXTERNO
////        if (Environment.MEDIA_MOUNTED.equals(state)) {
////            // Podremos leer y escribir en ella
////            mExternalStorageAvailable = mExternalStorageWriteable = true;
////        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
////            // En este caso solo podremos leer los datos
////            mExternalStorageAvailable = true;
////            mExternalStorageWriteable = false;
////        } else {
////            // No podremos leer ni escribir en ella
////            mExternalStorageAvailable = mExternalStorageWriteable = false;
////        }
////
////
////
////        if(mExternalStorageWriteable){
////
////            File sd = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
////
////
////
////
////
////
////        }
////
////    }
//
//
//    public static int calculateInSampleSize(
//            BitmapFactory.Options options, int reqWidth, int reqHeight) {
//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//
//            final int halfHeight = height / 2;
//            final int halfWidth = width / 2;
//
//            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
//            // height and width larger than the requested height and width.
//            while ((halfHeight / inSampleSize) > reqHeight
//                    && (halfWidth / inSampleSize) > reqWidth) {
//                inSampleSize *= 2;
//            }
//        }
//
//        return inSampleSize;
//    }
//
//
//    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
//
//        // First decode with inJustDecodeBounds=true to check dimensions
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(path, options);
//
//        // Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//
//        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
//        return BitmapFactory.decodeFile(path, options);
//    }
//
//    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
//                                                         int reqWidth, int reqHeight) {
//
//        // First decode with inJustDecodeBounds=true to check dimensions
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(res, resId, options);
//
//        // Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//
//        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
//        return BitmapFactory.decodeResource(res, resId, options);
//    }
//
//
////    public static void copyImageAndResize(File src, File dst,int reqWidth,int reqHeight) throws IOException {
////
////        Bitmap resizedImage = decodeSampledBitmapFromFile(src.getAbsolutePath(),reqWidth,reqHeight);
////
////        InputStream in = new FileInputStream(src);
////        OutputStream out = new FileOutputStream(dst);
////        resizedImage.compress(Bitmap.CompressFormat.JPEG, 20, out);
////
////        // Transfer bytes from in to out
////        byte[] buf = new byte[1024];
////        int len;
////        while ((len = in.read(buf)) > 0) {
////            out.write(buf, 0, len);
////        }
////        in.close();
////        out.close();
////    }
//
//    public static void copyImageAndResize(File src, File dst, int reqWidth, int reqHeight) throws IOException {
//
//        //Bitmap resizedImage = decodeSampledBitmapFromFile(src.getAbsolutePath(),reqWidth,reqHeight);
//        Bitmap resizedImage = decodeSampledBitmapFromFile(src.getAbsolutePath(), reqWidth, reqHeight);
//        Bitmap resized = Bitmap.createScaledBitmap(resizedImage, reqWidth, reqHeight, true);
//
//
//        // InputStream in = new FileInputStream(src);
//        OutputStream out = new FileOutputStream(dst);
//        resized.compress(Bitmap.CompressFormat.JPEG, 100, out);
//
//        // Transfer bytes from in to out
////        byte[] buf = new byte[1024];
////        int len;
////        while ((len = in.read(buf)) > 0) {
////            out.write(buf, 0, len);
////        }
////        in.close();
////        out.close();
//    }
//
//}
