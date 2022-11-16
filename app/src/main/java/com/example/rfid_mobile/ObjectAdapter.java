package com.example.rfid_mobile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.text.Normalizer;
import java.util.List;

public class ObjectAdapter extends ArrayAdapter<ObjectClass> {

    private LayoutInflater inflater;
    private int layout;
    private List<ObjectClass> objects;
    private Context context;

    public ObjectAdapter(Context context, int resource, List<ObjectClass> objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView nameView = view.findViewById(R.id.nameObject);
        TextView descView = view.findViewById(R.id.descObject);
        TextView statusView = view.findViewById(R.id.statusObject);
        TextView categoryView = view.findViewById(R.id.categoryObject);
        ImageView categoryImageView = view.findViewById(R.id.imageView);
        Button editObjectButton = view.findViewById(R.id.editObjectButton5);
        Button deleteObjectButton = view.findViewById(R.id.deleteObjectButton3);

        ObjectClass object = objects.get(position);

        nameView.setText(object.name);
        descView.setText(object.description);

        categoryView.setText(object.category);

        if (object.category.equals("music_column")) {
            categoryView.setText("");
            categoryView.setTextColor(Color.RED);
            categoryImageView.setImageResource(R.drawable.music_column);
        }

        if (object.category.equals("headphones")) {
            categoryView.setText("");
            categoryView.setTextColor(Color.RED);
            categoryImageView.setImageResource(R.drawable.headphons);
        }

        if (object.category.equals("microphone")) {
            categoryView.setText("");
            categoryView.setTextColor(Color.RED);
            categoryImageView.setImageResource(R.drawable.microphone);
        }


            if (!object.status) {
            statusView.setText("✔");
            statusView.setTextColor(Color.GREEN);
        } else {
            statusView.setText("╳");
            statusView.setTextColor(Color.RED);
        }

        editObjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(view.getContext(),FormActivity.class);
                myIntent.putExtra("id", object.id);
                myIntent.putExtra("type", "edit");
                view.getContext().startActivity(myIntent);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        deleteObjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("Внимание")
                        .setMessage("Вы точно хотите удалить " + object.name + "?")
                        .setCancelable(true)
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (Logic.deleteObject(object.id)) {
                                    builder.setTitle("Внимание")
                                            .setMessage("Объект успешно удален")
                                            .setCancelable(true)
                                            .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    objects.remove(object);
                                                    notifyDataSetChanged();
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
                                                    notifyDataSetChanged();
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

        return view;
    }
}
