package edd.ecosdedestruccion.crud;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.UnicodeSetSpanner;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Variables para los componentes
    private EditText edtCodigo, edtDescrip, edtPrecio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Relación de componentes con lógica
        edtCodigo = (EditText) findViewById(R.id.txt_codigo);
        edtDescrip = (EditText) findViewById(R.id.txt_descripcion);
        edtPrecio = (EditText) findViewById(R.id.txt_precio);
    }

    /*
    Método para dar de alta los productos
     */
    public void Registrar(View view){
        //Objeto para la base de datos -> administracion
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        //Apertura para lectura y escritura
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

        //Zona de trabajo de datos
        String codigo = edtCodigo.getText().toString();
        String descrip = edtDescrip.getText().toString();
        String precio = edtPrecio.getText().toString();
        //Validación de datos
        if(!codigo.isEmpty() && !descrip.isEmpty() && !precio.isEmpty()){
            //Obtenemos los valores de los campos y los guardamos en la variable -> registro
            ContentValues registro = new ContentValues();
            registro.put("codigo", codigo);
            registro.put("descrip", descrip);
            registro.put("precio", precio);

            //Guardado de los datos en la tabla
            baseDeDatos.insert("articulos", null, registro);
            baseDeDatos.close();

            //limpieza de campos
            edtCodigo.setText("");
            edtDescrip.setText("");
            edtPrecio.setText("");

            Toast.makeText(this, "Registro completo correcto", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    Método de consulta de registro
     */

    public void buscar(View view){
        //Objeto para la base de datos -> administracion
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        //Apertura para lectura y escritura
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

        //Zona de trabajo de datos
        String codigo = edtCodigo.getText().toString();

        //Validación
        if(!codigo.isEmpty()){
            //Búsqueda de la línea - >SELECT
            Cursor fila = baseDeDatos.rawQuery("SELECT descrip, precio FROM articulos WHERE codigo =" + codigo, null);
            //Condiciones para el control de la existencia de la línea
            if(fila.moveToFirst()){
                edtDescrip.setText(fila.getString(0));
                edtPrecio.setText(fila.getString(1));

                baseDeDatos.close();

            }else {
                Toast.makeText(this, "No existe el artículo", Toast.LENGTH_SHORT).show();
                baseDeDatos.close();
            }
        }else{
            Toast.makeText(this, "falta el código de artículo", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    Eliminar registro
     */
    public void eliminar(View view){
        //Objeto para la base de datos -> administracion
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        //Apertura para lectura y escritura
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

        //Zona de trabajo
        String codigo = edtCodigo.getText().toString();
        //Validación
        if(!codigo.isEmpty()){

            //Variable int de control de cantidad de registros a borrar
            int cantidad = baseDeDatos.delete("articulos", "codigo=" + codigo, null);
            baseDeDatos.close();

            edtCodigo.setText("");
            edtDescrip.setText("");
            edtPrecio.setText("");

            //Validación de registro 0 -> no hay registro // 1 -> hay 1 registro
            if(cantidad == 1){
                Toast.makeText(this, "Eliminación correcta", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Artículo no existe", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Debes introducir código", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    Modificar registros
     */
    public void modificar(View view){
        //Objeto para la base de datos -> administracion
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        //Apertura para lectura y escritura
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

        //Zona de trabajo
        String codigo = edtCodigo.getText().toString();
        String descrip = edtDescrip.getText().toString();
        String precio = edtPrecio.getText().toString();

        //validación de campos rellenos
        if(!codigo.isEmpty() && !descrip.isEmpty() &&!precio.isEmpty() ){
            //Objeto para contener los datos
            ContentValues registro = new ContentValues();
            registro.put("codigo", codigo);
            registro.put("descrip", descrip);
            registro.put("precio", precio);

            //Rellenado de la base de datos
            int cantidad = baseDeDatos.update("articulos", registro, "codigo=" + codigo, null);
            baseDeDatos.close();

            // //Validación de registro 0 -> no hay registro // 1 -> hay 1 registro
            if(cantidad == 1){
                Toast.makeText(this, "Modificación realizada", Toast.LENGTH_SHORT).show();

                edtCodigo.setText("");
                edtDescrip.setText("");
                edtPrecio.setText("");
            }else {
                Toast.makeText(this, "Artículo no existe", Toast.LENGTH_SHORT).show();
            }


        } else {
            Toast.makeText(this, "Debes rellenar los campos", Toast.LENGTH_SHORT).show();
        }



    }
}
