package com.example.appuserssqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //instanciar los ids del archivo xml
    TextView message;
    EditText id, name, value;
    CheckBox check;
    ImageButton save, search, edit, delete;
    dbLibrary tblBook = new dbLibrary(this,"dbLibrary",null,1);
    String idSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Referenciar los objetos instanciados previamente
        message = findViewById(R.id.tvMessage);
        id = findViewById(R.id.etId);
        name = findViewById(R.id.etName);
        value = findViewById(R.id.etvalue);
        check = findViewById(R.id.cCheck);
        save = findViewById(R.id.ibSave);
        search = findViewById(R.id.ibSearch);
        edit = findViewById(R.id.ibEdit);
        delete = findViewById(R.id.ibDelete);
        //Eventos de los botones
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check.isChecked()){
                String mId = id.getText().toString();
                SQLiteDatabase osdbUserW = tblBook.getWritableDatabase();
                osdbUserW.execSQL("delete from book where id = '"+mId+"'");
                id.setText("");
                name.setText("");
                value.setText("");
                check.setChecked(false);
                id.requestFocus();
                message.setTextColor(Color.BLUE);
                message.setText("El usuario fue eliminado correctamente ...");
                }
                else{
                    message.setTextColor(Color.RED);
                    message.setText("No se encuentra el libro");
                }
            }

        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase dbIdw = tblBook.getWritableDatabase();
                String mId = id.getText().toString();
                String mName = name.getText().toString();
                String mValue = value.getText().toString();
                CheckBox checkbox = (CheckBox)findViewById(R.id.cCheck);
                int isChecked = checkbox.isChecked() ? 1 : 0;
                if (!idSearch.equals(mId)) {
                    if (!searchId(mId)){//No lo encuentra
                        String query = "Update book set id = '"+mId+"', name = '"+mName+"', value = '"+mValue+"', checkbox = '"+isChecked+"' where id ='"+idSearch+"'";
                        dbIdw.execSQL(query);
                        message.setTextColor(Color.BLUE);
                        message.setText("Actualizacion correcta");
                    }
                    else {
                        message.setTextColor(Color.RED);
                        message.setText("Id existente... Utiliza otro ID");
                    }
                }
                else{
                    String query = "Update book set name = '"+mName+"', value = '"+mValue+"',checkbox= '"+isChecked+"' where id ='"+idSearch+"'";
                    dbIdw.execSQL(query);
                    message.setTextColor(Color.BLUE);
                    message.setText("Libro actualizado correctamente");

                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sdbTblBook = tblBook.getReadableDatabase();
                String sql = "Select id,name,value,checkbox from book where id ='"+id.getText().toString()+"'";
                Cursor cBook = sdbTblBook.rawQuery(sql,null);
                if(cBook.moveToFirst()){ //Encontro el email
                    idSearch = id.getText().toString();
                    // asignar los datos de la tabla cursor a cada editText
                    id.setText(cBook.getString(0));
                    name.setText(cBook.getString(1));
                    value.setText(cBook.getString(2));
                    check.setChecked(cBook.getInt(3) == 1);
                    message.setText("");
                }
                else{
                    message.setTextColor(Color.RED);
                    message.setText("ID de  ususario no existe . Intenta con otro");
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mId = id.getText().toString();
                String mName = name.getText().toString();
                String mValue = value.getText().toString();
                CheckBox checkbox = (CheckBox)findViewById(R.id.cCheck);
                int isChecked = checkbox.isChecked() ? 1 : 0;
                if (!mId.isEmpty() && !mName.isEmpty() && !mValue.isEmpty()){
                    //Buscar el usuario por email
                    if (!searchId(mId)){
                        //instrucciones para guardar
                        SQLiteDatabase osdbTbl = tblBook.getWritableDatabase();
                        // Generar una tabla temporal a traves de ContentValues
                        ContentValues cvBook = new ContentValues();
                        cvBook.put("id",mId);
                        cvBook.put("name",mName);
                        cvBook.put("value",mValue);
                        cvBook.put("checkbox",isChecked);
                        //Guardr el registro en la tabla fisica
                        osdbTbl.insert("book",null,cvBook);
                        osdbTbl.close();
                        message.setTextColor(Color.BLUE);
                        message.setText("Libro ingresado exitosamente...");
                    }else{
                        message.setTextColor(Color.RED);
                        message.setText("ID EXISTENTE. Intentelo con otro");
                    }

                }else{
                    message.setTextColor(Color.RED);
                    message.setText("Debe ingresar todos los datos");
                }

            }
        });

    }

    private boolean searchId(String mId) {
        //instanciar un objeto de la clase SQLiteDatabas en modo lectura, con base en el objeto SOH
        SQLiteDatabase sdbBook = tblBook.getReadableDatabase();
        String query = "Select id from book where id = '"+mId+"'";
        //Crear tabla cursor (temporal) para almacenar los regs devueltos por SELECT
        Cursor cBook = sdbBook.rawQuery(query,null);
        if(cBook.moveToFirst()){
           return true;
        }
        return false;
    }
}