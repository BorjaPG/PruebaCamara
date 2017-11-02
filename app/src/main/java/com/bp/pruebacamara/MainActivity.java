package com.bp.pruebacamara;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private File photoFile = null;
    private ImageView mImageThumbnail;
    private Button mTakePictureBtn;
    private byte[] mFileBytes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageThumbnail = (ImageView) findViewById(R.id.img_thumbnail);
        mTakePictureBtn = (Button) findViewById(R.id.take_picture);

        mTakePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abre la aplicación de fotos del dispositivo.
                takePictureIntent();
            }
        });
    }

    /* Método encargado de abrir la aplicación de fotos del dispositivo. */
    private void takePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //Llama a la aplicación de fotos del dispositivo.
            startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    /* Método que gestiona el resultado de tomar la foto. */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            //Si se valida la foto, se procede a guardar la imagen.
            if (resultCode == RESULT_OK) {
                retrieveCapturedPicture();
            }
        }
    }

    /* Método que recupera la imagen y la almacena. */
    private void retrieveCapturedPicture() {
        //Recupera la imagen y la configura con BitmapFactory.
        BitmapFactory.Options options = new BitmapFactory.Options();
        //Configura la imagen.
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options);
        mImageThumbnail.setImageBitmap(bitmap);

        //Configura el formato.
        try {
            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
            mFileBytes = stream1.toByteArray();
        } catch (OutOfMemoryError e) { //possibility of memoryexception
            e.printStackTrace();
        }

    }
}
