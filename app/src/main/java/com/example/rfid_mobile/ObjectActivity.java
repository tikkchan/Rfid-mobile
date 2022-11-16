package com.example.rfid_mobile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ObjectActivity extends AppCompatActivity {

    ObjectClass object;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getIntent().getExtras();
        String id = arguments.getString("id");
        String parent = arguments.getString("parent");
        this.object = Logic.getObjectById(id);
        if (this.object == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Ошибка")
                    .setMessage("Объект не найден")
                    .setCancelable(true)
                    .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .show();
        } else {
            if (parent.equals("MainActivity")) {
                setContentView(R.layout.object_from_catalog);
            } else {
                setContentView(R.layout.object_from_rfid);
            }
            TextView infoRental = findViewById(R.id.infoRental);
            if (object.status) {
                infoRental.setVisibility(View.VISIBLE);
                RentalClass rental = Logic.infoRental(id);
                infoRental.setText("Информация об аренде:\n" + rental.name + " " + rental.startDate + " - " + rental.endDate);
            } else {
                infoRental.setVisibility(View.GONE);
            }
            TextView name = findViewById(R.id.nameObject);
            name.setText(object.name);
            TextView cat = findViewById(R.id.categoryObject);
            cat.setText(object.category);
            TextView desc = findViewById(R.id.descObject);
            desc.setText(object.description);
            TextView status = findViewById(R.id.statusObject);
            if (!object.status) {
                status.setText("✔ на складе");
                status.setTextColor(Color.GREEN);
            } else {
                status.setText("╳ арендован");
                status.setTextColor(Color.RED);
            }

            Button editObjectButton = findViewById(R.id.editObjectButton5);
            Button deleteObjectButton = findViewById(R.id.deleteObjectButton3);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            editObjectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editObject();
                }
            });
            deleteObjectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.setTitle("Внимание")
                            .setMessage("Вы точно хотите удалить " + name.getText() + "?")
                            .setCancelable(true)
                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (Logic.deleteObject(id)) {
                                        builder.setTitle("Внимание")
                                                .setMessage("Объект успешно удален")
                                                .setCancelable(true)
                                                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        finish();
                                                    }
                                                })
                                                .show();
                                    } else {
                                        builder.setTitle("Ошибка")
                                                .setMessage("Объект не был удален")
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
                            }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })
                            .show();
                }
            });
            if (parent.equals("Logic")) {
                Button rentObjectButton = findViewById(R.id.rentObjectButton);
                if (!object.status) {
                    rentObjectButton.setText("Взять в аренду");
                    rentObjectButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rentObject();
                        }
                    });
                } else {
                    rentObjectButton.setText("Вернуть на склад");
                    rentObjectButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Logic.returnObject(object.id)) {
                                builder.setTitle("Внимание")
                                        .setMessage("Объект успешно возвращен на склад")
                                        .setCancelable(true)
                                        .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                finish();
                                            }
                                        })
                                        .show();
                            } else {
                                builder.setTitle("Ошибка")
                                        .setMessage("Объект не был возвращен на склад")
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
        }
    }
    void editObject() {
        Intent intent = new Intent(this, FormActivity.class);
        intent.putExtra("id", object.id);
        intent.putExtra("type", "edit");
        this.startActivityForResult(intent, 1);
    }

    void rentObject() {
        Intent intent = new Intent(this, FormActivity.class);
        intent.putExtra("id", object.id);
        intent.putExtra("type", "rent");
        this.startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent refreshIntent = getIntent();
        finish();
        startActivity(refreshIntent);
    }
}

