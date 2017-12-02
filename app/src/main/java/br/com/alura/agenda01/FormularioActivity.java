package br.com.alura.agenda01;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.alura.agenda01.dao.AlunoDAO;
import br.com.alura.agenda01.modelo.Aluno;

public class FormularioActivity extends AppCompatActivity {
    //Formulario use case
    private FormularioHelper helper;
    private Button btnSalvar;
    private Button btnExcluir;
    private ImageButton btnImageUpload;
    private String arquivo;

    private Aluno aluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        //Associate layout resources with object vars
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnExcluir = (Button) findViewById(R.id.btnExcluir);
        btnImageUpload = (ImageButton) findViewById(R.id.formulario_botao_foto);


        helper = new FormularioHelper(this);    //Create FormularioHelper

        //Set button upload image event
        btnImageUpload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                obterImagem();                  //Exec use case on image upload
            }
        });

        //Recover Intent Data
        Intent intent = getIntent();
        aluno = (Aluno) intent.getSerializableExtra("aluno");

        //according to check if aluno is null or not, allow some behaviors
        if(aluno != null){      //UPDATE a student

            //Fill each form field
            helper.adicionaImagemPerfil(carregaImagem(aluno.getFoto()));
            helper.preencheFormulario(aluno);

            btnExcluir.setOnClickListener(new View.OnClickListener() {        //Set exclude button event
                @Override
                public void onClick(View v) {
                    //Executar caso de uso excluir aluno
                    excluirAluno();
                }
            });

            btnSalvar.setOnClickListener(new View.OnClickListener() {        //Set save button event - update student
                @Override
                public void onClick(View v) {
                    alterarAluno();                       //Exec update student use case
                }
            });

        }else{                  //CREATE a new student

            btnExcluir.setVisibility(View.INVISIBLE);                       //Hide delete button
            btnSalvar.setOnClickListener(new View.OnClickListener() {       //set save button event - create student
                @Override
                public void onClick(View v) {
                    inserirAluno();                     //Exec add student use case
                }
            });
        }

    }

    //Pick a image from gallery or camera
    private void obterImagem() {
        // Check Camera
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//Open the image gallery
            startActivityForResult(intent, 1);          //Start pick image activity
        } else {
            Toast.makeText(FormularioActivity.this, "Camera não suportada!", Toast.LENGTH_LONG).show();
        }
    }

    //After activity finishes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){       //If activity not fail

            Uri selectedImage = data.getData();     //get Uri image from intent data
            final InputStream imageStream;          //Open inputStream WHY
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);      //WHY
                Bitmap finalImage = BitmapFactory.decodeStream(imageStream);            //Create bitmap

                helper.adicionaImagemPerfil(finalImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    //Save Image on internal smartphone storage
    private String salvarImagem(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageAlunos", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory,"aluno" + System.currentTimeMillis() + ".jpg");

        if (!directory.exists()) {      //Check if directory exists
            directory.mkdir();
        }
        if (!mypath.exists()) {         //check if image file exists
            try {
                mypath.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();        //return absolute image path to user
    }

    private Bitmap carregaImagem(String path) {
        Bitmap b = null;
        try {
            File f=new File(path);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return b;
    }

    public void inserirAluno() {
        Aluno aluno = helper.pegaAluno();
        if (aluno != null) {

            aluno.setFoto(salvarImagem(helper.obterImagemPerfil()));            //Get student profile image

            AlunoDAO alunoDAO = new AlunoDAO(this);
            alunoDAO.insere(aluno);
            alunoDAO.close();

            Toast.makeText(FormularioActivity.this, "Aluno " + aluno.getNome() + " inserido com sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(FormularioActivity.this, "Preencha todos os campos com exclamação", Toast.LENGTH_SHORT).show();
        }
    }

    //Excluir aluno
    public void excluirAluno(){
        Aluno a = helper.pegaAluno();
        AlunoDAO dao = new AlunoDAO(this);
        dao.deleta(a);
        dao.close();

        Toast.makeText(FormularioActivity.this, "Aluno " + a.getNome() + " excluido com sucesso!!!", Toast.LENGTH_SHORT).show();
    }

    //Consultar aluno
    public void alterarAluno(){
        aluno = helper.pegaAluno();

        aluno.setFoto(salvarImagem(helper.obterImagemPerfil()));            //Get student profile image

        AlunoDAO dao = new AlunoDAO(this);
        dao.alterar(aluno);
        dao.close();

        Toast.makeText(FormularioActivity.this, "Aluno " + aluno.getNome() + " alterado com sucesso!!!", Toast.LENGTH_SHORT).show();
    }
}


//UNNECESSARY CODE!!!

// Open default camera
            /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);*/

//Intent intentActivityAluno = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//startActivityForResult(intentActivityAluno , 1);

// start the image capture Intent
//startActivityForResult(intent, 100);

            /*arquivo = Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".jpg";    //Pick a new url
            File file = new File(arquivo);          //Create a new file with "arquivo" url
            Uri outputFileUri = Uri.fromFile(file);     //Get URL object from file "arquivo"
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);        //Add extra to putExtra on intent activity*/


            /*Intent chooserIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(Environment.getExternalStorageDirectory(), "POST_IMAGE.jpg");
            chooserIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            Uri outputFileUri = Uri.fromFile(f);
            startActivityForResult(chooserIntent, 1);*/

/*requestCode == 100 && */




            /*if(data.getSerializableExtra(MediaStore.EXTRA_OUTPUT) != null){             //Check if the Url is null or not
                Uri selectedImage = data.getData();
                getContentResolver().notifyChange(selectedImage, null);
                Bitmap reducedSizeBitmap = getBitmap(data.getData().getPath());
                if(reducedSizeBitmap != null){

                    helper.aluno.setFoto(arquivo);
                    helper.campoImagem.setImageBitmap(carregaImagem());

                    /*ImgPhoto.setImageBitmap(reducedSizeBitmap);
                    Button uploadImageButton = (Button) findViewById(R.id.uploadUserImageButton);
                    uploadImageButton.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(this,"Error while capturing the new Image",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this,"Error while capturing Image",Toast.LENGTH_LONG).show();
            }*/

//if(resultCode != RESULT_CANCELED){



//carregaImagem();

//}



            /*Uri selectedImage = data.getData();

            final InputStream imageStream;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
                final Bitmap finalImage = BitmapFactory.decodeStream(imageStream);
                //helper.preencherImagemPerfil(finalImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/


           /* FileInputStream fis;
            Bitmap bmp = null;
            try {
                fis = new FileInputStream(aluno.getFoto());
                bmp = BitmapFactory.decodeStream(fis);
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(bmp != null){
                bmp = Bitmap.createScaledBitmap(bmp, 50, 50, true);
            }
            return bmp;*/



    /*@Override
      public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_formulario, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_formulario_ok:
                Aluno aluno = helper.pegaAluno();
                AlunoDAO dao = new AlunoDAO(this);
                dao.insere(aluno);
                dao.close();

                Toast.makeText(FormularioActivity.this, "Aluno" + aluno.getNome() + "salvo!", Toast.LENGTH_SHORT).show();

                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }*/


    /*private Bitmap getBitmap(String path) {

        Uri uri = Uri.fromFile(new File(path));
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            in = getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap b = null;
            in = getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Log.d("", "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

            Log.d("", "bitmap size - width: " + b.getWidth() + ", height: " +
                    b.getHeight());
            return b;
        } catch (IOException e) {
            Log.e("", e.getMessage(), e);
            return null;
        }
    }*/


    /*



    private Bitmap carregaImagem() {
        FileInputStream fis;
        Bitmap bmp = null;
        try {
            fis = new FileInputStream(aluno.getFoto());
            bmp = BitmapFactory.decodeStream(fis);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(bmp != null){
            bmp = Bitmap.createScaledBitmap(bmp, 50, 50, true);
        }
        return bmp;
    }

     */