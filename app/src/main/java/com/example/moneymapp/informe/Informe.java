package com.example.moneymapp.informe;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.moneymapp.MenuPrincipal;
import com.example.mentorapp.R;
import com.example.moneymapp.mapa.model.MapaModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class Informe extends AppCompatActivity {

    private String nombreDirectorio = "MisPdfs";
    private String nombreDocumento = "MisPdfs";
    private Spinner spinnerAnio, spinnerMes;
    private int activos, pasivos, entradas, salidas, riquezaNeta, flujoEfectivoNeto;
    private FirebaseUser user;
    private DatabaseReference informeDatabaseReference, mapaDatabaseReference;
    private String usuario, estado ;

    private boolean validaMapa, validarInforme;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe);

        spinnerAnio =findViewById(R.id.spinnerAño);

        spinnerMes =findViewById(R.id.spinnerMes);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.
                WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.
                        WRITE_EXTERNAL_STORAGE,},1000);
        }

        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();


        informeDatabaseReference = FirebaseDatabase.getInstance().getReference("informe");
        mapaDatabaseReference = FirebaseDatabase.getInstance().getReference("mapa");

        muestraSpinner();

        ingresarDatosInforme();
    }

    private void ingresarDatosInforme() {

        Query query= mapaDatabaseReference.orderByChild("correo").equalTo(usuario);

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    MapaModel mapa =datanapshot.getValue(MapaModel.class);
                    activos =Integer.parseInt(mapa.getActivosTotal());
                    pasivos=Integer.parseInt(mapa.getPasivosTotal());
                    entradas=Integer.parseInt(mapa.getEntradasTotal());
                    salidas=Integer.parseInt(mapa.getSalidasTotal());
                    riquezaNeta =Integer.parseInt(mapa.getRiquezaNeta());
                    flujoEfectivoNeto =Integer.parseInt(mapa.getFlujoEfectivoNeto());

                    validaMapa =true;



                }


                Calendar fecha= Calendar.getInstance();
                final int DIA_REGISTRO= fecha.get(Calendar.DAY_OF_MONTH);

                int diaRegistro=DIA_REGISTRO-1;


                if(validaMapa==false){
                    Query query= informeDatabaseReference.orderByChild("correo").equalTo(usuario);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Informes obj;
                            estado ="";
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj=datanapshot.getValue(Informes.class);
                                validarInforme=true;
                                estado=obj.getEstadoRegistro();
                            }

                            if (validarInforme==false){
                                Calendar fecha= Calendar.getInstance();
                                final int ANIO_REGISTRO= fecha.get(Calendar.YEAR);
                                final int MES_REGISTRO= fecha.get(Calendar.MONTH)+1;
                                final int DIA_INICIAL_REGISTRO= fecha.get(Calendar.DAY_OF_MONTH);

                                Informes nuevoinforme= new Informes();
                                nuevoinforme.setCorreo(usuario);
                                nuevoinforme.setDia(String.valueOf(DIA_INICIAL_REGISTRO));
                                nuevoinforme.setMes(String.valueOf(MES_REGISTRO));
                                nuevoinforme.setAnio(String.valueOf(ANIO_REGISTRO));
                                nuevoinforme.setActivos(String.valueOf(activos));
                                nuevoinforme.setPasivos(String.valueOf(pasivos));
                                nuevoinforme.setRiquezaNeta(String.valueOf(riquezaNeta));
                                nuevoinforme.setEntradas(String.valueOf(entradas));
                                nuevoinforme.setSalidas(String.valueOf(salidas));
                                nuevoinforme.setFlujoEfectivoNeto(String.valueOf(flujoEfectivoNeto));
                                nuevoinforme.setEstadoRegistro("hecho");

                                String clave= informeDatabaseReference.push().getKey();
                                informeDatabaseReference.child(clave).setValue(nuevoinforme);
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void irMenuPrincipal(View v){
        Intent irMenuPrincipal = new Intent(this, MenuPrincipal.class);
        startActivity(irMenuPrincipal);
    }



    public void generarInforme(View view){


        String anioSeleccionado= (String) spinnerAnio.getSelectedItem();
        String mesSeleccionado=(String) spinnerMes.getSelectedItem();

        if (mesSeleccionado.equals("enero")){
            mesSeleccionado="1";
        }else if (mesSeleccionado.equals("febrero")){
            mesSeleccionado="2";
        }else if (mesSeleccionado.equals("marzo")){
            mesSeleccionado="3";
        }else if (mesSeleccionado.equals("abril")){
            mesSeleccionado="4";
        }else if (mesSeleccionado.equals("mayo")){
            mesSeleccionado="5";
        }else if (mesSeleccionado.equals("junio")){
            mesSeleccionado="6";
        }else if (mesSeleccionado.equals("julio")){
            mesSeleccionado="7";
        }else if (mesSeleccionado.equals("agosto")){
            mesSeleccionado="8";
        }else if (mesSeleccionado.equals("septiembre")){
            mesSeleccionado="9";
        }else if (mesSeleccionado.equals("octubre")){
            mesSeleccionado="10";
        }else if (mesSeleccionado.equals("noviembre")){
            mesSeleccionado="11";
        }else {
            mesSeleccionado="12";
        }

        crearPdf(anioSeleccionado,mesSeleccionado);
    }

    private void crearPdf(final String anioSeleccionado , final String mesSeleccionado) {
        Query query= informeDatabaseReference.orderByChild("correo").equalTo(usuario);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Informes objInforme;
                String anioInforme=anioSeleccionado;
                String mesInforme=mesSeleccionado;
                String activos="", pasivos="", riquezaNeta="",entradas="", salidas="",
                        flujoEfectivoNeto="";
                Boolean validaInforme = null;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    objInforme=datanapshot.getValue(Informes.class);

                   if(anioInforme.equals(objInforme.getAnio())&& mesInforme.equals
                           (objInforme.getMes())){
                        validaInforme=true;
                        activos=objInforme.getActivos();
                        pasivos=objInforme.getPasivos();
                        riquezaNeta=objInforme.getRiquezaNeta();
                        entradas=objInforme.getEntradas();
                        salidas=objInforme.getSalidas();
                        flujoEfectivoNeto=objInforme.getFlujoEfectivoNeto();
                        Toast.makeText(Informe.this, "informe generado",
                                Toast.LENGTH_LONG).show();
                    }
                }

                if (validaInforme){
                    Document documento= new Document();
                    try {
                        File file=crearFichero(nombreDocumento);
                        FileOutputStream ficheroPDF= new FileOutputStream(file.getAbsolutePath());
                        PdfWriter writer =PdfWriter.getInstance(documento,ficheroPDF);
                        documento.open();
                        documento.add(new Paragraph("INFORME MENSUAL MAPA DE RIQUEZA"+"\n\n"));
                        documento.add(new Paragraph("MAPA DE RIQUEZA"+"\n\n"));
                        documento.add(new Paragraph("fecha de emisión: "+anioSeleccionado+"-"
                                +mesSeleccionado+"\n\n"));

                        //insertamos una tabla
                        PdfPTable tabla= new PdfPTable(6);
                        tabla.addCell("ACTIVOS");
                        tabla.addCell("PASIVOS");
                        tabla.addCell("RIQUEZA NETA");
                        tabla.addCell("ENTRADAS");
                        tabla.addCell("SALIDAS");
                        tabla.addCell("FLUJO EFECTIVO NETO");
                        tabla.addCell(activos);
                        tabla.addCell(pasivos);
                        tabla.addCell(riquezaNeta);
                        tabla.addCell(entradas);
                        tabla.addCell(salidas);
                        tabla.addCell(flujoEfectivoNeto);

                        documento.add(tabla);
                    }catch (DocumentException e){
                        Toast.makeText(Informe.this, "Error al generar documento!",
                                Toast.LENGTH_LONG).show();
                    }catch (IOException e){
                        Toast.makeText(Informe.this, "Error al generar documento!",
                                Toast.LENGTH_LONG).show();
                    }finally {
                        documento.close();
                    }
                }else {
                    Toast.makeText(Informe.this, "no existe Informe", Toast.LENGTH_LONG).
                            show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public File crearFichero(String nombreFichero){
        File ruta= obtenerRuta();
        File fichero=null;
        if (ruta!=null){
            fichero=new File(ruta,nombreFichero);
        }
        return fichero;
    }

    public File obtenerRuta(){
        File ruta = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            ruta = new File(Environment.getExternalStoragePublicDirectory(Environment.
                    DIRECTORY_DOWNLOADS), nombreDirectorio);
            if (ruta!=null){
                if (!ruta.mkdirs()){
                    if (!ruta.exists()){
                        return null;
                    }
                }
            }
        }
        return ruta;
    }

    public void muestraSpinner(){
        //ingreso de los valores al spiner año
        String [] anios= {"2020","2019","2018","2017","2016","2015"};
        ArrayList<String> arrayAnios= new ArrayList<>(Arrays.asList(anios));
        ArrayAdapter<String> arrayAdapterAnios= new ArrayAdapter<>(this,R.layout.style_spinner,
                arrayAnios);
        spinnerAnio.setAdapter(arrayAdapterAnios);
        spinnerAnio.setSelection(0);

        //ingreso de los valores al spiner mes
        String[] meses= new String[]{"enero","febrero", "marzo","abril","mayo","junio","julio",
                "agosto", "septiembre", "octubre", "noviembre", "diciembre"};
        ArrayList<String> arrayMeses= new ArrayList<>(Arrays.asList(meses));
        ArrayAdapter<String> arrayAdapterMeses= new ArrayAdapter<>(this,R.layout.
                style_spinner, arrayMeses);
        spinnerMes.setAdapter(arrayAdapterMeses);
        spinnerMes.setSelection(0);
    }



}
