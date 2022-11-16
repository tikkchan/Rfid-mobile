package com.example.rfid_mobile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class FormActivity extends AppCompatActivity {

    ObjectClass object;
    Calendar dateAndTime=Calendar.getInstance();
    Calendar dateAndTime2=Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getIntent().getExtras();
        String type = arguments.getString("type");
        if (type.equals("edit")){
            setContentView(R.layout.update_form);
            String id = arguments.getString("id");
            this.object = Logic.getObjectById(id);

            TextInputLayout nameView = findViewById(R.id.inputNameBar);
            Spinner spinner = findViewById(R.id.spinner);
            TextInputLayout descView = findViewById(R.id.inputDescBar);

            Objects.requireNonNull(nameView.getEditText()).setText(this.object.name);
            Objects.requireNonNull(descView.getEditText()).setText(this.object.description);


            ArrayList<String> categories = Logic.getCategories();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, categories);
            spinner.setAdapter(adapter);

            for (int i=0; i<categories.size(); i++)
            {
                if (categories.get(i).equals(this.object.category)){
                    spinner.setSelection(i);
                    break;
                }
            }
            Button saveEditButton = findViewById(R.id.saveEditButton);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            saveEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = (Objects.requireNonNull((nameView).getEditText())).getText().toString().trim();
                    String description = (Objects.requireNonNull((descView).getEditText())).getText().toString().trim();
                    boolean status = Objects.requireNonNull(Logic.getObjectById(id)).status;
                    String category = spinner.getSelectedItem().toString();
                    if (name.isEmpty()){
                        builder.setTitle("Некорректный ввод")
                                .setMessage("Название не может быть пустым")
                                .setCancelable(true)
                                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                })
                                .show();
                        return;
                    }
                    if (description.isEmpty()){
                        builder.setTitle("Некорректный ввод")
                                .setMessage("Описание не может быть пустым")
                                .setCancelable(true)
                                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                })
                                .show();
                        return;
                    }
                    if (name.contains("|") || name.contains("&") || description.contains("|") || description.contains("&")){
                        builder.setTitle("Некорректный ввод")
                                .setMessage("Строки содержат запрещенные символы '|', '&'")
                                .setCancelable(true)
                                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                })
                                .show();
                        return;
                    }

                    ObjectClass obj = new ObjectClass(id, name, description, status, category);
                    if (Logic.saveEdit(obj))
                    {
                        builder.setTitle("Внимание")
                                .setMessage("Объект успешно изменен")
                                .setCancelable(true)
                                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                                .show();
                    }else{
                        builder.setTitle("Ошибка")
                                .setMessage("Объект не был изменен")
                                .setCancelable(true)
                                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                                .show();
                    }
                }
            });
        }
        else if (type.equals("rent")) {
            setContentView(R.layout.rent_form);
            String id = arguments.getString("id");
            setInitialDateTime();

            Button rentObjectButton2 = findViewById(R.id.rentObjectButton2);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            rentObjectButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextInputLayout renterName = findViewById(R.id.renterName);
                    Button dateButton = findViewById(R.id.dateButton);
                    Button dateButton2 = findViewById(R.id.dateButton2);
                    String name = (Objects.requireNonNull((renterName).getEditText())).getText().toString().trim();
                    String startDate = dateButton.getText().toString().trim();
                    String endDate = dateButton2.getText().toString().trim();
                    try {
                        Date startDate2 = new SimpleDateFormat("dd.MM.yy").parse(startDate);
                        Date endDate2 = new SimpleDateFormat("dd.MM.yy").parse(endDate);
                        if (startDate2.after(endDate2)){
                            builder.setTitle("Некорректный ввод")
                                    .setMessage("Дата начала не может быть позже даты конца")
                                    .setCancelable(true)
                                    .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    })
                                    .show();
                            return;
                        }
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                    if (name.isEmpty()){
                        builder.setTitle("Некорректный ввод")
                                .setMessage("Название не может быть пустым")
                                .setCancelable(true)
                                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                })
                                .show();
                        return;
                    }
                    if (name.contains("|") || name.contains("&")){
                        builder.setTitle("Некорректный ввод")
                                .setMessage("Строки содержат запрещенные символы '|', '&'")
                                .setCancelable(true)
                                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                })
                                .show();
                        return;
                    }
                    RentalClass rentalObject = new RentalClass(name, startDate, endDate, id);
                    if (Logic.newRental(rentalObject) && Logic.rentalObject(id)) {
                        builder.setTitle("Внимание")
                                .setMessage("Объект успешно арендован")
                                .setCancelable(true)
                                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                                .show();
                    }else{
                        builder.setTitle("Ошибка")
                                .setMessage("Объект не был арендован")
                                .setCancelable(true)
                                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                                .show();
                    }
                }
            });
        }
        else if (type.equals("add")){
            setContentView(R.layout.add_form);
            String id = arguments.getString("id");
            TextInputLayout nameView = findViewById(R.id.inputNameBar);
            Spinner spinner = findViewById(R.id.spinner);
            TextInputLayout descView = findViewById(R.id.inputDescBar);

            ArrayList<String> categories = Logic.getCategories();
            //create an adapter to describe how the items are displayed, adapters are used in several places in android.
            //There are multiple variations of this, but this is the basic variant.
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, categories);
            //set the spinners adapter to the previously created one.
            spinner.setAdapter(adapter);

            Button addObject = findViewById(R.id.addObject);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            addObject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //String name = (Objects.requireNonNull((nameView).getEditText())).getText().toString().trim();
                    String name = (Objects.requireNonNull((nameView).getEditText())).getText().toString().trim();
                    String description = (Objects.requireNonNull((descView).getEditText())).getText().toString().trim();
                    boolean status = true;
                    String category = spinner.getSelectedItem().toString();
                    if (name.isEmpty()){
                        builder.setTitle("Некорректный ввод")
                                .setMessage("Название не может быть пустым")
                                .setCancelable(true)
                                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();

                                    }
                                })
                                .show();
                        return;
                    }
                    if (description.isEmpty()){
                        builder.setTitle("Некорректный ввод")
                                .setMessage("Описание не может быть пустым")
                                .setCancelable(true)
                                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                })
                                .show();
                        return;
                    }

                    if (name.contains("|") || name.contains("&") || description.contains("|") || description.contains("&")){
                        builder.setTitle("Некорректный ввод")
                                .setMessage("Строки содержат запрещенные символы '|', '&'")
                                .setCancelable(true)
                                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                })
                                .show();
                        return;
                    }


                    ObjectClass obj = new ObjectClass(id, name, description, status, category);
                    if (Logic.addObject(obj))
                    {
                        builder.setTitle("Внимание")
                                .setMessage("Объект успешно добавлен")
                                .setCancelable(true)
                                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                                .show();
                    }else{
                        builder.setTitle("Ошибка")
                                .setMessage("Объект не был добавлен")
                                .setCancelable(true)
                                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                                .show();
                    }
                }
            });
        }
    }
    // отображаем диалоговое окно для выбора даты
    public void setDate(View v) {
        new DatePickerDialog(this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }
    public void setDate2(View v) {
        new DatePickerDialog(this, d2,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };
    DatePickerDialog.OnDateSetListener d2=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime2.set(Calendar.YEAR, year);
            dateAndTime2.set(Calendar.MONTH, monthOfYear);
            dateAndTime2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };
    private void setInitialDateTime() {
        Button dateButton = findViewById(R.id.dateButton);
        dateButton.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR));

        Button dateButton2 = findViewById(R.id.dateButton2);
        dateButton2.setText(DateUtils.formatDateTime(this,
                dateAndTime2.getTimeInMillis(),
                DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }
}
