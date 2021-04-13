package com.example.moneymapp.pasivos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneymapp.MenuPrincipal;
import com.example.mentorapp.R;
import com.example.moneymapp.pasivos.model.Pasivo;
import com.example.moneymapp.pasivos.model.TotalPasivo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Pasivos extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private FirebaseUser user;
    private EditText edt_ingresopasivo, edt_montop;
    private Button btn_ingresar;
    private String usuario, x;
    private static final int REQUEST_CODE=1;
    private int c;
    private DatabaseReference mDatabase, nDatabase;
    private TotalPasivo nuevototalpasivo;

    private TextView pruebapasivos;

    private List<Modelo> mLista= new ArrayList<>();
    private ListView mListview;
   // private ArrayAdapter<String>mAdapter;
    private ListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasivos);

        edt_ingresopasivo=findViewById(R.id.edTNamep);

        edt_montop=findViewById(R.id.edtMontop);

        btn_ingresar=findViewById(R.id.btnAgregarpasivos);
        mListview=findViewById(R.id.listPasivos);
        mListview.setOnItemClickListener(this);




        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();

        //referencia para acceso a la tabla pasivos de la bbdd
        mDatabase = FirebaseDatabase.getInstance().getReference("Pasivos");
        nDatabase = FirebaseDatabase.getInstance().getReference("TotalPasivos");

        //agrega la lista todos los pasivos ya existentes
        getPasivos();
        nuevoTotalPasivos();
        sumaTotalPasivos();


    }
    //sumatoria de pasivos
    private void sumaTotalPasivos() {
        Query q2=mDatabase.orderByChild("correo").equalTo(usuario);

        q2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
                x="";
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Pasivo nuevo=datanapshot.getValue(Pasivo.class);
                    int m=Integer.parseInt(nuevo.getGananciaPasivos());
                    c=c+m;
                }

                if(c!=0){
                    //pruebapasivos.setText(String.valueOf(c));
                    ingresaTotalGananciasPasivos(c);
                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //método para ingresar el total de las ganancias pasivas a total pasivos
    public void ingresaTotalGananciasPasivos(final int total){
        Query q3=nDatabase.orderByChild("correo").equalTo(usuario);

        q3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String gtotal=String.valueOf(total);
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    TotalPasivo m =datanapshot.getValue(TotalPasivo.class);
                    //modificacion
                    String clave=datanapshot.getKey();
                    nDatabase.child(clave).child("ganancia_total_pasivos").setValue(gtotal);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //método para crear un nuevo total pasivos
    private void nuevoTotalPasivos() {
        Query q2=nDatabase.orderByChild("correo").equalTo(usuario);

        q2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    c++;
                    //espacio para enviar toda la info a un registro en la bbdd para crear informes
                }

                if(c!=0){

                }else{
                    //aquí creo un mapa nuevo con la fecha de inicio y valores de 0
                    Calendar fecha= Calendar.getInstance();
                    int anio_inicio= fecha.get(Calendar.YEAR);
                    int mes_inicio= fecha.get(Calendar.MONTH)+1;
                    int dia_inicio= fecha.get(Calendar.DAY_OF_MONTH);

                    int z=dia_inicio-1;
                    String x= String.valueOf(z);

                    nuevototalpasivo= new TotalPasivo();
                    nuevototalpasivo.setCorreo(usuario);
                    nuevototalpasivo.setDiaInicio(x);
                    nuevototalpasivo.setMesInicio(String.valueOf(mes_inicio));
                    nuevototalpasivo.setAnioInicio(String.valueOf(anio_inicio));
                    nuevototalpasivo.setGananciaTotalPasivos("0");
                    String clave= nDatabase.push().getKey();
                    nDatabase.child(clave).setValue(nuevototalpasivo);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //método que separa por miles los números
    public String separador(String num){
        int x=Integer.parseInt(num);
        String str = String.format("%,d", x);
        String numfinal=str.replace(",",".");
        return numfinal;
    }


    //método que verifica si ya existe este ingreso pasivo y si no existe lo crea
    public void verificaPasivo(View v) {
        //se consulta a la bbdd para validar si existe ya el usuario y si no existe crear otro
        Query q2=mDatabase.orderByChild("nombre_pasivo").equalTo(edt_ingresopasivo.getText().toString());

        q2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
                Pasivo obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj=datanapshot.getValue(Pasivo.class);
                    if (usuario.equals(obj.getCorreo())){
                        c++;
                        Toast.makeText(Pasivos.this,"error! este pasivo ya existe!",Toast.LENGTH_LONG).show();
                        edt_ingresopasivo.setText("");
                        edt_montop.setText("");
                    }
                }

                if(c!=0){
                    //ya existe el usuario registrado

                }else{
                    if (edt_ingresopasivo.getText().toString().isEmpty() && edt_montop.getText().toString().isEmpty()){
                        Toast.makeText(Pasivos.this,"error! debe llenar los campos!",Toast.LENGTH_LONG).show();
                    }else if (edt_ingresopasivo.getText().toString().isEmpty() ){
                        Toast.makeText(Pasivos.this,"error! debe llenar los campos!",Toast.LENGTH_LONG).show();
                    }else if (edt_montop.getText().toString().isEmpty()){
                        Toast.makeText(Pasivos.this,"error! debe llenar los campos!",Toast.LENGTH_LONG).show();
                    }else {
                        agregarPasivos1();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    //método que muestra los pasivos ya existentes
    public void getPasivos(){
        Query q2=mDatabase.orderByChild("correo").equalTo(usuario);

        q2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
                Pasivo obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj=datanapshot.getValue(Pasivo.class);
                        c++;
                        Modelo modelo=new Modelo();
                        String x=obj.getNombrePasivo();
                        String y=separador(obj.getMontoInversion());
                        mostrarLista(x,y);
                }

                if(c!=0){
                    //ya existe el usuario registrado

                }else{
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }


    //método para ir a activity MenuPrincipal
    public void irMenuPrincipal(View v){
        Intent iramenuprincipal = new Intent(this, MenuPrincipal.class);
        startActivity(iramenuprincipal);
    }



    //metodo para agregar los pasivos ya existentes y agregarlos a la lista
    public void mostrarLista(String nombrepasivo, String montopasivo){
        Modelo modelo=new Modelo();
        modelo.setNombre(nombrepasivo);
        modelo.setMonto(montopasivo);

        mLista.add(modelo);
        edt_ingresopasivo.getText().clear();
        edt_montop.getText().clear();
        mAdapter=new ListAdapter(this,R.layout.item_row, mLista);
        mListview.setAdapter(mAdapter);

    }

    public void agregarPasivos1(){
        //aquí se agrega el nuevo pasivo al listview
        String texto=edt_ingresopasivo.getText().toString().trim();
        String monto=edt_montop.getText().toString().trim();

        Modelo modelo=new Modelo();
        modelo.setNombre(texto);
        modelo.setMonto(separador(monto));

        mLista.add(modelo);
        edt_ingresopasivo.getText().clear();
        edt_montop.getText().clear();
        mAdapter=new ListAdapter(this,R.layout.item_row, mLista);
        mListview.setAdapter(mAdapter);

        //aquí se agrega el nuevo pasivo a la base de datos
        Pasivo nuevopasivo= new Pasivo();
        nuevopasivo.setCorreo(usuario);
        nuevopasivo.setNombrePasivo(texto);
        nuevopasivo.setMontoInversion(monto);
        nuevopasivo.setGananciaPasivos("0");
        String clave= mDatabase.push().getKey();
        mDatabase.child(clave).setValue(nuevopasivo);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       //Toast.makeText(this,"elemento clickeado: "+position,Toast.LENGTH_LONG).show();
        Intent intent =new Intent(getApplicationContext(),IngresoPasivo.class);
        intent.putExtra("nombre",mAdapter.getItem(position).getNombre());
        intent.putExtra("monto",mAdapter.getItem(position).getMonto());
        intent.putExtra("posicion",String.valueOf(position));
        startActivityForResult(intent,REQUEST_CODE);
      /*   mLista.remove(position);
        mAdapter.notifyDataSetChanged();*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE){
            if (resultCode==RESULT_OK){
                int posicion=Integer.parseInt(data.getStringExtra("posicion"));
                mLista.remove(posicion);
                mAdapter.notifyDataSetChanged();
            }
        }else if (requestCode==2){
            //aquí otra acción
        }
    }
}
