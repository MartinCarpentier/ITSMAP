package com.example.martin.developercv;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.martin.developercv.Model.DeveloperInfo;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    Context context = this;

    @BindView(R.id.nameText)
    TextView nameText;

    @BindView(R.id.androidText)
    TextView androidText;

    @BindView(R.id.idText)
    TextView idText;

    @BindView(R.id.mitBillede)
    RoundedImageView myPicture;

    private Uri photoUri;

    private int REQUEST_EDIT_ACTIVITY = 12341;
    private int REQUEST_CAMERA = 54231;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            nameText.setText(savedInstanceState.getString(getString(R.string.NameKey)));
            idText.setText(savedInstanceState.getString(getString(R.string.IdKey)));
            androidText.setText(savedInstanceState.getString(getString(R.string.androidKey)));

            String photoUriString = savedInstanceState.getString("BitmapImage");

            if(photoUriString != "NaN")
            {
                photoUri = Uri.parse(photoUriString);
                try {
                    Bitmap myBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);

                    myPicture.setImageBitmap(myBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditCvActivity.class);

                DeveloperInfo info = new DeveloperInfo(nameText.getText().toString(), idText.getText().toString(), androidText.getText().toString());
                intent.putExtra("developerInfo", info);
                startActivityForResult(intent, REQUEST_EDIT_ACTIVITY);
            }
        });

        myPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(getString(R.string.NameKey), nameText.getText().toString());
        outState.putString(getString(R.string.IdKey), idText.getText().toString());
        outState.putString(getString(R.string.androidKey), androidText.getText().toString());

        if(photoUri == null)
        {
            outState.putString("BitmapImage", "NaN");
        }
        else
        {
            outState.putString("BitmapImage", photoUri.toString());
        }
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        if (requestCode == REQUEST_EDIT_ACTIVITY && resultCode == RESULT_OK) {
            DeveloperInfo developerInfo = (DeveloperInfo) resultIntent.getParcelableExtra("developerResult");

            String name = developerInfo.getName();
            String id = developerInfo.getId();
            String android = String.valueOf(developerInfo.isAndroid());

            nameText.setText(name);
            idText.setText(id);
            androidText.setText(android);
        }
        if(requestCode == REQUEST_CAMERA && resultCode == RESULT_OK)
        {
            try {
                Bitmap myBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);

                myPicture.setImageBitmap(myBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
